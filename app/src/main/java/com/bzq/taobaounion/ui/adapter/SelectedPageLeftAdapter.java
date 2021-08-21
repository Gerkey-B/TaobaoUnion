package com.bzq.taobaounion.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/7/30
 */
public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataDTO> mData = new ArrayList<>();

    private int mCurrentSelectedPagePosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public SelectedPageLeftAdapter.InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectedPageLeftAdapter.InnerHolder holder, @SuppressLint("RecyclerView") int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPagePosition == position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.color_pager_bg, null));
        } else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white, null));
        }

        SelectedPageCategory.DataDTO dataDTO = mData.get(position);
        itemTv.setText(dataDTO.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && mCurrentSelectedPagePosition != position) {
                    // 修改当前选中的位置
                    mCurrentSelectedPagePosition = position;
                    mItemClickListener.onLeftItemClick(dataDTO);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataDTO> data = categories.getData();
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPagePosition));
        }
    }

    public void reLoaded() {
        mCurrentSelectedPagePosition = 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }


    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectedPageCategory.DataDTO item);
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}

