package es.uvigo.esei.bgcastro.tfm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.activitys.MantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.activitys.ReparacionesActivity;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.content_provider.ReparacionesContentProvider;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entities.Reparacion;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class reparacionesActivityTest extends ActivityInstrumentationTestCase2<ReparacionesActivity> {
    private ReparacionesActivity activity;
    private ArrayList<Reparacion> listaReparaciones;
    private ArrayList<Mantenimiento> listaMantenimientos;

    public reparacionesActivityTest() {
        super(ReparacionesActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra(MantenimientosActivity.MANTENIMIENTO, new Mantenimiento());
        setActivityIntent(intent);

        activity = this.getActivity();

        Cursor c = activity.getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,null,null,null,null);

        listaMantenimientos = cursorToMantenimientoArrayList(c);

        listaReparaciones = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            listaReparaciones.add(new Reparacion("Nombre" + i, "Descripcion" + i, "Ref" + i, i, null));
        }
    }

    public void testInsertContentProvider() throws Exception {
        for (Mantenimiento m :
                listaMantenimientos) {

            for (Reparacion r:
                    listaReparaciones) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(ReparacionesContentProvider.NOMBRE_REPARACION,r.getNombreReparacion());
                contentValues.put(ReparacionesContentProvider.DESCRIPCION_REPARACION,r.getDescripcion());
                contentValues.put(ReparacionesContentProvider.PRECIO,r.getPrecio());
                contentValues.put(ReparacionesContentProvider.REFERENCIA,r.getReferencia());
                contentValues.put(ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION,m.getId());

                Uri uri = activity.getContentResolver().insert(ReparacionesContentProvider.CONTENT_URI,contentValues);
                assertNotNull(uri);
            }
        }
    }

    public void testQueryContentProvider() throws Exception {
        Cursor c = activity.getContentResolver().query(ReparacionesContentProvider.CONTENT_URI,null,null,null,null,null);
        assertNotNull(c);
    }

    public void testPreconditions() throws Exception {
        assertNotNull(listaMantenimientos);
        assertNotNull(listaReparaciones);
    }

    protected ArrayList<Mantenimiento> cursorToMantenimientoArrayList(Cursor cursor){
        ArrayList<Mantenimiento> arrayList;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (cursor.getCount() == 0){
            cursor.close();
            return null;
        }else{
            arrayList = new ArrayList<Mantenimiento>();
            Mantenimiento mantenimiento;

            while (cursor.moveToNext()){
                Date date;
                Vehiculo vehiculo = null;

                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_FECHA)));
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = null;
                }

                Uri uri = Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI, cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ID_VEHICULO)));

                Cursor cursorVehiculo = activity.getContentResolver().query(uri,null,null,null,null);

                if (cursorVehiculo.getCount() != 0) {
                    cursorVehiculo.moveToFirst();

                    vehiculo = new Vehiculo(cursorVehiculo.getInt(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_ID)),
                            cursorVehiculo.getBlob(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_IMAGEN_VEHICULO)),
                            cursorVehiculo.getString(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_MARCA)),
                            cursorVehiculo.getString(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_MODELO)),
                            cursorVehiculo.getString(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_MATRICULA)),
                            cursorVehiculo.getFloat(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE)),
                            cursorVehiculo.getString(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_COMBUSTIBLE)),
                            cursorVehiculo.getInt(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_CILINDRADA)),
                            cursorVehiculo.getFloat(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_POTENCIA)),
                            cursorVehiculo.getInt(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_COLOR)),
                            cursorVehiculo.getInt(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_ANHO)),
                            cursorVehiculo.getString(cursorVehiculo.getColumnIndex(VehiculosSQLite.COL_ESTADO))
                    );
                }

                mantenimiento = new  Mantenimiento(cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_MANTENIMIENTO)),
                        cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO_REPARACION)),
                        cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_DESCRIPCION)),
                        cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE_REPARACION)),
                        date,
                        cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO_SINCRONIZACION)),
                        vehiculo
                );

                arrayList.add(mantenimiento);
            }

            cursor.close();
            return arrayList;
        }
    }


}
