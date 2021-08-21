package com.bzq.taobaounion.utils;

/**
 * @author Gerkey
 * Created on 2021/7/6
 * 处理一些字符串
 */
public class UrlUtils {
    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pictUrl) {
        if (pictUrl.startsWith(Constants.HTTP) || pictUrl.startsWith(Constants.HTTPS)) {
            return pictUrl;
        } else {
            return Constants.HTTPS + ":" + pictUrl;
        }
    }

    public static String getCoverPathWithSize(String pictUrl, int size) {
        if (pictUrl.startsWith(Constants.HTTP) || pictUrl.startsWith(Constants.HTTPS)) {
            return pictUrl;
        } else {
            return Constants.HTTPS + ":" + pictUrl + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith(Constants.HTTP) || url.startsWith(Constants.HTTPS)) {
            return url;
        } else {
            return Constants.HTTPS + ":" + url;
        }
    }

    public static String getSelectedPageContentUrl(Integer categoryId) {
        return "recommend/" + categoryId;
    }

    public static String getOnSellPageUrl(int currentPage) {
        return "onSell/" + currentPage;
    }
}

