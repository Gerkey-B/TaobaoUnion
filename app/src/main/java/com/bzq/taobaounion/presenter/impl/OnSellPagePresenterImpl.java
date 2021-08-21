package com.bzq.taobaounion.presenter.impl;

import com.bzq.taobaounion.model.Api;
import com.bzq.taobaounion.model.DTO.OnSellContent;
import com.bzq.taobaounion.presenter.IOnSellPagePresenter;
import com.bzq.taobaounion.utils.RetrofitManager;
import com.bzq.taobaounion.utils.UrlUtils;
import com.bzq.taobaounion.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Gerkey
 * Created on 2021/8/3
 */
public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallback mOnSellContentCallback = null;
    private final Api mApi;

    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if (mOnSellContentCallback != null) {
            mOnSellContentCallback.onLoading();
        }
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> call = mApi.getOnSellPageContent(targetUrl);
        call.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent Result = response.body();
                    onSuccess(Result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void onError() {
        if (mOnSellContentCallback != null) {
            mOnSellContentCallback.onError();
        }
    }

    private void onSuccess(OnSellContent result) {
        if (mOnSellContentCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    onEmpty();
                } else {
                    mOnSellContentCallback.onContentLoadedSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private void onEmpty() {
        if (mOnSellContentCallback != null) {
            mOnSellContentCallback.onEmpty();
        }
    }

    @Override
    public void reLoad() {
        getOnSellContent();
    }

    @Override
    public void loadMore() {
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> call = mApi.getOnSellPageContent(targetUrl);
        call.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent Result = response.body();
                    onMoreLoaded(Result);
                } else {
                    onMoreLoadError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                t.printStackTrace();
                onMoreLoadError();
            }
        });

    }

    private void onMoreLoadError() {
        mCurrentPage--;
        if (mOnSellContentCallback != null) {
            mOnSellContentCallback.onMoreLoadError();
        }
    }

    private void onMoreLoaded(OnSellContent result) {
        if (mOnSellContentCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    onMoreLoadEmpty();
                } else {
                    mOnSellContentCallback.onMoreLoaded(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onMoreLoadEmpty();
            }
        }
    }

    private void onMoreLoadEmpty() {
        mCurrentPage--;
        if (mOnSellContentCallback != null) {
            mOnSellContentCallback.onMoreLoadEmpty();
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.mOnSellContentCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {
        mOnSellContentCallback = null;
    }
}

