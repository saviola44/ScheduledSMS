package mariusz.scheduledSMS;

import android.os.Environment;

/**
 * Created by mariusz on 21.09.15.
 */
public class DataConstant {
    private static final String APP_PACKAGE_NAME = "com.example.mariusz.sendsmstest";

    private static final String EXTERNAL_DATA_DIR_NAME = "sendsms";
    public static final String EXTERNAL_DATA_PATH =
            Environment.getExternalStorageDirectory() + "/" + DataConstant.EXTERNAL_DATA_DIR_NAME;

    public static final String DATABASE_NAME = "sendsms.db";
    public static final String DATABASE_PATH =
            Environment.getDataDirectory() + "/data/" + DataConstant.APP_PACKAGE_NAME + "/databases/"
                    + DataConstant.DATABASE_NAME;

    private DataConstant() {
    }
}
