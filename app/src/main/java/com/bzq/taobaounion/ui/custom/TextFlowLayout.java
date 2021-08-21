package com.bzq.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/8/10
 */
public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemViewHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;


    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public int getContentSize() {
        return mTextList.size();
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        mTextList.clear();
        mTextList.addAll(textList);
        Collections.reverse(mTextList);
        // 遍历内容
        for (String text : mTextList) {
            // 添加 ChildView
            TextView itemView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            itemView.setText(text);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(itemView);
        }
    }

    /**
     * 所有行的情况
     */
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        // 单行的情况
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // 测量 ChildView
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                continue;
            }
            // 测量前
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            // 测量后
            // 在添加item 的时候进行判断
            if (line == null) {
                line = createNewLine(itemView);
            } else {
                // 不为空时，要判断当前行是否还能添加
                if (canBeAdd(itemView, line)) {
                    // 可以添加，则继续添加
                    line.add(itemView);
                } else {
                    // 不能，则新创建一行，再继续添加
                    line = createNewLine(itemView);
                }
            }
        }
        mItemViewHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemViewHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        // 测量自己
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        // 说明当前行为空，可以添加进来
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行能否继续添加新的数据
     *
     * @param itemView
     * @param line
     * @return
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        // 所有已经添加的 View 的宽度 + 间距(line.size() + 1) * 水平间距(mItemHorizontalSpace) + 自身的宽度(itemView.getMeasureWidth())
        // 条件:如果小于或等于当前控件的宽度，则可以 添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            // 叠加所有控件
            totalWidth += view.getMeasuredWidth();
        }
        // 处理间距
        totalWidth += (line.size() + 1) * mItemHorizontalSpace;
        return totalWidth <= mSelfWidth;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 摆放 ChildView
        int topOffSet = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            // 每一行
            int leftOffSet = (int) mItemHorizontalSpace;
            for (View view : views) {
                // view 时、是行里的每一个 itemView
                view.layout(leftOffSet, topOffSet, leftOffSet + view.getMeasuredWidth(), topOffSet + view.getMeasuredHeight());
                leftOffSet += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffSet += mItemViewHeight + mItemHorizontalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }
}

