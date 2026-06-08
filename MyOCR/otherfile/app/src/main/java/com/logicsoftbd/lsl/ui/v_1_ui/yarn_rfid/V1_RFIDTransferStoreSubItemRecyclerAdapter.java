package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;

import java.util.List;

public class V1_RFIDTransferStoreSubItemRecyclerAdapter extends RecyclerView.Adapter<V1_RFIDTransferStoreSubItemRecyclerAdapter.ViewHolder> {
        List<V1_RFIDTransferModel.Rfid> rfidTransferModels;

        public V1_RFIDTransferStoreSubItemRecyclerAdapter(List<V1_RFIDTransferModel.Rfid> rfidTransferModels) {
                this.rfidTransferModels = rfidTransferModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfid_transfer_item_layout, parent, false);
                return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
                viewHolder.rfidNoTV.setText(rfidTransferModels.get(position).getRfidNo());
                viewHolder.weightTV.setText(rfidTransferModels.get(position).getBagWeight());

                viewHolder.unSelectCheckBox.setChecked(rfidTransferModels.get(position).isSelected());
                viewHolder.unSelectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        rfidTransferModels.get(viewHolder.getAdapterPosition()).setSelected(isChecked);
                });
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return rfidTransferModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
                TextView rfidNoTV, weightTV, serialTV;
                CheckBox unSelectCheckBox;

                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
                        weightTV = itemView.findViewById(R.id.weightTV);
                        serialTV = itemView.findViewById(R.id.serialTV);
                        unSelectCheckBox = itemView.findViewById(R.id.unSelectCheckBox);
                }
        }
}

