package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.app.entities.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

import static java.lang.Math.abs;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class MantenimientosActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MantenimientosActivity";
    private static final int URL_LOADER = 1;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

    private ListView listViewMantenimientos;
    private SimpleCursorAdapter adapter;

    private Vehiculo vehiculo;

    public static final String MANTENIMIENTO = "mantenimiento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_mantenimientos);

        //asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarMantenimientos);
        actionBar.setTitle(getString(R.string.titulo_toolbar_mantenimientos_activity));
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //asociamos elementos de la vista
        listViewMantenimientos = (ListView) findViewById(R.id.listViewMantenimientos);

        //Vista para cuando no hay mantenimientos
        TextView textViewEmptyMantenimientos = (TextView) findViewById(R.id.emptyMantenimientos);
        listViewMantenimientos.setEmptyView(textViewEmptyMantenimientos);

        Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{
                VehiculosSQLite.COL_NOMBRE,
                VehiculosSQLite.COL_DESCRIPCION,
                VehiculosSQLite.COL_KILOMETRAJE_REPARACION,
                VehiculosSQLite.COL_FECHA,
                VehiculosSQLite.COL_ESTADO_REPARACION};

        int[] into = new int[]{
                R.id.nombreMantenimientoItem,
                R.id.descripcionMantenimientoItem,
                R.id.kilometrajeMantenimientoItem,
                R.id.fechaMantenimientoItem,
                R.id.estadoMantenimientoItem
                };

        adapter = new SimpleCursorAdapter(this,R.layout.mantenimiento_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.estadoMantenimientoItem: {
                        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO)));

                        final Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
                        ((TextView) view).setTypeface(font);
                        ((TextView) view).setTextColor(view.getResources().getColor(R.color.grisClaro));
                        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

                        return true;
                    }

                    case R.id.kilometrajeMantenimientoItem:{
                        SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
                        StringBuilder kilometraje = new StringBuilder();
                        float valorKilometrajeMantenimiento = cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE_REPARACION));
                        float diferencia = valorKilometrajeMantenimiento - vehiculo.getKilometraje();

                        kilometraje.append(Float.toString(valorKilometrajeMantenimiento));
                        if (preferences.getBoolean(VehiculosPreferences.MEASURE_UNIT, VehiculosPreferences.MEASURE_UNIT_DEFAULT)) {
                            kilometraje.append("Km");
                        }else {
                            kilometraje.append("Millas");
                        }
                        kilometraje.append(" ");

                        if ( diferencia >= 0){
                            kilometraje.append(getString(R.string.faltan)).append(" ");
                            kilometraje.append(diferencia);
                        }else {
                            kilometraje.append(getString(R.string.pasado)).append(" ");
                            kilometraje.append(Float.toString(abs(diferencia)));
                        }
                        if (preferences.getBoolean(VehiculosPreferences.MEASURE_UNIT, VehiculosPreferences.MEASURE_UNIT_DEFAULT)) {
                            kilometraje.append("Km");
                        }else {
                            kilometraje.append("Millas");
                        }
                        kilometraje.append(")");

                        ((TextView)view).setText(kilometraje.toString());

                        return  true;
                    }

                    case R.id.fechaMantenimientoItem: {
                        DateTime fechaReparacion = new DateTime();
                        String fecha = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_FECHA));
                        String estado = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO_REPARACION));
                        StringBuilder stringBuilder = new StringBuilder();

                        try {
                            fechaReparacion = new DateTime(simpleDateFormat.parse(fecha));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int diferenciaDias = Days.daysBetween(new DateTime(), fechaReparacion).getDays();

                        if (estado.hashCode() == getString(R.string.fa_square_o).hashCode()){
                            stringBuilder.append(fecha).append(" ")
                                    .append(getString(R.string.faltan)).append(" ").append(diferenciaDias)
                                    .append(" ").append(getString(R.string.dias));
                        }else if (estado.hashCode() == getString(R.string.fa_check).hashCode() ) {
                                stringBuilder.append(fecha).append(" ")
                                        .append(getString(R.string.han_pasado)).append(" ")
                                        .append(abs(diferenciaDias)).append(" ").append(getString(R.string.dias));
                            }else {
                                stringBuilder.append(fecha).append(" ")
                                        .append(getString(R.string.restrasado)).append(" ")
                                        .append(abs(diferenciaDias)).append(" ").append(getString(R.string.dias));
                            }


                        ((TextView) view).setText(stringBuilder.toString());

                        return true;
                    }

                    default: return false;
                }
            }
        });

        listViewMantenimientos.setAdapter(adapter);

        listViewMantenimientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModificarMantenimiento = new Intent(MantenimientosActivity.this, GestionMantenimientosActivity.class);
                Bundle bundle = new Bundle();

                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                Mantenimiento mantenimiento = new Mantenimiento(c, getApplicationContext());

                bundle.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
                bundle.putParcelable(MANTENIMIENTO, mantenimiento);

                intentModificarMantenimiento.putExtras(bundle);

                Log.d(TAG, "onItemClick: position" + position);

                startActivity(intentModificarMantenimiento);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(URL_LOADER).forceLoad();
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

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void nuevoMantenimiento() {
        Log.d(TAG, "nuevoMantenimiento: vehiculo" + vehiculo);

        Intent intent = new Intent(MantenimientosActivity.this,GestionMantenimientosActivity.class);
        intent.putExtra(VehiculosActivity.VEHICULO, vehiculo);

        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to construct and return different loaders.
        switch (id) {
            case URL_LOADER:
                int idVehiculo = vehiculo.getId();
                String[] projection = null;
                String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?";
                String[] whereArgs = {Integer.toString(idVehiculo)};
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
