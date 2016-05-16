package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by braisgallegocastro on 31/12/15.
 */
public class VehiculosSQLite extends SQLiteOpenHelper {
    private static final String TAG = "VehiculosSQLite";

    //version de la BD
    public static int VERSION = 1;

    //Nombre BD
    public static final String NOMBRE_BBDD = "vehiculos.db";

    //Nombres de la tablas
    public static final String TABLA_VEHICULOS = "tabla_vehiculos";
    public static final String TABLA_MANTENIMIENTOS = "tabla_mantenimientos";
    public static final String TABLA_REPARACIONES = "tabla_reparaciones";

    //Campos tabla vehiculos
    public static final String COL_ID = "_id";
    public static final String COL_IMAGEN_VEHICULO = "imagen_vehiculo";
    public static final String COL_MARCA = "marca";
    public static final String COL_MODELO = "modelo";
    public static final String COL_MATRICULA = "matricula";
    public static final String COL_KILOMETRAJE = "kilometraje";
    public static final String COL_COMBUSTIBLE = "combustible";
    public static final String COL_CILINDRADA = "cilindrada";
    public static final String COL_POTENCIA = "potencia";
    public static final String COL_COLOR = "color";
    public static final String COL_ANHO = "anho";
    public static final String COL_ESTADO = "estado";

    //Campos tabla mantenimientos
    public static final String COL_ID_MANTENIMIENTO = "_id";
    public static final String COL_ID_VEHICULO = "id_vehiculo";
    public static final String COL_ESTADO_REPARACION = "estado";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_KILOMETRAJE_REPARACION = "kilometraje_reparacion";
    public static final String COL_FECHA = "reparacion";

    //Campos tabla reparaciones
    public static final String COL_ID_REPARACION = "_id";
    public static final String COL_NOMBRE_REPARACION = "nombre_reparacion";
    public static final String COL_DESCRIPCION_REPARACION = "descripcion_reparacion";
    public static final String COL_REFERENCIA = "referencia";
    public static final String COL_PRECIO = "precio";
    public static final String COL_ID_MANTENIMIENTO_REPARACION = "id_mantenimiento";


    //SQL de creacion tabla vehiculos
    private static final String CREATE_BDD = " CREATE TABLE " + TABLA_VEHICULOS + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COL_IMAGEN_VEHICULO + " BLOB, " +
            COL_MARCA + " TEXT NOT NULL, " +
            COL_MODELO + " TEXT NOT NULL, " +
            COL_MATRICULA + " TEXT NOT NULL, " +
            COL_KILOMETRAJE + " REAL, " +
            COL_COMBUSTIBLE + " TEXT, " +
            COL_CILINDRADA + " INTEGER, " +
            COL_POTENCIA + " REAL, " +
            COL_COLOR + " TEXT, " +
            COL_ANHO + " INTEGER, " +
            COL_ESTADO + " TEXT" +
            ")";

    //SQL de creacion tabla mantenimientos
    private static final String CREATE_TABLE_MANTENIMIENTOS = " CREATE TABLE " + TABLA_MANTENIMIENTOS + " (" +
            COL_ID_MANTENIMIENTO + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COL_ID_VEHICULO + " INTEGER, " +
            COL_ESTADO_REPARACION + " TEXT, " +
            COL_NOMBRE + " TEXT, " +
            COL_DESCRIPCION + " TEXT, " +
            COL_KILOMETRAJE_REPARACION + " REAL, " +
            COL_FECHA + " DATE, " +
            " FOREIGN KEY (" + COL_ID_VEHICULO + ") REFERENCES " +
            TABLA_VEHICULOS + "(" + COL_ID + ") " +
            "ON DELETE CASCADE" + ")";

    //SQL de creacion tabla reparaciones
    private static final String CREATE_TABLE_REPARACIONES = " CREATE TABLE " + TABLA_REPARACIONES + " (" +
            COL_ID_REPARACION + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COL_NOMBRE_REPARACION + " TEXT, " +
            COL_DESCRIPCION_REPARACION + " TEXT, " +
            COL_REFERENCIA + " TEXT, " +
            COL_PRECIO + " REAL, " +
            COL_ID_MANTENIMIENTO_REPARACION + " INTEGER, " +
            " FOREIGN KEY (" + COL_ID_MANTENIMIENTO_REPARACION + ") REFERENCES " +
            TABLA_MANTENIMIENTOS + "(" + COL_ID_MANTENIMIENTO + ") " +
            "ON DELETE CASCADE" + ")";

    public VehiculosSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
        Log.d(TAG, "onCreate: CREATE_BDD" + CREATE_BDD);

        db.execSQL(CREATE_TABLE_MANTENIMIENTOS);
        Log.d(TAG, "onCreate: CREATE_TABLE_MANTENIMIENTOS" + CREATE_TABLE_MANTENIMIENTOS);

        db.execSQL(CREATE_TABLE_REPARACIONES);
        Log.d(TAG, "onCreate: CREATE_TABLE_REPARACIONES" + CREATE_TABLE_REPARACIONES);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP" + TABLA_VEHICULOS);
        VERSION = newVersion;
        onCreate(db);
    }
}