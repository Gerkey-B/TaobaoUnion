package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.view.IOnSellPageCallback;

/**
 * @author Gerkey
 * Created on 2021/8/3
 */
public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {

    /**
     * 加载特惠内容
     */
    void getOnSellContent();

    /**
     * 重试加载
     */
    void reLoad();

    /**
     * 加载更多
     */
    void loadMore();
}
