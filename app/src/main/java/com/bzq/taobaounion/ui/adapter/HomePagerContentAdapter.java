package com.bzq.taobaounion.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bzq.taobaounion.R;
import com.bzq.taobaounion.model.DTO.HomePagerContent;
import com.bzq.taobaounion.utils.UrlUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerkey
 * Created on 2021/7/9
 */
public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    private List<HomePagerContent.DataDTO> mDataDTOList = new ArrayList<>();
    private OnListenItemClickListener mItemListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomePagerContentAdapter.InnerHolder holder, int position) {
        HomePagerContent.DataDTO dataDTO = mDataDTOList.get(position);
        holder.setData(dataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    HomePagerContent.DataDTO item = mDataDTOList.get(position);
                    mItemListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataDTOList.size();
    }

    public void setData(List<HomePagerContent.DataDTO> contents) {
        mDataDTOList.clear();
        mDataDTOList.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataDTO> contents) {
        int startPosition = mDataDTOList.size();
        mDataDTOList.addAll(contents);
        notifyItemRangeChanged(startPosition, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;
        @BindView(R.id.goods_title)
        public TextView title;
        @BindView(R.id.goods_off_price)
        public TextView offPriceTv;
        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;
        @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;
        @BindView(R.id.goods_sales_volume)
        public TextView salesVolumeTv;


        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataDTO dataDTO) {
            Context context = itemView.getContext();

            title.setText(dataDTO.getTitle());

            String offPriceFormat = context.getResources().getString(R.string.text_goods_off_price);
            long couponAmount = dataDTO.getCoupon_amount();
            offPriceTv.setText(String.format(offPriceFormat, couponAmount));

            String finalPrice = dataDTO.getZk_final_price();
            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;
            String resultPriceFormat = context.getResources().getString(R.string.text_goods_result_price);
            finalPriceTv.setText(String.format(resultPriceFormat, resultPrice));

            String originalPriceFormat = context.getResources().getString(R.string.text_goods_original_price);
            float originalPrice = Float.parseFloat(finalPrice);
            originalPriceTv.setText(String.format(originalPriceFormat, originalPrice));

            String salesVolumeFormat = context.getResources().getString(R.string.text_goods_sales_volume);
            salesVolumeTv.setText(String.format(salesVolumeFormat, dataDTO.getVolume()));

            // 我们的 Glide 获取的是未进行缩放的图片，所以会造成获取的图片过大，占用的资源过大，
            // 因此我们要动态的获取图片的大小，以免造成 OOM

            int height = cover.getLayoutParams().height;
            int width = cover.getLayoutParams().width;
            int coverSize = (Math.max(height, width)) / 2;
            String coverPathWithSize = UrlUtils.getCoverPathWithSize(dataDTO.getPict_url(), coverSize);

            Glide.with(context)
                    .load(coverPathWithSize)
                    .into(cover);
        }
    }


    public void setOnListenItemClickListener(OnListenItemClickListener listener) {
        this.mItemListener = listener;
    }

    public interface OnListenItemClickListener {
        // 利用回调来设置 item 的点击事件
        void onItemClick(HomePagerContent.DataDTO item);
    }
}

