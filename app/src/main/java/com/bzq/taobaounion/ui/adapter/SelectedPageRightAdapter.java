package com.bzq.taobaounion.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bzq.taobaounion.R;
import com.bzq.taobaounion.model.DTO.SelectedContent;
import com.bzq.taobaounion.utils.Constants;
import com.bzq.taobaounion.utils.UrlUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerkey
 * Created on 2021/7/31
 */
public class SelectedPageRightAdapter extends RecyclerView.Adapter<SelectedPageRightAdapter.InnerHolder> {

    private final List<SelectedContent
            .DataDTO
            .TbkDgOptimusMaterialResponseDTO
            .ResultListDTO.MapDataDTO> mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentListItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_right_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectedPageRightAdapter.InnerHolder holder, int position) {
        SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentListItemClickListener.onItemClick(itemData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mapData = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            mData.clear();
            for (int i = 0;i < mapData.size();++i) {
                if (mapData.get(i).getTitle() != null) {
                    mData.add(mapData.get(i));
                }
            }
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover)
        public ImageView cover;

        @BindView(R.id.selected_off_price)
        public TextView offPriceTv;

        @BindView(R.id.selected_title)
        public TextView title;

        @BindView(R.id.selected_buy_btn)
        public TextView buyBtn;

        @BindView(R.id.selected_original_price)
        public TextView originalPriceTv;



        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO itemData) {
            Context context = itemView.getContext();

            title.setText(itemData.getTitle());

            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                buyBtn.setVisibility(View.GONE);
                originalPriceTv.setText("晚啦，没有优惠券了");
            } else {
                buyBtn.setVisibility(View.VISIBLE);

                String finalPrice = itemData.getZk_final_price();
                String finalPriceFormat = context.getResources().getString(R.string.text_selected_original_price);
                originalPriceTv.setText(String.format(finalPriceFormat, finalPrice));
            }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPriceTv.setVisibility(View.GONE);
            } else {
                offPriceTv.setVisibility(View.VISIBLE);
                offPriceTv.setText(itemData.getCoupon_info());
            }

            String pictUrl = itemData.getPict_url();
            String targetUrl = UrlUtils.getCoverPathWithSize(pictUrl, 220);
            Glide.with(context).load(targetUrl).into(cover);
        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener) {
        this.mContentListItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener {
        void onItemClick(SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item);
    }
}

