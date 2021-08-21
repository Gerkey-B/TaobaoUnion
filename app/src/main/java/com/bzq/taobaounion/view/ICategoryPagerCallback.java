package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.HomePagerContent;

import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/7/6
 * MVP 的关键就是使用回调在 P 层与 V 层之间进行数据传输
 */
public interface ICategoryPagerCallback extends IBaseCallback {


    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataDTO> contents);

    /**
     * 返回 UI 层每个分类的 id
     * @return
     */
    int getCategoryId();


    /**
     * 在加载更多时网络错误我
     */
    void onLoadMoreError();

    /**
     * 加载更多时数据为空
     */
    void onLoadMoreEmpty();

    /**
     * 成功加载了更多数据
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents);

    /**
     * 轮播图加载成功
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataDTO> contents);
}
