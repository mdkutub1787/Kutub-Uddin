package com.logicsoftbd.lsl.ui.v_1_ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.manager.SysPrefManager;

public class V1_SettingsActivity extends AppCompatActivity {
    private static final String TAG = "V1_SettingsActivity";

    private Context _ctx;
    private Dialog mCustomDialog;
    private SwitchCompat mSoundCheckBox,mVibrationCheckBox,mMusicCheckBox;
    private TextView ok_btn;
    private boolean isSoundOn;
    private boolean isVibrationOn;
    private boolean isMusicOn;
    private SysPrefManager sysPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.activity_settings);
        _ctx = getApplicationContext();
        sysPrefManager = new SysPrefManager(_ctx);

        initViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
        mSoundCheckBox = findViewById(R.id.sound_checkbox);
        mVibrationCheckBox = findViewById(R.id.vibration_checkbox);
        ok_btn= findViewById(R.id.ok);
        populateSoundContents();
        populateVibrationContents();
        ok_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });
    }

    private void switchSoundCheckbox() {
        isSoundOn = !isSoundOn;
        sysPrefManager.setBool("sound", isSoundOn);
        populateSoundContents();
    }

    private void switchVibrationCheckbox() {
        isVibrationOn = !isVibrationOn;
        sysPrefManager.setBool("vibrate", isVibrationOn);
        populateVibrationContents();
    }
    protected void populateSoundContents() {
        mSoundCheckBox.setChecked(sysPrefManager.getBool("sound"));
        isSoundOn = sysPrefManager.getBool("sound");
    }
    protected void populateVibrationContents() {
        mVibrationCheckBox.setChecked(sysPrefManager.getBool("vibrate"));
        isVibrationOn = sysPrefManager.getBool("vibrate");
    }
    public void viewClickHandler(View view) {
        switch (view.getId()) {
            case R.id.sound_layout:
                switchSoundCheckbox();
                break;
            case R.id.sound_checkbox:
                switchSoundCheckbox();
                break;
            case R.id.vibration_layout:
                switchVibrationCheckbox();
                break;
            case R.id.vibration_checkbox:
                switchVibrationCheckbox();
                break;
            case R.id.ok:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        overridePendingTransition(R.anim.close_next, R.anim.open_next);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {

        if (_ctx != null) {
            if (mCustomDialog != null) {
                mCustomDialog.dismiss();
                mCustomDialog = null;
            }
            mVibrationCheckBox = null;
            mMusicCheckBox = null;
            mSoundCheckBox = null;
            _ctx = null;
            super.onDestroy();
        }
    }


}
