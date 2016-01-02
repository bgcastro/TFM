package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

/**
 * Created by braisgallegocastro on 31/12/15.
 */
public class VehiculoBDD {
    private static final int VERSION = 1;
    //Nombre de la tabla
    private static final String TABLA_VEHICULOS = "tabla_vehiculos";
    private static final String NOMBRE_BBDD = "vehiculos.db";

    //Campos
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_IMAGEN_VEHICULO = "IMAGEN_VEHICULO";
    private static final int NUM_COL_IMAGEN_VEHICULO = 1;
    private static final String COL_MARCA = "MARCA";
    private static final int NUM_COL_MARCA = 2;
    private static final String COL_MODELO = "MODELO";
    private static final int NUM_COL_MODELO = 3;
    private static final String COL_MATRICULA = "MATRICULA";
    private static final int NUM_COL_MATRICULA = 4;
    private static final String COL_KILOMETRAJE = "KILOMETRAJE";
    private static final int NUM_COL_KILOMETRAJE = 5;
    private static final String COL_COMBUSTIBLE = "COMBUSTIBLE";
    private static final int NUM_COL_COMBUSTIBLE = 6;
    private static final String COL_CILINDRADA = "CILINDRADA";
    private static final int NUM_COL_CILINDRADA = 7;
    private static final String COL_POTENCIA = "POTENCIA";
    private static final int NUM_COL_POTENCIA = 8;
    private static final String COL_COLOR = "COLOR";
    private static final int NUM_COL_COLOR = 9;
    private static final String COL_ANHO = "ANHO";
    private static final int NUM_COL_ANHO = 10;
    private static final String COL_ESTADO = "ESTADO";
    private static final int NUM_COL_ESTADO = 11;

   private static String cols[] = { COL_ID,
            COL_IMAGEN_VEHICULO,
            COL_MARCA, COL_MODELO,
            COL_MATRICULA,
            COL_KILOMETRAJE,
            COL_COMBUSTIBLE,
            COL_CILINDRADA,
            COL_POTENCIA,
            COL_COLOR,
            COL_ANHO,
            COL_ESTADO };

    //BBDD
    private SQLiteDatabase bbdd;
    private VehiculosSQLite vehiculosSQLite;

    public VehiculoBDD(Context context) {
        this.vehiculosSQLite = new VehiculosSQLite(context,NOMBRE_BBDD, null, VERSION);
    }

    public void openForReading(){
        bbdd = vehiculosSQLite.getReadableDatabase();
    }

    public void openForWriting(){
        bbdd = vehiculosSQLite.getWritableDatabase();
    }

    public void close(){
        bbdd.close();
    }

    public long insertVehiculo(Vehiculo vehiculo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
        contentValues.put(COL_MARCA, vehiculo.getMarca());
        contentValues.put(COL_MODELO, vehiculo.getModelo());
        contentValues.put(COL_MATRICULA, vehiculo.getMatricula());
        contentValues.put(COL_KILOMETRAJE, vehiculo.getKilometraje());
        contentValues.put(COL_COMBUSTIBLE, vehiculo.getCombustible());
        contentValues.put(COL_CILINDRADA, vehiculo.getCilindrada());
        contentValues.put(COL_POTENCIA, vehiculo.getPotencia());
        contentValues.put(COL_COLOR, vehiculo.getColor());
        contentValues.put(COL_ANHO, vehiculo.getAño().toString());
        contentValues.put(COL_ESTADO, vehiculo.getEstado());

        return bbdd.insert(TABLA_VEHICULOS, null, contentValues);

    }

    public int updateVehiculo(int id, Vehiculo vehiculo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
        contentValues.put(COL_MARCA, vehiculo.getMarca());
        contentValues.put(COL_MODELO, vehiculo.getModelo());
        contentValues.put(COL_MATRICULA, vehiculo.getMatricula());
        contentValues.put(COL_KILOMETRAJE, vehiculo.getKilometraje());
        contentValues.put(COL_COMBUSTIBLE, vehiculo.getCombustible());
        contentValues.put(COL_CILINDRADA, vehiculo.getCilindrada());
        contentValues.put(COL_POTENCIA, vehiculo.getPotencia());
        contentValues.put(COL_COLOR, vehiculo.getColor());
        contentValues.put(COL_ANHO, vehiculo.getAño().toString());
        contentValues.put(COL_ESTADO, vehiculo.getEstado());

        return bbdd.update(TABLA_VEHICULOS, contentValues, COL_ID + "=" + id, null);
    }

    public int removeVehiculo(int id){
        return bbdd.delete(TABLA_VEHICULOS, COL_ID + "=" + id, null);
    }

    public Vehiculo getVehiculo(int id){
        Cursor cursor = bbdd.query(TABLA_VEHICULOS, cols, COL_ID + "=" + id, null, null, null, COL_ID);

        return cursorToVehiculo(cursor);
    }

    public ArrayList<Vehiculo> getAllVehiculos(){
        Cursor cursor = bbdd.query(TABLA_VEHICULOS, cols, null, null, null, null, null);

        return cursorToArrayList(cursor);
    }

    private Vehiculo cursorToVehiculo(Cursor cursor) {
        Vehiculo vehiculo;

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return null;

        }else {
             vehiculo = new  Vehiculo(cursor.getBlob(NUM_COL_IMAGEN_VEHICULO),
                    cursor.getString(NUM_COL_MARCA),
                    cursor.getString(NUM_COL_MODELO),
                    cursor.getString(NUM_COL_MATRICULA),
                    cursor.getFloat(NUM_COL_KILOMETRAJE),
                    cursor.getString(NUM_COL_COMBUSTIBLE),
                    cursor.getInt(NUM_COL_CILINDRADA),
                    cursor.getFloat(NUM_COL_POTENCIA),
                    cursor.getString(NUM_COL_COLOR),
                    new Date(cursor.getLong(NUM_COL_ANHO)),
                    cursor.getString(NUM_COL_ESTADO)
                    );
            cursor.close();

            return vehiculo;
        }
    }

    private ArrayList<Vehiculo> cursorToArrayList(Cursor cursor){
        ArrayList<Vehiculo> arrayList;

        if (cursor.getCount() == 0){
            cursor.close();
            return null;
        }else{
            arrayList = new ArrayList<Vehiculo>();
            Vehiculo vehiculo;

            while (cursor.moveToNext()){
                vehiculo = new  Vehiculo(cursor.getBlob(NUM_COL_IMAGEN_VEHICULO),
                        cursor.getString(NUM_COL_MARCA),
                        cursor.getString(NUM_COL_MODELO),
                        cursor.getString(NUM_COL_MATRICULA),
                        cursor.getFloat(NUM_COL_KILOMETRAJE),
                        cursor.getString(NUM_COL_COMBUSTIBLE),
                        cursor.getInt(NUM_COL_CILINDRADA),
                        cursor.getFloat(NUM_COL_POTENCIA),
                        cursor.getString(NUM_COL_COLOR),
                        new Date(cursor.getLong(NUM_COL_ANHO)),
                        cursor.getString(NUM_COL_ESTADO)
                );

                arrayList.add(vehiculo);
            }

            cursor.close();
            return arrayList;
        }
    }

}
