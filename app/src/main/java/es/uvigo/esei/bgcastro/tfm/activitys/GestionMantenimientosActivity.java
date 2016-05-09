package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class GestionMantenimientosActivity extends BaseActivity{

    private static final String DATEPICKER_TAG = "datepicker";
    private static final String TAG = "GMantenimientosActivity";
    private EditText editTextNombreMantenimiento;
    private EditText editTextDescripcionMantenimiento;
    private EditText editTextKilometrajeMantenimiento;
    private TextView textViewFechaMantenimiento;

    private final Calendar calendar = Calendar.getInstance();

    private Vehiculo vehiculo;
    private Mantenimiento mantenimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gestion_mantenimientos);

        editTextNombreMantenimiento = (EditText) findViewById(R.id.editTextNombreMantenimiento);
        editTextDescripcionMantenimiento = (EditText) findViewById(R.id.editTextDescripcionMantenimiento);
        editTextKilometrajeMantenimiento = (EditText) findViewById(R.id.editTextKilometrajeMantenimiento);
        textViewFechaMantenimiento = (TextView) findViewById(R.id.fechaMantenimiento);

        Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        if (mantenimiento == null) {
            mantenimiento = new Mantenimiento();
        }

        calendar.add(Calendar.DATE, 1);

        StringBuilder diaSiguiente = new StringBuilder();
        diaSiguiente.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ").append(calendar.get(Calendar.MONTH)).append(" ").append(calendar.get(Calendar.YEAR));


        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionMantenimientos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewFechaMantenimiento.setText(diaSiguiente.toString());

        textViewFechaMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        calendar.set(year, month, day);
                        textViewFechaMantenimiento.setText(new StringBuilder().append(day).append(" ").append(month).append(" ").append(year).toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestion_mantenimientos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_mantenimiento:{
                nuevoMantenimiento();
                return true;
            }

            case R.id.action_remove_mantenimiento: {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        vehiculo = savedInstanceState.getParcelable(VehiculosActivity.VEHICULO);
    }

    private void nuevoMantenimiento() {

        mantenimiento.setEstado(getString(R.string.fa_square_o));

        if (uiToMantenimiento()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            ContentValues contentValues = new ContentValues();
            contentValues.put(MantenimientosContentProvider.ID_VEHICULO, vehiculo.getId());
            contentValues.put(MantenimientosContentProvider.ESTADO_REPARACION, mantenimiento.getEstado());
            contentValues.put(MantenimientosContentProvider.NOMBRE, mantenimiento.getNombre());
            contentValues.put(MantenimientosContentProvider.DESCRIPCION, mantenimiento.getDescripcion());
            contentValues.put(MantenimientosContentProvider.KILOMETRAJE_REPARACION, mantenimiento.getKilometrajeReparacion());
            contentValues.put(MantenimientosContentProvider.FECHA, simpleDateFormat.format(mantenimiento.getFecha()));
            contentValues.put(MantenimientosContentProvider.ESTADO_SINCRONIZACION, mantenimiento.getEstadoSincronizacion());

            Uri uri = getContentResolver().insert(MantenimientosContentProvider.CONTENT_URI, contentValues);
            mantenimiento.setId(Integer.parseInt(uri.getLastPathSegment()));

            Log.d(TAG, "nuevoMantenimiento: " + mantenimiento.toString());

        }
    }

    private boolean uiToMantenimiento(){
        boolean success = true;

        float kilometrajeReparacion;
        Date fecha = new Date();
        String nombre = editTextNombreMantenimiento.getText().toString();
        String descripcion = editTextDescripcionMantenimiento.getText().toString();
        String kilometraje = editTextKilometrajeMantenimiento.getText().toString();

        if (kilometraje.isEmpty()) {
            editTextKilometrajeMantenimiento.setError(getString(R.string.error_kilometraje_vacio_mantenimiento));
            success = false;
        } else {
            kilometrajeReparacion = Float.parseFloat(kilometraje);
            mantenimiento.setKilometrajeReparacion(kilometrajeReparacion);
        }

        if (nombre.isEmpty()) {
            editTextNombreMantenimiento.setError(getString(R.string.error_descripcion_vacia));
            success = false;
        }else {
            mantenimiento.setNombre(nombre);
        }

        if (descripcion.isEmpty()) {
            editTextDescripcionMantenimiento.setError(getString(R.string.error_nombre_mantenimiento_vacio));
            success = false;
        }else {
            mantenimiento.setDescripcion(descripcion);
        }

        mantenimiento.setFecha(calendar.getTime());
        mantenimiento.setVehiculo(vehiculo);

        return success;
    }
}
