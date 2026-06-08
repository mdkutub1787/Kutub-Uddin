package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V2_POColorWiseSizeModel;

import java.util.List;

public class V2_StyleWiseSewingSizeRecyclerAdapter extends RecyclerView.Adapter<V2_StyleWiseSewingSizeRecyclerAdapter.ViewHolder> {
        List<V2_POColorWiseSizeModel> sewingOutputSizeItemModels;
        Context context;
        private OnSizeSelectListener mOnImageHeadListener;
        private Integer selectedItemPosition = -1;

        public V2_StyleWiseSewingSizeRecyclerAdapter(List<V2_POColorWiseSizeModel> sewingOutputSizeItemModels, Context context, OnSizeSelectListener mOnDefectSelectListener) {
                this.sewingOutputSizeItemModels = sewingOutputSizeItemModels;
                this.context = context;
                this.mOnImageHeadListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_size_item, parent, false);
                return new ViewHolder(view, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                viewHolder.sizeNameTV.setText(sewingOutputSizeItemModels.get(position).getSizeName());
                viewHolder.sizeBadgeTV.setText(sewingOutputSizeItemModels.get(position).getBadgeQuantity());
                if(sewingOutputSizeItemModels.get(position).getBadgeQuantity().equals("0")){
                        viewHolder.sizeBadgeTV.setBackgroundResource(R.drawable.item_count_nill);
                }else {
                        viewHolder.sizeBadgeTV.setBackgroundResource(R.drawable.item_count);
                        setTextViewGradientColor(viewHolder.sizeBadgeTV);
                }
                if(sewingOutputSizeItemModels.get(position).getSelectedItem()){
                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
                }else{
                        viewHolder.sizeNameTV.setBackgroundColor(Color.parseColor("#324F5C"));
                }
        }

        private void setTextViewGradientColor(TextView textView) {
                Shader shader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                        new int[]{Color.BLUE,  Color.parseColor("#810366")},
                        null, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);

                textView.getPaint().setShader(shader);
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
                private TextView sizeNameTV, sizeBadgeTV;
                private LinearLayout sizeLayout;
                OnSizeSelectListener onDefectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSizeSelectListener mOnDefectSelectListener) {
                        super(itemView);
                        sizeNameTV = itemView.findViewById(R.id.sizeNameTV);
                        sizeBadgeTV = itemView.findViewById(R.id.sizeBadgeTV);
                        sizeLayout = itemView.findViewById(R.id.sizeLayout);

                        this.onDefectSelectListener = mOnDefectSelectListener;
                        itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                default:
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
