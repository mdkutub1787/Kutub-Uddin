package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectedRectifiedModel_V4;

import java.util.List;

public class V1_DefectedRectifiedRecyclerViewAdapter_v4 extends RecyclerView.Adapter<V1_DefectedRectifiedRecyclerViewAdapter_v4.ViewHolder> {
    private Context context;
    private List<V1_DefectedRectifiedModel_V4> dtlsIndexArrayList;
    private OnGmtsRectifiedHeadListener mOnHeadListener;

    public V1_DefectedRectifiedRecyclerViewAdapter_v4(Context context, List<V1_DefectedRectifiedModel_V4> dtlsIndexArrayList, OnGmtsRectifiedHeadListener mOnHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defect_wise_rectified_item_v4, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._poNumberTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getMstId()));
        holder._colorTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getColorName()));
        holder._sizeTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getSizeName()));
        holder._alterTV.setText(dtlsIndexArrayList.get(position).getAlterQty());
        holder._spotTV.setText(dtlsIndexArrayList.get(position).getSpotQty());
        holder._rectifiedTV.setText(dtlsIndexArrayList.get(position).getRectifiedQty());
        holder.defectCount.setText(dtlsIndexArrayList.get(position).getDefectCount());

        if(dtlsIndexArrayList.get(position).getSelect()){
            holder._checkBox.setChecked(true);
            holder._poNumberTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._poNumberTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._colorTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._colorTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._sizeTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._sizeTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._alterTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._alterTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._spotTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._spotTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._rectifiedTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._rectifiedTV.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            holder._checkBox.setChecked(false);
            holder._poNumberTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._poNumberTV.setTextColor(Color.parseColor("#000000"));

            holder._colorTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._colorTV.setTextColor(Color.parseColor("#000000"));

            holder._sizeTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._sizeTV.setTextColor(Color.parseColor("#000000"));

            holder._alterTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._alterTV.setTextColor(Color.parseColor("#000000"));

            holder._spotTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._spotTV.setTextColor(Color.parseColor("#000000"));

            holder._rectifiedTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._rectifiedTV.setTextColor(Color.parseColor("#000000"));
        }

        buttonClickListener(holder, position);
    }

    private void buttonClickListener(ViewHolder holder, int position) {
        holder.defectUp.setOnClickListener(v -> {
            dtlsIndexArrayList.get(position).setSelect(true);
            String value = holder.defectCount.getText().toString();
            if(value.isEmpty() && value.equals("")){
                holder.defectCount.setText("1");
                value = "1";
            }else{
                value = String.valueOf(Integer.parseInt(value) + 1);
                holder.defectCount.setText(value);
            }

            int alterSpot = Integer.parseInt(dtlsIndexArrayList.get(position).getAlterQty()) +  Integer.parseInt(dtlsIndexArrayList.get(position).getSpotQty());
            int rectified = Integer.parseInt(dtlsIndexArrayList.get(position).getRectifiedQty());

            int diff = alterSpot - rectified;

            if(Integer.parseInt(value) <= diff ){
                dtlsIndexArrayList.get(position).setDefectCount(holder.defectCount.getText().toString());
                Log.d("TAG", "buttonClickListener: +++"+(alterSpot-rectified)+ value);
            }else {
                showAlertMessage("Balance quantity is not available.", 0);
                value = String.valueOf(Integer.parseInt(value) - 1);
                holder.defectCount.setText(value);
                holder.defectUp.setEnabled(false);
            }

            notifyDataSetChanged();
        });

        holder.defectDown.setOnClickListener(v -> {
            String value = holder.defectCount.getText().toString();
            dtlsIndexArrayList.get(position).setSelect(true);
            if(value.isEmpty()){
                holder.defectCount.setText("0");
            }else{
                value = String.valueOf(Integer.parseInt(value) - 1);
                holder.defectCount.setText(value);
                if(Integer.parseInt(value) <= 0){
                    holder.defectCount.setText(String.valueOf(0));
                }
                holder.defectUp.setEnabled(true);
            }
            dtlsIndexArrayList.get(position).setDefectCount(holder.defectCount.getText().toString());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _poNumberTV, _colorTV, _sizeTV, _alterTV, _spotTV, _rectifiedTV, defectCount;
        private ImageButton defectUp, defectDown;
        RadioButton _checkBox;
        OnGmtsRectifiedHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnGmtsRectifiedHeadListener mOnRectifiedHeadListener) {
            super(itemView);
            _poNumberTV = itemView.findViewById(R.id.poNumberTV);
            _colorTV = itemView.findViewById(R.id.colorTV);
            _sizeTV = itemView.findViewById(R.id.sizeTV);
            _alterTV = itemView.findViewById(R.id.alterTV);
            _spotTV = itemView.findViewById(R.id.spotTV);
            _rectifiedTV = itemView.findViewById(R.id.rectifiedTV);
            _checkBox = itemView.findViewById(R.id.checkBox);

            defectCount = itemView.findViewById(R.id.defectCountET);

            defectUp = itemView.findViewById(R.id.defectUp);
            defectDown = itemView.findViewById(R.id.defectDown);

            this.onHeadListener = mOnRectifiedHeadListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHeadListener.onGmtsRectifiedHeadClick(getAdapterPosition(), v);
        }
    }

    public interface OnGmtsRectifiedHeadListener {
        void onGmtsRectifiedHeadClick(int position, View v);
    }

    private void showAlertMessage(String msg, Integer i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }
}
