package xxx.solzer.dlsbot;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;

public class BountyGround {
    
    private static final String TAG = "BountyGround";

    private static final String BTN_CAMPAIGN_FILE = "btn_campaign.png";
    private static final String BTN_BOUNTY_FILE = "btn_bg.png";
    private static final String BTN_MATCH_FILE = "btn_match.png";
    private static final String BTN_CHANCEL_FILE = "btn_chancel.png";
    private static final String BTN_BACK_FILE = "btn_back.png";
    private static final String WAIT_BOUNTY_FILE = "wait_bg.png";
    
    private final AssetManager am;

    private int step = 1;
    
    private boolean started = false;
    private boolean chanceled = false;
    
    private boolean event_started = false;

    public BountyGround(AssetManager am){
        this.am = am;
    }
    
    public void start() {
        Log.d(TAG, "Start");
        this.started = true;
        this.event_started = false;
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

        Mat mat = new Mat();
        Bitmap bmp32 = screen.bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);

        this.test_logic_1(mat);
        
        Handler handler = new Handler();
        handler.postDelayed(() -> App.bus.post(new OnTakeScreen()), 1000);
    }
    
    private void test_logic_1(Mat mat) {

        if(!this.event_started){
            Point btn_campaing_loc = findCampaignButton(mat);
            Point btn_bg_loc = findBountyGroundButton(mat);
            Point btn_match_loc = findMatchButton(mat);
            
            if(btn_campaing_loc != null){
                App.bus.post(new OnTap(btn_campaing_loc));
                Log.d(TAG, "Campaing pushed.");
                return;
            }
            
            if(btn_bg_loc != null){
                App.bus.post(new OnTap(btn_bg_loc));
                Log.d(TAG, "BountyGround pushed.");
                return;
            }
            
            if(btn_match_loc != null){
                App.bus.post(new OnTap(btn_match_loc));
                this.event_started = true;
                Log.d(TAG, "Match pushed.");
                Log.d(TAG, "Event started.");
                return;
            }
        }
        else {
            Point btn_back_loc = findBackButton(mat);
            Point btn_chancel_loc = findChancelButton(mat);
            Point wait_window_loc = findWaitWindow(mat);
            Point btn_campaing_loc = findCampaignButton(mat);

            if(btn_back_loc != null){
                App.bus.post(new OnTap(btn_back_loc));
                Log.d(TAG, "Back pushed.");
                return;
            }
            if(btn_chancel_loc != null){
                App.bus.post(new OnTap(btn_chancel_loc));
                Log.d(TAG, "Chancel pushed.");
                return;
            }
            if(btn_campaing_loc != null && wait_window_loc == null){
                this.event_started = false;
                Log.d(TAG, "Event finished.");
            }
            
        }
        
    }

    private Point findCampaignButton(Mat mat) {
        return App.findImage(mat, BTN_CAMPAIGN_FILE, 3E7);
    }
    
    private Point findBountyGroundButton(Mat mat) {
        return App.findImage(mat, BTN_BOUNTY_FILE, 1E8);
    }
    
    private Point findMatchButton(Mat mat) {
        return App.findImage(mat, BTN_MATCH_FILE, 4E7);
    }
    
    private Point findBackButton(Mat mat) {
        return App.findImage(mat, BTN_BACK_FILE, 3.9E7);
    }
    
    private Point findChancelButton(Mat mat) {
        return App.findImage(mat, BTN_CHANCEL_FILE, 2.2E7);
    }
    
    private Point findWaitWindow(Mat mat) {
        return App.findImage(mat, WAIT_BOUNTY_FILE, 2.9E7);
    }

}
