package xxx.solzer.dlsbot.ui;

import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.R;
import android.os.Bundle;
import androidx.core.content.SharedPreferencesCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        for(Module module : App.modules.getModules()){
            SwitchPreferenceCompat pref = findPreference(module.getKey());
            if(pref != null){
                boolean exists = App.isAssetDirExists(App.getAssetDirName() +"/"+module.getKey());
                if(!exists){
                    pref.setChecked(false);
                }
                pref.setEnabled(exists);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
