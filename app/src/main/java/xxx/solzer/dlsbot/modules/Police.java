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
    
    private static final String BTN_DRONE_FILE = "btn_drone.png";
    private static final double BTN_DRONE_THRESHOLD = 0.95;
    
    private static final String BTN_DEPLOY_FILE = "btn_deploy.png";
    private static final double BTN_DEPLOY_THRESHOLD = 0.98;

    private static final String BTN_SEARCH_FILE = "btn_search.png";
    private static final double BTN_SEARCH_THRESHOLD = 0.98;

    private static final String BTN_SEARCH_YELLOW_FILE = "btn_search_yellow.png";
    private static final double BTN_SEARCH_YELLOW_THRESHOLD = 0.98;

    private final Sprite btnDrone;
    private final Sprite btnDeploy;
    private final Sprite btnSearch;
    private final Sprite btnSearchYellow;
    
    public Police(){
        this.btnDrone = new Sprite(
            getAssetPath(BTN_DRONE_FILE), 
            Imgproc.TM_CCOEFF_NORMED,
            BTN_DRONE_THRESHOLD
        );
        this.btnDeploy = new Sprite(
            getAssetPath(BTN_DEPLOY_FILE), 
            Imgproc.TM_CCOEFF_NORMED,
            BTN_DEPLOY_THRESHOLD
        );
        this.btnSearch = new Sprite(
                getAssetPath(BTN_SEARCH_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_SEARCH_THRESHOLD
        );
        this.btnSearchYellow = new Sprite(
                getAssetPath(BTN_SEARCH_YELLOW_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_SEARCH_YELLOW_THRESHOLD
        );
    }
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }

        if(btnDrone.pushIfExists("Полицейский участок", 1500)){
            if(btnDeploy.pushIfExists("Развернуть", 1500)){
                while(state.isRunning()){
                    Mat mat = CommandService.takeScreenMat();

                    if(btnBack.pushIfExists(mat, "Назад", 1000)){
                        continue;
                    }

                    if(isMainWindow(mat)){
                        break;
                    }
                }
            }
        }

        if(btnSearch.pushIfExists("Полицейский участок", 1500)){
            btnSearchYellow.pushIfExists("Поиск", 1500);
            while(state.isRunning()) {
                Mat mat = CommandService.takeScreenMat();

                Point btn_ok_yellow_point = btnOkYellow.find(mat);
                if(btn_ok_yellow_point != null){
                    btnOkYellow.push(btn_ok_yellow_point, TAG + ": Жмем кнопку \"Ok\"");
                    continue;
                }

                Point btn_back_point = btnBack.find(mat);
                if(btn_back_point != null){
                    btnOkYellow.push(btn_back_point, TAG + ": Жмем кнопку \"Назад\"");
                    continue;
                }

                if(isMainWindow(mat)){
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
