package xxx.solzer.dlsbot;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.Subscribe;
import xxx.solzer.dlsbot.events.OnUserLog;

public class LogsMainFragment extends Fragment {

    private TextView logText;

    public LogsMainFragment() {
        super(R.layout.main_logs_fragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnUserLog event) {
        if (this.logText != null) {
            this.logText.setText(App.userLog.getLogs());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        this.logText = view.findViewById(R.id.mainLogText);
    }

    @Override
    public void onResume() {
        super.onResume();
        
        App.bus.register(this);

        this.logText.setText(App.userLog.getLogs());
    }

    @Override
    public void onPause() {
        super.onPause();
        
        App.bus.unregister(this);
    }
}
