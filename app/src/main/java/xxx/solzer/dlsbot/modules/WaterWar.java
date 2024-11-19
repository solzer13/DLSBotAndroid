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

public class WaterWar extends Module {
    
    public static final String TAG = "WaterWar";
    public static final String KEY = "water";
    
    private static final String BANNER_FILE = "baner_ww.png";
    private static final double BANNER_THRESHOLD = 0.98;
    private static final String BANNER_NAME = "Банер";
    
    private static final String WATER_LIGHT_FILE = "btn_ww_light.png";
    private static final double WATER_LIGHT_THRESHOLD = 0.98;
    private static final String WATER_LIGHT_NAME = "Война за воду";
    
    private static final String WATER_DARK_FILE = "btn_ww_dark.png";
    private static final double WATER_DARK_THRESHOLD = 0.98;
    private static final String WATER_DARK_NAME = "Война за воду";
    
    private static final String PICK_FILE = "btn_ww_pick.png";
    private static final double PICK_THRESHOLD = 0.98;
    private static final String PICK_NAME = "Подобрать";
    
    private static final String READY_FILE = "btn_ww_ready.png";
    private static final double READY_THRESHOLD = 0.98;
    private static final String READY_NAME = "Готово";
    
    private static final String VS_FILE = "bg_ww_vs.png";
    private static final double VS_THRESHOLD = 0.9;
    
    private static final String END_FILE = "bg_ww_end.png";
    private static final double END_THRESHOLD = 0.98;
    
    private static final String SKIP_FILE = "btn_ww_skip.png";
    private static final double SKIP_THRESHOLD = 0.98;
    private static final String SKIP_NAME = "Пропустить";
    
    private static final String EXIT_FILE = "btn_ww_exit.png";
    private static final double EXIT_THRESHOLD = 0.98;
    private static final String EXIT_NAME = "Выход";
    
    private final Sprite btnBanner;
    private final Sprite btnWaterLight;
    private final Sprite btnWaterDark;
    private final Sprite btnPick;
    private final Sprite btnReady;
    private final Sprite btnVS;
    private final Sprite wndEnd;
    private final Sprite btnSkip;
    private final Sprite btnExit;
    
    public WaterWar() {
        this.btnBanner =
                new Sprite(
                        getAssetPath(BANNER_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BANNER_THRESHOLD,
                        getPushMsgLog(BANNER_NAME));
        this.btnWaterDark =
                new Sprite(
                        getAssetPath(WATER_DARK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WATER_DARK_THRESHOLD,
                        getPushMsgLog(WATER_DARK_NAME));
        this.btnWaterLight =
                new Sprite(
                        getAssetPath(WATER_LIGHT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WATER_LIGHT_THRESHOLD,
                        getPushMsgLog(WATER_LIGHT_NAME));
        this.btnPick =
                new Sprite(
                        getAssetPath(PICK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        PICK_THRESHOLD,
                        getPushMsgLog(PICK_NAME));
        this.btnReady =
                new Sprite(
                        getAssetPath(READY_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        READY_THRESHOLD,
                        getPushMsgLog(READY_NAME));
        this.btnVS =
                new Sprite(
                        getAssetPath(VS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        VS_THRESHOLD);
        this.wndEnd =
                new Sprite(
                        getAssetPath(END_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        END_THRESHOLD);
        this.btnSkip =
                new Sprite(
                        getAssetPath(SKIP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        SKIP_THRESHOLD,
                        getPushMsgLog(SKIP_NAME));
        this.btnExit =
                new Sprite(
                        getAssetPath(EXIT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        EXIT_THRESHOLD,
                        getPushMsgLog(EXIT_NAME));
    }
    
    @Override
    public void run(CommandService.StateToken state) {
        if (App.DEBUG) {
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        int counter = 0;
        
        while(state.isRunning()){
            if(btnBanner.pushIfExists(1000)){
                break;
            }
            if(counter > 5){
                return;
            }
            counter++;
        }

        Mat mat = CommandService.takeScreenMat();

        if(btnWaterDark.pushIfExists(mat, 1000)){
            mat = CommandService.takeScreenMat();
        }

        if(btnPick.pushIfExists(mat, 1000)){
            while(state.isRunning()){
                mat = CommandService.takeScreenMat();
                if(btnReady.pushIfExists(mat,1000)){
                    continue;
                }
                if(btnVS.isFound(mat)){
                    break;
                }
                App.sleep(1000);
            }
            while(state.isRunning()){
                mat = CommandService.takeScreenMat();
                if(btnVS.pushIfExists(mat,2000)){
                    continue;
                }
                if(wndEnd.isFound(mat)){
                    logUserMsg("Матч завершен");
                    break;
                }
                App.sleep(2000);
            }
            while(state.isRunning()){
                mat = CommandService.takeScreenMat();
                if(wndEnd.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnSkip.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnExit.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnBack.pushIfExists(mat, 1000)){
                    continue;
                }
                if(btnRegion.isFound(mat)){
                    break;
                }
                App.sleep(1000);
            }
        }
    }

    @Override
    public String getKey(){
        return KEY;
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
