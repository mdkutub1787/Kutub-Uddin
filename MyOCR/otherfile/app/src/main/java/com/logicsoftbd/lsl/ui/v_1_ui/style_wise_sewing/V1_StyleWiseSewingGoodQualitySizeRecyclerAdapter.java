package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ColorWiseSizeItemModel;

import java.util.List;

public class V1_StyleWiseSewingGoodQualitySizeRecyclerAdapter extends RecyclerView.Adapter<V1_StyleWiseSewingGoodQualitySizeRecyclerAdapter.ViewHolder> {
        List<V1_ColorWiseSizeItemModel> sewingOutputSizeItemModels;
        Context context;
        private OnSizeSelectListener mOnImageHeadListener;
        private Integer selectedItemPosition = -1;

        public V1_StyleWiseSewingGoodQualitySizeRecyclerAdapter(List<V1_ColorWiseSizeItemModel> sewingOutputSizeItemModels, Context context, OnSizeSelectListener mOnDefectSelectListener) {
                this.sewingOutputSizeItemModels = sewingOutputSizeItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_size_item_good_quality, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                viewHolder.sizeNameTV.setText(sewingOutputSizeItemModels.get(position).getSizeName());

                if(sewingOutputSizeItemModels.get(position).getSelectedItem()){
                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
                }else{
                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#324F5C"));
                }
//                if(selectedItemPosition == position)
//                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
//                else
//                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#324F5C"));
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return sewingOutputSizeItemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView sizeNameTV;
                private CardView sizeCardView;
                OnSizeSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSizeSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        sizeNameTV = itemView.findViewById(R.id.sizeNameTV);
                        sizeCardView = itemView.findViewById(R.id.sizeCardView);

                        this.onDefectSelectListener = mOnDefectSelectListener;
                        sizeNameTV.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.sizeNameTV:
                                        selectedItemPosition = getAdapterPosition();
                                        notifyDataSetChanged();
                                        onDefectSelectListener.onSizeHeadClick(selectedItemPosition, v);
                                        break;
                        }
                }
        }

        public interface OnSizeSelectListener {
                void onSizeHeadClick(int position, View v);
        }
}
