package xxx.solzer.dlsbot.events;

import android.content.SharedPreferences;

public class OnPreferencesLoaded {
    
    public final SharedPreferences preference;
    
    public OnPreferencesLoaded(SharedPreferences pref){
        this.preference = pref;
    }
}
