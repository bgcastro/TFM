package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by braisgallegocastro on 31/12/15.
 */
public class VehiculosSQLite extends SQLiteOpenHelper {

    //Nombre de la tabla
    private static final String TABLA_VEHICULOS = "tabla_vehiculos";

    //Campos
    private static final String COL_ID = "ID";
    private static final String COL_IMAGEN_VEHICULO = "IMAGEN_VEHICULO";
    private static final String COL_MARCA = "MARCA";
    private static final String COL_MODELO = "MODELO";
    private static final String COL_MATRICULA = "MATRICULA";
    private static final String COL_KILOMETRAJE = "KILOMETRAJE";
    private static final String COL_COMBUSTIBLE = "COMBUSTIBLE";
    private static final String COL_CILINDRADA = "CILINDRADA";
    private static final String COL_POTENCIA = "POTENCIA";
    private static final String COL_COLOR = "COLOR";
    private static final String COL_ANHO = "ANHO";
    private static final String COL_ESTADO = "ESTADO";

    //SQL de creacion
    private static final String CREATE_BDD = " CREATE TABLE " + TABLA_VEHICULOS + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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


    public VehiculosSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP" + TABLA_VEHICULOS);
        onCreate(db);
    }
}
