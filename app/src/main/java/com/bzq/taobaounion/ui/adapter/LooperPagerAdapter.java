package com.bzq.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bzq.taobaounion.model.DTO.HomePagerContent;
import com.bzq.taobaounion.utils.UrlUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/7/11
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataDTO> mDataList = new ArrayList<>();
    private OnLooperPageClickListener mItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        int relativePosition = position % mDataList.size();
        HomePagerContent.DataDTO data = mDataList.get(relativePosition);
        int measuredWidth = container.getMeasuredWidth();
        int measuredHeight = container.getMeasuredHeight();
        int ivSize = Math.max(measuredWidth, measuredHeight) / 2;
        String coverPath = UrlUtils.getCoverPathWithSize(data.getPict_url(), ivSize);
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverPath).into(iv);
        container.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    HomePagerContent.DataDTO item = mDataList.get(relativePosition);
                    mItemClickListener.onLooperItemClick(item);
                }
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public void setData(List<HomePagerContent.DataDTO> contents) {
        mDataList.clear();
        mDataList.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return mDataList.size();
    }

    public void setOnLooperPageClickListener(OnLooperPageClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnLooperPageClickListener {
        void onLooperItemClick(HomePagerContent.DataDTO item);
    }
}

