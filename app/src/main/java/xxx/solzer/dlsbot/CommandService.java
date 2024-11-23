package xxx.solzer.dlsbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import android.widget.ImageButton;
import android.widget.Toast;
import androidx.core.os.ExecutorCompat;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.core.Mat;
import xxx.solzer.dlsbot.events.OnLoopStart;
import xxx.solzer.dlsbot.events.OnLoopStop;
import xxx.solzer.dlsbot.events.OnPreferencesLoaded;
import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;
import xxx.solzer.dlsbot.events.OnVisibleFloatingView;

public class CommandService extends AccessibilityService {
    
    private static final String TAG = "ProcessService";
    
    private static CommandService instance;
    public static boolean floatingVisible = false;
    
    private SharedPreferences preference;
    
    public volatile static Bitmap screen = null;

    private int mTakeScreenShotDelayMs = 100;

    private StateToken state = new StateToken();
    
    private WindowManager mWindowManager;
    private View myFloatingView;

    private int mX;
    private int mY;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        App.bus.register(this);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        myFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_view, null);
    }

    @Override
    public void onDestroy() {
        App.bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"STARTED");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }
    
    @Override
    public void onInterrupt() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnTap event) {
        Log.d("OnTap", "Point: " + event.point.x + "x" + event.point.y);
        tap((int)event.point.x, (int)event.point.y);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnVisibleFloatingView event) {
        floatingVisible = event.visible;
        if(floatingVisible){
            showView();
        }
        else {
            hideView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(OnPreferencesLoaded event) {
        this.preference = event.preference;
    }
        
    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = false)
    public void onEvent(OnLoopStart event) {
        this.state.start();
        var modules = App.modules.getActiveModules();
        while(this.state.isRunning()) {
            for(var module : modules){
                module.run(this.state);
            }
        }
    }
    
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(OnLoopStop event) {
        this.state.stop();
    }
     
    public static Mat takeScreenMat(){
        return App.BitmapToMat(takeScreenBitmap());
    }
    
    public static Bitmap takeScreenBitmap(){
        CommandService.screen = null;
        
        if (Build.VERSION.SDK_INT >= 34){
            instance.takeScreenshotOfWindow(
                    instance.getRootInActiveWindow().getWindowId(),
                    Executors.newSingleThreadExecutor(),
                    new TakeScreenshotCallback() {
                        @Override
                        public void onSuccess(ScreenshotResult screenshotResult) {
                            CommandService.screen = Bitmap.wrapHardwareBuffer(screenshotResult.getHardwareBuffer(), screenshotResult.getColorSpace());
                        }
    
                        @Override
                        public void onFailure(int i) {
                            Log.d(TAG, "Failure code is " + i);
                        }
                    }
            );
        }
        else {
            int delay = 100;
            instance.takeScreenshot(
                    Display.DEFAULT_DISPLAY,
                    Executors.newSingleThreadExecutor(),
                    new TakeScreenshotCallback() {
                        @Override
                        public void onSuccess(ScreenshotResult screenshotResult) {
                            CommandService.screen = Bitmap.wrapHardwareBuffer(screenshotResult.getHardwareBuffer(), screenshotResult.getColorSpace());
                        }
    
                        @Override
                        public void onFailure(int errorCode) {
                            try {
                                if (errorCode == AccessibilityService.ERROR_TAKE_SCREENSHOT_INTERVAL_TIME_SHORT) {
                                    // try again later, incrementing delay
                                    instance.mTakeScreenShotDelayMs += 50;
                                    App.sleep(instance.mTakeScreenShotDelayMs);
                                        try {
                                            instance.takeScreenshot(Display.DEFAULT_DISPLAY,
                                                    Executors.newSingleThreadExecutor(),
                                                    this
                                            );
                                        } catch (Exception ignored) {
                                            // instance might be gone
                                        }
                                    Log.w(TAG, "takeScreenShots: onFailure with ERROR_TAKE_SCREENSHOT_INTERVAL_TIME_SHORT - upped delay to " + instance.mTakeScreenShotDelayMs);
                                    return;
                                }
                                Log.e(TAG, "takeScreenShots: onFailure with error code " + errorCode);
                            } catch (Exception e) {
                                Log.e(TAG, "takeScreenShots: onFailure exception " + e);
                            }
                        }
                    }
            );
        }

        
        while(CommandService.screen == null);
        
        return CommandService.screen;
    }
    
    public static boolean isFloatingVisible(){
        return floatingVisible;
    }
       
    private void showView() {
        
        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        
        
        mWindowManager.addView(myFloatingView, params);

        // adding an touchlistener to make drag movement of the floating widget
        myFloatingView
                .findViewById(R.id.thisIsAnID)
                .setOnTouchListener(
                        new View.OnTouchListener() {
                            private int initialX;
                            private int initialY;
                            private float initialTouchX;
                            private float initialTouchY;

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // Log.d("TOUCH","THIS IS TOUCHED");
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        initialX = params.x;
                                        initialY = params.y;
                                        initialTouchX = event.getRawX();
                                        initialTouchY = event.getRawY();
                                        return true;

                                    case MotionEvent.ACTION_UP:
                                        return true;

                                    case MotionEvent.ACTION_MOVE:
                                        // this code is helping the widget to move around the screen
                                        // with fingers
                                        params.x =
                                                initialX + (int) (event.getRawX() - initialTouchX);
                                        params.y =
                                                initialY + (int) (event.getRawY() - initialTouchY);
                                        mWindowManager.updateViewLayout(myFloatingView, params);
                                        return true;
                                }
                                return false;
                            }
                        });

        myFloatingView.findViewById(R.id.btnFloatingPlay).setOnClickListener(this::onActionClick);
        //myFloatingView.findViewById(R.id.btnFloatingLog).setOnClickListener(this::onLogClick);
    }

    public void onActionClick(View v) {
        ImageButton btn = (ImageButton) v;

        if (this.state.isRunning()) {
            btn.setImageResource(R.drawable.play_24);
            App.bus.post(new OnLoopStop());
        } else {
            btn.setImageResource(R.drawable.pause_24);
            App.bus.post(new OnLoopStart());
        }
    }

    private void hideView() {
        mWindowManager.removeView(myFloatingView);
    }

    private void tap(int x, int y) {
        dispatchGesture(getTapGesture(x, y), null, null);
    }

    private GestureDescription getTapGesture(int x, int y){
        Path swipePath = new Path();
        swipePath.moveTo(x, y);
        swipePath.lineTo(x, y);
        GestureDescription.StrokeDescription tap =
                new GestureDescription.StrokeDescription(
                        swipePath, 0, ViewConfiguration.getTapTimeout());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(tap);
        
        return builder.build();
    }
    
    public class StateToken {
        
        private volatile boolean running = false;
        
        public boolean isRunning(){
            return this.running;
        }
        
        public void start(){
            this.running = true;
        }
        
        public void stop(){
            this.running = false;
        }
    }
  
}
