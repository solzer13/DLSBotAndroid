package xxx.solzer.dlsbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ImageButton;

public class FloatingService extends Service {

    private static final String TAG = "FloatingService";
    
    private WindowManager mWindowManager;
    private View myFloatingView;

    private BountyGround bounty;
    private WaterWar water;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "STARTED");
        
        myFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_view, null);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        // updateStatus();
        // Button stopButton = myFloatingView.findViewById(R.id.stop);
        // stopButton.setOnClickListener(this);
        
        get

        this.bounty = new BountyGround(getAssets());
        this.water = new WaterWar();
    }

    private void start() {
        this.showView();
    }

    private void stop() {}

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

        myFloatingView.findViewById(R.id.btnFloatingAction).setOnClickListener(this::onActionClick);
    }

    private void hideView() {
        mWindowManager.removeView(myFloatingView);
    }

    private void updateStatus() {
        if (App.isMyServiceRunning(FloatingService.class)) {
            final Handler handler = new Handler();
            handler.postDelayed(
                    () -> {
                        Log.d("Handler", "Running Handler");
                        updateStatus();
                    },
                    1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "STOPED");

        if (myFloatingView != null) mWindowManager.removeView(myFloatingView);

        getApplication().stopService(App.commandIntent);
        getApplication().stopService(App.floatingIntent);
    }

    public void onActionClick(View v) {
        ImageButton btn = (ImageButton) v;

        App.commandIntent.putExtra("action", "play");

        if (bounty.isStarted()) {
            // getApplication().stopService(App.processIntent);
            btn.setImageResource(R.drawable.play_24);
            this.bounty.stop();
        } else {
            // getApplication().startService(App.processIntent);
            btn.setImageResource(R.drawable.pause_24);
            this.bounty.start();
        }

        //        if(id == R.id.start){
        //                //Log.d("START","THIS IS STARTED");
        //                int[] location = new int[2];
        //                myFloatingView.getLocationOnScreen(location);
        //                intent.putExtra("action", "play");
        //                //intent.putExtra("x", location[0] - 1);
        //                //intent.putExtra("y", location[1] - 1);
        //        }
        //
        //        if(id ==R.id.stop){
        //                intent.putExtra("action", "stop");
        //                //mWindowManager.removeView(myFloatingView);
        //                Intent appMain = new Intent(getApplicationContext(), MainActivity.class);
        //
        //                //getApplication().startActivity(appMain);
        //                //requires the FLAG_ACTIVITY_NEW_TASK flag
        //        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.showView(); return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public android.os.IBinder onBind(Intent arg0) {
        return null;
    }
}
