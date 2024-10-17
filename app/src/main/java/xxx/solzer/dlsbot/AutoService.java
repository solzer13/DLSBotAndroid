package xxx.solzer.dlsbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;

public class AutoService extends AccessibilityService {
    
    private Handler mHandler;

    private int mX;
    private int mY;

    @Override
    public void onCreate() {
        super.onCreate();
        App.bus.register(this);
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        App.bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnTap event) {
        Log.d("OnTap", "Point: " + event.point.x + "x" + event.point.y);
        tap(event.point.x, event.point.y);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  
    public void onEvent(OnTakeScreen event) {
        final String tag_screen = "OnTakeScreen";

        takeScreenshot(
            Display.DEFAULT_DISPLAY,
            getApplicationContext().getMainExecutor(),
            new TakeScreenshotCallback() {
                @Override
                public void onSuccess(ScreenshotResult screenshotResult) {
                    Log.d(tag_screen, "Success");
                    Bitmap screen = Bitmap.wrapHardwareBuffer(screenshotResult.getHardwareBuffer(), screenshotResult.getColorSpace());
                    App.saveBitmap(screen, "last_screen.png");
                    App.bus.post(new OnScreenTaked(screen));
                }

                @Override
                public void onFailure(int i) {
                    Log.d(tag_screen, "Failure code is " + i);
                }
            }
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service","SERVICE STARTED");
        if(intent!=null){
            String action = intent.getStringExtra("action");
            
            if (action.equals("play")) {
                BountyGround bg = new BountyGround(getAssets());
//                mX = intent.getIntExtra("x", 0);
//                Log.d("x_value",Integer.toString(mX));
//                mY = intent.getIntExtra("y", 0);
//                if (mRunnable == null) {
//                    mRunnable = new IntervalRunnable();
//                }
//                //playTap(mX,mY);
//                //mHandler.postDelayed(mRunnable, 1000);
//                mHandler.post(mRunnable);
//                Toast.makeText(getBaseContext(), "Started", Toast.LENGTH_SHORT).show();
            }
            
            else if(action.equals("stop")){
                mHandler.removeCallbacksAndMessages(null);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void tap(int x, int y) {
        Path swipePath = new Path();
        swipePath.moveTo(x, y);
        swipePath.lineTo(x, y);
         GestureDescription.StrokeDescription tap =  new GestureDescription.StrokeDescription(swipePath, 0,
         ViewConfiguration.getTapTimeout());
         GestureDescription.Builder builder = new GestureDescription.Builder();
         builder.addStroke(tap);
         boolean resu = dispatchGesture(builder.build(), null, null);
        Log.d("TAPPED",String.valueOf(resu));
        
     }
    
    //@RequiresApi(api = Build.VERSION_CODES.N)
    private void playTap(int x, int y) {
       Log.d("TAPPED","STARTED TAPpING");
        try {
            Runtime.getRuntime().exec("input tap 500 500");
        }
        catch(Exception e){
            Log.e("TAPPED",e.getLocalizedMessage());
        }
        
        Path swipePath = new Path();
        swipePath.moveTo(x, y);
        swipePath.lineTo(x, y);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 10));
        //dispatchGesture(gestureBuilder.build(), null, null);
        
        Log.d("hello","hello?");
        boolean resu = dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.d("Gesture Completed","Gesture Completed");
                super.onCompleted(gestureDescription);
                //mHandler.postDelayed(mRunnable, 1);
                mHandler.post(mRunnable);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Log.d("Gesture Cancelled","Gesture Cancelled");
                super.onCancelled(gestureDescription);
            }
        }, null);
        Log.d("TAPPED",String.valueOf(resu));
        Log.d("hi","hi?");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }


    @Override
    public void onInterrupt() {
    }


    private IntervalRunnable mRunnable;

    private class IntervalRunnable implements Runnable {
        @Override
        public void run() {
            Log.d("clicked","click");
            //tap(mX, mY);
            playTap(mX, mY);
        }
    }
}
