package com.bzq.taobaounion.presenter.impl;

import com.bzq.taobaounion.model.Api;
import com.bzq.taobaounion.model.DTO.HomePagerContent;
import com.bzq.taobaounion.presenter.ICategoryPagerPresenter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.RetrofitManager;
import com.bzq.taobaounion.utils.UrlUtils;
import com.bzq.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Gerkey
 * Created on 2021/7/6
 */
public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    /**
     * 需要一个容器来存储每一个分类的页面
     */
    private Map<Integer, Integer> pagerMap = new HashMap<>();

    /**
     * 我们用 Presenter 来请求数据时是不知道这个请求到底是哪个页面的，
     * 所以我们用一个 List 集合来保存每一个请求，到后面再通过 id 来确认
     */
    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    private static final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;


    public CategoryPagerPresenterImpl() {

    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                // 当初这里犯了个低级失误，导致个bug，害我找了1个小时 QAQ
                callback.onLoading();
            }
        }
        // 根据分类 ID 去加载内容
        Integer targetPager = pagerMap.computeIfAbsent(categoryId, k -> DEFAULT_PAGE);
        Call<HomePagerContent> task = createTask(categoryId, targetPager);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "code ----- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
//                    LogUtils.d(CategoryPagerPresenterImpl.this, pagerContent.toString());
                    handleHomePagerContentResult(pagerContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, t.toString());
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent pagerContent, int categoryId) {
        // 通知 UI 层更新
        List<HomePagerContent.DataDTO> data = pagerContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataDTO> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        // 加载更多的数据
        // 1.拿到当前页面
        mCurrentPage = pagerMap.get(categoryId);
        // 2.页码加1
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        // 3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        // 4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                // 结果
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "load more code --- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handleLoadMoreResult(result, categoryId);
                } else {
                    handleLoadMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                // 请求失败
                LogUtils.d(CategoryPagerPresenterImpl.this, t.toString());
                handleLoadMoreError(categoryId);
            }
        });
    }

    private void handleLoadMoreResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId) {
        // 记得前面页码已经++了，如果错误的话，我们要把数据复原
        mCurrentPage--;
        pagerMap.put(categoryId, mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            callback.onLoadMoreError();
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPager) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPager);
        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        return task;
    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}

