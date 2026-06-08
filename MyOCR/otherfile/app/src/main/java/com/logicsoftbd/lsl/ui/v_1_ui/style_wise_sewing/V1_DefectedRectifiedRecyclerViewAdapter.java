package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectedRectifiedModel;

import java.util.ArrayList;
import java.util.List;

public class V1_DefectedRectifiedRecyclerViewAdapter extends RecyclerView.Adapter<V1_DefectedRectifiedRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<V1_DefectedRectifiedModel> dtlsIndexArrayList = new ArrayList<>();
    private OnRectifiedHeadListener mOnHeadListener;

    public V1_DefectedRectifiedRecyclerViewAdapter(Context context, List<V1_DefectedRectifiedModel> dtlsIndexArrayList, OnRectifiedHeadListener mOnHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defect_wise_rectified_item, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String operationName = dtlsIndexArrayList.get(position).getOperationName();
        if(operationName != null){
            boolean isNumber = operationName.matches("\\d+");
            if(isNumber){
                operationName = dtlsIndexArrayList.get(position).getOperationName();
            }else{
                String[] words = operationName.split("\\s+");
                StringBuilder sb = new StringBuilder();
                for (String word : words) {
                    try{
                        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                    }catch (Exception e){

                    }

                }
                String capitalizedSentence = sb.toString().trim();
                operationName = capitalizedSentence;
            }
        }

        holder._sysChallanTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getMstId()));
        holder._operationNameTV.setText(String.valueOf(operationName));
        holder._defectNameTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getDefectNames()));
        holder._defectTypeTV.setText(dtlsIndexArrayList.get(position).getDefectTypeNames());

        holder._colorTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getColorName()));
        holder._sizeTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getSizeName()));
        holder._dtlsIDTV.setText(dtlsIndexArrayList.get(position).getDtlsId());
        holder._alterQuantityTV.setText(dtlsIndexArrayList.get(position).getAlterQty());
        holder._spotQuantityTV.setText(dtlsIndexArrayList.get(position).getSpotQty());
        holder._productionDateTV.setText(dtlsIndexArrayList.get(position).getProductionDate());

        if(dtlsIndexArrayList.get(position).getSelect()){
            holder._checkBox.setChecked(true);
            holder._sysChallanTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._sysChallanTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._operationNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._operationNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._defectNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._defectNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._defectTypeTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._defectTypeTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._colorTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._colorTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._sizeTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._sizeTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._dtlsIDTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._dtlsIDTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._alterQuantityTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._alterQuantityTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._spotQuantityTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._spotQuantityTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._productionDateTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._productionDateTV.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            holder._checkBox.setChecked(false);
            holder._operationNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._operationNameTV.setTextColor(Color.parseColor("#000000"));

            holder._defectNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._defectNameTV.setTextColor(Color.parseColor("#000000"));

            holder._defectTypeTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._defectTypeTV.setTextColor(Color.parseColor("#000000"));

            holder._colorTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._colorTV.setTextColor(Color.parseColor("#000000"));

            holder._sizeTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._sizeTV.setTextColor(Color.parseColor("#000000"));

            holder._sysChallanTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._sysChallanTV.setTextColor(Color.parseColor("#000000"));

            holder._dtlsIDTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._dtlsIDTV.setTextColor(Color.parseColor("#000000"));

            holder._alterQuantityTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._alterQuantityTV.setTextColor(Color.parseColor("#000000"));

            holder._spotQuantityTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._spotQuantityTV.setTextColor(Color.parseColor("#000000"));

            holder._productionDateTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._productionDateTV.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _operationNameTV, _defectNameTV,_defectTypeTV, _colorTV, _sizeTV, _sysChallanTV, _dtlsIDTV, _alterQuantityTV, _spotQuantityTV, _productionDateTV;
        RadioButton _checkBox;
        OnRectifiedHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnRectifiedHeadListener mOnRectifiedHeadListener) {
            super(itemView);
            _operationNameTV = itemView.findViewById(R.id.operationNameTV);
            _defectNameTV = itemView.findViewById(R.id.defectNameTV);
            _defectTypeTV = itemView.findViewById(R.id.defectTypeTV);
            _colorTV = itemView.findViewById(R.id.colorTV);
            _sizeTV = itemView.findViewById(R.id.sizeTV);
            _sysChallanTV = itemView.findViewById(R.id.sysChallanTV);
            _dtlsIDTV = itemView.findViewById(R.id.dtlsIDTV);
            _alterQuantityTV = itemView.findViewById(R.id.alterQuantityTV);
            _spotQuantityTV = itemView.findViewById(R.id.spotQuantityTV);
            _productionDateTV = itemView.findViewById(R.id.productionDateTV);
            _checkBox = itemView.findViewById(R.id.checkBox);

            this.onHeadListener = mOnRectifiedHeadListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHeadListener.onRectifiedHeadClick(getAdapterPosition(), v);
        }
    }

    public interface OnRectifiedHeadListener {
        void onRectifiedHeadClick(int position, View v);
    }
}
