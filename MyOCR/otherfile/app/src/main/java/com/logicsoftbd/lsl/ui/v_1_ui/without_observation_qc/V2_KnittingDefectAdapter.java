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
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeDetailsFromBatchFinishQCResponse;

import java.util.List;

public class V2_KnittingDefectAdapter extends RecyclerView.Adapter<V2_KnittingDefectAdapter.ViewHolder> {
    private static final String TAG = "FinishProductionSlittin";
    List<V1_BarcodeDetailsFromBatchFinishQCResponse.Knitting> dtlsIndexArrayList;
    Context context;
    OnKnittingHeadListener mOnSelectListener;

    public V2_KnittingDefectAdapter(List<V1_BarcodeDetailsFromBatchFinishQCResponse.Knitting> dtlsIndexArrayList, Context context, OnKnittingHeadListener mOnSelectListener) {
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.context = context;
        this.mOnSelectListener = mOnSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defect_layout, parent, false);
        return new ViewHolder(view, mOnSelectListener);
    }

    @SuppressLint({"DefaultLocale", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull V2_KnittingDefectAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        viewHolder.defectNameTV.setText(dtlsIndexArrayList.get(position).getDefectName());
        if(dtlsIndexArrayList.get(position).isStatus()){
            viewHolder.defectNameTV.setBackground(context.getResources().getDrawable(R.drawable.approve_approve_background));
        }else{
            viewHolder.defectNameTV.setBackground(context.getResources().getDrawable(R.drawable.backgroundimgbutton));
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
        TextView defectNameTV;
        OnKnittingHeadListener onKnittingDefectClick;

        public ViewHolder(@NonNull View itemView, OnKnittingHeadListener onSelectClick) {
            super(itemView);
            defectNameTV = itemView.findViewById(R.id.defectNameTV);

            this.onKnittingDefectClick = onSelectClick;
            defectNameTV.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.defectNameTV) {
                onKnittingDefectClick.onKnittingDefectClick(getAdapterPosition(), v);
            }
        }
    }

    public interface OnKnittingHeadListener {
        void onKnittingDefectClick(int position, View v);
    }
}
