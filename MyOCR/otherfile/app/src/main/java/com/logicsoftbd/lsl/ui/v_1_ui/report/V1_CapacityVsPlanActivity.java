package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CapacityVsPlanModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CapacityVsPlanModelClass;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class V1_CapacityVsPlanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CapacityVsPlanActivity";

    private android.app.AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog pDialog;
    private int company = 0, location = 0;
    private String base_url = "", date, urlstringbase, urladdressChk, urladdress, urlstring_c_wise_l, currentDate;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private int Year, Month, Day, userId = 0;
    private Retrofit retrofit;
    private Spinner rCompany, rLocation;
    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> locationNameList = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public String[] companyNameArray, locationNameArray, floorNameArray, lineNameArray;
    private int companyId = 0, locationId = 0, floorId = 0, lineId = 0;
    private Button startButton, endButton, showButton;
    private TextView monthNameTV, planFirstTV, planTotalTV, bookedFirstTV, bookedTotalTV, balanceFirstTV, balanceTotalTV;

    private LinearLayout plan_vs_booked;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;

    public final ArrayList<String> rCompanyNameList = new ArrayList<>();
    public final ArrayList<String> rLocationNameList = new ArrayList<>();
    public final ArrayList<String> monthNameList = new ArrayList<>();
    public final ArrayList<String> planMinuteList = new ArrayList<>();
    public final ArrayList<String> CapacityMinuteList = new ArrayList<>();

    public String[] rCompanyNameArray, rLocationNameArray, monthNameArray;
    public String[] planMinuteArray, capacityMinuteArray;
    public static ArrayList<V1_CapacityVsPlanModelClass> modelArrayList;
    private V1_CapacityVsBookedAdapter capacityVsBookedAdapter;
    private GridView planBookedGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_capacity_vs_plan);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        alertDialogBuilder = new android.app.AlertDialog.Builder(V1_CapacityVsPlanActivity.this);
        Intent intent = getIntent();
        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");

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
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        initialization();
    }

    private void initialization() {
        plan_vs_booked = findViewById(R.id.header_plan_vs_booked);
        rCompany = findViewById(R.id.companySpinner);
        rLocation = findViewById(R.id.locationSpinnner);
        monthNameTV = findViewById(R.id.monthNameText);
        startButton = findViewById(R.id.startDateBtn);
        startButton.setOnClickListener(this);
        endButton = findViewById(R.id.endDateBtn);
        endButton.setOnClickListener(this);
        showButton = findViewById(R.id.showButton);
        showButton.setOnClickListener(this);
        planBookedGridView = findViewById(R.id.planBookedGridView);
        planFirstTV = findViewById(R.id.planfirstText);
        planTotalTV = findViewById(R.id.planTotalText);
        bookedFirstTV = findViewById(R.id.bookedfirstText);
        bookedTotalTV = findViewById(R.id.bookedTotalText);
        balanceFirstTV = findViewById(R.id.balancefirst);
        balanceTotalTV = findViewById(R.id.balanceTotal);

        sendRequestToServer();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        startButton.setText(currentDate);
        endButton.setText(currentDate);
    }

    private void sendRequestToServer() {
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingInputClass> call, Response<V1_BundleWiseSewingInputClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    V1_BundleWiseSewingInputClass.Company companyName;
                    List<V1_BundleWiseSewingInputClass.Company> companies = response.body().getResultset().getCompany();
                    companyNameList.add(0, "--Select Company--");
                    for(V1_BundleWiseSewingInputClass.Company d : companies)
                    {
                        companyName = d;
                        final V1_BundleWiseSewingInputClass.Company CompanyName = companyName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyNameList.add(String.valueOf(CompanyName.getCompany()));
                            }
                        });
                    }

                    companyNameArray = new String[companyNameList.size()];
                    for(int i = 0; i < companyNameList.size(); i++)
                    {
                        companyNameArray[i] = companyNameList.get(i);
                    }

                    V1_BundleWiseSewingInputClass.Company companyId;
                    List<V1_BundleWiseSewingInputClass.Company> companiesId = response.body().getResultset().getCompany();
                    companyNameId.add(0, 0);
                    for(V1_BundleWiseSewingInputClass.Company d : companiesId)
                    {
                        companyId = d;
                        final V1_BundleWiseSewingInputClass.Company CompanyId = companyId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyNameId.add(CompanyId.getId());
                            }
                        });
                    }
                    setSourceadapterData();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingInputClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_CapacityVsPlanActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setSourceadapterData() {
        ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, companyNameArray);
        rCompany.setAdapter(adapterCompany);
        rCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int companyPosition = position;
                companyId = companyNameId.get(companyPosition);
                companyWiseLocation(companyId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void companyWiseLocation(int companyPosition) {
        showDialog();
        apiInterface.getBundleWiseCompanyToLocationClassCall(companyPosition).enqueue(new Callback<V1_BundleWiseCompanyToLocationClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseCompanyToLocationClass> call, Response<V1_BundleWiseCompanyToLocationClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    if(locationNameList.isEmpty())
                    {
                        loadDataLocation(response);
                    }
                    else if(!locationNameList.isEmpty())
                    {
                        locationNameList.clear();
                        loadDataLocation(response);
                    }
                }
            }
            private void loadDataLocation(Response<V1_BundleWiseCompanyToLocationClass> response) {
                V1_BundleWiseCompanyToLocationClass.Resultset locationName;
                List<V1_BundleWiseCompanyToLocationClass.Resultset> locationses = response.body().getResultset();
                locationNameList.add(0, "--Select Location--");
                for(V1_BundleWiseCompanyToLocationClass.Resultset d : locationses)
                {
                    locationName = d;
                    final V1_BundleWiseCompanyToLocationClass.Resultset LocationName = locationName;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            locationNameList.add(String.valueOf(LocationName.getName()));
                        }
                    });
                }
                locationNameArray = new String[locationNameList.size()];
                for(int i = 0; i < locationNameList.size(); i++)
                {
                    locationNameArray[i] = locationNameList.get(i);
                }

                V1_BundleWiseCompanyToLocationClass.Resultset locationId;
                List<V1_BundleWiseCompanyToLocationClass.Resultset> locationsesId = response.body().getResultset();
                locationNameId.add(0, 0);
                for(V1_BundleWiseCompanyToLocationClass.Resultset d : locationsesId)
                {
                    locationId = d;
                    final V1_BundleWiseCompanyToLocationClass.Resultset LocationId = locationId;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            locationNameId.add(LocationId.getId());
                        }
                    });
                }

                setAdapterData_Location();
            }
            @Override
            public void onFailure(Call<V1_BundleWiseCompanyToLocationClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_CapacityVsPlanActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }


    private void setAdapterData_Location() {
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(V1_CapacityVsPlanActivity.this, R.layout.sewing_spinner_layout, locationNameArray);
        rLocation.setAdapter(adapterLocation);
        rLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int locationPosition = position;
                locationId = locationNameId.get(locationPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showReportFromServer() {
        showDialog();
        apiInterface.getCapacityVsPlanModelCall(companyId, locationId, startButton.getText().toString(), endButton.getText().toString()).enqueue(new Callback<V1_CapacityVsPlanModel>() {
            @Override
            public void onResponse(Call<V1_CapacityVsPlanModel> call, Response<V1_CapacityVsPlanModel> response) {
                if(response.isSuccessful()){
                    hideDialog();
                    plan_vs_booked.setVisibility(View.GONE);
                    monthNameTV.setText(String.valueOf(response.body().getData().get(0).getMonth()));

                    V1_CapacityVsPlanModel.Datum rCompanyName;
                    final List<V1_CapacityVsPlanModel.Datum> CompanyName = response.body().getData();
                    for(V1_CapacityVsPlanModel.Datum d : CompanyName)
                    {
                        rCompanyName = d;
                        final V1_CapacityVsPlanModel.Datum finalName = rCompanyName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rCompanyNameList.add(String.valueOf(finalName.getCompanyName()));
                            }
                        });
                    }

                    V1_CapacityVsPlanModel.Datum rLocationName;
                    final List<V1_CapacityVsPlanModel.Datum> LocationName = response.body().getData();
                    for(V1_CapacityVsPlanModel.Datum d : LocationName)
                    {
                        rLocationName = d;
                        final V1_CapacityVsPlanModel.Datum finalName = rLocationName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rLocationNameList.add(String.valueOf(finalName.getLocationName()));
                            }
                        });
                    }

                    V1_CapacityVsPlanModel.Datum rMonth;
                    final List<V1_CapacityVsPlanModel.Datum> MonthName = response.body().getData();
                    for(V1_CapacityVsPlanModel.Datum d : MonthName)
                    {
                        rMonth = d;
                        final V1_CapacityVsPlanModel.Datum finalName = rMonth;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                monthNameList.add(String.valueOf(finalName.getMonth()));
                            }
                        });
                    }

                    V1_CapacityVsPlanModel.Datum rPlanMinute;
                    final List<V1_CapacityVsPlanModel.Datum> PlanMinute = response.body().getData();
                    for(V1_CapacityVsPlanModel.Datum d : PlanMinute)
                    {
                        rPlanMinute = d;
                        final V1_CapacityVsPlanModel.Datum finalName = rPlanMinute;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                planMinuteList.add((String.valueOf(finalName.getPlanMinute())));
                            }
                        });
                    }

                    V1_CapacityVsPlanModel.Datum rBookedMinute;
                    final List<V1_CapacityVsPlanModel.Datum> BookedMinute = response.body().getData();
                    for(V1_CapacityVsPlanModel.Datum d : BookedMinute)
                    {
                        rBookedMinute = d;
                        final V1_CapacityVsPlanModel.Datum finalName = rBookedMinute;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CapacityMinuteList.add((String.valueOf(finalName.getCapacityMinute())));
                            }
                        });
                    }

                    modelArrayList = getModel();
                    capacityVsBookedAdapter = new V1_CapacityVsBookedAdapter(V1_CapacityVsPlanActivity.this, R.layout.plan_vs_booked_report_layout, modelArrayList);
                    planBookedGridView.setAdapter(capacityVsBookedAdapter);

                } else {
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<V1_CapacityVsPlanModel> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private ArrayList<V1_CapacityVsPlanModelClass> getModel() {
        ArrayList<V1_CapacityVsPlanModelClass> list = new ArrayList<>();
        rCompanyNameArray = new String[rCompanyNameList.size()];
        for(int i = 0; i < rCompanyNameList.size(); i++)
        {
            rCompanyNameArray[i] = rCompanyNameList.get(i);
        }

        rLocationNameArray = new String[rLocationNameList.size()];
        for(int i = 0; i < rLocationNameList.size(); i++)
        {
            rLocationNameArray[i] = rLocationNameList.get(i);
        }

        monthNameArray = new String[monthNameList.size()];
        for(int i = 0; i < monthNameList.size(); i++)
        {
            monthNameArray[i] = monthNameList.get(i);
        }

        planMinuteArray = new String[planMinuteList.size()];
        for(int i = 0; i < planMinuteList.size(); i++)
        {
            planMinuteArray[i] = planMinuteList.get(i);
        }

        capacityMinuteArray = new String[CapacityMinuteList.size()];
        for(int i = 0; i < CapacityMinuteList.size(); i++)
        {
            capacityMinuteArray[i] = CapacityMinuteList.get(i);
        }

        double[] longPlan = new double[planMinuteArray.length];
        for (int i = 0; i < planMinuteArray.length; i++)
            longPlan[i] = Double.parseDouble(planMinuteArray[i]);

        Double plansum = Double.valueOf(0);
        for( Double num : longPlan) {
            plansum = plansum +num;
        }

        double[] longBooked = new double[capacityMinuteArray.length];
        for (int i = 0; i < capacityMinuteArray.length; i++)
            longBooked[i] = Double.parseDouble(capacityMinuteArray[i]);

        Double bookedsum = Double.valueOf(0);
        for( Double num : longBooked) {
            bookedsum = bookedsum +num;
        }

        planFirstTV.setText(String.valueOf(plansum));
        planTotalTV.setText(String.valueOf(plansum));
        bookedFirstTV.setText(String.valueOf(bookedsum));
        bookedTotalTV.setText(String.valueOf(bookedsum));

        balanceFirstTV.setText(String.valueOf(bookedsum-plansum));
        balanceTotalTV.setText(String.valueOf(bookedsum-plansum));


        for(int i = 0; i < rCompanyNameArray.length; i++){
            V1_CapacityVsPlanModelClass capacityVsPlanModelClass = new V1_CapacityVsPlanModelClass();
            capacityVsPlanModelClass.setCompanyName(rCompanyNameArray[i]);
            capacityVsPlanModelClass.setLocationName(rLocationNameArray[i]);
            capacityVsPlanModelClass.setMonth(monthNameArray[i]);
            capacityVsPlanModelClass.setPlan(String.valueOf(planMinuteArray[i]));
            capacityVsPlanModelClass.setCapacity(String.valueOf(capacityMinuteArray[i]));
            list.add(capacityVsPlanModelClass);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_CapacityVsPlanActivity.this, V1_MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.startDateBtn:
                datepicker(startButton);
                break;

            case R.id.endDateBtn:
                datepicker(endButton);
                break;

            case R.id.showButton:
                if(companyId != 0 && locationId != 0){
                    showReportFromServer();
                } else {
                    Toast.makeText(this, "Please Select Company and Location", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void datepicker(final Button setdate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(V1_CapacityVsPlanActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setdate.setText((monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void datePickerMethod(Button sDate) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, dateListener,Year, Month, Day);
        dpd.show();

    }
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month + 1;
            date = String.format("%d-%d", month, year);
            endButton.setText(date);
        }
    };
}