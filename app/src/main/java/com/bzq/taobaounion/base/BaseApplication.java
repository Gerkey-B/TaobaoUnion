package com.bzq.taobaounion.base;

import android.app.Application;
import android.content.Context;

/**
 * @author Gerkey
 * Created on 2021/7/13
 */
public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}

