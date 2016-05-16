package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.dialog.ColorPickerDialog;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.OnClickListener;
import static android.view.View.OnFocusChangeListener;

public class GestionVehiculosActivity extends BaseActivity implements ColorPickerDialog.NoticeDialogListener{
    private static final String TAG = "GesVehiculosActivity";
    private static final int TOMAR_FOTO_REQUEST = 1;
    private static final String UPDATE_DATE = "update_date";
    private byte[] foto = new byte[0];
    private Vehiculo vehiculo;

    private Date updateDate;

    private ImageView imagenVehiculo;
    private EditText editTextMarca;
    private EditText editTextModelo;
    private EditText editTextMatricula;
    private EditText editTextKilometraje;
    private Spinner spinnerCombustible;
    private EditText editTextCilindrada;
    private SurfaceView selectorDeColor;
    private EditText editTextPotencia;
    private EditText editTextAnho;

    private boolean edicionActivada = true;

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnFocusChangeListener focusChangeListenerCambios;

        Intent intent = getIntent();

        if (intent != null) {
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);
        }
        //si no existe lo creamos
        if (vehiculo == null) {
            vehiculo = new Vehiculo();
        }

        //Inflamos el layout
        setContentView(R.layout.activity_gestion_vehiculos);

        //Asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionVehiculos);
        actionBar.setTitle(getString(R.string.titulo_toolbar_gestion_vehiculos_activity));
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //asociamos los elementos de la vista
        imagenVehiculo = (ImageView) findViewById(R.id.imagenGestionVehiculos);
        editTextMarca = (EditText) findViewById(R.id.editTextMarca);
        editTextModelo = (EditText) findViewById(R.id.editTextModelo);
        editTextMatricula = (EditText) findViewById(R.id.editTextMatricula);
        editTextKilometraje = (EditText) findViewById(R.id.editTextKilometraje);
        spinnerCombustible = (Spinner) findViewById(R.id.tipoCombustible);
        editTextCilindrada = (EditText) findViewById(R.id.editTextCilindrada);
        selectorDeColor = (SurfaceView) findViewById(R.id.selectorDeColor);
        editTextPotencia = (EditText) findViewById(R.id.editTextPotencia);
        editTextAnho = (EditText) findViewById(R.id.editTextAnho);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.tipos_combustible, R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(spinnerAdapter);

        //on click para tomar una foto
        imagenVehiculo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imagenVehiculo");
                tomarFoto();
            }
        });

        //on click para seleccionar un color
        selectorDeColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick selectorDeColor: color" + color);
                ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstace(color);
                colorPickerDialog.show(getFragmentManager(),"colorPickerDialog");
            }
        });

        TextView allMantenimientos = (TextView) findViewById(R.id.todosMantenimientos);

        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        allMantenimientos.setTypeface(font);
        allMantenimientos.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

        allMantenimientos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: todos los mantenimientos");

                if (vehiculo.getId() < 0){
                    Toast.makeText(getApplicationContext(),R.string.vehiculo_no_creado,Toast.LENGTH_LONG).show();
                }else {
                    showMantenimientos();
                }
            }
        });

        if (intent.hasExtra(VehiculosActivity.VEHICULO)){
            //Rellenamos con los valores que tiene el vehiculo existente
            rellenarUI(vehiculo);

            //desactivamos la edicion
            desactivarEdicion();
        }

      /*  if (savedInstanceState != null) {
            updateDate = (Date) savedInstanceState.getSerializable(UPDATE_DATE);
        }

        if (updateDate == null || updateDate.getDay() != GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_MONTH)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_message);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "onClick: updateDate");
                    updateKilometraje(10);
                }
            });

            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}});

            AlertDialog dialog = builder.create();
            dialog.show();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestion_vehiculos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_vehiculo:{
                if (vehiculo != null && vehiculo.getId() != -1) {
                    modificarVehiculo();
                }else {
                    nuevoVehiculo();
                }
                return true;
            }

            case R.id.action_remove_vehiculo: {
                removeVehiculo(vehiculo.getId());
                return true;
            }

            case R.id.action_modify_vehiculo:
                activarEdicion();
                invalidateOptionsMenu();
                return true;

            default: {
                return false;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!edicionActivada){
            //si el vehiculo se modifica
            menu.removeItem(R.id.action_add_vehiculo);
        }else {
            //si el vehiculo se añade
            menu.removeItem(R.id.action_remove_vehiculo);
            menu.removeItem(R.id.action_modify_vehiculo);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState: " + vehiculo);
        
        outState.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
        outState.putSerializable(UPDATE_DATE,updateDate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        vehiculo = savedInstanceState.getParcelable(VehiculosActivity.VEHICULO);
        Log.d(TAG, "onRestoreInstanceState: " + vehiculo);

        updateDate = (Date) savedInstanceState.getSerializable(UPDATE_DATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Se ha tomado la foto correctamente
        if(requestCode == TOMAR_FOTO_REQUEST && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: se ha tomado la foto");

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imagenVehiculo.setImageBitmap(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, DRAWING_CACHE_QUALITY_AUTO, stream);

            foto = stream.toByteArray();
            vehiculo.setImagenVehiculo(foto);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPositiveButton(ColorPickerDialog dialog) {
        //Se ha seleccionado un nuevo color
        color = dialog.getSelectedColor();
        Log.d(TAG, "setPositiveButton: color" + color);

        selectorDeColor.setBackgroundColor(color);
        vehiculo.setColor(color);
    }

    @Override
    public void setNegativeButton(ColorPickerDialog dialog) {}

    private void rellenarUI(Vehiculo vehiculo) {
        foto = vehiculo.getImagenVehiculo();
        this.imagenVehiculo.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
        this.editTextMarca.setText(vehiculo.getMarca());
        this.editTextModelo.setText(vehiculo.getModelo());
        this.editTextMatricula.setText(vehiculo.getMatricula());
        this.editTextKilometraje.setText(Float.toString(vehiculo.getKilometraje()));
        this.spinnerCombustible.setSelection( new ArrayList<String>(Arrays.asList( getResources().getStringArray(R.array.tipos_combustible))).indexOf(vehiculo.getCombustible()));
        this.editTextCilindrada.setText(Integer.toString(vehiculo.getCilindrada()));
        color = vehiculo.getColor();
        this.selectorDeColor.setBackgroundColor(color);
        this.editTextPotencia.setText(Float.toString(vehiculo.getPotencia()));
        this.editTextAnho.setText(Integer.toString(vehiculo.getAño()));
    }

    private void nuevoVehiculo(){
        if (uiToVehiculo()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VehiculoContentProvider.IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
            contentValues.put(VehiculoContentProvider.MARCA, vehiculo.getMarca());
            contentValues.put(VehiculoContentProvider.MODELO, vehiculo.getModelo());
            contentValues.put(VehiculoContentProvider.MATRICULA, vehiculo.getMatricula());
            contentValues.put(VehiculoContentProvider.KILOMETRAJE, vehiculo.getKilometraje());
            contentValues.put(VehiculoContentProvider.COMBUSTIBLE, vehiculo.getCombustible());
            contentValues.put(VehiculoContentProvider.CILINDRADA, vehiculo.getCilindrada());
            contentValues.put(VehiculoContentProvider.POTENCIA, vehiculo.getPotencia());
            contentValues.put(VehiculoContentProvider.COLOR, vehiculo.getColor());
            contentValues.put(VehiculoContentProvider.ANHO, vehiculo.getAño());
            contentValues.put(VehiculoContentProvider.ESTADO, vehiculo.getEstado());

            //guardamos el vehiculo
            Uri uri = getContentResolver().insert(VehiculoContentProvider.CONTENT_URI,contentValues);
            String idNuevoVehiculo = uri.getLastPathSegment();
            if (!idNuevoVehiculo.isEmpty()){
                vehiculo.setId(Integer.parseInt(uri.getLastPathSegment()));

                //Cambiamos el menu
                invalidateOptionsMenu();

                //desactivamos la edicion
                desactivarEdicion();

                Log.d(TAG, "nuevoVehiculo" + vehiculo.toString());
            }
        }
    }

    private void modificarVehiculo() {

        if (uiToVehiculo()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VehiculoContentProvider.IMAGEN_VEHICULO, vehiculo.getImagenVehiculo());
            contentValues.put(VehiculoContentProvider.MARCA, vehiculo.getMarca());
            contentValues.put(VehiculoContentProvider.MODELO, vehiculo.getModelo());
            contentValues.put(VehiculoContentProvider.MATRICULA, vehiculo.getMatricula());
            contentValues.put(VehiculoContentProvider.KILOMETRAJE, vehiculo.getKilometraje());
            contentValues.put(VehiculoContentProvider.COMBUSTIBLE, vehiculo.getCombustible());
            contentValues.put(VehiculoContentProvider.CILINDRADA, vehiculo.getCilindrada());
            contentValues.put(VehiculoContentProvider.POTENCIA, vehiculo.getPotencia());
            contentValues.put(VehiculoContentProvider.COLOR, vehiculo.getColor());
            contentValues.put(VehiculoContentProvider.ANHO, vehiculo.getAño());
            contentValues.put(VehiculoContentProvider.ESTADO, vehiculo.getEstado());

            String updateID = Integer.toString(vehiculo.getId());

            //actualizamos el vehiculo en la BD
            int resultado = getContentResolver().update(Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI, updateID), contentValues, null, null);

            //desabilitamos la edicion
            desactivarEdicion();

            Log.d(TAG, "modificarVehiculo: bd output " + resultado);
        }
    }

    private void removeVehiculo(int id) {
        Log.d(TAG, "removeVehiculo: " + id);

        String deleteID = Integer.toString(id);
        getContentResolver().delete( Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI,deleteID), null, null);

        finish();
    }

    private void tomarFoto() {
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentFoto, TOMAR_FOTO_REQUEST);
    }

    private boolean uiToVehiculo(){
        String nombre = editTextModelo.getText().toString();
        String marca = editTextMarca.getText().toString();
        String kilometraje = editTextKilometraje.getText().toString();
        String potencia = editTextPotencia.getText().toString();
        String cilidranda = editTextCilindrada.getText().toString();
        String anho = editTextAnho.getText().toString();
        boolean success = true;

        vehiculo.setMatricula(editTextMatricula.getText().toString());
        vehiculo.setEstado(getString(R.string.fa_check));

        //comprobamos que el nombre no sea vacio
        if (nombre.isEmpty()){
            editTextModelo.setError(getString(R.string.error_modelo_vacio));
            success = false;
        }else{
            vehiculo.setModelo(nombre);
        }

        //comprobamos que la marca no sea vacia
        if (marca.isEmpty()){
            editTextMarca.setError(getString(R.string.error_marca_vacio));
            success = false;
        }else {
            vehiculo.setMarca(marca);
        }

        //comprobamos que el kilometraje no sea vacio
        if (kilometraje.isEmpty()){
            editTextKilometraje.setError(getString(R.string.error_kilometraje_vacio));
            success = false;
        }else {
            vehiculo.setKilometraje(Float.parseFloat(kilometraje));
        }

        if (potencia.isEmpty()) {
            vehiculo.setPotencia(0);
        }else {
            vehiculo.setPotencia(Float.parseFloat(editTextPotencia.getText().toString()));
        }

        if (cilidranda.isEmpty()){
            vehiculo.setCilindrada(0);
        }else{
            vehiculo.setCilindrada(Integer.parseInt(cilidranda));
        }

        if (anho.isEmpty()){
            vehiculo.setAño(0);
        }else{
            vehiculo.setAño(Integer.parseInt(anho));
        }

        vehiculo.setCombustible((String) spinnerCombustible.getSelectedItem());

        if (foto.length == 0){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap bitmap = ((BitmapDrawable)imagenVehiculo.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, DRAWING_CACHE_QUALITY_AUTO, stream);
            foto = stream.toByteArray();
            vehiculo.setImagenVehiculo(foto);
        }

        return success;
    }

    private void desactivarEdicion(){
        edicionActivada = false;

        imagenVehiculo.setClickable(false);
        editTextMarca.setEnabled(false);
        editTextModelo.setEnabled(false);
        editTextMatricula.setEnabled(false);
        editTextKilometraje.setEnabled(false);
        spinnerCombustible.setEnabled(false);
        editTextCilindrada.setEnabled(false);
        selectorDeColor.setEnabled(false);
        editTextPotencia.setEnabled(false);
        editTextAnho.setEnabled(false);
    }

    private void activarEdicion(){
        edicionActivada = true;

        imagenVehiculo.setClickable(true);
        editTextMarca.setEnabled(true);
        editTextModelo.setEnabled(true);
        editTextMatricula.setEnabled(true);
        editTextKilometraje.setEnabled(true);
        spinnerCombustible.setEnabled(true);
        editTextCilindrada.setEnabled(true);
        selectorDeColor.setEnabled(true);
        editTextPotencia.setEnabled(true);
        editTextAnho.setEnabled(true);
    }

    private void showMantenimientos() {
        Log.d(TAG, "showMantenimientos: ");
        Intent intentMantenimientos = new Intent(GestionVehiculosActivity.this,MantenimientosActivity.class);

        intentMantenimientos.putExtra(VehiculosActivity.VEHICULO,vehiculo);

        startActivity(intentMantenimientos);
    }

    private void updateKilometraje(float nuevoKilometraje){
        String updateID = Integer.toString(vehiculo.getId());

        ContentValues contentValues = new ContentValues();
        contentValues.put(VehiculoContentProvider.KILOMETRAJE, nuevoKilometraje);

        int resultado = getContentResolver().update(Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI,updateID),contentValues,null,null);

        if (resultado > 0){
            updateDate = new Date(System.currentTimeMillis());
        }
    }
}
