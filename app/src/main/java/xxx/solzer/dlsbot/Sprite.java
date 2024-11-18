package xxx.solzer.dlsbot;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.InputStream;
import java.nio.file.Path;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import xxx.solzer.dlsbot.events.OnTap;
import xxx.solzer.dlsbot.events.OnUserLog;

public class Sprite {
    
    private final Path path;
    
    private final double trashold;
    
    private final int type;
    
    private String logMessage;
    
    public Sprite(Path file, int type, double trashold){
        this.path = file;
        this.type = type;
        this.trashold = trashold;
    }
    
    public Sprite(Path file, int type, double trashold, String msg){
        this(file, type, trashold);
        this.logMessage = msg;
    }
    
    public void setLogMessage(String msg){
        this.logMessage = msg;
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

    public Point find(){
        return find(CommandService.takeScreenMat());
    }
    
    public Point find(int sleep){
        Point point = find();
        App.sleep(sleep);
        return point;
    }
    
    public Point find(Mat mat){
        return findImage(mat, this.path, this.type, this.trashold);
    }
        
    public static Point findImage(Mat big, Path small, int type, double trashold){ 
        return findImage(big, App.getAsset(small), type, trashold);
    }

    public static Point findImage(Mat big, Mat small, int type, double trashold){
        try {
            Mat result = new Mat();

            Imgproc.matchTemplate(big, small, result, type);

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
}
