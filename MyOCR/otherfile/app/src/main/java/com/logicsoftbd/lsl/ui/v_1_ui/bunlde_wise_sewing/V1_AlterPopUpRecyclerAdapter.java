package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel;

import java.util.List;

public class V1_AlterPopUpRecyclerAdapter extends RecyclerView.Adapter<V1_AlterPopUpRecyclerAdapter.ViewHolder> {
        List<V1_SewingAlterModel> sewingDefectTypeArrayList;
        Context context;
        private OnDefectSelectListener mOnImageHeadListener;

        public V1_AlterPopUpRecyclerAdapter(List<V1_SewingAlterModel> sewingDefectTypeArrayList, Context context, OnDefectSelectListener mOnDefectSelectListener) {
                this.sewingDefectTypeArrayList = sewingDefectTypeArrayList;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public V1_AlterPopUpRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_pop_up_layout, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull V1_AlterPopUpRecyclerAdapter.ViewHolder viewHolder, int position) {
                final int adapterPosition = position;
//                viewHolder.slNumber.setText(String.valueOf(adapterPosition+1));
                viewHolder.defectName.setText(sewingDefectTypeArrayList.get(position).getDefectName());
                viewHolder.defectCount.setText(sewingDefectTypeArrayList.get(position).getDefectCount());
//                if(sewingDefectTypeArrayList.get(position).getDefectSelect()){
//                        viewHolder.selectImage.setImageResource(R.drawable.ic_baseline_touch_app_24_red);
//                }else{
//                        viewHolder.selectImage.setImageResource(R.drawable.ic_baseline_touch_app_24);
//                }
                viewHolder.defectCount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                sewingDefectTypeArrayList.get(adapterPosition).setDefectCount(String.valueOf(s));
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                });
//                viewHolder.selectImage.setOnClickListener(v -> {
//                        viewHolder.selectImage.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_touch_app_24_red));
//                });
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
                });

                holder.defectDown.setOnClickListener(v -> {
                        String value = holder.defectCount.getText().toString();
                        if(value.isEmpty()){
                                holder.defectCount.setText("1");
                        }else{
                                holder.defectCount.setText(String.valueOf(Integer.parseInt(value)-1));
                                if(Integer.parseInt(value) <= 0){
                                        holder.defectCount.setText(String.valueOf(0));
                                }
                        }
                        sewingDefectTypeArrayList.get(position).setDefectCount(holder.defectCount.getText().toString());
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
                private TextView slNumber, defectName, defectCount;
                private ImageView selectImage;
                private ImageButton defectUp, defectDown;
                OnDefectSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnDefectSelectListener mOnDefectSelectListener) {
                        super(itemView);

                        slNumber = itemView.findViewById(R.id.slTV);
                        defectName = itemView.findViewById(R.id.defectNameTV);
                        defectCount = itemView.findViewById(R.id.defectCountET);
                        selectImage = itemView.findViewById(R.id.selectImage);

                        defectUp = itemView.findViewById(R.id.defectUp);
                        defectDown = itemView.findViewById(R.id.defectDown);

//                        this.onDefectSelectListener = mOnDefectSelectListener;
//                        selectImage.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.selectImage:
                                        selectImage.setImageResource(R.drawable.ic_baseline_touch_app_24_red);
                                        onDefectSelectListener.onAlterDefectHeadClick(getAdapterPosition(), v);
                                        break;
                        }
                }
        }

        public interface OnDefectSelectListener {
                void onAlterDefectHeadClick(int position, View v);
        }
}
