package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;

import java.util.ArrayList;
import java.util.List;

public class V1_BagKeepingQCRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagKeepingQCRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagKeepingDataBySystemResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;
    private OnRejectHeadListener mOnRejectHeadListener;

    public V1_BagKeepingQCRecyclerViewAdapter(List<V1_BagKeepingDataBySystemResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, OnRejectHeadListener mOnRejectHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.mOnRejectHeadListener = mOnRejectHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagKeepingQCRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_keeping_qc_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener, mOnRejectHeadListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        V1_BagKeepingDataBySystemResponse.ResultSet currentItem = bagKeepingResponses.get(position);

        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getWeight());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getIrIb());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
        viewHolder.rfidNoTV.setText(bagKeepingResponses.get(position).getRfidNo());
        viewHolder.qrCodeNoTV.setText(bagKeepingResponses.get(position).getQrNo());

        if(bagKeepingResponses.get(position).getIsRejecting().equals("1")){
            viewHolder.rejectBT.setText("YES");
        }else{
            viewHolder.rejectBT.setText("NO");
        }

        if(bagKeepingResponses.get(position).getIsRejecting().equals("1")){
            viewHolder.ly.setBackgroundColor(Color.parseColor("#c9ebee"));
            viewHolder.rejectBT.setBackgroundResource(R.drawable.approve_unapprove_background);
        }else{
            viewHolder.ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.rejectBT.setBackgroundResource(R.drawable.approve_approve_background);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bagKeepingResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bagNoTV, qrCodeNoTV, rfidNoTV, batchNoTV, weightTV, irbrTV, colorTV, buyerNameTV;
        private Button rejectBT;
        private ImageButton removeBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;
        private OnRejectHeadListener onRejectHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener, OnRejectHeadListener mOnRejectHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            qrCodeNoTV = itemView.findViewById(R.id.qrCodeNoTV);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            irbrTV = itemView.findViewById(R.id.irbrTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            rejectBT = itemView.findViewById(R.id.rejectBT);
            ly = itemView.findViewById(R.id.ly);

            removeBT = itemView.findViewById(R.id.removeBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);

            this.onRejectHeadListener = mOnRejectHeadListener;
            rejectBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.rejectBT:
                    onRejectHeadListener.onRejectHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
    public interface OnRejectHeadListener {
        void onRejectHeadClick(int position, View v);
    }
}
