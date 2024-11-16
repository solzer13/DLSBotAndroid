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
    private static final String BTN_REGION_FILE = "btn_region.png";
    private static final String BTN_BACK_FILE = "btn_back.png";
    private static final String BTN_OK_YELLOW_FILE = "btn_ok_yellow.png";

    private static final String BTN_FREE_SPACE_FILE = "btn_free_space.png";
    private static final double BTN_FREE_SPACE_THRESHOLD = 0.77;

    protected final Sprite btnFreeSpace;

    public Module(){
        this.btnFreeSpace = new Sprite(
                getAssetPath(BTN_FREE_SPACE_FILE),
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
        Point btn_back_point = findBackButton(CommandService.takeScreenMat());
        if(btn_back_point != null){
            App.bus.post(new OnTap(btn_back_point));
            logUserMsg(getPushMsgLog("Назад"));
        }
        App.sleep(1000);
        return btn_back_point != null;
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

    private String getAssetFilePath(String file){
        return App.getAssetDirName()+ "/" + file;
    }
     
}
