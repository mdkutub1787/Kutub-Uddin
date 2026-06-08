package com.kutub.insurance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kutub.insurance.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnPolicyList, btnBillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        btnPolicyList = findViewById(R.id.btnPolicyList);
        btnBillList = findViewById(R.id.btnBillList);


        // Set click listeners
        btnPolicyList.setOnClickListener(v -> {
            // Start PolicyActivity
            startActivity(new Intent(HomeActivity.this, PolicyActivity.class));
        });

        btnBillList.setOnClickListener(v -> {
            // Start BillActivity
            startActivity(new Intent(HomeActivity.this, BillActivity.class));
        });

    }
}