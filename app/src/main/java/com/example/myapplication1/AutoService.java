package com.example.myapplication1;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

public class AutoService extends AccessibilityService {
    private Handler mHandler;
    private int mX;
    private int mY;


    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onServiceConnected() {

    }
    
    @Override
    public void takeScreenshot(int displayId, Executor executor, TakeScreenshotCallback callback) {
        super.takeScreenshot(displayId, executor, callback);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service","SERVICE STARTED");
        if(intent!=null){
            String action = intent.getStringExtra("action");
            
            if (action.equals("play")) {
                takeScreenshot(
                        Display.DEFAULT_DISPLAY,
                        getApplicationContext().getMainExecutor(),
                        new TakeScreenshotCallback() {
                            @RequiresApi(api = Build.VERSION_CODES.R)
                            @Override
                            public void onSuccess(ScreenshotResult screenshotResult) {

                                Log.i("ScreenShotResult", "onSuccess");
                                Bitmap bitmap =
                                        Bitmap.wrapHardwareBuffer(
                                                screenshotResult.getHardwareBuffer(),
                                                screenshotResult.getColorSpace());
                                try (FileOutputStream out = new FileOutputStream(getFilesDir() + "/screen.png")) {
                                    bitmap.compress(
                                            Bitmap.CompressFormat.PNG,
                                            100,
                                            out); // bmp is your Bitmap instance
                                    // PNG is a lossless format, the compression factor (100) is
                                    // ignored
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // AccessibilityUtils.saveImage(bitmap, getApplicationContext(),
                                // "WhatsappIntegration");
                            }

                            @Override
                            public void onFailure(int i) {

                                Log.i("ScreenShotResult", "onFailure code is " + i);
                            }
                        });
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
