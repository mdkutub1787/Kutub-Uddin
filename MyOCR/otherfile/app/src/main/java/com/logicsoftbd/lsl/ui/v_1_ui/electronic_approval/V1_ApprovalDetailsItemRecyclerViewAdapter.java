package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalItemDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class V1_ApprovalDetailsItemRecyclerViewAdapter extends RecyclerView.Adapter<V1_ApprovalDetailsItemRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<V1_ApprovalItemDetailsModel.Datum> dtlsIndexArrayList = new ArrayList<>();
    public V1_ApprovalDetailsItemRecyclerViewAdapter(Context context, List<V1_ApprovalItemDetailsModel.Datum> dtlsIndexArrayList) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_for_approval_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemNameTV.setText(dtlsIndexArrayList.get(position).getItemCategory());
        holder.lastSupplierTV.setText(dtlsIndexArrayList.get(position).getLastSupplier());
        holder.lastRateTV.setText(dtlsIndexArrayList.get(position).getLastRate());
        holder.rateTV.setText(dtlsIndexArrayList.get(position).getRate());
        holder.qtyTV.setText(dtlsIndexArrayList.get(position).getQuantity());
        holder.amountTV.setText(dtlsIndexArrayList.get(position).getAmount());
        holder.stockTV.setText(dtlsIndexArrayList.get(position).getStock());
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView itemNameTV, lastSupplierTV, lastRateTV, rateTV, qtyTV, amountTV, stockTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTV = itemView.findViewById(R.id.itemNameTV);
            lastSupplierTV = itemView.findViewById(R.id.lastSupplierTV);
            lastRateTV = itemView.findViewById(R.id.lastRateTV);
            rateTV = itemView.findViewById(R.id.rateTV);
            qtyTV = itemView.findViewById(R.id.qtyTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            stockTV = itemView.findViewById(R.id.stockTV);
        }
    }
}
