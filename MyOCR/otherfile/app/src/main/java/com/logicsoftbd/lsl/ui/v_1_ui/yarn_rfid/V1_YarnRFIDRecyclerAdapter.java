package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnRFIDModel;

import java.util.List;

public class V1_YarnRFIDRecyclerAdapter extends RecyclerView.Adapter<V1_YarnRFIDRecyclerAdapter.ViewHolder> {
        List<V1_YarnRFIDModel> yarnRFIDModels;
        Context context;
        private OnDeleteSelectListener mOnImageDeleteListener;
        private Integer selectedItemPosition = -1;

        public V1_YarnRFIDRecyclerAdapter(List<V1_YarnRFIDModel> yarnRFIDModels, Context context, OnDeleteSelectListener mOnDefectSelectListener) {
                this.yarnRFIDModels = yarnRFIDModels;
                this.context = context;
                this.mOnImageDeleteListener = mOnDefectSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yarn_rfid_layout, parent, false);
                return new ViewHolder(view, mOnImageDeleteListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
                viewHolder.rfidItemTV.setText(yarnRFIDModels.get(position).getRfidItem());
                viewHolder.dateTimeTV.setText(yarnRFIDModels.get(position).getDateTime());
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return yarnRFIDModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private TextView rfidItemTV, dateTimeTV, serialTV;
                private ImageView deleteBtn, qrCodeClick;
                OnDeleteSelectListener onDeleteSelectListener;

                public ViewHolder(@NonNull View itemView, OnDeleteSelectListener onDeleteListener) {
                        super(itemView);
                        rfidItemTV = itemView.findViewById(R.id.rfidItemTV);
                        dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
                        deleteBtn = itemView.findViewById(R.id.deleteBtn);
                        serialTV = itemView.findViewById(R.id.serialTV);

                        this.onDeleteSelectListener = onDeleteListener;
                        deleteBtn.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        switch (v.getId()){
                                case R.id.deleteBtn:
                                        selectedItemPosition = getAdapterPosition();
                                        notifyDataSetChanged();
                                        onDeleteSelectListener.onDeleteClick(selectedItemPosition, v);
                                        break;
                        }
                }
        }

        public interface OnDeleteSelectListener {
                void onDeleteClick(int position, View v);
        }
}
