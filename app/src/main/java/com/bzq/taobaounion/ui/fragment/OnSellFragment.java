package com.bzq.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.model.DTO.OnSellContent;
import com.bzq.taobaounion.presenter.IOnSellPagePresenter;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.ui.activity.TicketActivity;
import com.bzq.taobaounion.ui.adapter.OnSellPageContentAdapter;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.utils.SizeUtils;
import com.bzq.taobaounion.utils.ToastUtils;
import com.bzq.taobaounion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/6/27
 */
public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {

    private IOnSellPagePresenter mOnSellPagePresenter;

    public static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView(R.id.with_bar_title)
    public TextView barTitleTv;

    private OnSellPageContentAdapter mContentAdapter;


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @androidx.annotation.Nullable @Nullable Bundle savedInstanceState) {
        barTitleTv.setText("特惠宝贝");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected View loadRootView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container,false);
    }

    @Override
    protected void initPresenter() {
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void initListener() {
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                // 去加载更多的内容
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });

        mContentAdapter.setOnOnSellContentItemClickListener(new OnSellPageContentAdapter.OnOnSellContentItemClickListener() {
            @Override
            public void onItemClick(OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item) {
                // 处理数据
                String title = item.getTitle();
                // 有些商品有优惠，而有些没有，相应的跳转界面也不同
                String url = item.getCoupon_click_url();
                if (TextUtils.isEmpty(url)) {
                    url = item.getClick_url();
                }
                String cover = item.getPict_url();
                // 拿到 ticketPresenter 去加载数据
                ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
                ticketPresenter.getTicket(title,url,cover);
                startActivity(new Intent(getContext(), TicketActivity.class));
            }
        });
    }

    @Override
    protected void initView(View rootView) {
        mContentList.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT));
        mContentAdapter = new OnSellPageContentAdapter();
        mContentList.setAdapter(mContentAdapter);
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = SizeUtils.dp2px(getContext(), 2.5f);
                outRect.bottom = SizeUtils.dp2px(getContext(), 2.5f);
                outRect.left = SizeUtils.dp2px(getContext(), 2.5f);
                outRect.right = SizeUtils.dp2px(getContext(), 2.5f);
            }
        });
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        setUpState(State.SUCCESS);
        // 加载的数据从这里回来
        mContentAdapter.setData(result);

    }

    @Override
    public void onMoreLoaded(OnSellContent result) {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("成功加载了更多的宝贝");
        // 加载更多的数据从这里回来
        mContentAdapter.setMoreLoadedData(result);
    }

    @Override
    public void onMoreLoadError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常，请稍后重试...");
    }

    @Override
    public void onMoreLoadEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多的内容...");
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void onRetryClick() {
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.reLoad();
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void release() {
        super.release();
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }
    }
}



