package com.logicsoftbd.lsl.ui.slitting_squeezing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzFunctionalbatchResponse;

import java.util.ArrayList;
import java.util.List;

public class SlittingFunctionalBatchScanRecyclerViewAdapter extends RecyclerView.Adapter<SlittingFunctionalBatchScanRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<SlitteringSequzFunctionalbatchResponse.DtlsIndex> dtlsIndexArrayList = new ArrayList<>();

    public SlittingFunctionalBatchScanRecyclerViewAdapter(Context context, List<SlitteringSequzFunctionalbatchResponse.DtlsIndex> dtlsIndexArrayList) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
    }

    @NonNull
    @Override
    public SlittingFunctionalBatchScanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slittering_sequezzing_functional_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlittingFunctionalBatchScanRecyclerViewAdapter.ViewHolder holder, int position) {
        holder._slTV.setText(String.valueOf(position+1));
        holder._cons_compositeTV.setText(dtlsIndexArrayList.get(position).getConstComposition());
        holder._gsmTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._dia_widthTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._d_w_widthTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._roll_noTV.setText(dtlsIndexArrayList.get(position).getRollNo());
        holder._batchQtyTV.setText(dtlsIndexArrayList.get(position).getBatchQty());
        holder._prodQtyTV.setText(dtlsIndexArrayList.get(position).getProdQty());

        if( dtlsIndexArrayList.get(position).getBarcode_status() != null && dtlsIndexArrayList.get(position).getBarcode_status() ){
            holder._layout.setBackgroundColor(Color.parseColor("#D7E3D7"));
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView _slTV, _cons_compositeTV, _gsmTV, _dia_widthTV, _d_w_widthTV, _roll_noTV, _batchQtyTV, _prodQtyTV;

        LinearLayout _layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _slTV = itemView.findViewById(R.id.slTV);
            _cons_compositeTV = itemView.findViewById(R.id.cons_compositeTV);
            _gsmTV = itemView.findViewById(R.id.gsmTV);
            _dia_widthTV = itemView.findViewById(R.id.dia_widthTV);
            _d_w_widthTV = itemView.findViewById(R.id.d_w_widthTV);
            _roll_noTV = itemView.findViewById(R.id.roll_noTV);
            _batchQtyTV = itemView.findViewById(R.id.batchQtyTV);
            _prodQtyTV = itemView.findViewById(R.id.prodQtyTV);
            _layout = itemView.findViewById(R.id.layout);
        }


    }

}
