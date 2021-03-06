package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.alarm.AlarmReceiverService;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.app.entities.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 20/2/16.
 * Activity para la gestion de mantenimientos
 */
public class GestionMantenimientosActivity extends BaseActivity{
    //nombre para la recuperacio de un parcelable
    public static final String MANTENIMIENTO = "mantenimiento";
    private static final String DATEPICKER_TAG = "datepicker";
    private static final String TAG = "GMantenimientosActivity";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    //elementos de UI
    private EditText editTextNombreMantenimiento;
    private EditText editTextDescripcionMantenimiento;
    private EditText editTextKilometrajeMantenimiento;
    private TextView textViewFechaMantenimiento;
    private TextView textViewRepar;
    //Variables para la gestion de fecha
    private Calendar calendar = Calendar.getInstance();
    //PendingIntent para las notificaciones
    private PendingIntent pendingIntent;
    //entities
    private Vehiculo vehiculo;
    private Mantenimiento mantenimiento;
    //variable para el control de la edcion
    private boolean edicionActivada = true;

    /**
     * Implementacion principal de la funcionalidad
     *
     * @param savedInstanceState Estado anterior
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflamos la UI
        setContentView(R.layout.activity_gestion_mantenimientos);

        //intent de arranque de la activity
        final Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
            mantenimiento = intent.getParcelableExtra(MANTENIMIENTO);
        }

        if (mantenimiento == null) {
            mantenimiento = new Mantenimiento();
        }

        //Ligamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionMantenimientos);
        actionBar.setTitle(getString(R.string.titulo_toolbar_gestion_mantenimientos_activity));
        setSupportActionBar(actionBar);
        //habilitar boton de atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //elementos de la UI
        editTextNombreMantenimiento = (EditText) findViewById(R.id.editTextNombreMantenimiento);
        editTextDescripcionMantenimiento = (EditText) findViewById(R.id.editTextDescripcionMantenimiento);
        editTextKilometrajeMantenimiento = (EditText) findViewById(R.id.editTextKilometrajeMantenimiento);
        textViewFechaMantenimiento = (TextView) findViewById(R.id.fechaMantenimiento);
        textViewRepar = (TextView) findViewById(R.id.reparar);

        //tipografia
        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        textViewRepar.setTypeface(font);
        textViewRepar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        //OnClick del boton reparar
        textViewRepar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: reparar");

                if (mantenimiento.getId() < 0) {
                    //si aun esta sin guardar
                    Toast.makeText(getApplicationContext(),R.string.mantenimiento_no_creado,Toast.LENGTH_LONG).show();
                } else {
                    //sino comenzar ReparacionesActivity
                    Intent intentReparar = new Intent(GestionMantenimientosActivity.this, ReparacionesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(GestionMantenimientosActivity.MANTENIMIENTO, mantenimiento);

                    intentReparar.putExtras(bundle);

                    startActivity(intentReparar);
                }
            }
        });

        //Ponemos la hora y el minuto que ul usuario tiene en sus preferencias
        SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
        String[] pieces = preferences.getString(VehiculosPreferences.NOTIFICATION_HOUR,VehiculosPreferences.NOTIFICATION_HOUR_DEFAULT).split(":");

        int horaNotificacion = Integer.parseInt(pieces[0]);
        int minutoNotificacion = Integer.parseInt(pieces[1]);

        calendar.set(Calendar.HOUR,horaNotificacion);
        calendar.set(Calendar.MINUTE,minutoNotificacion);

        StringBuilder diaActual = new StringBuilder();
        diaActual.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ")
                .append(calendar.get(Calendar.MONTH)).append(" ")
                .append(calendar.get(Calendar.YEAR));

        textViewFechaMantenimiento.setText(diaActual.toString());

        // OnClick para el dialogo selector de fechas
        textViewFechaMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        calendar.set(year, month, day);
                        textViewFechaMantenimiento.setText(new StringBuilder().append(day).append(" ").append(month).append(" ").append(year).toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        //si ya se selecciona uno ya creado
        if (intent.hasExtra(MANTENIMIENTO)){
            //Rellenar UI
            rellenarUI();

            //desactivamos la edicion
            desactivarEdicion();
        }
    }

    /**
     * Inicializa el menu
     *
     * @param menu Menu en el que colocar items.
     * @return Boolean para mostrar el menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestion_mantenimientos, menu);

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
            //Guardar
            case R.id.action_save_mantenimiento:{
                guardarMantenimiento();
                return true;
            }

            //Modificar
            case R.id.action_modify_mantenimiento:
                activarEdicion();
                invalidateOptionsMenu();
                return true;

            //Eliminar
            case R.id.action_remove_mantenimiento: {
                eliminarMantenimiento();
                return true;
            }

            //Boton atras
            case android.R.id.home: {
                this.onBackPressed();
                return true;
            }

            //Otras opciones
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Metodo llamado antes de mostrar el menu
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Se elige que mostrar en funcion del estado de edicion
        if (!edicionActivada){
            //si el vehiculo se modifica
            menu.removeItem(R.id.action_save_mantenimiento);
        }else {
            //si el vehiculo se añade
            menu.removeItem(R.id.action_remove_mantenimiento);
            menu.removeItem(R.id.action_modify_mantenimiento);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Guardado del estado
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
    }

    /**
     * Recuperacion del estado
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        vehiculo = savedInstanceState.getParcelable(VehiculosActivity.VEHICULO);
    }

    /**
     * Guardado del mantenimiento
     */
    private void guardarMantenimiento(){
        if (mantenimiento != null && mantenimiento.getId() != -1) {
            modificarMantenimiento();
        }else {
            nuevoMantenimiento();
        }
    }

    /**
     * Creacion y guardado en BD de un nuevo mantemiento
     */
    private void nuevoMantenimiento() {
        //Si los datos introducidos por el usuario son correctos procedemos a guardar
        if (uiToMantenimiento()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MantenimientosContentProvider.ID_VEHICULO, vehiculo.getId());
            contentValues.put(MantenimientosContentProvider.ESTADO_REPARACION, mantenimiento.getEstado());
            contentValues.put(MantenimientosContentProvider.NOMBRE, mantenimiento.getNombre());
            contentValues.put(MantenimientosContentProvider.DESCRIPCION, mantenimiento.getDescripcion());
            contentValues.put(MantenimientosContentProvider.KILOMETRAJE_REPARACION, mantenimiento.getKilometrajeReparacion());
            contentValues.put(MantenimientosContentProvider.FECHA, simpleDateFormat.format(mantenimiento.getFecha()));

            Uri uri = getContentResolver().insert(MantenimientosContentProvider.CONTENT_URI, contentValues);

            //completamos el mantenimiento con el nuevo id asignado por la bd
            mantenimiento.setId(Integer.parseInt(uri.getLastPathSegment()));

            //actualizacion del estado
            actualizarEstado(mantenimiento.getVehiculo().getId());

            Log.d(TAG, "nuevoMantenimiento: " + mantenimiento.toString());

            //Preparamos una nueva notificacion
            if (new DateTime(calendar).isAfterNow()){
                setNotification(calendar, mantenimiento);
            }

            //Cambiamos el menu
            invalidateOptionsMenu();

            //desactivamos la edicion
            desactivarEdicion();
        }
    }

    /**
     * Modificacion y guardado en BD de un mantemiento
     */
    private void modificarMantenimiento(){
        //Si los datos introducidos por el usuario son correctos procedemos a guardar
        if (uiToMantenimiento()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MantenimientosContentProvider.ID_VEHICULO, vehiculo.getId());
            contentValues.put(MantenimientosContentProvider.ESTADO_REPARACION, mantenimiento.getEstado());
            contentValues.put(MantenimientosContentProvider.NOMBRE, mantenimiento.getNombre());
            contentValues.put(MantenimientosContentProvider.DESCRIPCION, mantenimiento.getDescripcion());
            contentValues.put(MantenimientosContentProvider.KILOMETRAJE_REPARACION, mantenimiento.getKilometrajeReparacion());
            contentValues.put(MantenimientosContentProvider.FECHA, simpleDateFormat.format(mantenimiento.getFecha()));

            String updateID = Integer.toString(mantenimiento.getId());

            getContentResolver().update(Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI, updateID),contentValues,null,null);

            //Actualizamos el estado del vehiculo que ahora pasa a estar pendiente de mantenimiento
            ContentValues contentValuesVehiculo = new ContentValues();
            if (mantenimiento.getEstado().hashCode() == getString(R.string.fa_exclamation_triangle).hashCode()){
                contentValuesVehiculo.put(VehiculoContentProvider.ESTADO, getString(R.string.fa_exclamation_triangle));
            }else {
                contentValuesVehiculo.put(VehiculoContentProvider.ESTADO, getString(R.string.fa_wrench));
            }
            getContentResolver().update(Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI, Integer.toString(mantenimiento.getVehiculo().getId())),contentValuesVehiculo,null,null);

            Log.d(TAG, "modificarMantenimiento: " + mantenimiento.toString());

            //modificamos la notificacion
            if (new DateTime(calendar).isAfterNow()){
                setNotification(calendar, mantenimiento);
            }

            //Cambiamos el menu
            invalidateOptionsMenu();

            //desactivamos la edicion
            desactivarEdicion();
        }
    }

    /**
     * Eliminacion de un mantenimiento
     */
    private void eliminarMantenimiento(){
        String deleteID = Integer.toString(mantenimiento.getId());
        getContentResolver().delete( Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI,deleteID), null, null);

        Log.d(TAG, "eliminarMantenimiento: " + deleteID);

        actualizarEstado(mantenimiento.getVehiculo().getId());

        finish();
    }

    /**
     * Se rellena la UI con datos existentes
     */
    private void rellenarUI() {

        editTextNombreMantenimiento.setText(mantenimiento.getNombre());
        editTextDescripcionMantenimiento.setText(mantenimiento.getDescripcion());
        editTextKilometrajeMantenimiento.setText(Float.toString(mantenimiento.getKilometrajeReparacion()));
        textViewFechaMantenimiento.setText(simpleDateFormat.format(mantenimiento.getFecha()));
    }

    /**
     * Creacion de un mantenimiento a partir de la UI
     * @return Si se ha construido con exito
     */
    private boolean uiToMantenimiento(){
        boolean success = true;

        float kilometrajeReparacion;
        String nombre = editTextNombreMantenimiento.getText().toString();
        String descripcion = editTextDescripcionMantenimiento.getText().toString();
        String kilometraje = editTextKilometrajeMantenimiento.getText().toString();
        DateTime fechaReparacion = new DateTime(calendar.getTime());

        mantenimiento.setFecha(calendar.getTime());
        mantenimiento.setVehiculo(vehiculo);

        if (kilometraje.isEmpty()) {
            editTextKilometrajeMantenimiento.setError(getString(R.string.error_kilometraje_vacio_mantenimiento));
            success = false;
        } else {
            kilometrajeReparacion = Float.parseFloat(kilometraje);
            mantenimiento.setKilometrajeReparacion(kilometrajeReparacion);

            if (fechaReparacion.isBeforeNow() || vehiculo.getKilometraje() - kilometrajeReparacion > 0 ){
                mantenimiento.setEstado(getString(R.string.fa_exclamation_triangle));
            }else {
                mantenimiento.setEstado(getString(R.string.fa_square_o));
            }
        }

        if (nombre.isEmpty()) {
            editTextNombreMantenimiento.setError(getString(R.string.error_descripcion_vacia));
            success = false;
        }else {
            mantenimiento.setNombre(nombre);
        }

        if (descripcion.isEmpty()) {
            editTextDescripcionMantenimiento.setError(getString(R.string.error_descripcion_mantenimiento_vacio));
            success = false;
        }else {
            mantenimiento.setDescripcion(descripcion);
        }

        return success;
    }

    /**
     * Desabilita la UI
     */
    private void desactivarEdicion() {
        edicionActivada = false;

        editTextNombreMantenimiento.setEnabled(false);
        editTextDescripcionMantenimiento.setEnabled(false);
        editTextKilometrajeMantenimiento.setEnabled(false);
        textViewFechaMantenimiento.setEnabled(false);
    }

    /**
     * Habilita la UI
     */
    private void activarEdicion() {
        edicionActivada = true;

        editTextNombreMantenimiento.setEnabled(true);
        editTextDescripcionMantenimiento.setEnabled(true);
        editTextKilometrajeMantenimiento.setEnabled(true);
        textViewFechaMantenimiento.setEnabled(true);
    }

    /**
     * Crea la notificacion
     * @param fechaNotificacion fecha de la notificacion
     * @param mantenimiento Mantenimiento relacionado
     */
    private void setNotification(Calendar fechaNotificacion, Mantenimiento mantenimiento) {
        AlarmManager alarmManager ;
        Intent intent;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Toast.makeText(getApplicationContext(),
                getString(R.string.notificacion) + " " +
                fechaNotificacion.get(Calendar.DATE) + " " +
                fechaNotificacion.get(Calendar.MONTH) + " " +
                fechaNotificacion.get(Calendar.YEAR) + " a las " +
                fechaNotificacion.get(Calendar.HOUR_OF_DAY) +
                ":" + fechaNotificacion.get(Calendar.MINUTE),
                Toast.LENGTH_LONG).show();

        //Datos a mostrar en la notificacion
        intent = new Intent(getApplicationContext(),AlarmReceiverService.class);
        intent.putExtra(AlarmReceiverService.TITULO, mantenimiento.getNombre());
        intent.putExtra(AlarmReceiverService.CONTENIDO, mantenimiento.getDescripcion());
        intent.putExtra(AlarmReceiverService.MANTENIMIENTO,mantenimiento);

        //PendingIntent para cuando se haga click en la notificacion
        pendingIntent = PendingIntent.getService(getApplicationContext(), mantenimiento.getId(),intent,PendingIntent.FLAG_ONE_SHOT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, fechaNotificacion.getTimeInMillis(), pendingIntent);
    }

    /**
     * Metodo que actualiza los estados
     * @param idVehiculo
     */
    private void actualizarEstado(int idVehiculo){
        String[] projection = {MantenimientosContentProvider.ID_MANTENIMIENTO,MantenimientosContentProvider.ESTADO_REPARACION};
        String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?";
        String[] whereArgs = {Integer.toString(idVehiculo)};
        String sortOrder = null;

        // Query URI
        Uri queryUri = MantenimientosContentProvider.CONTENT_URI;

        // Create the new Cursor loader.
        Cursor cursor = getContentResolver().query(queryUri, projection, where, whereArgs, sortOrder);

        ContentValues contentValuesVehiculo = new ContentValues();

        if (cursor.getCount() == 0){
            //ya no hay mas mantenimientos
            contentValuesVehiculo.put(VehiculoContentProvider.ESTADO,getString(R.string.fa_check));
        }else {
            while (cursor.moveToNext()){
                //Si hay mantenimientos programados
                String estado = cursor.getString(cursor.getColumnIndex(MantenimientosContentProvider.ESTADO_REPARACION));
                String fa_square = getString(R.string.fa_wrench);
                contentValuesVehiculo.put(VehiculoContentProvider.ESTADO,fa_square);

                String fa_triangle = getString(R.string.fa_exclamation_triangle);
                if (estado.hashCode() == fa_triangle.hashCode()){
                    //si hay mantenimiento pendientes
                    contentValuesVehiculo.put(VehiculoContentProvider.ESTADO,fa_triangle);
                    break;
                }
            }
        }

        Uri uriVehiculos = Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getVehiculo().getId()));
        getContentResolver().update(uriVehiculos,contentValuesVehiculo,null,null);
    }
}
