package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalUnApprovalDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class V1_ApprovalDetailsRecyclerViewAdapter extends RecyclerView.Adapter<V1_ApprovalDetailsRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<V1_ApprovalUnApprovalDetailsModel> dtlsIndexArrayList = new ArrayList<>();
    private List<V1_ApprovalUnApprovalDetailsModel> filteredList = new ArrayList<>();

    private final OnHeadListener mOnHeadListener;
    private final OnDetailsListener mONDetailsListener;
    private final OnDenyHeadListener mOnDenyHeadListener;
    private final OnUnApproveHeadListener mOnUnApproveHeadListener;

    public V1_ApprovalDetailsRecyclerViewAdapter(Context context, List<V1_ApprovalUnApprovalDetailsModel> dtlsIndexArrayList, OnHeadListener mOnHeadListener, OnDetailsListener mONDetailsListener, OnDenyHeadListener onDenyHeadListener, OnUnApproveHeadListener onUnApproveHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList; // Copy the original list
        this.filteredList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
        this.mONDetailsListener = mONDetailsListener;
        this.mOnDenyHeadListener = onDenyHeadListener;
        this.mOnUnApproveHeadListener = onUnApproveHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_details, parent, false);
        return new ViewHolder(view, mOnHeadListener, mONDetailsListener, mOnDenyHeadListener, mOnUnApproveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        V1_ApprovalUnApprovalDetailsModel data = filteredList.get(position);

        holder._detailsTV.setText(String.valueOf(data.getDesc()));
        holder._reqNumberTextField.setText(data.getSysNumber());
        holder._dateTextview.setText(data.getDate());
        holder._dateTV.setText(data.getDate());

        int color = ContextCompat.getColor(context, R.color.blue_50);
        if(data.getMarked() != null && data.getMarked()){
//            holder._markedCardView.setCardBackgroundColor(color);
            holder._markedCardView.setBackgroundColor(Color.parseColor("#D7E3D7"));
            Log.d("TAG", "onBindViewHolder: GGGG");
        }

        if(data.getIsApproval()){
            holder._unApproveBtn.setVisibility(View.GONE);
            holder._approveBtn.setVisibility(View.VISIBLE);
            holder._denyBtn.setVisibility(View.VISIBLE);
        }else{
            holder._unApproveBtn.setVisibility(View.VISIBLE);
            holder._denyBtn.setVisibility(View.GONE);
            holder._approveBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _detailsTV, _dateTV, _reqNumberTextField, _approveBtn, _detailsBtn, _denyBtn, _unApproveBtn, _dateTextview;
        EditText _reasonET;
        LinearLayout _layout;
        CardView _markedCardView;
        OnHeadListener onHeadListener;
        OnDetailsListener onDetailsListener;
        OnDenyHeadListener onDenyHeadListener;
        OnUnApproveHeadListener onUnApproveHeadListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener mOnHeadListener, OnDetailsListener mOnDetailsListener, OnDenyHeadListener onDenyHeadListener, OnUnApproveHeadListener onUnApproveHeadListener) {
            super(itemView);
            _detailsTV = itemView.findViewById(R.id.detailsTV);
            _dateTV = itemView.findViewById(R.id.dateTV);
            _approveBtn = itemView.findViewById(R.id.approveBtn);
            _detailsBtn = itemView.findViewById(R.id.detailsBtn);
            _unApproveBtn = itemView.findViewById(R.id.unApproveBtn);
            _denyBtn = itemView.findViewById(R.id.denyBtn);
            _reqNumberTextField = itemView.findViewById(R.id.reqNumberTextField);
            _dateTextview = itemView.findViewById(R.id.dateTextview);
            _layout = itemView.findViewById(R.id.layout);
            _markedCardView = itemView.findViewById(R.id.markedCardView);
            _reasonET = itemView.findViewById(R.id.reasonET);

            this.onHeadListener = mOnHeadListener;
            _approveBtn.setOnClickListener(this);

            this.onDetailsListener = mOnDetailsListener;
            _detailsBtn.setOnClickListener(this);

            this.onDenyHeadListener = onDenyHeadListener;
            _denyBtn.setOnClickListener(this);

            this.onUnApproveHeadListener = onUnApproveHeadListener;
            _unApproveBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.approveBtn:
                    onHeadListener.onHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.detailsBtn:
                    onDetailsListener.onDetailsClick(getAdapterPosition(), v);
                    break;
                case R.id.denyBtn:
                    onDenyHeadListener.onDenyHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.unApproveBtn:
                    onUnApproveHeadListener.onUnApproveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }

    public interface OnDetailsListener {
        void onDetailsClick(int position, View v);
    }

    public interface OnDenyHeadListener {
        void onDenyHeadClick(int position, View v);
    }

    public interface OnUnApproveHeadListener {
        void onUnApproveHeadClick(int position, View v);
    }

    // Method to filter the list based on query
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(String query) {
        query = query.toLowerCase();
        filteredList = new ArrayList<>();

        Log.d("Filter", "Query: " + query+" "+dtlsIndexArrayList.size()); // Debugging

        if (query.isEmpty()) {
            filteredList.addAll(dtlsIndexArrayList);
        } else {
            for (V1_ApprovalUnApprovalDetailsModel item : dtlsIndexArrayList) {
                Log.d("Filter", "Checking item: " + item.getSysNumber()); // Debugging
                if (item.getSysNumber() != null && item.getSysNumber().toLowerCase().contains(query)) {
                    filteredList.add(item);
                    Log.d("Filter", "Matched item: " + item.getSysNumber()); // Debugging
                }
            }
        }

        notifyDataSetChanged();
        Log.d("Filter", "Filtered List Size: " + filteredList.size()); // Debugging
    }


}
