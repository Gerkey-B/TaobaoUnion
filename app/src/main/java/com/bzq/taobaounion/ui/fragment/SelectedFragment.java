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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.model.DTO.SelectedContent;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;
import com.bzq.taobaounion.presenter.ISelectedPagePresenter;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.ui.activity.TicketActivity;
import com.bzq.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.bzq.taobaounion.ui.adapter.SelectedPageRightAdapter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.utils.SizeUtils;
import com.bzq.taobaounion.view.ISelectedPageCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/6/27
 */
public class SelectedFragment extends BaseFragment implements ISelectedPageCallback {


    private ISelectedPagePresenter mSelectedPagePresenter;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    @BindView(R.id.with_bar_title)
    public TextView barTitleTv;

    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageRightAdapter mRightAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected View loadRootView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @androidx.annotation.Nullable @Nullable Bundle savedInstanceState) {
        barTitleTv.setText("精选宝贝");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageRightAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dp2px(getContext(), 4);
                int leftAndRight = SizeUtils.dp2px(getContext(), 6);
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });
    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();

    }

    @Override
    protected void initListener() {
        mLeftAdapter.setOnLeftItemClickListener(new SelectedPageLeftAdapter.OnLeftItemClickListener() {
            @Override
            public void onLeftItemClick(SelectedPageCategory.DataDTO item) {
                // 左边的分类点击
                mSelectedPagePresenter.getContentByCategoryId(item);
            }
        });

        mRightAdapter.setOnSelectedPageContentItemClickListener(new SelectedPageRightAdapter.OnSelectedPageContentItemClickListener() {
            @Override
            public void onItemClick(SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item) {
                // 右边的内容被点击了
                String title = item.getTitle();
                String cover = item.getPict_url();
                String url = item.getCoupon_click_url();
                if (TextUtils.isEmpty(url)) {
                    url = item.getClick_url();
                }
                ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
                ticketPresenter.getTicket(title, url, cover);
                startActivity(new Intent(getContext(), TicketActivity.class));
            }
        });
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        // 分类内容
        LogUtils.d(this, "onCategoriesLoaded --- > " + categories.toString());
        // 根据当前选中的分类，获取分类详情内容
        List<SelectedPageCategory.DataDTO> data = categories.getData();
        mSelectedPagePresenter.getContentByCategoryId(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    protected void onRetryClick() {
        // 重试
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reLoadContent();
            mLeftAdapter.reLoaded();
        }
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

    }
}
