package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out;

import android.annotation.SuppressLint;
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

public class V1_transfer_out_recycler_adapter extends RecyclerView.Adapter<V1_transfer_out_recycler_adapter.MyViewHolder>{

    Context context;
    static List<DtlsPart> listOfBarcodeInAdapter;

    public V1_transfer_out_recycler_adapter(Context context, List<DtlsPart> lists) {
        this.context = context;
        this.listOfBarcodeInAdapter = lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transfer_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DtlsPart dtlsPart = listOfBarcodeInAdapter.get(position);
        holder.binding.rfidItemTV.setText(String.valueOf(dtlsPart.getBARCODENO()));
        holder.binding.serialTV.setText(String.valueOf(position+1));
        holder.binding.weightTV.setText(String.format("%.2f", Double.parseDouble(dtlsPart.getQNTY())));
    }

    @Override
    public int getItemCount() {
        if (listOfBarcodeInAdapter.isEmpty()) {
            return 0;
        }
        return listOfBarcodeInAdapter.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TransferLayoutBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TransferLayoutBinding.bind(itemView);
            itemView.findViewById(R.id.deleteBtn).setOnClickListener(view -> {
                DtlsPart dtlsPart= listOfBarcodeInAdapter.get(getAdapterPosition());
                DataControllerTransferOut.instance.getClickInterface().deleteItemFromTransferOutRecycleView(dtlsPart);
            });
        }
    }

}