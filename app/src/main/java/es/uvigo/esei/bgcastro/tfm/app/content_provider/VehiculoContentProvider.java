package es.uvigo.esei.bgcastro.tfm.app.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;

/**
 * Created by braisgallegocastro on 11/4/16.
 * Content provider para los vehiculos
 */
public class VehiculoContentProvider extends ContentProvider{
    //Campos
    public static final String ID = "_id";
    public static final String IMAGEN_VEHICULO = "imagen_vehiculo";
    public static final String MARCA = "marca";
    public static final String MODELO = "modelo";
    public static final String MATRICULA = "matricula";
    public static final String KILOMETRAJE = "kilometraje";
    public static final String COMBUSTIBLE = "combustible";
    public static final String CILINDRADA = "cilindrada";
    public static final String POTENCIA = "potencia";
    public static final String COLOR = "color";
    public static final String ANHO = "anho";
    public static final String ESTADO = "estado";
    //URI de acceso
    public static final Uri CONTENT_URI = Uri.parse("content://es.uvigo.esei.bgcastro.tfm.vehiculos/vehiculos");
    private static final String TAG = "VehiculoContentProvider";
    // Constantes para diferenciar las URI
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    // Constantes para diferenciar las URI
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.vehiculos", "vehiculos", ALLROWS);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.vehiculos", "vehiculos/#", SINGLE_ROW);
    }

    //URIs
    private final String CONTENT_PROVIDER_MIME_ALLROWS = "vnd.android.cursor.dir/es.uvigo.esei.bgcastro.tfm.vehiculos";
    private final String CONTENT_PROVIDER_MIME_SINGLE_ROW = "vnd.android.cursor.item/es.uvigo.esei.bgcastro.tfm.vehiculos";
    private VehiculosSQLite bddHelper;

    /**
     * Metodo de inicializacion
     *
     * @return True si exito
     */
    @Override
    public boolean onCreate() {
        bddHelper = new VehiculosSQLite(getContext(),VehiculosSQLite.NOMBRE_BBDD,null,VehiculosSQLite.VERSION);

        return bddHelper != null;
    }

    /**
     * Metodo de busqueda
     * @param uri URI
     * @param projection Proyeccion
     * @param selection Campos de selecion
     * @param selectionArgs Argumentos de seleccion
     * @param sortOrder Orden
     * @return Cursor con resultados
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase bdd;

        try {
            bdd = bddHelper.getWritableDatabase();
        }catch (SQLiteException e){
            bdd = bddHelper.getReadableDatabase();
        }

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(VehiculosSQLite.COL_ID + "=" + rowID);

                break;

            default: break;
        }

        queryBuilder.setTables(VehiculosSQLite.TABLA_VEHICULOS);
        String groupBy = null;
        String having = null;
        Cursor cursor = queryBuilder.query(bdd, projection, selection, selectionArgs, groupBy, having, sortOrder);

        Log.d(TAG, "query: cursor count " + cursor.getCount());

        return cursor;
    }

    /**
     * Metodo que devuelve si es consulta unica o de todos los elementos
     * @param uri Uri
     * @return Tipo
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                return CONTENT_PROVIDER_MIME_ALLROWS;
            case SINGLE_ROW:
                return CONTENT_PROVIDER_MIME_SINGLE_ROW;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.ilegalArgument));
        }
    }

    /**
     * Metodo que devuelve si es consulta unica o de todos los elementos
     * @param uri Uri
     * @return Tipo
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase bdd = bddHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id = bdd.insert(VehiculosSQLite.TABLA_VEHICULOS, nullColumnHack, values);

        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        } else
            return null;
    }

    /**
     * Metodo para borrar elementos
     * @param uri URI
     * @param selection Campos de seleccion
     * @param selectionArgs Argumentos de seleccion
     * @return Numero de borrados
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase bdd = bddHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = VehiculosSQLite.COL_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }
        if (selection == null) {
            selection = "1";
        }

        int deleteCount = bdd.delete(VehiculosSQLite.TABLA_VEHICULOS, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    /**
     * Metodo para actualizar elementos
     * @param uri URI
     * @param values Valores nuevos
     * @param selection Campos de seleccion
     * @param selectionArgs Argumentos
     * @return Numero de actualizaciones
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = bddHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = VehiculosSQLite.COL_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default: break;
        }

        int updateCount = db.update(VehiculosSQLite.TABLA_VEHICULOS, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}

