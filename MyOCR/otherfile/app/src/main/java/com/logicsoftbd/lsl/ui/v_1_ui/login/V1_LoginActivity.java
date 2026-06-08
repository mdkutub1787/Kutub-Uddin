package com.logicsoftbd.lsl.ui.v_1_ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApkVersionResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LoginResponse;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.apk_update.V1_ApkUpdateActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.base_url.BaseUrlActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "V1_LoginActivity";
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, V1_LoginActivity.class);
        return intent;
    }

    private EditText urlEditText;
    private EditText verificationText;
    private EditText usernameText;
    private EditText passwordText;
    private TextView versionTV, base_urlTV;
    private ImageView base_urlImageview;
    private Button loginButton;
    private Button guestBtn;
    private CheckBox logoutCheckBox;

    //get From user
    private String urlstring, base_url, base_url_full, version, deviceToken, logo_location = "";
    //get From Activity
    private String username;
    private String password;

    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private ProgressDialog pDialog;
    private SessionManager session;

    private int userID = 0;
    private String urlAddress = " ", macAddress = "";
    private PackageInfo pInfo;
    private LinearLayout webAddress, location, products, contacts;
    private ArrayList<V1_LoginResponse.ProVariable> proVariableArrayList = new ArrayList<>();

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_login_test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            version = pInfo.versionName;
            versionTV = findViewById(R.id.versionTV);
            versionTV.setText("V"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        macAddress = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        initializationmethod();
    }

    private void initializationmethod() {

        setBaseUrl();

        base_urlTV = findViewById(R.id.base_urlTV);
        base_urlImageview = findViewById(R.id.base_urlImageview);
        base_urlImageview.setOnClickListener(this);
        urlEditText = findViewById(R.id.urlET);
        usernameText = findViewById(R.id.usernameET);
        passwordText = findViewById(R.id.passwordET);
        verificationText = findViewById(R.id.verifycodeET);
        loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);

        webAddress = findViewById(R.id.webAddress);
        webAddress.setOnClickListener(this);
        location = findViewById(R.id.location);
        location.setOnClickListener(this);
        products = findViewById(R.id.products);
        products.setOnClickListener(this);
        contacts = findViewById(R.id.contacts);
        contacts.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

//        base_urlTV.setText(base_url);
        base_urlTV.setText("202.4.102.250:7964/platform_erp");

        if (session.isLoggedIn()) {
            Intent intent = V1_MenuActivity.getStartIntent(V1_LoginActivity.this);
            startActivity(intent);
            finish();
        }

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            deviceToken = task.getResult();
            Log.d("token ##########", deviceToken);

        });
    }

    private void setBaseUrl() {
        base_url_full = "http://202.4.102.250:7964/platform_erp/logic-api/index.php/api/Android/";
        apiUtils = new ApiUtils(this);
        apiInterface = apiUtils.getInterface(base_url_full);
        checkApkVersion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                loginButtonMethod();
                break;
            case R.id.base_urlImageview:
                startActivity(new Intent(this, BaseUrlActivity.class));
                finish();
                break;
            case R.id.webAddress:
                Intent intentWebAddress = new Intent(getApplicationContext(), CompanyInfoActivity.class);
                intentWebAddress.putExtra("html", "https://www.logicsoftbd.com/");
                startActivity(intentWebAddress);
                break;
            case R.id.location:
            case R.id.contacts:
                Intent intentLocation = new Intent(getApplicationContext(), CompanyInfoActivity.class);
                intentLocation.putExtra("html", "https://www.logicsoftbd.com/?page_id=26");
                startActivity(intentLocation);
                break;
            case R.id.products:
                Intent intentProducts = new Intent(getApplicationContext(), CompanyInfoActivity.class);
                intentProducts.putExtra("html", "https://www.logicsoftbd.com/?page_id=694");
                startActivity(intentProducts);
                break;
        }
    }

    private void loginButtonMethod() {
        username = usernameText.getText().toString().trim();
        password = passwordText.getText().toString().trim();

        if(!username.isEmpty() && !password.isEmpty()){
            checkLogin(urlAddress, username, password);
        }
        else {
            Toast.makeText(this, "Please enter the credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLogin(final String urlAddress, final String username, final String password) {
        pDialog.setMessage("Processing on going in ....");
        showDialog();

        apiInterface.loginResponseCall(username, password, macAddress, deviceToken, "android").enqueue(new Callback<V1_LoginResponse>() {
            @Override
            public void onResponse(Call<V1_LoginResponse> call, Response<V1_LoginResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){

                    if(response.body().getResultset() != null){
                        try {
                            userID = Integer.parseInt(response.body().getResultset().getUser_id());
                            if(!response.body().getResultset().getStatus().equals("Failed") || response.body().getResultset().getStatus() == null){
                                userID = Integer.parseInt(response.body().getResultset().getUser_id());
                            }
                        }catch (Exception e){

                        }

                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = "";
                        try {
                            json = objectMapper.writeValueAsString(response.body().getResultset().getProVariable());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        try {
                            if((response.body().getResultset().getModule() != null && response.body().getResultset().getModule().size() > 0) || response.body().getResultset().getProVariable() != null){
                                session.setLogin(true);
                                SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_LoginActivity.this);
                                SharedPreferences.Editor _editor = _preferences.edit();
                                String[] spitesUrl = base_url_full.split("/");
                                Log.d(TAG, "onResponse: "+spitesUrl.length);
                                String urlCheck = "";
                                if(spitesUrl.length <= 3){
                                    urlCheck = spitesUrl[2];
                                }else{
                                    urlCheck = spitesUrl[2]+"/"+spitesUrl[3];
                                }
                                _editor.putString("login_url", "http://"+urlAddress+"/");
                                _editor.putString("check_url", urlCheck);
                                _editor.putString("login_username", username);
                                _editor.putString("login_password", password);
                                _editor.putString("login_userid", String.valueOf(userID));
                                _editor.putString("base_url", base_url_full);
                                _editor.putString("company_id", response.body().getResultset().getCompany_id());
                                _editor.putString("logo_location", logo_location);
                                _editor.putString("proVariableArrayList", json);
                                _editor.commit();

                                Intent intent = V1_MenuActivity.getStartIntent(V1_LoginActivity.this);
                                startActivity(intent);
                                finish();
                            } else {
                                alertForLoggedIn(response.body().getResultset().getMsg(), response.body().getResultset().getReference_data());
                            }
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: #########"+e.getMessage());
                        }

                    }else {
                        Toast.makeText(V1_LoginActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(V1_LoginActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_LoginResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(V1_LoginActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong user or password", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void checkApkVersion() {
//        showDialog();
        apiInterface.getAppVersionCheckResponseCall().enqueue(new Callback<V1_ApkVersionResponse>() {
            @Override
            public void onResponse(Call<V1_ApkVersionResponse> call, Response<V1_ApkVersionResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "onResponse: ############### Success ######### "+ response.body().getData().getVersion());
                    versionTV.setText("Version-"+version);
                    logo_location = response.body().getData().getCompany_image();
                    Log.d(TAG, "checkApkVersion: ############# "+version);
                    if(!version.equals(response.body().getData().getVersion())){
                        alertForApkUpdate(response.body().getData().getAppUrl(), response.body().getData().getVersion());
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_ApkVersionResponse> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onFailure: ############## Failed ########## "+   t.getMessage());
            }
        });

    }

    private void alertForApkUpdate(String appUrl, String appVersion){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_update_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);

        messageTV.setText("A newer version V"+appVersion+" is available. Please update to get the latest features and best experience.");
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {

            String result = appUrl.substring(3);
            String newUrl = "";

            String[] spitesBaseUrl = base_url_full.split("/");
            Log.d(TAG, "initUI: "+spitesBaseUrl.length);
            if(spitesBaseUrl.length == 3){
                newUrl = spitesBaseUrl[0]+"/"+spitesBaseUrl[1]+"/"+spitesBaseUrl[2]+"/"+spitesBaseUrl[3]+"/"+result;
            }else{
                newUrl = spitesBaseUrl[0]+"/"+spitesBaseUrl[2]+"/"+result;
            }
            Log.d(TAG, "alertForApkUpdate: "+newUrl);

            Intent intent = new Intent(V1_LoginActivity.this, V1_ApkUpdateActivity.class);
            intent.putExtra("apkUrl", newUrl);
            startActivity(intent);
            dialog.dismiss();
            finish();
        });

        cancel.setOnClickListener( v -> {
            dialog.dismiss();
            finish();
        });
    }

    private void showAlertMessage(String msg, Integer i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_LoginActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
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
            startActivity(new Intent(V1_LoginActivity.this, BaseUrlActivity.class));
            finish();
            dialog.dismiss();
        });

        cancel.setOnClickListener( v -> {

        });
    }

    private void alertForLoggedIn(String msg, String message){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_alert_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);

        String[] spitesUrl = message.split("\\*\\*");
        String urlCheck = spitesUrl[0]+" "+spitesUrl[1]+" "+spitesUrl[2]+" "+spitesUrl[3];

        messageTV.setText(msg+"\n"+urlCheck);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            usernameText.setText("");
            passwordText.setText("");
            dialog.dismiss();
        });

        cancel.setOnClickListener( v -> dialog.dismiss());
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    private void showDialog() {
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setBaseUrl();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}