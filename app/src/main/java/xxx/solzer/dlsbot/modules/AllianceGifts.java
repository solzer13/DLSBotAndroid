package xxx.solzer.dlsbot.modules;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class AllianceGifts extends Module {
    
    private static final String TAG = "AllianceGifts";
    private static final String KEY = "alliance_gifts";
    
    private final Sprite btnAlliance;
    private final Sprite btnGifts;

    private static final String BTN_ALLIANCE_FILE = "btn_alliance.png";
    private static final double BTN_ALLIANCE_TRASHOLD = 0.98;
    
    private static final String BTN_GIFTS_FILE = "btn_gifts.png";
    private static final double BTN_GIFTS_TRASHOLD = 0.98;
    
    private static final String BTN_CLAIM_ALL_FILE = "btn_claim_all.png";
    private static final double BTN_CLAIM_ALL_TRASHOLD = 0.98;
    
    private static final String BTN_CLAIM_FILE = "btn_claim.png";
    private static final double BTN_CLAIM_TRASHOLD = 0.98;
    
    private static final String BTN_PURCHASES_GIFTS_FILE = "btn_purchases_gifts.png";
    private static final double BTN_PURCHASES_GIFTS_TRASHOLD = 0.99;
    
    public AllianceGifts(){
        this.btnAlliance = new Sprite(
            getAssetPath(BTN_ALLIANCE_FILE), 
            Imgproc.TM_CCOEFF_NORMED, 
            BTN_ALLIANCE_TRASHOLD
        );
        
        this.btnGifts = new Sprite(
            getAssetPath(BTN_GIFTS_FILE), 
            Imgproc.TM_CCOEFF_NORMED, 
            BTN_GIFTS_TRASHOLD
        );
    }
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            logUserMsg("Start");
        }
        
        this.btnAlliance.pushIfExists(getPushMsgLog("Альянс"), 1500);
        this.btnGifts.pushIfExists(getPushMsgLog("Подарки"), 1500);
    }
    
    public String getKey(){
        return KEY;
    }
    
    public String getTag(){
        return TAG;
    }
    
    private void pushAllianceButton(){  
        Point btn = findAllianceButton(CommandService.takeScreenMat());
    
        if(btn != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Альянс\""));
            App.bus.post(new OnTap(btn));
        }
        
        try{Thread.sleep(1500);}catch(Exception e){}
    }
        
    private Point findAllianceButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_ALLIANCE_FILE), BTN_ALLIANCE_TRASHOLD);
    }
            
    private Point findGiftsButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_GIFTS_FILE), 1E8);
    }
            
    private Point findClaimAllButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_CLAIM_ALL_FILE), 3.7E7);
    }
            
    private Point findClaimButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_CLAIM_FILE), 2E7);
    }
            
    private Point findPurchasesGiftsButton(Mat mat) {
        return App.tm_ccoeff_normed(mat, getAssetFilePath(BTN_PURCHASES_GIFTS_FILE), BTN_PURCHASES_GIFTS_TRASHOLD);
    }

}
