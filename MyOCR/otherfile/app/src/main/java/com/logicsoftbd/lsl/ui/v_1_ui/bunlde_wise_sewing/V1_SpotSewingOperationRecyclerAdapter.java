package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpotSewingOutputOperationItemModel;

import java.util.List;

public class V1_SpotSewingOperationRecyclerAdapter extends RecyclerView.Adapter<V1_SpotSewingOperationRecyclerAdapter.ViewHolder> {
        List<V1_SpotSewingOutputOperationItemModel> sewingOutputOperationItemModels;
        Context context;
        private OnSpotDefectSelectListener mOnImageHeadListener;

        public V1_SpotSewingOperationRecyclerAdapter(List<V1_SpotSewingOutputOperationItemModel> sewingOutputOperationItemModels, Context context, OnSpotDefectSelectListener mOnDefectSelectListener) {
                this.sewingOutputOperationItemModels = sewingOutputOperationItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public V1_SpotSewingOperationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_operation_layout, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull V1_SpotSewingOperationRecyclerAdapter.ViewHolder viewHolder, int position) {
                viewHolder.operationNameTV.setText(sewingOutputOperationItemModels.get(position).getOperationName());
                if(sewingOutputOperationItemModels.get(position).getStatus() == 1){
                        viewHolder.operationNameTV.setChecked(true);
                }
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return sewingOutputOperationItemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private CheckBox operationNameTV;
                OnSpotDefectSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSpotDefectSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        operationNameTV = itemView.findViewById(R.id.operationNameTV);
                        this.onDefectSelectListener = mOnDefectSelectListener;
                        operationNameTV.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.operationNameTV:
                                        onDefectSelectListener.onSpotDefectHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnSpotDefectSelectListener {
                void onSpotDefectHeadClick(int position, View v);
        }
}
