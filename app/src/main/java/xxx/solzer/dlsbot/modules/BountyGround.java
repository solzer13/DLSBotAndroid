package xxx.solzer.dlsbot;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class BountyGround extends Module {
    
    private static final String TAG = "BountyGround";
    
    private static final String KEY = "bounty";

    private static final String BTN_CAMPAIGN_FILE = "btn_campaign.png";
    private static final String BTN_BOUNTY_FILE = "btn_bg.png";
    private static final String BTN_MATCH_FILE = "btn_match.png";
    private static final String BTN_CHANCEL_FILE = "btn_chancel.png";
    private static final String BG_PROCESS_FILE = "bg_process.png";
    private static final String BG_SELECTION_FILE = "bg_selection.png";
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(!isMatchFinished()){
            return;
        }
        
        try{Thread.sleep(1000);}catch(Exception e){}
        
        pushButton(
            findCampaignButton(CommandService.takeScreenMat()),
            "Компании"
        );
        
        try{Thread.sleep(1500);}catch(Exception e){}
            
        pushButton(
            findBountyGroundButton(CommandService.takeScreenMat()),
            "Охота за призом"
        );
        
        try{Thread.sleep(1500);}catch(Exception e){}
            
        pushButton(
            findMatchButton(CommandService.takeScreenMat()),
            "Начало матча"
        );
        
        try{Thread.sleep(1000);}catch(Exception e){}
        
        while(state.isRunning()) {
            try{Thread.sleep(1000);}catch(Exception e){}
            
            Mat mat = CommandService.takeScreenMat();
            
            Point btn_back_loc = findBackButton(mat);
            if(btn_back_loc != null){
                App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Назад\""));
                App.bus.post(new OnTap(btn_back_loc));
                continue;
            }
            
            Point btn_chancel_loc = findChancelButton(mat);
            if(btn_chancel_loc != null){
                App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Отмена\""));
                App.bus.post(new OnTap(btn_chancel_loc));
                continue;
            }
            
            Point btn_home_loc = findHomeButton(mat);
            if(btn_home_loc != null){
                App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Убежище\""));
                App.bus.post(new OnTap(btn_home_loc));
                continue;
            }
            
            Point bg_selection_loc = findSelectionWindow(mat);
            if(bg_selection_loc != null){
                continue;
            }
            
            break;
        }
    }
    
    public String getKey(){
        return KEY;
    }
    
    private boolean isMatchFinished(){
        Mat mat = CommandService.takeScreenMat();
        
        Point btn_campaing_loc = findCampaignButton(mat);
        Point bg_selection_loc = findSelectionWindow(mat);
        Point bg_process_loc = findProcessWindow(mat);
        
        return 
            bg_selection_loc == null &&
            bg_process_loc == null &&
            btn_campaing_loc != null;
    }
    
    private boolean pushButton(Point point, String name){
        if(point != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"" + name + "\""));
            App.bus.post(new OnTap(point));
        }
        return point != null;
    }

    private Point findCampaignButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_CAMPAIGN_FILE), 3E7);
    }
    
    private Point findBountyGroundButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_BOUNTY_FILE), 1E8);
    }
    
    private Point findMatchButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_MATCH_FILE), 4E7);
    }
    
    private Point findChancelButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_CHANCEL_FILE), 2.2E7);
    }
    
    private Point findSelectionWindow(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BG_SELECTION_FILE), 8.5E7);
    }
    
    private Point findProcessWindow(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BG_PROCESS_FILE), 7.6E7);
    }

    private String getAssetFilePath(String file){
        return App.getAssetDirName() + "/" + KEY + "/" + file;
    }
   
}
