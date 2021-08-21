package com.bzq.taobaounion.presenter.impl;

import com.bzq.taobaounion.model.Api;
import com.bzq.taobaounion.model.DTO.Histories;
import com.bzq.taobaounion.model.DTO.SearchRecommend;
import com.bzq.taobaounion.model.DTO.SearchResult;
import com.bzq.taobaounion.presenter.ISearchPresenter;
import com.bzq.taobaounion.utils.JsonCacheUtils;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.RetrofitManager;
import com.bzq.taobaounion.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Gerkey
 * Created on 2021/8/8
 */
public class SearchPresenterImpl implements ISearchPresenter {

    public static final int DEFAULT_PAGE = 1;
    public static final String KEY_HISTORY = "key_history";
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int mHistoriesSize = DEFAULT_HISTORIES_SIZE;
    private final Api mApi;
    private ISearchPageCallback mSearchPageCallback = null;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyWord = null;
    private JsonCacheUtils mJsonCacheUtils;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtils = JsonCacheUtils.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORY, Histories.class);
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtils.deleteCache(KEY_HISTORY);
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesDeleted();
        }
    }

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORY, Histories.class);
        // 如果已经存在，先删除，再添加
        // 去重
        List<String> hisList = null;
        if (histories != null && histories.getHistories() != null) {
            hisList = histories.getHistories();
            if (hisList.contains(history)) {
                hisList.remove(history);
            }
        }
        // 处理没有数据的情况
        if (hisList == null) {
            hisList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(hisList);
        // 对个数进行限制
        if (hisList.size() > mHistoriesSize) {
            hisList = hisList.subList(0, mHistoriesSize);
        }
        // 添加记录
        hisList.add(history);
        // 保存记录
        mJsonCacheUtils.saveCache(KEY_HISTORY, histories);
    }

    @Override
    public void doSearch(String keyWord) {
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onLoading();
        }
        if (mCurrentKeyWord == null || !mCurrentKeyWord.equals(keyWord)) {
            saveHistory(keyWord);
            this.mCurrentKeyWord = keyWord;
        }
        Call<SearchResult> task = mApi.getSearchResult(mCurrentPage, keyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "doSearch code == > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void onError() {
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isEmpty(result)) {
                mSearchPageCallback.onEmpty();
            } else {
                mSearchPageCallback.onSearchSuccess(result);
            }
        }
    }

    private boolean isEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public void research() {
        if (mSearchPageCallback != null) {
            if (mCurrentKeyWord == null) {
                mSearchPageCallback.onEmpty();
            } else {
                doSearch(mCurrentKeyWord);
            }
        }
    }

    @Override
    public void loadMore() {
        mCurrentPage++;
        if (mSearchPageCallback != null) {
            if (mCurrentKeyWord == null) {
                mSearchPageCallback.onEmpty();
            } else {
                doSearchMore();
            }
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.getSearchResult(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "doSearchMore code == > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoadMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isEmpty(result)) {
                mSearchPageCallback.onMoreLoadEmpty();
            } else {
                mSearchPageCallback.onMoreLoaded(result);
            }
        }
    }

    private void onLoadMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onMoreLoadError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "getRecommendWords code --- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    List<SearchRecommend.DataDTO> data = response.body().getData();
                    if (mSearchPageCallback != null) {
                        mSearchPageCallback.onRecommendWordLoaded(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.mSearchPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        mSearchPageCallback = null;
    }
}

