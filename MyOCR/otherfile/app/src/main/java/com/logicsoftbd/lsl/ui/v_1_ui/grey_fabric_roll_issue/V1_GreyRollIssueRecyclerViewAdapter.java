package com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_issue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssueItemModel;

import java.util.ArrayList;

public class V1_GreyRollIssueRecyclerViewAdapter extends RecyclerView.Adapter<V1_GreyRollIssueRecyclerViewAdapter.ViewHolder> {
    private ArrayList<V1_GreyRollIssueItemModel> greyRollReceiveItemModels = new ArrayList<>();
    private Context context;
    private OnMoreHeadListener mOnMoreHeadListener;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_GreyRollIssueRecyclerViewAdapter(ArrayList<V1_GreyRollIssueItemModel> greyRollReceiveItemModels, OnMoreHeadListener mOnMoreHeadListener, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.greyRollReceiveItemModels = greyRollReceiveItemModels;
        this.mOnMoreHeadListener = mOnMoreHeadListener;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_GreyRollIssueRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grey_roll_receive_item_layout, parent, false);
        return new ViewHolder(view, mOnMoreHeadListener, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.slTV.setText(String.valueOf(position+1));
        viewHolder.barcodeTV.setText(greyRollReceiveItemModels.get(position).getBarcodeNo());
        viewHolder.rollTV.setText(greyRollReceiveItemModels.get(position).getRollNo());
        viewHolder.rollWgtTV.setText(greyRollReceiveItemModels.get(position).getQnty());
        viewHolder.fsoJobTV.setText(greyRollReceiveItemModels.get(position).getJobNo());
        if(greyRollReceiveItemModels.get(position).getJobNo() != null){
            viewHolder.fsoJobTV.setText(greyRollReceiveItemModels.get(position).getJobNo().substring(greyRollReceiveItemModels.get(position).getJobNo().length()-4));
        }
        viewHolder.fabBookingTV.setText(greyRollReceiveItemModels.get(position).getFso_booking());
        viewHolder.compositionTV.setText(greyRollReceiveItemModels.get(position).getComposition());
        viewHolder.constructionTV.setText(greyRollReceiveItemModels.get(position).getConstruction());
        viewHolder.gsmTV.setText(greyRollReceiveItemModels.get(position).getGsm());
        viewHolder.machineNameTV.setText(greyRollReceiveItemModels.get(position).getMachineName());
        viewHolder.diaTV.setText(greyRollReceiveItemModels.get(position).getWidth());
        viewHolder.fabricColorTV.setText(greyRollReceiveItemModels.get(position).getColorName());
        viewHolder.yarnLotTV.setText(String.valueOf(greyRollReceiveItemModels.get(position).getYarnLot()));
        viewHolder.stitchLengthTV.setText(String.valueOf(greyRollReceiveItemModels.get(position).getStitchLength()));
        viewHolder.brandTV.setText(String.valueOf(greyRollReceiveItemModels.get(position).getBrandName()));
        viewHolder.programNoTV.setText(String.valueOf(greyRollReceiveItemModels.get(position).getProgramNo()));

        if(greyRollReceiveItemModels.get(position).getStatus() == true){
            viewHolder.slF.setVisibility(View.VISIBLE);
            viewHolder.barcodeF.setVisibility(View.VISIBLE);
            viewHolder.rollF.setVisibility(View.VISIBLE);
            viewHolder.rollWgtF.setVisibility(View.VISIBLE);
            viewHolder.internalRefFTV.setVisibility(View.VISIBLE);
            viewHolder.fsoJobF.setVisibility(View.VISIBLE);
            viewHolder.fsoJobF.setVisibility(View.VISIBLE);
            viewHolder.fabBookingF.setVisibility(View.VISIBLE);
            viewHolder.machineNameF.setVisibility(View.VISIBLE);
            viewHolder.compositionF.setVisibility(View.VISIBLE);
            viewHolder.constructionF.setVisibility(View.VISIBLE);
            viewHolder.programNoF.setVisibility(View.VISIBLE);
            viewHolder.gsmF.setVisibility(View.VISIBLE);
            viewHolder.diaF.setVisibility(View.VISIBLE);
            viewHolder.fabricColorF.setVisibility(View.VISIBLE);
            viewHolder.yarnLotF.setVisibility(View.VISIBLE);
            viewHolder.stitchLengthF.setVisibility(View.VISIBLE);
            viewHolder.brandF.setVisibility(View.VISIBLE);
            viewHolder.removeF.setVisibility(View.VISIBLE);
            viewHolder.moreF.setVisibility(View.VISIBLE);
            viewHolder.moreBT.setBackground( context.getResources().getDrawable(R.color.navy_blue_dark));
        }else{
            viewHolder.slF.setVisibility(View.GONE);
            viewHolder.barcodeF.setVisibility(View.GONE);
            viewHolder.rollF.setVisibility(View.GONE);
            viewHolder.rollWgtF.setVisibility(View.GONE);
            viewHolder.internalRefFTV.setVisibility(View.GONE);
            viewHolder.fsoJobF.setVisibility(View.GONE);
            viewHolder.fsoJobF.setVisibility(View.GONE);
            viewHolder.fabBookingF.setVisibility(View.GONE);
            viewHolder.machineNameF.setVisibility(View.GONE);
            viewHolder.compositionF.setVisibility(View.GONE);
            viewHolder.constructionF.setVisibility(View.GONE);
            viewHolder.programNoF.setVisibility(View.GONE);
            viewHolder.gsmF.setVisibility(View.GONE);
            viewHolder.diaF.setVisibility(View.GONE);
            viewHolder.fabricColorF.setVisibility(View.GONE);
            viewHolder.yarnLotF.setVisibility(View.GONE);
            viewHolder.stitchLengthF.setVisibility(View.GONE);
            viewHolder.brandF.setVisibility(View.GONE);
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
        return greyRollReceiveItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView slTV, barcodeTV, rollTV, rollWgtTV, internalRefFTV, fsoJobTV, fabBookingTV, programNoTV, machineNameTV, compositionTV, constructionTV, gsmTV, diaTV, fabricColorTV, yarnLotTV, stitchLengthTV, brandTV,
                slF, barcodeF, rollF, rollWgtF, fsoJobF, fabBookingF, programNoF, machineNameF, compositionF, constructionF, gsmF, diaF, fabricColorF, yarnLotF, stitchLengthF, brandF, removeF, moreF;
        private ImageButton removeBT, moreBT;
        private OnMoreHeadListener onMoreHeadListener;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnMoreHeadListener mOnMoreHeadListener, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            slTV = itemView.findViewById(R.id.slTV);
            barcodeTV = itemView.findViewById(R.id.barcodeTV);
            rollTV = itemView.findViewById(R.id.rollTV);
            rollWgtTV = itemView.findViewById(R.id.rollWgtTV);
            fsoJobTV = itemView.findViewById(R.id.fsoJobTV);
            internalRefFTV = itemView.findViewById(R.id.internalRefF);
            fabBookingTV = itemView.findViewById(R.id.fabBookingTV);
            programNoTV = itemView.findViewById(R.id.programNoTV);
            machineNameTV = itemView.findViewById(R.id.machineNameTV);
            compositionTV = itemView.findViewById(R.id.compositionTV);
            constructionTV = itemView.findViewById(R.id.constructionTV);
            gsmTV = itemView.findViewById(R.id.gsmTV);
            diaTV = itemView.findViewById(R.id.diaTV);
            fabricColorTV = itemView.findViewById(R.id.fabricColorTV);
            yarnLotTV = itemView.findViewById(R.id.yarnLotTV);
            stitchLengthTV = itemView.findViewById(R.id.stitchLengthTV);
            brandTV = itemView.findViewById(R.id.brandTV);

            removeBT = itemView.findViewById(R.id.removeBT);
            moreBT = itemView.findViewById(R.id.moreBT);

            slF = itemView.findViewById(R.id.slF);
            barcodeF = itemView.findViewById(R.id.barcodeF);
            rollF = itemView.findViewById(R.id.rollF);
            rollWgtF = itemView.findViewById(R.id.rollWgtF);
            fsoJobF = itemView.findViewById(R.id.fsoJobF);
            fabBookingF = itemView.findViewById(R.id.fabBookingF);
            programNoF = itemView.findViewById(R.id.programNoF);
            machineNameF = itemView.findViewById(R.id.machineNameF);
            compositionF = itemView.findViewById(R.id.compositionF);
            constructionF = itemView.findViewById(R.id.constructionF);
            gsmF = itemView.findViewById(R.id.gsmF);
            diaF = itemView.findViewById(R.id.diaF);
            fabricColorF = itemView.findViewById(R.id.fabricColorF);
            yarnLotF = itemView.findViewById(R.id.yarnLotF);
            stitchLengthF = itemView.findViewById(R.id.stitchLengthF);
            brandF = itemView.findViewById(R.id.brandF);
            removeF = itemView.findViewById(R.id.removeF);
            moreF = itemView.findViewById(R.id.moreF);

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
