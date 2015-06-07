package io.hackathon.www.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.badlogic.gdx.Gdx;
import io.hackathon.www.ScreenActivity;
import android.net.Uri;


/**
 * Created by bisunday on 6/6/15.
 */
public class AndroidScreenActivity implements ScreenActivity {
    Handler uiThread;
    Activity activity;

    public AndroidScreenActivity(Activity activity) {
        uiThread = new Handler();
        this.activity = activity;
    }
    public void pushMessage(int id) {
        Gdx.app.log("Tag:", "hello");
        // appContext.startActivity(new Intent(this.appContext, SubActivity.class));
        //Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
        // appContext.startActivity(intent);
    }

    @Override
    public void finish() {
        this.activity.setResult(Activity.RESULT_OK);
        this.activity.finish();
        this.activity.overridePendingTransition(0, 0);
    }
}
