package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Police extends Module {
    
    private static final String TAG = "Police";
    
    private static final String KEY = "police";

    private static final String BTN_SEARCH_FILE = "btn_search.png";
    private static final String BTN_SEARCH_YELLOW_FILE = "btn_search_yellow.png";

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
        App.bus.post(new OnUserLog(TAG + ": point01"));
        
        Point btn_search_yellow_loc = findSearchYellowButton(CommandService.takeScreenMat());
        if(btn_search_yellow_loc != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Поиск\""));
            App.bus.post(new OnTap(btn_search_yellow_loc));
        }
        
        App.bus.post(new OnUserLog(TAG + ": point02"));
        
        while(state.isRunning()) {
            try{Thread.sleep(1500);}catch(Exception e){}
            
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
                App.bus.post(new OnUserLog(TAG + ": break"));
                break;
            }
        }
    }
    
    public String getKey(){
        return KEY;
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
