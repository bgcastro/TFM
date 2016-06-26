package es.uvigo.esei.bgcastro.tfm.app.activitys;

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

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 17/5/16.
 * Activity para la generacion de informes
 */
public class InformeActvity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int URL_LOADER = 3;
    private static final String TAG = "InformeActvity";
    private static final String DATEPICKER_INICIO_TAG = "fechaInicioDialog";
    private static final String DATEPICKER_FIN_TAG = "fechaFinDialog";
    //Variables para fechas
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    //Relacionados con UI
    private static String[] fromColumns = new String[]{VehiculosSQLite.COL_NOMBRE, VehiculosSQLite.COL_PRECIO};
    private static int[] into = new int[]{R.id.nombreMantenimientoInformeItem, R.id.totalMantenimientoInformeItem};
    String stringFechaFin;
    //Entities
    private Vehiculo vehiculo;
    private Calendar fechaInicio = Calendar.getInstance();
    private Calendar fechaFin = Calendar.getInstance();
    private String stringFechaInicio;
    //Adapter
    private SimpleCursorAdapter adapter;
    private TextView textViewPrecioTotal;
    private TextView textViewFechaInicio;
    private TextView textViewFechaFin;

    /**
     * Implementacion principal de la funcionalidad
     *
     * @param savedInstanceState Estado anterior
     */
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

        //Fecha de inicio
        fechaInicio.add(Calendar.MONTH, -3);

        //Elementos de UI
        textViewFechaInicio = (TextView) findViewById(R.id.fechaInicio);
        textViewFechaFin = (TextView) findViewById(R.id.fechaFin);
        ListView listViewGastos = (ListView) findViewById(R.id.listViewInformes);
        TextView emptyView = (TextView) findViewById(R.id.emptyInformes);
        textViewPrecioTotal = (TextView) findViewById(R.id.totalInformes);

        listViewGastos.setEmptyView(emptyView);

        adapter = new SimpleCursorAdapter(this,R.layout.informe_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        //Vinculacion del adapter
        listViewGastos.setAdapter(adapter);

        //Fechas de inicio y fin en la UI
        stringFechaInicio = simpleDateFormat.format(fechaInicio.getTime());
        stringFechaFin = simpleDateFormat.format(fechaFin.getTime());

        textViewFechaInicio.setText(stringFechaInicio);
        textViewFechaFin.setText(stringFechaFin);

        //load manager que se encarga de recoger los datos en background
        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //Dialogo selector de fecha de inicio
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

        //Dialogo selector de fecha de fin
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

    /**
     * Inicializacion del loader
     * @param loader No usado
     * @param data No usado
     */
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

        // Ponemos los resultados en la UI
        adapter.swapCursor(data);
    }

    /**
     * Metodo llamado en el reset que se encarga de resetear el Adapter
     * @param loader no usado
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * Metodo que vuelve a recargar los datos de nuevo
     */
    private void updateResults(){
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }
}
