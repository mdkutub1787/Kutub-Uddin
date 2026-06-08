package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConsolitatedOrderSummeryModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_Consolitated_Order_Summery_Model;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ConsolitatedOrderSummeryctivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Consolitated Order Summery Report";

    private String base_url = "", date, currentDate, start_date, end_date, barcode, urladdressChk, urladdress, urlString, urlstringbase, urlstring_c_wise_l, urlstring_l_wise_f,
            urlstring_f_wise_l, urlstring_sewing_input, urlConsolitatedOrderSummeryData;
    private int Year, Month, Day, userId = 0;

    private Button startdate, endDate, sSave, sReportData;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private ArrayList<V1_Consolitated_Order_Summery_Model> consolitatedOrderSummeryModelArrayList;

    private SessionManager session;
    public final ArrayList<String> montH = new ArrayList<>();
    public final ArrayList<String> com_namE = new ArrayList<>();
    public final ArrayList<String> confirM = new ArrayList<String>();
    public final ArrayList<String> projectioN = new ArrayList<>();
    public final ArrayList<String> confirm_qtY = new ArrayList<>();
    public final ArrayList<String> confirm_ammounT = new ArrayList<String>();
    public final ArrayList<String> avG = new ArrayList<String>();


    public String[] com_name_array;
    public String[] month_array;
    public String[] confirm_array;
    public String[] projection_array;
    public String[] confirm_qty_array;
    public String[] confirm_ammount_array;
    public String[] avg_array;

    private V1_CustomDialogConsolitatedOrderSummeryList customDialogConsolitatedOrderSummeryList;
    private ProgressDialog pDialog;
    private DatePickerDialog datePickerDialog;
    private  String type_entry, barcodeNumber;

    private int company = 0, location = 0, line = 0, floor = 0;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_consolitated_order_summeryctivity);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.shipmentReportDataBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportShow();
            }
        });
        startdate = findViewById(R.id.startdate);
        startdate.setOnClickListener(this);

        endDate = findViewById(R.id.enddate);
        endDate.setOnClickListener(this);

        initialization();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        type_entry = intent.getStringExtra("qc");


        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();

        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userId = intent.getIntExtra("userId", 0);

        }else {
            urladdress = loginData.get(0).getUrl();
            userId = Integer.parseInt(loginData.get(0).getUserId());
        }
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        company = (_preferences.getInt("company", 0));
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);
    }


    private void initialization() {
        //set Date
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        startdate.setText(currentDate);
        endDate.setText(currentDate);
    }


    private void ReportShow() {
        start_date = startdate.getText().toString();
        end_date = endDate.getText().toString();

        apiInterface.getConsolitatedOrderSummeryModelClassCall(1, 1, start_date, end_date).enqueue(new Callback<V1_ConsolitatedOrderSummeryModelClass>() {
            @Override
            public void onResponse(Call<V1_ConsolitatedOrderSummeryModelClass> call, Response<V1_ConsolitatedOrderSummeryModelClass> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(ShipmentScheduleManagementActivity.this, "True", Toast.LENGTH_SHORT).show();

                    if (!montH.isEmpty()) {
                        com_namE.clear();
                        confirM.clear();
                        projectioN.clear();
                        confirm_qtY.clear();
                        confirm_ammounT.clear();
                        avG.clear();

                    }


                    V1_ConsolitatedOrderSummeryModelClass.Datum month;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> month_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : month_List) {
                        month = d;
                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = month;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                montH.add(finalName.getMONTH());
                            }
                        });
                    }
                    V1_ConsolitatedOrderSummeryModelClass.Datum com_name;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> company_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : company_List) {
                        com_name = d;

                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = com_name;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com_namE.add(finalName.getCOMPANY());
                            }
                        });
                    }

                    V1_ConsolitatedOrderSummeryModelClass.Datum confirm;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> confirm_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : confirm_List) {
                        confirm = d;

                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = confirm;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirM.add(finalName.getCONFIRM());
                            }
                        });
                    }

                    V1_ConsolitatedOrderSummeryModelClass.Datum projection;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> projection_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : projection_List) {
                        projection = d;
                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = projection;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                projectioN.add(finalName.getPROJECTION());
                            }
                        });

                    }

                    V1_ConsolitatedOrderSummeryModelClass.Datum confirm_qty;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> confirm_qty_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : confirm_qty_List) {
                        confirm_qty = d;

                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = confirm_qty;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirm_qtY.add(finalName.getCONFIRMQTY());
                            }
                        });
                    }

                    V1_ConsolitatedOrderSummeryModelClass.Datum confirm_ammount;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> confirm_ammount_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : confirm_ammount_List) {
                        confirm_ammount = d;

                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = confirm_ammount;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirm_ammounT.add(finalName.getCONFIRMAMOUNT());
                            }
                        });
                    }

                    V1_ConsolitatedOrderSummeryModelClass.Datum avg;
                    List<V1_ConsolitatedOrderSummeryModelClass.Datum> avg_List = response.body().getData();
                    for (V1_ConsolitatedOrderSummeryModelClass.Datum d : avg_List) {
                        avg = d;

                        final V1_ConsolitatedOrderSummeryModelClass.Datum finalName = avg;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                avG.add(finalName.getAVG());
                            }
                        });
                    }



                    consolitatedOrderSummeryModelArrayList = getCosolitatedModel();


                    V1_Consolitated_Order_Summery_Adapter shipmentAdapter = new V1_Consolitated_Order_Summery_Adapter(getApplicationContext(), R.layout.consolitated_report_items, consolitatedOrderSummeryModelArrayList);


                    customDialogConsolitatedOrderSummeryList = new V1_CustomDialogConsolitatedOrderSummeryList(V1_ConsolitatedOrderSummeryctivity.this, shipmentAdapter);


                    customDialogConsolitatedOrderSummeryList.show();

                } else {
                    Toast.makeText(V1_ConsolitatedOrderSummeryctivity.this, "Data Not Found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_ConsolitatedOrderSummeryModelClass> call, Throwable t) {
                Toast.makeText(V1_ConsolitatedOrderSummeryctivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private ArrayList<V1_Consolitated_Order_Summery_Model> getCosolitatedModel() {


        ArrayList<V1_Consolitated_Order_Summery_Model> consolitatedModels = new ArrayList<>();

        try {
            com_name_array = new String[com_namE.size()];
            for (int i = 0; i < com_namE.size(); i++) {
                com_name_array[i] = com_namE.get(i);
            }

            month_array = new String[montH.size()];
            for (int i = 0; i < montH.size(); i++) {
                month_array[i] = montH.get(i);
            }

            confirm_array = new String[confirM.size()];
            for (int i = 0; i < confirM.size(); i++) {
                confirm_array[i] = confirM.get(i);

            }


            projection_array = new String[projectioN.size()];
            for (int i = 0; i < projectioN.size(); i++) {
                projection_array[i] = projectioN.get(i);

            }

            confirm_qty_array = new String[confirm_qtY.size()];
            for (int i = 0; i < confirm_qtY.size(); i++) {
                confirm_qty_array[i] = confirm_qtY.get(i);

            }

            confirm_ammount_array = new String[confirm_ammounT.size()];
            for (int i = 0; i < confirm_ammounT.size(); i++) {
                confirm_ammount_array[i] = confirm_ammounT.get(i);
            }
            avg_array = new String[avG.size()];
            for (int i = 0; i < avG.size(); i++) {
                avg_array[i] = avG.get(i);
            }



            for (int i = 0; i < month_array.length; i++) {
                V1_Consolitated_Order_Summery_Model consolitatedModel = new V1_Consolitated_Order_Summery_Model();
                consolitatedModel.setMONTH(String.valueOf(month_array[i]));
                consolitatedModel.setCOMPANY(String.valueOf(com_name_array[i]));
                consolitatedModel.setCONFIRM(String.valueOf(confirm_array[i]));
                consolitatedModel.setPROJECTION(String.valueOf(projection_array[i]));
                consolitatedModel.setCONFIRM_QTY(String.valueOf(confirm_qty_array[i]));
                consolitatedModel.setCONFIRM_AMOUNT(String.valueOf(confirm_ammount_array[i]));
                consolitatedModel.setAVG(String.valueOf(avg_array[i]));




                consolitatedModels.add(consolitatedModel);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return consolitatedModels;


    }



    /*Date Picker*/
    private void datePickerMethod(Button sDate) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, dateListener, Year, Month, Day);
        dpd.show();

    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month + 1;
            date = String.format("%d-%d-%d", dayOfMonth, month, year);
        }
    };

    private void datepicker(final Button button) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(V1_ConsolitatedOrderSummeryctivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        button.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startdate: {
                datepicker(startdate);
                break;
            }
            case R.id.enddate: {
                datepicker(endDate);
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_ConsolitatedOrderSummeryctivity.this, V1_MenuActivity.class);
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