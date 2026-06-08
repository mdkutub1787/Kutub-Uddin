package com.logicsoftbd.lsl.ui.v_1_ui.linking;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LinkingOutputModel;

import java.util.ArrayList;

public class V1_LinkingOutputAdapter extends ArrayAdapter<V1_LinkingOutputModel> {

    ArrayList<V1_LinkingOutputModel> lineSewingOutputModelClasses;
    LayoutInflater vi;
    private int Resource, qty = 0, qc_qty = 0, reject_number = 0, alter_number = 0, spot_number = 0, replace_number = 0;

    public V1_LinkingOutputAdapter(@NonNull Context context, int resource, ArrayList<V1_LinkingOutputModel> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lineSewingOutputModelClasses = objects;
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

        holder = new V1_LinkingOutputAdapter.ViewHolder();
        qty = lineSewingOutputModelClasses.get(position).getQuantity();
        qc_qty = qty;

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.linking_output_object_layout, null);

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

            buttonClickListener(holder, position);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        int i = position + 1;

        holder.barcodeNumber.setText(lineSewingOutputModelClasses.get(position).getBarcode_no());
        holder.bundleNotv.setText(lineSewingOutputModelClasses.get(position).getBundle_no());
        holder.yeartv.setText(lineSewingOutputModelClasses.get(position).getYearNo());
        holder.jobNotv.setText(lineSewingOutputModelClasses.get(position).getJobNo());
        holder.buyertv.setText(lineSewingOutputModelClasses.get(position).getBuyer());
        holder.orderNotv.setText(lineSewingOutputModelClasses.get(position).getOrderNo());
        holder.gmtsItemtv.setText(lineSewingOutputModelClasses.get(position).getItemNo());
        holder.countrytv.setText(lineSewingOutputModelClasses.get(position).getCountry());
        holder.colortv.setText(lineSewingOutputModelClasses.get(position).getColorNo());
        holder.sizetv.setText(lineSewingOutputModelClasses.get(position).getSizeNo());
        holder.qtytv.setText(String.valueOf(lineSewingOutputModelClasses.get(position).getQuantity()));
        holder.qty_qc.setText(String.valueOf(lineSewingOutputModelClasses.get(position).getQuantity()));

        lineSewingOutputModelClasses.get(position).setQc_qty(Integer.parseInt(holder.qty_qc.getText().toString()));

        return convertView;
    }

    private void buttonClickListener(final ViewHolder holder, final int position) {


        holder.reject_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject_number = reject_number +  1;
                holder.reject.setText(String.valueOf(reject_number));
                calculateData(holder, position);
            }
        });

        holder.reject_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject_number = reject_number -  1;
                if(reject_number >= 0){
                    holder.reject.setText(String.valueOf(reject_number));
                }else {
                    reject_number = 0;
                    holder.reject.setText(String.valueOf(0));
                }

                calculateData(holder, position);
            }
        });

        holder.alter_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_number = alter_number +  1;
                holder.alter.setText(String.valueOf(alter_number));
                calculateData(holder, position);
            }
        });

        holder.alter_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_number = alter_number -  1;
                if(alter_number >= 0){
                    holder.alter.setText(String.valueOf(alter_number));
                }else {
                    alter_number = 0;
                    holder.alter.setText(String.valueOf(0));
                }

                calculateData(holder, position);
            }
        });

        holder.spot_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spot_number = spot_number +  1;
                holder.spot.setText(String.valueOf(spot_number));
                calculateData(holder, position);
            }
        });

        holder.spot_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spot_number = spot_number -  1;
                if(spot_number >= 0){
                    holder.spot.setText(String.valueOf(spot_number));
                }else {
                    spot_number = 0;
                    holder.spot.setText(String.valueOf(0));
                }

                calculateData(holder, position);
            }
        });

        holder.replace_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace_number = replace_number +  1;
                holder.replace.setText(String.valueOf(replace_number));
                calculateData(holder, position);
            }
        });

        holder.replace_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace_number = replace_number -  1;
                if(replace_number >= 0){
                    holder.replace.setText(String.valueOf(replace_number));
                }else {
                    replace_number = 0;
                    holder.replace.setText(String.valueOf(0));
                }

                calculateData(holder, position);
            }
        });




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

        qc_qty = qty - (re + al + sp + rep);
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

        lineSewingOutputModelClasses.get(position).setReject(reject_number);
        lineSewingOutputModelClasses.get(position).setAlter(alter_number);
        lineSewingOutputModelClasses.get(position).setSpot(spot_number);
        lineSewingOutputModelClasses.get(position).setReplace(replace_number);
        lineSewingOutputModelClasses.get(position).setQc_qty(qc_qty);

    }

    public class ViewHolder {
        private TextView barcodeNumber, bundleNotv, yeartv, jobNotv, buyertv, orderNotv, gmtsItemtv, countrytv, colortv, sizetv, qtytv, qty_qc, reject, alter, spot, replace;
        private ImageButton reject_up, reject_down, alter_up, alter_down, spot_up, spot_down, replace_up, replace_down;
    }
}
