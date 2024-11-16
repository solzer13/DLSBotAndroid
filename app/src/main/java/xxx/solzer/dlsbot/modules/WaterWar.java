package xxx.solzer.dlsbot;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.Subscribe;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class WaterWar extends Module {
    
    private static final String TAG = "WaterWar";
    private static final String KEY = "water";
    
    private static final String BANER_WW_FILE = "baner_ww.png";
    private static final String BTN_WW_light_FILE = "btn_ww_light.png";
    private static final String BTN_WW_dark_FILE = "btn_ww_dark.png";
    private static final String BTN_WW_PICK_FILE = "btn_ww_pick.png";
    private static final String BTN_WW_READY_FILE = "btn_ww_ready.png";
    private static final String BG_WW_VS_FILE = "bg_ww_vs.png";
    private static final String BG_WW_END_FILE = "bg_ww_end.png";
    private static final String BTN_WW_SKIP_FILE = "btn_ww_skip.png";
    private static final String BTN_WW_EXIT_FILE = "btn_ww_exit.png";
    
    private boolean match_started = false;
    private boolean exit = false;
    
    public void run(CommandService.StateToken state) {
        this.exit = false;
        this.match_started = false;
        
        App.bus.post(new OnUserLog("Война за воду началась"));
        
        Mat mat = CommandService.takeScreenMat();
        
        while(state.isRunning()){
            
            if(this.match_started){
                finishMatchChain(mat);
            }
            else {
                startMatchChain(mat);
            }
            
            try{Thread.sleep(1000);}catch(Exception e){}
            
            if(!exit){
                mat = CommandService.takeScreenMat();
            }
        }
        
        App.bus.post(new OnUserLog("Война за воду закончилась"));
    }
    
    public String getKey(){
        return KEY;
    }
    
    private void startMatchChain(Mat mat){
        
        Point baner_ww_loc = findBanerWW(mat);
        
        if(baner_ww_loc != null){
            App.bus.post(new OnUserLog("Жмем банер \"Война за воду\""));
            App.bus.post(new OnTap(baner_ww_loc));
            return;
        }
        
        Point btn_ww_dark_loc = findWWDarkButton(mat);
        
        if(btn_ww_dark_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"Война за воду\""));
            App.bus.post(new OnTap(btn_ww_dark_loc));
            return;
        }
        
        Point btn_ww_light_loc = findWWLightButton(mat);
        Point btn_ww_pick_loc = findWWPickButton(mat);
        
        if(btn_ww_light_loc != null && btn_ww_pick_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"Подобрать\""));
            App.bus.post(new OnTap(btn_ww_pick_loc));
            return;
        }
        
        Point btn_ww_ready_loc = findWWReadyButton(mat);
        
        if(btn_ww_ready_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"Готово\""));
            App.bus.post(new OnTap(btn_ww_ready_loc));
            return;
        }
        
        Point bg_ww_vs_loc = findWWVSBg(mat);
        
        if(bg_ww_vs_loc != null){
            this.match_started = true;
            App.bus.post(new OnUserLog("Матч начался"));
            App.bus.post(new OnUserLog("Ждём завершения матча..."));
            return;
        }
    }
    
    private void finishMatchChain(Mat mat){
    
        Point bg_ww_vs_loc = findWWVSBg(mat);
        
        if(bg_ww_vs_loc != null){
            App.bus.post(new OnTap(bg_ww_vs_loc));
            return;
        }
        
        Point bg_ww_end_loc = findWWEndBg(mat);
        
        if(bg_ww_end_loc != null){
            App.bus.post(new OnUserLog("Матч завершен"));
            App.bus.post(new OnTap(bg_ww_end_loc));
            return;
        }
        
        Point btn_ww_skip_loc = findWWSkipButton(mat);
        
        if(btn_ww_skip_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"пропустить\""));
            App.bus.post(new OnTap(btn_ww_skip_loc));
            return;
        }
        
        Point btn_ww_exit_loc = findWWExitButton(mat);
        
        if(btn_ww_exit_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"выход\""));
            App.bus.post(new OnTap(btn_ww_exit_loc));
            return;
        }
        
        Point btn_back_loc = findBackButton(mat);
        
        if(btn_back_loc != null){
            App.bus.post(new OnUserLog("Жмем кнопку \"назад\""));
            App.bus.post(new OnTap(btn_back_loc));
            this.match_started = false;
            this.exit = true;
            return;
        }
    }
    
    private Point findBanerWW(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BANER_WW_FILE), 6.5E7);
    }

    private Point findWWLightButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_light_FILE), 9E7);
    }
    
    private Point findWWDarkButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_dark_FILE), 1E8);
    }
     
    private Point findWWPickButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_PICK_FILE), 8E7);
    }
     
    private Point findWWReadyButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_READY_FILE), 3E7);
    }
     
    private Point findWWVSBg(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BG_WW_VS_FILE), 2.4E7);
    }
     
    private Point findWWEndBg(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BG_WW_END_FILE), 1E8);
    }
    
    private Point findWWSkipButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_SKIP_FILE), 5.3E7);
    }
    
    private Point findWWExitButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WW_EXIT_FILE), 3E7);
    }

    private String getAssetFilePath(String file){
        return App.getAssetDirName()+ "/water/" + file;
    }
     
}
