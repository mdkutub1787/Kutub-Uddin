package com.logicsoftbd.lsl.ui.v_1_ui.menu;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static android.graphics.Color.parseColor;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LineWiseHourlyProductionResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_MenuModelClass;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.home.HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.about.V1_GuestActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.approval.V1_ApprovalActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.config.V1_ConfigActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.home.V1_HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_CapacityVsAllocatedReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_CapacityVsPlanActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ConsolitatedOrderSummeryctivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_PlanVsBookedReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_PlanVsBookedVsCapacityActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ReportHomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ShipmentPendingReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ShipmentScheduleManagementActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_BatchDetailsActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class V1_v1_MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, V1_v1_MenuActivity.class);
        return intent;
    }

    private static final String TAG = "MenuActivity";
    private SessionManager session;
    private DBAdapter dbAdapter;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    //    private GraphView graph;
//    private GraphView graphLN;
//    private GraphView graphDT;
    private PieChartView pieChartView;

    private int userID = 0;
    private String urladdress;
    private String urladdressChk;
    private String barcode;

    private static final int MENU_ONE = Menu.NONE;
    private static final int MENU_TWO = Menu.NONE;
    private static final int MENU_THREE = Menu.NONE;

    SubMenu subMenuEntry, subMenuReport;
    private String urlstringbase, lineWiseHrProUrl;
    private Retrofit retrofit;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    public final ArrayList<String> menuNameList = new ArrayList<>();
    public final ArrayList<String> menuLocationList = new ArrayList<>();
    public final ArrayList<Integer> menuWisesaveList = new ArrayList<>();
    public final ArrayList<Integer> menuWiseupdateList = new ArrayList<>();

    public String[] menuArray, locationArray;
    public Integer[] updateArray, saveArray;
    private String[] reportMenuArray = {"Schedule Management", "Shipment Pending", "Consolidated Order Summery", "Plan vs Booked", "Plan vs Booked vs Capacity", "Capacity vs Plan", "Capacity vs Allocated"};

    private TextView _userName, _currectHourTargetTV, _dayTargetTV, _efficiencyTV, _plannedTV, _currectHourProductionTV, _dayProductionTV, _dhuTV, _varianceTV, _alterTV,
            _spotTV, _rejectTV, _totalTV, _companyNameTV, _locationTV, _floorTV, _lineTV;
    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //QC Entry Scan
    private int qc_entry = 1;
    //Finish fabric
    private int finish_fabric_entry = 2;
    Timer timer;
    private int companyId = 0, locationId = 0, floorId = 0, lineId = 0;
    private String base_url = "", companyName = "", locationName = "", floorName = "", lineName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_menu);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_menu);
        _userName = headerView.findViewById(R.id.usernameET);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        urladdressChk = intent.getStringExtra("url");

        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        lineName = (_preferences.getString("lineName", ""));
        floorName = (_preferences.getString("floorName", ""));
        base_url = (_preferences.getString("base_url", ""));

        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userID = intent.getIntExtra("userId", 0);

        }else {
            if(loginData.size() > 0){
                urladdress = loginData.get(0).getUrl();
                userID = Integer.parseInt(loginData.get(0).getUserId());
            }
        }
        _userName.setText(String.valueOf(urladdress));
        pieChartView = findViewById(R.id.chart);

        piechart();

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new SessionManager(getApplicationContext());

        dbAdapter = new DBAdapter(this);

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        initialization();
        lineWiseHourlyProductionRequest();
        sendRequestToServer();

        menuMethodReport();
        onMenuDrawerMethod(navigationView);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lineWiseHourlyProductionRequest();
            }
        },0, 5000);
    }

    private void lineWiseHourlyProductionRequest() {

        apiInterface.homeDataResponseCall(String.valueOf(companyId), String.valueOf(locationId), String.valueOf(floorId), String.valueOf(lineId), "", "", "").enqueue(new Callback<V1_LineWiseHourlyProductionResponse>() {
            @Override
            public void onResponse(Call<V1_LineWiseHourlyProductionResponse> call, Response<V1_LineWiseHourlyProductionResponse> response) {
                if(response.isSuccessful()){
                    if(!response.body().getResultset().getHomeData().getMsg().equals("This line is not allocated in actual production resource entry page!")){
                        _currectHourTargetTV.setText(response.body().getResultset().getHomeData().getHourlyTarget());
                        _dayTargetTV.setText(response.body().getResultset().getHomeData().getDayTarget());
                        _efficiencyTV.setText(String.format("%.1f", Double.parseDouble(response.body().getResultset().getHomeData().getEfficiency())));
                        _plannedTV.setText("Planned: "+response.body().getResultset().getHomeData().getPlanned());
                        _currectHourProductionTV.setText(response.body().getResultset().getHomeData().getCurHourQty());
                        _dayProductionTV.setText(response.body().getResultset().getHomeData().getDayTotalQty());
                        _dhuTV.setText(String.format("%.1f", Double.parseDouble(response.body().getResultset().getHomeData().getDhu())));
                        _varianceTV.setText(response.body().getResultset().getHomeData().getVarience());

                        int alter = response.body().getResultset().getHomeData().getAlterQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getAlterQty());
                        int spot = response.body().getResultset().getHomeData().getSpotQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getSpotQty());
                        int reject =  response.body().getResultset().getHomeData().getRejectQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getRejectQty());

                        _alterTV.setText(String.valueOf(alter));
                        _spotTV.setText(String.valueOf(spot));
                        _rejectTV.setText(String.valueOf(reject));
                        int total = alter + spot + reject;
                        _totalTV.setText(String.valueOf(total));
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_LineWiseHourlyProductionResponse> call, Throwable t) {

            }
        });
    }

    private void initialization() {
        _currectHourTargetTV = findViewById(R.id.currectHourTargetTV);
        _dayTargetTV = findViewById(R.id.dayTargetTV);
        _efficiencyTV = findViewById(R.id.efficiencyTV);
        _plannedTV = findViewById(R.id.plannedTV);
        _currectHourProductionTV = findViewById(R.id.currectHourProductionTV);
        _dayProductionTV = findViewById(R.id.dayProductionTV);
        _dhuTV = findViewById(R.id.dhuTV);
        _varianceTV = findViewById(R.id.varianceTV);
        _alterTV = findViewById(R.id.alterTV);
        _spotTV = findViewById(R.id.spotTV);
        _rejectTV = findViewById(R.id.rejectTV);
        _totalTV = findViewById(R.id.totalTV);
        _companyNameTV = findViewById(R.id.companyNameTV);
        _locationTV = findViewById(R.id.locationTV);
        _floorTV = findViewById(R.id.floorTV);
        _lineTV = findViewById(R.id.lineTV);

        if(companyName != null && locationName != null && floorName != null && lineName != null){
            _companyNameTV.setText("Company Name: "+companyName);
            _locationTV.setText("Location Name: "+locationName);
            _floorTV.setText("Floor Name: "+floorName);
            _lineTV.setText("Line Name: "+lineName);
        }
    }

    private void menuMethodReport() {
        Menu menu = navigationView.getMenu();
        subMenuReport = menu.addSubMenu(getString(R.string.report));
        for(int i=0; i<reportMenuArray.length; i++)
        {
            subMenuReport.add(i, Menu.CATEGORY_SECONDARY+i, Menu.CATEGORY_SECONDARY, reportMenuArray[i]);
        }

    }

    private void sendRequestToServer() {
        apiInterface.menuResponseCall(String.valueOf(userID)).enqueue(new Callback<V1_MenuModelClass>() {
            @Override
            public void onResponse(Call<V1_MenuModelClass> call, Response<V1_MenuModelClass> response) {
                if(response.isSuccessful())
                {
                    V1_MenuModelClass.Datum menuName;
                    List<V1_MenuModelClass.Datum> menunames = response.body().getData();
                    for(V1_MenuModelClass.Datum d : menunames)
                    {
                        menuName = d;
                        final V1_MenuModelClass.Datum menuName1 = menuName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menuNameList.add(String.valueOf(menuName1.getMenuName()));
                            }
                        });
                    }

                    menuArray = new String[menuNameList.size()];
                    for(int i = 0; i < menuNameList.size(); i++)
                    {
                        menuArray[i] = menuNameList.get(i);
                    }

                    V1_MenuModelClass.Datum menuLocation;
                    for(V1_MenuModelClass.Datum d : menunames)
                    {
                        menuLocation = d;
                        final V1_MenuModelClass.Datum menuName1 = menuLocation;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menuLocationList.add(String.valueOf(menuName1.getLocation()));
                            }
                        });
                    }

                    locationArray = new String[menuLocationList.size()];
                    for(int i = 0; i < menuLocationList.size(); i++)
                    {
                        locationArray[i] = menuLocationList.get(i);
                    }

                    Log.d(TAG, "onResponse: "+ locationArray.toString());

                    Menu menu = navigationView.getMenu();
                    subMenuEntry = menu.addSubMenu(getString(R.string.entry));
                    for(int i=0; i<menuArray.length; i++)
                    {
                        subMenuEntry.add(i, Menu.FIRST+i, Menu.FIRST, menuArray[i]);
                    }

                    V1_MenuModelClass.Datum menuSave;
                    List<V1_MenuModelClass.Datum> menusaves = response.body().getData();
                    for(V1_MenuModelClass.Datum d : menusaves)
                    {
                        menuSave = d;
                        final V1_MenuModelClass.Datum menuName1 = menuSave;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menuWisesaveList.add(menuName1.getSave());
                            }
                        });
                    }

                    saveArray = new Integer[menuWisesaveList.size()];
                    for(int i = 0; i < menuWisesaveList.size(); i++)
                    {
                        saveArray[i] = menuWisesaveList.get(i);
                    }

                    V1_MenuModelClass.Datum menuUpdate;
                    List<V1_MenuModelClass.Datum> menuUpdates = response.body().getData();
                    for(V1_MenuModelClass.Datum d : menuUpdates)
                    {
                        menuUpdate = d;
                        final V1_MenuModelClass.Datum menuName1 = menuUpdate;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menuWiseupdateList.add(menuName1.getUpdate());
                            }
                        });
                    }

                    updateArray = new Integer[menuWiseupdateList.size()];
                    for(int i = 0; i < menuWiseupdateList.size(); i++)
                    {
                        updateArray[i] = menuWiseupdateList.get(i);
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<V1_MenuModelClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_v1_MenuActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    /*Toast.makeText(MenuActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();*/
                }
            }
        });
    }

    private void onMenuDrawerMethod(NavigationView navigationView) {

    }

    private void logoutUser() {
        session.setLogin(false);
        dbAdapter.deleteUsers();

        Intent intent = HomeActivity.getStartIntent(V1_v1_MenuActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.v1_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                logoutUser();
                break;
            case R.id.refresh:
                Intent intent = V1_v1_MenuActivity.getStartIntent(V1_v1_MenuActivity.this);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void piechart() {
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, BLUE).setLabel("Q1: $10"));
        pieData.add(new SliceValue(25, GRAY).setLabel("Q2: $4"));
        pieData.add(new SliceValue(10, RED).setLabel("Q3: $18"));
        pieData.add(new SliceValue(30, YELLOW).setLabel("Q4: $28"));
        pieData.add(new SliceValue(15, BLACK).setLabel("Q5: $280"));
        pieData.add(new SliceValue(15, GREEN).setLabel("Q6: $80"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Logic Software Ltd.").setCenterText1FontSize(20).setCenterText1Color(parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_approval){
            Intent intent = V1_ApprovalActivity.getStartIntent(V1_v1_MenuActivity.this);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        } else if (id == R.id.nav_swing_config) {
            Intent intent = V1_ConfigActivity.getStartIntent(V1_v1_MenuActivity.this);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        } else if (id == R.id.nav_swing_input) {
            Intent intent = V1_SewingInputActivity.getStartIntent(V1_v1_MenuActivity.this);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        } else if (id == R.id.nav_swing_output) {
            Intent intent = new Intent(this, V1_SewingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }
        else if (id == R.id.nav_report) {
            Intent intent = new Intent(this, V1_ReportHomeActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }
        else if (id == R.id.nav_swing_output) {
            Intent intent = new Intent(this, V1_SewingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        }
        else if (id == R.id.linewisenav_swing_input) {
            Intent intent = new Intent(this, V1_LineWiseSewingInputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        }
        else if (id == R.id.linewisenav_swing_output) {
            Intent intent = new Intent(this, V1_LineWiseSewingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        }
        else if (id == R.id.linking_input) {
            Intent intent = new Intent(this, V1_LinkingInputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        }
        else if (id == R.id.linking_output) {
            Intent intent = new Intent(this, V1_LinkingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);

        }
        else if(id == R.id.nav_bundle_tracking){
            Intent intent = new Intent(this, V1_ReportHomeActivity.class);
            intent.putExtra("status", 2);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, V1_GuestActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_share) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra("userId", userID);
//            intent.putExtra("url", "192.168.21.240/erp_test");
//            intent.putExtra("s", 1);
//            intent.putExtra("u", 1);
//            intent.putExtra("qc", "grey_fabric_qc_entry");
//            startActivity(intent);
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_swing_output) {
            Intent intent = new Intent(V1_v1_MenuActivity.this, V1_SewingOutputActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_grey_fabric_qc_observation) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra("userId", userID);
//            intent.putExtra("url", urladdress);
//            intent.putExtra("s", saveArray[0]);
//            intent.putExtra("u", updateArray[0]);
//            intent.putExtra("qc", qc_entry);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_finish_fabric_qc_observation) {
//            Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
//            intent.putExtra("userId", userID);
//            intent.putExtra("url", "203.82.196.18/erp");
//            intent.putExtra("s", saveArray[2]);
//            intent.putExtra("u", updateArray[2]);
//            intent.putExtra("qc", locationArray[2]);
//            startActivity(intent);
            Intent intent = new Intent(this, V1_BatchDetailsActivity.class);
            startActivity(intent);
        }
        else if(id == subMenuReport.getItem(0).getItemId()){
            Intent intent = new Intent(this, V1_ShipmentScheduleManagementActivity.class);
            startActivity(intent);
        }else if(id == subMenuReport.getItem(1).getItemId()){
            Intent intent = new Intent(this, V1_ShipmentPendingReportActivity.class);
            startActivity(intent);
        }else if(id == subMenuReport.getItem(2).getItemId()){
            Intent intent = new Intent(this, V1_ConsolitatedOrderSummeryctivity.class);
            startActivity(intent);
        }
        else if(id == subMenuReport.getItem(3).getItemId()){
            Intent intent = new Intent(this, V1_PlanVsBookedReportActivity.class);
            startActivity(intent);
        }
        else if(id == subMenuReport.getItem(4).getItemId()){
            Intent intent = new Intent(this, V1_PlanVsBookedVsCapacityActivity.class);
            startActivity(intent);
        }
        else if(id == subMenuReport.getItem(5).getItemId()){
            Intent intent = new Intent(this, V1_CapacityVsPlanActivity.class);
            startActivity(intent);
        }else if(id == subMenuReport.getItem(6).getItemId()){
            Intent intent = new Intent(this, V1_CapacityVsAllocatedReportActivity.class);
            startActivity(intent);
        }
//        else if(id == subMenuReport.getItem(3).getItemId()){
//            Intent intent = new Intent(this, ChallanReportActivity.class);
////            intent.putExtra("status", 3);
////            intent.putExtra("userId", userID);
////            intent.putExtra("url", urladdress);
//            startActivity(intent);
//        }

        else if (id == subMenuEntry.getItem(0).getItemId()) {
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", saveArray[0]);
            intent.putExtra("u", updateArray[0]);
            intent.putExtra("qc", locationArray[0]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(1).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", saveArray[1]);
            intent.putExtra("u", updateArray[1]);
            intent.putExtra("qc", locationArray[1]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(2).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", saveArray[2]);
            intent.putExtra("u", updateArray[2]);
            intent.putExtra("qc", locationArray[2]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(3).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", saveArray[3]);
            intent.putExtra("u", updateArray[3]);
            intent.putExtra("qc", locationArray[3]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(4).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", saveArray[4]);
            intent.putExtra("u", updateArray[4]);
            intent.putExtra("qc", locationArray[4]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(5).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("type", 11);
            intent.putExtra("s", saveArray[5]);
            intent.putExtra("u", updateArray[5]);
            intent.putExtra("qc", locationArray[5]);
            startActivity(intent);
        }
        else if(id == subMenuEntry.getItem(6).getItemId()){
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("type", 12);
            intent.putExtra("s", saveArray[6]);
            intent.putExtra("u", updateArray[6]);
            intent.putExtra("qc", locationArray[6]);
            startActivity(intent);
        }

//        drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;  //Toast.makeText(this, updateArray[0], Toast.LENGTH_SHORT).show();
    }
}
