package com.logicsoftbd.lsl.ui.v_1_ui.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;

public class V1_GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_guest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_GuestActivity.this, V1_LoginActivity.class);
        startActivity(intent);
        finish();
    }
}