package es.uvigo.esei.bgcastro.tfm.activitys;

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
import es.uvigo.esei.bgcastro.tfm.adapter.OpinionArrayAdapter;
import es.uvigo.esei.bgcastro.tfm.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.loaders.OpinionesListLoader;
import es.uvigo.esei.bgcastro.tfm.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 23/5/16.
 */
public class BuscarOpinionActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Opinion>>{
    private static final int LOADER_ID = 5;
    private static final String TAG = "BuscarOpinionActivity";
    private OpinionArrayAdapter opinionArrayAdapter;
    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buscar_opinion);

        //Ligamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarBuscarOpinion);
        actionBar.setTitle(getString(R.string.titulo_toolbar_buscar_opiniones_activity));
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        ListView listView = (ListView) findViewById(R.id.listViewOpiniones);
        TextView textViewEmpty = (TextView) findViewById(R.id.emptyOpiniones);

        listView.setEmptyView(textViewEmpty);

        opinionArrayAdapter = new OpinionArrayAdapter(this, R.layout.opinion_item);

        listView.setAdapter(opinionArrayAdapter);

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<Opinion>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
        String address = preferences.getString(VehiculosPreferences.SERVER_ADDRESS, VehiculosPreferences.SERVER_ADDRESS_DEFAULT);
        int port = preferences.getInt(VehiculosPreferences.SERVER_PORT, VehiculosPreferences.SERVER_PORT_DEFAULT);

        return new OpinionesListLoader(getApplicationContext(), address, port, queryString);
    }

    @Override
    public void onLoadFinished(Loader<List<Opinion>> loader, List<Opinion> data) {
        if (data != null) {
            opinionArrayAdapter.setOpinionArrayList((ArrayList<Opinion>) data);
            opinionArrayAdapter.notifyDataSetChanged();
        }else if (queryString != null && !queryString.isEmpty()){
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Opinion>> loader) {
        opinionArrayAdapter.setOpinionArrayList(null);
    }
}
