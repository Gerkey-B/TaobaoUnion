package com.bzq.taobaounion.utils;

/**
 * @author Gerkey
 * Created on 2021/7/26
 * 处理字符串，从中获取淘口令
 */
public class StringUtils {
    public static String getSubModel(String str) {
        boolean fA = false;
        int startPos = 0;
        int endPos = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '￥' && !fA) {
                fA = true;
                startPos = i;
            }
            if (str.charAt(i) == ' ' && str.charAt(i - 1) == '￥') {
                endPos = i;
            }
            if (startPos != 0 && endPos != 0) {
                break;
            }
        }
        return str.substring(startPos, endPos);
    }
}

