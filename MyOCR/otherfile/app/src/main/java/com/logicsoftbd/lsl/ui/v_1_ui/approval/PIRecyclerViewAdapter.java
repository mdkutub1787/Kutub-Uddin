package com.logicsoftbd.lsl.ui.v_1_ui.approval;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalModelClass;

import java.util.ArrayList;

public class PIRecyclerViewAdapter extends RecyclerView.Adapter<PIRecyclerViewAdapter.ViewHolder>{

    private ArrayList<V1_ApprovalModelClass> mHeadingClasses = new ArrayList<>();
    private OnHeadListener mOnHeadListener;

    public PIRecyclerViewAdapter(ArrayList<V1_ApprovalModelClass> headingClasses, OnHeadListener onHeadListener) {
        this.mHeadingClasses = headingClasses;
        this.mOnHeadListener = onHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_layout, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mHeadingClasses.get(position).getAppTitle().trim());
        holder.approvalTag.setText("  Approval");
    }

    @Override
    public int getItemCount() {
        return mHeadingClasses.size();
    }

    public class ViewHolder extends RecyclerView

            .ViewHolder implements View.OnClickListener{

        TextView title, approvalTag;

        OnHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener onHeadListener) {
            super(itemView);
            title = itemView.findViewById(R.id.titleName);
            approvalTag = itemView.findViewById(R.id.approvalTag);
            this.onHeadListener = onHeadListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHeadListener.onHeadClick(getAdapterPosition());

        }
    }

    public interface OnHeadListener{
        void onHeadClick(int position);
    }
}
