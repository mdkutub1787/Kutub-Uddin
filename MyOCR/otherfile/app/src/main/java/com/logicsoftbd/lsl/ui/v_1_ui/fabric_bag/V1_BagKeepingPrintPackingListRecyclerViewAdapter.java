package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagPrintResponse;

import java.util.List;
public class V1_BagKeepingPrintPackingListRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagKeepingPrintPackingListRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagPrintResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemovePackingHeadListener mOnRemovePackingHeadListener;
    private OnPrintPackingHeadListener mOnPrintPackingHeadListener;
    private OnPrintPackingDetailsHeadListener mOnPrintPackingDetailsHeadListener;

    public V1_BagKeepingPrintPackingListRecyclerViewAdapter(List<V1_BagPrintResponse.ResultSet> bagKeepingResponses, OnRemovePackingHeadListener mOnRemovePackingHeadListener, OnPrintPackingHeadListener mOnPrintPackingHeadListener, OnPrintPackingDetailsHeadListener mOnPrintPackingDetailsHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemovePackingHeadListener = mOnRemovePackingHeadListener;
        this.mOnPrintPackingHeadListener = mOnPrintPackingHeadListener;
        this.mOnPrintPackingDetailsHeadListener = mOnPrintPackingDetailsHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagKeepingPrintPackingListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_keeping_packing_list_layout, parent, false);
        return new ViewHolder(view, mOnRemovePackingHeadListener, mOnPrintPackingHeadListener, mOnPrintPackingDetailsHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.packingListTV.setText(bagKeepingResponses.get(position).getPackingList());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bagKeepingResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView packingListTV;

        private ImageButton removeBT, printBT;
        private LinearLayout ly;
        private OnRemovePackingHeadListener onRemovePackingHeadListener;
        private OnPrintPackingHeadListener onPrintPackingHeadListener;
        private OnPrintPackingDetailsHeadListener onPrintPackingDetailsHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemovePackingHeadListener mOnRemovePackingHeadListener, OnPrintPackingHeadListener mOnPrintPackingHeadListener, OnPrintPackingDetailsHeadListener mOnPrintPackingDetailsHeadListener) {
            super(itemView);
            packingListTV = itemView.findViewById(R.id.packingListTV);
            ly = itemView.findViewById(R.id.ly);

            removeBT = itemView.findViewById(R.id.removeBT);
            printBT = itemView.findViewById(R.id.printBT);

            this.onRemovePackingHeadListener = mOnRemovePackingHeadListener;
            removeBT.setOnClickListener(this);

            this.onPrintPackingHeadListener = mOnPrintPackingHeadListener;
            printBT.setOnClickListener(this);

            this.onPrintPackingDetailsHeadListener = mOnPrintPackingDetailsHeadListener;
            packingListTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemovePackingHeadListener.onRemovePackingHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.printBT:
                    onPrintPackingHeadListener.onPrintPackingHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.packingListTV:
                    onPrintPackingDetailsHeadListener.onPrintPackingDetailsHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemovePackingHeadListener {
        void onRemovePackingHeadClick(int position, View v);
    }
    public interface OnPrintPackingHeadListener {
        void onPrintPackingHeadClick(int position, View v);
    }

    public interface OnPrintPackingDetailsHeadListener {
        void onPrintPackingDetailsHeadClick(int position, View v);
    }
}
