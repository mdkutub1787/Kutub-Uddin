package com.logicsoftbd.lsl.ui.process.greyroll.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.ui.base.BaseDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HourDialog extends BaseDialog {

    @BindView(R.id.edit_text_hour)
    EditText editTextHour;

    @BindView(R.id.edit_text_minute)
    EditText editTextMinute;

    private int hour = 0;
    private int minute = 0;
    Calendar rightNow;
    private static  OnTimeListener mTimeListener;
    private static final String TAG = "HourDialog";
    public interface OnTimeListener{
        void onTimeSubmit(String hour, String minute);
    }
    public static HourDialog newInstance(Context context, OnTimeListener timeListener) {
        HourDialog fragment = new HourDialog();
        mTimeListener = timeListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hour_dialog, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setUp(View view) {
        editTextHour.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        editTextMinute.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);

        editTextHour.setText(String.format("%02d", hour));
        editTextMinute.setText(String.format("%02d", minute));
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    public void dismissDialog() {
        super.dismissDialog(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        mTimeListener.onTimeSubmit(String.format("%02d",Integer.parseInt(editTextHour.getText().toString())),
                String.format("%02d",Integer.parseInt(editTextMinute.getText().toString())));
        dismissDialog();

    }

    @OnClick(R.id.button_up_hour)
    void buttonUpHour() {
        if(!editTextHour.getText().toString().isEmpty()) {
            hour = Integer.parseInt(editTextHour.getText().toString());
        }
        hourIncrease();
        updateHour();
    }



    @OnClick(R.id.button_down_hour)
    void buttonDownHour() {
        if(!editTextHour.getText().toString().isEmpty()) {
            hour = Integer.parseInt(editTextHour.getText().toString());
        }
       hourDecrease();
        updateHour();
    }

    @OnClick(R.id.button_up_minute)
    void buttonUpMinute() {
        if(!editTextMinute.getText().toString().isEmpty()) {
            minute = Integer.parseInt(editTextMinute.getText().toString());
        }
       minuteIncrease();
        updateMinute();
    }

    @OnClick(R.id.button_down_minute)
    void buttonDownMinute() {
        if(!editTextMinute.getText().toString().isEmpty()) {
            minute = Integer.parseInt(editTextMinute.getText().toString());
        }
       minuteDecrease();
        updateMinute();
    }

    private void updateHour() {
        if(hour >= 0 && hour <24) {
            editTextHour.setText(String.format("%02d",hour ));
        }

    }

    private void updateMinute() {
        if(minute >= 0 && minute <60) {
            editTextMinute.setText(String.format("%02d",minute ));
        }
    }

    public void hourIncrease() {
        if(hour==23)
            hour = -1;
        hour++;
    }

    public void hourDecrease () {
        if(hour==0)
            hour = 24;
        hour--;
    }

    public void minuteIncrease() {
        if(minute==59)
            minute = -1;
        minute++;
    }

    public void minuteDecrease () {
        if(minute==0)
            minute = 60;
        minute--;
    }

    @OnClick(R.id.btn_later)
    void onLater() {
        dismissDialog();
    }
}
