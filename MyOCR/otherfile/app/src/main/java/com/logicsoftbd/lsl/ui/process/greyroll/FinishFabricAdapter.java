package com.logicsoftbd.lsl.ui.process.greyroll;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishFabricAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;


    private boolean mExpanded = false;

    private FinishFabricAdapter.Callback mCallback;
    private List<FinishFabricResponse.ResultSet> mProcessResponseList = new ArrayList<>();

    public FinishFabricAdapter(List<FinishFabricResponse.ResultSet> openSourceResponseList) {
        mProcessResponseList = openSourceResponseList;
    }

    public void setCallback(FinishFabricAdapter.Callback callback) {
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
                return new FinishFabricAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_gmts, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new FinishFabricAdapter.EmptyViewHolder(
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

    public void addItems(List<FinishFabricResponse.ResultSet> repoList) {
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
            final FinishFabricResponse.ResultSet repo = mProcessResponseList.get(position);



            if (repo.getBARCODE_NO() != null) {
                titleTextView.setText(repo.getBARCODE_NO());
            }

            if(repo.getQNTY() > 0) {
                textViewQty.setText(repo.getQNTY()+"");
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

