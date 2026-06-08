package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric_roll_receive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;

import java.util.List;

public class V1_GmtFinishReceiveRecyclerViewAdapter extends RecyclerView.Adapter<V1_GmtFinishReceiveRecyclerViewAdapter.ViewHolder> {
    private List<V1_GMTFinishReceiveResponse.Data> detailsList;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_GmtFinishReceiveRecyclerViewAdapter(List<V1_GMTFinishReceiveResponse.Data> detailsList, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.detailsList = detailsList;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_GmtFinishReceiveRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gtm_finish_receive_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.serialTV.setText(String.valueOf(position + 1));
        viewHolder.bundleTV.setText(detailsList.get(position).getBundleNo());
        viewHolder.bookingTV.setText(detailsList.get(position).getGrouping());
        viewHolder.itemTV.setText(detailsList.get(position).getItemNumberName());
        viewHolder.colorTV.setText(detailsList.get(position).getColorNumberName());
        viewHolder.sizeTV.setText(detailsList.get(position).getSizeNumberName());
        viewHolder.bundleQNtyTV.setText(detailsList.get(position).getBundleQnty());
        viewHolder.finRCVQntyTV.setText(detailsList.get(position).getProductionQnty());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView serialTV, bundleTV, bookingTV, itemTV, colorTV, sizeTV, bundleQNtyTV, finRCVQntyTV;
        private ImageButton removeBT, moreBT;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            serialTV = itemView.findViewById(R.id.serialTV);
            bundleTV = itemView.findViewById(R.id.bundleTV);
            bookingTV = itemView.findViewById(R.id.bookingTV);
            itemTV = itemView.findViewById(R.id.itemTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            sizeTV = itemView.findViewById(R.id.sizeTV);
            bundleQNtyTV = itemView.findViewById(R.id.bundleQNtyTV);
            finRCVQntyTV = itemView.findViewById(R.id.finRCVQntyTV);
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
