package com.logicsoftbd.lsl.ui.v_1_ui.production_operation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationItemModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class V1_OperationRecyclerViewAdapter extends RecyclerView.Adapter<V1_OperationRecyclerViewAdapter.ViewHolder> {
    ArrayList<V1_OperationItemModel> operationItemModels = new ArrayList<>();
    Context context;

    private OnEndHeadListener mOnEndHeadListener;
    private OnCancelHeadListener mOnCancelHeadListener;

    public V1_OperationRecyclerViewAdapter(ArrayList<V1_OperationItemModel> operationItemModels, OnEndHeadListener mOnEndHeadListener, OnCancelHeadListener mOnCancelHeadListener, Context context) {
        this.operationItemModels = operationItemModels;
        this.mOnEndHeadListener = mOnEndHeadListener;
        this.mOnCancelHeadListener = mOnCancelHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_OperationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operational_item_layout, parent, false);
        return new ViewHolder(view, mOnEndHeadListener, mOnCancelHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(operationItemModels.get(position).getIs_selected()){
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#D7E3D7"));
        }
        viewHolder.slTV.setText(String.valueOf(position+1));
        viewHolder.countingTV.setText(operationItemModels.get(position).getTimer());
        viewHolder.empIdTV.setText(operationItemModels.get(position).getEmp_id());
        viewHolder.bundleIdTV.setText(operationItemModels.get(position).getBundleNo());
        viewHolder.operationTV.setText(operationItemModels.get(position).getOperation_name());
        viewHolder.colorTV.setText(operationItemModels.get(position).getColor());
        viewHolder.sizeTV.setText(operationItemModels.get(position).getSize());
        viewHolder.qtyTV.setText(operationItemModels.get(position).getQty());
        viewHolder.jobNoTV.setText(operationItemModels.get(position).getJobNo());
        viewHolder.buyerTV.setText(operationItemModels.get(position).getBuyerName());
        viewHolder.poNumberTV.setText(operationItemModels.get(position).getJobNo());
        viewHolder.countryTV.setText(String.valueOf(operationItemModels.get(position).getCountry()));
        viewHolder.gmtsTV.setText(String.valueOf(operationItemModels.get(position).getItem()));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return operationItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView countingTV, slTV, empIdTV, bundleIdTV, operationTV, colorTV, sizeTV, qtyTV, jobNoTV, buyerTV, poNumberTV, countryTV, gmtsTV;
        private Button endBT, cancleBT;
        private LinearLayout  linearLayout;
        private Timer timer;
        private TimerTask timerTask;
        private Double time = 0.0;
        OnEndHeadListener onEndHeadListener;
        OnCancelHeadListener onCancelHeadListener;

        public ViewHolder(@NonNull View itemView, OnEndHeadListener mOnEndHeadListener, OnCancelHeadListener mOnCancelHeadListener) {
            super(itemView);

            countingTV = itemView.findViewById(R.id.countingTV);
            slTV = itemView.findViewById(R.id.slTV);
            empIdTV = itemView.findViewById(R.id.empIdTV);
            bundleIdTV = itemView.findViewById(R.id.bundleIdTV);
            operationTV = itemView.findViewById(R.id.operationTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            sizeTV = itemView.findViewById(R.id.sizeTV);
            qtyTV = itemView.findViewById(R.id.qtyTV);
            jobNoTV = itemView.findViewById(R.id.jobNoTV);
            jobNoTV = itemView.findViewById(R.id.jobNoTV);
            buyerTV = itemView.findViewById(R.id.buyerTV);
            poNumberTV = itemView.findViewById(R.id.poNumberTV);
            countryTV = itemView.findViewById(R.id.countryTV);
            gmtsTV = itemView.findViewById(R.id.gmtsTV);
            linearLayout = itemView.findViewById(R.id.ly);

            endBT = itemView.findViewById(R.id.endBT);
            cancleBT = itemView.findViewById(R.id.cancleBT);

            this.onEndHeadListener = mOnEndHeadListener;
            endBT.setOnClickListener(this);

            this.onCancelHeadListener = mOnCancelHeadListener;
            cancleBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.endBT:
                    onEndHeadListener.onEndHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.cancleBT:
                    onCancelHeadListener.onCancelHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnEndHeadListener {
        void onEndHeadClick(int position, View v);
    }

    public interface OnCancelHeadListener {
        void onCancelHeadClick(int position, View v);
    }
}
