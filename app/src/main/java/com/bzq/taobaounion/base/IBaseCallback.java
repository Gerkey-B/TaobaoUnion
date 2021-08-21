package com.bzq.taobaounion.base;

/**
 * @author Gerkey
 * Created on 2021/7/6
 */
public interface IBaseCallback {

    /**
     * 网络错误时调用
     */
    void onLoading();

    /**
     * 正在加载时调用
     */
    void onError();

    /**
     * 返回为空时调用
     */
    void onEmpty();

}
