package com.fushiginopixel.fushiginopixeldungeon;

import android.app.Application;

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCrashHandler();
    }

    protected void initCrashHandler() {
        /*
        if (BuildConfig.isDebug) {
            CustomActivityOnCrash.install(this);
        } else {
            CrashHandler handler = CrashHandler.getInstance();
            handler.init(getApplicationContext());
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }
        */

        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
