package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.adapter.OpinionArrayAdapter;
import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.app.loaders.OpinionesListLoader;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 23/5/16.
 * Activity para buscar opiniones en el servidor a partir de una busqueda
 */
public class BuscarOpinionActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Opinion>>{
    private static final int LOADER_ID = 5;
    private static final String TAG = "BuscarOpinionActivity";

    //Adapter para las opiniones
    private OpinionArrayAdapter opinionArrayAdapter;

    //String de busqueda
    private String queryString;

    /**
     * Implementacion principal de la funcionalidad
     *
     * @param savedInstanceState Estado anterior
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflamos la UI
        setContentView(R.layout.activity_buscar_opinion);

        //Ligamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarBuscarOpinion);
        actionBar.setTitle(getString(R.string.titulo_toolbar_buscar_opiniones_activity));
        setSupportActionBar(actionBar);
        //habilitar boton de atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Elementos de la UI
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        ListView listView = (ListView) findViewById(R.id.listViewOpiniones);
        //TextView para la lista vacia
        TextView textViewEmpty = (TextView) findViewById(R.id.emptyOpiniones);

        //Asignacion del TextView a mostrar en la lista vacia
        listView.setEmptyView(textViewEmpty);

        // Inicializacion del OpinionArrayAdapter
        opinionArrayAdapter = new OpinionArrayAdapter(this, R.layout.opinion_item);
        //Establecer el adapter
        listView.setAdapter(opinionArrayAdapter);

        //Activar el boton de busqueda para pulsar
        searchView.setSubmitButtonEnabled(true);
        //listener para el cuadro de busqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * onQueryTextSubmit
             * Metodo llamado al ser pulsado el boton de busqueda
             * @param query Texto a buscar
             * @return Verdadero si se ha manejado la busqueda
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    queryString = query;
                    Log.d(TAG, "onClick: onQueryTextSubmit " + queryString);
                    getLoaderManager().restartLoader(LOADER_ID, null, BuscarOpinionActivity.this).forceLoad();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Inicio del Loader
        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    /**
     * LLamado cuando se pulsa un item
     * @param item Item seleccionado
     * @return true si se ha atendido la accion.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inicializacion del loader
     * @param id no usado
     * @param args no usado
     * @return
     */
    @Override
    public Loader<List<Opinion>> onCreateLoader(int id, Bundle args) {
        //recuperamos el puerto y la direccion del servidor
        SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
        String address = preferences.getString(VehiculosPreferences.SERVER_ADDRESS, VehiculosPreferences.SERVER_ADDRESS_DEFAULT);
        int port = preferences.getInt(VehiculosPreferences.SERVER_PORT, VehiculosPreferences.SERVER_PORT_DEFAULT);

        //Retorno del OpinionesListLoader
        return new OpinionesListLoader(getApplicationContext(), address, port, queryString);
    }

    /**
     * Metodo llamado al finalizar la carga y que se encarga de llenar el Adapter
     * @param loader No usado
     * @param data Array con las opiniones
     */
    @Override
    public void onLoadFinished(Loader<List<Opinion>> loader, List<Opinion> data) {
        if (data != null) {
            opinionArrayAdapter.setOpinionArrayList((ArrayList<Opinion>) data);
            opinionArrayAdapter.notifyDataSetChanged();
        }else if (queryString != null && !queryString.isEmpty()){
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo llamado en el reset que se encarga de resetear el Adapter
     * @param loader no usado
     */
    @Override
    public void onLoaderReset(Loader<List<Opinion>> loader) {
        opinionArrayAdapter.setOpinionArrayList(null);
    }
}
