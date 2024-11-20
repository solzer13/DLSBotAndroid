package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnUserLog;

public class AirDrop extends Module {
    
    private static final String TAG = "AirDrop";
    private static final String KEY = "airdrop";

    private static final String AIRDROP_FILE = "airdrop.png";
    private static final double AIRDROP_THRESHOLD = 0.9;
    private static final String AIRDROP_NAME = "Эйрдроп";

    private static final String CLOSE_FILE = "close.png";
    private static final double CLOSE_THRESHOLD = 0.9;
    private static final String CLOSE_NAME = "Закрыть";

    private final Sprite btnAirDrop;
    private final Sprite btnClose;

    public AirDrop(){
        this.btnAirDrop =
                new Sprite(
                        getAssetPath(AIRDROP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        AIRDROP_THRESHOLD,
                        getPushMsgLog(AIRDROP_NAME));
        this.btnClose =
                new Sprite(
                        getAssetPath(CLOSE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CLOSE_THRESHOLD,
                        getPushMsgLog(CLOSE_NAME));

    }

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(btnAirDrop.pushIfExists(1500)){

            while (state.isRunning()) {
                Mat mat = CommandService.takeScreenMat();

                btnClaim.pushIfExists(mat, 1000);
                
                if(btnClose.pushIfExists(mat, 1000)){
                    break;
                }
            }
        }
    }
    
    public String getKey(){
        return KEY;
    }

    public String getTag() {
        return TAG;
    }
}
