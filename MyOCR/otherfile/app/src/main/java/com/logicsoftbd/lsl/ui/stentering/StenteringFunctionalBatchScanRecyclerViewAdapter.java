package com.logicsoftbd.lsl.ui.stentering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.StenteringFunctionalBatchScanResponse;

import java.util.ArrayList;
import java.util.List;

public class StenteringFunctionalBatchScanRecyclerViewAdapter extends RecyclerView.Adapter<StenteringFunctionalBatchScanRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<StenteringFunctionalBatchScanResponse.Resultset> dtlsIndexArrayList = new ArrayList<>();

    public StenteringFunctionalBatchScanRecyclerViewAdapter(Context context, List<StenteringFunctionalBatchScanResponse.Resultset> dtlsIndexArrayList) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
    }

    @NonNull
    @Override
    public StenteringFunctionalBatchScanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stentering_functional_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StenteringFunctionalBatchScanRecyclerViewAdapter.ViewHolder holder, int position) {
        holder._slTV.setText(String.valueOf(position+1));
        holder._cons_compositeTV.setText(dtlsIndexArrayList.get(position).getConstComposition());
//        holder._gsmTV.setText(dtlsIndexArrayList.get(position).get());
        holder._dia_widthTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._d_w_widthTV.setText(dtlsIndexArrayList.get(position).getWidthDiaType());
        holder._roll_noTV.setText(dtlsIndexArrayList.get(position).getRollNo());
        holder._barcodeTV.setText(dtlsIndexArrayList.get(position).getBarcode());
        holder._batchQtyTV.setText(dtlsIndexArrayList.get(position).getBatchQty());
        holder._prodQtyTV.setText(dtlsIndexArrayList.get(position).getProdQty());
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView _slTV, _cons_compositeTV, _gsmTV, _dia_widthTV, _d_w_widthTV, _roll_noTV, _barcodeTV, _batchQtyTV, _prodQtyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _slTV = itemView.findViewById(R.id.slTV);
            _cons_compositeTV = itemView.findViewById(R.id.cons_compositeTV);
            _gsmTV = itemView.findViewById(R.id.gsmTV);
            _dia_widthTV = itemView.findViewById(R.id.dia_widthTV);
            _d_w_widthTV = itemView.findViewById(R.id.d_w_widthTV);
            _roll_noTV = itemView.findViewById(R.id.roll_noTV);
            _barcodeTV = itemView.findViewById(R.id.barcodeTV);
            _batchQtyTV = itemView.findViewById(R.id.batchQtyTV);
            _prodQtyTV = itemView.findViewById(R.id.prodQtyTV);
        }


    }

}
