package com.logicsoftbd.lsl.ui.v_1_ui.config;

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

public class V1_StyleWiseSewingOperationConfigRecyclerAdapter extends RecyclerView.Adapter<V1_StyleWiseSewingOperationConfigRecyclerAdapter.ViewHolder> {
        List<V1_ConfigSewingOperationModel> sewingOutputOperationItemModels;
        Context context;
        private OnOperationSelectListener mOnImageHeadListener;
        private Integer selectedItemPosition = 0;

        public V1_StyleWiseSewingOperationConfigRecyclerAdapter(List<V1_ConfigSewingOperationModel> sewingOutputOperationItemModels, Context context, OnOperationSelectListener mOnDefectSelectListener) {
                this.sewingOutputOperationItemModels = sewingOutputOperationItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_operation_item, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                viewHolder.operationNameTV.setText(sewingOutputOperationItemModels.get(position).getOperationName());
                if(sewingOutputOperationItemModels.get(position).getStatus()){
                        viewHolder.operationNameTV.setChecked(true);
                        viewHolder.operationNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
                }else{
                        viewHolder.operationNameTV.setChecked(false);
                        viewHolder.operationNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
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
//                                        selectedItemPosition = getAdapterPosition();
//                                        notifyDataSetChanged();
                                        onDefectSelectListener.onOperationHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnOperationSelectListener {
                void onOperationHeadClick(int position, View v);
        }
}
