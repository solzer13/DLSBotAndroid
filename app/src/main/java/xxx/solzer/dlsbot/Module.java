package xxx.solzer.dlsbot;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.opencv.imgproc.Imgproc;

import xxx.solzer.dlsbot.events.OnUserLog;

public abstract class Module {

    private static final String HOME_FILE = "home.png";
    private static final double HOME_THRESHOLD = 0.98;
    private static final String HOME_NAME = "Убежище";

    private static final String REGION_FILE = "region.png";
    private static final double REGION_THRESHOLD = 0.98;
    private static final String REGION_NAME = "Регион";

    private static final String BACK_FILE = "back.png";
    private static final double BACK_THRESHOLD = 0.98;
    private static final String BACK_NAME = "Назад";

    private static final String OK_FILE = "ok.png";
    private static final double OK_THRESHOLD = 0.7;
    private static final String OK_NAME = "Ok";

    private static final String FREE_SPACE_FILE = "push_free_space.png";
    private static final double FREE_SPACE_THRESHOLD = 0.77;
    private static final String FREE_SPACE_NAME = "Пустое место";

    private static final String CLAIM_FILE = "claim.png";
    private static final double CLAIM_THRESHOLD = 0.95;
    private static final String CLAIM_NAME = "Получить";

    private static final String CLOSE_LIGHT_FILE = "close_light.png";
    private static final double CLOSE_LIGHT_THRESHOLD = 0.9;
    private static final String CLOSE_LIGHT_NAME = "Закрыть";

    private static final String CLOSE_DARK_FILE = "close_dark.png";
    private static final double CLOSE_DARK_THRESHOLD = 0.9;
    private static final String CLOSE_DARK_NAME = "Закрыть";

    protected final Sprite btnHome;
    protected final Sprite btnRegion;
    protected final Sprite btnBack;
    protected final Sprite btnOkYellow;
    protected final Sprite btnFreeSpace;
    protected final Sprite btnClaim;
    protected final Sprite btnCloseLight;
    protected final Sprite btnCloseDark;

    public Module() {
        this.btnHome =
                new Sprite(
                        getAssetRootPath(HOME_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        HOME_THRESHOLD,
                        getPushMsgLog(HOME_NAME));
        this.btnRegion =
                new Sprite(
                        getAssetRootPath(REGION_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        REGION_THRESHOLD,
                        getPushMsgLog(REGION_NAME));
        this.btnBack =
                new Sprite(
                        getAssetRootPath(BACK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        BACK_THRESHOLD,
                        getPushMsgLog(BACK_NAME));
        this.btnOkYellow =
                new Sprite(
                        getAssetRootPath(OK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        OK_THRESHOLD,
                        getPushMsgLog(OK_NAME));
        this.btnFreeSpace =
                new Sprite(
                        getAssetRootPath(FREE_SPACE_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        FREE_SPACE_THRESHOLD,
                        getPushMsgLog(FREE_SPACE_NAME));
        this.btnClaim =
                new Sprite(
                        getAssetRootPath(CLAIM_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CLAIM_THRESHOLD,
                        getPushMsgLog(CLAIM_NAME));
        this.btnCloseLight =
                new Sprite(
                        getAssetRootPath(CLOSE_LIGHT_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CLOSE_LIGHT_THRESHOLD,
                        getPushMsgLog(CLOSE_LIGHT_NAME));
        this.btnCloseDark =
                new Sprite(
                        getAssetRootPath(CLOSE_DARK_FILE),
                        Imgproc.TM_CCOEFF_NORMED,
                        CLOSE_DARK_THRESHOLD,
                        getPushMsgLog(CLOSE_DARK_NAME));
    }

    public abstract void run(CommandService.StateToken state);

    public abstract String getKey();

    public abstract String getTag();

    protected String getPushMsgLog(String button_name) {
        return getMsgLog("Жмем кнопку \"" + button_name + "\"");
    }

    protected String getMsgLog(String msg) {
        return getTag() + ": " + msg;
    }

    protected void logUserMsg(String msg) {
        App.bus.post(new OnUserLog(getMsgLog(msg)));
    }

    protected Path getAssetPath(String file_name) {
        return Paths.get(App.getAssetDirName()).resolve(getKey()).resolve(file_name);
    }

    protected Path getAssetRootPath(String file_name) {
        return Paths.get(App.getAssetDirName()).resolve(file_name);
    }

    private String getAssetFilePath(String file) {
        return App.getAssetDirName() + "/" + file;
    }
}
