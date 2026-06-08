package com.logicsoftbd.lsl.ui.v_1_ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_FinishFabricObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_GreyObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_FinishFabricActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_GreyFabricActivity;

public class V1_HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private SessionManager session;

    private ImageButton scanButton;
    private EditText barcodeET;
    private  int userID = 0;
    private String urladdress;
    private String barcode;
    private DBAdapter dbAdapter;

    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //QC Entry Scan
    private String qc_entry;
    //Finish fabric
    private int finish_fabric_entry = 0, type = 0;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_home);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scanButton = findViewById(R.id.scanBtn);
        barcodeET = findViewById(R.id.barcodenumberText);
        scanButton.setOnClickListener(this);

        // SqLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        userID = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        qc_entry = intent.getStringExtra("qc");
        type = intent.getIntExtra("type", 0);

//        if(qc_entry.equals("line_wise_sewing_input_challan_wise")){
//            Intent intent1 = new Intent(this, V1_ChallanReportActivity.class);
//            startActivity(intent1);
//        }

        dbAdapter = new DBAdapter(this);


    }

    private void logoutUser() {
        session.setLogin(false);

        dbAdapter.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(V1_HomeActivity.this, V1_LoginActivity.class);
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
            case R.id.scanBtn:
                barcodeactionmethod();
                break;
            case R.id.toolbar:
                Intent intent = V1_MenuActivity.getStartIntent(V1_HomeActivity.this);
                startActivity(intent);
                finish();
        }
        //startActivity(new Intent(HomeActivity.this, QRBarcodeScannerActivity.class));

    }

    private void barcodeactionmethod() {
        barcode = barcodeET.getText().toString();
        if(!barcode.isEmpty())
        {
            if(qc_entry.equals("Grey_Fabric_QC_Entry_V1")) {
                Intent intent = new Intent(this, V1_GreyFabricActivity.class);
                intentDataToActivity(intent);
                startActivity(intent);
//                if(urladdress.equals("203.82.196.18/erp_test")){
//                    Intent intent = new Intent(this, V1_GreyObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }else if(urladdress.equals("203.82.196.18/erp")) {
//                    Intent intent = new Intent(this, V1_GreyObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//
//                }else if(urladdress.equals("192.168.21.240/erp_test")) {
//                    Intent intent = new Intent(this, V1_GreyObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//
//                }else if(urladdress.equals("192.168.21.240/erp")) {
//                    Intent intent = new Intent(this, V1_GreyObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//
//                }else {
//                    Intent intent = new Intent(this, V1_GreyFabricActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }

            } else if(qc_entry.equals("Grey_Fabric_With_Observation")){
                Intent intent = new Intent(this, V1_GreyObsActivity.class);
                intentDataToActivity(intent);
                startActivity(intent);
            } else if(qc_entry.equals("Finish_Fabric_QC_Entry")) {
                Intent intent = new Intent(this, V1_FinishFabricActivity.class);
                intentDataToActivity(intent);
                startActivity(intent);
//                if(urladdress.equals("203.82.196.18/erp_test")){
//                    Intent intent = new Intent(this, V1_FinishFabricObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }else if(urladdress.equals("203.82.196.18/erp")){
//                    Intent intent = new Intent(this, V1_FinishFabricObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }else if(urladdress.equals("192.168.21.240/erp_test")){
//                    Intent intent = new Intent(this, V1_FinishFabricObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }else if(urladdress.equals("192.168.21.240/erp")){
//                    Intent intent = new Intent(this, V1_FinishFabricObsActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }else {
//                    Intent intent = new Intent(this, V1_FinishFabricActivity.class);
//                    intentDataToActivity(intent);
//                    startActivity(intent);
//                }
            } else if(qc_entry.equals("Finish_Fabric_With_Observation")) {
                Intent intent = new Intent(this, V1_FinishFabricObsActivity.class);
                    intentDataToActivity(intent);
                    startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(this, V1_QRBarcodeScannerActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", savemenu);
            intent.putExtra("u", updatemenu);
            intent.putExtra("qc", qc_entry);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    private void intentDataToActivity(Intent intent) {
        intent.putExtra("result", barcode);
        intent.putExtra("url", urladdress);
        intent.putExtra("userId", userID);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        intent.putExtra("qc", qc_entry);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_HomeActivity.this, V1_MenuActivity.class);
        intent.putExtra("userId", userID);
        intent.putExtra("url", urladdress);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}