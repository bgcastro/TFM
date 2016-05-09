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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.ReparacionesContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class ReparacionesActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int URL_LOADER = 2;
    private static final String TAG = "ReparacionesActivity";
    private Mantenimiento mantenimiento;

    private TextView textViewPrecioTotal;
    private ListView listViewReparaciones;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reparaciones);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarReparaciones);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewPrecioTotal = (TextView) findViewById(R.id.totalReparacion);

        listViewReparaciones = (ListView) findViewById(R.id.listViewReparaciones);

        Intent intent = getIntent();

        if (intent != null) {
            mantenimiento = intent.getParcelableExtra(MantenimientosActivity.MANTENIMIENTO);
        }

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{ VehiculosSQLite.COL_NOMBRE_REPARACION,
                VehiculosSQLite.COL_DESCRIPCION_REPARACION,
                VehiculosSQLite.COL_REFERENCIA,
                VehiculosSQLite.COL_PRECIO };

        int[] into = new int[]{R.id.nombreRepacionItem,
                R.id.descripcionReparacionItem,
                R.id.precioReparacionItem,
                R.id.referenciaReparacionItem};

        adapter = new SimpleCursorAdapter(this,R.layout.reparacion_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        listViewReparaciones.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to construct and return different loaders.
        switch (id) {
            case URL_LOADER:
                int idMantenimiento = mantenimiento.getId();
                String[] projection = null;
                String where = ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION + "=" + "?";
                String[] whereArgs = {Integer.toString(idMantenimiento)};
                String sortOrder = null;

                // Query URI
                Uri queryUri = ReparacionesContentProvider.CONTENT_URI;

                // Create the new Cursor loader.
                return new CursorLoader(ReparacionesActivity.this, queryUri, projection, where, whereArgs, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int totalPrecio = 0;
        while (data.moveToNext()){
            totalPrecio += data.getInt(data.getColumnIndex(ReparacionesContentProvider.PRECIO));
        }

        Log.d(TAG, "onLoadFinished: total precio " + totalPrecio);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.coste_total))
                .append(" ").append(totalPrecio).append(" ")
                .append(getString(R.string.divisa));

        textViewPrecioTotal.setText(stringBuilder);

        Log.d(TAG, "onLoadFinished: string builder " + stringBuilder);

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