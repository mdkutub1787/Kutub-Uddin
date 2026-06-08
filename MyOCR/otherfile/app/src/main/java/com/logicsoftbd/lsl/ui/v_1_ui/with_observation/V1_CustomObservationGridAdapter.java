package com.logicsoftbd.lsl.ui.v_1_ui.with_observation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class V1_CustomObservationGridAdapter extends BaseAdapter {
    private Context context;
    private int number = 0;
    private int defectnumber = 0;
    public int holeCount = 0;
    private String defectItemName;
    private String qcItemName;
    public Map<Integer, Integer> selectedItems = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> selectedItemsdefect = new HashMap<Integer, Integer>();
    private V1_QcModelRND qcModelRND;


    public V1_CustomObservationGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return V1_GreyObsActivity.modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return V1_GreyObsActivity.modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_layout, null, false);



            holder.tvItemName = convertView.findViewById(R.id.itemName);
            holder.tvDefectnumber = convertView.findViewById(R.id.itemCount);
            holder.tvDefectCount = convertView.findViewById(R.id.defectCountTV);
            holder.tv = convertView.findViewById(R.id.itemCount);
            holder.btn_plus = convertView.findViewById(R.id.addBtn);
            holder.btn_minus = convertView.findViewById(R.id.removeBtn);
            holder.mSpinner = convertView.findViewById(R.id.spinner);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItemName.setText(V1_GreyObsActivity.modelArrayList.get(position).getQcItemName());
        holder.tvDefectnumber.setText(String.valueOf(V1_GreyObsActivity.modelArrayList.get(position).getQcItemNumber()));


        if(position == 0){
            holder.btn_plus.setTag(R.integer.btn_plus_view, convertView);
            holder.btn_plus.setTag(R.integer.btn_plus_pos, position);
            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View tempview = (View) holder.btn_plus.getTag(R.integer.btn_plus_view);
                    Integer pos = (Integer) holder.btn_plus.getTag(R.integer.btn_plus_pos);

                    number = Integer.parseInt(holder.tv.getText().toString()) + 1;

                    if(number <= 0) {
                        colorBlack(holder);
                    }
                    else if(number >= 0){
                        colorRed(holder);
                    }
                    else {
                        colorBlack(holder);
                    }
                    holder.tv.setText(String.valueOf(number));
                    defectCountNumber(holder, holeCount);
                    V1_GreyObsActivity.modelArrayList.get(pos).setQcItemNumber(number);
                }

                private void defectCountNumber(ViewHolder holder, int holeCount) {
                    if(holeCount == 0)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 1)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 2)
                    {
                        holeDefectCountNumberCalculation();
                    }
                }

                private void holeDefectCountNumberCalculation() {
                    int spinnerItem = holder.mSpinner.getSelectedItemPosition();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*spinnerItem*2;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    calculationOfDefect();

                }
            });
            holder.btn_minus.setTag(R.integer.btn_minus_view, convertView);
            holder.btn_minus.setTag(R.integer.btn_minus_pos, position);
            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View tempview = (View) holder.btn_minus.getTag(R.integer.btn_minus_view);
                    Integer pos = (Integer) holder.btn_minus.getTag(R.integer.btn_minus_pos);

                    number = Integer.parseInt(holder.tv.getText().toString()) - 1;
                    if(number >= 0)
                    {
                        holder.tv.setText(String.valueOf(number).trim());
                    }
                    else {
                        number = 0;
                    }
                    if(number == 0)
                    {
                        colorBlackNegative(holder);
                    }

                    holder.tv.setText(String.valueOf(number));
                    defectCountNumber(holder, holeCount);
                    V1_GreyObsActivity.modelArrayList.get(pos).setQcItemNumber(number);
                }

                private void defectCountNumber(ViewHolder holder, int holeCount) {
                    if(holeCount == 0)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 1)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 2)
                    {
                        holeDefectCountNumberCalculation();
                    }
                }
                private void holeDefectCountNumberCalculation() {
                    int spinnerItem = holder.mSpinner.getSelectedItemPosition();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*spinnerItem*2;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    calculationOfDefect();
                }

            });
        }
        else {
            holder.btn_plus.setTag(R.integer.btn_plus_view, convertView);
            holder.btn_plus.setTag(R.integer.btn_plus_pos, position);
            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View tempview = (View) holder.btn_plus.getTag(R.integer.btn_plus_view);
                    Integer pos = (Integer) holder.btn_plus.getTag(R.integer.btn_plus_pos);

                    number = Integer.parseInt(holder.tv.getText().toString()) + 1;

                    if(number <= 0) {
                        colorBlack(holder);
                    }
                    else if(number >= 0){
                        colorRed(holder);
                    }
                    else {
                        colorBlack(holder);
                    }
                    holder.tv.setText(String.valueOf(number));
                    defectCountNumber(holder, holeCount);
                    V1_GreyObsActivity.modelArrayList.get(pos).setQcItemNumber(number);
                }

                private void defectCountNumber(ViewHolder holder, int holeCount) {
                    if(holeCount == 0)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 1)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 2)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 3)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 4)
                    {
                        holeDefectCountNumberCalculation();
                    }
                }

                private void holeDefectCountNumberCalculation() {
                    int spinnerItem = holder.mSpinner.getSelectedItemPosition();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*spinnerItem;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    calculationOfDefect();

                }
            });
            holder.btn_minus.setTag(R.integer.btn_minus_view, convertView);
            holder.btn_minus.setTag(R.integer.btn_minus_pos, position);
            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View tempview = (View) holder.btn_minus.getTag(R.integer.btn_minus_view);
                    Integer pos = (Integer) holder.btn_minus.getTag(R.integer.btn_minus_pos);

                    number = Integer.parseInt(holder.tv.getText().toString()) - 1;
                    if(number >= 0)
                    {
                        holder.tv.setText(String.valueOf(number).trim());
                    }
                    else {
                        number = 0;
                    }
                    if(number == 0)
                    {
                        colorBlackNegative(holder);
                    }

                    holder.tv.setText(String.valueOf(number));
                    defectCountNumber(holder, holeCount);
                    V1_GreyObsActivity.modelArrayList.get(pos).setQcItemNumber(number);
                }

                private void defectCountNumber(ViewHolder holder, int holeCount) {
                    if(holeCount == 0)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 1)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 2)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 3)
                    {
                        holeDefectCountNumberCalculation();
                    }
                    else if(holeCount == 4)
                    {
                        holeDefectCountNumberCalculation();
                    }
                }
                private void holeDefectCountNumberCalculation() {
                    int spinnerItem = holder.mSpinner.getSelectedItemPosition();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*spinnerItem;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    calculationOfDefect();
                }

            });
        }



        if(position == 0){
            ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.hole_in_inch
                    , R.layout.spinner_item);
            holder.mSpinner.setAdapter(adapterhole);

            if(selectedItems.get(position) != null){
                holder.mSpinner.setSelection(selectedItems.get(position));
            }

             int d = V1_GreyObsActivity.modelArrayList.get(position).getSpinneritem();

            if(d>0 ){
                if(d == 0)
                {
                    selectedItems.put(position, 0);
                }
                else if(d == 5)
                {
                    selectedItems.put(position, 1);
                }
                else
                {
                    selectedItems.put(position, 2);
                }
            }

            holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {
                    holeCount = holder.mSpinner.getSelectedItemPosition();
                    switch (selectedIndex){
                        case 0:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                        case 1:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder, selectedIndex*2);
                            defectInch(holder,selectedIndex+4);
                            break;
                        case 2:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder, selectedIndex*2);
                            defectInch(holder,selectedIndex+4);
                            break;
                    }
                }

                private void defectInch(ViewHolder holder, int selectedIndex) {
                    V1_GreyObsActivity.modelArrayList.get(position).setSpinneritem(selectedIndex);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

                private void defectCountNumber(ViewHolder holder, int selectedIndex) {
                    defectItemName = holder.mSpinner.getSelectedItem().toString();
                    qcItemName = holder.tvItemName.getText().toString();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*selectedIndex;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    V1_GreyObsActivity.modelArrayList.get(position).setQcItemName(qcItemName);
                    calculationOfDefect();
                }
            });
        }
        else{
            ArrayAdapter<CharSequence> adapterdefect = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.defect_in_inch
                    , R.layout.spinner_item);
            holder.mSpinner.setAdapter(adapterdefect);

            if(selectedItems.get(position) != null){
                holder.mSpinner.setSelection(selectedItems.get(position));
            }
            int d = V1_GreyObsActivity.modelArrayList.get(position).getSpinneritem();
            holder.mSpinner.setSelection(d);


            holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                    holeCount = holder.mSpinner.getSelectedItemPosition();

                    switch (selectedIndex){
                        case 0:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                        case 1:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                        case 2:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                        case 3:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                        case 4:
                            selectedItems.put(position, selectedIndex);
                            defectCountNumber(holder,selectedIndex);
                            defectInch(holder,selectedIndex);
                            break;
                    }
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                }

                private void defectInch(ViewHolder holder, int selectedIndex) {
                    V1_GreyObsActivity.modelArrayList.get(position).setSpinneritem(selectedIndex);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
                private void defectCountNumber(ViewHolder holder, int selectedIndex) {
                    defectItemName = holder.mSpinner.getSelectedItem().toString();
                    qcItemName = holder.tvItemName.getText().toString();
                    int numberItem = Integer.parseInt(holder.tvDefectnumber.getText().toString());
                    defectnumber = numberItem*selectedIndex;
                    textColorChange(defectnumber, holder);
                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                    V1_GreyObsActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
                    V1_GreyObsActivity.modelArrayList.get(position).setQcItemName(qcItemName);
                    calculationOfDefect();

                }
            });
        }

        return convertView;
    }


    public void calculationOfDefect() {


        double inch = 0;

        inch = ((V1_GreyObsActivity)context).roll_inch;


        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < ((V1_GreyObsActivity)context).gradeArray.length; i++) {
            list.add(V1_GreyObsActivity.modelArrayList.get(i).getQcDefectNumber());
        }

        int sum = 0;
        for (int i : list)
        {
            sum += i;
        }

        double totalPanalty = 0;
        totalPanalty = ((36*100*sum) / (inch * (((V1_GreyObsActivity)context).yds)));

        if(sum == 0){
            ((V1_GreyObsActivity)context).tvtotalPennalty.setTextColor(Color.BLACK);
            ((V1_GreyObsActivity)context).tvpoint.setTextColor(Color.BLACK);

        }
        else {
            ((V1_GreyObsActivity)context).tvtotalPennalty.setTextColor(Color.RED);
            ((V1_GreyObsActivity)context).tvpoint.setTextColor(Color.RED);

        }
        ((V1_GreyObsActivity)context).tvtotalPennalty.findViewById(R.id.penaltyPointTV);
        ((V1_GreyObsActivity)context).tvtotalPennalty.setText(String.valueOf(sum));
        if((((V1_GreyObsActivity)context).yds) == 0){
            ((V1_GreyObsActivity)context).tvpoint.setText("0.00");
        }



        ((V1_GreyObsActivity)context).calculationYds();
        qcModelRND = new V1_QcModelRND();
        qcModelRND.setTotalPoint(totalPanalty);



        //fabricGradeMethod();
        ((V1_GreyObsActivity)context).tvfebricGrade.findViewById(R.id.febricGradeTV);

    }



    private void colorBlackNegative(ViewHolder holder) {

    }

    private void colorRed(ViewHolder holder) {

    }

    private void colorBlack(ViewHolder holder) {
       /* holder.tv.setTextColor(Color.BLACK);
        holder.tvItemName.setTextColor(Color.BLACK);
        holder.tvItemName.setBackgroundColor(Color.WHITE);*/
    }

    private void textColorChange(int defectnumber, ViewHolder holder) {
        if(defectnumber == 0){
            holder.tvDefectCount.setTextColor(Color.BLACK);
            holder.tvDefectnumber.setTextColor(Color.BLACK);
            holder.tvItemName.setTextColor(Color.BLACK);
        }
        else {
            holder.tvDefectCount.setTextColor(Color.RED);
            holder.tvDefectCount.setBackgroundColor(Color.CYAN);
            holder.tvDefectnumber.setTextColor(Color.BLUE);
            holder.tvItemName.setTextColor(Color.BLUE);

        }
    }

    public class ViewHolder {
        protected Button btn_plus, btn_minus;
        public TextView tvItemName, tvDefectnumber, tvDefectCount, tv;
        public TextView totalPenaltyPOintTV, totalPoint;
        public Spinner mSpinner;
    }
}
