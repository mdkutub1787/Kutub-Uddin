package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ButtonListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;


    private boolean mExpanded = false;

    private ButtonListAdapter.Callback mCallback;
    private List<DefectInchModel.Result> mProcessResponseList = new ArrayList<>();
    private Activity mActivity;

    public ButtonListAdapter(List<DefectInchModel.Result> openSourceResponseList, Activity activity) {
        mProcessResponseList = openSourceResponseList;
        mActivity=activity;
    }

    public void setCallback(ButtonListAdapter.Callback callback) {
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
                return new ButtonListAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new ButtonListAdapter.EmptyViewHolder(
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

    public void addItems(List<DefectInchModel.Result> repoList) {
        if(mProcessResponseList != null && mProcessResponseList.size() > 0)
            mProcessResponseList.clear();

        mProcessResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onItemClick(String name);
    }



    public class ViewHolder extends BaseViewHolder {



        @BindView(R.id.button)
        Button button;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //coverImageView.setImageDrawable(null);

        }

        public void onBind(final int position) {
            super.onBind(position);
            final DefectInchModel.Result repo = mProcessResponseList.get(position);


//
            if (repo.getDEFECT_INCH_NAME() != null) {
                button.setText("P"+repo.getDEFECT_INCH_NAME());
            }
//
//            if(repo.getQNTY() > 0) {
//                textViewQty.setText(repo.getQNTY()+"");
//            }


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mCallback.onItemClick(repo.getDEFECT_INCH_ID());
                    Log.e("fff","dd"+position);


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
