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


public class V1_SewingOutputRecyclerViewAdapter_v1 extends RecyclerView.Adapter<V1_SewingOutputRecyclerViewAdapter_v1.ViewHolder>{
    ArrayList<V1_SewingOutputModelClass> sewingOutputModelClasses = new ArrayList<>();
    LayoutInflater vi;
    Context context;
    private int Resource, qty = 0, qc_qty = 0, reject_number = 0, defect_number = 0, replace_number = 0;
    private boolean replaceStatus = false;
    Handler handler = new Handler();
    Runnable runnable;

    private final OnRejectHeadListener mOnRejectHeadListener;
    private final OnDefectHeadListener mOnDefectHeadListener;

    public V1_SewingOutputRecyclerViewAdapter_v1(ArrayList<V1_SewingOutputModelClass> sewingOutputModelClasses, OnRejectHeadListener mOnRejectHeadListener, OnDefectHeadListener mOnDefectHeadListener, Context context) {
        this.sewingOutputModelClasses = sewingOutputModelClasses;
        this.mOnRejectHeadListener = mOnRejectHeadListener;
        this.mOnDefectHeadListener = mOnDefectHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_SewingOutputRecyclerViewAdapter_v1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sewing_output_object_layout_v1, parent, false);
        return new ViewHolder(view, mOnRejectHeadListener, mOnDefectHeadListener);
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
        private final TextView defectTV;
        private final EditText reject;
        private final EditText defect;
        private final EditText replace;
        private final ImageButton reject_up;
        private final ImageButton reject_down;
        private final ImageButton defect_up;
        private final ImageButton defect_down;
        private final ImageButton replace_up;
        private final ImageButton replace_down;
        OnRejectHeadListener onRejectHeadListener;
        OnDefectHeadListener onDefectHeadListener;


        public ViewHolder(@NonNull View itemView, OnRejectHeadListener mOnRejectHeadListener, OnDefectHeadListener mOnDefectHeadListener) {
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
            defect = itemView.findViewById(R.id.defectET);

            replace = itemView.findViewById(R.id.replaceET);
            qty_qc = itemView.findViewById(R.id.Qc_qty_Tv);

            reject_up = itemView.findViewById(R.id.rejectup);
            reject_down = itemView.findViewById(R.id.rejectdown);

            defect_up = itemView.findViewById(R.id.defectUp);
            defect_down = itemView.findViewById(R.id.defectDown);

            replace_up = itemView.findViewById(R.id.replaceup);
            replace_down = itemView.findViewById(R.id.replacedown);
            rejectTV = itemView.findViewById(R.id.rejectTV);
            defectTV = itemView.findViewById(R.id.defectTV);

            this.onRejectHeadListener = mOnRejectHeadListener;
            rejectTV.setOnClickListener(this);

            this.onDefectHeadListener = mOnDefectHeadListener;
            defectTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rejectTV:
                    onRejectHeadListener.onRejectHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.defectTV:
                    onDefectHeadListener.onDefectHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRejectHeadListener {
        void onRejectHeadClick(int position, View v);
    }

    public interface OnDefectHeadListener {
        void onDefectHeadClick(int position, View v);
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
        holder.defect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    defect_number = 0;
                } else {
                    defect_number = Integer.parseInt(String.valueOf(s));
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

        holder.defect_up.setOnClickListener(v -> {
            defect_number = defect_number +  1;
            holder.defect.setText(String.valueOf(defect_number));
            calculateData(holder, position, "a");
        });

        holder.defect_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.defect_up.isPressed()) return;
                    increaseValue(defect_number, holder.defect, holder, position, "a");
                    handler.postDelayed(runnable, 200);
                }
            };

            handler.postDelayed(runnable, 200);
            return true;
        });

        holder.defect_down.setOnClickListener(v -> {
            defect_number = defect_number -  1;
            if(defect_number >= 0){
                holder.defect.setText(String.valueOf(defect_number));
            }else {
                defect_number = 0;
                holder.defect.setText(String.valueOf(0));
            }

            calculateData(holder, position, "a");
        });

        holder.defect_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.defect_down.isPressed()) return;
                    decreaseValue(defect_number, holder.defect, holder, position, "a");
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
        int re = 0, al = 0, rep = 0;
        try {
            al = Integer.parseInt((holder.defect.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        try {
            re = Integer.parseInt((holder.reject.getText().toString()));
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
            qc_qty = qty - (re + al)+rep;
        }

        if(qc_qty <= 0) {
            holder.qty_qc.setText(String.valueOf(qty));
            if(defectItem.equals("r")){
                holder.reject.setText("");
                reject_number = 0;
            }
            if(defectItem.equals("a")){
                holder.defect.setText("");
                defect_number = 0;
            }
            if(defectItem.equals("re")){
                holder.replace.setText("");
                replace_number = 0;
            }
        }

        sewingOutputModelClasses.get(position).setReject(reject_number);
        sewingOutputModelClasses.get(position).setAlter(defect_number);

        if (qc_qty>qty){
            replaceStatus = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.defect.getContext());
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
