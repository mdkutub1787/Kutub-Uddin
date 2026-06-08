/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.ui.process.greyroll;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.ui.base.BaseViewHolder;
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Janisharali on 25-05-2017.
 */

public class ReceiveAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;

    private boolean mExpanded = false;

    private Callback mCallback;
    private List<BarcodeResponse.Challan.ProductBarcode> mProcessResponseList = new ArrayList<>();

    public ReceiveAdapter(List<BarcodeResponse.Challan.ProductBarcode> openSourceResponseList) {
        mProcessResponseList = openSourceResponseList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.onBind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mExpanded = (mExpanded == false ? true: false);
                notifyItemChanged(position);
            }
        });


    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_barcode, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mProcessResponseList != null && mProcessResponseList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mProcessResponseList != null && mProcessResponseList.size() > 0) {
            return mProcessResponseList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BarcodeResponse.Challan.ProductBarcode> repoList) {
        if(mProcessResponseList != null && mProcessResponseList.size() > 0)
            mProcessResponseList.clear();

        mProcessResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onRepoEmptyViewRetryClick();
        void onItemDelete(int position);
    }



    public class ViewHolder extends BaseViewHolder {


        @BindView(R.id.title_text_view)
        TextView titleTextView;

        @BindView(R.id.sub_title_text_view1)
        TextView subTitleTextView1;
        @BindView(R.id.sub_title_text_view)
        TextView subTitleTextView;

        @BindView(R.id.sub_sub_title_text_view)
        TextView subSubTitleTextView;

        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        @BindView(R.id.sub_item)
        LinearLayout subItem;

        @BindView(R.id.sub_text_1)
        TextView subText1;

        @BindView(R.id.sub_text_2)
        TextView subText2;

        @BindView(R.id.sub_text_3)
        TextView subText3;

        @BindView(R.id.sub_text_4)
        TextView subText4;

       /* @BindView(R.id.sub_text_5)
        TextView subText5;*/

        @BindView(R.id.sub_text_6)
        TextView subText6;

        @BindView(R.id.sub_text_7)
        TextView subText7;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            subTitleTextView.setText("");
            subSubTitleTextView.setText("");
        }

        public void onBind(final int position) {
            super.onBind(position);
            subItem.setVisibility(mExpanded ? View.VISIBLE : View.GONE);
            final BarcodeResponse.Challan.ProductBarcode repo = mProcessResponseList.get(position);

            if (repo.getBarcodeNo() != null) {
                titleTextView.setText(repo.getBarcodeNo());
            }
            if (repo.getBuyerName() != null) {
                subTitleTextView1.setText(ViewUtils.makeSectionOfTextBold("Job No: "+repo.getJobNo(), "Job No:"));
                subTitleTextView.setText(ViewUtils.makeSectionOfTextBold("Buyer: "+repo.getBuyerName(), "Buyer:"));
            }
//            if (repo.getPoNumber() != null) {
                subSubTitleTextView.setText(ViewUtils.makeSectionOfTextBold("Order No: "+repo.getPoNumber()+"", "Order No: "));
          //  }

            subText1.setText(ViewUtils.makeSectionOfTextBold("Body Part: "+ repo.getBodyPartName(), "Body Part: "));
            subText2.setText(ViewUtils.makeSectionOfTextBold("Construction: "+ repo.getConstruction(), "Construction: "));
            subText3.setText(ViewUtils.makeSectionOfTextBold("Composition: "+ repo.getComposition(), "Composition: "));
            subText4.setText(ViewUtils.makeSectionOfTextBold("GSM: "+ repo.getGsm(), "GSM: "));
           // subText5.setText(ViewUtils.makeSectionOfTextBold("Dia: "+ repo.getD(), "Dia: "));
            subText6.setText(ViewUtils.makeSectionOfTextBold("Fabric Color: "+ repo.getColorName(), "Fabric Color: "));
            subText7.setText(ViewUtils.makeSectionOfTextBold("Color Range: "+ repo.getColorRangeName(), "Color Range: "));

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onItemDelete(position);
                }
            });

        }
    }

    public class EmptyViewHolder extends BaseViewHolder {


        @BindView(R.id.tv_message)
        TextView messageTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

    }
}
