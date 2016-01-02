package es.uvigo.esei.bgcastro.tfm.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculoBDD;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.R;

public class GestionVehiculosActivity extends AppCompatActivity{
    private static final String TAG = "GesVehiculosActivity";
    private Vehiculo vehiculo;

    private ImageView imagenVehiculo;
    private EditText editTextMarca;
    private EditText editTextModelo;
    private EditText editTextMatricula;
    private EditText editTextKilometraje;
    private EditText editTextCombustible;
    private EditText editTextCilindrada;
    private ImageView selectorDeColor;
    private EditText editTextPotencia;
    private EditText editTextAnho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_vehiculos);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarGestionVehiculos);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagenVehiculo = (ImageView) findViewById(R.id.imagenGestionVehiculos);
        editTextMarca = (EditText) findViewById(R.id.editTextMarca);
        editTextModelo = (EditText) findViewById(R.id.editTextModelo);
        editTextMatricula = (EditText) findViewById(R.id.editTextMatricula);
        editTextKilometraje = (EditText) findViewById(R.id.editTextKilometraje);
        editTextCombustible = (EditText) findViewById(R.id.editTextCombustible);
        editTextCilindrada = (EditText) findViewById(R.id.editTextCilindrada);
        selectorDeColor = (ImageView) findViewById(R.id.selectorDeColor);
        editTextPotencia = (EditText) findViewById(R.id.editTextPotencia);
        editTextAnho = (EditText) findViewById(R.id.editTextAnho);

        imagenVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imagenVehiculo");
            }
        });

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

            case R.id.action_settings:{
                abrirOpciones();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    
    private void nuevoVehiculo(){
        //TODO Revisar argumentos

        float kilometraje;
        float potencia;
        int cilindrada;

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

        vehiculo = new Vehiculo(new byte[0],
                editTextMarca.getText().toString(),
                editTextModelo.getText().toString(),
                editTextMatricula.getText().toString(),
                kilometraje,
                editTextCombustible.getText().toString(),
                cilindrada,
                potencia,
                "color",
                new Date(),
                "sin estado");

        Log.d(TAG, vehiculo.toString());

        //guardamos en la BBDD
        VehiculoBDD bdd = new VehiculoBDD(this);
        bdd .openForWriting();
        bdd.insertVehiculo(vehiculo);
        bdd.close();
    }

    private void abrirOpciones() {
        Log.d(TAG, "abrirOpciones: ");
    }
    
}
