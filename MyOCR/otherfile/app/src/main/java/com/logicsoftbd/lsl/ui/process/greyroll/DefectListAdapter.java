package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;

import com.logicsoftbd.lsl.ui.base.BaseViewHolder;
import com.logicsoftbd.lsl.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefectListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_EXPAND = 2;


    private boolean mExpanded = false;

    private DefectListAdapter.Callback mCallback;
    private List<DefectListModel.Result> mProcessResponseList = new ArrayList<>();
    private Activity mActivity;

    public DefectListAdapter(List<DefectListModel.Result> openSourceResponseList,Activity activity) {
        mProcessResponseList = openSourceResponseList;
        mActivity=activity;
    }

    public void setCallback(DefectListAdapter.Callback callback) {
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
                return new DefectListAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_defect_layout, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new DefectListAdapter.EmptyViewHolder(
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

    public void addItems(List<DefectListModel.Result> repoList) {
        if(mProcessResponseList != null && mProcessResponseList.size() > 0)
            mProcessResponseList.clear();

        mProcessResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onRepoEmptyViewRetryClick();
        void onItemDelete(int position);

        void onItemForwardClick(int position,String name,boolean current);
    }



    public class ViewHolder extends BaseViewHolder {



        @BindView(R.id.edit_defect)
        EditText edit_defect;

        @BindView(R.id.lay_top)
        LinearLayout lay_top;




        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            //coverImageView.setImageDrawable(null);

        }

        public void onBind(final int position) {
            super.onBind(position);
            final DefectListModel.Result repo = mProcessResponseList.get(position);


//
            if (repo.getDEFECT_NAME() != null) {
                edit_defect.setText(repo.getDEFECT_NAME());
            }
//
//            if(repo.getQNTY() > 0) {
//                textViewQty.setText(repo.getQNTY()+"");
//            }


            edit_defect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (repo.isFirst()){
                        if (AppConstants.STATUS_CODE_FAILED.equals("knitting")){
                            repo.setFirst(true);
                        }
                        else {
                            repo.setFirst(false);
                        }

                       // lay_top.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                    }
                    else{
                        repo.setFirst(true);


                    }
                    notifyDataSetChanged();

                    mCallback.onItemForwardClick( position,repo.getDEFECT_NAME(),repo.isFirst());
                    Log.e("fff","dd"+repo.isFirst());


                }
            });
            if (repo.isFirst()){
                lay_top.setBackgroundColor(mActivity.getResources().getColor(R.color.click));

            }
            else{
               // edit_defect.setTextColor(mActivity.getResources().getColor(R.color.black));


            }


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
