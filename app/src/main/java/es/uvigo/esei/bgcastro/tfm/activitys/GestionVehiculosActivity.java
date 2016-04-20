package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculoDAO;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.dialog.ColorPickerDialog;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.OnClickListener;
import static android.view.View.OnFocusChangeListener;

public class GestionVehiculosActivity extends BaseActivity implements ColorPickerDialog.NoticeDialogListener{
    private static final String TAG = "GesVehiculosActivity";
    private static final int TOMAR_FOTO_REQUEST = 1;
    private byte[] foto = new byte[0];
    private Vehiculo vehiculo;

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

    private ImageButton botonMantenimientos;

    private ArrayAdapter spinnerAdapter;

    private Intent intent;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnFocusChangeListener focusChangeListenerCambios;
        
        intent = getIntent();

        setContentView(R.layout.activity_gestion_vehiculos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionVehiculos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        botonMantenimientos = (ImageButton) findViewById(R.id.botonMantenimientos);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.tipos_combustible, R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(spinnerAdapter);

        imagenVehiculo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imagenVehiculo");

                tomarFoto();
            }
        });

        botonMantenimientos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: botonMantenimientos");

                showMantenimientos();
            }
        });

        selectorDeColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick selectorDeColor: color" + color);
                ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstace(color); // new ColorPickerDialog();

                Log.d(TAG, "onClick: selectorDeColor");

                colorPickerDialog.show(getFragmentManager(),"colorPickerDialog");
            }
        });
        
        if (intent.hasExtra(VehiculosActivity.VEHICULO)){
            
            vehiculo = intent.getParcelableExtra(VehiculosActivity.VEHICULO);

            rellenarUI(vehiculo);
            focusChangeListenerCambios = new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        modificarVehiculo(v);
                    }
                }
            };

            editTextMarca.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextModelo.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextMatricula.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextKilometraje.setOnFocusChangeListener(focusChangeListenerCambios);
            //spinnerCombustible.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextCilindrada.setOnFocusChangeListener(focusChangeListenerCambios);
            //selectorDeColor.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextPotencia.setOnFocusChangeListener(focusChangeListenerCambios);
            editTextAnho.setOnFocusChangeListener(focusChangeListenerCambios);

            spinnerCombustible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.d(TAG, "onItemSelected: position " + position);
                    modificarVehiculo(parent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

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
                nuevoVehiculo();
                return true;
            }

            case R.id.action_remove_vehiculo: {
                removeVehiculo(vehiculo.getId());
                return true;
            }

            default: {
                return false;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (vehiculo != null){
            //si el vehiculo se modifica
            menu.removeItem(R.id.action_add_vehiculo);
        }else {
            //si el vehiculo se añade
            menu.removeItem(R.id.action_remove_vehiculo);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState: " + vehiculo);
        
        outState.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(VehiculosActivity.VEHICULO, vehiculo);
        Log.d(TAG, "onRestoreInstanceState: " + vehiculo);
    }

    private void nuevoVehiculo(){
        //TODO Revisar argumentos

        float kilometraje;
        float potencia;
        int cilindrada;
        int anho;

        if (editTextKilometraje.getText().toString().isEmpty()){
            kilometraje = 0;
        }else {
            kilometraje = Float.parseFloat(editTextKilometraje.getText().toString());
        }

        if (editTextPotencia.getText().toString().isEmpty()) {
            potencia = 0;
        }else {
            potencia = Float.parseFloat(editTextPotencia.getText().toString());
        }

        if (editTextCilindrada.getText().toString().isEmpty()){
            cilindrada = 0;
        }else{
            cilindrada = Integer.parseInt(editTextCilindrada.getText().toString());
        }

        if (editTextAnho.getText().toString().isEmpty()){
            anho = 0;
        }else{
            anho = Integer.parseInt(editTextAnho.getText().toString());
        }

        if (foto.length == 0){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap bitmap = ((BitmapDrawable)imagenVehiculo.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, DRAWING_CACHE_QUALITY_AUTO, stream);
            foto = stream.toByteArray();
        }

        vehiculo = new Vehiculo(foto, editTextMarca.getText().toString(),
                editTextModelo.getText().toString(),
                editTextMatricula.getText().toString(),
                kilometraje,
                (String) spinnerCombustible.getSelectedItem(),
                cilindrada,
                potencia,
                color,
                anho,
                getString(R.string.fa_wrench));

        Log.d(TAG, "nuevoVehiculo" + vehiculo.toString());

        //// TODO: 4/1/16 otro hilo
        //guardamos en la BBDD
        VehiculoDAO bdd = new VehiculoDAO(this);
        bdd .openForWriting();
        long idVehiculo = bdd.insertVehiculo(vehiculo);
        vehiculo.setId((int) idVehiculo);
        bdd.close();
    }

    private void modificarVehiculo(View v) {
        //TODO Revisar argumentos
        //creamos el vehiculo si no existe
        // if (vehiculo == null) {
        //     vehiculo = new Vehiculo();
        // }

        switch (v.getId()){
            case R.id.imagenGestionVehiculos:
                //TODO ver como utilizar imagenes
                vehiculo.setImagenVehiculo(foto);
                break;

            case R.id.editTextMarca:
                vehiculo.setMarca(editTextMarca.getText().toString());
                break;

            case R.id.editTextModelo:
                vehiculo.setModelo(editTextModelo.getText().toString());
                break;

            case R.id.editTextMatricula:
                vehiculo.setMatricula(editTextMatricula.getText().toString());
                break;

            case R.id.editTextKilometraje:
                float kilometraje;

                if (editTextKilometraje.getText().toString().isEmpty()){
                    kilometraje = 0;
                }else {
                    kilometraje = Float.parseFloat(editTextKilometraje.getText().toString());
                }
                vehiculo.setKilometraje(kilometraje);
                break;

            case R.id.tipoCombustible:
                vehiculo.setCombustible((String) spinnerCombustible.getSelectedItem());
                Log.d(TAG, "modificarVehiculo: "+ spinnerCombustible.getSelectedItem());
                break;

            case R.id.editTextCilindrada:
                int cilindrada;

                if (editTextCilindrada.getText().toString().isEmpty()){
                    cilindrada = 0;
                }else{
                    cilindrada = Integer.parseInt(editTextCilindrada.getText().toString());
                }
                vehiculo.setCilindrada(cilindrada);
                break;

            case R.id.selectorDeColor:
                //TODO revisar lo de selector de color
                vehiculo.setColor(color);
                break;

            case R.id.editTextPotencia:
                float potencia;

                if (editTextPotencia.getText().toString().isEmpty()) {
                    potencia = 0;
                }else {
                    potencia = Float.parseFloat(editTextPotencia.getText().toString());
                }
                vehiculo.setPotencia(potencia);
                break;

            case R.id.editTextAnho:
                int anho;

                if (editTextAnho.getText().toString().isEmpty()){
                    anho = 0;
                }else{
                    anho = Integer.parseInt(editTextAnho.getText().toString());
                }
                vehiculo.setAño(anho);
                break;

        }

        Log.d(TAG, "modificarVehiculo: " + vehiculo.toString());

        //// TODO: 4/1/16 otro hilo
        //guardamos en la BBDD
        VehiculoDAO bdd = new VehiculoDAO(this);
        bdd .openForWriting();
        int bdOutput = bdd.updateVehiculo(vehiculo.getId(),vehiculo);
        bdd.close();

        Log.d(TAG, "modificarVehiculo: bd output " + bdOutput);
    }

    private void removeVehiculo(int id) {
        Log.d(TAG, "removeVehiculo: " + id);

        //// TODO: 4/1/16 otro hilo
        //guardamos en la BBDD
        VehiculoDAO bdd = new VehiculoDAO(this);
        bdd .openForWriting();
        bdd.removeVehiculo(id);
        bdd.close();

        finish();
    }

    private void rellenarUI(Vehiculo vehiculo) {
        foto = vehiculo.getImagenVehiculo();
        this.imagenVehiculo.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
        this.editTextMarca.setText(vehiculo.getMarca());
        this.editTextModelo.setText(vehiculo.getModelo());
        this.editTextMatricula.setText(vehiculo.getMatricula());
        this.editTextKilometraje.setText(Float.toString(vehiculo.getKilometraje()));
        this.spinnerCombustible.setSelection( new ArrayList<String>(Arrays.asList( getResources().getStringArray(R.array.tipos_combustible))).indexOf(vehiculo.getCombustible()));
        this.editTextCilindrada.setText(Integer.toString(vehiculo.getCilindrada()));
        //// TODO: 4/1/16 revisar el color
        color = vehiculo.getColor();
        this.selectorDeColor.setBackgroundColor(color);
        this.editTextPotencia.setText(Float.toString(vehiculo.getPotencia()));
        this.editTextAnho.setText(Integer.toString(vehiculo.getAño()));
    }

    private void tomarFoto() {
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentFoto, TOMAR_FOTO_REQUEST);
    }

    private void showMantenimientos() {
        Log.d(TAG, "showMantenimientos: ");
        Intent intentMantenimientos = new Intent(GestionVehiculosActivity.this,MantenimientosActivity.class);

        intentMantenimientos.putExtra(VehiculosActivity.VEHICULO,vehiculo);

        startActivity(intentMantenimientos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == TOMAR_FOTO_REQUEST && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: se ha tomado la foto");

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imagenVehiculo.setImageBitmap(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, DRAWING_CACHE_QUALITY_AUTO, stream);

            foto = stream.toByteArray();

            //comprobamos que tenemos creado un vehiculo recibido por el intent
            if (vehiculo != null) {
                modificarVehiculo(imagenVehiculo);
            }

            //vehiculo.setImagenVehiculo(stream.toByteArray());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPositiveButton(ColorPickerDialog dialog) {
        color = dialog.getSelectedColor();
        Log.d(TAG, "setPositiveButton: color" + color);

        selectorDeColor.setBackgroundColor(color);

        if (vehiculo != null) {
            modificarVehiculo(selectorDeColor);
        }
    }

    @Override
    public void setNegativeButton(ColorPickerDialog dialog) {

    }
}
