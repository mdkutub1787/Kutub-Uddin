package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ReportQRBarcodeScannerActivity;

public class V1_ReportHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionManager session;

    private ImageButton scanButton;
    private EditText barcodeET;
    private  int userID = 0, status = 0;
    private String urladdress, barcode;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_report_home);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scanButton = findViewById(R.id.report_scanBtn);
        barcodeET = findViewById(R.id.report_barcodenumberText);
        scanButton.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        userID = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");

        dbAdapter = new DBAdapter(this);


    }

    private void logoutUser() {
        session.setLogin(false);

        dbAdapter.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(V1_ReportHomeActivity.this, V1_LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                logoutUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.report_scanBtn:
                barcodeactionmethod();

                break;
        }
        //startActivity(new Intent(HomeActivity.this, QRBarcodeScannerActivity.class));

    }

    private void barcodeactionmethod() {
        barcode = barcodeET.getText().toString();
        if(!barcode.isEmpty())
        {
            if(status == 1){
                Intent intent = new Intent(this, V1_QcReportActivity.class);
                intent.putExtra("result", barcode);
                intent.putExtra("status", status);
                intent.putExtra("url", urladdress);
                intent.putExtra("userId", userID);
                startActivity(intent);
            }else if(status == 2){
                Intent intent = new Intent(this, V1_BundleTrackingReportActivity.class);
                intent.putExtra("result", barcode);
                intent.putExtra("status", status);
                intent.putExtra("url", urladdress);
                intent.putExtra("userId", userID);
                startActivity(intent);
            }

        }
        else {
            Intent intent = new Intent(this, V1_ReportQRBarcodeScannerActivity.class);
            intent.putExtra("status", status);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_ReportHomeActivity.this, V1_MenuActivity.class);
        intent.putExtra("userId", userID);
        intent.putExtra("url", urladdress);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}