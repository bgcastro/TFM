package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 31/12/15.
 */
public class VehiculoDAO extends VehiculoBD{
    private static final String TAG = "VehiculoBDD";

    //Campos
    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_IMAGEN_VEHICULO = 1;
    private static final int NUM_COL_MARCA = 2;
    private static final int NUM_COL_MODELO = 3;
    private static final int NUM_COL_MATRICULA = 4;
    private static final int NUM_COL_KILOMETRAJE = 5;
    private static final int NUM_COL_COMBUSTIBLE = 6;
    private static final int NUM_COL_CILINDRADA = 7;
    private static final int NUM_COL_POTENCIA = 8;
    private static final int NUM_COL_COLOR = 9;
    private static final int NUM_COL_ANHO = 10;
    private static final int NUM_COL_ESTADO = 11;


/*    private static String cols[] = { COL_ID,
            COL_IMAGEN_VEHICULO,
            COL_MARCA, COL_MODELO,
            COL_MATRICULA,
            COL_KILOMETRAJE,
            COL_COMBUSTIBLE,
            COL_CILINDRADA,
            COL_POTENCIA,
            COL_COLOR,
            COL_ANHO,
            COL_ESTADO };*/

    public VehiculoDAO(Context context) {
        super(context);
    }


    public long insertVehiculo(Vehiculo vehiculo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(VehiculosSQLite.COL_IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
        contentValues.put(VehiculosSQLite.COL_MARCA, vehiculo.getMarca());
        contentValues.put(VehiculosSQLite.COL_MODELO, vehiculo.getModelo());
        contentValues.put(VehiculosSQLite.COL_MATRICULA, vehiculo.getMatricula());
        contentValues.put(VehiculosSQLite.COL_KILOMETRAJE, vehiculo.getKilometraje());
        contentValues.put(VehiculosSQLite.COL_COMBUSTIBLE, vehiculo.getCombustible());
        contentValues.put(VehiculosSQLite.COL_CILINDRADA, vehiculo.getCilindrada());
        contentValues.put(VehiculosSQLite.COL_POTENCIA, vehiculo.getPotencia());
        contentValues.put(VehiculosSQLite.COL_COLOR, vehiculo.getColor());
        contentValues.put(VehiculosSQLite.COL_ANHO, vehiculo.getAño());
        contentValues.put(VehiculosSQLite.COL_ESTADO, vehiculo.getEstado());

        return bbdd.insert(VehiculosSQLite.TABLA_VEHICULOS, null, contentValues);

    }

    public int updateVehiculo(int id, Vehiculo vehiculo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(VehiculosSQLite.COL_IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
        contentValues.put(VehiculosSQLite.COL_MARCA, vehiculo.getMarca());
        contentValues.put(VehiculosSQLite.COL_MODELO, vehiculo.getModelo());
        contentValues.put(VehiculosSQLite.COL_MATRICULA, vehiculo.getMatricula());
        contentValues.put(VehiculosSQLite.COL_KILOMETRAJE, vehiculo.getKilometraje());
        contentValues.put(VehiculosSQLite.COL_COMBUSTIBLE, vehiculo.getCombustible());
        contentValues.put(VehiculosSQLite.COL_CILINDRADA, vehiculo.getCilindrada());
        contentValues.put(VehiculosSQLite.COL_POTENCIA, vehiculo.getPotencia());
        contentValues.put(VehiculosSQLite.COL_COLOR, vehiculo.getColor());
        contentValues.put(VehiculosSQLite.COL_ANHO, vehiculo.getAño());
        contentValues.put(VehiculosSQLite.COL_ESTADO, vehiculo.getEstado());

        return bbdd.update(VehiculosSQLite.TABLA_VEHICULOS, contentValues, VehiculosSQLite.COL_ID + "=" + id, null);
    }

    public int removeVehiculo(int id){
        return bbdd.delete(VehiculosSQLite.TABLA_VEHICULOS, VehiculosSQLite.COL_ID + "=" + id, null);
    }

    public Vehiculo getVehiculo(int id){
        Cursor cursor = bbdd.query(VehiculosSQLite.TABLA_VEHICULOS, null, VehiculosSQLite.COL_ID + "=" + id, null, null, null, VehiculosSQLite.COL_ID);

        return cursorToVehiculo(cursor);
    }

    public ArrayList<Vehiculo> getAllVehiculos(){
        Cursor cursor = bbdd.query(VehiculosSQLite.TABLA_VEHICULOS, null, null, null, null, null, null);

        return cursorToArrayList(cursor);
    }

    protected static Vehiculo cursorToVehiculo(Cursor cursor) {
        Vehiculo vehiculo;

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return null;

        }else {
             vehiculo = new  Vehiculo(cursor.getInt(NUM_COL_ID),
                     cursor.getBlob(NUM_COL_IMAGEN_VEHICULO),
                    cursor.getString(NUM_COL_MARCA),
                    cursor.getString(NUM_COL_MODELO),
                    cursor.getString(NUM_COL_MATRICULA),
                    cursor.getFloat(NUM_COL_KILOMETRAJE),
                    cursor.getString(NUM_COL_COMBUSTIBLE),
                    cursor.getInt(NUM_COL_CILINDRADA),
                    cursor.getFloat(NUM_COL_POTENCIA),
                    cursor.getInt(NUM_COL_COLOR),
                    cursor.getInt(NUM_COL_ANHO),
                    cursor.getString(NUM_COL_ESTADO)
                    );
            cursor.close();

            return vehiculo;
        }
    }

    public static ArrayList<Vehiculo> cursorToArrayList(Cursor cursor){
        ArrayList<Vehiculo> arrayList;

        if (cursor.getCount() == 0){
            cursor.close();
            return null;
        }else{
            arrayList = new ArrayList<Vehiculo>();
            Vehiculo vehiculo;

            while (cursor.moveToNext()){
                vehiculo = new  Vehiculo(cursor.getInt(NUM_COL_ID),
                        cursor.getBlob(NUM_COL_IMAGEN_VEHICULO),
                        cursor.getString(NUM_COL_MARCA),
                        cursor.getString(NUM_COL_MODELO),
                        cursor.getString(NUM_COL_MATRICULA),
                        cursor.getFloat(NUM_COL_KILOMETRAJE),
                        cursor.getString(NUM_COL_COMBUSTIBLE),
                        cursor.getInt(NUM_COL_CILINDRADA),
                        cursor.getFloat(NUM_COL_POTENCIA),
                        cursor.getInt(NUM_COL_COLOR),
                        cursor.getInt(NUM_COL_ANHO),
                        cursor.getString(NUM_COL_ESTADO)
                );

                arrayList.add(vehiculo);
            }

            cursor.close();
            return arrayList;
        }
    }

}
