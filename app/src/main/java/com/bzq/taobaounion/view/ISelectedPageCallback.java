package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.SelectedContent;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;

/**
 * @author Gerkey
 * Created on 2021/7/29
 */
public interface ISelectedPageCallback extends IBaseCallback {

    /**
     * 分类内容结果
     * @param categories
     */
    void onCategoriesLoaded(SelectedPageCategory categories);

    /**
     * 内容加载成功
     * @param content
     */
    void onContentLoaded(SelectedContent content);
}
