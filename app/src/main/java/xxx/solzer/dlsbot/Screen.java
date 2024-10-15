package xxx.solzer.dlsbot;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class Screen {

    private static final String BTN_EVENT_FILE = "btn_event.png";

    private final AssetManager am;
    private final Bitmap bmpScreen;
    private final Mat matScreen;

    public Screen(AssetManager am, Bitmap bitmap){
        this.am = am;
        this.bmpScreen = bitmap;
        this.matScreen = new Mat();

        Utils.bitmapToMat(this.bmpScreen, this.matScreen);
    }

    private Point findEventsButton(){
        try {
            Mat result = new Mat();
            Mat matBtn = this.getAsset(BTN_EVENT_FILE);

            Imgproc.matchTemplate(this.matScreen, matBtn, result, Imgproc.TM_SQDIFF_NORMED);

            Point matchLoc = Core.minMaxLoc(result).minLoc;

            return new Point(matchLoc.x + (double)(matBtn.cols() / 2), matchLoc.y + (double)(matBtn.rows() / 2));
        }
        catch (Exception ex){

        }

        return null;
    }

    private Mat getAsset(String file) throws IOException {
        Mat result = new Mat();
        InputStream stream = this.am.open(file);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        Utils.bitmapToMat(bitmap, result);
        return result;
    }

}

