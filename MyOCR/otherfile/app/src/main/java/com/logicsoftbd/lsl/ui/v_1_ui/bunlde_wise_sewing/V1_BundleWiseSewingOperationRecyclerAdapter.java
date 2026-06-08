package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigSewingOperationModel;

import java.util.List;

public class V1_BundleWiseSewingOperationRecyclerAdapter extends RecyclerView.Adapter<V1_BundleWiseSewingOperationRecyclerAdapter.ViewHolder> {
        List<V1_ConfigSewingOperationModel> sewingOutputOperationItemModels;
        Context context;
        private OnOperationSelectListener mOnImageHeadListener;
        private int selectedPosition = -1;

        public V1_BundleWiseSewingOperationRecyclerAdapter(List<V1_ConfigSewingOperationModel> sewingOutputOperationItemModels, Context context, OnOperationSelectListener mOnDefectSelectListener) {
                this.sewingOutputOperationItemModels = sewingOutputOperationItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public V1_BundleWiseSewingOperationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_operation_item, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull V1_BundleWiseSewingOperationRecyclerAdapter.ViewHolder viewHolder, int position) {
                viewHolder.operationNameTV.setText(sewingOutputOperationItemModels.get(position).getOperationName());
                if(selectedPosition != position){
                        viewHolder.operationNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
                        viewHolder.operationNameTV.setChecked(false);
                }else{
                        viewHolder.operationNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
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
                OnOperationSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnOperationSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        operationNameTV = itemView.findViewById(R.id.operationNameTV);
                        this.onDefectSelectListener = mOnDefectSelectListener;
                        operationNameTV.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.operationNameTV:
                                        selectedPosition = getAdapterPosition();
                                        onDefectSelectListener.onOperationHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnOperationSelectListener {
                void onOperationHeadClick(int position, View v);
        }
}
