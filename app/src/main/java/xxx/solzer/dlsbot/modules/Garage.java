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

public class Garage extends Module {
    
    private static final String TAG = "Garage";
    private static final String KEY = "garage";

    private static final String GARAGE_FILE = "garage.png";
    private static final double GARAGE_THRESHOLD = 0.75;
    private static final String GARAGE_NAME = "Гараж";

    private static final String DETAILS_GOLD_FILE = "details_gold.png";
    private static final double DETAILS_GOLD_THRESHOLD = 0.8;
    private static final String DETAILS_GOLD_NAME = "Собрать";

    private static final String DETAILS_VIOLET_FILE = "details_violet.png";
    private static final double DETAILS_VIOLET_THRESHOLD = 0.8;
    private static final String DETAILS_VIOLET_NAME = "Собрать";

    private static final String DETAILS_BLUE_FILE = "details_blue.png";
    private static final double DETAILS_BLUE_THRESHOLD = 0.8;
    private static final String DETAILS_BLUE_NAME = "Собрать";

    private static final String CONGRATS_FILE = "congrats.png";
    private static final double CONGRATS_THRESHOLD = 0.8;
    private static final String CONGRATS_NAME = "Пустое место";

    private static final String PRODUCE_FILE = "produce.png";
    private static final double PRODUCE_THRESHOLD = 0.8;
    private static final String PRODUCE_NAME = "Произвести";

    private final Sprite btnGarage;
    private final Sprite btnDetailsGold;
    private final Sprite btnDetailsViolet;
    private final Sprite btnDetailsBlue;
    private final Sprite btnCongrats;
    private final Sprite btnProduce;

    public Garage(){
        btnGarage =
                new Sprite(
                        getAssetPath(GARAGE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        GARAGE_THRESHOLD,
                        getPushMsgLog(GARAGE_NAME));
        btnGarage.setScale(App.getCurrentScreen().scaleMap);

        btnDetailsGold =
                new Sprite(
                        getAssetPath(DETAILS_GOLD_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        DETAILS_GOLD_THRESHOLD,
                        getPushMsgLog(DETAILS_GOLD_NAME));
  
        btnDetailsViolet =
                new Sprite(
                        getAssetPath(DETAILS_VIOLET_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        DETAILS_VIOLET_THRESHOLD,
                        getPushMsgLog(DETAILS_VIOLET_NAME));
        
        btnDetailsBlue =
                new Sprite(
                        getAssetPath(DETAILS_BLUE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        DETAILS_BLUE_THRESHOLD,
                        getPushMsgLog(DETAILS_BLUE_NAME));
        
         btnCongrats =
                new Sprite(
                        getAssetPath(CONGRATS_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CONGRATS_THRESHOLD,
                        getPushMsgLog(CONGRATS_NAME));
       
        btnProduce =
                new Sprite(
                        getAssetPath(PRODUCE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        PRODUCE_THRESHOLD,
                        getPushMsgLog(PRODUCE_NAME));
    }

    public void run(CommandService.StateToken state) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        if(btnGarage.pushIfExists(1500)){
            if(btnDetailsGold.pushIfExists(1500) || btnDetailsViolet.pushIfExists(1500) || btnDetailsBlue.pushIfExists(1500)){
                btnCongrats.pushIfExists(2000);
            }
            
            while(btnProduce.pushIfExists(500));

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
