package es.uvigo.esei.bgcastro.tfm.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class MantenimientosActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MantenimientosActivity";
    private static final int URL_LOADER = 1;

    ListView listViewMantenimientos;
    //ArrayList<Mantenimiento> mantenimientos;
    //MantenimientoAdapter adapter;
    private SimpleCursorAdapter adapter;

    Vehiculo vehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_mantenimientos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarMantenimientos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listViewMantenimientos = (ListView) findViewById(R.id.listViewMantenimientos);

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        Intent intent = getIntent();

        if (vehiculo == null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{VehiculosSQLite.COL_ID_MANTENIMIENTO,
                VehiculosSQLite.COL_NOMBRE,
                VehiculosSQLite.COL_DESCRIPCION,
                VehiculosSQLite.COL_KILOMETRAJE_REPARACION,
                VehiculosSQLite.COL_FECHA,
                VehiculosSQLite.COL_ESTADO_SINCRONIZACION };

        int[] into = new int[]{R.id.estadoMantenimientoItem,
                R.id.nombreMantenimientoItem,
                R.id.descripcionMantenimientoItem,
                R.id.kilometrajeMantenimientoItem,
                R.id.estadoSincronizacion};

        adapter = new SimpleCursorAdapter(this,R.layout.mantenimiento_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

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
    
    /*private void inicializar(){
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

    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to construct and return different loaders.
        switch (id) {
            case URL_LOADER:
                String[] projection = null;
                String where = null;
                String[] whereArgs = null;
                String sortOrder = null;

                // Query URI
                Uri queryUri = MantenimientosContentProvider.CONTENT_URI;

                // Create the new Cursor loader.
                return new CursorLoader(MantenimientosActivity.this, queryUri, projection, where, whereArgs, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Replace the result Cursor displayed by the Cursor Adapter with the new result set.
        adapter.swapCursor(data);
        // This handler is not synchronized with the UI thread, so you
        // will need to synchronize it before modifying any UI elements directly.

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Remove the existing result Cursor from the List Adapter.
        adapter.swapCursor(null);
        // This handler is not synchronized with the UI thread, so you
        // will need to synchronize it before modifying any UI elements directly.
    }
}
