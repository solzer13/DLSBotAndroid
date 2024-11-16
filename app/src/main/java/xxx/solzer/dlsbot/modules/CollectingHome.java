package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class CollectingHome extends Module {
    
    private static final String TAG = "CollectingHome";
    
    private static final String KEY = "collecting_home";

    private static final String BTN_TOMATOES_FILE = "btn_tomatoes.png";
    private static final String BTN_WOOD_FILE = "btn_wood.png";
    private static final String BTN_STEEL_FILE = "btn_steel.png";
    private static final String BTN_OIL_FILE = "btn_oil.png";

    public void run(CommandService.StateToken state) {
    
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }

        Point btn_tomatoes_loc = findTomatoesButton(CommandService.takeScreenMat());
        
        if(btn_tomatoes_loc != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку сбора помидор"));
            App.bus.post(new OnTap(btn_tomatoes_loc));
            try{Thread.sleep(1000);}catch(Exception e){}
        }
    }
    
    public String getKey(){
        return KEY;
    }
        
    private Point findTomatoesButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_TOMATOES_FILE), 1.4E7);
    }
        
    private Point findWoodButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_WOOD_FILE), 6161646.5);
    }
        
    private Point findSteelButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_STEEL_FILE), 1.1E7);
    }
        
    private Point findOilButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_OIL_FILE), 1.1E7);
    }

    private String getAssetFilePath(String file){
        return App.getAssetDirName() + "/" + KEY + "/" + file;
    }
   
}
