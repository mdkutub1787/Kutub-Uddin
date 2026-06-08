package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnRFIDModel;

import java.util.List;

public class V1_FinishReceiveRecyclerAdapter extends RecyclerView.Adapter<V1_FinishReceiveRecyclerAdapter.ViewHolder> {
        List<FinishFabricRollReceive.DetailsSet> finishFabricRollReceive;
        Context context;
        private OnDeleteSelectListener mOnImageDeleteListener;
        private Integer selectedItemPosition = -1;

        public V1_FinishReceiveRecyclerAdapter(List<FinishFabricRollReceive.DetailsSet> finishFabricRollReceive, Context context, OnDeleteSelectListener mOnDefectSelectListener) {
                this.finishFabricRollReceive = finishFabricRollReceive;
                this.context = context;
                this.mOnImageDeleteListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finish_receive_item_layout, parent, false);
                return new ViewHolder(view, mOnImageDeleteListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
                viewHolder.barcodeTV.setText(finishFabricRollReceive.get(position).getBARCODE_NO());
                viewHolder.weightTV.setText(finishFabricRollReceive.get(position).getQNTY());
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return finishFabricRollReceive.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView barcodeTV, weightTV, serialTV;
                private ImageView deleteBtn, qrCodeClick;
                OnDeleteSelectListener onDeleteSelectListener;

                public ViewHolder(@NonNull View itemView, OnDeleteSelectListener onDeleteListener) {
                        super(itemView);
                        barcodeTV = itemView.findViewById(R.id.barcodeTV);
                        weightTV = itemView.findViewById(R.id.weightTV);
                        deleteBtn = itemView.findViewById(R.id.deleteBtn);
                        serialTV = itemView.findViewById(R.id.serialTV);

                        this.onDeleteSelectListener = onDeleteListener;
                        deleteBtn.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.deleteBtn:
                                        selectedItemPosition = getAdapterPosition();
                                        notifyDataSetChanged();
                                        onDeleteSelectListener.onDeleteClick(selectedItemPosition, v);
                                        break;
                        }
                }
        }

        public interface OnDeleteSelectListener {
                void onDeleteClick(int position, View v);
        }
}
