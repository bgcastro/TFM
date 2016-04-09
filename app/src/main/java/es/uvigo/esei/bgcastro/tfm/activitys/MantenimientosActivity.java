package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.DAO.MantenimientoDAO;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.adapter.MantenimientoAdapter;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class MantenimientosActivity extends BaseActivity{
    private static final String TAG = "MantenimientosActivity";
    ListView listViewMantenimientos;
    ArrayList<Mantenimiento> mantenimientos;
    MantenimientoAdapter adapter;
    Vehiculo vehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_mantenimientos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarMantenimientos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (vehiculo == null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        inicializar();

        listViewMantenimientos = (ListView) findViewById(R.id.listViewMantenimientos);

        adapter = new MantenimientoAdapter(this,R.layout.mantenimiento_item,mantenimientos);

        listViewMantenimientos.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(VehiculosActivity.VEHICULO, vehiculo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        vehiculo = savedInstanceState.getParcelable(VehiculosActivity.VEHICULO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mantenimientos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_mantenimiento:{
                nuevoMantenimiento();
                return true;
            }

            case R.id.action_remove_mantenimiento: {
                //TODO añadir eliminar
                return true;
            }

            default: {
                return false;
            }
        }
    }

    private void nuevoMantenimiento() {
        Log.d(TAG, "nuevoMantenimiento: vehiculo" + vehiculo);

        Intent intent = new Intent(MantenimientosActivity.this,GestionMantenimientosActivity.class);
        intent.putExtra(VehiculosActivity.VEHICULO, vehiculo);

        startActivity(intent);
    }
    
    private void inicializar(){
        //TODO otro hilo
        //recuperamos de la BBDD
        MantenimientoDAO mantenimientoDAO = new MantenimientoDAO(this);

        try {
            Log.d(TAG, "inicializar: " + vehiculo);

            mantenimientoDAO.openForReading();
            mantenimientos = mantenimientoDAO.getManteninimientosFromVehiculo(vehiculo);

            if (mantenimientos == null) {
                mantenimientos = new ArrayList<>();
            }

            Log.d(TAG, "inicializar: size" + mantenimientos.size());
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            mantenimientoDAO.close();
        }

    }
}
