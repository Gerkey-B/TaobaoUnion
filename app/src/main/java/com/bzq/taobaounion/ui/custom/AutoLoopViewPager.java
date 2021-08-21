package com.bzq.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.utils.LogUtils;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @author Gerkey
 * Created on 2021/7/22
 */
public class AutoLoopViewPager extends ViewPager {

    /**
     * 切换间隔时长，单位毫秒
      */
    private static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull @NotNull Context context) {
        this(context,null);
    }

    public AutoLoopViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        // 读取属性
        init(context,attrs);
    }

    private void init(@NotNull Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopViewPager);
        // 获取属性
        mDuration = t.getInteger(R.styleable.AutoLoopViewPager_MyDuration,(int) DEFAULT_DURATION);
        LogUtils.d(this, "mDuration --- > " + mDuration);
        // 释放资源
        t.recycle();
    }

    private boolean isLoop = false;

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            // 先拿到当前的位置
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this,mDuration);
            }
        }
    };

    public void startLoop() {
        isLoop = true;
        post(mTask);
    }

    public void stopLoop() {
        isLoop = false;
        removeCallbacks(mTask);
    }

    /**
     * 设置切换时长
     * @param duration
     */
    public void setDuration(long duration) {
        this.mDuration =  duration;
    }
}

