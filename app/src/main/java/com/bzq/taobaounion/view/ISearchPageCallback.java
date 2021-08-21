package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.Histories;
import com.bzq.taobaounion.model.DTO.SearchRecommend;
import com.bzq.taobaounion.model.DTO.SearchResult;

import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/8/8
 */
public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     * @param histories
     */
    void onHistoriesLoaded(Histories histories);

    /**
     * 历史删除完成
     */
    void onHistoriesDeleted();


    /**
     * 搜索结果
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载更多成功
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络错误
     */
    void onMoreLoadError();

    /**
     * 没有更多内容
     */
    void onMoreLoadEmpty();

    /**
     * 获取关键词结果
     * @param recommendWords
     */
    void onRecommendWordLoaded(List<SearchRecommend.DataDTO> recommendWords);
}
