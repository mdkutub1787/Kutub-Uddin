package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;

import java.util.List;

public class V1_RFIDTransferStoreHeaderRecyclerAdapter extends RecyclerView.Adapter<V1_RFIDTransferStoreHeaderRecyclerAdapter.ViewHolder> {
        List<V1_RFIDTransferModel.Datum> rfidTransferModels;
        private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        Context context;

        public V1_RFIDTransferStoreHeaderRecyclerAdapter(List<V1_RFIDTransferModel.Datum> rfidTransferModels, Context context) {
                this.rfidTransferModels = rfidTransferModels;
                this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
                return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                viewHolder.titleTextView.setText("Desc: "+rfidTransferModels.get(position).getTxtItemDesc()+"  -  "+"Lot: "+rfidTransferModels.get(position).getTxtYarnLot());
                // Create layout manager with initial prefetch item count
                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        viewHolder.rvSubItem.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                );
                layoutManager.setInitialPrefetchItemCount(rfidTransferModels.get(position).getRfids().size());
                // Create sub item view adapter
                V1_RFIDTransferStoreSubItemRecyclerAdapter subItemAdapter = new V1_RFIDTransferStoreSubItemRecyclerAdapter(rfidTransferModels.get(position).getRfids());
                viewHolder.rvSubItem.setLayoutManager(layoutManager);
                viewHolder.rvSubItem.setAdapter(subItemAdapter);
                viewHolder.rvSubItem.setRecycledViewPool(viewPool);
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
                TextView titleTextView;
                RecyclerView rvSubItem;

                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        titleTextView = itemView.findViewById(R.id.titleTextView);
                        rvSubItem = itemView.findViewById(R.id.rv_sub_item);
                }
        }
}

