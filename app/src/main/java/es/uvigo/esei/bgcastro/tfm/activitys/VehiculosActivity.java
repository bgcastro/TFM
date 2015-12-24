package es.uvigo.esei.bgcastro.tfm.activitys;

import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.adapter.VehiculoAdapter;

public class VehiculosActivity extends AppCompatActivity {
    private  ArrayList <Vehiculo> listaVehiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        listaVehiculos = new ArrayList<>();

        inicializar();

        //asociamos elementos de la vista
        ListView listViewVehiculos = (ListView) findViewById(R.id.listViewVehiculos);

        VehiculoAdapter adapter = new VehiculoAdapter(this, R.layout.vehiculo_item,listaVehiculos);


        //vinculamos un adapter
        listViewVehiculos.setAdapter(adapter);
    }

    public void setListaVehiculos(ArrayList<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    public void inicializar() {
        this.listaVehiculos.add(new Vehiculo(R.drawable.ic_directions_car_black_48dp, "marca", "modelo", "matricula",  80000, "combustible", 1598, 115,  "color", new Date(), "estado"));
    }
}
