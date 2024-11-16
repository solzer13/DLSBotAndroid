package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Help extends Module {
    
    private static final String TAG = "Help";
    
    private static final String KEY = "help";

    private static final String BTN_HELP_FILE = "btn_help.png";

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        Mat mat = CommandService.takeScreenMat();
            
        Point btn_help_loc = findHelpButton(mat);
    
        if(btn_help_loc != null){
            App.bus.post(new OnUserLog(TAG + ": Жмем кнопку \"Помощь\""));
            App.bus.post(new OnTap(btn_help_loc));
        }
    }
    
    public String getKey(){
        return KEY;
    }
        
    private Point findHelpButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_HELP_FILE), 2.3E7);
    }

    private String getAssetFilePath(String file){
        return App.getAssetDirName() + "/" + KEY + "/" + file;
    }
   
}
