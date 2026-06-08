package com.logicsoftbd.lsl.ui.v_1_ui.cutting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreReceiveModel;

import java.util.ArrayList;

public class V1_StoreRollReceiveRecyclerViewAdapter extends RecyclerView.Adapter<V1_StoreRollReceiveRecyclerViewAdapter.ViewHolder> {
    ArrayList<V1_CuttingStoreReceiveModel> cuttingStoreReceiveModelArrayList = new ArrayList<>();
    Context context;
    private OnMoreHeadListener mOnMoreHeadListener;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_StoreRollReceiveRecyclerViewAdapter(ArrayList<V1_CuttingStoreReceiveModel> cuttingStoreReceiveModelArrayList, OnMoreHeadListener mOnMoreHeadListener, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.cuttingStoreReceiveModelArrayList = cuttingStoreReceiveModelArrayList;
        this.mOnMoreHeadListener = mOnMoreHeadListener;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_StoreRollReceiveRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutting_store_receive_layout, parent, false);
        return new ViewHolder(view, mOnMoreHeadListener, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.slTV.setText(String.valueOf(position+1));
        viewHolder.bundleNoTV.setText(cuttingStoreReceiveModelArrayList.get(position).getBundleNo());
        viewHolder.qntyPcsTV.setText(cuttingStoreReceiveModelArrayList.get(position).getQcPassQnty());
        viewHolder.poTV.setText(cuttingStoreReceiveModelArrayList.get(position).getPoNumber());
        viewHolder.buyerTV.setText(cuttingStoreReceiveModelArrayList.get(position).getBuyerName());
        viewHolder.floorTV.setText(cuttingStoreReceiveModelArrayList.get(position).getCuttingFloorName());
        viewHolder.colorTV.setText(cuttingStoreReceiveModelArrayList.get(position).getColorName());
        viewHolder.sizeTV.setText(cuttingStoreReceiveModelArrayList.get(position).getSizeName());

        if(cuttingStoreReceiveModelArrayList.get(position).getStatus() == true){
            viewHolder.slF.setVisibility(View.VISIBLE);
            viewHolder.bundleNoF.setVisibility(View.VISIBLE);
            viewHolder.qntyPcsF.setVisibility(View.VISIBLE);
            viewHolder.poF.setVisibility(View.VISIBLE);
            viewHolder.buyerF.setVisibility(View.VISIBLE);
            viewHolder.floorF.setVisibility(View.VISIBLE);
            viewHolder.colorF.setVisibility(View.VISIBLE);
            viewHolder.sizeF.setVisibility(View.VISIBLE);
            viewHolder.removeF.setVisibility(View.VISIBLE);
            viewHolder.moreF.setVisibility(View.VISIBLE);
            viewHolder.moreBT.setBackground( context.getResources().getDrawable(R.color.navy_blue_dark));
        }else{
            viewHolder.slF.setVisibility(View.GONE);
            viewHolder.bundleNoF.setVisibility(View.GONE);
            viewHolder.qntyPcsF.setVisibility(View.GONE);
            viewHolder.poF.setVisibility(View.GONE);
            viewHolder.buyerF.setVisibility(View.GONE);
            viewHolder.floorF.setVisibility(View.GONE);
            viewHolder.colorF.setVisibility(View.GONE);
            viewHolder.sizeF.setVisibility(View.GONE);
            viewHolder.removeF.setVisibility(View.GONE);
            viewHolder.moreF.setVisibility(View.GONE);
            viewHolder.moreBT.setBackground( context.getResources().getDrawable(R.color.violet_color_picker));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cuttingStoreReceiveModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView slTV, bundleNoTV, qntyPcsTV, poTV, buyerTV, floorTV, colorTV, sizeTV,
                slF, bundleNoF, qntyPcsF, poF, buyerF, floorF, colorF, sizeF, removeF, moreF;
        private ImageButton removeBT, moreBT;
        private OnMoreHeadListener onMoreHeadListener;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnMoreHeadListener mOnMoreHeadListener, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            slF = itemView.findViewById(R.id.slF);
            bundleNoF = itemView.findViewById(R.id.bundleNoF);
            qntyPcsF = itemView.findViewById(R.id.qntyPcsF);
            poF = itemView.findViewById(R.id.poF);
            buyerF = itemView.findViewById(R.id.buyerF);
            floorF = itemView.findViewById(R.id.floorF);
            colorF = itemView.findViewById(R.id.colorF);
            sizeF = itemView.findViewById(R.id.sizeF);
            moreF = itemView.findViewById(R.id.moreF);
            removeF = itemView.findViewById(R.id.removeF);

            slTV = itemView.findViewById(R.id.slTV);
            bundleNoTV = itemView.findViewById(R.id.bundleNoTV);
            qntyPcsTV = itemView.findViewById(R.id.qntyPcsTV);
            poTV = itemView.findViewById(R.id.poTV);
            buyerTV = itemView.findViewById(R.id.buyerTV);
            floorTV = itemView.findViewById(R.id.floorTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            sizeTV = itemView.findViewById(R.id.sizeTV);

            removeBT = itemView.findViewById(R.id.removeBT);
            moreBT = itemView.findViewById(R.id.moreBT);

            this.onMoreHeadListener = mOnMoreHeadListener;
            moreBT.setOnClickListener(this);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.moreBT:
                    onMoreHeadListener.onMoreHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }
    public interface OnMoreHeadListener {
        void onMoreHeadClick(int position, View v);
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
}
