package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.view.ICategoryPagerCallback;

/**
 * @author Gerkey
 * Created on 2021/7/6
 */
public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类 Id 来获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);


    /**
     * 通过 id 来对每个页面加载更多
     * @param categoryId
     */
    void loadMore(int categoryId);


    void reload(int categoryId);
}
