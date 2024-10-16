package xxx.solzer.dlsbot.events;

import android.graphics.Bitmap;
import xxx.solzer.dlsbot.Event;

public class OnScreenTaked extends Event {
    
    public final Bitmap bitmap;
    
    public OnScreenTaked(Bitmap bitmap){
        this.bitmap = bitmap;
    }
}
