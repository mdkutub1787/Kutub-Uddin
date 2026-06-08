package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;
import com.logicsoftbd.lsl.databinding.TransferLayoutBinding;

import java.util.List;

public class V1_transfer_in_recycler_adapter extends RecyclerView.Adapter<V1_transfer_in_recycler_adapter.MyViewHolder>{
    Context context;
    static List<DtlsPart> transferInListOfBarcodeInAdapter;

    public V1_transfer_in_recycler_adapter(Context context, List<DtlsPart> transferInListOfBarcodeInAdapter) {
        this.context = context;
        this.transferInListOfBarcodeInAdapter = transferInListOfBarcodeInAdapter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transfer_layout, parent, false);
        return new V1_transfer_in_recycler_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DtlsPart dtlsPart = transferInListOfBarcodeInAdapter.get(position);
        holder.binding.rfidItemTV.setText(String.valueOf(dtlsPart.getBARCODENO()));
        holder.binding.serialTV.setText(String.valueOf(position+1));
        holder.binding.weightTV.setText(String.valueOf(dtlsPart.getQNTY()));
    }

    @Override
    public int getItemCount() {
        if (transferInListOfBarcodeInAdapter.isEmpty()) {
            return 0;
        }
        return transferInListOfBarcodeInAdapter.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TransferLayoutBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TransferLayoutBinding.bind(itemView);
            itemView.findViewById(R.id.deleteBtn).setOnClickListener(view -> {
                DtlsPart dtlsPart= transferInListOfBarcodeInAdapter.get(getAdapterPosition());
                DataControllerTransferIn.instance.getClickInterface().deleteItemFromTransferInRecycleView(dtlsPart);
            });
        }
    }
}
