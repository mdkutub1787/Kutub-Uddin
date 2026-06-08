package com.logicsoftbd.lsl.ui.v_1_ui.device_session;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ActiveDeviceModel;

import java.util.ArrayList;
import java.util.List;

public class V1_ActiveDeviceStatusRecyclerViewAdapter extends RecyclerView.Adapter<V1_ActiveDeviceStatusRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<V1_ActiveDeviceModel.Resultset> dtlsIndexArrayList = new ArrayList<>();
    private OnHeadListener mOnHeadListener;
    private Integer selectedItemPosition = -1;

    public V1_ActiveDeviceStatusRecyclerViewAdapter(Context context, List<V1_ActiveDeviceModel.Resultset> dtlsIndexArrayList, OnHeadListener mOnHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_device_item, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._floorNameTV.setText(dtlsIndexArrayList.get(position).getFloorName());
        holder._lineNameTV.setText(dtlsIndexArrayList.get(position).getLineNumber());
        holder._deviceIDTV.setText(dtlsIndexArrayList.get(position).getDeviceId());
        holder._itemNameTV.setText(dtlsIndexArrayList.get(position).getInternalRef());
        holder._jobNoTV.setText(dtlsIndexArrayList.get(position).getJobNo());
        holder._userNameTV.setText(dtlsIndexArrayList.get(position).getUSER_NAME());

        if(selectedItemPosition == position){
            holder._floorNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._floorNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._lineNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._lineNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._deviceIDTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._deviceIDTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._itemNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._itemNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._jobNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._jobNoTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._userNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._userNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._removeBtn.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        else{
            holder._floorNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._floorNameTV.setTextColor(Color.parseColor("#000000"));

            holder._lineNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._lineNameTV.setTextColor(Color.parseColor("#000000"));

            holder._deviceIDTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._deviceIDTV.setTextColor(Color.parseColor("#000000"));

            holder._itemNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._itemNameTV.setTextColor(Color.parseColor("#000000"));

            holder._jobNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._jobNoTV.setTextColor(Color.parseColor("#000000"));

            holder._userNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._userNameTV.setTextColor(Color.parseColor("#000000"));

            holder._removeBtn.setBackgroundColor(Color.parseColor("#FF9800"));
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _floorNameTV, _lineNameTV, _deviceIDTV, _itemNameTV, _jobNoTV, _userNameTV;
        Button _removeBtn;
        OnHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener mOnRectifiedHeadListener) {
            super(itemView);
            _floorNameTV = itemView.findViewById(R.id.floorNameTV);
            _lineNameTV = itemView.findViewById(R.id.lineNameTV);
            _deviceIDTV = itemView.findViewById(R.id.deviceIDTV);
            _itemNameTV = itemView.findViewById(R.id.itemNameTV);
            _jobNoTV = itemView.findViewById(R.id.jobNoTV);
            _userNameTV = itemView.findViewById(R.id.userNameTV);
            _removeBtn = itemView.findViewById(R.id.removeBtn);

            this.onHeadListener = mOnRectifiedHeadListener;
            _removeBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selectedItemPosition = getAdapterPosition();
            onHeadListener.onHeadClick(getAdapterPosition(), v);
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }
}
