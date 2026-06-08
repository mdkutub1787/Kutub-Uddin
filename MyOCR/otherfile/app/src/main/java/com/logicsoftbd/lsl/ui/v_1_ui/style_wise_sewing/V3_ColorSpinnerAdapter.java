package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
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

public class V3_ColorSpinnerAdapter extends ArrayAdapter<V1_StyleWiseOperationResponse.Color> {

    public V3_ColorSpinnerAdapter(Context context, List<V1_StyleWiseOperationResponse.Color> colorArrayList)
    {
        super(context, 0, colorArrayList);
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
        // It is used to set our custom view.
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.sewing_color_item, parent, false);
        }

        TextView colorNameTV, colorBadgeTV;
        colorNameTV = itemView.findViewById(R.id.colorNameTV);
        colorBadgeTV = itemView.findViewById(R.id.colorBadgeTV);
        V1_StyleWiseOperationResponse.Color currentItem = getItem(position);

        if (currentItem != null) {
            colorNameTV.setText(currentItem.getColourName());
            colorBadgeTV.setText(currentItem.getColor_output_qnty());
            setTextViewGradientColor(colorBadgeTV);
        }
        return itemView;
    }
    private void setTextViewGradientColor(TextView textView) {
        Shader shader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                new int[]{Color.BLUE,  Color.parseColor("#810366")},
                null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        textView.getPaint().setShader(shader);
    }
}