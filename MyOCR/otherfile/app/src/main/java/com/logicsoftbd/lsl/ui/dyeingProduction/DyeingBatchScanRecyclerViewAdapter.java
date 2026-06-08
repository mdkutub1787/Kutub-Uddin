package com.logicsoftbd.lsl.ui.dyeingProduction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.DyeingProdBatchScanResponse;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DyeingBatchScanRecyclerViewAdapter extends RecyclerView.Adapter<DyeingBatchScanRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<DyeingProdBatchScanResponse.DtlsIndex> dtlsIndexArrayList = new ArrayList<>();

    public DyeingBatchScanRecyclerViewAdapter(Context context, List<DyeingProdBatchScanResponse.DtlsIndex> dtlsIndexArrayList) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
    }

    @NonNull
    @Override
    public DyeingBatchScanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dyeing_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DyeingBatchScanRecyclerViewAdapter.ViewHolder holder, int position) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        holder._slTV.setText(String.valueOf(position+1));
        holder._cons_compositeTV.setText(dtlsIndexArrayList.get(position).getConsComps());
        holder._gsmTV.setText(dtlsIndexArrayList.get(position).getGsm());
        holder._dia_widthTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._d_w_widthTV.setText(dtlsIndexArrayList.get(position).getDiaWidth());
        holder._roll_noTV.setText(dtlsIndexArrayList.get(position).getBarcodeNo());
        holder._batchQtyTV.setText(decimalFormat.format(Double.parseDouble(dtlsIndexArrayList.get(position).getBatchQnty())));
        holder._prodQtyTV.setText(decimalFormat.format(Double.parseDouble(dtlsIndexArrayList.get(position).getProdQty())));
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView _slTV, _cons_compositeTV, _gsmTV, _dia_widthTV, _d_w_widthTV, _roll_noTV, _batchQtyTV, _prodQtyTV;

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
        }


    }

}
