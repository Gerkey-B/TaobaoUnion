package com.bzq.taobaounion.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.model.DTO.Categories;
import com.bzq.taobaounion.model.DTO.HomePagerContent;
import com.bzq.taobaounion.presenter.ICategoryPagerPresenter;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.ui.activity.TicketActivity;
import com.bzq.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.bzq.taobaounion.ui.adapter.LooperPagerAdapter;
import com.bzq.taobaounion.ui.custom.AutoLoopViewPager;
import com.bzq.taobaounion.utils.Constants;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.utils.SizeUtils;
import com.bzq.taobaounion.utils.ToastUtils;
import com.bzq.taobaounion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/7/2
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    @BindView(R.id.home_pager_contain_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout LooperPointContainer;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    public static HomePagerFragment newInstance(Categories.DataDTO category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        // 利用 Bundle 来传输数据
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 可见的时候，开始 loop
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 不可见的时候，暂停 loop
        looperPager.stopLoop();
    }

    @Override
    protected void initView(View rootView) {
        // RecyclerView 适配器的三连
        // 1.创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        // 2.设置适配器
        mContentList.setAdapter(mContentAdapter);
        // 3.设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Adapter 的美化
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.bottom = SizeUtils.dp2px(getContext(), 2);
                outRect.top = SizeUtils.dp2px(getContext(), 2);
            }
        });
        // ViewPager
        // 1.创建适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        // 2.设置适配器
        looperPager.setAdapter(mLooperPagerAdapter);

        // 设置与 Refresh 相关的内容
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        mLooperPagerAdapter.setOnLooperPageClickListener(new LooperPagerAdapter.OnLooperPageClickListener() {
            @Override
            public void onLooperItemClick(HomePagerContent.DataDTO item) {
                // 轮播图被点击了
                 LogUtils.d(HomePagerFragment.this, "looper item click --- > " + item.getTitle());
                handleItemClick(item);
            }
        });

        mContentAdapter.setOnListenItemClickListener(new HomePagerContentAdapter.OnListenItemClickListener() {
            @Override
            public void onItemClick(HomePagerContent.DataDTO item) {
                // 列表内容被点击了
                LogUtils.d(HomePagerFragment.this, "list item click ---- > " + item.getTitle());
                handleItemClick(item);
            }
        });

        // 解决嵌套滑动
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measuredHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    // 避免频繁的调用这个 Listener 和创建 ViewHolder，造成资源浪费
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                // 切换指示器
                upDataLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mCategoryPagePresenter != null) {
                    mCategoryPagePresenter.loadMore(mMaterialId);
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mCategoryPagePresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        // 小知识，这里有一个关于 ViewPager 的预加载和懒加载的知识(后面再去了解)
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.getContentByCategoryId(mMaterialId);
        }
        currentCategoryTitleTv.setText(title);
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents) {
        // 数据列表加载到了
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
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
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoadMoreError() {
        if (mTwinklingRefreshLayout != null) {
            ToastUtils.showToast("网络异常，请稍后重试");
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        if (mTwinklingRefreshLayout != null) {
            ToastUtils.showToast("没有更多的数据了");
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents) {
        // 拿到数据后，我们要把数据添加到底部
        mContentAdapter.addData(contents);
        // 添加完后，我们要把刷新动作结束
        if (mTwinklingRefreshLayout != null) {
            mTwinklingRefreshLayout.finishLoadmore();
            ToastUtils.showToast("加载成功");
        }
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents) {
        mLooperPagerAdapter.setData(contents);
        // 完善伪无限轮播的逻辑，解决偏移
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        // 将数据返回的地方放在中间
        looperPager.setCurrentItem(targetCenterPosition);

        // 动态添加点
        LooperPointContainer.removeAllViews();
        Context context = getContext();
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(context);
            int size = SizeUtils.dp2px(context, 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dp2px(context, 5);
            layoutParams.rightMargin = SizeUtils.dp2px(context, 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            LooperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragmeng_home_pager;
    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */
    private void upDataLooperIndicator(int targetPosition) {
        for (int i = 0; i < LooperPointContainer.getChildCount(); i++) {
            View point = LooperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    private void handleItemClick(HomePagerContent.DataDTO item) {
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
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}

