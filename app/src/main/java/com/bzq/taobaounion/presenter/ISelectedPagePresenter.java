package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;
import com.bzq.taobaounion.view.ISelectedPageCallback;

/**
 * @author Gerkey
 * Created on 2021/7/29
 */
public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {
    /**
     * 获取分类
     */
    void getCategories();


    /**
     * 根据分类 Id 获取分类内容
     * @param item
     */
    void getContentByCategoryId(SelectedPageCategory.DataDTO item);


    /**
     * 错误时，重新加载内容
     */
    void reLoadContent();

}
