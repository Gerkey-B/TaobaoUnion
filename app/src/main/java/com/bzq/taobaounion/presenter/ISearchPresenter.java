package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.view.ISearchPageCallback;

/**
 * @author Gerkey
 * Created on 2021/8/8
 */
public interface ISearchPresenter extends IBasePresenter<ISearchPageCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索
     * @param keyWord
     */
    void doSearch(String keyWord);

    /**
     * 重新搜索
     */
    void research();

    /**
     * 获取更多搜索历史
     */
    void loadMore();

    /**
     * 后去推荐关键词
     */
    void getRecommendWords();
}

