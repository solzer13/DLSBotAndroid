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

public class DonateTechnologies extends Module {
    
    private static final String TAG = "DonateTechnologies";
    private static final String KEY = "donate_technologies";

    private static final String TECHNOLOGIES_FILE = "technologies.png";
    private static final double TECHNOLOGIES_THRESHOLD = 0.85;
    private static final String TECHNOLOGIES_NAME = "Технологии";

    private static final String SELECTED_FILE = "selected.png";
    private static final double SELECTED_THRESHOLD = 0.8;
    private static final String SELECTED_NAME = "Отмеченая технология";

    private static final String DONATE_ENABLE_FILE = "donate_enable.png";
    private static final double DONATE_ENABLE_THRESHOLD = 0.85;
    private static final String DONATE_ENABLE_NAME = "Пожертвовать";

    private static final String DONATE_DISABLE_FILE = "donate_disable.png";
    private static final double DONATE_DISABLE_THRESHOLD = 0.85;

    private final Sprite btnTechnologies;
    private final Sprite btnSelected;
    private final Sprite btnDonateEnable;
    private final Sprite btnDonateDisable;

    public DonateTechnologies(){
        btnTechnologies =
                new Sprite(
                        getAssetPath(TECHNOLOGIES_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TECHNOLOGIES_THRESHOLD,
                        getPushMsgLog(TECHNOLOGIES_NAME));
        
        btnSelected =
                new Sprite(
                        getAssetPath(SELECTED_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        SELECTED_THRESHOLD,
                        getPushMsgLog(SELECTED_NAME));
        
        btnDonateEnable =
                new Sprite(
                        getAssetPath(DONATE_ENABLE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        DONATE_ENABLE_THRESHOLD,
                        getPushMsgLog(DONATE_ENABLE_NAME));
        
        btnDonateDisable =
                new Sprite(
                        getAssetPath(DONATE_DISABLE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        DONATE_DISABLE_THRESHOLD);

    }

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(btnAlliance.pushIfExists(1500)){
            if(btnTechnologies.pushIfExists(1500)){
                if(btnSelected.pushIfExists(1500)){
                    while(state.isRunning()) {
                    	Mat mat = CommandService.takeScreenMat();
                        if(btnDonateEnable.pushIfExists(mat, 500)){
                            continue;
                        }
                        if(btnDonateDisable.isFound(mat, 1000)){
                            break;
                        }
                        App.sleep(1000);
                    }
                }
            }
            
            while (state.isRunning()) {
                Mat mat = CommandService.takeScreenMat();

                if(btnBack.pushIfExists(mat, 1000)) {
                    continue;
                }
                if(btnCloseDark.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnHome.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnRegion.isFound(mat, 1000)){
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
