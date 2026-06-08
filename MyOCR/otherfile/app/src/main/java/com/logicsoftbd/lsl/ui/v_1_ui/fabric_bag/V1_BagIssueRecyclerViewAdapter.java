package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;

import java.util.List;

public class V1_BagIssueRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagIssueRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagIssueResponse.ResultSet> bagIssueResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_BagIssueRecyclerViewAdapter(List<V1_BagIssueResponse.ResultSet> bagIssueResponses, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.bagIssueResponses = bagIssueResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagIssueRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_issue_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bagNoTV.setText(bagIssueResponses.get(position).getBagNo());
        viewHolder.qrCodeNoTV.setText(bagIssueResponses.get(position).getQrNo());
        viewHolder.companyNameTV.setText(bagIssueResponses.get(position).getCompanyName());
        viewHolder.storeNameTV.setText(bagIssueResponses.get(position).getStoreName());
        viewHolder.weightTV.setText(bagIssueResponses.get(position).getWeight());
        viewHolder.uomTV.setText(bagIssueResponses.get(position).getUom());
        viewHolder.noOfRollTV.setText(bagIssueResponses.get(position).getRollQnty());
        viewHolder.roomNameTV.setText(bagIssueResponses.get(position).getRoomName());
        viewHolder.rackNameTV.setText(bagIssueResponses.get(position).getRackName());
        viewHolder.shelfNameTV.setText(bagIssueResponses.get(position).getShelfName());
        viewHolder.checkBox.setChecked(bagIssueResponses.get(position).getChecked());

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bagIssueResponses.get(position).setChecked(isChecked);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bagIssueResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView companyNameTV, bagNoTV, storeNameTV, qrCodeNoTV, noOfRollTV, weightTV, uomTV, roomNameTV, rackNameTV, shelfNameTV;
        private ImageButton removeBT, moreBT;
        private CheckBox checkBox;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            qrCodeNoTV = itemView.findViewById(R.id.qrCodeNoTV);
            noOfRollTV = itemView.findViewById(R.id.noOfRollTV);
            companyNameTV = itemView.findViewById(R.id.companyNameTV);
            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            uomTV = itemView.findViewById(R.id.uomTV);
            roomNameTV = itemView.findViewById(R.id.roomNameTV);
            rackNameTV = itemView.findViewById(R.id.rackNameTV);
            shelfNameTV = itemView.findViewById(R.id.shelfNameTV);
            checkBox = itemView.findViewById(R.id.checkBox);

            removeBT = itemView.findViewById(R.id.removeBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
}
