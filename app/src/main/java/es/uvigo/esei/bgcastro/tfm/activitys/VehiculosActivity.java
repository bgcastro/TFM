package es.uvigo.esei.bgcastro.tfm.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.adapter.VehiculoCursorAdapter;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;

import static android.view.View.OnClickListener;

public class VehiculosActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String TAG = "VehiculosActivity";

    public static final String VEHICULO = "VEHICULO";
    public static final int MODIFICAR_VEHICULO = 1;
    private static final int URL_LOADER = 0;

    private ImageButton botonAnadirVehiculo;
    private VehiculoCursorAdapter adapter;
    //private VehiculoArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_vehiculos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarVehiculos);
        setSupportActionBar(actionBar);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(URL_LOADER, null, this);

        //asociamos elementos de la vista
        ListView listViewVehiculos = (ListView) findViewById(R.id.listViewVehiculos);
        botonAnadirVehiculo = (ImageButton) findViewById(R.id.anadirVehiculo);

        adapter = new VehiculoCursorAdapter(this,null,VehiculoCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,R.layout.vehiculo_item);

        //vinculamos un adapter
        listViewVehiculos.setAdapter(adapter);

        //gestionamos el click en la lista;
        listViewVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModificacionItem = new Intent(VehiculosActivity.this,GestionVehiculosActivity.class);
                Bundle bundle = new Bundle();

                //enviamos el vehiculo a modificar
               // bundle.putParcelable(VEHICULO, adapter.getItem(position));
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

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

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
                Uri queryUri = VehiculoContentProvider.CONTENT_URI;

                // Create the new Cursor loader.
                return new CursorLoader(VehiculosActivity.this, queryUri, projection, where, whereArgs, sortOrder);

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


   /* @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: result count" + data.getCount());

        listaVehiculos.clear();

        while (data.moveToNext()) {
            Vehiculo newItem = new Vehiculo(data);
            listaVehiculos.add(newItem);
        }

    }*/

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Remove the existing result Cursor from the List Adapter.
        adapter.swapCursor(null);
        // This handler is not synchronized with the UI thread, so you
        // will need to synchronize it before modifying any UI elements directly.
    }

/*    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Remove the existing result Cursor from the List Adapter.
        adapter.clear();
        // This handler is not synchronized with the UI thread, so you
        // will need to synchronize it before modifying any UI elements directly.
    }*/
}