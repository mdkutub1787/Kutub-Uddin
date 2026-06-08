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
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Janisharali on 25-05-2017.
 */

public class CuttingQcAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;


    private boolean mExpanded = false;

    private Callback mCallback;
    private List<CuttingQcBarcodeResponse.Result.DetailsPart.BundleData> mProcessResponseList = new ArrayList<>();

    public CuttingQcAdapter(List<CuttingQcBarcodeResponse.Result.DetailsPart.BundleData> openSourceResponseList) {
        mProcessResponseList = openSourceResponseList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.onBind(position);

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_gmts, parent, false));
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

    public void addItems(List<CuttingQcBarcodeResponse.Result.DetailsPart.BundleData> repoList) {
        if(mProcessResponseList != null && mProcessResponseList.size() > 0)
            mProcessResponseList.clear();

        mProcessResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onRepoEmptyViewRetryClick();
        void onItemDelete(int position);

        void onItemForwardClick(int position);
    }



    public class ViewHolder extends BaseViewHolder {


        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.text_view_qty)
        TextView textViewQty;



        @BindView(R.id.iv_forward)
        ImageView ivForward;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //coverImageView.setImageDrawable(null);
            titleTextView.setText("");
        }

        public void onBind(final int position) {
            super.onBind(position);
            final CuttingQcBarcodeResponse.Result.DetailsPart.BundleData repo = mProcessResponseList.get(position);

            if (repo.getBundleNo() != null) {
                titleTextView.setText(repo.getBundleNo());
            }

            if(repo.getQcPassQty() > 0) {
                textViewQty.setText(repo.getQcPassQty()+"");
            }


            ivForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onItemForwardClick( position);
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
