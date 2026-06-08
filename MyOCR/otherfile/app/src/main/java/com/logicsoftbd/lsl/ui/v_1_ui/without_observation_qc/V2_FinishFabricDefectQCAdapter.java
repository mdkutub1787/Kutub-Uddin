package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishFabricQCDefectModel;

import java.util.List;

public class V2_FinishFabricDefectQCAdapter extends RecyclerView.Adapter<V2_FinishFabricDefectQCAdapter.ViewHolder> {
    private static final String TAG = "FinishProductionSlittin";
    List<V1_FinishFabricQCDefectModel> dtlsIndexArrayList;
    Context context;
    OnIncrementHeadListener mOnIncrementListener;
    OnDecrementHeadListener mOnDecrementListener;
    OnRemoveHeadListener mOnRemoveListener;

    public V2_FinishFabricDefectQCAdapter(List<V1_FinishFabricQCDefectModel> dtlsIndexArrayList, Context context, OnIncrementHeadListener mOnIncrementListener, OnDecrementHeadListener mOnDecrementListener, OnRemoveHeadListener mOnRemoveListener) {
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.context = context;
        this.mOnIncrementListener = mOnIncrementListener;
        this.mOnDecrementListener = mOnDecrementListener;
        this.mOnRemoveListener = mOnRemoveListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defect_count_finish_fabric_layout, parent, false);
        return new ViewHolder(view, mOnIncrementListener, mOnDecrementListener, mOnRemoveListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull V2_FinishFabricDefectQCAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        int p = position + 1;
        viewHolder.serialTV.setText(String.valueOf(p));
        viewHolder.defectNameTV.setText(dtlsIndexArrayList.get(position).getDefectName());
        viewHolder.defectCountTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getDefectCount()));
        viewHolder.defectFoundTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getDefectFound()));
        viewHolder.pointTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getDefectPenalty()));
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
        TextView serialTV, defectNameTV, defectCountTV, defectFoundTV, pointTV;
        private Button increamentBtn, decrementBtn;
        private ImageView removeBtn;
        OnIncrementHeadListener onInCrementHeadListener;
        OnDecrementHeadListener onDecrementHeadListener;
        OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnIncrementHeadListener onIncrementClick, OnDecrementHeadListener onDecrementClick, OnRemoveHeadListener onRemoveClick) {
            super(itemView);
            serialTV = itemView.findViewById(R.id.serialTV);
            defectNameTV = itemView.findViewById(R.id.defectNameTV);
            defectCountTV = itemView.findViewById(R.id.defectCountTV);
            defectFoundTV = itemView.findViewById(R.id.defectFoundTV);
            pointTV = itemView.findViewById(R.id.pointTV);
            increamentBtn = itemView.findViewById(R.id.increamentBtn);
            decrementBtn = itemView.findViewById(R.id.decrementBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);

            this.onInCrementHeadListener = onIncrementClick;
            increamentBtn.setOnClickListener(this);

            this.onDecrementHeadListener = onDecrementClick;
            decrementBtn.setOnClickListener(this);

            this.onRemoveHeadListener = onRemoveClick;
            removeBtn.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.increamentBtn:
                    onInCrementHeadListener.onIncrementClick(getAdapterPosition(), v);
                    break;
                case R.id.decrementBtn:
                    onDecrementHeadListener.onDecrementClick(getAdapterPosition(), v);
                    break;
                case R.id.removeBtn:
                    onRemoveHeadListener.onRemoveClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnIncrementHeadListener {
        void onIncrementClick(int position, View v);
    }

    public interface OnDecrementHeadListener {
        void onDecrementClick(int position, View v);
    }

    public interface OnRemoveHeadListener {
        void onRemoveClick(int position, View v);
    }
}
