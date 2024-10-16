package xxx.solzer.dlsbot;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

import xxx.solzer.dlsbot.events.OnScreenTaked;
import xxx.solzer.dlsbot.events.OnTakeScreen;
import xxx.solzer.dlsbot.events.OnTap;

public class BountyGround {
    
    private static final String TAG = "BountyGround";

    private static final String BTN_CAMPAIGN_FILE = "btn_campaign.png";

    private final AssetManager am;

    private int step = 1;

    public BountyGround(AssetManager am){
        this.am = am;
        App.bus.register(this);
        App.bus.post(new OnTakeScreen());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(OnScreenTaked screen) throws InterruptedException {
        Log.d(TAG, "Screen taked");

        Mat mat = new Mat();
        Bitmap bmp32 = screen.bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);

        if(this.step == 1){
            Log.d(TAG, "Start step 1");
            Point btn_loc = findCampaignButton(mat);

            if(btn_loc != null){
                this.step = 2;
                App.bus.post(new OnTap(btn_loc));
                Thread.sleep(2000);
                App.bus.post(new OnTakeScreen());
            }
            else {
                Log.e(TAG, "Did not find CampaignButton");
            }
        }

        if(this.step == 2){
            Log.d(TAG, "Start step 2");
        }
    }

    private Point findCampaignButton(Mat mat) {
        try {
            Mat result = new Mat();
            Mat matBtn = this.getAsset(BTN_CAMPAIGN_FILE);

            Imgproc.matchTemplate(mat, matBtn, result, Imgproc.TM_SQDIFF_NORMED);

            var mml = Core.minMaxLoc(result);

            Log.e(TAG, mml.minVal + " / " + mml.maxVal);

            Point matchLoc = mml.minLoc;

            return new Point(
                    matchLoc.x + (double) (matBtn.cols() / 2),
                    matchLoc.y + (double) (matBtn.rows() / 2));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
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
