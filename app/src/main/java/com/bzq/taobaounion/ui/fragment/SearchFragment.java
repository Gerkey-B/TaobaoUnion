package com.bzq.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.model.DTO.Histories;
import com.bzq.taobaounion.model.DTO.SearchRecommend;
import com.bzq.taobaounion.model.DTO.SearchResult;
import com.bzq.taobaounion.presenter.ISearchPresenter;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.ui.activity.TicketActivity;
import com.bzq.taobaounion.ui.adapter.SearchResultAdapter;
import com.bzq.taobaounion.ui.custom.TextFlowLayout;
import com.bzq.taobaounion.utils.KeyBoardUtils;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.utils.SizeUtils;
import com.bzq.taobaounion.utils.ToastUtils;
import com.bzq.taobaounion.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/6/27
 */
public class SearchFragment extends BaseFragment implements ISearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoryView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_history_container)
    public View mHistoryContainer;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_delete)
    public ImageView mHistoryDelete;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchList;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshContainer;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    @BindView(R.id.search_cancel_btn)
    public ImageView mCancelInputBtn;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;


    private ISearchPresenter mSearchPresenter;
    private SearchResultAdapter mSearchResultAdapter;

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultAdapter = new SearchResultAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = SizeUtils.dp2px(Objects.requireNonNull(getContext()), 2);
                outRect.bottom = SizeUtils.dp2px(Objects.requireNonNull(getContext()), 2);
            }
        });

        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(false);
        mRefreshContainer.setEnableOverScroll(true);

    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        // 获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        mSearchPresenter.getHistories();
//        mSearchPresenter.doSearch("键盘");
    }

    @Override
    protected void initListener() {
        mHistoryView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);
        mSearchBtn.setOnClickListener(v -> {
            // 如果搜索框有内容就进行搜索，否则就清空
            if (hasContent(false)) {
                // 发起搜索
                if (mSearchPresenter != null) {
                    String keyWord = mSearchInputBox.getText().toString().trim();
                    KeyBoardUtils.hide(Objects.requireNonNull(getContext()), v);
                    toSearch(keyWord);
                }
            } else {
                KeyBoardUtils.hide(Objects.requireNonNull(getContext()), v);
                mSearchInputBox.setText("");
                switch2HistoryPage();
            }
        });

        // 清除输入框中的内容
        mCancelInputBtn.setOnClickListener(v -> {
            mSearchInputBox.setText("");
            // 回到原始界面，并清除搜索
            switch2HistoryPage();
        });

        // 监听输入框中的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 变化的时候通知
                // 如果长度不为0，显示按钮
                // 否则隐藏
                // 经典问题，null、空格 和 多个空格的区别
                mCancelInputBtn.setVisibility(hasContent(true) ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(hasContent(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchInputBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                String keyword = v.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    return false;
                }
                toSearch(keyword);
            }
            return false;
        });

        mHistoryDelete.setOnClickListener(v -> {
            // 删除历史
            mSearchPresenter.delHistories();
        });

        mSearchResultAdapter.setOnListenItemClickListener(item -> {
            // 处理数据
            String title = item.getTitle();
            // 有些商品有优惠，而有些没有，相应的跳转界面也不同
            String url = item.getCoupon_share_url();
            if (url == null) {
                url = item.getUrl();
            }
            String cover = item.getPict_url();
            // 拿到 ticketPresenter 去加载数据
            ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
            ticketPresenter.getTicket(title, url, cover);
            startActivity(new Intent(getContext(), TicketActivity.class));
        });

        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loadMore();
                }
            }
        });
    }

    private void switch2HistoryPage() {
        setUpState(State.SUCCESS);
        mRefreshContainer.setVisibility(View.GONE);
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        if (mRecommendView.getContentSize() != 0) {
            mRecommendContainer.setVisibility(View.VISIBLE);
        } else {
            mRecommendContainer.setVisibility(View.GONE);
        }

    }

    boolean hasContent(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.getText().toString().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        }
    }

    @Override
    protected void onRetryClick() {
        // 重新加载
        if (mSearchPresenter != null) {
            LogUtils.d(this, "onRetryClick...");
            mSearchPresenter.research();
        }
    }

    @Override
    protected View loadRootView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        LogUtils.d(this, "histories --- > " + histories);
        if (histories == null || histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoryView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        // 隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        // 显示搜索结果
        mRefreshContainer.setVisibility(View.VISIBLE);
        // 设置数据
        mSearchResultAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> resultData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(resultData);
        ToastUtils.showToast("加载更多成功");
        mRefreshContainer.finishLoadmore();
    }

    @Override
    public void onMoreLoadError() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showToast("网络异常，请稍后重试...");
    }

    @Override
    public void onMoreLoadEmpty() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showToast("没有更多数据了...");
    }

    @Override
    public void onRecommendWordLoaded(List<SearchRecommend.DataDTO> recommendWords) {
        List<String> recommendKeyWords = new ArrayList<>();
        for (SearchRecommend.DataDTO item : recommendWords) {
            LogUtils.d(this, "recommendWords size --  > " + recommendWords.get(0).getKeyword());
            recommendKeyWords.add(item.getKeyword());
        }
        if (recommendWords == null || recommendKeyWords.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendContainer.setVisibility(View.VISIBLE);
        }
        mRecommendView.setTextList(recommendKeyWords);
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
    public void onFlowItemClick(String text) {
        // 发起搜索
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.setSelection(text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}

