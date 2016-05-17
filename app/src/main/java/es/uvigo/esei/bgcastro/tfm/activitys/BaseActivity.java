package es.uvigo.esei.bgcastro.tfm.activitys;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import es.uvigo.esei.bgcastro.tfm.R;

/**
 * Created by braisgallegocastro on 10/2/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    //carga de la fuente para iconos
    private static Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:{
                abrirOpciones();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void abrirOpciones() {
        Log.d(TAG, "abrirOpciones: ");

        Intent intentPreferencias = new Intent(BaseActivity.this, PreferencesActivity.class);
        startActivity(intentPreferencias);
    }
}
