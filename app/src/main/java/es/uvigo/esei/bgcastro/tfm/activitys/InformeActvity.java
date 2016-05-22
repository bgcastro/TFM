package es.uvigo.esei.bgcastro.tfm.activitys;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 17/5/16.
 */
public class InformeActvity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int URL_LOADER = 3;
    private static final String TAG = "InformeActvity";
    private static final String DATEPICKER_INICIO_TAG = "fechaInicioDialog";
    private static final String DATEPICKER_FIN_TAG = "fechaFinDialog";

    private Vehiculo vehiculo;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    private Calendar fechaInicio = Calendar.getInstance();
    private Calendar fechaFin = Calendar.getInstance();
    private String stringFechaInicio;
    String stringFechaFin;

    private SimpleCursorAdapter adapter;

    private static String[] fromColumns = new String[]{ VehiculosSQLite.COL_NOMBRE, VehiculosSQLite.COL_PRECIO};
    private static int[] into = new int[]{R.id.nombreMantenimientoInformeItem,R.id.totalMantenimientoInformeItem};

    private TextView textViewPrecioTotal;
    private TextView textViewFechaInicio;
    private TextView textViewFechaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_informe);

        Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        //Asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarInformes);
        actionBar.setTitle(getString(R.string.titulo_toolbar_informes_activity));
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fechaInicio.add(Calendar.MONTH,-3);

        textViewFechaInicio = (TextView) findViewById(R.id.fechaInicio);
        textViewFechaFin = (TextView) findViewById(R.id.fechaFin);
        ListView listViewGastos = (ListView) findViewById(R.id.listViewInformes);
        TextView emptyView = (TextView) findViewById(R.id.emptyInformes);
        textViewPrecioTotal = (TextView) findViewById(R.id.totalInformes);

        listViewGastos.setEmptyView(emptyView);

        adapter = new SimpleCursorAdapter(this,R.layout.informe_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        listViewGastos.setAdapter(adapter);

        stringFechaInicio = simpleDateFormat.format(fechaInicio.getTime());
        stringFechaFin = simpleDateFormat.format(fechaFin.getTime());

        textViewFechaInicio.setText(stringFechaInicio);
        textViewFechaFin.setText(stringFechaFin);

        //load manager que se encarga de recoger los datos en background
        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        textViewFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        fechaInicio.set(year, month, day);
                        textViewFechaInicio.setText(new StringBuilder().append(day).append(" ").append(month).append(" ").append(year).toString());
                        updateResults();
                    }
                }, fechaInicio.get(Calendar.YEAR), fechaInicio.get(Calendar.MONTH), fechaInicio.get(Calendar.DAY_OF_MONTH), false);

                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_INICIO_TAG);
            }
        });

        textViewFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        fechaFin.set(year, month, day);
                        textViewFechaFin.setText(new StringBuilder().append(day).append(" ").append(month).append(" ").append(year).toString());
                        updateResults();
                    }
                }, fechaFin.get(Calendar.YEAR), fechaFin.get(Calendar.MONTH), fechaFin.get(Calendar.DAY_OF_MONTH), false);

                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_FIN_TAG);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to construct and return different loaders.
        switch (id) {
            case URL_LOADER:
                VehiculosSQLite bddHelper = new VehiculosSQLite(getApplicationContext(),VehiculosSQLite.NOMBRE_BBDD,null,VehiculosSQLite.VERSION);

                String idVehiculo = Integer.toString(vehiculo.getId());

                String rawQuery = "SELECT SUM(R.precio) as precio, M.nombre, M.estado, M.reparacion, M._id, R.id_mantenimiento " +
                        "FROM " + VehiculosSQLite.TABLA_REPARACIONES + " R LEFT JOIN " +
                        VehiculosSQLite.TABLA_MANTENIMIENTOS + " M ON R.id_mantenimiento = M._id " +
                        " WHERE M.id_vehiculo = ? AND M.reparacion BETWEEN ? AND ?" +
                        " GROUP BY M._id";
                String[] queryParams = {idVehiculo, simpleDateFormat.format(fechaInicio.getTime()), simpleDateFormat.format(fechaFin.getTime())};// to substitute placeholders
                Log.d(TAG, "onCreateLoader: " + rawQuery);
                SQLiteCursorLoader loader = new SQLiteCursorLoader(getApplicationContext(), bddHelper, rawQuery, queryParams);
                return loader;

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int totalPrecio = 0;
        while (data.moveToNext()){
            totalPrecio += data.getInt(data.getColumnIndex("precio"));
        }

        Log.d(TAG, "onLoadFinished: total precio " + totalPrecio);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.coste_total_informe))
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

    private void updateResults(){
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }
}
