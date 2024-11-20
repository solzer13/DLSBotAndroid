package xxx.solzer.dlsbot.modules;


import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnUserLog;

public class BountyGround extends Module {

    public static final String TAG = "BountyGround";
    public static final String KEY = "bounty";

    private static final String CAMPAIGN_FILE = "campaign.png";
    private static final double CAMPAIGN_THRESHOLD = 0.95;
    private static final String CAMPAIGN_NAME = "Компании";

    private static final String BOUNTY_FILE = "bounty.png";
    private static final double BOUNTY_THRESHOLD = 0.98;
    private static final String BOUNTY_NAME = "Охота за призом";

    private static final String MATCH_FILE = "match.png";
    private static final double MATCH_THRESHOLD = 0.98;
    private static final String MATCH_NAME = "Матч";

    private static final String CHANCEL_FILE = "chancel.png";
    private static final double CHANCEL_THRESHOLD = 0.98;
    private static final String CHANCEL_NAME = "Отмена";

    private static final String PROCESS_FILE = "process.png";
    private static final double PROCESS_THRESHOLD = 0.95;

    private static final String SELECTION_FILE = "selection.png";
    private static final double SELECTION_THRESHOLD = 0.95;

    private final Sprite btnCampaign;
    private final Sprite btnBounty;
    private final Sprite btnMatch;
    private final Sprite btnChancel;
    private final Sprite wndProcess;
    private final Sprite wndSelection;

    public BountyGround() {

        this.btnCampaign =
                new Sprite(
                        getAssetPath(CAMPAIGN_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CAMPAIGN_THRESHOLD,
                        getPushMsgLog(CAMPAIGN_NAME));

        this.btnBounty =
                new Sprite(
                        getAssetPath(BOUNTY_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOUNTY_THRESHOLD,
                        getPushMsgLog(BOUNTY_NAME));

        this.btnMatch =
                new Sprite(
                        getAssetPath(MATCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        MATCH_THRESHOLD,
                        getPushMsgLog(MATCH_NAME));

        this.btnChancel =
                new Sprite(
                        getAssetPath(CHANCEL_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CHANCEL_THRESHOLD,
                        getPushMsgLog(CHANCEL_NAME));

        this.wndProcess =
                new Sprite(getAssetPath(PROCESS_FILE), Imgproc.TM_CCOEFF_NORMED, PROCESS_THRESHOLD);

        this.wndSelection =
                new Sprite(
                        getAssetPath(SELECTION_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        SELECTION_THRESHOLD);
    }

    public void run(CommandService.StateToken state) {

        if (App.DEBUG) {
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }

        Mat mat = CommandService.takeScreenMat();

        if (wndProcess.find(mat) == null
                && wndSelection.find(mat) == null
                && btnCampaign.find(mat) != null) {
            App.sleep(1000);

            if (btnCampaign.pushIfExists(mat, 1500)) {
                if (btnBounty.pushIfExists(1500)) {
                    btnMatch.pushIfExists(1000);
                }
            }

            while (state.isRunning()) {
                mat = CommandService.takeScreenMat();

                if(btnBack.pushIfExists(mat, 1000)) {
                    continue;
                }
                if(btnChancel.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnHome.pushIfExists(mat, 1000)){
                    continue;
                }
                if(wndSelection.isFound(mat, 1000)){
                    continue;
                }
                if(wndProcess.isFound(mat)){
                    break;
                }
                if(!wndSelection.isFound(mat) && !wndProcess.isFound(mat) && 
                    btnRegion.isFound(mat) && btnCampaign.isFound(mat)){
                    break;
                }
            }
        }
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
