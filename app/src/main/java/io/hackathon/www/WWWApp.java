package io.hackathon.www;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by linroid on 6/6/15.
 */
public class WWWApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "p01bivro1qkqxzfwxbs4gpgepkvn9i67zyw3i0ggxtt0v1ry", "9uli7a87v85qmpj9dn8ijb4hxtpakt4u25qv8p7noxp7ka56");
    }
}
