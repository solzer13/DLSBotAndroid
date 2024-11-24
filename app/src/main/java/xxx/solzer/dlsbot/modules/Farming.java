package xxx.solzer.dlsbot.modules;

import android.content.SharedPreferences;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.ThreadLocalRandom;

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

    private static final String TOMATOES_MAP_FILE = "map_tomatoes.png";
    private static final double TOMATOES_MAP_THRESHOLD = 0.9;
    private static final String TOMATOES_MAP_NAME = "Плитка помидор";

    private static final String WOOD_MAP_FILE = "map_wood.png";
    private static final double WOOD_MAP_THRESHOLD = 0.9;
    private static final String WOOD_MAP_NAME = "Плитка дерева";

    private static final String STEEL_MAP_FILE = "map_steel.png";
    private static final double STEEL_MAP_THRESHOLD = 0.9;
    private static final String STEEL_MAP_NAME = "Плитка стали";

    private static final String OIL_MAP_FILE = "map_oil.png";
    private static final double OIL_MAP_THRESHOLD = 0.9;
    private static final String OIL_MAP_NAME = "Плитка нефти";

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
    private final Sprite btnMapTomatoes;
    private final Sprite btnMaphWood;
    private final Sprite btnMapSteel;
    private final Sprite btnMapOil;
    private final Sprite btnCollect;
    private final Sprite btnArrow;
    private final Sprite btnCreateTroop;
    private final Sprite btnMarch;
    
    private final int farmingCount;
    private final boolean farmingTomatoes;
    private final boolean farmingWood;
    private final boolean farmingSteel;
    private final boolean farmingOil;

    private long next_time;
    
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

        btnMapTomatoes =
                new Sprite(
                        getAssetPath(TOMATOES_MAP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        TOMATOES_MAP_THRESHOLD,
                        getPushMsgLog(TOMATOES_MAP_NAME));

        btnMaphWood =
                new Sprite(
                        getAssetPath(WOOD_MAP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        WOOD_MAP_THRESHOLD,
                        getPushMsgLog(WOOD_MAP_NAME));

        btnMapSteel =
                new Sprite(
                        getAssetPath(STEEL_MAP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        STEEL_MAP_THRESHOLD,
                        getPushMsgLog(STEEL_MAP_NAME));

        btnMapOil =
                new Sprite(
                        getAssetPath(OIL_MAP_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        OIL_MAP_THRESHOLD,
                        getPushMsgLog(OIL_MAP_NAME));

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

        if(System.currentTimeMillis() > next_time){
            int busy = busy(mat);

            if(farmingCount >= busy){
                while(state.isRunning()){
                    if(farmingCount <= busy){
                        break;
                    }

                    if(sendRandom(state)){
                        next_time = System.currentTimeMillis() + 1000*60*3;
                        break;
                    }

                    busy = busy(CommandService.takeScreenMat());
                }
            }
        }

    }

    private boolean sendRandom(CommandService.StateToken state){
        while(state.isRunning()){
            int rand = ThreadLocalRandom.current().nextInt(0, 3);
            if(rand == 0 && farmingTomatoes){
                return sendToResource(state, ResourceType.Tomatoes);
            }
            if(rand == 1 && farmingWood){
                return sendToResource(state, ResourceType.Wood);
            }
            if(rand == 2 && farmingSteel){
                return sendToResource(state, ResourceType.Steel);
            }
            if(rand == 3 && farmingOil){
                return sendToResource(state, ResourceType.Oil);
            }
        }
        return false;
    }

    private boolean sendToResource(CommandService.StateToken state, ResourceType type){
        boolean result = false;
        if(btnRegion.pushTimeout(state)) {
            if(btnSearchIcon.pushTimeout(state)){
                if(searchResource(state, type)){
                    if(btnSearch.pushTimeout(state, 3000)) {
                        App.bus.post(new OnTap(new Point((double) App.getScreenWidth() / 2, (double) App.getScreenHeight() / 2)));
                        if(btnCollect.pushTimeout(state)) {
                            if(btnCreateTroop.pushTimeout(state)) {
                                if(btnMarch.pushTimeout(state)){
                                    result = true;
                                }
                            }
                        }
                    }
                }
            }
            goToHome(state);
        }
        return result;
    }

    private void goToHome(CommandService.StateToken state){
        while (state.isRunning()) {
            Mat mat = CommandService.takeScreenMat();

            if(btnBack.pushIfExists(mat)) {
                continue;
            }
            if(btnHome.pushIfExists(mat)){
                continue;
            }
            if(btnRegion.isFound(mat)){
                break;
            }
        }
    }

    private boolean searchResource(CommandService.StateToken state, ResourceType type){
        switch (type){
            case Tomatoes: return btnSearchTomatoes.pushTimeout(state);
            case Wood: return btnSearchWood.pushTimeout(state);
            case Steel: return btnSearchSteel.pushTimeout(state);
            case Oil: return btnSearchOil.pushTimeout(state);
        }
        return false;
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

    private enum ResourceType {
        Tomatoes, Wood, Steel, Oil;
    }
}
