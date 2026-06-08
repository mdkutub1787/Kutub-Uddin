package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;



import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_IssueReturnRFIDModel;

import java.util.ArrayList;
import java.util.List;

public class V1_YarnRFIDRecyclerViewAdapter extends RecyclerView.Adapter<V1_YarnRFIDRecyclerViewAdapter.ViewHolder> {
    private List<V1_IssueReturnRFIDModel> detailsListView;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;
    public V1_YarnRFIDRecyclerViewAdapter(List<V1_IssueReturnRFIDModel> detailsListView, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.detailsListView = detailsListView;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_YarnRFIDRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfid_issue_return_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.serialTV.setText(String.valueOf(position+1));
        viewHolder.rfidNoTV.setText(detailsListView.get(position).getRfid());
        viewHolder.weightTV.setText(detailsListView.get(position).getWeight());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(detailsListView != null){
            return  detailsListView.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView serialTV, rfidNoTV, weightTV;
        private Spinner fabSpinner;
        private ImageButton removeBT, printBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            serialTV = itemView.findViewById(R.id.serialTV);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
            weightTV = itemView.findViewById(R.id.weightTV);


            removeBT = itemView.findViewById(R.id.removeBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
}
