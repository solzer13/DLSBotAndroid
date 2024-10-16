package xxx.solzer.dlsbot;
import org.opencv.core.Mat;
import org.opencv.core.Point;

public class BountyGround {
    
    private static final String TAG = "BountyGround";
    
    private static final String BTN_BOUNTY_FILE = "btn_event.png";
    private static final String BTN_CAMPAIGN_FILE = "btn_campaign.png";
    
    private void loop(){
        
    }

//    private Point findCampaignButton() {
//        try {
//            Mat result = new Mat();
//            Mat matBtn = this.getAsset(BTN_CAMPAIGN_FILE);
//
//            Imgproc.matchTemplate(this.matScreen, matBtn, result, Imgproc.TM_SQDIFF_NORMED);
//
//            Point matchLoc = Core.minMaxLoc(result).minLoc;
//
//            return new Point(
//                    matchLoc.x + (double) (matBtn.cols() / 2),
//                    matchLoc.y + (double) (matBtn.rows() / 2));
//        } catch (Exception ex) {
//            Log.e(TAG, ex.getMessage());
//        }
//
//        return null;
//    }
}
