package xxx.solzer.dlsbot;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import org.greenrobot.eventbus.EventBus;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Scalar;

public class App extends Application {
    
    private static App instance;
    
    public static EventBus bus;
    
    public static final Scalar RED = new Scalar(255, 0, 0, 255);
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOG_FILE = "log.json";
    public static final String SETTINGS_FILE = "settings.json";
    public static final String TASKS_FILE = "tasks.json";
    
    public App(){
        instance = this;
    }

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

    public static void saveBitmap(Bitmap bmp, String file){
        try (FileOutputStream out = new FileOutputStream(instance.getFilesDir() + "/" + file)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); 
        } catch (IOException e) {
            Log.e("saveBitmap", e.getMessage());
        }
    }
    
}