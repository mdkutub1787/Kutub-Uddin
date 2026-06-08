package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModelClass;

import java.util.ArrayList;


public class V1_SewingOutputRecyclerViewAdapter extends RecyclerView.Adapter<V1_SewingOutputRecyclerViewAdapter.ViewHolder>{
    ArrayList<V1_SewingOutputModelClass> sewingOutputModelClasses = new ArrayList<>();
    LayoutInflater vi;
    Context context;
    private int Resource, qty = 0, qc_qty = 0, reject_number = 0, alter_number = 0, spot_number = 0, replace_number = 0;
    private boolean replaceStatus = false;
    Handler handler = new Handler();
    Runnable runnable;

    private final OnRejectHeadListener mOnRejectHeadListener;
    private final OnAlterHeadListener mOnAlterHeadListener;
    private final OnSpotHeadListener mOnSpotHeadListener;

    public V1_SewingOutputRecyclerViewAdapter(ArrayList<V1_SewingOutputModelClass> sewingOutputModelClasses, OnRejectHeadListener mOnRejectHeadListener, OnAlterHeadListener mOnAlterHeadListener, OnSpotHeadListener mOnSpotHeadListener, Context context) {
        this.sewingOutputModelClasses = sewingOutputModelClasses;
        this.mOnRejectHeadListener = mOnRejectHeadListener;
        this.mOnAlterHeadListener = mOnAlterHeadListener;
        this.mOnSpotHeadListener = mOnSpotHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_SewingOutputRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_object_layout_test_test, parent, false);
        return new ViewHolder(view, mOnRejectHeadListener, mOnAlterHeadListener, mOnSpotHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.barcodeNumber.setText(sewingOutputModelClasses.get(position).getBarcode_no());
        viewHolder.bundleNotv.setText(sewingOutputModelClasses.get(position).getBundle_no());
        viewHolder.yeartv.setText(sewingOutputModelClasses.get(position).getYearNo());
        viewHolder.jobNotv.setText(sewingOutputModelClasses.get(position).getJobNo());
        viewHolder.buyertv.setText(sewingOutputModelClasses.get(position).getBuyer());
        viewHolder.orderNotv.setText(sewingOutputModelClasses.get(position).getOrderNo());
        viewHolder.gmtsItemtv.setText(sewingOutputModelClasses.get(position).getItemNo());
        viewHolder.countrytv.setText(sewingOutputModelClasses.get(position).getCountry());
        viewHolder.colortv.setText(sewingOutputModelClasses.get(position).getColorNo());
        viewHolder.sizetv.setText(sewingOutputModelClasses.get(position).getSizeNo());
        viewHolder.qtytv.setText(String.valueOf(sewingOutputModelClasses.get(position).getQuantity()));
        viewHolder.qty_qc.setText(String.valueOf(sewingOutputModelClasses.get(position).getQuantity()));
        sewingOutputModelClasses.get(position).setQc_qty(Integer.parseInt(viewHolder.qty_qc.getText().toString()));

        buttonClickListener(viewHolder, position);
    }

    @Override
    public int getItemViewType(int position) {
        qty = sewingOutputModelClasses.get(position).getQuantity();
        qc_qty = qty;
        return position;
    }

    @Override
    public int getItemCount() {
        return sewingOutputModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private final TextView barcodeNumber;
        private final TextView bundleNotv;
        private final TextView yeartv;
        private final TextView jobNotv;
        private final TextView buyertv;
        private final TextView orderNotv;
        private final TextView gmtsItemtv;
        private final TextView countrytv;
        private final TextView colortv;
        private final TextView sizetv;
        private final TextView qtytv;
        private final TextView qty_qc;
        private final TextView rejectTV;
        private final TextView alterTV;
        private final TextView spotTV;
        private final EditText reject;
        private final EditText alter;
        private final EditText spot;
        private final EditText replace;
        private final ImageButton reject_up;
        private final ImageButton reject_down;
        private final ImageButton alter_up;
        private final ImageButton alter_down;
        private final ImageButton spot_up;
        private final ImageButton spot_down;
        private final ImageButton replace_up;
        private final ImageButton replace_down;
        OnRejectHeadListener onRejectHeadListener;
        OnAlterHeadListener onAlterHeadListener;
        OnSpotHeadListener onSpotHeadListener;

        public ViewHolder(@NonNull View itemView, OnRejectHeadListener mOnRejectHeadListener, OnAlterHeadListener mOnAlterHeadListener, OnSpotHeadListener mOnSpotHeadListener) {
            super(itemView);

            barcodeNumber = itemView.findViewById(R.id.barcodeTV);
            bundleNotv = itemView.findViewById(R.id.bundleNoTV);
            yeartv = itemView.findViewById(R.id.yearTV);
            jobNotv = itemView.findViewById(R.id.jobNoTV);
            buyertv = itemView.findViewById(R.id.buyerTV);
            orderNotv = itemView.findViewById(R.id.orderNoTV);
            gmtsItemtv = itemView.findViewById(R.id.gmtsTV);
            countrytv = itemView.findViewById(R.id.countryTV);
            colortv = itemView.findViewById(R.id.colorTV);
            sizetv = itemView.findViewById(R.id.sizeTV);
            qtytv = itemView.findViewById(R.id.qtyTV);

            reject = itemView.findViewById(R.id.rejectET);
            alter = itemView.findViewById(R.id.alterET);
            spot = itemView.findViewById(R.id.spotET);

            replace = itemView.findViewById(R.id.replaceET);
            qty_qc = itemView.findViewById(R.id.Qc_qty_Tv);

            reject_up = itemView.findViewById(R.id.rejectup);
            reject_down = itemView.findViewById(R.id.rejectdown);

            alter_up = itemView.findViewById(R.id.alterup);
            alter_down = itemView.findViewById(R.id.alterdown);

            spot_up = itemView.findViewById(R.id.spotup);
            spot_down = itemView.findViewById(R.id.spotdown);

            replace_up = itemView.findViewById(R.id.replaceup);
            replace_down = itemView.findViewById(R.id.replacedown);
            rejectTV = itemView.findViewById(R.id.rejectTV);
            alterTV = itemView.findViewById(R.id.alterTV);
            spotTV = itemView.findViewById(R.id.spotTV);

            this.onRejectHeadListener = mOnRejectHeadListener;
            rejectTV.setOnClickListener(this);

            this.onAlterHeadListener = mOnAlterHeadListener;
            alterTV.setOnClickListener(this);

            this.onSpotHeadListener = mOnSpotHeadListener;
            spotTV.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rejectTV:
                    onRejectHeadListener.onRejectHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.alterTV:
                    onAlterHeadListener.onAlterHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.spotTV:
                    onSpotHeadListener.onSportHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRejectHeadListener {
        void onRejectHeadClick(int position, View v);
    }

    public interface OnAlterHeadListener {
        void onAlterHeadClick(int position, View v);
    }

    public interface OnSpotHeadListener {
        void onSportHeadClick(int position, View v);
    }

    private void buttonClickListener(final ViewHolder holder, final int position) {
        if(String.valueOf(sewingOutputModelClasses.get(position).getReplace_field_disable()).equals("1")){
            holder.replace_up.setEnabled(false);
            holder.replace_down.setEnabled(false);
            holder.replace.setEnabled(false);
        }

        holder.reject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    reject_number = 0;
                } else {
                    reject_number = Integer.parseInt(String.valueOf(s));
                }
//                calculateData(holder, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.alter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    alter_number = 0;
                } else {
                    alter_number = Integer.parseInt(String.valueOf(s));
                }
//                calculateData(holder, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    spot_number = 0;
                } else {
                    spot_number = Integer.parseInt(String.valueOf(s));
                }
//                calculateData(holder, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.replace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    replace_number = 0;
                } else {
                    replace_number = Integer.parseInt(String.valueOf(s));
                }
//                calculateData(holder, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.reject_up.setOnClickListener(v -> {
            reject_number = reject_number +  1;
            holder.reject.setText(String.valueOf(reject_number));
            calculateData(holder, position, "r");
        });

        holder.reject_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.reject_up.isPressed()) return;
                    increaseValue(reject_number, holder.reject, holder, position, "r");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.reject_down.setOnClickListener(v -> {
            reject_number = reject_number -  1;
            if(reject_number >= 0){
                holder.reject.setText(String.valueOf(reject_number));
            }else {
                reject_number = 0;
                holder.reject.setText(String.valueOf(0));
            }

            calculateData(holder, position, "r");
        });

        holder.reject_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.reject_down.isPressed()) return;
                    decreaseValue(reject_number, holder.reject, holder, position, "r");
                    handler.postDelayed(runnable, 100);
                }
            };

            handler.postDelayed(runnable, 100);
            return true;
        });

        holder.alter_up.setOnClickListener(v -> {
            alter_number = alter_number +  1;
            holder.alter.setText(String.valueOf(alter_number));
            calculateData(holder, position, "a");
        });

        holder.alter_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.alter_up.isPressed()) return;
                    increaseValue(alter_number, holder.alter, holder, position, "a");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.alter_down.setOnClickListener(v -> {
            alter_number = alter_number -  1;
            if(alter_number >= 0){
                holder.alter.setText(String.valueOf(alter_number));
            }else {
                alter_number = 0;
                holder.alter.setText(String.valueOf(0));
            }

            calculateData(holder, position, "a");
        });

        holder.alter_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.alter_down.isPressed()) return;
                    decreaseValue(alter_number, holder.alter, holder, position, "a");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.spot_up.setOnClickListener(v -> {
            spot_number = spot_number +  1;
            holder.spot.setText(String.valueOf(spot_number));
            calculateData(holder, position, "s");
        });

        holder.spot_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.spot_up.isPressed()) return;
                    increaseValue(spot_number, holder.spot, holder, position, "s");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.spot_down.setOnClickListener(v -> {
            spot_number = spot_number -  1;
            if(spot_number >= 0){
                holder.spot.setText(String.valueOf(spot_number));
            }else {
                spot_number = 0;
                holder.spot.setText(String.valueOf(0));
            }

            calculateData(holder, position, "s");
        });
        
        holder.spot_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.spot_down.isPressed()) return;
                    decreaseValue(spot_number, holder.spot, holder, position, "s");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.replace_up.setOnClickListener(v -> {
            if(replaceStatus){
                replace_number = replace_number +  1;
                holder.replace.setText(String.valueOf(replace_number));
            }
            calculateData(holder, position, "re");
        });

        holder.replace_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.replace_up.isPressed()) return;
                    increaseValue(replace_number, holder.replace, holder, position, "re");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.replace_down.setOnClickListener(v -> {
            replace_number = replace_number -  1;
            if(replace_number >= 0){
                holder.replace.setText(String.valueOf(replace_number));
            }else {
                replace_number = 0;
                holder.replace.setText(String.valueOf(0));
            }

            calculateData(holder, position, "re");
        });

        holder.replace_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.replace_down.isPressed()) return;
                    decreaseValue(replace_number, holder.replace, holder, position, "re");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });
    }

    private void increaseValue(int number, EditText editText, ViewHolder holder, int position, String defectItem) {
        number = number +  1;
        editText.setText(String.valueOf(number));
        calculateData(holder, position, defectItem);
    }

    private void decreaseValue(int number, EditText editText, ViewHolder holder, int position, String defectItem) {
        number = number -  1;
        if(number >= 0){
            editText.setText(String.valueOf(number));
        }else {
            number = 0;
            editText.setText(String.valueOf(number));
        }
        calculateData(holder, position, defectItem);
    }

    private void calculateData(ViewHolder holder, int position, String defectItem) {

        //int qc_qty = 0;
        int re = 0, al = 0, sp = 0, rep = 0;
        try {
            al = Integer.parseInt((holder.alter.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        try {
            re = Integer.parseInt((holder.reject.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        try {
            sp = Integer.parseInt((holder.spot.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        try {
            rep = Integer.parseInt((holder.replace.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

//        qc_qty = qty - (re + al + sp + rep);
        if(String.valueOf(sewingOutputModelClasses.get(position).getReplace_field_disable()).equals("1")){
            qc_qty = qty - (re )+rep;
        }else {
            qc_qty = qty - (re + al + sp )+rep;
        }

        if(qc_qty <= 0) {
            holder.qty_qc.setText(String.valueOf(qty));
            if(defectItem.equals("r")){
                holder.reject.setText("");
                reject_number = 0;
            }
            if(defectItem.equals("a")){
                holder.alter.setText("");
                alter_number = 0;
            }
            if(defectItem.equals("s")){
                holder.spot.setText("");
                spot_number = 0;
            }
            if(defectItem.equals("re")){
                holder.replace.setText("");
                replace_number = 0;
            }
        }

        sewingOutputModelClasses.get(position).setReject(reject_number);
        sewingOutputModelClasses.get(position).setAlter(alter_number);
        sewingOutputModelClasses.get(position).setSpot(spot_number);

        if (qc_qty>qty){
            replaceStatus = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.alter.getContext());
            builder.setTitle("Message")
                    .setMessage("QC pass quantity can't be higher than total quantity!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        replace_number--;
                        holder.replace.setText(String.valueOf(replace_number));
                        sewingOutputModelClasses.get(position).setReplace(replace_number);
                        dialog.dismiss();
                    });
            AlertDialog dialog  = builder.create();
            dialog.show();
        }else{
            sewingOutputModelClasses.get(position).setReplace(replace_number);
            holder.qty_qc.setText(String.valueOf(qc_qty));
            sewingOutputModelClasses.get(position).setQc_qty(qc_qty);
            replaceStatus = true;
        }
    }
}
