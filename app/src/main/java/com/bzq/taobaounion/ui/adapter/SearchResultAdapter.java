package com.bzq.taobaounion.ui.adapter;

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
import com.bzq.taobaounion.model.DTO.SearchResult;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.ToastUtils;
import com.bzq.taobaounion.utils.UrlUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerkey
 * Created on 2021/8/14
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.InnerHolder> {

    private List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> mData = new ArrayList<>();
    private OnListenItemClickListener mItemListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchResultAdapter.InnerHolder holder, int position) {
        SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO dto = mData.get(position);
        holder.setData(dto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.onItemClick(mData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SearchResult result) {
        List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> resultData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mData.clear();
        mData.addAll(resultData);
        notifyDataSetChanged();
    }

    public void addData(List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> resultData) {
        int oldSize = mData.size();
        mData.addAll(resultData);
        notifyItemRangeChanged(oldSize, resultData.size());
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

        public void setData(SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO dto) {
            Context context = itemView.getContext();

            title.setText(dto.getTitle());

            String finalPrice = dto.getZk_final_price();
            String couponAmount = dto.getCoupon_amount();

            if (couponAmount == null) {
                couponAmount = "0";
            }

            float resultPrice = Float.parseFloat(finalPrice) - Float.parseFloat(couponAmount);
            String resultPriceFormat = context.getResources().getString(R.string.text_goods_result_price);
            String tkTotalSales = dto.getTk_total_sales();
            LogUtils.d(this, "tkTotalSales -- > " + tkTotalSales);
            finalPriceTv.setText(String.format(resultPriceFormat, resultPrice));

            offPriceTv.setText(String.format("省%1$s元", couponAmount));

            String originalPriceFormat = context.getResources().getString(R.string.text_goods_original_price);
            float originalPrice = Float.parseFloat(finalPrice);
            originalPriceTv.setText(String.format(originalPriceFormat, originalPrice));

            String salesVolumeFormat = context.getResources().getString(R.string.text_goods_sales_volume);
            salesVolumeTv.setText(String.format(salesVolumeFormat, dto.getVolume()));

            String pictUrl = dto.getPict_url();
            String targetUrl = UrlUtils.getCoverPathWithSize(pictUrl, 100);
            Glide.with(context).load(targetUrl).into(cover);
        }
    }

    public void setOnListenItemClickListener(OnListenItemClickListener listener) {
        this.mItemListener = listener;
    }

    public interface OnListenItemClickListener {
        void onItemClick(SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO item);
    }
}

