package xxx.solzer.dlsbot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xxx.solzer.dlsbot.events.OnUserLog;

public class UserLog {
    
    private StringBuilder buff;
    
    public UserLog(){
        this.buff = new StringBuilder();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnUserLog event) {
        this.buff.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        this.buff.append(" - ");
        this.buff.append(event.message);
        this.buff.append("\r\n");
    }

    public String getLogs(){
        return this.buff.toString();
    }
}
