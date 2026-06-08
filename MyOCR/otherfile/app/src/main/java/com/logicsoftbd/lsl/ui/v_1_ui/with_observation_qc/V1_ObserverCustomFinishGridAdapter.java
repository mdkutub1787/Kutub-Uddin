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

public class V1_ObserverCustomFinishGridAdapter extends BaseAdapter {
    private Context context;
    public int holeCount = 0;
    public Map<Integer, Integer> selectedItemsInch = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> selectedItemsDepartment = new HashMap<Integer, Integer>();

    public V1_ObserverCustomFinishGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return V1_FinishFabricObsActivity.observationmodelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return V1_FinishFabricObsActivity.observationmodelArrayList.get(position);
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
        final V1_ObserverCustomFinishGridAdapter.ViewHolder holder;

        if(convertView == null)
        {
            holder = new V1_ObserverCustomFinishGridAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.observation_grid_layout, null, true);


            holder.mDefectName = convertView.findViewById(R.id.obs_defectName);
            holder.mFouncInchSpinner = convertView.findViewById(R.id.obs_founc_inInch);
            holder.mDepartmentSpinner = convertView.findViewById(R.id.obs_departmant);

            convertView.setTag(holder);
        }else {
            holder = (V1_ObserverCustomFinishGridAdapter.ViewHolder) convertView.getTag();
        }

        holder.mDefectName.setText(V1_FinishFabricObsActivity.observationmodelArrayList.get(position).getObservationDefectName());


        ArrayAdapter<CharSequence> adapterdefectInch = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.obs_found_inch
                , R.layout.spinner_item);

//        ArrayAdapter<CharSequence> adapterdefectInch = ArrayAdapter.createFromResource(context.getApplicationContext(),R.array.obs_found_inch
//                , R.layout.spinner_item);
        //holder.mFouncInchSpinner.setAdapter(adapterdefectInch);
        holder.mFouncInchSpinner.setAdapter(new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line
        , V1_FinishFabricObsActivity.obsDefectValueArray));

        if(selectedItemsInch.get(position) != null){
            holder.mFouncInchSpinner.setSelection(selectedItemsInch.get(position));
        }

        int inch = V1_FinishFabricObsActivity.observationmodelArrayList.get(position).getObservationInchSpinner();
        holder.mFouncInchSpinner.setSelection(inch);


        holder.mFouncInchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                holeCount = holder.mFouncInchSpinner.getSelectedItemPosition();

                holeCount = holder.mFouncInchSpinner.getSelectedItemPosition();




                selectedItemsInch.put(position, selectedIndex);
                defectInch(holder, selectedIndex);
            }

            private void defectInch(V1_ObserverCustomFinishGridAdapter.ViewHolder holder, int selectedIndex) {
                V1_FinishFabricObsActivity.observationmodelArrayList.get(position).setObservationInchSpinner(selectedIndex);
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
        int department = V1_FinishFabricObsActivity.observationmodelArrayList.get(position).getObservationDepartmentSpinner();
        holder.mDepartmentSpinner.setSelection(department);


        holder.mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                holeCount = holder.mDepartmentSpinner.getSelectedItemPosition();
                selectedItemsDepartment.put(position, selectedIndex);
                defectInch(holder,selectedIndex);

            }

            private void defectInch(V1_ObserverCustomFinishGridAdapter.ViewHolder holder, int selectedIndex) {
                V1_FinishFabricObsActivity.observationmodelArrayList.get(position).setObservationDepartmentSpinner(selectedIndex);
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
