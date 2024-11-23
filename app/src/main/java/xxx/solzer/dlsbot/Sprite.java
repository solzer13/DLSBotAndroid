package xxx.solzer.dlsbot;

import android.util.Log;

import java.nio.file.Path;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Sprite {
    
    private final Path path;
    private final double threshold;
    private final int type;
    private String logMessage;
    private double scale;
    
    public Sprite(Path file, int type, double threshold){
        this.path = file;
        this.type = type;
        this.threshold = threshold;

        var screen = App.getCurrentScreen();
        if(screen != null){
            this.scale = screen.scaleInterface;
        }
        else {
            this.scale = 1;
        }
    }
    
    public Sprite(Path file, int type, double threshold, String msg){
        this(file, type, threshold);
        this.logMessage = msg;
    }
    
    public void setLogMessage(String msg){
        this.logMessage = msg;
    }
    
    public void setScale(double scale){
        this.scale = scale;
    }

    public boolean pushTimeout(CommandService.StateToken state){
        return pushTimeout(state, 0, 10000, 500);
    }

    public boolean pushTimeout(CommandService.StateToken state, Mat mat){
        return pushTimeout(state, mat, 0, 10000, 500);
    }

    public boolean pushTimeout(CommandService.StateToken state, Mat mat, int delay_after){
        return pushTimeout(state, mat, 0, 10000, delay_after);
    }

    public boolean pushTimeout(CommandService.StateToken state, int delay_after){
        return pushTimeout(state, 0, 10000, delay_after);
    }

    public boolean pushTimeout(CommandService.StateToken state, int delay_before, int timeout, int delay_after){
        return pushTimeout(state, CommandService.takeScreenMat(), delay_before, timeout, delay_after);
    }

    public boolean pushTimeout(CommandService.StateToken state, Mat mat, int delay_before, int timeout, int delay_after){
        long started = System.currentTimeMillis();
        while(state.isRunning()){
            delay(state, delay_before);
            Point point = find(mat);
            if(point != null){
                App.bus.post(new OnTap(point));
                if(this.logMessage != null){
                    App.bus.post(new OnUserLog(this.logMessage + " (millis: " + (System.currentTimeMillis() - started) + ")"));
                }
                delay(state, delay_after);
                return true;
            }
            
            if((started + delay_before + delay_after + timeout) < System.currentTimeMillis()){
                break;
            }
        }
        return false;
    }
    
    public boolean pushIfExists(int sleep){
        Point point = find();
        if(point != null){
            push(point);
        }
        App.sleep(sleep);
        return point != null;
    }

    public boolean pushIfExists(Mat mat, int sleep){
        Point point = find(mat);
        if(point != null){
            push(point);
            App.sleep(sleep);
        }
        return point != null;
    }

    public boolean pushIfExists(Point point, int sleep){
        if(point != null){
            push(point);
            App.sleep(sleep);
        }
        return point != null;
    }

    public void push(Point point, int sleep){
        push(point);
        App.sleep(sleep);
    }
    
    public void push(Point point){
        App.bus.post(new OnTap(point));
        if(this.logMessage != null){
            App.bus.post(new OnUserLog(this.logMessage));
        }
    }
    
    public boolean isFound(){
        return find() != null;
    }

    public boolean isFound(Mat mat){
        return find(mat) != null;
    }

    public boolean isFound(Mat mat, int sleep){
        return find(mat, sleep) != null;
    }

    public Point find(){
        return find(CommandService.takeScreenMat());
    }
    
    public Point find(int sleep){
        Point point = find();
        App.sleep(sleep);
        return point;
    }
    
    public Point find(Mat mat, int sleep){
        Point point = find(mat);
        App.sleep(sleep);
        return point;
    }
     
    public Point find(Mat mat){
        return findImage(mat, this.path, this.type, this.threshold);
    }
    
    public static Mat scale(Mat source, double scale){
        Mat result = new Mat();
        Imgproc.resize(source, result, new Size(source.cols() * scale, source.rows() * scale));
        return result;
    }
        
    public Point findImage(Mat big, Path small, int type, double trashold){ 
        return findImage(big, App.getAsset(small), type, trashold);
    }

    public Point findImage(Mat big, Mat small, int type, double trashold){
        try {
            Mat result = new Mat();

            Imgproc.matchTemplate(big, scale(small, this.scale), result, type);

            var mml = Core.minMaxLoc(result);
            var loc = mml.maxLoc;
            
            if(mml.maxVal > trashold){
                return new Point(
                    loc.x + (double) (small.cols() / 2),
                    loc.y + (double) (small.rows() / 2));
            }

        } catch (Exception ex) {
            Log.e(App.TAG, ex.getMessage());
        }
        
        return null;
    }
    
    public static void delay(CommandService.StateToken state, long millis){
        long start = System.currentTimeMillis();
        while(state.isRunning()) {
        	if((start + millis) < System.currentTimeMillis()){
                return;
            }
        }
    }
    
}
