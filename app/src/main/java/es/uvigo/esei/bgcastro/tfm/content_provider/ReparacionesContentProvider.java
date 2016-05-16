package es.uvigo.esei.bgcastro.tfm.content_provider;

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

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class ReparacionesContentProvider extends ContentProvider {

    private static final String TAG = "ReparacionesContProv";
    private VehiculosSQLite bddHelper;

    private final String CONTENT_PROVIDER_MIME_ALLROWS = "vnd.android.cursor.dir/es.uvigo.esei.bgcastro.tfm.reparaciones";
    private final String CONTENT_PROVIDER_MIME_SINGLE_ROW = "vnd.android.cursor.item/es.uvigo.esei.bgcastro.tfm.reparaciones";

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.reparaciones","reparaciones", ALLROWS);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.reparaciones", "reparaciones/#", SINGLE_ROW);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://es.uvigo.esei.bgcastro.tfm.reparaciones/reparaciones");

    public static final String ID_REPARACION = "_id";
    public static final String NOMBRE_REPARACION = "nombre_reparacion";
    public static final String DESCRIPCION_REPARACION = "descripcion_reparacion";
    public static final String REFERENCIA = "referencia";
    public static final String PRECIO = "precio";
    public static final String ID_MANTENIMIENTO_REPARACION = "id_mantenimiento";

    @Override
    public boolean onCreate() {
        bddHelper = new VehiculosSQLite(getContext(),VehiculosSQLite.NOMBRE_BBDD,null,VehiculosSQLite.VERSION);

        if (bddHelper == null) {
            return false;
        }else {
            return true;
        }
    }

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
                queryBuilder.appendWhere(VehiculosSQLite.COL_ID_REPARACION + "=" + rowID);

                break;

            default: break;
        }

        queryBuilder.setTables(VehiculosSQLite.TABLA_REPARACIONES);
        String groupBy = null;
        String having = null;
        Cursor cursor = queryBuilder.query(bdd, projection, selection, selectionArgs, groupBy, having, sortOrder);

        Log.d(TAG, "query: cursor count " + cursor.getCount());

        return cursor;
    }

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

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Open a read / write database to support the transaction.
        SQLiteDatabase bdd = bddHelper.getWritableDatabase();

        // To add empty rows to your database by passing in an empty
        // Content Values object you must use the null column hack
        // parameter to specify the name of the column that can be set to null.
        String nullColumnHack = null;

        // Insert the values into the table
        long id = bdd.insert(VehiculosSQLite.TABLA_REPARACIONES, nullColumnHack, values);

        Log.d(TAG, "insert: "+ id);

        // Construct and return the URI of the newly inserted row.
        if (id > -1) {
            // Construct and return the URI of the newly inserted row.
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

            // Notify any observers of the change in the data set.
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        } else
            return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase bdd = bddHelper.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = VehiculosSQLite.COL_ID_REPARACION + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }
        // To return the number of deleted items you must specify a where // clause. To delete all rows and return a value pass in “1”.
        if (selection == null) {
            selection = "1";
        }

        // Perform the deletion.
        int deleteCount = bdd.delete(VehiculosSQLite.TABLA_REPARACIONES, selection, selectionArgs);

        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the number of deleted items.
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = bddHelper.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1); selection = VehiculosSQLite.COL_ID_REPARACION + "=" + rowID
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default: break;
        }

        // Perform the update.
        int updateCount = db.update(VehiculosSQLite.TABLA_REPARACIONES, values, selection, selectionArgs);
        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
