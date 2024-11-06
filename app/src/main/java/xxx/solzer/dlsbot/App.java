package xxx.solzer.dlsbot;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.greenrobot.eventbus.EventBus;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class App extends Application {
    
    private static final String TAG = "dlsbot";
    
    private static App instance;
    
    public static EventBus bus;
    public static Intent floatingIntent;
    public static Intent commandIntent;
    
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
        
        floatingIntent = new Intent(getApplicationContext(), FloatingService.class);
        commandIntent = new Intent(getApplicationContext(), CommandService.class);
        
        //bus.register(new Log(this.getFilesDir().toPath().resolve(LOG_FILE)));
        //bus.register(new Settings(this.getFilesDir().toPath().resolve(SETTINGS_FILE)));
        //bus.register(new Tasks(this.getFilesDir().toPath().resolve(TASKS_FILE)));
        //bus.register(new Alarms(this.getApplicationContext()));
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Point findImage(Mat big, String small, double trashold){
        return findImage(big, small, trashold, null);
    }
    
    public static Point findImage(Mat big, Mat small, double trashold){
        return findImage(big, small, trashold, null);
    }
    
    public static Point findImage(Mat big, String small, double trashold, String name){
        try {
            Mat mat_small = App.getAsset(small);
            return findImage(big, mat_small, trashold);
        
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        
        return null;
    }

    public static Point findImage(Mat big, Mat small, double trashold, String name){
        try {
            Mat result = new Mat();

            Imgproc.matchTemplate(big, small, result, Imgproc.TM_CCOEFF);

            var mml = Core.minMaxLoc(result);
            var loc = mml.maxLoc;
            
            if(name != null){
                Log.d(TAG, name);
                Log.d(TAG, "Min value: " + mml.minVal);
                Log.d(TAG, "Max value: " + mml.maxVal);
                Log.d(TAG, "Treshold: " + trashold);
            
                saveDebugScreen(big, small, loc, name);
            }

            if(mml.maxVal > trashold){
                return new Point(
                    loc.x + (double) (small.cols() / 2),
                    loc.y + (double) (small.rows() / 2));
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        
        return null;
    }

    public static void saveDebugScreen(Mat big, Mat small, Point loc, String name){
        Mat img_display = new Mat();
        
        big.copyTo(img_display);
        
        Imgproc.rectangle(
            img_display, 
            loc, 
            new Point(loc.x + small.cols(), loc.y + small.rows()), 
            App.RED, 
            2, 
            8, 
            0);      
            
        Bitmap bmp = Bitmap.createBitmap(img_display.cols(), img_display.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_display, bmp);
        App.saveBitmap(bmp, name + ".png");
    }
    
    public static Mat BitmapToMat(Bitmap bitmap){
        Mat mat = new Mat();
        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);
        return mat;
    }
    
    public static Mat getAsset(String file) throws IOException {
        Mat result = new Mat();
        InputStream stream = instance.getAssets().open(file);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        Utils.bitmapToMat(bitmap, result);
        return result;
    }

    public static void saveBitmap(Bitmap bmp, String file){
        try (FileOutputStream out = new FileOutputStream(instance.getFilesDir() + "/" + file)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); 
        } catch (IOException e) {
            Log.e("saveBitmap", e.getMessage());
        }
    }
    
}