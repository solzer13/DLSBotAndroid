package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Police extends Module {
    
    private static final String TAG = "Police";
    private static final String KEY = "police";
    
    private static final String DRONE_FILE = "btn_drone.png";
    private static final double DRONE_THRESHOLD = 0.80;
    private static final String DRONE_NAME = "Полицейский участок";
    
    private static final String DEPLOY_FILE = "btn_deploy.png";
    private static final double DEPLOY_THRESHOLD = 0.98;
    private static final String DEPLOY_NAME = "Развернуть";

    private static final String SEARCH_FILE = "btn_search.png";
    private static final double SEARCH_THRESHOLD = 0.80;
    private static final String SEARCH_NAME = "Полицейский участок";

    private static final String SEARCH_YELLOW_FILE = "btn_search_yellow.png";
    private static final double SEARCH_YELLOW_THRESHOLD = 0.98;
    private static final String SEARCH_YELLOW_NAME = "Поиск";

    private final Sprite btnDrone;
    private final Sprite btnDeploy;
    private final Sprite btnSearch;
    private final Sprite btnSearchYellow;
    
    public Police(){
        this.btnDrone = new Sprite(
            getAssetPath(DRONE_FILE),
            Imgproc.TM_CCOEFF_NORMED,
                DRONE_THRESHOLD,
                getPushMsgLog(DRONE_NAME)
        );
        this.btnDeploy = new Sprite(
            getAssetPath(DEPLOY_FILE),
            Imgproc.TM_CCOEFF_NORMED,
                DEPLOY_THRESHOLD,
                getPushMsgLog(DEPLOY_NAME)
        );
        this.btnSearch = new Sprite(
                getAssetPath(SEARCH_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                SEARCH_THRESHOLD,
                getPushMsgLog(SEARCH_NAME)
        );
        this.btnSearchYellow = new Sprite(
                getAssetPath(SEARCH_YELLOW_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                SEARCH_YELLOW_THRESHOLD,
                getPushMsgLog(SEARCH_YELLOW_NAME)
        );
    }
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }

        if(btnDrone.pushIfExists(1500)){
            if(btnDeploy.pushIfExists(1500)){
                while(state.isRunning()){
                    Mat mat = CommandService.takeScreenMat();

                    if(btnBack.pushIfExists(mat, 1000)){
                        continue;
                    }

                    if(btnRegion.isFound(mat)){
                        break;
                    }
                }
            }
        }

        if(btnSearch.pushIfExists(1500)){
            btnSearchYellow.pushIfExists(1500);
            while(state.isRunning()) {
                Mat mat = CommandService.takeScreenMat();

                if(btnSearchYellow.pushIfExists(mat, 1500)){
                    continue;
                }
                
                if(btnOkYellow.pushIfExists(mat, 1500)){
                    continue;
                }

                if(btnBack.pushIfExists(mat, 1500)){
                    continue;
                }

                if(btnRegion.isFound(mat)){
                    break;
                }
                App.sleep(1000);
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
