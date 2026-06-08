package com.logicsoftbd.lsl.ui.v_1_ui.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseConfigResponse;

import java.util.List;

public class V1_CustomeStyleAdapter extends BaseAdapter {
    Context context;
    List<V1_StyleWiseConfigResponse.V1_DataItem> countryNames;
    LayoutInflater inflter;

    public V1_CustomeStyleAdapter(Context applicationContext, List<V1_StyleWiseConfigResponse.V1_DataItem> countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.style_wise_tab_config_item, null);
        TextView jobNoTV = view.findViewById(R.id.jobNoTV);
        TextView styleTV = view.findViewById(R.id.styleTV);
        TextView poNumberTV = view.findViewById(R.id.poNumberTV);
        TextView countryTV = view.findViewById(R.id.countryTV);
        jobNoTV.setText(countryNames.get(i).getStyleRefNo());
        styleTV.setText(countryNames.get(i).getPoNumber());
        poNumberTV.setText(countryNames.get(i).getJobNo());
        countryTV.setText(countryNames.get(i).getItemNumberId());
        return view;
    }
}