package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by braisgallegocastro on 22/2/16.
 */
public class VehiculoBD{
    private static final int VERSION = 1;
    private static final String NOMBRE_BBDD = "vehiculos.db";

    //BBDD
    protected SQLiteDatabase bbdd;
    private VehiculosSQLite vehiculosSQLite;
    private Context context;

    public VehiculoBD(Context context) {
        this.context = context;
        this.vehiculosSQLite = new VehiculosSQLite(context,NOMBRE_BBDD, null, VERSION);
    }

    public void openForReading() throws SQLiteException {
        bbdd = vehiculosSQLite.getReadableDatabase();
    }

    public void openForWriting() throws SQLiteException{
        bbdd = vehiculosSQLite.getWritableDatabase();
    }

    public void close(){
        bbdd.close();
    }
}
