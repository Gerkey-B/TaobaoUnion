package com.bzq.taobaounion.utils;

import android.util.Log;

/**
 * @author Gerkey
 * Created on 2021/6/30
 * 产品上线后，我们不可能把我们的调试信息传给客户，因此，上线后，我们要修改这里的 currentLev 至相应的等级，
 * 方便以后观察 Log ，同时又不会造成信息泄漏
 */
public class LogUtils {

    public static int currentLev = 4;
    public static final int DEBUG_LEV = 4;
    public static final int INFO_LEV = 3;
    public static final int WARNING_LEV = 2;
    public static final int ERROR_LEV = 1;


    public static void d(Object object, String log) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

    public static void i(Object object, String log) {
        if (currentLev > INFO_LEV) {
            Log.i(object.getClass().getSimpleName(), log);
        }
    }

    public static void w(Object object, String log) {
        if (currentLev > WARNING_LEV) {
            Log.w(object.getClass().getSimpleName(), log);
        }
    }

    public static void e(Object object ,String log) {
        if (currentLev > ERROR_LEV) {
            Log.e(object.getClass().getSimpleName(), log);
        }
    }


}

