package es.uvigo.esei.bgcastro.tfm.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

public class VehiculosActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String TAG = "VehiculosActivity";

    public static final String VEHICULO = "VEHICULO";
    public static final int MODIFICAR_VEHICULO = 1;
    private static final int URL_LOADER = 0;

    private SimpleCursorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_vehiculos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarVehiculos);
        setSupportActionBar(actionBar);

        //load manager que se encarga de recoger los datos en background
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(URL_LOADER, null, this);

        //asociamos elementos de la vista
        ListView listViewVehiculos = (ListView) findViewById(R.id.listViewVehiculos);

        //simple cursor adapter que rellena la IU
        String[] fromColumns = new String[]{VehiculosSQLite.COL_IMAGEN_VEHICULO,VehiculosSQLite.COL_MODELO,VehiculosSQLite.COL_MATRICULA,
                VehiculosSQLite.COL_KILOMETRAJE,VehiculosSQLite.COL_ESTADO};
        int[] into = new int[]{R.id.imagenVehiculo,R.id.nombreMarca,R.id.matricula,R.id.kilometraje,R.id.estadoVehiculo};

        adapter = new SimpleCursorAdapter(this,R.layout.vehiculo_item,null,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.imagenVehiculo:
                        byte[] img = cursor.getBlob(cursor.getColumnIndex(VehiculosSQLite.COL_IMAGEN_VEHICULO));
                        ((ImageView)view).setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

                        return true;
                    
                    case R.id.estadoVehiculo:
                        ((TextView)view).setText(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO)));

                        final Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
                        ((TextView)view).setTypeface(font);
                        ((TextView)view).setTextColor(view.getResources().getColor(R.color.grisClaro));
                        ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

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

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().getLoader(URL_LOADER).forceLoad();
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
                CursorLoader cursorLoader = new CursorLoader(VehiculosActivity.this, queryUri, projection, where, whereArgs, sortOrder);
                Log.d(TAG, "onCreateLoader: " + cursorLoader.getUri());
                return cursorLoader;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vehiculos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_vehiculo:{
                nuevoVehiculo();
                return true;
            }

            default: {
                return false;
            }
        }
    }

    private void nuevoVehiculo() {
        Intent nuevoVehiculoIntent;
        Log.d(TAG, "onClick: anadir vehiculo");

        //lanzamos un intent para abrir una nueva activity que permita la gestión de vehículos
        nuevoVehiculoIntent = new Intent(VehiculosActivity.this, GestionVehiculosActivity.class);
        startActivity(nuevoVehiculoIntent);
    }
}