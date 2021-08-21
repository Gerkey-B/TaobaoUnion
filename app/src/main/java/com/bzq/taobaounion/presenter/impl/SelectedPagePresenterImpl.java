package com.bzq.taobaounion.presenter.impl;

import com.bzq.taobaounion.model.Api;
import com.bzq.taobaounion.model.DTO.SelectedContent;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;
import com.bzq.taobaounion.presenter.ISelectedPagePresenter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.RetrofitManager;
import com.bzq.taobaounion.utils.UrlUtils;
import com.bzq.taobaounion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Gerkey
 * Created on 2021/7/29
 */
public class SelectedPagePresenterImpl implements ISelectedPagePresenter {

    private ISelectedPageCallback mViewCallback = null;
    private final Api mApi;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        // 拿 retrofit
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this, "getCategories code --- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 成功
                    SelectedPageCategory result = response.body();
//                    LogUtils.d(SelectedPagePresenterImpl.this, "getCategories result === " + result);
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }
                } else {
                    // 失败
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void getContentByCategoryId(SelectedPageCategory.DataDTO item) {
        Integer categoryId = item.getFavorites_id();
        String targetUrl = UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedPageContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this, "getContentByCategoryId code --- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
//                    LogUtils.d(SelectedPagePresenterImpl.this, "getContentByCategoryId result = > " + result);
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reLoadContent() {
        getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }
}

