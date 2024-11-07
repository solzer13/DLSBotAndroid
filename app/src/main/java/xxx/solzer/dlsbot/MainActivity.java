package xxx.solzer.dlsbot;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private static final String TAG = "MainActivity";
    
    private Toolbar toolbar;
    private BottomNavigationView bottom_nav;

    public MainActivity() {
        if (OpenCVLoader.initLocal()) {
            Log.i("TAG", "OpenCV loaded successfully");
        } else {
            Log.e("TAG", "OpenCV initialization failed!");
        }
    }
    
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        //your package /   accesibility service path/class
        final String service = "xxx.solzer.dlsbot/xxx.solzer.dlsbot.FloatingService";
    
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
    
        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
    
                    Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }
    
        return accessibilityFound;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bottom_nav = findViewById(R.id.mainBottomNavigationView);
        
        this.bottom_nav.setOnItemSelectedListener(this::onBottomNavClick);
        
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_container, new SettingsFragment())
            .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(App.isMyServiceRunning(CommandService.class)){
            Log.d(TAG, "TRUE");
        }
        else {
            Log.d(TAG, "FALSE");
            //askPermission();
        }
        //this.toolbar.setTitle("Стандартный заголовок");
        // mToolbar.setSubtitle("Подзаголовок");
        // mToolbar.setLogo(R.drawable.ic_launcher_foreground);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_start);
        item.setIcon(R.drawable.ic_launcher_foreground);
        
        if(App.isMyServiceRunning(FloatingService.class)){
            item.setIcon(R.drawable.stop_32);
        }
        else {
            item.setIcon(R.drawable.play_32);
        }
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.action_start){
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
        return super.onOptionsItemSelected(item);
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

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS, Uri.parse("package:" + getPackageName()));
        //startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
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
