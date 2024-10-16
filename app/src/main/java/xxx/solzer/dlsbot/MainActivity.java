package xxx.solzer.dlsbot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.events.OnScreenTaked;

public class MainActivity extends AppCompatActivity {

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.bus.register(this);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }
        
        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }
        
        findViewById(R.id.startFloat).setOnClickListener(this::onClick);
        
        //test();
    }

    @Override
    public void onDestroy() {
        App.bus.unregister(this);
        super.onDestroy();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)  
    public void onEvent(OnScreenTaked screen) {
        Log.d(TAG, "Screen taked");
    }
    
    private void test(){
        
        try {
            Mat img_display = new Mat();
            Mat result = new Mat();
            Mat screen = getAsset("screen2.png");
            Mat templ = getAsset("btn_event.png");
            
            screen.copyTo(img_display);
            
            Imgproc.matchTemplate(screen, templ, result, Imgproc.TM_SQDIFF_NORMED);
            
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            
            Point matchLoc = mmr.minLoc;
            
            Imgproc.rectangle(img_display, matchLoc, new Point(matchLoc.x + templ.cols(), matchLoc.y + templ.rows()), new Scalar(255, 0, 0, 255), 2, 8, 0);
            //Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + templ.cols(), matchLoc.y + templ.rows()), new Scalar(0, 0, 0), 2, 8, 0);
        
            Bitmap bmp = Bitmap.createBitmap(img_display.cols(), img_display.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_display, bmp);
            saveImage("out.png", bmp);
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
        
    }

    private Mat getAsset(String file) throws IOException {
        Mat result = new Mat();
        InputStream stream = getAssets().open(file);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        Utils.bitmapToMat(bitmap, result);
        return result;
    }
    
    private void saveImage(String file, Bitmap bitmap){
        try (FileOutputStream out = new FileOutputStream(getFilesDir() + "/" + file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is
            // ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    public void onClick(View v) {
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, FloatingView.class));
            finish();
        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatingView.class));
            finish();
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void showToast(String msg) {
        var toast = new Toast(this);
        toast.setText(msg);
        toast.show();
    }
}
