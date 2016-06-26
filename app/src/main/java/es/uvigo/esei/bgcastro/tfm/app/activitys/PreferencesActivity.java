package es.uvigo.esei.bgcastro.tfm.app.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.fragment.PreferencesFragment;

/**
 * Created by braisgallegocastro on 17/5/16.
 * Activity para cambiar las preferencias
 */
public class PreferencesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        //Asociamos la toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbarPreferences);
        actionBar.setTitle(getString(R.string.toolbarPreferences));

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Fragmento con las interfaz de las preferencias
        getFragmentManager().beginTransaction().replace(R.id.contentPreferencias, new PreferencesFragment()).commit();
    }

    /**
     * LLamado cuando se pulsa un item
     *
     * @param item Item seleccionado
     * @return true si se ha atendido la accion.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Boton atras
        if (item.getItemId() == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
