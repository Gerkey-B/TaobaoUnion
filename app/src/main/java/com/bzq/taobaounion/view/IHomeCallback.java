package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.Categories;

/**
 * @author Gerkey
 * Created on 2021/7/2
 */
public interface IHomeCallback extends IBaseCallback{

    /**
     * 通知 UI 更新的接口
     * @param categories
     */

    void onCategoriesLoaded(Categories categories);

}
