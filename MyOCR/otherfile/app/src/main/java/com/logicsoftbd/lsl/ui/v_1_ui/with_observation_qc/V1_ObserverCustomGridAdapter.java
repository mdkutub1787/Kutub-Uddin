package com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.logicsoftbd.lsl.R;

import java.util.HashMap;
import java.util.Map;

public class V1_ObserverCustomGridAdapter extends BaseAdapter {
    private Context context;
    public int holeCount = 0;
    public Map<Integer, Integer> selectedItemsInch = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> selectedItemsDepartment = new HashMap<Integer, Integer>();

    public V1_ObserverCustomGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return V1_GreyObsActivity.observationmodelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return V1_GreyObsActivity.observationmodelArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.observation_grid_layout, null, true);


            holder.mDefectName = convertView.findViewById(R.id.obs_defectName);
            holder.mFouncInchSpinner = convertView.findViewById(R.id.obs_founc_inInch);
            holder.mDepartmentSpinner = convertView.findViewById(R.id.obs_departmant);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDefectName.setText(V1_GreyObsActivity.observationmodelArrayList.get(position).getObservationDefectName());



//        if(position == 0){
//            ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.obs_found_inch
//                    , R.layout.spinner_item);
//            holder.mFouncInchSpinner.setAdapter(adapterhole);
//
//            if(selectedItemsInch.get(position) != null){
//                holder.mFouncInchSpinner.setSelection(selectedItemsInch.get(position));
//            }
//
//             int d = GreyObsActivity.observationmodelArrayList.get(position).getObservationInchSpinner();
//
//            if(d>0 ){
//                if(d == 0)
//                {
//                    selectedItemsInch.put(position, 0);
//                }
//                else if(d == 1)
//                {
//                    selectedItemsInch.put(position, 1);
//                }
//                else if(d == 2)
//                {
//                    selectedItemsInch.put(position, 2);
//                }
//                else if(d == 3)
//                {
//                    selectedItemsInch.put(position, 3);
//                }
//                else if(d == 4)
//                {
//                    selectedItemsInch.put(position, 4);
//                }
//                else if(d == 5)
//                {
//                    selectedItemsInch.put(position, 5);
//                }
//                else
//                {
//                    selectedItemsInch.put(position, 6);
//                }
//            }
//
//            holder.mFouncInchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {
//                    holeCount = holder.mFouncInchSpinner.getSelectedItemPosition();
//                    selectedItemsInch.put(position, selectedIndex);
//                    defectInch(holder,selectedIndex);
////                    switch (selectedIndex){
////                        case 0:
////                            selectedItems.put(position, selectedIndex);
////                            defectInch(holder,selectedIndex);
////                            break;
////                        case 1:
////                            selectedItems.put(position, selectedIndex);
////                            defectInch(holder,selectedIndex);
////                            break;
////                        case 2:
////                            selectedItems.put(position, selectedIndex);
////                            defectInch(holder,selectedIndex);
////                            break;
////                    }
//                }
//
//                private void defectInch(ViewHolder holder, int selectedIndex) {
//                    GreyObsActivity.observationmodelArrayList.get(position).setObservationInchSpinner(selectedIndex);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }

            ArrayAdapter<CharSequence> adapterdefectInch = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.obs_found_inch
                    , R.layout.spinner_item);
            holder.mFouncInchSpinner.setAdapter(adapterdefectInch);

            if(selectedItemsInch.get(position) != null){
                holder.mFouncInchSpinner.setSelection(selectedItemsInch.get(position));
            }
            int inch = V1_GreyObsActivity.observationmodelArrayList.get(position).getObservationInchSpinner();
            holder.mFouncInchSpinner.setSelection(inch);


            holder.mFouncInchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                    holeCount = holder.mFouncInchSpinner.getSelectedItemPosition();

                    holeCount = holder.mFouncInchSpinner.getSelectedItemPosition();
                    selectedItemsInch.put(position, selectedIndex);
                    defectInch(holder,selectedIndex);
//                    switch (selectedIndex){
//                        case 0:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 1:
//                            selectedItemsInch.put(position, selectedIndex);
//                           // defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 2:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 3:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 4:
//                            selectedItemsInch.put(position, selectedIndex);
//                           // defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                    }
                    //holder.tvDefectCount.setText(String.valueOf(defectnumber));
                }

                private void defectInch(ViewHolder holder, int selectedIndex) {
                    V1_GreyObsActivity.observationmodelArrayList.get(position).setObservationInchSpinner(selectedIndex);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ArrayAdapter<CharSequence> adapterdefectDepartment = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.obs_department
                    , R.layout.spinner_item);
            holder.mDepartmentSpinner.setAdapter(adapterdefectDepartment);

            if(selectedItemsDepartment.get(position) != null){
                holder.mDepartmentSpinner.setSelection(selectedItemsDepartment.get(position));
            }
            int department = V1_GreyObsActivity.observationmodelArrayList.get(position).getObservationDepartmentSpinner();
            holder.mDepartmentSpinner.setSelection(department);


            holder.mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                    holeCount = holder.mDepartmentSpinner.getSelectedItemPosition();
                    selectedItemsDepartment.put(position, selectedIndex);
                    defectInch(holder,selectedIndex);

//                    switch (selectedIndex){
//                        case 0:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 1:
//                            selectedItemsInch.put(position, selectedIndex);
//                            // defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 2:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 3:
//                            selectedItemsInch.put(position, selectedIndex);
//                            //defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                        case 4:
//                            selectedItemsInch.put(position, selectedIndex);
//                            // defectCountNumber(holder,selectedIndex);
//                            defectInch(holder,selectedIndex);
//                            break;
//                    }
//                    holder.tvDefectCount.setText(String.valueOf(defectnumber));
                }

                private void defectInch(ViewHolder holder, int selectedIndex) {
                    V1_GreyObsActivity.observationmodelArrayList.get(position).setObservationDepartmentSpinner(selectedIndex);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        return convertView;
    }

    public class ViewHolder {
        public TextView mDefectName;
        public Spinner mFouncInchSpinner, mDepartmentSpinner;
    }
}
