package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RejectSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingInputPendingModel;


import java.util.ArrayList;
import java.util.List;

public class V1_SewingInputPendingAdapter extends ArrayAdapter<V1_SewingInputPendingModel> {

    ArrayList<V1_SewingInputPendingModel> pendingModels;
    LayoutInflater vi;
    int Resource;

    public V1_SewingInputPendingAdapter(@NonNull Context context, int resource, ArrayList<V1_SewingInputPendingModel> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pendingModels = objects;
        Resource = resource;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 500;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PendingViewHolder holder;

        holder = new V1_SewingInputPendingAdapter.PendingViewHolder();

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.pending_data_item, null);

            holder.seral = convertView.findViewById(R.id.serial_no);
            holder.job_no = convertView.findViewById(R.id.job_no);
            holder.po_no = convertView.findViewById(R.id.break_down);
            holder.pen_barcode = convertView.findViewById(R.id.barcode);
            holder.pen_bundle = convertView.findViewById(R.id.bundle);
            holder.pen_cut = convertView.findViewById(R.id.cut);
            holder.pen_pro_qty = convertView.findViewById(R.id.pro_qty);

            convertView.setTag(holder);
        }else {
            holder = (PendingViewHolder) convertView.getTag();
        }

        int pos = position + 1;

        holder.seral.setText(String.valueOf(pos));
        holder.job_no.setText(pendingModels.get(position).getJOB_NO());
        holder.po_no.setText(pendingModels.get(position).getPO_NUMBER());
        holder.pen_barcode.setText(pendingModels.get(position).getBARCODE_NO());
        holder.pen_bundle.setText(pendingModels.get(position).getBUNDLE_NO());
        holder.pen_cut.setText(pendingModels.get(position).getCUT_NO());
        holder.pen_pro_qty.setText(pendingModels.get(position).getPRODUCTION_QNTY());

        return convertView;
    }

    public class PendingViewHolder {
        public TextView seral, job_no, po_no, pen_barcode, pen_bundle, pen_cut, pen_pro_qty;
    }

    public static class V1_SewingOutputOperationRecyclerAdapter extends RecyclerView.Adapter<V1_SewingOutputOperationRecyclerAdapter.ViewHolder> {
            List<V1_RejectSewingOutputOperationItemModel> sewingOutputOperationItemModels;
            Context context;
            private OnRejectDefectSelectListener mOnImageHeadListener;

            public V1_SewingOutputOperationRecyclerAdapter(List<V1_RejectSewingOutputOperationItemModel> sewingOutputOperationItemModels, Context context, OnRejectDefectSelectListener mOnDefectSelectListener) {
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
    //                if(sewingOutputOperationItemModels.get(position).getStatus() == 1){
    //                        viewHolder.operationNameTV.setChecked(true);
    //                }
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
                    private TextView operationNameTV;
                    OnRejectDefectSelectListener onDefectSelectListener;

                    public ViewHolder(@NonNull View itemView, OnRejectDefectSelectListener mOnDefectSelectListener) {
                            super(itemView);
                            operationNameTV = itemView.findViewById(R.id.operationNameTV);
                            this.onDefectSelectListener = mOnDefectSelectListener;
                            operationNameTV.setOnClickListener(this);
                    }

                    @Override
                    public void onClick(View v) {
                            switch (v.getId()){
                                    case R.id.operationNameTV:
                                            onDefectSelectListener.onRejectDefectHeadClick(getAdapterPosition(), v);
                                            break;
                            }
                    }
            }

            public interface OnRejectDefectSelectListener {
                    void onRejectDefectHeadClick(int position, View v);
            }
    }
}
