package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;

import java.util.List;

public class V1_POSpinnerAdapter extends ArrayAdapter<V1_StyleWiseOperationResponse.PoId> {

    public V1_POSpinnerAdapter(Context context, List<V1_StyleWiseOperationResponse.PoId> posArrayList)
    {
        super(context, 0, posArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View itemView,
                          ViewGroup parent)
    {
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.sewing_po_item, parent, false);
        }

        TextView poNameTV;
        poNameTV = itemView.findViewById(R.id.poNameTV);
        V1_StyleWiseOperationResponse.PoId currentItem = getItem(position);

        if (currentItem != null) {
            poNameTV.setText(currentItem.getPoNumber());
        }
        return itemView;
    }
}