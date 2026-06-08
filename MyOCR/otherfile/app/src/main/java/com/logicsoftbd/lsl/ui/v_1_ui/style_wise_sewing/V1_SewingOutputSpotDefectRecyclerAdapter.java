package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel;

import java.util.List;

public class V1_SewingOutputSpotDefectRecyclerAdapter extends RecyclerView.Adapter<V1_SewingOutputSpotDefectRecyclerAdapter.ViewHolder> {
        List<V1_SewingSpotModel> sewingDefectTypeArrayList;
        Context context;
        private OnSpotDefectSelectListener mOnImageHeadListener;

        public V1_SewingOutputSpotDefectRecyclerAdapter(List<V1_SewingSpotModel> sewingDefectTypeArrayList, Context context, OnSpotDefectSelectListener mOnDefectSelectListener) {
                this.sewingDefectTypeArrayList = sewingDefectTypeArrayList;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_pop_up_layout, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                final int adapterPosition = position;
                viewHolder.defectSerialNoTV.setText(sewingDefectTypeArrayList.get(position).getDefectSerialNo());
                viewHolder.defectName.setText(sewingDefectTypeArrayList.get(position).getDefectName());
                viewHolder.defectCount.setText(sewingDefectTypeArrayList.get(position).getDefectCount());
                if(Integer.parseInt(sewingDefectTypeArrayList.get(position).getDefectCount()) <= 0 || viewHolder.defectCount.getText().toString().equals("")){
                        viewHolder.defectName.setBackgroundColor(Color.parseColor("#E5F0FF"));
                } else {
                        viewHolder.defectName.setBackgroundColor(Color.parseColor("#FF861F"));
                }
                if(sewingDefectTypeArrayList.get(position).getDefectCount().equals("0")){
                        viewHolder.defectCount.setText("");
                }
                buttonClickListener(viewHolder, position);
        }

        private void buttonClickListener(final ViewHolder holder, final int position) {
                holder.defectUp.setOnClickListener(v -> {
                        String value = holder.defectCount.getText().toString();
                        if(value.isEmpty()){
                                holder.defectCount.setText("1");
                        }else{
                                holder.defectCount.setText(String.valueOf(Integer.parseInt(value)+1));
                        }
                        sewingDefectTypeArrayList.get(position).setDefectCount(holder.defectCount.getText().toString());
                        notifyDataSetChanged();
                });

                holder.defectDown.setOnClickListener(v -> {
                        String value = holder.defectCount.getText().toString();
                        if(value.isEmpty()){
                                holder.defectCount.setText("0");
                        }else{
                                holder.defectCount.setText(String.valueOf(Integer.parseInt(value)-1));
                                if(Integer.parseInt(value) <= 0){
                                        holder.defectCount.setText(String.valueOf(0));
                                }
                        }
                        sewingDefectTypeArrayList.get(position).setDefectCount(holder.defectCount.getText().toString());
                        notifyDataSetChanged();
                });
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return sewingDefectTypeArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView  defectName, defectCount, defectSerialNoTV;
                private ImageView selectImage;
                private ImageButton defectUp, defectDown;
                OnSpotDefectSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSpotDefectSelectListener mOnDefectSelectListener) {
                        super(itemView);

                        defectName = itemView.findViewById(R.id.defectNameTV);
                        defectCount = itemView.findViewById(R.id.defectCountET);
                        selectImage = itemView.findViewById(R.id.selectImage);
                        defectSerialNoTV = itemView.findViewById(R.id.defectSerialNoTV);

                        defectUp = itemView.findViewById(R.id.defectUp);
                        defectDown = itemView.findViewById(R.id.defectDown);

                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.selectImage:
                                        selectImage.setImageResource(R.drawable.ic_baseline_touch_app_24_red);
                                        onDefectSelectListener.onSpotDefectHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnSpotDefectSelectListener {
                void onSpotDefectHeadClick(int position, View v);
        }
}
