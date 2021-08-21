package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.view.IHomeCallback;

/**
 * @author Gerkey
 * Created on 2021/7/2
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallback> {

    /**
     * 获取商品分类  (因为是异步执行操作，需要等待，所以我们还要一个获取成功后，通知 UI 更新的接口，这个接口在 View 里)
     */
    void getCategories();

}
