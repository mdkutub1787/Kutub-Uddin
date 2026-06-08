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
import com.logicsoftbd.lsl.data.network.v1_model.V1_ShipmentModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_Shipment_Schedule_Model;
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

public class V1_ShipmentScheduleManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Shipment Report";

    private String date, currentDate, start_date, end_date, barcode, urladdressChk, urladdress, urlString, urlstringbase, urlstring_c_wise_l, urlstring_l_wise_f,
            urlstring_f_wise_l, urlstring_sewing_input, urlShipmentReportData;
    private int Year, Month, Day, userId = 0;
    private double totalQuantity = 0,QuantityValueTotal=0,fullShippedValueCal=0,partialShippedValueCal=0,runningTotal=0;

    private Button startdate, endDate, sSave, sReportData;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private ArrayList<V1_Shipment_Schedule_Model> shipmentModelArrayList;

    private SessionManager session;
    public final ArrayList<String> com_name = new ArrayList<>();
    public final ArrayList<String> buyer_name = new ArrayList<>();
    public final ArrayList<String> quntity = new ArrayList<String>();
    public final ArrayList<String> quntity_val = new ArrayList<>();
    public final ArrayList<String> quntity_val_pre = new ArrayList<>();
    public final ArrayList<String> full_shipped = new ArrayList<String>();
    public final ArrayList<String> partial_shipped = new ArrayList<String>();
    public final ArrayList<String> running = new ArrayList<String>();
    public final ArrayList<String> ex_fact_per = new ArrayList<String>();


    public String[] com_name_array;
    public String[] buyer_name_array;
    public String[] quntity_array;
    public String[] quntity_val_array;
    public String[] quntity_val_pre_array;
    public String[] full_shipped_array;
    public String[] partial_shipped_array;
    public String[] running_array;
    public String[] ex_fact_per_array;
    private V1_CustomDialogShipmentList customDialogShipmentList;
    private ProgressDialog pDialog;
    private DatePickerDialog datePickerDialog;
    private  String base_url = "", type_entry, barcodeNumber;

    private int company = 0, location = 0, line = 0, floor = 0;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_shipment_schedule_management);
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

        apiInterface.getShipmentModelClassCall(3, 1, start_date, end_date).enqueue(new Callback<V1_ShipmentModelClass>() {
            @Override
            public void onResponse(Call<V1_ShipmentModelClass> call, Response<V1_ShipmentModelClass> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(ShipmentScheduleManagementActivity.this, "True", Toast.LENGTH_SHORT).show();

                    if (!com_name.isEmpty()) {
                        buyer_name.clear();
                        quntity.clear();
                        quntity_val.clear();
                        quntity_val_pre.clear();
                        full_shipped.clear();
                        partial_shipped.clear();
                        running.clear();
                        ex_fact_per.clear();
                    }


                    V1_ShipmentModelClass.Datum com_Name;
                    List<V1_ShipmentModelClass.Datum> company_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : company_List) {
                        com_Name = d;
                        final V1_ShipmentModelClass.Datum finalName = com_Name;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com_name.add(finalName.getCOMPANYNAME());
                            }
                        });
                    }

                    V1_ShipmentModelClass.Datum buyer_Name;
                    List<V1_ShipmentModelClass.Datum> buyer_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : buyer_List) {
                        buyer_Name = d;
                        final V1_ShipmentModelClass.Datum finalName = buyer_Name;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buyer_name.add(finalName.getBUYERNAME());
                            }
                        });
                    }

                    V1_ShipmentModelClass.Datum Quntity;
                    List<V1_ShipmentModelClass.Datum> quntity_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : quntity_List) {
                        Quntity = d;
                        final V1_ShipmentModelClass.Datum finalName = Quntity;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                quntity.add(finalName.getQUANTITY());
                            }
                        });

                    }

                    V1_ShipmentModelClass.Datum Quntity_val;
                    List<V1_ShipmentModelClass.Datum> quantity_val_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : quantity_val_List) {
                        Quntity_val = d;
                        final V1_ShipmentModelClass.Datum finalName = Quntity_val;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                quntity_val.add(finalName.getQUANTITYVALUE());
                            }
                        });
                    }

                    V1_ShipmentModelClass.Datum Quntity_val_pre;
                    List<V1_ShipmentModelClass.Datum> quantity_val_pre_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : quantity_val_pre_List) {
                        Quntity_val_pre = d;
                        final V1_ShipmentModelClass.Datum finalName = Quntity_val_pre;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                quntity_val_pre.add(finalName.getQUANTITYVALUEPERCENTAGE());
                            }
                        });
                    }

                    V1_ShipmentModelClass.Datum Full_shipped;
                    List<V1_ShipmentModelClass.Datum> full_shipped_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : full_shipped_List) {
                        Full_shipped = d;
                        final V1_ShipmentModelClass.Datum finalName = Full_shipped;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                full_shipped.add(finalName.getFULLSHIPPED());
                            }
                        });
                    }
                    V1_ShipmentModelClass.Datum Partial_shipped;
                    List<V1_ShipmentModelClass.Datum> partial_shipeed_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : partial_shipeed_List) {
                        Partial_shipped = d;
                        final V1_ShipmentModelClass.Datum finalName = Partial_shipped;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                partial_shipped.add(finalName.getPARTIALSHIPPED());

                            }
                        });
                    }
                    V1_ShipmentModelClass.Datum Running;
                    List<V1_ShipmentModelClass.Datum> running_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : running_List) {
                        Running = d;
                        final V1_ShipmentModelClass.Datum finalName = Running;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                running.add(finalName.getRUNNING());

                            }
                        });
                    }
                    V1_ShipmentModelClass.Datum Ex_fact_per;
                    List<V1_ShipmentModelClass.Datum> ex_fact_List = response.body().getData();
                    for (V1_ShipmentModelClass.Datum d : ex_fact_List) {
                        Ex_fact_per = d;
                        final V1_ShipmentModelClass.Datum finalName = Ex_fact_per;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ex_fact_per.add(finalName.getEXFACTORYPERCENTAGE());
                                ex_fact_per.add(String.valueOf(finalName.getEXFACTORYPERCENTAGE()));
                            }
                        });
                    }

                    shipmentModelArrayList = getShipmentModel();


                    V1_Shipment_Schedule_Adapter shipmentAdapter = new V1_Shipment_Schedule_Adapter(getApplicationContext(), R.layout.shipment_report_items, shipmentModelArrayList);


                    customDialogShipmentList = new V1_CustomDialogShipmentList(V1_ShipmentScheduleManagementActivity.this, shipmentAdapter);


                    customDialogShipmentList.show();

                } else {
                    Toast.makeText(V1_ShipmentScheduleManagementActivity.this, "Data Not Found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_ShipmentModelClass> call, Throwable t) {
                Toast.makeText(V1_ShipmentScheduleManagementActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<V1_Shipment_Schedule_Model> getShipmentModel() {


        ArrayList<V1_Shipment_Schedule_Model> shipmentModels = new ArrayList<>();

        try {
            com_name_array = new String[com_name.size()];
            for (int i = 0; i < com_name.size(); i++) {
                com_name_array[i] = com_name.get(i);
            }

            buyer_name_array = new String[buyer_name.size()];
            for (int i = 0; i < buyer_name.size(); i++) {
                buyer_name_array[i] = buyer_name.get(i);
            }

            quntity_array = new String[quntity.size()];
            for (int i = 0; i < quntity.size(); i++) {
                quntity_array[i] = quntity.get(i);

            }


            quntity_val_array = new String[quntity_val.size()];
            for (int i = 0; i < quntity_val.size(); i++) {
                quntity_val_array[i] = quntity_val.get(i);

            }

            quntity_val_pre_array = new String[quntity_val_pre.size()];
            for (int i = 0; i < quntity_val_pre.size(); i++) {
                quntity_val_pre_array[i] = quntity_val_pre.get(i);

            }

            full_shipped_array = new String[full_shipped.size()];
            for (int i = 0; i < full_shipped.size(); i++) {
                full_shipped_array[i] = full_shipped.get(i);
            }
            partial_shipped_array = new String[partial_shipped.size()];
            for (int i = 0; i < partial_shipped.size(); i++) {
                partial_shipped_array[i] = partial_shipped.get(i);
            }
            running_array = new String[running.size()];
            for (int i = 0; i < running.size(); i++) {
                running_array[i] = running.get(i);
            }
            ex_fact_per_array = new String[ex_fact_per.size()];
            for (int i = 0; i < ex_fact_per.size(); i++) {
                ex_fact_per_array[i] = ex_fact_per.get(i);
            }


            for (int i = 0; i < com_name_array.length; i++) {
                V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
                shipmentModel.setCOMPANY_NAME(String.valueOf(com_name.get(i)));
                shipmentModel.setBUYER_NAME(String.valueOf(buyer_name_array[i]));
                shipmentModel.setQUANTITY(String.valueOf(quntity_array[i]));
                shipmentModel.setQUANTITY_VALUE(String.valueOf(quntity_val_array[i]));
                shipmentModel.setQUANTITY_VALUE_PERCENTAGE(String.valueOf(quntity_val_pre_array[i]));
                shipmentModel.setFULL_SHIPPED(String.valueOf(full_shipped_array[i]));
                shipmentModel.setPARTIAL_SHIPPED(String.valueOf(partial_shipped_array[i]));
                shipmentModel.setRUNNING(String.valueOf(running_array[i]));
                shipmentModel.setEX_FACTORY_PERCENTAGE(String.valueOf(ex_fact_per_array[i]));



                shipmentModels.add(shipmentModel);


                QuantityTotal();
                QuantityValueTotal();
                fullShippedValueCal();
                partialShippedValueCal();
                runningTotal();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return shipmentModels;


    }

    public double QuantityTotal() {


        double totalQuantity = 0;
        for (int i = 0; i < quntity.size(); i++) {
            V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
            totalQuantity += Double.parseDouble(quntity.get(i));

            shipmentModel.setTotalQuantity(String.valueOf(totalQuantity));
        }

        return totalQuantity;




    }
    public double QuantityValueTotal() {
        double totalQuantityVal = 0;
        for (int i = 0; i < quntity_val.size(); i++) {
            V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
            totalQuantityVal += Double.parseDouble(quntity_val.get(i));
            shipmentModel.setTotalQuantityValue(String.valueOf(totalQuantityVal));

        }

        return totalQuantityVal;


    }
    public double fullShippedValueCal() {
        long fullShippedTotal = 0;
        for (int i = 0; i < full_shipped.size(); i++) {
            V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
            fullShippedTotal += Long.parseLong(full_shipped.get(i));
            shipmentModel.setTotalFullshipped(String.valueOf(fullShippedTotal));

        }

        return fullShippedTotal;

    }
    public double partialShippedValueCal() {
        long partialShippedTotal = 0;
        for (int i = 0; i < partial_shipped.size(); i++) {
            V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
            partialShippedTotal += Long.parseLong(partial_shipped.get(i));
            shipmentModel.setTotalPartialShipped(String.valueOf(partialShippedTotal));

        }

        return partialShippedTotal;

    }
    public double runningTotal() {
        long totalRunning = 0;
        for (int i = 0; i < running.size(); i++) {
            V1_Shipment_Schedule_Model shipmentModel = new V1_Shipment_Schedule_Model();
            totalRunning += Long.parseLong(running.get(i));
            shipmentModel.setTotalRunning(String.valueOf(totalRunning));

        }

        return totalRunning;



    }








    //    Intent i = new Intent(ShipmentScheduleManagementActivity.this,CustomDialogShipmentList.class);
//
//                i.putExtra("Quantity",totalQuantity);
//                i.putExtra("QuantityValue",QuantityValueTotal);
//                i.putExtra("FullShipped",fullShippedValueCal);
//                i.putExtra("PatialShipped",partialShippedValueCal);
//                i.putExtra("Running",runningTotal);
//    startActivity(i);



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
        datePickerDialog = new DatePickerDialog(V1_ShipmentScheduleManagementActivity.this,
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
        Intent intent = new Intent(V1_ShipmentScheduleManagementActivity.this, V1_MenuActivity.class);
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