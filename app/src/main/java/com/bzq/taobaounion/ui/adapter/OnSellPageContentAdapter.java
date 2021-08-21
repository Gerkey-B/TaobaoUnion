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
import com.bzq.taobaounion.model.DTO.OnSellContent;
import com.bzq.taobaounion.utils.UrlUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerkey
 * Created on 2021/8/4
 */
public class OnSellPageContentAdapter extends RecyclerView.Adapter<OnSellPageContentAdapter.InnerHolder> {

    private List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mDataDTOList = new ArrayList<>();
    private OnOnSellContentItemClickListener mContentListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OnSellPageContentAdapter.InnerHolder holder, int position) {
        OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO mapDataDTO = mDataDTOList.get(position);
        holder.setData(mapDataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentListener != null) {
                    mContentListener.onItemClick(mapDataDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataDTOList.size();
    }

    public void setData(OnSellContent result) {
        mDataDTOList.clear();
        mDataDTOList.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    public void setMoreLoadedData(OnSellContent result) {
        List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> moreLoadData = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int oldDataSize = mDataDTOList.size();
        mDataDTOList.addAll(moreLoadData);
        notifyItemRangeChanged(oldDataSize - 1,moreLoadData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_cover)
        public ImageView cover;

        @BindView(R.id.on_sell_title)
        public TextView title;

        @BindView(R.id.on_sell_original_price)
        public TextView originalPriceTv;

        @BindView(R.id.on_sell_final_prise)
        public TextView finalPriceTv;
        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO mapDataDTO) {
            Context context = itemView.getContext();
            title.setText(mapDataDTO.getTitle());

            float originalPrice = Float.parseFloat(mapDataDTO.getZk_final_price());
            String originalPriceFormat = context.getResources().getString(R.string.text_on_sell_originalPrice);
            originalPriceTv.setText(String.format(originalPriceFormat, originalPrice));

            long couponAmount = mapDataDTO.getCoupon_amount();
            float finalPrice = originalPrice - couponAmount;
            String finalPriceFormat = context.getResources().getString(R.string.text_on_sell_finalPrice);
            finalPriceTv.setText(String.format(finalPriceFormat, finalPrice));

            String pictUrl = mapDataDTO.getPict_url();
            String targetUrl = UrlUtils.getCoverPathWithSize(pictUrl, 160);
            Glide.with(context).load(targetUrl).into(cover);
        }
    }

    public void setOnOnSellContentItemClickListener(OnOnSellContentItemClickListener listener) {
        this.mContentListener = listener;
    }

    public interface OnOnSellContentItemClickListener{
        void onItemClick(OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item);
    }
}

