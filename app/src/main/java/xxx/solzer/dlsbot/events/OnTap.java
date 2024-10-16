package xxx.solzer.dlsbot.events;

import org.opencv.core.Point;
import xxx.solzer.dlsbot.Event;

public class OnTap extends Event {

    public final Point point;

    public OnTap(Point point){
        this.point = point;
    }
}
