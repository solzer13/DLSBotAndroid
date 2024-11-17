package xxx.solzer.dlsbot;

import android.graphics.Bitmap;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public abstract class Module {
    
    private static final String BTN_HOME_FILE = "btn_home.png";
    private static final double BTN_HOME_THRESHOLD = 0.98;
    
    private static final String BTN_REGION_FILE = "btn_region.png";
    private static final double BTN_REGION_THRESHOLD = 0.98;
    
    private static final String BTN_BACK_FILE = "btn_back.png";
    private static final double BTN_BACK_THRESHOLD = 0.98;
    
    private static final String BTN_OK_YELLOW_FILE = "btn_ok_yellow.png";
    private static final double BTN_OK_YELLOW_THRESHOLD = 0.98;
    
    private static final String BTN_FREE_SPACE_FILE = "btn_free_space.png";
    private static final double BTN_FREE_SPACE_THRESHOLD = 0.77;

    protected final Sprite btnHome;
    protected final Sprite btnRegion;
    protected final Sprite btnBack;
    protected final Sprite btnOkYellow;
    protected final Sprite btnFreeSpace;

    public Module(){
        this.btnHome = new Sprite(
                getAssetRootPath(BTN_HOME_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_HOME_THRESHOLD
        );
        this.btnRegion = new Sprite(
                getAssetRootPath(BTN_REGION_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_REGION_THRESHOLD
        );
        this.btnBack = new Sprite(
                getAssetRootPath(BTN_BACK_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_BACK_THRESHOLD
        );
        this.btnOkYellow = new Sprite(
                getAssetRootPath(BTN_OK_YELLOW_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_OK_YELLOW_THRESHOLD
        );
        this.btnFreeSpace = new Sprite(
                getAssetRootPath(BTN_FREE_SPACE_FILE),
                Imgproc.TM_CCOEFF_NORMED,
                BTN_FREE_SPACE_THRESHOLD
        );
    }

    public abstract void run(CommandService.StateToken state);
       
    public abstract String getKey();
    public abstract String getTag();
    
    protected boolean isMainWindow(Mat mat){
        return (findRegionButton(mat) != null);
    }

    protected boolean pushBack(){
        return btnBack.pushIfExists("Назад", 1000);
    }

    protected Point findHomeButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_HOME_FILE), 1E8);
    }
    
    protected Point findRegionButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_REGION_FILE), 9.1E7);
    }
    
    protected Point findBackButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_BACK_FILE), 3.4E7);
    }
    
    protected Point findOkYellowButton(Mat mat) {
        return App.findImage(mat, getAssetFilePath(BTN_OK_YELLOW_FILE), 1.3E7);
    }
    
    protected String getPushMsgLog(String button_name){
        return getMsgLog("Жмем кнопку \"" + button_name + "\"");
    }
    
    protected String getMsgLog(String msg){
        return getTag() + ": " + msg;
    }
    
    protected void logUserMsg(String msg) {
    	App.bus.post(new OnUserLog(getMsgLog(msg)));
    }
        
    protected Path getAssetPath(String file_name){
        return Path.of(App.getAssetDirName()).resolve(getKey()).resolve(file_name);
    }
   
    protected Path getAssetRootPath(String file_name){
        return Path.of(App.getAssetDirName()).resolve(file_name);
    }
    
    private String getAssetFilePath(String file){
        return App.getAssetDirName()+ "/" + file;
    }
     
}
