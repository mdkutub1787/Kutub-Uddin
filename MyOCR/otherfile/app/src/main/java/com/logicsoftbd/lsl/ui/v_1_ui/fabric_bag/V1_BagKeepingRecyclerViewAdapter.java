package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;



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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class V1_BagKeepingRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagKeepingRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagKeepingResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;
    private OnPrintHeadListener mOnPrintHeadListener;
    private List<String> _fabricTypeName = new ArrayList<>();
    private List<String> _fabricTypeId = new ArrayList<>();
    private List<String> _aopName = new ArrayList<>();
    private List<String> _aopId = new ArrayList<>();

    public V1_BagKeepingRecyclerViewAdapter(List<V1_BagKeepingResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.mOnPrintHeadListener = mOnPrintHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagKeepingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_keeping_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener, mOnPrintHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        V1_BagKeepingResponse.ResultSet currentItem = bagKeepingResponses.get(position);

        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
//        viewHolder.weightET.setText(bagKeepingResponses.get(position).getFinishWeight());
//        viewHolder.greyWeightET.setText(bagKeepingResponses.get(position).getGreyWeight());
        //        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getFinishWeight());
        double finishWeight = 0.0;
        try {
            finishWeight = Double.parseDouble(bagKeepingResponses.get(position).getFinishWeight());
        } catch (NumberFormatException e) {
            finishWeight = 0.0;
        }
        viewHolder.weightET.setText(String.format("%.2f", finishWeight));

//        viewHolder.greyWeightTV.setText(bagKeepingResponses.get(position).getGreyWeight());
        double greyWeight = 0.0;
        try {
            greyWeight = Double.parseDouble(bagKeepingResponses.get(position).getGreyWeight());
        } catch (NumberFormatException e) {
            greyWeight = 0.0;
        }

        viewHolder.greyWeightET.setText(String.format("%.2f", greyWeight));
        viewHolder.gsmET.setText(bagKeepingResponses.get(position).getGsm());
        viewHolder.diaET.setText(bagKeepingResponses.get(position).getDia());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getIrIb());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
        viewHolder.bagColorTV.setText(bagKeepingResponses.get(position).getBagColorName());
        viewHolder.rfidNoTV.setText(bagKeepingResponses.get(position).getRfidNo());


        if(bagKeepingResponses.get(position).getPrintingStatus()){
            viewHolder.ly.setBackgroundColor(Color.parseColor("#c9ebee"));
        }else{
            viewHolder.ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if(bagKeepingResponses.get(position).getSaveStatus()){
            viewHolder.printBT.setVisibility(View.VISIBLE);
        }else{
            viewHolder.printBT.setVisibility(View.VISIBLE);
        }

        _fabricTypeName.clear();
        _fabricTypeId.clear();
        for (V1_BagKeepingResponse.FabricType fabricType : bagKeepingResponses.get(0).getFabricType()) {
            _fabricTypeName.add(fabricType.getName());
            _fabricTypeId.add(fabricType.getId());
        }
        _fabricTypeName.add(0, "-Select-");
        _fabricTypeId.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, _fabricTypeName);
        viewHolder.fabSpinner.setAdapter(adapter);

        String currentFabTypeId = String.valueOf(bagKeepingResponses.get(position).getFabType());

        int index = _fabricTypeId.indexOf(currentFabTypeId);

        if (index != -1) {
            viewHolder.fabSpinner.setSelection(index);
        } else {
            viewHolder.fabSpinner.setSelection(0);
        }

        _aopName.clear();
        _aopId.clear();
        _aopName.add(0, "-Select-");
        _aopName.add(1, "YES");
        _aopName.add(2, "NO");
        _aopId.add(0, "-1");
        _aopId.add(1, "1");
        _aopId.add(2, "0");

        ArrayAdapter<String> aopAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, _aopName);
        viewHolder.aopSpinner.setAdapter(aopAdapter);

        String currentAOPTypeId = String.valueOf(bagKeepingResponses.get(position).getAop());

        int indexAOP = _aopId.indexOf(currentAOPTypeId);

        if (index != -1) {
            viewHolder.aopSpinner.setSelection(indexAOP);
        } else {
            viewHolder.aopSpinner.setSelection(0);
        }


        if (viewHolder.gsmET.getTag() instanceof TextWatcher) {
            viewHolder.gsmET.removeTextChangedListener((TextWatcher) viewHolder.gsmET.getTag());
        }
        if (viewHolder.diaET.getTag() instanceof TextWatcher) {
            viewHolder.diaET.removeTextChangedListener((TextWatcher) viewHolder.diaET.getTag());
        }
        if (viewHolder.weightET.getTag() instanceof TextWatcher) {
            viewHolder.weightET.removeTextChangedListener((TextWatcher) viewHolder.weightET.getTag());
        }
        if (viewHolder.fabSpinner.getTag() instanceof AdapterView.OnItemSelectedListener) {
            viewHolder.fabSpinner.setOnItemSelectedListener(null);
        }

        // Add TextWatchers
        TextWatcher gsmWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentItem.setGsm(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
        viewHolder.gsmET.addTextChangedListener(gsmWatcher);
        viewHolder.gsmET.setTag(gsmWatcher);

        TextWatcher diaWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentItem.setDia(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
        viewHolder.diaET.addTextChangedListener(diaWatcher);
        viewHolder.diaET.setTag(diaWatcher);

        TextWatcher weightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentItem.setFinishWeight(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
        viewHolder.weightET.addTextChangedListener(weightWatcher);
        viewHolder.weightET.setTag(weightWatcher);

        TextWatcher greyWeightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentItem.setGreyWeight(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
        viewHolder.greyWeightET.addTextChangedListener(greyWeightWatcher);
        viewHolder.greyWeightET.setTag(greyWeightWatcher);

        // Add Spinner OnItemSelectedListener
        AdapterView.OnItemSelectedListener fabSpinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentItem.setFabType(_fabricTypeId.get(pos));
                currentItem.setFabTypeName(_fabricTypeName.get(pos));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        viewHolder.fabSpinner.setOnItemSelectedListener(fabSpinnerListener);
        viewHolder.fabSpinner.setTag(fabSpinnerListener);

        AdapterView.OnItemSelectedListener aopSpinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentItem.setAop(_aopId.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };

        viewHolder.aopSpinner.setOnItemSelectedListener(aopSpinnerListener);
        viewHolder.aopSpinner.setTag(aopSpinnerListener);

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
        private TextView bagNoTV, batchNoTV, irbrTV, colorTV, buyerNameTV, rfidNoTV, bagColorTV;
        private EditText gsmET, diaET, weightET, greyWeightET;
        private Spinner fabSpinner, aopSpinner;
        private ImageButton removeBT, printBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;
        private OnPrintHeadListener onPrintHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            gsmET = itemView.findViewById(R.id.gsmET);
            diaET = itemView.findViewById(R.id.diaET);
            weightET = itemView.findViewById(R.id.weightET);
            greyWeightET = itemView.findViewById(R.id.greyWeightET);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            irbrTV = itemView.findViewById(R.id.irbrTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            fabSpinner = itemView.findViewById(R.id.fabSpinner);
            aopSpinner = itemView.findViewById(R.id.aopSpinner);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
            bagColorTV = itemView.findViewById(R.id.bagColorTV);
            ly = itemView.findViewById(R.id.ly);

            removeBT = itemView.findViewById(R.id.removeBT);
            printBT = itemView.findViewById(R.id.printBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);

            this.onPrintHeadListener = mOnPrintHeadListener;
            printBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.printBT:
                    onPrintHeadListener.onPrintHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
    public interface OnPrintHeadListener {
        void onPrintHeadClick(int position, View v);
    }
}
