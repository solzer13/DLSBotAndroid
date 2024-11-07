package xxx.solzer.dlsbot;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private static final int SYSTEM_ALERT_WINDOW_CODE = 1767575;
    private static final int BIND_ACCESSIBILITY_SERVICE_CODE = 87347475;

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private static final String TAG = "MainActivity";
    
    private MaterialToolbar toolbar;
    private BottomNavigationView bottom_nav;

    public MainActivity() {
        if (OpenCVLoader.initLocal()) {
            Log.i("TAG", "OpenCV loaded successfully");
        } else {
            Log.e("TAG", "OpenCV initialization failed!");
        }
    }

    private void checkPremission(){
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // request permission via start activity for result
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.mainMaterialToolbar);
        this.bottom_nav = findViewById(R.id.mainBottomNavigationView);

        this.toolbar.setOnMenuItemClickListener(this::onToolbarItemClick);
        this.bottom_nav.setOnItemSelectedListener(this::onBottomNavClick);
        
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_container, new SettingsFragment())
            .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPremission();

        //checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, SYSTEM_ALERT_WINDOW_CODE);

        if(App.isMyServiceRunning(CommandService.class)){
            Log.d(TAG, "TRUE");
        }
        else {
            Log.d(TAG, "FALSE");
            //askPermission();
        }
    }

    public boolean onToolbarItemClick(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.btnPlay){
            if(App.isMyServiceRunning(FloatingService.class)){
                this.onStopClick();
                item.setIcon(R.drawable.play_32);
            }
            else {
                this.onStartClick();
                item.setIcon(R.drawable.stop_32);
            }
            return true;
        }
        return false;
    }
    
    private boolean onBottomNavClick(MenuItem item){
        if(item.getItemId() == R.id.btnSettings){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SettingsFragment())
                .commit();
            return true;
        }
        if(item.getItemId() == R.id.btnLogs){  
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new LogsMainFragment())
                .commit();
            return true;
        }
        return false;
    }

    public void onStopClick() {
        stopService(App.floatingIntent);
    }

    public void onStartClick() {
        startService(App.floatingIntent);
        
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            startService(App.floatingIntent);
//            startActivity(startMain);
//            //finish();
//        } else if (Settings.canDrawOverlays(this)) {
//            startService(new Intent(MainActivity.this, FloatingService.class));
//            startActivity(startMain);
//            //finish();
//        } else {
//            askPermission();
//            Toast.makeText(
//                            this,
//                            "You need System Alert Window Permission to do this",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

    public void showToast(String msg) {
        var toast = new Toast(this);
        toast.setText(msg);
        toast.show();
    }

}
