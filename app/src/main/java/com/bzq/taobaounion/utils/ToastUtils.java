package com.bzq.taobaounion.utils;

import android.widget.Toast;

import com.bzq.taobaounion.base.BaseApplication;

/**
 * @author Gerkey
 * Created on 2021/7/13
 * 将 Toast 静态了，不静态，假如一直点某一个 Toast，Toast 会一直跳出，不停下来，
 * 静态化后，同一时间只会存在一个 Toast，不会有其他 Toast 在后面排队等弹出的情况
 */
public class ToastUtils {

    private static Toast sToast;

    public static void showToast(String tips) {

        if (sToast == null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_LONG);
        } else {
            sToast.setText(tips);
        }
        sToast.show();
    }
}

