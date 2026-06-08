package com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;

import java.util.List;

public class V1_HangerArchiveRecyclerAdapter extends RecyclerView.Adapter<V1_HangerArchiveRecyclerAdapter.ViewHolder> {
        List<HangerArchiveModel> hangerArchiveModels;
        Context context;
        private OnDeleteSelectListener mOnImageDeleteListener;
        private OnHeadSelectListener mOnImageHeadListener;
        private Integer selectedItemPosition = -1;

        public V1_HangerArchiveRecyclerAdapter(List<HangerArchiveModel> hangerArchiveModels, Context context, OnDeleteSelectListener mOnDefectSelectListener, OnHeadSelectListener mOnImageHeadListener) {
                this.hangerArchiveModels = hangerArchiveModels;
                this.context = context;
                this.mOnImageDeleteListener = mOnDefectSelectListener;
                this.mOnImageHeadListener = mOnImageHeadListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hanger_fabric_layout, parent, false);
                return new ViewHolder(view, mOnImageDeleteListener, mOnImageHeadListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
                viewHolder.garmentsItemTV.setText("Dispo No: "+hangerArchiveModels.get(position).getGarmentItem());
                viewHolder.dateTimeTV.setText(hangerArchiveModels.get(position).getDateTime());
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return hangerArchiveModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView garmentsItemTV, dateTimeTV, serialTV;
                private ImageView deleteBtn, qrCodeClick;
                OnDeleteSelectListener onDeleteSelectListener;
                OnHeadSelectListener onHeadSelectListener;

                public ViewHolder(@NonNull View itemView, OnDeleteSelectListener onDeleteListener, OnHeadSelectListener onHeadSelectListener) {
                        super(itemView);
                        garmentsItemTV = itemView.findViewById(R.id.garmentsItemTV);
                        dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
                        deleteBtn = itemView.findViewById(R.id.deleteBtn);
                        qrCodeClick = itemView.findViewById(R.id.qrCodeClick);
                        serialTV = itemView.findViewById(R.id.serialTV);

                        this.onDeleteSelectListener = onDeleteListener;
                        deleteBtn.setOnClickListener(this);

                        this.onHeadSelectListener = onHeadSelectListener;
                        qrCodeClick.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.deleteBtn:
                                        selectedItemPosition = getAdapterPosition();
                                        notifyDataSetChanged();
                                        onDeleteSelectListener.onDeleteClick(selectedItemPosition, v);
                                        break;
                                case R.id.qrCodeClick:
                                        selectedItemPosition = getAdapterPosition();
                                        onHeadSelectListener.onHeadClick(selectedItemPosition, v);
                                        break;
                        }
                }
        }

        public interface OnDeleteSelectListener {
                void onDeleteClick(int position, View v);
        }

        public interface OnHeadSelectListener {
                void onHeadClick(int position, View v);
        }
}
