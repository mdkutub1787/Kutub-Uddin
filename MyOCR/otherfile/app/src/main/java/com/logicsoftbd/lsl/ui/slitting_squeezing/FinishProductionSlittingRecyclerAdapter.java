package com.logicsoftbd.lsl.ui.slitting_squeezing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;

import java.util.List;

public class FinishProductionSlittingRecyclerAdapter extends RecyclerView.Adapter<FinishProductionSlittingRecyclerAdapter.ViewHolder> {
        private static final String TAG = "FinishProductionSlittin";
        List<SlitteringSequzBarCodeResponse.DtlsIndex> dtlsIndexArrayList;
        Context context;
        OnSelectSelectListener mOnSelectListener;
        Integer selectedItemPosition = -1;

        public FinishProductionSlittingRecyclerAdapter(List<SlitteringSequzBarCodeResponse.DtlsIndex> dtlsIndexArrayList, Context context, OnSelectSelectListener mOnSelectListener) {
                this.dtlsIndexArrayList = dtlsIndexArrayList;
                this.context = context;
                this.mOnSelectListener = mOnSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finish_production_item_layout, parent, false);
                return new ViewHolder(view, mOnSelectListener);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
                viewHolder.bcodeNoTV.setText(dtlsIndexArrayList.get(position).getProdId());
                viewHolder.barcodeTV.setText(dtlsIndexArrayList.get(position).getBarcodeNo());
                viewHolder.weightTV.setText(dtlsIndexArrayList.get(position).getBatchQnty());

                viewHolder.weightTV.setText(String.format("%.2f", Double.parseDouble(dtlsIndexArrayList.get(position).getBatchQnty())));
                viewHolder.prodWeightET.setText(String.format("%.2f", Double.parseDouble(dtlsIndexArrayList.get(position).getBatchQnty())));


                if( dtlsIndexArrayList.get(position).getBarcode_status() != null && dtlsIndexArrayList.get(position).getBarcode_status() ){
                        viewHolder._layout.setBackgroundColor(Color.parseColor("#A5B4C8"));
                        viewHolder.unSelectCheckBox.setChecked(true);
                        viewHolder.prodWeightET.setEnabled(true);
                        dtlsIndexArrayList.get(position).setProdBatchQnty(dtlsIndexArrayList.get(position).getBatchQnty());
                }else{
                        viewHolder._layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        viewHolder.unSelectCheckBox.setChecked(false);
                        viewHolder.prodWeightET.setEnabled(false);
                }

                if( dtlsIndexArrayList.get(position).getChecked() != null && dtlsIndexArrayList.get(position).getChecked().equals("1") ){
                        viewHolder._layout.setBackgroundColor(Color.parseColor("#b3ccff"));
                        viewHolder.unSelectCheckBox.setChecked(true);
                        viewHolder.unSelectCheckBox.setEnabled(false);
                        viewHolder.prodWeightET.setEnabled(false);
                        dtlsIndexArrayList.get(position).setProdBatchQnty(dtlsIndexArrayList.get(position).getBatchQnty());
                }

                viewHolder.prodWeightET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                try{
                                        String editedWeight = charSequence.toString();
                                        dtlsIndexArrayList.get(position).setProdBatchQnty(editedWeight);
                                }catch (Exception e){
                                        Log.d(TAG, "onTextChanged: "+e.getMessage());
                                }

                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                });
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return dtlsIndexArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView bcodeNoTV, barcodeTV, weightTV, serialTV;
                EditText prodWeightET;
                CheckBox unSelectCheckBox;
                LinearLayout _layout;
                OnSelectSelectListener onSelectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSelectSelectListener onSelectSelectListener) {
                        super(itemView);
                        bcodeNoTV = itemView.findViewById(R.id.bcodeNoTV);
                        barcodeTV = itemView.findViewById(R.id.barcodeTV);
                        weightTV = itemView.findViewById(R.id.weightTV);
                        prodWeightET = itemView.findViewById(R.id.prodWeightET);
                        unSelectCheckBox = itemView.findViewById(R.id.unSelectCheckBox);
                        serialTV = itemView.findViewById(R.id.serialTV);
                        _layout = itemView.findViewById(R.id.layout);

                        this.onSelectSelectListener = onSelectSelectListener;
                        unSelectCheckBox.setOnClickListener(this);
                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                        if (v.getId() == R.id.unSelectCheckBox) {
                                selectedItemPosition = getAdapterPosition();
                                notifyDataSetChanged();
                                if(unSelectCheckBox.isChecked()) {
                                        unSelectCheckBox.setChecked(false);
                                }
                                onSelectSelectListener.onSelectClick(selectedItemPosition, v);
                        }
                }
        }

        public interface OnSelectSelectListener {
                void onSelectClick(int position, View v);
        }
}
