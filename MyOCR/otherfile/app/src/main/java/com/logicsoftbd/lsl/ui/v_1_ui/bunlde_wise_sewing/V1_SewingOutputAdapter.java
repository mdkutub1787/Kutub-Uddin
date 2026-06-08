package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModelClass;
//import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;


public class V1_SewingOutputAdapter extends ArrayAdapter<V1_SewingOutputModelClass>{
    ArrayList<V1_SewingOutputModelClass> sewingOutputModelClasses;
    LayoutInflater vi;
    private int Resource, qty = 0, qc_qty = 0, reject_number = 0, alter_number = 0, spot_number = 0, replace_number = 0;
    Handler handler = new Handler();
    Runnable runnable;

    public V1_SewingOutputAdapter(@NonNull Context context, int resource, ArrayList<V1_SewingOutputModelClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sewingOutputModelClasses = objects;
        Resource = resource;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 500;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        holder = new V1_SewingOutputAdapter.ViewHolder();
        qty = sewingOutputModelClasses.get(position).getQuantity();
        qc_qty = qty;

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.sewing_output_object_layout, null);
            holder.barcodeNumber = convertView.findViewById(R.id.barcodeTV);
            holder.bundleNotv = convertView.findViewById(R.id.bundleNoTV);
            holder.yeartv = convertView.findViewById(R.id.yearTV);
            holder.jobNotv = convertView.findViewById(R.id.jobNoTV);
            holder.buyertv = convertView.findViewById(R.id.buyerTV);
            holder.orderNotv = convertView.findViewById(R.id.orderNoTV);
            holder.gmtsItemtv = convertView.findViewById(R.id.gmtsTV);
            holder.countrytv = convertView.findViewById(R.id.countryTV);
            holder.colortv = convertView.findViewById(R.id.colorTV);
            holder.sizetv = convertView.findViewById(R.id.sizeTV);
            holder.qtytv = convertView.findViewById(R.id.qtyTV);

            holder.reject = convertView.findViewById(R.id.rejectET);
            holder.alter = convertView.findViewById(R.id.alterET);
            holder.spot = convertView.findViewById(R.id.spotET);
            holder.replace = convertView.findViewById(R.id.replaceET);
            holder.qty_qc = convertView.findViewById(R.id.Qc_qty_Tv);

            holder.reject_up = convertView.findViewById(R.id.rejectup);
            holder.reject_down = convertView.findViewById(R.id.rejectdown);

            holder.alter_up = convertView.findViewById(R.id.alterup);
            holder.alter_down = convertView.findViewById(R.id.alterdown);

            holder.spot_up = convertView.findViewById(R.id.spotup);
            holder.spot_down = convertView.findViewById(R.id.spotdown);

            holder.replace_up = convertView.findViewById(R.id.replaceup);
            holder.replace_down = convertView.findViewById(R.id.replacedown);
            holder.rejectTV = convertView.findViewById(R.id.rejectTV);

            /*reject pop up*/
//            this.mOnHeadListener = mOnHeadListener;
            holder.rejectTV.setOnClickListener((View.OnClickListener) this);

            holder.rejectLinearLayout = convertView.findViewById(R.id.popUpLinearLayout);
            holder.rejectGridView = convertView.findViewById(R.id.popUpGridView);
//            holder.rejectPopUpAdapter = new RejectPopUpAdapter(getContext(), 0, null);
            /*reject pop up*/
            buttonClickListener(holder, position);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        int i = position + 1;
        holder.barcodeNumber.setText(sewingOutputModelClasses.get(position).getBarcode_no());
        holder.bundleNotv.setText(sewingOutputModelClasses.get(position).getBundle_no());
        holder.yeartv.setText(sewingOutputModelClasses.get(position).getYearNo());
        holder.jobNotv.setText(sewingOutputModelClasses.get(position).getJobNo());
        holder.buyertv.setText(sewingOutputModelClasses.get(position).getBuyer());
        holder.orderNotv.setText(sewingOutputModelClasses.get(position).getOrderNo());
        holder.gmtsItemtv.setText(sewingOutputModelClasses.get(position).getItemNo());
        holder.countrytv.setText(sewingOutputModelClasses.get(position).getCountry());
        holder.colortv.setText(sewingOutputModelClasses.get(position).getColorNo());
        holder.sizetv.setText(sewingOutputModelClasses.get(position).getSizeNo());
        holder.qtytv.setText(String.valueOf(sewingOutputModelClasses.get(position).getQuantity()));
        holder.qty_qc.setText(String.valueOf(sewingOutputModelClasses.get(position).getQuantity()));
        sewingOutputModelClasses.get(position).setQc_qty(Integer.parseInt(holder.qty_qc.getText().toString()));
        return convertView;
    }

    private void buttonClickListener(final ViewHolder holder, final int position) {
        if(String.valueOf(sewingOutputModelClasses.get(position).getReplace_field_disable()).equals("1")){
            holder.replace_up.setEnabled(false);
            holder.replace_down.setEnabled(false);
        }

        holder.reject_up.setOnClickListener(v -> {
            reject_number = reject_number +  1;
            holder.reject.setText(String.valueOf(reject_number));
            calculateData(holder, position);
        });

        holder.replace_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.replace_up.isPressed()) return;
                    increaseValue(reject_number, holder.reject, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
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

            calculateData(holder, position);
        });

        holder.reject_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.reject_down.isPressed()) return;
                    decreaseValue(reject_number, holder.reject, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
            return true;
        });

        holder.alter_up.setOnClickListener(v -> {
            alter_number = alter_number +  1;
            holder.alter.setText(String.valueOf(alter_number));
            calculateData(holder, position);
        });

        holder.alter_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.alter_up.isPressed()) return;
                    increaseValue(alter_number, holder.alter, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
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

            calculateData(holder, position);
        });

        holder.alter_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.alter_down.isPressed()) return;
                    decreaseValue(alter_number, holder.alter, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
            return true;
        });

        holder.spot_up.setOnClickListener(v -> {
            spot_number = spot_number +  1;
            holder.spot.setText(String.valueOf(spot_number));
            calculateData(holder, position);
        });

        holder.spot_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.spot_up.isPressed()) return;
                    increaseValue(spot_number, holder.spot, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
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

            calculateData(holder, position);
        });

        holder.spot_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.spot_down.isPressed()) return;
                    decreaseValue(spot_number, holder.spot, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
            return true;
        });

        holder.replace_up.setOnClickListener(v -> {
            replace_number = replace_number +  1;
            holder.replace.setText(String.valueOf(replace_number));
            calculateData(holder, position);

        });

        holder.replace_up.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.replace_up.isPressed()) return;
                    increaseValue(replace_number, holder.replace, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
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

            calculateData(holder, position);
        });

        holder.replace_down.setOnLongClickListener(v -> {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!holder.replace_down.isPressed()) return;
                    decreaseValue(replace_number, holder.replace, holder, position);
                    handler.postDelayed(runnable, 10);
                }
            };

            handler.postDelayed(runnable, 10);
            return true;
        });
        
        holder.rejectTV.setOnClickListener(v -> Toast.makeText(getContext(), "Popup", Toast.LENGTH_SHORT).show());
        
        
    }

    private void increaseValue(int number, EditText editText, ViewHolder holder, int position) {
        number = number +  1;
        editText.setText(String.valueOf(number));
        calculateData(holder, position);
    }

    private void decreaseValue(int number, EditText editText, ViewHolder holder, int position) {
        number = number -  1;
        if(number >= 0){
            editText.setText(String.valueOf(number));
        }else {
            number = 0;
            editText.setText(String.valueOf(0));
        }
        calculateData(holder, position);
    }

    private void calculateData(ViewHolder holder, int position) {

        //int qc_qty = 0;
        int re = 0, al = 0, sp = 0, rep = 0;
        try {
            re = Integer.parseInt((holder.reject.getText().toString()));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        try {
            al = Integer.parseInt((holder.alter.getText().toString()));
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
        holder.qty_qc.setText(String.valueOf(qc_qty));



        if(qc_qty <= 0) {
            holder.qty_qc.setText(String.valueOf(qty));
            holder.reject.setText("");
            holder.alter.setText("");
            holder.spot.setText("");
            holder.replace.setText("");

            reject_number = 0;
            alter_number = 0;
            spot_number = 0;
            replace_number = 0;
        }

        if (qc_qty>qty){
//            TastyToast.makeText(getContext(), "QC pass quantity can't be higher than total quantity!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//            Toasty.error(getContext(), "QC pass quantity can't be higher than total quantity!", Toast.LENGTH_SHORT, true).show();

            Toast.makeText(getContext(), "QC pass quantity can't be higher than total quantity!", Toast.LENGTH_SHORT).show();


        }




        sewingOutputModelClasses.get(position).setReject(reject_number);
        sewingOutputModelClasses.get(position).setAlter(alter_number);
        sewingOutputModelClasses.get(position).setSpot(spot_number);
        sewingOutputModelClasses.get(position).setReplace(replace_number);
        sewingOutputModelClasses.get(position).setQc_qty(qc_qty);




    }


//    public class ViewHolder implements View.OnClickListener {
    public class ViewHolder {
        private TextView barcodeNumber, bundleNotv, yeartv, jobNotv, buyertv, orderNotv, gmtsItemtv, countrytv, colortv, sizetv, qtytv, qty_qc, rejectTV;
        private EditText reject, alter, spot, replace;
        private ImageButton reject_up, reject_down, alter_up, alter_down, spot_up, spot_down, replace_up, replace_down;
        /*reject pop up*/
        private LinearLayout rejectLinearLayout;
        private V1_RejectPopUpRecyclerAdapter rejectPopUpRecyclerAdapter;
        private GridView rejectGridView;

//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.rejectTV:
//                    mOnHeadListener.onHeadClick(v);
//                    break;
//            }
//        }
    }

    public interface OnHeadListener{
        void onHeadClick(View v);
    }

}
