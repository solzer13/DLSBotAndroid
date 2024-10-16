package xxx.solzer.dlsbot;

import android.app.Application;
import org.greenrobot.eventbus.EventBus;

public class App extends Application {
    
    public static EventBus bus;
    
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOG_FILE = "log.json";
    public static final String SETTINGS_FILE = "settings.json";
    public static final String TASKS_FILE = "tasks.json";

    @Override
    public void onCreate() {
        super.onCreate();
        
        bus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();
        
        //bus.register(new Log(this.getFilesDir().toPath().resolve(LOG_FILE)));
        //bus.register(new Settings(this.getFilesDir().toPath().resolve(SETTINGS_FILE)));
        //bus.register(new Tasks(this.getFilesDir().toPath().resolve(TASKS_FILE)));
        //bus.register(new Alarms(this.getApplicationContext()));
    }

}