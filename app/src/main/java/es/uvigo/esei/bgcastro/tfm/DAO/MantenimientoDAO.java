package es.uvigo.esei.bgcastro.tfm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 22/2/16.
 */
public class MantenimientoDAO extends VehiculoBD {
    private static final String TAG = "MantenimientoDAO";

    //Mantenimiento
    private static final int NUM_COL_ID_MANTENIMIENTO = 0;
    private static final int NUM_COL_ID_VEHICULO = 1;
    private static final int NUM_COL_ESTADO_REPACION = 2;
    private static final int NUM_COL_NOMBRE = 3;
    private static final int NUM_COL_DESCRIPCION = 4;
    private static final int NUM_COL_KILOMETRAJE_REPARACION = 5;
    private static final int NUM_COL_FECHA = 6;
    private static final int NUM_COL_ESTADO_SINCRONIZACION = 7;

    //vehiculo
    private static final int NUM_COL_ID = 8;
    private static final int NUM_COL_IMAGEN_VEHICULO = 9;
    private static final int NUM_COL_MARCA = 10;
    private static final int NUM_COL_MODELO = 11;
    private static final int NUM_COL_MATRICULA = 12;
    private static final int NUM_COL_KILOMETRAJE = 13;
    private static final int NUM_COL_COMBUSTIBLE = 14;
    private static final int NUM_COL_CILINDRADA = 15;
    private static final int NUM_COL_POTENCIA = 16;
    private static final int NUM_COL_COLOR = 17;
    private static final int NUM_COL_ANHO = 18;
    private static final int NUM_COL_ESTADO = 19;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static String query = "SELECT * " +
            "FROM" + VehiculosSQLite.TABLA_MANTENIMIENTOS + " mant," + VehiculosSQLite.TABLA_VEHICULOS + "veh" +
            "WHERE" + "mant."+ VehiculosSQLite.COL_ID_VEHICULO + "=" + "veh" + VehiculosSQLite.COL_ID;


    public MantenimientoDAO(Context context) {
        super(context);
    }

    public long insertMantenimiento(Mantenimiento mantenimiento){
        ContentValues contentValues = new ContentValues();
        contentValues.put(VehiculosSQLite.COL_ID_VEHICULO, mantenimiento.getVehiculo().getId());
        contentValues.put(VehiculosSQLite.COL_ESTADO_REPARACION, mantenimiento.getEstado());
        contentValues.put(VehiculosSQLite.COL_NOMBRE, mantenimiento.getNombre());
        contentValues.put(VehiculosSQLite.COL_DESCRIPCION, mantenimiento.getDescripcion());
        contentValues.put(VehiculosSQLite.COL_KILOMETRAJE_REPARACION, mantenimiento.getKilometrajeReparacion());
        contentValues.put(VehiculosSQLite.COL_FECHA, simpleDateFormat.format(mantenimiento.getFecha()));

        return bbdd.insert(VehiculosSQLite.TABLA_MANTENIMIENTOS,null,contentValues);
    }

    public int removeMantenimientos(int id){
        return bbdd.delete(VehiculosSQLite.TABLA_MANTENIMIENTOS, VehiculosSQLite.COL_ID_MANTENIMIENTO + "=" + id, null);
    }

    public Mantenimiento getMantenimiento(int id){
        Cursor cursor = bbdd.query(VehiculosSQLite.TABLA_MANTENIMIENTOS, null, VehiculosSQLite.COL_ID_MANTENIMIENTO + "=" + id, null, null, null, VehiculosSQLite.COL_ID_MANTENIMIENTO);

        return cursorToMantenimiento(cursor);
    }

    public ArrayList<Mantenimiento> getManteninimientosFromVehiculo(Vehiculo vehiculo){
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        //sqLiteQueryBuilder.setTables(VehiculosSQLite.TABLA_MANTENIMIENTOS + "," + VehiculosSQLite.TABLA_VEHICULOS);
        //String query = sqLiteQueryBuilder.buildQuery(null,VehiculosSQLite.COL_ID_VEHICULO + "=" + vehiculo.getId(),null,null,null,null);

        String query = "SELECT * FROM tabla_mantenimientos a INNER JOIN tabla_vehiculos b ON a.id_vehiculo=b.id WHERE b.id=?";

        Log.d(TAG, "getManteninimientosFromVehiculo: query " + query);

        Cursor cursor = bbdd.rawQuery(query, new String[]{Integer.toString(vehiculo.getId())});
        return cursorToMantenimientoArrayList(cursor);
    }

    public ArrayList<Mantenimiento> getAllMantenimientos(){
        Log.d(TAG, "getAllMantenimientos: " + query);
        Cursor cursor = bbdd.rawQuery(query, null);

        return cursorToMantenimientoArrayList(cursor);
    }

    protected Mantenimiento cursorToMantenimiento(Cursor cursor) {
        Mantenimiento mantenimiento;

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        String queryEmpleados = sqLiteQueryBuilder.buildQuery(null, VehiculosSQLite.COL_ID + "=" + cursor.getInt(NUM_COL_ID_VEHICULO), null, null, null, null);

        Vehiculo vehiculo = VehiculoDAO.cursorToVehiculo(bbdd.query(VehiculosSQLite.TABLA_VEHICULOS, null, VehiculosSQLite.COL_ID + "=" + cursor.getInt(NUM_COL_ID_VEHICULO),null,null,null,null));

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return null;

        }else {
            Date date;
            try {
                date = simpleDateFormat.parse(cursor.getString(NUM_COL_FECHA));
            } catch (ParseException e) {
                e.printStackTrace();
                date = null;
            }
            mantenimiento = new  Mantenimiento(cursor.getInt(NUM_COL_ID_MANTENIMIENTO),
                    cursor.getString(NUM_COL_ESTADO_REPACION),
                    cursor.getString(NUM_COL_NOMBRE),
                    cursor.getString(NUM_COL_DESCRIPCION),
                    cursor.getFloat(NUM_COL_KILOMETRAJE_REPARACION),
                    date,
                    cursor.getString(NUM_COL_ESTADO_SINCRONIZACION),
                    vehiculo
            );
            cursor.close();

            return mantenimiento;
        }
    }

    protected ArrayList<Mantenimiento> cursorToMantenimientoArrayList(Cursor cursor){
        ArrayList<Mantenimiento> arrayList;

        if (cursor.getCount() == 0){
            cursor.close();
            return null;
        }else{
            arrayList = new ArrayList<Mantenimiento>();
            Mantenimiento mantenimiento;

            while (cursor.moveToNext()){
                Date date;
                Vehiculo vehiculo;

                try {
                    date = simpleDateFormat.parse(cursor.getString(NUM_COL_FECHA));
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = null;
                }

                vehiculo = new Vehiculo(cursor.getInt(NUM_COL_ID),
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

                mantenimiento = new  Mantenimiento(cursor.getInt(NUM_COL_ID_MANTENIMIENTO),
                        cursor.getString(NUM_COL_ESTADO_REPACION),
                        cursor.getString(NUM_COL_NOMBRE),
                        cursor.getString(NUM_COL_DESCRIPCION),
                        cursor.getFloat(NUM_COL_KILOMETRAJE_REPARACION),
                        date,
                        cursor.getString(NUM_COL_ESTADO_SINCRONIZACION),
                        vehiculo
                );

                arrayList.add(mantenimiento);
            }

            cursor.close();
            return arrayList;
        }
    }

}
