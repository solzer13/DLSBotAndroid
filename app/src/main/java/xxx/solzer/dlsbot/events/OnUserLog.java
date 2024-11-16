package xxx.solzer.dlsbot.events;

import xxx.solzer.dlsbot.Event;

public class OnUserLog extends Event {

    public final String message;

    public OnUserLog(String msg){
        this.message = msg;
    }
}
