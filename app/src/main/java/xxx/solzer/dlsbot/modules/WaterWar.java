package xxx.solzer.dlsbot.modules;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.Subscribe;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class WaterWar extends Module {
    
    private static final String TAG = "WaterWar";
    private static final String KEY = "water";
    
    private static final String BANER_FILE = "baner_ww.png";
    private static final double BANER_THRESHOLD = 0.98;
    private static final String BANER_NAME = "Банер";
    
    private static final String WATER_LIGHT_FILE = "btn_ww_light.png";
    private static final double WATER_LIGHT_THRESHOLD = 0.98;
    private static final String WATER_LIGHT_NAME = "Война за воду";
    
    private static final String WATER_DARK_FILE = "btn_ww_dark.png";
    private static final double WATER_DARK_THRESHOLD = 0.98;
    private static final String WATER_DARK_NAME = "Война за воду";
    
    private static final String PICK_FILE = "btn_ww_pick.png";
    private static final double PICK_THRESHOLD = 0.98;
    private static final String PICK_NAME = "baner_ww.png";
    
    private static final String READY_FILE = "btn_ww_ready.png";
    private static final double READY_THRESHOLD = 0.98;
    private static final String READY_NAME = "baner_ww.png";
    
    private static final String VS_FILE = "bg_ww_vs.png";
    private static final double VS_THRESHOLD = 0.98;
    private static final String VS_NAME = "baner_ww.png";
    
    private static final String END_FILE = "bg_ww_end.png";
    private static final double END_THRESHOLD = 0.98;
    private static final String END_NAME = "baner_ww.png";
    
    private static final String SKIP_FILE = "btn_ww_skip.png";
    private static final double SKIP_THRESHOLD = 0.98;
    private static final String SKIP_NAME = "baner_ww.png";
    
    private static final String EXIT_FILE = "btn_ww_exit.png";
    private static final double EXIT_THRESHOLD = 0.98;
    private static final String EXIT_NAME = "baner_ww.png";
    
    private final Sprite btnBaner;
    private final Sprite btnWaterLight;
    private final Sprite btnWaterDark;
    private final Sprite btnPick;
    
    public WaterWar() {
        this.btnBaner =
                new Sprite(
                        getAssetPath(BANER_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BANER_THRESHOLD,
                        getPushMsgLog(BANER_NAME));
        this.btnWaterDark =
                new Sprite(
                        getAssetPath(WATER_DARK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WATER_DARK_THRESHOLD,
                        getPushMsgLog(WATER_DARK_NAME));
        this.btnWaterLight =
                new Sprite(
                        getAssetPath(WATER_LIGHT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WATER_LIGHT_THRESHOLD,
                        getPushMsgLog(WATER_LIGHT_NAME));
        this.btnPick =
                new Sprite(
                        getAssetPath(PICK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        PICK_THRESHOLD,
                        getPushMsgLog(PICK_NAME));

    }
    
    @Override
    public void run(CommandService.StateToken state) {
        if (App.DEBUG) {
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        int counter = 0;
        
        while(state.isRunning()){
            if(btnBaner.pushIfExists(1000)){
                break;
            }
            if(counter > 5){
                return;
            }
            counter++;
        }
        
        btnWaterDark.pushIfExists(1000);
        
        Mat mat = CommandService.takeScreenMat();
        
        Point btn_water_light_point = btnWaterLight.find(mat);
        Point btn_pick_point = btnPick.find(mat);
        
        if(btn_water_light_point != null && btn_pick_point != null){
            btnPick.push(btn_pick_point, 1000);
        }
        
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

    @Override
    public String getKey(){
        return KEY;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    private void startMatchChain(Mat mat){
        
        Point banner_ww_loc = findBanerWW(mat);
        
        if(banner_ww_loc != null){
            App.bus.post(new OnUserLog("Жмем банер \"Война за воду\""));
            App.bus.post(new OnTap(banner_ww_loc));
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
