package com.logicsoftbd.lsl.ui.v_1_ui.base_url;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.BaseUrlDBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_BaseUrl;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;

import java.util.ArrayList;

public class BaseUrlActivity extends AppCompatActivity {
    private static final String TAG = "BaseUrlActivity";
    private EditText baseURLET;
    private TextView versionTV;
    private Button baseURLBtn;
    private String urladdress, version = "";
    private PackageInfo pInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_url);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        baseURLET = findViewById(R.id.baseURLET);
        baseURLBtn = findViewById(R.id.baseURLBtn);
        versionTV = findViewById(R.id.versionTV);

        try {
            pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            version = pInfo.versionName;
            versionTV = findViewById(R.id.versionTV);
            versionTV.setText("V"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<V1_BaseUrl> v1_baseUrls = new BaseUrlDBAdapter(this).getLoginData();
        if(v1_baseUrls.size() > 0){
            urladdress = v1_baseUrls.get(0).getUrl();
            baseURLET.setText(urladdress);
        }

        Log.d(TAG, "onCreate: Base url"+urladdress);

        baseURLBtn.setOnClickListener(v -> {
            String urlAddress = baseURLET.getText().toString().trim();
            String base_url_full;
            V1_BaseUrl l = new V1_BaseUrl();
            l.setUrl(urlAddress);

            BaseUrlDBAdapter dbAdapter = new BaseUrlDBAdapter(this);
            dbAdapter.deleteUsers();
            if(new BaseUrlDBAdapter(BaseUrlActivity.this).saveLoginData(l))
            {
                SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(BaseUrlActivity.this);
                SharedPreferences.Editor _editor = _preferences.edit();
                base_url_full = "http://"+urlAddress+"/logic-api/index.php/api/Android/";
                _editor.putString("base_url", base_url_full);
                _editor.commit();
                baseURLET.setText("");
//                startActivity(new Intent(BaseUrlActivity.this, V1_LoginActivity.class));
                showAlertBaseURLMessage("Smart Track Need to restart application");
            }
        });

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseUrlActivity.this, V1_LoginActivity.class));
                finish();
            }
        });
    }

    private void showAlertBaseURLMessage(String msg){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV, titileTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_update_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);
        titileTV = alertCustomDialog.findViewById(R.id.titileTV);
        titileTV.setText("Message");
        updateBtn.setText("Okay");

        messageTV.setText(msg);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            System.exit(0);
        });

        cancel.setOnClickListener( v -> {

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}