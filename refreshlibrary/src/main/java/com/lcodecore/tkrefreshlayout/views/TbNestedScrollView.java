package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @author Gerkey
 * Created on 2021/7/14
 * 嵌套滑动知识
 */
public class TbNestedScrollView extends NestedScrollView {

    private static final String TAG = "TbNestedScrollView";
    private int mHeaderHeight;
    private int originalHeight = 0;
    private RecyclerView mRecyclerView;

    public TbNestedScrollView(@NonNull @NotNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    public void setHeaderHeight(int headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull @NotNull View target, int dx, int dy,
                                  @NonNull @NotNull int[] consumed, int type) {
        if(target instanceof RecyclerView) {
            this.mRecyclerView = (RecyclerView) target;
        }
        if (originalHeight < mHeaderHeight) {
            scrollBy(dx,dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        this.originalHeight = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类是否已经滑动到了底部
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            // 正数表示手指上划，内容下滑，负数表示手指下划，内容上滑
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
//            Log.d(TAG,"isBottom -==== > " + isBottom);
            return isBottom;
        }
        return false;
    }
}

