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
 */
public class VehiculoContentProvider extends ContentProvider{
    private static final String TAG = "VehiculoContentProvider";
    private VehiculosSQLite bddHelper;

    private final String CONTENT_PROVIDER_MIME_ALLROWS = "vnd.android.cursor.dir/es.uvigo.esei.bgcastro.tfm.vehiculos";
    private final String CONTENT_PROVIDER_MIME_SINGLE_ROW = "vnd.android.cursor.item/es.uvigo.esei.bgcastro.tfm.vehiculos";

    // Create the constants used to differentiate between the different URI // requests.
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;
    // Populate the UriMatcher object, where a URI ending in
    // ‘elements’ will correspond to a request for all items,
    // and ‘elements/[rowID]’ represents a single row.
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.vehiculos","vehiculos", ALLROWS);
        uriMatcher.addURI("es.uvigo.esei.bgcastro.tfm.vehiculos", "vehiculos/#", SINGLE_ROW);
    }

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


    public static final Uri CONTENT_URI = Uri.parse("content://es.uvigo.esei.bgcastro.tfm.vehiculos/vehiculos");
    @Override
    public boolean onCreate() {
        bddHelper = new VehiculosSQLite(getContext(),VehiculosSQLite.NOMBRE_BBDD,null,VehiculosSQLite.VERSION);

        return bddHelper != null;
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
        long id = bdd.insert(VehiculosSQLite.TABLA_VEHICULOS, nullColumnHack, values);

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
                selection = VehiculosSQLite.COL_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }
        // To return the number of deleted items you must specify a where // clause. To delete all rows and return a value pass in “1”.
        if (selection == null) {
            selection = "1";
        }

        // Perform the deletion.
        int deleteCount = bdd.delete(VehiculosSQLite.TABLA_VEHICULOS, selection, selectionArgs);

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
                String rowID = uri.getPathSegments().get(1);
                selection = VehiculosSQLite.COL_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default: break;
        }

        // Perform the update.
        int updateCount = db.update(VehiculosSQLite.TABLA_VEHICULOS, values, selection, selectionArgs);
        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}

