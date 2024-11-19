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

public class Help extends Module {
    
    private static final String TAG = "Help";
    private static final String KEY = "help";

    private static final String HELP_FILE = "btn_help.png";
    private static final double HELP_THRESHOLD = 0.9;
    private static final String HELP_NAME = "Помощь";

    private final Sprite btnHelp;

    public Help(){
        this.btnHelp =
                new Sprite(
                        getAssetPath(HELP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        HELP_THRESHOLD,
                        getPushMsgLog(HELP_NAME));

    }

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        btnHelp.pushIfExists(1000);
    }
    
    public String getKey(){
        return KEY;
    }

    public String getTag() {
        return TAG;
    }
}
