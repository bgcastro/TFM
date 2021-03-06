package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.entities.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

public class VehiculosActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String VEHICULO = "VEHICULO";
    public static final int MODIFICAR_VEHICULO = 1;
    private static final int URL_LOADER = 0;
    private static String TAG = "VehiculosActivity";
    //Adapter
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflamos el layout
        setContentView(R.layout.activity_vehiculos);

        //Asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarVehiculos);
        actionBar.setTitle(getString(R.string.titulo_toolbar_vehiculos_activity));

        setSupportActionBar(actionBar);

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //asociamos elementos de la vista
        ListView listViewVehiculos = (ListView) findViewById(R.id.listViewVehiculos);

        //Vista para cuando no hay vehiculos
        TextView textViewEmptyVehiculos = (TextView) findViewById(R.id.emptyVehiculos);
        listViewVehiculos.setEmptyView(textViewEmptyVehiculos);

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{VehiculosSQLite.COL_IMAGEN_VEHICULO,
                VehiculosSQLite.COL_MODELO, VehiculosSQLite.COL_MATRICULA,
                VehiculosSQLite.COL_KILOMETRAJE, VehiculosSQLite.COL_ESTADO};

        int[] into = new int[]{R.id.imagenVehiculo, R.id.nombreMarca,
                R.id.matricula, R.id.kilometraje, R.id.estadoVehiculo};

        adapter = new SimpleCursorAdapter(this,R.layout.vehiculo_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        //Modificamos algunos elementos de la vista
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    //Imagen
                    case R.id.imagenVehiculo:
                        byte[] img = cursor.getBlob(cursor.getColumnIndex(VehiculosSQLite.COL_IMAGEN_VEHICULO));
                        ((ImageView)view).setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

                        return true;
                    //Estado del vehiculo
                    case R.id.estadoVehiculo:
                        ((TextView)view).setText(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO)));

                        final Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
                        ((TextView)view).setTypeface(font);
                        ((TextView)view).setTextColor(view.getResources().getColor(R.color.grisClaro));
                        ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

                        return true;

                    //Kilometraje
                    case R.id.kilometraje:
                        SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
                        StringBuilder kilometraje = new StringBuilder();

                        kilometraje.append(" ").append(cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE)));
                        if (preferences.getBoolean(VehiculosPreferences.MEASURE_UNIT, VehiculosPreferences.MEASURE_UNIT_DEFAULT)) {
                            kilometraje.append("Km");
                        }else {
                            kilometraje.append("Millas");
                        }
                        ((TextView)view).setText(kilometraje);

                        return true;
                }
                return false;
            }
        });

        //vinculamos un adapter
        listViewVehiculos.setAdapter(adapter);

        //gestionamos el click en la lista;
        listViewVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModificacionItem = new Intent(VehiculosActivity.this,GestionVehiculosActivity.class);
                Bundle bundle = new Bundle();

                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                //enviamos el vehiculo a modificar
                Vehiculo veh = new Vehiculo(c);

                bundle.putParcelable(VEHICULO, veh);
                intentModificacionItem.putExtras(bundle);

                Log.d(TAG, "onItemClick: position" + position);

                //lanzamos un intent para modificar un vehiculo
                startActivityForResult(intentModificacionItem, MODIFICAR_VEHICULO);
            }
        });

    }

    /**
     * Metodo llamado cuando la activity pasa a resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    /**
     * Inicializacion del loader
     *
     * @param id   ID del loader
     * @param args No usado
     * @return Los datos
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                String[] projection = null;
                String where = null;
                String[] whereArgs = null;
                String sortOrder = null;

                // Query URI
                Uri queryUri = VehiculoContentProvider.CONTENT_URI;

                // Nuevo Cursor loader.
                CursorLoader cursorLoader = new CursorLoader(VehiculosActivity.this, queryUri, projection, where, whereArgs, sortOrder);
                Log.d(TAG, "onCreateLoader: " + cursorLoader.getUri());
                return cursorLoader;

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
     * Inicializa el menu
     *
     * @param menu Menu en el que colocar items.
     * @return Boolean para mostrar el menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vehiculos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * LLamado cuando se pulsa un item
     * @param item Item seleccionado
     * @return true si se ha atendido la accion.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_vehiculo:{
                nuevoVehiculo();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Metodo que lanza la activity GestionVehiculosActivity
     */
    private void nuevoVehiculo() {
        Intent nuevoVehiculoIntent;
        Log.d(TAG, "onClick: anadir vehiculo");

        //lanzamos un intent para abrir una nueva activity que permita la gestión de vehículos
        nuevoVehiculoIntent = new Intent(VehiculosActivity.this, GestionVehiculosActivity.class);
        startActivity(nuevoVehiculoIntent);
    }
}