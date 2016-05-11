package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.ReparacionesContentProvider;
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

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionReparaciones);
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
        if (uiToReparacion() && reparacion.getId() == -1){
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReparacionesContentProvider.NOMBRE_REPARACION, reparacion.getNombreReparacion());
            contentValues.put(ReparacionesContentProvider.DESCRIPCION_REPARACION, reparacion.getDescripcion());
            contentValues.put(ReparacionesContentProvider.REFERENCIA, reparacion.getReferencia());
            contentValues.put(ReparacionesContentProvider.PRECIO, reparacion.getPrecio());
            contentValues.put(ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION, reparacion.getMantenimiento().getId());

            Uri uri = getContentResolver().insert(ReparacionesContentProvider.CONTENT_URI,contentValues);

            reparacion.setId(Integer.parseInt(uri.getLastPathSegment()));
        }
    }
}
