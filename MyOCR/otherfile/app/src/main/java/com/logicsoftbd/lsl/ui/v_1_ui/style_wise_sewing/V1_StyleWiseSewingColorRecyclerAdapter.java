package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import static com.logicsoftbd.lsl.R.drawable.color_gradient_background;

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
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;

import java.util.List;

public class V1_StyleWiseSewingColorRecyclerAdapter extends RecyclerView.Adapter<V1_StyleWiseSewingColorRecyclerAdapter.ViewHolder> {
        List<V1_StyleWiseOperationResponse.Color> sewingOutputColorItemModels;
        Context context;
        private OnColorSelectListener mOnImageHeadListener;
        private Integer selectedItemPosition = -1;

        public V1_StyleWiseSewingColorRecyclerAdapter(List<V1_StyleWiseOperationResponse.Color> sewingOutputColorItemModels, Context context, OnColorSelectListener mOnDefectSelectListener) {
                this.sewingOutputColorItemModels = sewingOutputColorItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_color_item, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                viewHolder.colorNameTV.setText(sewingOutputColorItemModels.get(position).getColourName());
                viewHolder.colorBadgeTV.setText(sewingOutputColorItemModels.get(position).getColor_output_qnty());

                if(selectedItemPosition == position){
                        viewHolder.colorNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
                        viewHolder.colorNameTV.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else {
//                        viewHolder.colorNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
                        viewHolder.colorNameTV.setBackgroundResource(color_gradient_background);
//                        viewHolder.colorCard.setBackgroundColor(R.drawable.color_gradient_background);
                        viewHolder.colorNameTV.setTextColor(Color.parseColor("#000000"));
                }
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return sewingOutputColorItemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView colorNameTV, colorBadgeTV;
                private CardView colorCard;
                OnColorSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnColorSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        colorNameTV = itemView.findViewById(R.id.colorNameTV);
                        colorCard = itemView.findViewById(R.id.colorCard);
                        colorBadgeTV = itemView.findViewById(R.id.colorBadgeTV);
                        this.onDefectSelectListener = mOnDefectSelectListener;
                        colorNameTV.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.colorNameTV:
                                        selectedItemPosition = getAdapterPosition();
                                        notifyDataSetChanged();
                                        onDefectSelectListener.onColorHeadClick(selectedItemPosition, v);
                                        break;
                        }
                }
        }

        public interface OnColorSelectListener {
                void onColorHeadClick(int position, View v);
        }
}
