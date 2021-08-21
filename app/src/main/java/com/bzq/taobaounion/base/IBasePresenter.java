package com.bzq.taobaounion.base;

/**
 * @author Gerkey
 * Created on 2021/7/6
 */
public interface IBasePresenter<T> {
    /**
     * 在我们成功获取商品分类后，并且拿到了一个 Callback，为了将 Callback 返回到 presenter 层，我们还需要注册与销毁
     * 注册 UI 通知接口
     * @param callback
     */

    void registerViewCallback(T callback);


    /**
     * 取消注册
     * @param callback
     */

    void unregisterViewCallback(T callback);
}
