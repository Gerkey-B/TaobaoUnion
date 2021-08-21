package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.OnSellContent;

/**
 * @author Gerkey
 * Created on 2021/8/3
 */
public interface IOnSellPageCallback extends IBaseCallback {

    /**
     * 加载特惠内容结果
     * @param result
     */
    void onContentLoadedSuccess(OnSellContent result);

    /**
     * 加载更多的结果
     * @param result
     */
    void onMoreLoaded(OnSellContent result);


    /**
     * 加载更多失败
     */
    void onMoreLoadError();


    /**
     * 没有更多内容
     */
    void onMoreLoadEmpty();

}
