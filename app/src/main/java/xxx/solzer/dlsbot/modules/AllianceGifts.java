package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
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

    private static final String GIFTS_FILE = "gifts.png";
    private static final double GIFTS_THRESHOLD = 0.9;
    private static final String GIFTS_NAME = "Подарки";

    private static final String GET_FILE = "get.png";
    private static final double GET_THRESHOLD = 0.8;
    private static final String GET_NAME = "Собрать";

    private static final String GET_ALL_FILE = "get_all.png";
    private static final double GET_ALL_THRESHOLD = 0.9;
    private static final String GET_ALL_NAME = "Собрать все";

    private static final String ACTIVITY_GIFTS_FILE = "activity_gifts.png";
    private static final double ACTIVITY_GIFTS_THRESHOLD = 0.8;
    private static final String ACTIVITY_GIFTS_NAME = "Награда за активность";

    private static final String PURCHASES_GIFTS_FILE = "purchases_gifts.png";
    private static final double PURCHASES_GIFTS_THRESHOLD = 0.8;
    private static final String PURCHASES_GIFTS_NAME = "Награда за покупки";

    private static final String CONGRATS_FILE = "congrats.png";
    private static final double CONGRATS_THRESHOLD = 0.8;
    private static final String CONGRATS_NAME = "Пустое место";
    
    private final Sprite btnGifts;
    private final Sprite btnClaim;
    private final Sprite btnClaimAll;
    private final Sprite btnActivityGifts;
    private final Sprite btnPurchasesGifts;
    private final Sprite btnCongrats;
    
    public AllianceGifts() {
        super();

        this.btnGifts =
                new Sprite(
                        getAssetPath(GIFTS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        GIFTS_THRESHOLD,
                        getPushMsgLog(GIFTS_NAME));
        this.btnClaim =
                new Sprite(
                        getAssetPath(GET_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        GET_THRESHOLD,
                        getPushMsgLog(GET_NAME));
        this.btnClaimAll =
                new Sprite(
                        getAssetPath(GET_ALL_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        GET_ALL_THRESHOLD,
                        getPushMsgLog(GET_ALL_NAME));
        this.btnActivityGifts =
                new Sprite(
                        getAssetPath(ACTIVITY_GIFTS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        ACTIVITY_GIFTS_THRESHOLD,
                        getPushMsgLog(ACTIVITY_GIFTS_NAME));
        this.btnPurchasesGifts =
                new Sprite(
                        getAssetPath(PURCHASES_GIFTS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        PURCHASES_GIFTS_THRESHOLD,
                        getPushMsgLog(PURCHASES_GIFTS_NAME));
        this.btnCongrats =
                new Sprite(
                        getAssetPath(CONGRATS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CONGRATS_THRESHOLD,
                        getPushMsgLog(CONGRATS_NAME));
    }

    public void run(CommandService.StateToken state) {

        if (App.DEBUG) {
            logUserMsg("Start");
        }

        if(this.btnAlliance.pushIfExists(1500)){
            if(this.btnGifts.pushIfExists(1500)){

                if(this.btnActivityGifts.pushIfExists(1500)){
                    if(btnClaimAll.pushIfExists(2000)){
                        btnCongrats.pushIfExists(1500);
                    }
                    while (state.isRunning()) {
                        if (!this.btnClaim.pushIfExists(500)) {
                            break;
                        }
                    }
                }

                if(this.btnPurchasesGifts.pushIfExists(1500)) {
                    while (state.isRunning()) {
                        if (!this.btnClaim.pushIfExists(500)) {
                            break;
                        }
                    }
                }

            }
        }

        while (state.isRunning()) {
            Mat mat = CommandService.takeScreenMat();
            if (btnBack.pushIfExists(mat, 1500)){
                continue;
            }
            if (btnRegion.isFound(mat)) {
                return;
            }
            App.sleep(1000);
        }
    }

    public String getKey() {
        return KEY;
    }

    public String getTag() {
        return TAG;
    }
}
