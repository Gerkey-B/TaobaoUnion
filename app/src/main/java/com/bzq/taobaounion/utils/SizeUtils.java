package com.bzq.taobaounion.utils;

import android.content.Context;

/**
 * @author Gerkey
 * Created on 2021/7/11
 * px ，像素单位
 * dp ，适配单位
 * sp ，字体单位
 * 一般要填写的尺寸，都是 px，我们如果要用 dp 的话，需要做屏幕适配
 */
public class SizeUtils {
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

