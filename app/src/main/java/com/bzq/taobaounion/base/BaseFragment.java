package com.bzq.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Gerkey
 * Created on 2021/6/27
 */
public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public enum State {
        // 相关 View 的状态
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private Unbinder mBind;
    protected FrameLayout mBaseContainer;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);

        loadStatesView(inflater, container);

        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initPresenter();
        initListener();
        loadData();

        return rootView;
    }

    protected View loadRootView(@NotNull LayoutInflater inflater, @org.jetbrains.annotations.Nullable ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    @OnClick(R.id.network_error_tips)
    public void retry() {
        // 网络错误，点击重试
        LogUtils.d(this, "onRetrying...");
        // 正常来说，base 是不知道怎么去重新获取的，所以，应该由子类来实现
        onRetryClick();
    }

    protected void onRetryClick() {
        // 由子类来实现，覆盖即可
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    /**
     * 加载各种状态的 View
     * 既然有多个状态的 View ，我们就要判断和在相应条件下切换
     *
     * @param inflater
     * @param container
     */
    protected void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        // 加载成功的View
        LogUtils.d(this, "loadStatesView.....");
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        // 加载 loading 中 View
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        // 加载 网络状态码非 200 的 View (error)
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        // 加载 网络请求失败的 View (empty)
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        // 在把所有的状态都加载到容器里后，我们再根据网络请求返回的状态来决定展现给用户的是哪一个 View，
        // 所以，这里 方法 实际上只是完成一个初始化任务，在相应的子界面中还会再根据状态来调用。
        setUpState(State.NONE);

    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int id = getRootViewResId();
        return inflater.inflate(id, container, false);
    }

    /**
     * 子类通过这个方法来切换界面
     *
     * @param state
     */
    public void setUpState(State state) {
        LogUtils.d(this, "currentState is ------  " + state);
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    protected void initView(View rootView) {
        //找到控件
    }

    protected void release() {
        // 释放资源
    }

    protected void initPresenter() {
        // 创建 Presenter
    }

    /**
     * 如果子类需要设置相关的点击事件，让子类覆写
     */
    protected void initListener() {
        // 设置点击事件
    }

    protected void loadData() {
        // 加载数据
    }

    /**
     * 找到资源文件
     *
     * @return
     */
    protected abstract int getRootViewResId();
}

