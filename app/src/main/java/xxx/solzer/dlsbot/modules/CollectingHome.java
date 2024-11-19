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

public class CollectingHome extends Module {
    
    private static final String TAG = "CollectingHome";
    private static final String KEY = "collecting_home";

    private static final String TOMATOES_FILE = "btn_tomatoes.png";
    private static final double TOMATOES_THRESHOLD = 0.9;
    private static final String TOMATOES_NAME = "Собрать помидоры";

    private static final String WOOD_FILE = "btn_wood.png";
    private static final double WOOD_THRESHOLD = 0.9;
    private static final String WOOD_NAME = "Собрать древесину";

    private static final String STEEL_FILE = "btn_steel.png";
    private static final double STEEL_THRESHOLD = 0.9;
    private static final String STEEL_NAME = "Собрать сталь";

    private static final String OIL_FILE = "btn_oil.png";
    private static final double OIL_THRESHOLD = 0.9;
    private static final String OIL_NAME = "Собрать нефть";

    private final Sprite btnTomatoes;
    private final Sprite btnWood;
    private final Sprite btnSteel;
    private final Sprite btnOil;

    public CollectingHome(){

        this.btnTomatoes =
                new Sprite(
                        getAssetPath(TOMATOES_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TOMATOES_THRESHOLD,
                        getPushMsgLog(TOMATOES_NAME));

        this.btnWood =
                new Sprite(
                        getAssetPath(WOOD_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WOOD_THRESHOLD,
                        getPushMsgLog(WOOD_NAME));

        this.btnSteel =
                new Sprite(
                        getAssetPath(STEEL_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        STEEL_THRESHOLD,
                        getPushMsgLog(STEEL_NAME));

        this.btnOil =
                new Sprite(
                        getAssetPath(OIL_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        OIL_THRESHOLD,
                        getPushMsgLog(OIL_NAME));

    }

    @Override
    public void run(CommandService.StateToken state) {
    
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }

        Mat mat = CommandService.takeScreenMat();

        if(btnTomatoes.pushIfExists(mat, 1000)){
            return;
        }

        if(btnWood.pushIfExists(mat, 1000)){
            return;
        }

        if(btnSteel.pushIfExists(mat, 1000)){
            return;
        }

        if(btnOil.pushIfExists(mat, 1000)){
            return;
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
