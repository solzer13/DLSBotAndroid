package xxx.solzer.dlsbot.modules;

import android.content.SharedPreferences;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.App;
import xxx.solzer.dlsbot.CommandService;
import xxx.solzer.dlsbot.Module;
import xxx.solzer.dlsbot.Sprite;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Farming extends Module {
    
    private static final String TAG = "Farming";
    private static final String KEY = "farming";

    private static final String SEARCH_ICON_FILE = "search_icon.png";
    private static final double SEARCH_ICON_THRESHOLD = 0.8;
    private static final String SEARCH_ICON_NAME = "Поиск (иконка)";

    private static final String SEARCH_BUTTON_FILE = "search_button.png";
    private static final double SEARCH_BUTTON_THRESHOLD = 0.8;
    private static final String SEARCH_BUTTON_NAME = "Поиск";

    private static final String ONE_5_FILE = "1_5.png";
    private static final double ONE_5_THRESHOLD = 0.9;
    
    private static final String TWO_5_FILE = "2_5.png";
    private static final double TWO_5_THRESHOLD = 0.9;
    
    private static final String THREE_5_FILE = "3_5.png";
    private static final double THREE_5_THRESHOLD = 0.9;
    
    private static final String FOUR_5_FILE = "4_5.png";
    private static final double FOUR_5_THRESHOLD = 0.9;
    
    private static final String FIVE_5_FILE = "5_5.png";
    private static final double FIVE_5_THRESHOLD = 0.9;
    
    private static final String TOMATOES_SEARCH_FILE = "tomatoes_search.png";
    private static final double TOMATOES_SEARCH_THRESHOLD = 0.9;
    private static final String TOMATOES_SEARCH_NAME = "Поиск помидор";

    private static final String WOOD_SEARCH_FILE = "wood_search.png";
    private static final double WOOD_SEARCH_THRESHOLD = 0.9;
    private static final String WOOD_SEARCH_NAME = "Поиск дерева";

    private static final String STEEL_SEARCH_FILE = "steel_search.png";
    private static final double STEEL_SEARCH_THRESHOLD = 0.9;
    private static final String STEEL_SEARCH_NAME = "Поиск стали";

    private static final String OIL_SEARCH_FILE = "oil_search.png";
    private static final double OIL_SEARCH_THRESHOLD = 0.9;
    private static final String OIL_SEARCH_NAME = "Поиск нефти";

    private static final String COLLECT_FILE = "collect.png";
    private static final double COLLECT_THRESHOLD = 0.8;
    private static final String COLLECT_NAME = "Собрать";

    private static final String ARROW_FILE = "arrow.png";
    private static final double ARROW_THRESHOLD = 0.7;
    private static final String ARROW_NAME = "Ресурсная плитка";

    private static final String CREATE_TROOP_FILE = "create_troop.png";
    private static final double CREATE_TROOP_THRESHOLD = 0.7;
    private static final String CREATE_TROOP_NAME = "Создать отряд";

    private static final String MARCH_FILE = "march.png";
    private static final double MARCH_THRESHOLD = 0.7;
    private static final String MARCH_NAME = "Марш";

    private final Sprite btnSearchIcon;
    private final Sprite btnSearch;
    private final Sprite busyOne;
    private final Sprite busyTwo;
    private final Sprite busyThree;
    private final Sprite busyFour;
    private final Sprite busyFive;
    private final Sprite btnSearchTomatoes;
    private final Sprite btnSearchWood;
    private final Sprite btnSearchSteel;
    private final Sprite btnSearchOil;
    private final Sprite btnCollect;
    private final Sprite btnArrow;
    private final Sprite btnCreateTroop;
    private final Sprite btnMarch;
    
    private final int farmingCount;
    private final boolean farmingTomatoes;
    private final boolean farmingWood;
    private final boolean farmingSteel;
    private final boolean farmingOil;
    
    public Farming(){
        var prefs = App.getPreferences();
        
        farmingCount = prefs.getInt("farming_count", 0);
        farmingTomatoes = prefs.getBoolean("farming_tomatoes", true);
        farmingWood = prefs.getBoolean("farming_wood", true);
        farmingSteel = prefs.getBoolean("farming_steel", true);
        farmingOil = prefs.getBoolean("farming_oil", true);
        
        btnSearch =
                new Sprite(
                        getAssetPath(SEARCH_BUTTON_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        SEARCH_BUTTON_THRESHOLD,
                        getPushMsgLog(SEARCH_BUTTON_NAME));
        
        btnSearchIcon =
                new Sprite(
                        getAssetPath(SEARCH_ICON_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        SEARCH_ICON_THRESHOLD,
                        getPushMsgLog(SEARCH_ICON_NAME));

        busyOne =
                new Sprite(
                        getAssetPath(ONE_5_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        ONE_5_THRESHOLD);
        
        busyTwo =
                new Sprite(
                        getAssetPath(TWO_5_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TWO_5_THRESHOLD);
        
        busyThree =
                new Sprite(
                        getAssetPath(THREE_5_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        THREE_5_THRESHOLD);
        
        busyFour =
                new Sprite(
                        getAssetPath(FOUR_5_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        FOUR_5_THRESHOLD);
        
        busyFive =
                new Sprite(
                        getAssetPath(FIVE_5_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        FIVE_5_THRESHOLD);
                
        btnSearchTomatoes =
                new Sprite(
                        getAssetPath(TOMATOES_SEARCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TOMATOES_SEARCH_THRESHOLD,
                        getPushMsgLog(TOMATOES_SEARCH_NAME));
        
        btnSearchWood =
                new Sprite(
                        getAssetPath(WOOD_SEARCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WOOD_SEARCH_THRESHOLD,
                        getPushMsgLog(WOOD_SEARCH_NAME));
        
        btnSearchSteel =
                new Sprite(
                        getAssetPath(STEEL_SEARCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        STEEL_SEARCH_THRESHOLD,
                        getPushMsgLog(STEEL_SEARCH_NAME));
        
        btnSearchOil =
                new Sprite(
                        getAssetPath(OIL_SEARCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        OIL_SEARCH_THRESHOLD,
                        getPushMsgLog(OIL_SEARCH_NAME));
        
        btnCollect =
                new Sprite(
                        getAssetPath(COLLECT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        COLLECT_THRESHOLD,
                        getPushMsgLog(COLLECT_NAME));
        
        btnArrow =
                new Sprite(
                        getAssetPath(ARROW_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        ARROW_THRESHOLD,
                        getPushMsgLog(ARROW_NAME));
        
        btnCreateTroop =
                new Sprite(
                        getAssetPath(CREATE_TROOP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CREATE_TROOP_THRESHOLD,
                        getPushMsgLog(CREATE_TROOP_NAME));
        
        btnMarch =
                new Sprite(
                        getAssetPath(MARCH_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        MARCH_THRESHOLD,
                        getPushMsgLog(MARCH_NAME));

    }

    public void run(CommandService.StateToken state, Mat mat) {
        
        if(App.DEBUG){
            App.bus.post(new OnUserLog(TAG + ": Start"));
        }
        
        int busy = busy(mat);
        
        App.bus.post(new OnUserLog(TAG + ": Busy - " + busy));
        App.bus.post(new OnUserLog(TAG + ": farmingCount - " + farmingCount));
        
        while(farmingCount >= busy){
            if(btnRegion.pushTimeout(state)) {
                if(btnSearchIcon.pushTimeout(state)){
                    if(btnSearchTomatoes.pushTimeout(state)){
                        if(btnSearch.pushTimeout(state)) {
                            if(btnArrow.pushTimeout(state)) {
                                if(btnCollect.pushTimeout(state)) {
                                    if(btnCreateTroop.pushTimeout(state)) {
                                        btnMarch.pushTimeout(state);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            App.bus.post(new OnUserLog("Exit"));
            while (state.isRunning()) {
                mat = CommandService.takeScreenMat();

                if(btnBack.pushIfExists(mat, 1000)) {
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
    
    private int busy(Mat mat){
        if(busyFive.isFound(mat)){
            return 5;
        }
        else if(busyFour.isFound(mat)){
            return 4;
        }
        else if(busyThree.isFound(mat)){
            return 3;
        }
        else if(busyTwo.isFound(mat)){
            return 2;
        }
        else if(busyOne.isFound(mat)){
            return 1;
        }
        else {
            return 0;
        }
    }
    
    public String getKey(){
        return KEY;
    }

    public String getTag() {
        return TAG;
    }
}
