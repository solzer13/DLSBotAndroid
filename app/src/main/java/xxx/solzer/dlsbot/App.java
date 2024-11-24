package xxx.solzer.dlsbot;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.preference.PreferenceManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.greenrobot.eventbus.EventBus;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.modules.AllianceGifts;
import xxx.solzer.dlsbot.modules.BountyGround;
import xxx.solzer.dlsbot.modules.CollectingHome;
import xxx.solzer.dlsbot.modules.DonateTechnologies;
import xxx.solzer.dlsbot.modules.Farming;
import xxx.solzer.dlsbot.modules.Garage;
import xxx.solzer.dlsbot.modules.Help;
import xxx.solzer.dlsbot.modules.Police;
import xxx.solzer.dlsbot.modules.Radar;
import xxx.solzer.dlsbot.modules.WaterWar;

public class App extends Application {
    
    public static final String TAG = "dlsbot";
    
    public static final boolean DEBUG = true;
    
    private static App instance;
    
    public static EventBus bus;
    public static UserLog userLog;
    public static ModuleRepository modules;
    
    public static final Scalar RED = new Scalar(255, 0, 0, 255);
    
    public static final Screen[] SCREENS = new Screen[]{
            new Screen(3200, 1440, 1.34, 1.34),
            new Screen(2179, 1080, 1, 1),
            new Screen(1340, 800, 0.695, 0.56)
    };
    
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
        
        userLog = new UserLog();

        if(isScreenSupported()) {
            modules = new ModuleRepository(
                    new Help(),
                    new CollectingHome(),
                    new Garage(),
                    new DonateTechnologies(),
                    new Police(),
                    new Radar(),
                    new AllianceGifts(),
                    new Farming(),
                    new BountyGround(),
                    new WaterWar()
            );
        }
        else {
            modules = new ModuleRepository();
        }
        
        bus.register(userLog);
        bus.register(modules);
    }
    
    public static SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(instance);
    }
    
    public static void sleep(int millis){
        try{Thread.sleep(millis);}catch(Exception e){}
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
    
    public static Point tm_ccoeff_normed(Mat big, String small, double trashold){
        return findImage(big, small, Imgproc.TM_CCOEFF_NORMED, trashold);
    }
    
    public static Point findImage(Mat big, String small, double trashold){
        return findImage(big, App.getAsset(small), trashold);
    }

    public static Point findImage(Mat big, Mat small, double trashold){
        return findImage(big, small, Imgproc.TM_CCOEFF, trashold);
    }
    
    public static Point findImage(Mat big, String small, int type, double trashold){ 
        return findImage(big, App.getAsset(small), type, trashold);
    }

    public static Point findImage(Mat big, Mat small, int type, double trashold){
        try {
            Mat result = new Mat();

            Imgproc.matchTemplate(big, small, result, type);

            var mml = Core.minMaxLoc(result);
            var loc = mml.maxLoc;
            
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
    
    public static Mat getAsset(Path file) {
        return getAsset(file.toString());
    }
    
    public static Mat getAsset(String file) {
        Mat result = new Mat();
        try {
            InputStream stream = instance.getAssets().open(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            Utils.bitmapToMat(bitmap, result);
        } catch(Exception ex) {
        	Log.e(TAG, ex.getMessage());
        }
        return result;
    }

    public static void saveBitmap(Bitmap bmp, String file){
        try (FileOutputStream out = new FileOutputStream(instance.getFilesDir() + "/" + file)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); 
        } catch (IOException e) {
            Log.e("saveBitmap", e.getMessage());
        }
    }
    
    public static boolean isScreenSupported(){
        return getCurrentScreen() != null;
        //return isAssetDirExists(getAssetDirName());
    }
    
    public static boolean isAssetDirExists(String path){
        AssetManager manager = instance.getAssets();
        try {
            String[] files = manager.list(path);
            if (files.length > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String getAssetDirName(){
        return "auto";
//        if(getScreenWidth() > getScreenHeight()){
//            return String.valueOf(getScreenWidth()) + "x" + String.valueOf(getScreenHeight());
//        }
//        else {
//            return String.valueOf(getScreenHeight()) + "x" + String.valueOf(getScreenWidth());
//        }
    }
    
    public static Screen getCurrentScreen(){
        for(Screen screen : SCREENS) {
        	if(screen.width == getScreenMax()){
                return screen;
            }
        }
        return null;
    }
    
    public static int getScreenMax(){
        return getScreenWidth() > getScreenHeight() ? getScreenWidth() : getScreenHeight();
    }
    
    public static int getScreenMin(){
        return getScreenWidth() < getScreenHeight() ? getScreenWidth() : getScreenHeight();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}