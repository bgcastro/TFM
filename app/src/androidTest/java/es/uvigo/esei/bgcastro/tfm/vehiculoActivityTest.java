package es.uvigo.esei.bgcastro.tfm;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;

/**
 * Created by braisgallegocastro on 24/12/15.
 */
public class vehiculoActivityTest extends ActivityInstrumentationTestCase2<VehiculosActivity> {
    private VehiculosActivity activity;
    private ArrayList<Vehiculo> listaDePruebas = new ArrayList<Vehiculo>();
    private ListView lista;

    public vehiculoActivityTest() {
        super(VehiculosActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        for (int i = 0; i < 5; i++) {
            //listaDePruebas.add(new Vehiculo(R.drawable.ic_directions_car_black_48dp, "marca", "modelo", "matricula",  80000, "combustible", 1598, 115,  "color", new Date(), "estado"));
        }

        activity = this.getActivity();
        activity.setListaVehiculos(listaDePruebas);

        lista = (ListView) activity.findViewById(R.id.listViewVehiculos);
    }

    public void testPreconditions(){
        assertNotNull(listaDePruebas);
        assertNotNull(activity);
        assertNotNull(lista);
        assertNotNull(lista.getAdapter());
        assertEquals(5,lista.getAdapter().getCount());
    }

    public void testVehiculosItems(){
        int cont = 0;
        for (int i = 0; i < listaDePruebas.size(); i++) {
            Vehiculo v1 = listaDePruebas.get(i);
            Vehiculo v2 = (Vehiculo) lista.getAdapter().getItem(i);
            assertEquals(v1,v2);
        }
    }
}
