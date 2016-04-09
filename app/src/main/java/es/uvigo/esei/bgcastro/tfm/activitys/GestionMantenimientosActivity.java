package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.MantenimientoDAO;
import es.uvigo.esei.bgcastro.tfm.R;
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

        Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }

        calendar.add(Calendar.DATE, 1);

        StringBuilder diaSiguiente = new StringBuilder();
        diaSiguiente.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ").append(calendar.get(Calendar.MONTH)).append(" ").append(calendar.get(Calendar.YEAR));

        setContentView(R.layout.activity_gestion_mantenimientos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionMantenimientos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextNombreMantenimiento = (EditText) findViewById(R.id.editTextNombreMantenimiento);
        editTextDescripcionMantenimiento = (EditText) findViewById(R.id.editTextDescripcionMantenimiento);
        editTextKilometrajeMantenimiento = (EditText) findViewById(R.id.editTextKilometrajeMantenimiento);
        textViewFechaMantenimiento = (TextView) findViewById(R.id.fechaMantenimiento);

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

            default: {
                return false;
            }
        }
    }

    private void nuevoMantenimiento() {
        float kilometrajeReparacion;
        Date fecha = calendar.getTime();
        if (editTextKilometrajeMantenimiento.getText().toString().isEmpty())
            kilometrajeReparacion = 0;
        else
            kilometrajeReparacion = Float.parseFloat(editTextKilometrajeMantenimiento.getText().toString());


        mantenimiento = new Mantenimiento(getString(R.string.fa_square),
                editTextNombreMantenimiento.getText().toString(),
                editTextDescripcionMantenimiento.getText().toString(),
                kilometrajeReparacion,
                fecha,
                getString(R.string.fa_wrench),
                vehiculo);

        Log.d(TAG, "nuevoMantenimiento: " + mantenimiento.toString());

        //// TODO: 4/1/16 otro hilo
        //guardamos en la BBDD
        MantenimientoDAO bdd = new MantenimientoDAO(this);
        try {
            bdd .openForWriting();
            long idMantenimiento = bdd.insertMantenimiento(mantenimiento);
            mantenimiento.setId((int) idMantenimiento);
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            bdd.close();
        }

    }
}
