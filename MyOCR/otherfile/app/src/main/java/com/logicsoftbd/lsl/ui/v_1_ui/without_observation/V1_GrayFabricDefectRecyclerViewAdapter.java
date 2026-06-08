package com.logicsoftbd.lsl.ui.v_1_ui.without_observation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class V1_GrayFabricDefectRecyclerViewAdapter extends RecyclerView.Adapter<V1_GrayFabricDefectRecyclerViewAdapter.ViewHolder> {
    private ArrayList<V1_QcModelRND> _qcModel;
    private Context context;
    private OnIncrementHeadListener mIncrementHeadListener;
    private OnDecrementHeadListener mDecrementHeadListener;
    private OnSpinnerListener mOnSpinnerListener;
    public Map<Integer, Integer> selectedItems = new HashMap<Integer, Integer>();


    public V1_GrayFabricDefectRecyclerViewAdapter(ArrayList<V1_QcModelRND> _qcModel, Context context, OnIncrementHeadListener mIncrementHeadListener, OnDecrementHeadListener mDecrementHeadListener, OnSpinnerListener mOnSpinnerListener) {
        this._qcModel = _qcModel;
        this.context = context;
        this.mIncrementHeadListener = mIncrementHeadListener;
        this.mDecrementHeadListener = mDecrementHeadListener;
        this.mOnSpinnerListener = mOnSpinnerListener;
    }

    @NonNull
    @Override
    public V1_GrayFabricDefectRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        return new ViewHolder(view, mIncrementHeadListener, mDecrementHeadListener, mOnSpinnerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull V1_GrayFabricDefectRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvItemName.setText(_qcModel.get(position).getQcItemName());
        holder.tvDefectnumber.setText(String.valueOf(_qcModel.get(position).getQcItemNumber()));
        holder.tvDefectCount.setText(String.valueOf(_qcModel.get(position).getQcDefectNumber()));

        if(position == 0){
            ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.hole_in_inch
                    , R.layout.spinner_item);
            holder.mSpinner.setAdapter(adapterhole);
        }else{
            ArrayAdapter<CharSequence> adapterdefect = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.defect_in_inch
                    , R.layout.spinner_item);
            holder.mSpinner.setAdapter(adapterdefect);
        }

        if(_qcModel.get(position).getQcDefectNumber() == 0){
            holder.tvDefectCount.setTextColor(Color.BLACK);
            holder.tvDefectnumber.setTextColor(Color.BLACK);
            holder.tvItemName.setTextColor(Color.BLACK);
        }else{
            holder.tvDefectCount.setTextColor(Color.RED);
            holder.tvDefectCount.setBackgroundColor(Color.CYAN);
            holder.tvDefectnumber.setTextColor(Color.BLUE);
            holder.tvItemName.setTextColor(Color.BLUE);
        }

        if(selectedItems.get(position) != null){
            holder.mSpinner.setSelection(selectedItems.get(position));
        }


        holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int selectedIndex, long id) {
                selectedItems.put(position, selectedIndex);
                if (mOnSpinnerListener != null) {
                    mOnSpinnerListener.onSpinnerHeadClick(position, selectedItemView, selectedIndex);
                    _qcModel.get(position).setSpinneritem(selectedIndex);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing selected
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private Button btn_plus, btn_minus;
        private TextView tvItemName, tvDefectnumber, tvDefectCount;
        private Spinner mSpinner;
        OnIncrementHeadListener onIncrementHeadListener;
        OnDecrementHeadListener onDecrementHeadListener;
        OnSpinnerListener onSpinnerListener;

        public ViewHolder(@NonNull View itemView, OnIncrementHeadListener mIncrementHeadListener, OnDecrementHeadListener mDecrementHeadListener, OnSpinnerListener mOnSpinnerListener) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.itemName);
            tvDefectnumber = itemView.findViewById(R.id.itemCount);
            tvDefectCount = itemView.findViewById(R.id.defectCountTV);
            btn_plus = itemView.findViewById(R.id.addBtn);
            btn_minus = itemView.findViewById(R.id.removeBtn);
            mSpinner = itemView.findViewById(R.id.spinner);

            this.onIncrementHeadListener = mIncrementHeadListener;
            btn_plus.setOnClickListener(this);

            this.onDecrementHeadListener = mDecrementHeadListener;
            btn_minus.setOnClickListener(this);

            this.onSpinnerListener = mOnSpinnerListener;


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.addBtn:
                    onIncrementHeadListener.onIncrementHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.removeBtn:
                    onDecrementHeadListener.onDecrementHeadClick(getAdapterPosition(), v);
                    break;
//                case R.id.spinner:
//                    onSpinnerListener.onSpinnerHeadClick(getAdapterPosition(), v);
//                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return _qcModel.size();
    }

    public interface OnIncrementHeadListener {
        void onIncrementHeadClick(int position, View v);
    }

    public interface OnDecrementHeadListener {
        void onDecrementHeadClick(int position, View v);
    }

    public interface OnSpinnerListener {
        void onSpinnerHeadClick(int position, View v, int selectedItem);
    }

}
