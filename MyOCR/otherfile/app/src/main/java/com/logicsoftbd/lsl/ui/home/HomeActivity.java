package com.logicsoftbd.lsl.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.ui.login.LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;


public class HomeActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.v1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.getStartIntent(HomeActivity.this);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.v2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = V1_LoginActivity.getStartIntent(HomeActivity.this);
//                startActivity(intent);
//                finish();

                Intent intent = V1_MenuActivity.getStartIntent(HomeActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }
}