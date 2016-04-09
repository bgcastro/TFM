package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculoDAO;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.adapter.VehiculoAdapter;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.OnClickListener;

public class VehiculosActivity extends AppCompatActivity {
    public static final String VEHICULO = "VEHICULO";
    public static final int MODIFICAR_VEHICULO = 1;
    private static String TAG = "VehiculosActivity";
    private ArrayList <Vehiculo> listaVehiculos;
    private ImageButton botonAnadirVehiculo;
    private VehiculoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_vehiculos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarVehiculos);
        setSupportActionBar(actionBar);

        //listaVehiculos = new ArrayList<>();

        inicializar();

        //asociamos elementos de la vista
        ListView listViewVehiculos = (ListView) findViewById(R.id.listViewVehiculos);
        botonAnadirVehiculo = (ImageButton) findViewById(R.id.anadirVehiculo);

        //creamos un adapter para manejar los datos
        adapter = new VehiculoAdapter(this, R.layout.vehiculo_item,listaVehiculos);


        //vinculamos un adapter
        listViewVehiculos.setAdapter(adapter);

        //gestionamos el click en la lista;
        listViewVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModificacionItem = new Intent(VehiculosActivity.this,GestionVehiculosActivity.class);
                Bundle bundle = new Bundle();

                //enviamos el vehiculo a modificar
                bundle.putParcelable(VEHICULO, adapter.getItem(position));
                intentModificacionItem.putExtras(bundle);

                Log.d(TAG, "onItemClick: position" + position);

                //lanzamos un intent para modificar un vehiculo
                startActivityForResult(intentModificacionItem, MODIFICAR_VEHICULO);


            }
        });

        //onclicklistener para el boton de añadir
        botonAnadirVehiculo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoVehiculoIntent;
                Log.d(TAG, "onClick: anadir vehiculo");

                //lanzamos un intent para abrir una nueva activity que permita la gestión de vehículos
                nuevoVehiculoIntent = new Intent(VehiculosActivity.this, GestionVehiculosActivity.class);
                startActivity(nuevoVehiculoIntent);
            }
        });
    }

    public void setListaVehiculos(ArrayList<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    public void inicializar() {
        //// TODO: 4/1/16 otro hilo
        //recuperamos de la BBDD
        VehiculoDAO bdd = new VehiculoDAO(this);
        bdd.openForReading();
        this.listaVehiculos = bdd.getAllVehiculos();
        if(this.listaVehiculos == null) {
            this.listaVehiculos = new ArrayList();
        }
        Log.d(TAG, "inicializar: " + listaVehiculos.size());

        bdd.close();
    }
}