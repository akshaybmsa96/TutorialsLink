package tutorialslink.com.tutorialslinkwebview.global;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


/**
 * Created by smpx-imac1 on 30/03/16.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    public static final String JSON_OBJECT_REQUEST = "json_obj_req";


    public static AppController mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}
