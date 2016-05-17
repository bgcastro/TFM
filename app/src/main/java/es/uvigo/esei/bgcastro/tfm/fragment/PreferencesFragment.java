package es.uvigo.esei.bgcastro.tfm.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.preferences.VehiculosPreferences;

/**
 * Created by braisgallegocastro on 17/5/16.
 */
public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(VehiculosPreferences.PREFERENCES_FILE);

        addPreferencesFromResource(R.xml.preferences_screen);
    }
}