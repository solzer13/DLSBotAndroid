package xxx.solzer.dlsbot;

import android.os.Handler;
import android.util.Log;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.Subscribe;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;

public class WaterWar {
    
    private static final String TAG = "WaterWar";
    
    private static final String BANER_WW_FILE = "baner_ww.png";
    private static final String BTN_WW_FILE = "btn_ww.png";
    private static final String BTN_WW_PICK_FILE = "btn_ww_pick.png";
    private static final String BTN_WW_READY_FILE = "btn_ww_ready.png";
    
    private boolean started = false;
    
    public void start() {
        Log.d(TAG, "Start");
        this.started = true;
        App.bus.register(this);
        App.bus.post(new OnTakeScreen());
    }
    
    public void stop() {
        Log.d(TAG, "Stop");
    	App.bus.unregister(this);
        this.started = false;
    }
    
    public boolean isStarted() {
    	return this.started;
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(OnScreenTaked screen) throws InterruptedException {
        Log.d(TAG, "Screen taked");
        
        Mat mat = App.BitmapToMat(screen.bitmap);

        this.test_logic_1(mat);
        
        Handler handler = new Handler();
        handler.postDelayed(() -> App.bus.post(new OnTakeScreen()), 1000);
    }
    
    private void test_logic_1(Mat mat) {
        Point baner_ww_loc = findBanerWW(mat);
    }
    
    private Point findBanerWW(Mat mat) {
        return App.findImage(mat, BANER_WW_FILE, 3E7, "baner_ww");
    }

    private Point findWWButton(Mat mat) {
        return App.findImage(mat, BTN_WW_FILE, 3E7);
    }
    
}
