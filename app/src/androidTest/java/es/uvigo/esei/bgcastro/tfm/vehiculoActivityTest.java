package es.uvigo.esei.bgcastro.tfm;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculoDAO;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by braisgallegocastro on 24/12/15.
 */
public class vehiculoActivityTest extends ActivityInstrumentationTestCase2<VehiculosActivity> {
    private VehiculosActivity activity;
    private ArrayList<Vehiculo> listaDePruebas = new ArrayList<Vehiculo>();

    public vehiculoActivityTest() {
        super(VehiculosActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
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

            listaDePruebas.add(vehiculo);
        }

    }

    public void testPreconditions(){
        assertNotNull(listaDePruebas);
        assertNotNull(activity);
    }

    public void testTestInsertDAO() throws Exception {
        long [] resultado = new long[3];
        int cont = 0;

        VehiculoDAO bdd = new VehiculoDAO(activity.getApplicationContext());
        bdd .openForWriting();
        for (Vehiculo v:listaDePruebas
                ) {
            resultado[cont] = bdd.insertVehiculo(v);
            cont++;
        }

        bdd.close();

        for (int i = 0; i < resultado.length; i++) {
            assertNotSame(i,is(not(1)));
        }
    }
}
