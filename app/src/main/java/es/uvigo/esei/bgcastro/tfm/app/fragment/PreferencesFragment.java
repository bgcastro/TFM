package es.uvigo.esei.bgcastro.tfm.app.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 17/5/16.
 * Fragment para la gestion de las preferencias de usuario
 */
public class PreferencesFragment extends PreferenceFragment {
    /**
     * Metodo que reune la logica principal
     *
     * @param savedInstanceState Estado guardado
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(VehiculosPreferences.PREFERENCES_FILE);

        addPreferencesFromResource(R.xml.preferences_screen);
    }
}