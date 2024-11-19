package xxx.solzer.dlsbot.ui;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.R;
import android.os.Bundle;
import androidx.core.content.SharedPreferencesCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import xxx.solzer.dlsbot.modules.BountyGround;
import xxx.solzer.dlsbot.modules.WaterWar;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        for (Module module : App.modules.getModules()) {
            SwitchPreferenceCompat pref = findPreference(module.getKey());
            if (pref != null) {
                boolean exists =
                        App.isAssetDirExists(App.getAssetDirName() + "/" + module.getKey());
                if (!exists) {
                    pref.setChecked(false);
                }
                pref.setEnabled(exists);
            }
        }
        updatePrefs();
    }

    @Override
    public void onPause() {
        
        super.onPause();
    }

    private void onSharedPreferenceChangeListener(SharedPreferences pref, String key) {
        updatePrefs();
    }

    private void updatePrefs() {
        SwitchPreferenceCompat bounty = findPreference(BountyGround.KEY);
        SwitchPreferenceCompat water = findPreference(WaterWar.KEY);

        bounty.setEnabled(!water.isChecked());
        water.setEnabled(!bounty.isChecked());
    }

    @Override
    public void onAttach(Context arg0) {
        super.onAttach(arg0);
        
                PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this::onSharedPreferenceChangeListener);

    }

    @Override
    public void onDetach() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this::onSharedPreferenceChangeListener);
        super.onDetach();
    }
}
