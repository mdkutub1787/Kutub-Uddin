package com.logicsoftbd.lsl.ui.dyeingProduction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.DyeingProFunctionalBatchResponse;

import java.util.ArrayList;
import java.util.List;

public class DyeingFunctionalBatchScanRecyclerViewAdapter extends RecyclerView.Adapter<DyeingFunctionalBatchScanRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<DyeingProFunctionalBatchResponse.FunctionalBatchIndex> functionalBatchIndices = new ArrayList<>();

    public DyeingFunctionalBatchScanRecyclerViewAdapter(Context context, List<DyeingProFunctionalBatchResponse.FunctionalBatchIndex> dtlsIndexArrayList) {
        this.context = context;
        this.functionalBatchIndices = dtlsIndexArrayList;
    }

    @NonNull
    @Override
    public DyeingFunctionalBatchScanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dyeing_functional_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DyeingFunctionalBatchScanRecyclerViewAdapter.ViewHolder holder, int position) {
        holder._slTV.setText(String.valueOf(position+1));
        holder._batchNoTV.setText(functionalBatchIndices.get(position).getBatchNo());
        holder._processStartDateTV.setText(functionalBatchIndices.get(position).getProcessStartDate());
        holder._batchQtyTV.setText(functionalBatchIndices.get(position).getBatchQty());
        holder._prodQtyTV.setText(functionalBatchIndices.get(position).getProductionQty());
    }

    @Override
    public int getItemCount() {
        return functionalBatchIndices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView _slTV, _batchNoTV, _processStartDateTV, _batchQtyTV, _prodQtyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _slTV = itemView.findViewById(R.id.slTV);
            _batchNoTV = itemView.findViewById(R.id.batchNoTV);
            _processStartDateTV = itemView.findViewById(R.id.processStartDateTV);
            _batchQtyTV = itemView.findViewById(R.id.batchQtyTV);
            _prodQtyTV = itemView.findViewById(R.id.prodQtyTV);
        }


    }

}
