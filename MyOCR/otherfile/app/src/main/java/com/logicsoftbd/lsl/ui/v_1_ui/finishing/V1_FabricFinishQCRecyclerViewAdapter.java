package com.logicsoftbd.lsl.ui.v_1_ui.finishing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricFinishQCUpdateModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssueItemModel;

import java.util.ArrayList;

public class V1_FabricFinishQCRecyclerViewAdapter extends RecyclerView.Adapter<V1_FabricFinishQCRecyclerViewAdapter.ViewHolder> {
    private ArrayList<V1_FabricFinishQCUpdateModel.ResultSet> fabricFinishQCDataList;
    private Context context;
    private OnMoreHeadListener mOnMoreHeadListener;

    public V1_FabricFinishQCRecyclerViewAdapter(ArrayList<V1_FabricFinishQCUpdateModel.ResultSet> fabricFinishQCDataList, OnMoreHeadListener mOnMoreHeadListener, Context context) {
        this.fabricFinishQCDataList = fabricFinishQCDataList;
        this.mOnMoreHeadListener = mOnMoreHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_FabricFinishQCRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_update_item_layout, parent, false);
        return new ViewHolder(view, mOnMoreHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.batchNoTV.setText(fabricFinishQCDataList.get(position).getBatchNo());
        viewHolder.fabricDescriptionTV.setText(fabricFinishQCDataList.get(position).getFabDesc());
        viewHolder.colorNameTV.setText(fabricFinishQCDataList.get(position).getFabColor());
        viewHolder.barcodeNoTV.setText(fabricFinishQCDataList.get(position).getBarcodeNo());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return fabricFinishQCDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView batchNoTV, fabricDescriptionTV, colorNameTV, barcodeNoTV;
        private LinearLayout moreLayout;
        private OnMoreHeadListener onMoreHeadListener;

        public ViewHolder(@NonNull View itemView, OnMoreHeadListener mOnMoreHeadListener) {
            super(itemView);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            fabricDescriptionTV = itemView.findViewById(R.id.fabricDescriptionTV);
            colorNameTV = itemView.findViewById(R.id.colorNameTV);
            barcodeNoTV = itemView.findViewById(R.id.barcodeNoTV);
            moreLayout = itemView.findViewById(R.id.moreLayout);

            this.onMoreHeadListener = mOnMoreHeadListener;
            moreLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.moreLayout:
                    onMoreHeadListener.onMoreHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnMoreHeadListener {
        void onMoreHeadClick(int position, View v);
    }

}
