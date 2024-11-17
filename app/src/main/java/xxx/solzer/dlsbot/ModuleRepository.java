package xxx.solzer.dlsbot;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import xxx.solzer.dlsbot.events.OnPreferencesLoaded;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xxx.solzer.dlsbot.events.OnUserLog;

public class ModuleRepository {
    
    private final Module[] modules;
    private SharedPreferences preference;
    
    public ModuleRepository(Module ...modules){
        this.modules = modules;
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(OnPreferencesLoaded event) {
        this.preference = event.preference;
    }

    public Module[] getModules(){
        return modules;
    }

    public List<Module> getActiveModules(){
        var list = new ArrayList<Module>();
        for(var module : this.modules) {
        	if(this.preference.getBoolean(module.getKey(), false)){
                list.add(module);
            }
        }
        return list;
    }

}
