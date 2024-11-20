package xxx.solzer.dlsbot.modules;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Radar extends Module {
    
    private static final String TAG = "Radar";
    private static final String KEY = "radar";

    private static final String RADAR_FILE = "radar.png";
    private static final double RADAR_THRESHOLD = 0.9;
    private static final String RADAR_NAME = "Радар";

    private static final String BOX_VIOLET_FILE = "box_violet.png";
    private static final double BOX_VIOLET_THRESHOLD = 0.6;
    private static final String BOX_VIOLET_NAME = "Парашют";

    private static final String BOX_GOLD_FILE = "box_gold.png";
    private static final double BOX_GOLD_THRESHOLD = 0.6;
    private static final String BOX_GOLD_NAME = "Парашют";
    
    private static final String TRUCK_FILE = "truck_gold.png";
    private static final double TRUCK_THRESHOLD = 0.55;
    private static final String TRUCK_NAME = "Грузовик";
    
    private static final String COLLECT_FILE = "collect.png";
    private static final double COLLECT_THRESHOLD = 0.90;
    private static final String COLLECT_NAME = "Собрать";

    private static final String NEXT_FILE = "next.png";
    private static final double NEXT_THRESHOLD = 0.90;
    private static final String NEXT_NAME = "Вперед";

    private static final String TRANSPORT_FILE = "transport.png";
    private static final double TRANSPORT_THRESHOLD = 0.90;
    private static final String TRANSPORT_NAME = "Транспортировать";

    private final Sprite btnRadar;
    private final Sprite btnBoxViolet;
    private final Sprite btnBoxGold;
    private final Sprite btnTruck;
    private final Sprite btnCollect;
    private final Sprite btnNext;
    private final Sprite btnTransport;

    public Radar(){
        this.btnRadar =
                new Sprite(
                        getAssetPath(RADAR_FILE),
                        Imgproc.TM_CCORR_NORMED,
                        RADAR_THRESHOLD,
                        getPushMsgLog(RADAR_NAME));
        this.btnBoxViolet =
                new Sprite(
                        getAssetPath(BOX_VIOLET_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOX_VIOLET_THRESHOLD,
                        getPushMsgLog(BOX_VIOLET_NAME));
        this.btnBoxGold =
                new Sprite(
                        getAssetPath(BOX_GOLD_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOX_GOLD_THRESHOLD,
                        getPushMsgLog(BOX_GOLD_NAME));
        this.btnTruck =
                new Sprite(
                        getAssetPath(TRUCK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TRUCK_THRESHOLD,
                        getPushMsgLog(TRUCK_NAME));
        this.btnCollect =
                new Sprite(
                        getAssetPath(COLLECT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        COLLECT_THRESHOLD,
                        getPushMsgLog(COLLECT_NAME));
        this.btnNext =
                new Sprite(
                        getAssetPath(NEXT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        NEXT_THRESHOLD,
                        getPushMsgLog(NEXT_NAME));
        this.btnTransport =
                new Sprite(
                        getAssetPath(TRANSPORT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TRANSPORT_THRESHOLD,
                        getPushMsgLog(TRANSPORT_NAME));
    }

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(btnRadar.pushIfExists(2000)){
            
            while (state.isRunning()) {
                if(btnBoxViolet.pushIfExists(1500) || btnBoxGold.pushIfExists(1500)){
                    if(btnNext.pushIfExists(2000)){
                        if(btnCollect.pushIfExists(2000)){
                            if(btnRadar.pushIfExists(2000)){
                                continue;
                            }
                        }
                    }
                }
                if(btnTruck.pushIfExists(1500)){
                    if(btnNext.pushIfExists(2000)){
                        if(btnTransport.pushIfExists(2000)){
                            if(btnOkYellow.pushIfExists(1500)){
                                if(btnRadar.pushIfExists(2000)){
                                    continue;
                                }
                            }
                        }
                    }
                }
                else {
                    break;
                }
            }
            
            while (state.isRunning()) {
                Mat mat = CommandService.takeScreenMat();

                if(btnBack.pushIfExists(mat, 1000)) {
                    continue;
                }
                if(btnCloseDark.pushIfExists(mat, 1000)) {
                    continue;
                }
                if(btnHome.pushIfExists(mat, 2000)) {
                    continue;
                }
                if(btnRegion.isFound(mat)){
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
