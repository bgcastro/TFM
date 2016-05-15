package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.content_provider.ReparacionesContentProvider;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Reparacion;

/**
 * Created by braisgallegocastro on 9/5/16.
 */
public class GestionReparacionesActivity extends BaseActivity {
    private static final String TAG = "GestionReparActivity";
    private Mantenimiento mantenimiento;

    private Reparacion reparacion;

    private EditText editTextReparacion;
    private EditText editTextDescripcionReparacion;
    private EditText editTextReferencia;
    private EditText editTextPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gestion_reparaciones);

        //asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionReparaciones);
        actionBar.setTitle(getString(R.string.titulo_toolbar_gestion_reparaciones_activity));
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            mantenimiento = intent.getParcelableExtra(MantenimientosActivity.MANTENIMIENTO);
            Log.d(TAG, "onCreate: mantenimiento");
        }
        if (reparacion == null) {
            reparacion = new Reparacion();
        }

        editTextReparacion = (EditText) findViewById(R.id.editTextReparacion);
        editTextDescripcionReparacion = (EditText) findViewById(R.id.editTextDescripcionReparacion);
        editTextReferencia = (EditText) findViewById(R.id.editTextReferencia);
        editTextPrecio = (EditText) findViewById(R.id.editTextPrecio);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestion_reparaciones, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nueva_reparacion:{
                nuevaReparacion();
                return true;
            }

            case R.id.action_remove_reparacion: {
                //TODO añadir eliminar
                return true;
            }

            case android.R.id.home: {
                this.onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }

    }

    private boolean uiToReparacion(){
        boolean success = true;
        String nombreReparacion = editTextReparacion.getText().toString();
        String descripcionReparacion = editTextDescripcionReparacion.getText().toString();
        String referencia = editTextReferencia.getText().toString();
        String precio = editTextPrecio.getText().toString();

        if (mantenimiento != null) {
            reparacion.setMantenimiento(mantenimiento);
        }

        if (nombreReparacion.isEmpty()){
            editTextReparacion.setError(getString(R.string.error_nombre_reparacion_vacio));
            success = false;
        }else {
            reparacion.setNombreReparacion(nombreReparacion);
        }

        if (descripcionReparacion.isEmpty()) {
            editTextDescripcionReparacion.setError(getString(R.string.error_descripcion_reparacion_vacia));
            success = false;
        } else {
            reparacion.setDescripcion(descripcionReparacion);
        }

        if (precio.isEmpty()){
            editTextPrecio.setError(getString(R.string.error_precio_vacio));
        } else {
            reparacion.setPrecio(Float.parseFloat(precio));
        }

        if (!referencia.isEmpty()){
            reparacion.setReferencia(referencia);
        }

        return success;
    }

    private void nuevaReparacion(){
        //si los datos introducidos son validos podemos guardar la reparacion
        if (uiToReparacion() && reparacion.getId() == -1){
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReparacionesContentProvider.NOMBRE_REPARACION, reparacion.getNombreReparacion());
            contentValues.put(ReparacionesContentProvider.DESCRIPCION_REPARACION, reparacion.getDescripcion());
            contentValues.put(ReparacionesContentProvider.REFERENCIA, reparacion.getReferencia());
            contentValues.put(ReparacionesContentProvider.PRECIO, reparacion.getPrecio());
            contentValues.put(ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION, reparacion.getMantenimiento().getId());

            //guardamos la reparacion
            Uri uri = getContentResolver().insert(ReparacionesContentProvider.CONTENT_URI,contentValues);

            reparacion.setId(Integer.parseInt(uri.getLastPathSegment()));

            //actualizamos el estado del mantenimiento y del vehiculo
            actualizarEstado();
        }
    }

    private void actualizarEstado(){
        ContentValues contentValuesMantenimientos = new ContentValues();
        contentValuesMantenimientos.put(MantenimientosContentProvider.ESTADO_REPARACION,getString(R.string.fa_check));

        Uri uriMantenimiento = Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getId()));
        getContentResolver().update(uriMantenimiento,contentValuesMantenimientos,null,null);

        int idVehiculo = mantenimiento.getVehiculo().getId();
        String[] projection = {MantenimientosContentProvider.ID_MANTENIMIENTO,MantenimientosContentProvider.ESTADO_REPARACION};
        String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?";
        String[] whereArgs = {Integer.toString(idVehiculo)};
        String sortOrder = null;

        // Query URI
        Uri queryUri = MantenimientosContentProvider.CONTENT_URI;

        // Create the new Cursor loader.
        Cursor cursor = getContentResolver().query(queryUri, projection, where, whereArgs, sortOrder);

        boolean mantenimientoResueltos = true;
        while (cursor.moveToNext()){
            String estado = cursor.getString(cursor.getColumnIndex(MantenimientosContentProvider.ESTADO_REPARACION));
            String fa_square = getString(R.string.fa_square_o);
            String fa_triangle = getString(R.string.fa_exclamation_triangle);
            if (estado.hashCode() == fa_square.hashCode() || estado.hashCode() == fa_triangle.hashCode()){
                mantenimientoResueltos = false;
                break;
            }
        }

        //Cambiamos el estado a chequeado porque ya no existen mas mantenimientos que realizar
        if (mantenimientoResueltos){
            Uri uriVehiculos = Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getVehiculo().getId()));

            ContentValues contentValuesVehiculo = new ContentValues();
            contentValuesVehiculo.put(VehiculoContentProvider.ESTADO,getString(R.string.fa_check));

            getContentResolver().update(uriVehiculos,contentValuesVehiculo,null,null);
        }
    }
}
