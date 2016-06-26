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
 * Created by braisgallegocastro on 21/4/16.
 * Content provider para los mantenimientos
 */
public class MantenimientosContentProvider extends ContentProvider {

    //URI de acceso
    public static final Uri CONTENT_URI = Uri.parse("content://es.uvigo.esei.bgcastro.tfm.mantenimientos/matenimientos");
    //Campos
    public static final String ID_MANTENIMIENTO = "_id";
    public static final String ID_VEHICULO = "id_vehiculo";
    public static final String ESTADO_REPARACION = "estado";
    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String KILOMETRAJE_REPARACION = "kilometraje_reparacion";
    public static final String FECHA = "reparacion";
    private static final String TAG = "MantenContProvider";
    // Constantes para diferenciar las URI
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    // URImatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.mantenimientos","matenimientos", ALLROWS);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.mantenimientos", "matenimientos/#", SINGLE_ROW);
    }

    //URIs
    private final String CONTENT_PROVIDER_MIME_ALLROWS = "vnd.android.cursor.dir/es.uvigo.esei.bgcastro.tfm.matenimientos";
    private final String CONTENT_PROVIDER_MIME_SINGLE_ROW = "vnd.android.cursor.item/es.uvigo.esei.bgcastro.tfm.matenimientos";
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
                queryBuilder.appendWhere(VehiculosSQLite.COL_ID_MANTENIMIENTO + "=" + rowID);

                break;

            default: break;
        }

        queryBuilder.setTables(VehiculosSQLite.TABLA_MANTENIMIENTOS);
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
     * Metodo para insertar elementos
     * @param uri URI
     * @param values Valores a insertar
     * @return ID de BD
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase bdd = bddHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id = bdd.insert(VehiculosSQLite.TABLA_MANTENIMIENTOS, nullColumnHack, values);

        Log.d(TAG, "insert: "+ id);

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
                selection = VehiculosSQLite.COL_ID_MANTENIMIENTO + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }

        if (selection == null) {
            selection = "1";
        }

        int deleteCount = bdd.delete(VehiculosSQLite.TABLA_MANTENIMIENTOS, selection, selectionArgs);

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
                String rowID = uri.getPathSegments().get(1); selection = VehiculosSQLite.COL_ID_MANTENIMIENTO + "=" + rowID
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default: break;
        }

        int updateCount = db.update(VehiculosSQLite.TABLA_MANTENIMIENTOS, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
