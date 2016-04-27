package es.uvigo.esei.bgcastro.tfm;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.activitys.MantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;

/**
 * Created by braisgallegocastro on 26/4/16.
 */
public class mantenimientosActivityTest extends ActivityInstrumentationTestCase2<MantenimientosActivity> {
    private MantenimientosActivity activity;
    private ArrayList<Vehiculo> listaDePruebas = new ArrayList<Vehiculo>();
    private ArrayList<Mantenimiento> listaDeMantenimientos = new ArrayList<Mantenimiento>();


    public mantenimientosActivityTest() {
        super(MantenimientosActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();


        activity = this.getActivity();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)activity.getResources().getDrawable(R.drawable.ic_directions_car_black_48dp)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, DRAWING_CACHE_QUALITY_AUTO, stream);
        byte[] foto = stream.toByteArray();

        for (int i = 0; i < 3; i++) {
            Vehiculo vehiculo = new Vehiculo(foto, "marca "+i, "modelo "+i, "matricula "+i, i, "combustible "+i, i, i, i, i, "");
            assertNotNull(vehiculo);
            if (i == 0){
                vehiculo.setEstado(activity.getString(R.string.fa_wrench));
            }else if (i == 1){
                vehiculo.setEstado(activity.getString( R.string.fa_exclamation_triangle));
            }else{
                vehiculo.setEstado(activity.getString( R.string.fa_check));
            }

            vehiculo.setId(i+1);

            listaDePruebas.add(vehiculo);

            for (int j = 0; j < 3; j++) {
                Mantenimiento mantenimiento = new Mantenimiento("estado" + j,"nombre" + j,"descripcion" + j, j, new Date(),"estado sincronizacion" + j,vehiculo);
                listaDeMantenimientos.add(mantenimiento);
            }
        }
    }


    public void testQueryMantenimientoscontentProvider() throws Exception {
        Cursor cursor = activity.getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,null,null,null,null);

        assertNotNull(cursor);
    }

    public void testInsertMantenimientoContentProvider() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Mantenimiento m :
                listaDeMantenimientos) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(MantenimientosContentProvider.ID_VEHICULO, m.getVehiculo().getId());
            contentValues.put(MantenimientosContentProvider.ESTADO_REPARACION, m.getEstado());
            contentValues.put(MantenimientosContentProvider.NOMBRE, m.getNombre());
            contentValues.put(MantenimientosContentProvider.DESCRIPCION, m.getDescripcion());
            contentValues.put(MantenimientosContentProvider.KILOMETRAJE_REPARACION, m.getKilometrajeReparacion());
            contentValues.put(MantenimientosContentProvider.FECHA, simpleDateFormat.format(m.getFecha()));
            contentValues.put(MantenimientosContentProvider.ESTADO_SINCRONIZACION, m.getEstadoSincronizacion());

            Uri uri = activity.getContentResolver().insert(MantenimientosContentProvider.CONTENT_URI,contentValues);

            assertNotNull(uri);
        }

    }

}
