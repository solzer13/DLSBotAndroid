package xxx.solzer.dlsbot.modules;

import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnTap;

public class AllianceGifts extends Module {
    
    private static final String TAG = "AllianceGifts";
    private static final String KEY = "alliance_gifts";
    
    private final Sprite btnAlliance;
    private final Sprite btnGifts;
    private final Sprite btnClaimAll;
    private final Sprite btnClaim;
    private final Sprite btnActivityGifts;
    private final Sprite btnPurchasesGifts;

    private static final String BTN_ALLIANCE_FILE = "btn_alliance.png";
    private static final double BTN_ALLIANCE_THRESHOLD = 0.98;
    
    private static final String BTN_GIFTS_FILE = "btn_gifts.png";
    private static final double BTN_GIFTS_THRESHOLD = 0.98;
    
    private static final String BTN_CLAIM_ALL_FILE = "btn_claim_all.png";
    private static final double BTN_CLAIM_ALL_THRESHOLD = 0.98;
    
    private static final String BTN_CLAIM_FILE = "btn_claim.png";
    private static final double BTN_CLAIM_THRESHOLD = 0.98;

    private static final String BTN_ACTIVITY_GIFTS_FILE = "btn_activity_gifts.png";
    private static final double BTN_ACTIVITY_GIFTS_THRESHOLD = 0.97;

    private static final String BTN_PURCHASES_GIFTS_FILE = "btn_purchases_gifts.png";
    private static final double BTN_PURCHASES_GIFTS_THRESHOLD = 0.97;
    
    public AllianceGifts(){
        super();

        this.btnAlliance = new Sprite(
            getAssetPath(BTN_ALLIANCE_FILE), 
            Imgproc.TM_CCOEFF_NORMED,
                BTN_ALLIANCE_THRESHOLD
        );
        
        this.btnGifts = new Sprite(
            getAssetPath(BTN_GIFTS_FILE), 
            Imgproc.TM_CCOEFF_NORMED,
                BTN_GIFTS_THRESHOLD
        );

        this.btnClaimAll = new Sprite(
                getAssetPath(BTN_CLAIM_ALL_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_CLAIM_ALL_THRESHOLD
        );

        this.btnClaim = new Sprite(
                getAssetPath(BTN_CLAIM_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_CLAIM_THRESHOLD
        );

        this.btnActivityGifts = new Sprite(
                getAssetPath(BTN_ACTIVITY_GIFTS_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_ACTIVITY_GIFTS_THRESHOLD
        );

        this.btnPurchasesGifts = new Sprite(
                getAssetPath(BTN_PURCHASES_GIFTS_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_PURCHASES_GIFTS_THRESHOLD
        );
    }
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            logUserMsg("Start");
        }
        
        this.btnAlliance.pushIfExists(getPushMsgLog("Альянс"), 1500);
        this.btnGifts.pushIfExists(getPushMsgLog("Подарки"), 1500);
        this.btnActivityGifts.pushIfExists(getPushMsgLog("Награда за активность"), 1000);
        
        if(this.btnClaimAll.pushIfExists(getPushMsgLog("Собрать все"), 1500)){
            while(!this.btnFreeSpace.pushIfExists(getPushMsgLog("На пустое место"), 1000));
        }
        
        this.btnPurchasesGifts.pushIfExists(getPushMsgLog("Награда за покупки"), 1000);
        while(state.isRunning()){
            Point btn_claim_point = this.btnClaim.find();
            if(btn_claim_point != null){
                this.btnClaim.push(btn_claim_point);
            }
            else {
                break;
            }
            App.sleep(1000);
        }
        
        while(state.isRunning()){
            Point btn_home_point = btnHome.find();
            if(btn_home_point != null) {
            	return;
            }
            else {
                while(!pushBack());
            }
        }
    }
    
    public String getKey(){
        return KEY;
    }
    
    public String getTag(){
        return TAG;
    }

}
