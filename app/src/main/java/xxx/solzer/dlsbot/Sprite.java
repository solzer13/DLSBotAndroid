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
    
    public Sprite(Path file, int type, double trashold){
        this.path = file;
        this.type = type;
        this.trashold = trashold;
    }
    
    public boolean pushIfExists(String msg, int sleep){
        Point point = find();
    
        if(point != null){
            push(point, msg);
        }
        
        try{Thread.sleep(sleep);}catch(Exception e){}
        
        return point != null;
    }
    
    public void push(Point point, String msg, int sleep){
        push(point, msg);
        try{Thread.sleep(sleep);}catch(Exception e){}
    }
    
    public void push(Point point, String msg){
        push(point);
        App.bus.post(new OnUserLog(msg));
    }
    
    public void push(Point point){
        App.bus.post(new OnTap(point));
    }
    
    public boolean isFound(){
        return find() != null;
    }
    
    public Point find(){
        return find(CommandService.takeScreenMat());
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
