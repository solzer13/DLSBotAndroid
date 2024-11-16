package xxx.solzer.dlsbot;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.preference.PreferenceManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.Subscribe;
import org.opencv.android.OpenCVLoader;
import xxx.solzer.dlsbot.events.OnPreferencesLoaded;
import xxx.solzer.dlsbot.events.OnVisibleFloatingView;

public class MainActivity extends AppCompatActivity {

    private static final int SYSTEM_ALERT_WINDOW_CODE = 1767575;
    private static final int BIND_ACCESSIBILITY_SERVICE_CODE = 87347475;

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private static final String TAG = "MainActivity";

    private MaterialToolbar toolbar;
    private BottomNavigationView bottom_nav;
    private SharedPreferences preferences;

    public MainActivity() {
        if (OpenCVLoader.initLocal()) {
            Log.i("TAG", "OpenCV loaded successfully");
        } else {
            Log.e("TAG", "OpenCV initialization failed!");
        }
    }

    private void checkPremission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent =
                    new Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
        int accessEnabled = 0;
        try {
            accessEnabled =
                    Settings.Secure.getInt(
                            this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.mainMaterialToolbar);
        this.bottom_nav = findViewById(R.id.mainBottomNavigationView);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        this.toolbar.setOnMenuItemClickListener(this::onToolbarItemClick);
        this.bottom_nav.setOnItemSelectedListener(this::onBottomNavClick);
        this.preferences.registerOnSharedPreferenceChangeListener(this::onSharedPreferenceChangeListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        App.bus.register(this);
        
        checkPremission();
        
        setActionIcon(CommandService.isFloatingVisible());

        if (App.isMyServiceRunning(CommandService.class)) {
            this.showToast("CommandService runing");
        } else {
            this.showToast("CommandService stoped");
        }
                
        App.bus.postSticky(new OnPreferencesLoaded(this.preferences));
        
        showToast(String.valueOf(App.isScreenSupported()));
    }

    @Override
    protected void onPause() {
        App.bus.unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnVisibleFloatingView event) {
        setActionIcon(event.visible);
    }
    
    private void setActionIcon(boolean runing){
        if (runing) {
            this.toolbar.getMenu().findItem(R.id.btnPlay).setIcon(R.drawable.stop_32);
        } else {
            this.toolbar.getMenu().findItem(R.id.btnPlay).setIcon(R.drawable.play_32);
        }
    }
    
    private void onSharedPreferenceChangeListener(SharedPreferences pref, String key){
        App.bus.postSticky(new OnPreferencesLoaded(pref));
    }

    public boolean onToolbarItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnPlay) {
            if (CommandService.isFloatingVisible()) {
                App.bus.post(new OnVisibleFloatingView(false));
            } else {
                App.bus.post(new OnVisibleFloatingView(true));
            }
            return true;
        }
        return false;
    }

    private boolean onBottomNavClick(MenuItem item) {
        if (item.getItemId() == R.id.btnSettings) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new SettingsFragment())
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.btnLogs) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new LogsMainFragment())
                    .commit();
            return true;
        }
        return false;
    }

    public void showToast(String msg) {
        var toast = new Toast(this);
        toast.setText(msg);
        toast.show();
    }

}
