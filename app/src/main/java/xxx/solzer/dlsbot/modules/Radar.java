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
    private static final double RADAR_THRESHOLD = 0.6;
    private static final String RADAR_NAME = "Радар";

    private static final String BOX_VIOLET_FILE = "box_violet.png";
    private static final double BOX_VIOLET_THRESHOLD = 0.55;
    private static final String BOX_VIOLET_NAME = "Парашют";

    private static final String BOX_GOLD_FILE = "box_gold.png";
    private static final double BOX_GOLD_THRESHOLD = 0.55;
    private static final String BOX_GOLD_NAME = "Парашют";

    private static final String BOX_RED_FILE = "box_red.png";
    private static final double BOX_RED_THRESHOLD = 0.55;
    private static final String BOX_RED_NAME = "Парашют";
    
    private static final String TRUCK_RED_FILE = "truck_red.png";
    private static final double TRUCK_RED_THRESHOLD = 0.55;
    private static final String TRUCK_RED_NAME = "Грузовик";
    
    private static final String TRUCK_GOLD_FILE = "truck_gold.png";
    private static final double TRUCK_GOLD_THRESHOLD = 0.55;
    private static final String TRUCK_GOLD_NAME = "Грузовик";
    
    private static final String TRUCK_VIOLET_FILE = "truck_violet.png";
    private static final double TRUCK_VIOLET_THRESHOLD = 0.55;
    private static final String TRUCK_VIOLET_NAME = "Грузовик";
    
    private static final String COLLECT_FILE = "collect.png";
    private static final double COLLECT_THRESHOLD = 0.8;
    private static final String COLLECT_NAME = "Собрать";

    private static final String NEXT_FILE = "next.png";
    private static final double NEXT_THRESHOLD = 0.8;
    private static final String NEXT_NAME = "Вперед";

    private static final String TRANSPORT_FILE = "transport.png";
    private static final double TRANSPORT_THRESHOLD = 0.8;
    private static final String TRANSPORT_NAME = "Транспортировать";

    private static final String CLOSE_FILE = "close.png";
    private static final double CLOSE_THRESHOLD = 0.8;
    private static final String CLOSE_NAME = "Закрыть окно";

    private final Sprite btnRadar;
    private final Sprite btnBoxRed;
    private final Sprite btnBoxGold;
    private final Sprite btnBoxViolet;
    private final Sprite btnTruckRed;
    private final Sprite btnTruckGold;
    private final Sprite btnTruckViolet;
    private final Sprite btnCollect;
    private final Sprite btnNext;
    private final Sprite btnTransport;
    private final Sprite btnClose;

    public Radar(){
        this.btnRadar =
                new Sprite(
                        getAssetPath(RADAR_FILE),
                        Imgproc.TM_CCORR_NORMED,
                        RADAR_THRESHOLD,
                        getPushMsgLog(RADAR_NAME));
        this.btnBoxRed =
                new Sprite(
                        getAssetPath(BOX_RED_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOX_RED_THRESHOLD,
                        getPushMsgLog(BOX_RED_NAME));
        this.btnBoxGold =
                new Sprite(
                        getAssetPath(BOX_GOLD_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOX_GOLD_THRESHOLD,
                        getPushMsgLog(BOX_GOLD_NAME));
        this.btnBoxViolet =
                new Sprite(
                        getAssetPath(BOX_VIOLET_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BOX_VIOLET_THRESHOLD,
                        getPushMsgLog(BOX_VIOLET_NAME));
        this.btnTruckRed =
                new Sprite(
                        getAssetPath(TRUCK_RED_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TRUCK_RED_THRESHOLD,
                        getPushMsgLog(TRUCK_RED_NAME));
        this.btnTruckGold =
                new Sprite(
                        getAssetPath(TRUCK_GOLD_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TRUCK_GOLD_THRESHOLD,
                        getPushMsgLog(TRUCK_GOLD_NAME));
        this.btnTruckViolet =
                new Sprite(
                        getAssetPath(TRUCK_VIOLET_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TRUCK_VIOLET_THRESHOLD,
                        getPushMsgLog(TRUCK_VIOLET_NAME));
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
        this.btnClose =
                new Sprite(
                        getAssetPath(CLOSE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CLOSE_THRESHOLD,
                        getPushMsgLog(CLOSE_NAME));
    }

    public void run(CommandService.StateToken state, Mat mat) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(btnRadar.pushIfExists(mat, 2000)){
            
            while (state.isRunning()) {
                mat = CommandService.takeScreenMat();
                if(btnBoxRed.pushIfExists(mat) || btnBoxGold.pushIfExists(mat) || btnBoxViolet.pushIfExists(mat)){
                    if(btnNext.pushTimeout(state)){
                        if(btnCollect.pushTimeout(state)){
                            if(btnRadar.pushTimeout(state)){
                                continue;
                            }
                        }
                    }
                }
                else if(btnTruckRed.pushIfExists(mat) || btnTruckGold.pushIfExists(mat) || btnTruckViolet.pushIfExists(mat)){
                    if(btnNext.pushTimeout(state)){
                        if(btnTransport.pushTimeout(state)){
                            if(btnOk.pushTimeout(state)){
                                if(btnRadar.pushTimeout(state)){
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
                Mat mat_exit = CommandService.takeScreenMat();

                if(btnBack.pushIfExists(mat_exit, 1000)) {
                    continue;
                }
                if(btnClose.pushIfExists(mat_exit, 1000)) {
                    continue;
                }
                if(btnHome.pushIfExists(mat_exit, 2000)) {
                    continue;
                }
                if(btnRegion.isFound(mat_exit)){
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
