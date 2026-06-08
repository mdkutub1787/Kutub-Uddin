package com.logicsoftbd.lsl.ui.process.greyroll;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishFabricQrAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;

    private int selectedPosition = -1;
    private boolean mExpanded = false;

    private FinishFabricQrAdapter.Callback mCallback;
    private List<FinishFabricQrCodeResponses.ResultSet> mProcessResponseList = new ArrayList<>();

    public FinishFabricQrAdapter(List<FinishFabricQrCodeResponses.ResultSet> openSourceResponseList) {
        mProcessResponseList = openSourceResponseList;
    }

    public void setCallback(FinishFabricQrAdapter.Callback callback) {
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
                return new FinishFabricQrAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_qr, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new FinishFabricQrAdapter.EmptyViewHolder(
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

    public void addItems(ArrayList<FinishFabricQrCodeResponses.ResultSet> repoList) {
        if(mProcessResponseList != null && mProcessResponseList.size() > 0)
            mProcessResponseList.clear();

        mProcessResponseList.addAll(repoList);
        mExpanded=false;
        notifyDataSetChanged();
    }
    public void add(boolean ok) {
        if (ok){
            mExpanded=true;
        }
        else{
            mExpanded=false;
        }

        notifyDataSetChanged();
    }
    public interface Callback {
        void onRepoEmptyViewRetryClick();
        void onItemDelete(int position);

        void onItemForwardClick(FinishFabricQrCodeResponses.ResultSet resultSet,String type);
    }



    public class ViewHolder extends BaseViewHolder {


        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.checkbox)
        CheckBox checkbox;


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

            final FinishFabricQrCodeResponses.ResultSet repo = mProcessResponseList.get(position);

            selectedPosition=position;

            if (repo.getBARCODE_NO() != null) {
                titleTextView.setText(repo.getBARCODE_NO());
            }



            if (mExpanded){
                checkbox.setChecked(true);
            }
            else{
                checkbox.setChecked(false);
            }

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // checkbox.setChecked(true);
                    if (checkbox.isChecked()){
                        mCallback.onItemForwardClick(repo,"Add");

                    }
                    else{
                        mCallback.onItemForwardClick(repo,"Delete");
                    }

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
            messageTextView.setVisibility(View.GONE);
        }

        @Override
        protected void clear() {

        }

    }
}

