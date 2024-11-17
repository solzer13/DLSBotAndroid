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
    private static final String BTN_SEARCH_YELLOW_FILE = "btn_search_yellow.png";

    private final Sprite btnDrone;
    private final Sprite btnDeploy;
    
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
    }
    
    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        
        Point btn_search_loc = findSearchButton(CommandService.takeScreenMat());
        if(btn_search_loc != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Полицейский участок\""));
            App.bus.post(new OnTap(btn_search_loc));
        }
        
        try{Thread.sleep(1500);}catch(Exception e){}
        
        Point btn_search_yellow_loc = findSearchYellowButton(CommandService.takeScreenMat());
        if(btn_search_yellow_loc != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Поиск\""));
            App.bus.post(new OnTap(btn_search_yellow_loc));
        }
        
        while(state.isRunning()) {
            try{Thread.sleep(1500);}catch(Exception ignored){}
            
            Mat mat = CommandService.takeScreenMat();
            
            Point btn_ok_yellow_loc = findOkYellowButton(mat);
            if(btn_ok_yellow_loc != null){
                App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Ok\""));
                App.bus.post(new OnTap(btn_ok_yellow_loc));
                continue;
            }

            Point btn_back_loc = findBackButton(mat);
            if(btn_back_loc != null){
                App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Назад\""));
                App.bus.post(new OnTap(btn_back_loc));
                continue;
            }
            
            if(isMainWindow(mat)){
                break;
            }
        }
    }
    
    public String getKey(){
        return KEY;
    }

    public String getTag() {
        return TAG;
    }

    private Point findSearchButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_SEARCH_FILE), 7800000);
    }
    
    private Point findSearchYellowButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_SEARCH_YELLOW_FILE), 2.5E7);
    }
    
    private String getAssetFilePath(String file){
        return App.getAssetDirName() + "/" + KEY + "/" + file;
    }
   
}
