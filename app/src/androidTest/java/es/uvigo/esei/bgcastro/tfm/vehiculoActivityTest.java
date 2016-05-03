package es.uvigo.esei.bgcastro.tfm;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculoDAO;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;

/**
 * Created by braisgallegocastro on 24/12/15.
 */
public class vehiculoActivityTest extends ActivityInstrumentationTestCase2<VehiculosActivity> {
    private VehiculosActivity activity;
    private ArrayList<Vehiculo> listaDeVehiculos = new ArrayList<Vehiculo>();
    private ArrayList<Mantenimiento> listaDeMantenimientos = new ArrayList<>();

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

            listaDeVehiculos.add(vehiculo);
        }

        for (int i = 0; i < 3; i++) {
            Mantenimiento mantenimiento = new Mantenimiento("estado"+i,"nombre"+i,"descripcion"+i,i,new Date(),"sincronizacion"+i, listaDeVehiculos.get(0));
            listaDeMantenimientos.add(mantenimiento);
        }

    }

    public void testPreconditions(){
        assertNotNull(listaDeVehiculos);
        assertNotNull(activity);
    }

    /*public void testTestInsertDAO() throws Exception {
        long [] resultado = new long[3];
        int cont = 0;

        VehiculoDAO bdd = new VehiculoDAO(activity.getApplicationContext());
        bdd .openForWriting();
        for (Vehiculo v: listaDeVehiculos
                ) {
            resultado[cont] = bdd.insertVehiculo(v);
            cont++;
        }

        bdd.close();

        for (int i = 0; i < resultado.length; i++) {
            assertNotSame(i,is(not(1)));
        }
    }*/

/*    public void testInsertContentProviderMantenimientos() throws Exception {
        long [] resultado = new long[3];
        int cont = 0;

        for (Mantenimiento m: listaDeMantenimientos) {
            ContentValues contentValues = new ContentValues();
            activity.getContentResolver().insert(MantenimientosContentProvider.CONTENT_URI,m);
        }

    }*/

    public void testInsertContentProviderVehiculos() throws Exception {
        for (Vehiculo v :
                listaDeVehiculos) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(VehiculoContentProvider.IMAGEN_VEHICULO, v.getImagenVehiculo());
            contentValues.put(VehiculoContentProvider.MARCA, v.getMarca());
            contentValues.put(VehiculoContentProvider.MODELO, v.getModelo());
            contentValues.put(VehiculoContentProvider.MATRICULA, v.getMatricula());
            contentValues.put(VehiculoContentProvider.KILOMETRAJE, v.getKilometraje());
            contentValues.put(VehiculoContentProvider.COMBUSTIBLE, v.getCombustible());
            contentValues.put(VehiculoContentProvider.CILINDRADA, v.getCilindrada());
            contentValues.put(VehiculoContentProvider.POTENCIA, v.getPotencia());
            contentValues.put(VehiculoContentProvider.COLOR, v.getColor());
            contentValues.put(VehiculoContentProvider.ANHO, v.getAño());
            contentValues.put(VehiculoContentProvider.ESTADO, v.getEstado());

            assertNotNull(activity.getContentResolver().insert(VehiculoContentProvider.CONTENT_URI,contentValues));
        }

    }

    public void testQueryContentProviderVehoculos() throws Exception {
        Cursor c = activity.getContentResolver().query(VehiculoContentProvider.CONTENT_URI,null,null,null,null);
        ArrayList<Vehiculo> vehiculosBD = VehiculoDAO.cursorToArrayList(c);
        for (Vehiculo v:
             vehiculosBD) {

            assertNotNull(v);

        }
    }
}
