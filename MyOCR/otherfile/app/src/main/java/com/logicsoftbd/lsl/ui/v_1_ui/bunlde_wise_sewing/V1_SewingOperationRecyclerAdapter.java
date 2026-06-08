package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputOperationItemModel;
import java.util.List;

public class V1_SewingOperationRecyclerAdapter extends RecyclerView.Adapter<V1_SewingOperationRecyclerAdapter.ViewHolder> {
        List<V1_SewingOutputOperationItemModel> sewingOutputOperationItemModels;
        Context context;
        private OnDefectSelectListener mOnImageHeadListener;

        public V1_SewingOperationRecyclerAdapter(List<V1_SewingOutputOperationItemModel> sewingOutputOperationItemModels, Context context, OnDefectSelectListener mOnDefectSelectListener) {
                this.sewingOutputOperationItemModels = sewingOutputOperationItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public V1_SewingOperationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_operation_layout, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull V1_SewingOperationRecyclerAdapter.ViewHolder viewHolder, int position) {
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
                OnDefectSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnDefectSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        operationNameTV = itemView.findViewById(R.id.operationNameTV);
                        this.onDefectSelectListener = mOnDefectSelectListener;
                        operationNameTV.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.operationNameTV:
                                        onDefectSelectListener.onDefectHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnDefectSelectListener {
                void onDefectHeadClick(int position, View v);
        }
}
