package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeByBatchForQCResponse;

import java.util.List;

public class V2_BatchWiseBarcodeForFinishQCAdapter extends RecyclerView.Adapter<V2_BatchWiseBarcodeForFinishQCAdapter.ViewHolder> {
    private static final String TAG = "FinishProductionSlittin";
    List<V1_BarcodeByBatchForQCResponse.Datum> dtlsIndexArrayList;
    Context context;
    OnHeadListener mOnSelectListener;

    public V2_BatchWiseBarcodeForFinishQCAdapter(List<V1_BarcodeByBatchForQCResponse.Datum> dtlsIndexArrayList, Context context, OnHeadListener mOnSelectListener) {
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.context = context;
        this.mOnSelectListener = mOnSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roll_layout, parent, false);
        return new ViewHolder(view, mOnSelectListener);
    }

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull V2_BatchWiseBarcodeForFinishQCAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        viewHolder.bcodeNoTV.setText(dtlsIndexArrayList.get(position).getBarcodeNo());
        if(dtlsIndexArrayList.get(position).getStatus() != null && dtlsIndexArrayList.get(position).getStatus()){
            viewHolder.bcodeNoTV.setBackground(context.getResources().getDrawable(R.drawable.approve_approve_background));
        }else{
            viewHolder.bcodeNoTV.setBackground(context.getResources().getDrawable(R.drawable.unselect_background));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bcodeNoTV;
        OnHeadListener onSelectClick;

        public ViewHolder(@NonNull View itemView, OnHeadListener onSelectClick) {
            super(itemView);
            bcodeNoTV = itemView.findViewById(R.id.bcodeNoTV);

            this.onSelectClick = onSelectClick;
            bcodeNoTV.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bcodeNoTV) {
                onSelectClick.onSelectClick(getAdapterPosition(), v);
            }
        }
    }

    public interface OnHeadListener {
        void onSelectClick(int position, View v);
    }
}
