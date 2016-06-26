package es.uvigo.esei.bgcastro.tfm.app.activitys;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.ReparacionesContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.app.entities.Reparacion;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class ReparacionesActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int URL_LOADER = 2;
    private static final String TAG = "ReparacionesActivity";

    //Entities
    private Mantenimiento mantenimiento;

    //Elementos de UI
    private TextView textViewPrecioTotal;
    private ListView listViewReparaciones;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reparaciones);

        //Asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarReparaciones);
        actionBar.setTitle(getString(R.string.titulo_toolbar_reparaciones_activity));
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //asociamos elementos de la vista
        textViewPrecioTotal = (TextView) findViewById(R.id.totalReparacion);

        listViewReparaciones = (ListView) findViewById(R.id.listViewReparaciones);

        //Vista para cuando no hay reparaciones
        TextView textViewEmptyReparaciones = (TextView) findViewById(R.id.emptyReparaciones);
        listViewReparaciones.setEmptyView(textViewEmptyReparaciones);

        Intent intent = getIntent();

        if (intent != null) {
            mantenimiento = intent.getParcelableExtra(GestionMantenimientosActivity.MANTENIMIENTO);
        }

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{ VehiculosSQLite.COL_NOMBRE_REPARACION,
                VehiculosSQLite.COL_DESCRIPCION_REPARACION,
                VehiculosSQLite.COL_PRECIO,
                VehiculosSQLite.COL_REFERENCIA };

        int[] into = new int[]{R.id.nombreRepacionItem,
                R.id.descripcionReparacionItem,
                R.id.precioReparacionItem,
                R.id.referenciaReparacionItem};

        adapter = new SimpleCursorAdapter(this,R.layout.reparacion_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        //Viculacion del adapter
        listViewReparaciones.setAdapter(adapter);

        //OnClick de item
        listViewReparaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModificarMantenimiento = new Intent(ReparacionesActivity.this, GestionReparacionesActivity.class);
                Bundle bundle = new Bundle();

                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                Reparacion reparacion = new Reparacion(c, getApplicationContext());

                bundle.putParcelable(GestionReparacionesActivity.REPARACION,reparacion);
                bundle.putParcelable(GestionMantenimientosActivity.MANTENIMIENTO, mantenimiento);

                intentModificarMantenimiento.putExtras(bundle);

                Log.d(TAG, "onItemClick: position" + position);

                //Lanzamiento de la activity GestionReparacionesActivity
                startActivity(intentModificarMantenimiento);
            }
        });

    }

    /**
     * Inicializa el menu
     *
     * @param menu Menu en el que colocar items.
     * @return Boolean para mostrar el menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reparaciones, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * LLamado cuando se pulsa un item
     *
     * @param item Item seleccionado
     * @return true si se ha atendido la accion.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Nueva reparacion
            case R.id.action_add_reparacion:{
                nuevaReparacion();
                return true;
            }

            //Atras
            case android.R.id.home: {
                this.onBackPressed();
                return true;
            }

            //Otros
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(URL_LOADER).forceLoad();
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

    /**
     * Metodo llamado al terminar la busqueda de datos
     * @param loader No usado
     * @param data Datos encontrados
     */
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

        adapter.swapCursor(data);
    }

    /**
     * Metodo llamado en el reset de loader
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * Metodo que arranca la activity GestionReparacionesActivity
     */
    private void nuevaReparacion() {
        Log.d(TAG, "nuevaReparacion: nuevaReparacion");
        Intent intentNuevaReparacion = new Intent(ReparacionesActivity.this,GestionReparacionesActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelable(MantenimientosActivity.MANTENIMIENTO, mantenimiento);
        intentNuevaReparacion.putExtras(bundle);

        startActivity(intentNuevaReparacion);
    }
}
