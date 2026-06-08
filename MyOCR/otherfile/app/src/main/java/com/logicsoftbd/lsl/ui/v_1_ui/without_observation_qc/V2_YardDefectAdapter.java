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

public class V2_YardDefectAdapter extends RecyclerView.Adapter<V2_YardDefectAdapter.ViewHolder> {
    List<V1_BarcodeDetailsFromBatchFinishQCResponse.Yarn> dtlsIndexArrayList;
    Context context;
    OnYarnHeadListener mOnSelectListener;

    public V2_YardDefectAdapter(List<V1_BarcodeDetailsFromBatchFinishQCResponse.Yarn> dtlsIndexArrayList, Context context, OnYarnHeadListener mOnSelectListener) {
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
    public void onBindViewHolder(@NonNull V2_YardDefectAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
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
        OnYarnHeadListener onYarnDefectClick;

        public ViewHolder(@NonNull View itemView, OnYarnHeadListener onSelectClick) {
            super(itemView);
            defectNameTV = itemView.findViewById(R.id.defectNameTV);

            this.onYarnDefectClick = onSelectClick;
            defectNameTV.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.defectNameTV) {
                onYarnDefectClick.onYarnDefectClick(getAdapterPosition(), v);
            }
        }
    }

    public interface OnYarnHeadListener {
        void onYarnDefectClick(int position, View v);
    }
}
