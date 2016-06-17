package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.background_tasks.UploadOpinionTask;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.ReparacionesContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.dialog.OpinionDialog;
import es.uvigo.esei.bgcastro.tfm.app.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.app.entities.Reparacion;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 9/5/16.
 */
public class GestionReparacionesActivity extends BaseActivity implements OpinionDialog.NoticeDialogListener{
    private static final String TAG = "GestionReparActivity";
    private Mantenimiento mantenimiento;

    private Reparacion reparacion;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

    private EditText editTextReparacion;
    private EditText editTextDescripcionReparacion;
    private EditText editTextReferencia;
    private EditText editTextTaller;
    private EditText editTextPrecio;

    private boolean edicionActivada = true;

    public static final String REPARACION = "reparacion";

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
            reparacion = intent.getParcelableExtra(REPARACION);
            Log.d(TAG, "onCreate: mantenimiento");
        }
        if (reparacion == null) {
            reparacion = new Reparacion();
        }

        editTextReparacion = (EditText) findViewById(R.id.editTextReparacion);
        editTextDescripcionReparacion = (EditText) findViewById(R.id.editTextDescripcionReparacion);
        editTextReferencia = (EditText) findViewById(R.id.editTextReferencia);
        editTextTaller = (EditText) findViewById(R.id.editTextTaller);
        editTextPrecio = (EditText) findViewById(R.id.editTextPrecio);

        if (intent.hasExtra(REPARACION)){
            //Rellenar UI
            rellenarUI();

            //desactivamos la edicion
            desactivarEdicion();
        }

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
                if (reparacion != null && reparacion.getId() != -1) {
                    modificarReparacion();
                }else {
                    nuevaReparacion();
                }
                return true;
            }

            case R.id.action_modify_reparacion: {
                activarEdicion();
                invalidateOptionsMenu();
                return true;
            }

            case R.id.action_remove_reparacion: {
                eliminarReparacion();
                return true;
            }

            case android.R.id.home: {
                this.onBackPressed();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!edicionActivada){
            //si la reparacion se modifica
            menu.removeItem(R.id.action_nueva_reparacion);
        }else {
            //si la reparacion se añade
            menu.removeItem(R.id.action_remove_reparacion);
            menu.removeItem(R.id.action_modify_reparacion);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setPositiveButton(OpinionDialog dialog) {
        Opinion opinion = new Opinion(dialog.getPuntuacion(), reparacion.getPrecio(), reparacion.getTaller(), dialog.getComentario());

        Log.d(TAG, "setPositiveButton: opinion" + opinion);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);
            String address = preferences.getString(VehiculosPreferences.SERVER_ADDRESS, VehiculosPreferences.SERVER_ADDRESS_DEFAULT);
            int port = preferences.getInt(VehiculosPreferences.SERVER_PORT, VehiculosPreferences.SERVER_PORT_DEFAULT);

            UploadOpinionTask uploadOpinion = new UploadOpinionTask(getApplicationContext(), address, port);
            uploadOpinion.execute(opinion);
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setNegativeButton(OpinionDialog dialog) {

    }

    private boolean uiToReparacion(){
        boolean success = true;
        String nombreReparacion = editTextReparacion.getText().toString();
        String descripcionReparacion = editTextDescripcionReparacion.getText().toString();
        String referencia = editTextReferencia.getText().toString();
        String taller = editTextTaller.getText().toString();
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

        if (!taller.isEmpty()){
            reparacion.setTaller(taller);
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
            contentValues.put(ReparacionesContentProvider.TALLER, reparacion.getTaller());
            contentValues.put(ReparacionesContentProvider.PRECIO, reparacion.getPrecio());
            contentValues.put(ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION, reparacion.getMantenimiento().getId());

            //guardamos la reparacion
            Uri uri = getContentResolver().insert(ReparacionesContentProvider.CONTENT_URI,contentValues);

            reparacion.setId(Integer.parseInt(uri.getLastPathSegment()));

            //actualizamos el estado del mantenimiento y del vehiculo
            actualizarEstado();

            //Cambiamos el menu
            invalidateOptionsMenu();

            //desactivamos la edicion
            desactivarEdicion();

            SharedPreferences preferences = getSharedPreferences(VehiculosPreferences.PREFERENCES_FILE,MODE_PRIVATE);

            if (preferences.getBoolean(VehiculosPreferences.ALERT_COMENTAR, VehiculosPreferences.ALERT_COMENTAR_DEFAULT) && !reparacion.getTaller().isEmpty()){
                OpinionDialog dialog = OpinionDialog.newInstace();
                dialog.show(getFragmentManager(), OpinionDialog.fragmentTag );
            }
        }
    }

    private void modificarReparacion(){
        //si los datos introducidos son validos podemos guardar la reparacion
        if (uiToReparacion() && reparacion.getId() == -1){
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReparacionesContentProvider.NOMBRE_REPARACION, reparacion.getNombreReparacion());
            contentValues.put(ReparacionesContentProvider.DESCRIPCION_REPARACION, reparacion.getDescripcion());
            contentValues.put(ReparacionesContentProvider.REFERENCIA, reparacion.getReferencia());
            contentValues.put(ReparacionesContentProvider.TALLER, reparacion.getTaller());
            contentValues.put(ReparacionesContentProvider.PRECIO, reparacion.getPrecio());
            contentValues.put(ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION, reparacion.getMantenimiento().getId());

            //guardamos la reparacion
            String updateID = Integer.toString(reparacion.getId());

            getContentResolver().update(Uri.withAppendedPath(ReparacionesContentProvider.CONTENT_URI,updateID),contentValues,null,null);

            //actualizamos el estado del mantenimiento y del vehiculo
            actualizarEstado();

            //Cambiamos el menu
            invalidateOptionsMenu();

            //desactivamos la edicion
            desactivarEdicion();
        }
    }

    private void eliminarReparacion(){
        String deleteID = Integer.toString(reparacion.getId());
        getContentResolver().delete( Uri.withAppendedPath(ReparacionesContentProvider.CONTENT_URI,deleteID), null, null);

        Log.d(TAG, "eliminarReparacion: " + deleteID);

        actualizarEstadoBorrado();

        finish();
    }

    private void actualizarEstado(){
        ContentValues contentValuesMantenimientos = new ContentValues();
        contentValuesMantenimientos.put(MantenimientosContentProvider.ESTADO_REPARACION,getString(R.string.fa_check));
        contentValuesMantenimientos.put(MantenimientosContentProvider.FECHA, simpleDateFormat.format(new Date()));

        Uri uriMantenimiento = Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getId()));
        getContentResolver().update(uriMantenimiento,contentValuesMantenimientos,null,null);

        int idVehiculo = mantenimiento.getVehiculo().getId();
        String[] projection = {MantenimientosContentProvider.ID_MANTENIMIENTO,MantenimientosContentProvider.ESTADO_REPARACION};
        String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?";
        String[] whereArgs = {Integer.toString(idVehiculo)};
        String sortOrder = null;

        // Query URI
        Uri queryUri = MantenimientosContentProvider.CONTENT_URI;

        //Buscamos todos los mantenimientos del vehiculo para ver si se puede cambiar el estado
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

    private void actualizarEstadoBorrado(){
        ContentValues contentValuesMantenimientos = new ContentValues();
        ContentValues contentValuesVehiculo = new ContentValues();
        //Si el estado es que necesita reparacion lo dejamos tal y como está y sino revisamos el nuevo estado
        if (mantenimiento.getEstado().hashCode() != getString(R.string.fa_exclamation_triangle).hashCode() ) {
            int idMantenimiento = mantenimiento.getId();
            String[] projection = {ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION,ReparacionesContentProvider.ID_REPARACION};
            String where = ReparacionesContentProvider.ID_MANTENIMIENTO_REPARACION + "=" + "?";
            String[] whereArgs = {Integer.toString(idMantenimiento)};
            String sortOrder = null;

            // Query URI
            Uri queryUri = ReparacionesContentProvider.CONTENT_URI;

            //Buscamos todos las reparaciones del vehiculo para ver si se puede cambiar el estado
            Cursor cursor = getContentResolver().query(queryUri, projection, where, whereArgs, sortOrder);

            //Si era la último reparacion
            if (cursor.getCount() == 0){
                //Si la reparacion ya se deberia haber hecho le cambiamos al estado "necesario reparar" sino cambiamos a "programado"
                if (new DateTime(mantenimiento.getFecha()).isBeforeNow() ||
                        (mantenimiento.getVehiculo().getKilometraje() - mantenimiento.getKilometrajeReparacion() > 0)) {
                    contentValuesMantenimientos.put(MantenimientosContentProvider.ESTADO_REPARACION, getString(R.string.fa_exclamation_triangle));
                    contentValuesVehiculo.put(VehiculoContentProvider.ESTADO, getString(R.string.fa_exclamation_triangle));

                } else {
                    contentValuesMantenimientos.put(MantenimientosContentProvider.ESTADO_REPARACION, getString(R.string.fa_square_o));
                    contentValuesVehiculo.put(VehiculoContentProvider.ESTADO, getString(R.string.fa_wrench));
                }

                String updateID = Integer.toString(mantenimiento.getId());
                getContentResolver().update(Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI, updateID), contentValuesMantenimientos, null, null);

                String updateIDvehiculo = Integer.toString(mantenimiento.getVehiculo().getId());
                getContentResolver().update(Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI, updateIDvehiculo), contentValuesVehiculo, null, null);

            }
        }
    }

    private void desactivarEdicion() {
        edicionActivada = false;

        editTextReparacion.setEnabled(false);
        editTextDescripcionReparacion.setEnabled(false);
        editTextReferencia.setEnabled(false);
        editTextTaller.setEnabled(false);
        editTextPrecio.setEnabled(false);
    }

    private void activarEdicion() {
        edicionActivada = true;

        editTextReparacion.setEnabled(true);
        editTextDescripcionReparacion.setEnabled(true);
        editTextReferencia.setEnabled(true);
        editTextTaller.setEnabled(true);
        editTextPrecio.setEnabled(true);
    }

    private void rellenarUI() {
        editTextReparacion.setText(reparacion.getNombreReparacion());
        editTextDescripcionReparacion.setText(reparacion.getDescripcion());
        editTextReferencia.setText(reparacion.getReferencia());
        editTextTaller.setText(reparacion.getTaller());
        editTextPrecio.setText(Float.toString(reparacion.getPrecio()));
    }

}
