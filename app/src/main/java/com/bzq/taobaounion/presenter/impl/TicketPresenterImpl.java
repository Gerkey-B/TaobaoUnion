package com.bzq.taobaounion.presenter.impl;

import com.bzq.taobaounion.model.Api;
import com.bzq.taobaounion.model.DTO.TicketParams;
import com.bzq.taobaounion.model.DTO.TicketResult;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.RetrofitManager;
import com.bzq.taobaounion.utils.UrlUtils;
import com.bzq.taobaounion.view.ITicketPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketPageCallback mViewCallback = null;
    private TicketResult mTicketResult;
    private String mCover;

    enum LoadState {
        // 增加一个状态判断，防止数据获取到了，callback 还没被注册，造成 UI 空白
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        onTicketLoadedLoading();
        mCover = cover;
//        LogUtils.d(this, "title --- > " + title);
//        LogUtils.d(this, "url --- > " + url);
//        LogUtils.d(this, "cover --- > " + cover);
        // 获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String targetUrl = UrlUtils.getTicketUrl(url);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPresenterImpl.this, "result code ==== > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 请求成功
                    mTicketResult = response.body();
//                    LogUtils.d(TicketPresenterImpl.this, "response body --- > " + mTicketResult.toString());
                    // 通知 UI 更新
                    onTicketLoadedSuccess();
                } else {
                    // 请求失败
                    onTicketLoadedError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                // 失败
                LogUtils.d(TicketPresenterImpl.this, t.toString());
                onTicketLoadedError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onTicketLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    private void onTicketLoadedLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void registerViewCallback(ITicketPageCallback callback) {
        if (mCurrentState != LoadState.NONE) {
            // 说明状态已经改变
            // 更新 UI
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoadedLoading();
            } else if (mCurrentState == LoadState.ERROR) {
                onTicketLoadedError();
            }
        }
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ITicketPageCallback callback) {
        this.mViewCallback = null;
    }
}

