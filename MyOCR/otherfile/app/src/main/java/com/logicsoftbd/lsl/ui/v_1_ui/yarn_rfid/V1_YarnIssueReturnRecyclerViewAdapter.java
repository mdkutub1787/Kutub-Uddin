package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;



import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnResponse;

import java.util.ArrayList;
import java.util.List;

public class V1_YarnIssueReturnRecyclerViewAdapter extends RecyclerView.Adapter<V1_YarnIssueReturnRecyclerViewAdapter.ViewHolder> {
    private List<V1_YarnIssueReturnResponse.DtlsIssueDetail> yarnIssueReturnList;
    private Context context;
    private OnHeadListener mOnHeadListener;

    public V1_YarnIssueReturnRecyclerViewAdapter(List<V1_YarnIssueReturnResponse.DtlsIssueDetail> yarnIssueReturnList, OnHeadListener mOnHeadListener, Context context) {
        this.yarnIssueReturnList = yarnIssueReturnList;
        this.mOnHeadListener = mOnHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_YarnIssueReturnRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfif_tag_for_issue_return_layout, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.serialTV.setText(String.valueOf(position+1));
        viewHolder.itemDetailsTV.setText(yarnIssueReturnList.get(position).getItemNameDetails());
        viewHolder.lotNoTV.setText(yarnIssueReturnList.get(position).getLotNo());
        viewHolder.brandTV.setText(yarnIssueReturnList.get(position).getBrand());
        viewHolder.issueDateTV.setText(yarnIssueReturnList.get(position).getIssueDate());

        if(yarnIssueReturnList.get(position).getSelectedStatus()){
           viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#6EABD1"));
        }else{
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return yarnIssueReturnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView serialTV, itemDetailsTV, lotNoTV, brandTV, issueDateTV;
        private LinearLayout linearLayout;
        private OnHeadListener onHeadListener;


        public ViewHolder(@NonNull View itemView, OnHeadListener mOnHeadListener) {
            super(itemView);
            serialTV = itemView.findViewById(R.id.serialTV);
            itemDetailsTV = itemView.findViewById(R.id.itemDetailsTV);
            lotNoTV = itemView.findViewById(R.id.lotNoTV);
            brandTV = itemView.findViewById(R.id.brandTV);
            issueDateTV = itemView.findViewById(R.id.issueDateTV);

            linearLayout = itemView.findViewById(R.id.linearLayout);


            this.onHeadListener = mOnHeadListener;
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linearLayout:
                    onHeadListener.onHeadClick(getAdapterPosition(), v);
                    break;

            }
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }
}
