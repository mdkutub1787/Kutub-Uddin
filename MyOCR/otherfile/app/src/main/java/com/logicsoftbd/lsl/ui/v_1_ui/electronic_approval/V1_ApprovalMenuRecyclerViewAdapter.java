package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalNotificationsModel;

import java.util.ArrayList;
import java.util.List;

public class V1_ApprovalMenuRecyclerViewAdapter extends RecyclerView.Adapter<V1_ApprovalMenuRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<V1_ApprovalNotificationsModel.Datum> dtlsIndexArrayList = new ArrayList<>();
    private OnHeadListener mOnHeadListener;
    private OnHeadLongClickListener mOnHeadLongListener;

    public V1_ApprovalMenuRecyclerViewAdapter(Context context, List<V1_ApprovalNotificationsModel.Datum> dtlsIndexArrayList, OnHeadListener mOnHeadListener, OnHeadLongClickListener mOnHeadLongListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
        this.mOnHeadLongListener = mOnHeadLongListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_menu, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._menuNameTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getMenu()));
        holder._countTV.setText(String.valueOf(dtlsIndexArrayList.get(position).getNotifications()));
        holder._layout.setBackgroundColor(Color.parseColor("#037EBE"));
        if(dtlsIndexArrayList.get(position).getNotifications().equals("0")){
            holder._layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder._countTV.setVisibility(View.GONE);
        }else{
            holder._layout.setBackgroundColor(Color.parseColor("#76D7EA"));
            holder._countTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        TextView _menuNameTV, _countTV;
        LinearLayout _layout;
        OnHeadListener onHeadListener;
        OnHeadLongClickListener onHeadLongListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener mOnHeadListener) {
            super(itemView);
            _menuNameTV = itemView.findViewById(R.id.menuNameTV);
            _countTV = itemView.findViewById(R.id.countTV);
            _layout = itemView.findViewById(R.id.card_view);

            this.onHeadListener = mOnHeadListener;
            itemView.setOnClickListener(this);

            this.onHeadLongListener = mOnHeadLongListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHeadListener.onHeadClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            onHeadLongListener.onHeadLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }

    public interface OnHeadLongClickListener {
        void onHeadLongClick(int position, View v);
    }


}
