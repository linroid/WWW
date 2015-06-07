package io.hackathon.www.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.badlogic.gdx.Gdx;
import io.hackathon.www.ScreenActivity;


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
    public void finish(int score) {
        Intent intent = this.activity.getIntent();
        intent.putExtra("score", score);
        this.activity.setResult(Activity.RESULT_OK, intent);
        this.activity.finish();
        this.activity.overridePendingTransition(0, 0);
    }
}
