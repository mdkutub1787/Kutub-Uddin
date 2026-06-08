package com.logicsoftbd.lsl.ui.v_1_ui.menu;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static android.graphics.Color.parseColor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalNotificationsModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApkVersionResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalResponseModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LineWiseHourlyProductionResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LoginResponse;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.compacting.CompactingActivity;
import com.logicsoftbd.lsl.ui.compacting.CompactingRollWiseActivity;
import com.logicsoftbd.lsl.ui.dyeingProduction.DyeingProductionActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricQrCodeActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.ui.slitting_squeezing.SlittingSqeezingRollWiseActivity;
import com.logicsoftbd.lsl.ui.slitting_squeezing.SlittingSqueezingActivity;
import com.logicsoftbd.lsl.ui.stentering.StenteringActivity;
import com.logicsoftbd.lsl.ui.stentering.StenteringRollWiseActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.apk_update.V1_ApkUpdateActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bi.V1_BI_01_Activity;
import com.logicsoftbd.lsl.ui.v_1_ui.bundle_wise_sewing_pcs.V1_BundleWiseSewingInput_PCSActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity_v1;
import com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting.V1_MeetingArchiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.config.V1_StyleWiseLineConfigActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.config.V1_ConfigActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.config.V1_StyleWiseLineConfigActivity_v3;
import com.logicsoftbd.lsl.ui.v_1_ui.cutting.V1_CuttingStoreIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.cutting.V1_CuttingStoreReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.device_session.V1_ActiveDeviceStatusActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.dyeing_production.V1_DyeingProductionPDAActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval.V1_ElectronicApprovalActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_AOPBagKeepingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_AOPDeptBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagDeliveryActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingPrintActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingQcActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagReturnActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_DyedAOPDeptBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_EmptyBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_GreyStoreRejectFabricBagActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.V1_Finish_Fabric_Receive;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive_old.V1_FinishFabricReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric_roll_receive.V1_GmtFinishReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finishing.V1_FabricFinishingQCActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finishing.V1_FinishingForPdaActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_issue.V1_GreyFabricRollIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_issue.V1_GreyFabricRollIssueForPDODeviceActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_receive.V1_GreyRollReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_receive.V1_GreyRollReceiveForPDODeviceActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.home.V1_HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.production_operation.V1_ProductionOperationActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_CapacityVsAllocatedReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_CapacityVsPlanActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ConsolitatedOrderSummeryctivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_PlanVsBookedReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_PlanVsBookedVsCapacityActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ReportHomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ShipmentPendingReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ShipmentScheduleManagementActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.roll_wise_gray_fabric_delivery_store.V1_RollWiseGreyFabricDeliveryToStoreActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.roll_wise_gray_fabric_delivery_store.V1_RollWiseGreyFabricDeliveryToStorePDAActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.settings.V1_SettingsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_StyleWiseSewingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_StyleWiseSewingActivity_v3;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_StyleWiseSewingActivity_v4;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_StyleWiseSewingActivity_v6;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_StyleWiseSewingActivity_v8;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in.V1_TransferInActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in.V1_TransferInForPDADeviceActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out.V1_TransferoutActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out.V1_TransferoutForPDADeviceActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_BatchDetailsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_FinishFabricScannerDashboardActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid.V1_RFIDTagForYarnIssueReturnActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid.V1_YarnRfidReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid.V1_YarnTransferEntryActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.viewModel.ApprovalViewModel;

import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_MenuActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {
    private static final String TAG = "V1_MenuActivity";
    private static final int NOTIFICATION_PERMISSION_CODE = 123;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, V1_MenuActivity.class);
        return intent;
    }
    private SessionManager session;
    private DBAdapter dbAdapter;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private PackageInfo pInfo;
    Toolbar toolbar;
    private DrawerLayout drawer;
    private ExpandableListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private PieChartView pieChartView;
    private TextView _urlTV, _userName, _versionTV, _currectHourTargetTV, _dayTargetTV, _efficiencyTV, _efficiencyMinTV, _plannedTV, _dayTotalQntyTV, _currectHourProductionTV, _checkedQuantityTV, _dayProductionTV, _dhuTV, _varianceTV, _alterTV,
            _spotTV, _rejectTV, _totalTV, _companyNameTV, _locationTV, _floorTV, _lineTV, _textNotificationItemCount;
    private ProgressBar progressBar;

    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    HashMap<String, List<String>> listDataChildLink = new HashMap<String, List<String>>();

    List<String> module_List = new ArrayList<>();
    List<String> menu_list_01 =new ArrayList<>();
    List<String> menu_list_link_01 =new ArrayList<>();
    List<String> menu_list_02 =new ArrayList<>();
    List<String> menu_list_link_02 =new ArrayList<>();
    List<String> menu_list_03 =new ArrayList<>();
    List<String> menu_list_link_03 =new ArrayList<>();
    List<String> menu_list_04 =new ArrayList<>();
    List<String> menu_list_link_04 =new ArrayList<>();
    List<String> menu_list_05 =new ArrayList<>();
    List<String> menu_list_link_05 =new ArrayList<>();
    List<String> menu_list_06 =new ArrayList<>();
    List<String> menu_list_link_06 =new ArrayList<>();
    List<String> menu_list_07 =new ArrayList<>();
    List<String> menu_list_link_07 =new ArrayList<>();
    List<String> menu_list_08 =new ArrayList<>();
    List<String> menu_list_link_08 =new ArrayList<>();
    List<String> menu_list_09 =new ArrayList<>();
    List<String> menu_list_link_09 =new ArrayList<>();
    List<String> menu_list_10 =new ArrayList<>();
    List<String> menu_list_link_10 =new ArrayList<>();
    List<String> menu_list_11 =new ArrayList<>();
    List<String> menu_list_link_11 =new ArrayList<>();
    List<String> menu_list_12 =new ArrayList<>();
    List<String> menu_list_link_12 =new ArrayList<>();
    List<String> menu_list_13 =new ArrayList<>();
    List<String> menu_list_link_13 =new ArrayList<>();
    List<String> menu_list_14 =new ArrayList<>();
    List<String> menu_list_link_14 =new ArrayList<>();
    List<Process> processList= new ArrayList();

    private int companyId = 0, locationId = 0, floorId = 0, lineId = 0, totalBadge = 0;
    private String deviceToken = "", base_url = "", base_url_full = "", userID = "", username = "", password = "", macAddress="", urladdress = "", companyName = "", locationName = "", floorName = "", lineName = "", version="", item_numberId = "", countryId ="", jobId="";

    Button positiveDialog;
    Button negativeDialog;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    private ApprovalViewModel approvalViewModel;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showSystemUI();
        setContentView(R.layout.activity_v1_menu_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        macAddress = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        try {
            pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        session = new SessionManager(getApplicationContext());
        dbAdapter = new DBAdapter(this);

        initUI();
        piechart();
        initDrawer();
        checkApkVersion();
        lineWiseHourlyProductionRequest();
    }
    private void showSystemUI() {

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: "+intent.toString());
        Log.d(TAG, "handleIntent: "+intent.getStringExtra("cmp"));
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.MAIN")) {
                // The app was launched from the launcher icon
            } else if (intent.getAction().equals("your.custom.action.FCM_NOTIFICATION")) {
                // The app was launched from an FCM notification
                // Handle the notification here, if needed
            }
        }
    }

    private void initUI() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        lineName = (_preferences.getString("lineName", ""));
        floorName = (_preferences.getString("floorName", ""));
        item_numberId = _preferences.getString("itemNumber", "");
        countryId = _preferences.getString("country", "");
        jobId = (_preferences.getString("jobId", ""));
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);
        Log.d(TAG, "initUI: "+base_url);

//        flooeWiseline(floorId);

        urladdress = _preferences.getString("login_url", "");
        userID = _preferences.getString("login_userid", "");
        username = _preferences.getString("login_username", "");
        password = _preferences.getString("login_password", "");
        pieChartView = findViewById(R.id.chart);

        progressBar = findViewById(R.id.progressBar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        _urlTV = headerView.findViewById(R.id.urlET);
        _userName = headerView.findViewById(R.id.usernameET);
        _versionTV = headerView.findViewById(R.id.versionTV);
        _currectHourTargetTV = findViewById(R.id.currectHourTargetTV);
        _dayTargetTV = findViewById(R.id.dayTargetTV);
        _efficiencyTV = findViewById(R.id.efficiencyTV);
        _efficiencyMinTV = findViewById(R.id.efficiencyMinTV);
        _plannedTV = findViewById(R.id.plannedTV);
        _dayTotalQntyTV = findViewById(R.id.dayTotalQntyTV);
        _currectHourProductionTV = findViewById(R.id.currectHourProductionTV);
        _checkedQuantityTV = findViewById(R.id.checkedQuantityTV);
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
        String[] spitesUrl = base_url.split("/");
        Log.d(TAG, "initUI: "+spitesUrl.length);
        if(spitesUrl.length <= 3){
            _urlTV.setText(spitesUrl[2]);
        }else{
            _urlTV.setText(spitesUrl[2]+"/"+spitesUrl[3]);
        }

        _userName.setText(username);
        _versionTV.setText("version-"+version);


        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            deviceToken = task.getResult();
            Log.d("token ##########", deviceToken);
        });
    }

    private void initDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);
        prepareListData();
        drawerList.setOnChildClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer, toolbar, R.string.drawer_open , R.string.drawer_close ){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void flooeWiseline(int floorId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        apiInterface.getBundleWiseLocationWiseFloorClassCall(companyId, locationId, floorId, currentDate).enqueue(new Callback<V1_BundleWiseFloorWiseLineClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseFloorWiseLineClass> call, Response<V1_BundleWiseFloorWiseLineClass> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    if(response.body().getResultset().size() == 0){
                        showAlertMessage("You have to configure your device.", 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseFloorWiseLineClass> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (t instanceof IOException) {
                    Toast.makeText(V1_MenuActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void prepareListData() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.loginResponseCall(username, password, macAddress, deviceToken, "android").enqueue(new Callback<V1_LoginResponse>() {
            @Override
            public void onResponse(Call<V1_LoginResponse> call, Response<V1_LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful())
                {
                    if(response.body().getResultset() != null){
                        try{
                            if(response.body().getResultset().getModule().size() > 0 && response.body().getResultset().getModule() != null){
                                if(response.body().getResultset().getModule() != null){
                                    for(int i=0; i<response.body().getResultset().getModule().size(); i++){
                                        module_List.add(response.body().getResultset().getModule().get(i).getModuleName());
                                    }
                                }
                                for(int i=0; i<response.body().getResultset().getModule().size(); i++){
                                    if(response.body().getResultset().getModule().get(i).getModuleWiseMenu() != null ) {
                                        for(int j=0; j < response.body().getResultset().getModule().get(i).getModuleWiseMenu().size(); j++){
                                            if(i==0){
                                                menu_list_01.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_01.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==1){
                                                menu_list_02.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_02.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==2){
                                                menu_list_03.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_03.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==3){
                                                menu_list_04.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_04.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==4){
                                                menu_list_05.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_05.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==5){
                                                menu_list_06.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_06.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==6){
                                                menu_list_07.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_07.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }else if(i==7){
                                                menu_list_08.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenu());
                                                menu_list_link_08.add(response.body().getResultset().getModule().get(i).getModuleWiseMenu().get(j).getMenuLink());
                                            }
                                        }
                                    }
                                }

                                listDataHeader = module_List;
                                for(int i=0; i<module_List.size(); i++){
                                    if(i==0){
                                        listDataChild.put(listDataHeader.get(i), menu_list_01);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_01);
                                    }else if(i==1){
                                        listDataChild.put(listDataHeader.get(i), menu_list_02);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_02);
                                    }else if(i==2){
                                        listDataChild.put(listDataHeader.get(i), menu_list_03);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_03);
                                    }else if(i==3){
                                        listDataChild.put(listDataHeader.get(i), menu_list_04);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_04);
                                    }else if(i==4){
                                        listDataChild.put(listDataHeader.get(i), menu_list_05);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_05);
                                    }else if(i==5){
                                        listDataChild.put(listDataHeader.get(i), menu_list_06);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_06);
                                    }else if(i==6){
                                        listDataChild.put(listDataHeader.get(i), menu_list_07);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_07);
                                    }else if(i==7){
                                        listDataChild.put(listDataHeader.get(i), menu_list_08);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_08);
                                    }else if(i==8){
                                        listDataChild.put(listDataHeader.get(i), menu_list_09);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_09);
                                    }else if(i==9){
                                        listDataChild.put(listDataHeader.get(i), menu_list_10);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_10);
                                    }else if(i==10){
                                        listDataChild.put(listDataHeader.get(i), menu_list_11);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_11);
                                    }else if(i==11){
                                        listDataChild.put(listDataHeader.get(i), menu_list_12);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_12);
                                    }else if(i==12){
                                        listDataChild.put(listDataHeader.get(i), menu_list_13);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_13);
                                    }else if(i==13){
                                        listDataChild.put(listDataHeader.get(i), menu_list_14);
                                        listDataChildLink.put(listDataHeader.get(i), menu_list_link_14);
                                    }

                                }
                                drawerList.setAdapter(new V1_MenuAdapter(getApplicationContext(), listDataHeader, listDataChild));
                            }
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: #####"+e.getMessage());
                        }

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(V1_MenuActivity.this, R.string.api_default_error, Toast.LENGTH_SHORT).show();

            }
        });

        fetchBatchData();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if (message != null && message.equals("Approval")) {
                    totalBadge = 0;
                    fetchBatchData();
                }
            }
        };
    }

    private void fetchBatchData() {
        approvalViewModel = new ViewModelProvider(this).get(ApprovalViewModel.class);

        approvalViewModel.getApprovalResponse(userID).observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getData() != null) {
                List<V1_ApprovalNotificationsModel.Datum> approvalList = apiResponse.getData();
                for( V1_ApprovalNotificationsModel.Datum item : approvalList){
                    totalBadge += Integer.valueOf(item.getNotifications());
                }
                setupBadge();
            }

        });
    }

    private void checkApkVersion() {
        apiInterface.getAppVersionCheckResponseCall().enqueue(new Callback<V1_ApkVersionResponse>() {
            @Override
            public void onResponse(Call<V1_ApkVersionResponse> call, Response<V1_ApkVersionResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "onResponse: ############### Success ######### "+ response.body().getData().getVersion());
                    _versionTV.setText("Version-"+version);
                    Log.d(TAG, "checkApkVersion: ############# "+version);
                    if(!version.equals(response.body().getData().getVersion())){
                        alertForApkUpdate(response.body().getData().getAppUrl(), response.body().getData().getVersion());
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_ApkVersionResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ############## Failed ##########");
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

            String[] spitesBaseUrl = base_url.split("/");
            Log.d(TAG, "initUI: "+spitesBaseUrl.length);

            if(spitesBaseUrl.length == 3){
                newUrl = spitesBaseUrl[0]+"/"+spitesBaseUrl[1]+"/"+spitesBaseUrl[2]+"/"+spitesBaseUrl[3]+"/"+result;
            }else{
                newUrl = spitesBaseUrl[0]+"/"+spitesBaseUrl[2]+"/"+result;
            }
            Log.d(TAG, "alertForApkUpdate: "+newUrl);

            Intent intent = new Intent(this, V1_ApkUpdateActivity.class);
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


    private void lineWiseHourlyProductionRequest() {
        Log.d(TAG, "lineWiseHourlyProductionRequest: "+companyId+" "+locationId+" "+floorId+" "+lineId+ " "+countryId+" "+item_numberId+" "+jobId);
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.homeDataResponseCall(String.valueOf(companyId), String.valueOf(locationId), String.valueOf(floorId), String.valueOf(lineId), countryId, item_numberId, jobId).enqueue(new Callback<V1_LineWiseHourlyProductionResponse>() {
            @Override
            public void onResponse(Call<V1_LineWiseHourlyProductionResponse> call, Response<V1_LineWiseHourlyProductionResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "onResponse: "+ response.toString());
                if(response.isSuccessful()){
                    try {
                        if(!response.body().getResultset().getHomeData().getMsg().equals("This line is not allocated in actual production resource entry page!") && response.body().getResultset().getHomeData().getMsg().equals("")){
                            _currectHourTargetTV.setText(response.body().getResultset().getHomeData().getHourlyTarget() == null ? "" : response.body().getResultset().getHomeData().getHourlyTarget());
                            _dayTargetTV.setText(response.body().getResultset().getHomeData().getDayTarget() == null ? "" : response.body().getResultset().getHomeData().getDayTarget());
                            if(!response.body().getResultset().getHomeData().getEfficiency().equals("inf")){
                                _efficiencyTV.setText(response.body().getResultset().getHomeData().getEfficiency() == null ? "" : String.format("%.1f", Double.parseDouble(response.body().getResultset().getHomeData().getEfficiency())));
                            }
                            if(!response.body().getResultset().getHomeData().getEfficiency_min().equals("inf")){
                                _efficiencyMinTV.setText(response.body().getResultset().getHomeData().getEfficiency_min() == null ? "" : String.format("%.1f", Double.parseDouble(response.body().getResultset().getHomeData().getEfficiency_min())));
                            }
                            _plannedTV.setText("Planned: "+response.body().getResultset().getHomeData().getPlanned() == null ? "" : "Planned: "+String.format("%.2f", Double.parseDouble(response.body().getResultset().getHomeData().getPlanned())));
                            _dayTotalQntyTV.setText("Input: "+response.body().getResultset().getHomeData().getDayTotalQty() == null ? "" : "Input: "+response.body().getResultset().getHomeData().getToDayInputQty());
                            _currectHourProductionTV.setText(response.body().getResultset().getHomeData().getCurHourQty() == null ? "" : response.body().getResultset().getHomeData().getCurHourQty());
                            _checkedQuantityTV.setText(response.body().getResultset().getHomeData().getCheckQty() == null ? "" : response.body().getResultset().getHomeData().getCheckQty());
                            _dayProductionTV.setText(response.body().getResultset().getHomeData().getDayTotalQty() == null ? "" : response.body().getResultset().getHomeData().getDayTotalQty());
                            _dhuTV.setText(response.body().getResultset().getHomeData().getDhu() == null ? "" : String.format("%.2f", Double.parseDouble(response.body().getResultset().getHomeData().getDhu())));
                            _varianceTV.setText(response.body().getResultset().getHomeData().getVarience() == null ? "" : response.body().getResultset().getHomeData().getVarience());

                            int alter = response.body().getResultset().getHomeData().getAlterQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getAlterQty());
                            int spot = response.body().getResultset().getHomeData().getSpotQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getSpotQty());
                            int reject =  response.body().getResultset().getHomeData().getRejectQty() == null? 0: Integer.parseInt(response.body().getResultset().getHomeData().getRejectQty());

                            _alterTV.setText(String.valueOf(alter));
                            _spotTV.setText(String.valueOf(spot));
                            _rejectTV.setText(String.valueOf(reject));
                            int total = alter + spot + reject;
                            _totalTV.setText(String.valueOf(total));
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: ");
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_LineWiseHourlyProductionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: Failed");
            }
        });
    }

    public void sendBarcodeData(String barcodeData, String identifier) {
        Intent intent = new Intent("com.example.ACTION_BARCODE_DATA"); // Ensure this matches the filter in MainActivity
        intent.putExtra("com.symbol.datawedge.data_string", barcodeData);
        intent.putExtra("identifier", identifier);
        sendBroadcast(intent);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
//        Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition)
//                + " : "
//                + listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();


        if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_With_Observation")) {
            Intent intent = new Intent(this, V1_BatchDetailsActivity.class);
//            Intent intent = new Intent(this, V1_Finish_Fabric_Receive.class);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_QC_Entry_V1")) {
            Intent intent = new Intent(this, V1_ScannerActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Grey_Fabric_QC_Entry_V1");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Gtm_Finish_Fabric_Roll_Receive")) {
            Intent intent = new Intent(this, V1_Finish_Fabric_Receive.class);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Production_In_operation_Level")) {
            Intent intent = new Intent(this, V1_ProductionOperationActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Production_In_operation_Level");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("grey roll receive-v1")) {
            Intent intent = new Intent(this, V1_GreyRollReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "grey roll receive-v1");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("grey roll receivepdo-v1")) {
            Intent intent = new Intent(this, V1_GreyRollReceiveForPDODeviceActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "grey roll receivepdo-v1");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_Roll_Issue_Entry_V1")) {
            Intent intent = new Intent(this, V1_GreyFabricRollIssueActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Grey_Fabric_Roll_Issue_Entry_V1");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("grey roll issue pdo-v1")) {
            Intent intent = new Intent(this, V1_GreyFabricRollIssueForPDODeviceActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "grey roll issue pdo-v1");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_With_Observation")) {
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Grey_Fabric_With_Observation");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_QC_Entry")) {
            Intent intent = new Intent(this, V1_ScannerActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Finish_Fabric_QC_Entry");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_QC_Entry_V2")) {
            Intent intent = new Intent(this, V1_FinishFabricScannerDashboardActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Finish_Fabric_QC_Entry_V2");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("gmts_finish_receive_v1")) {
            Intent intent = new Intent(this, V1_GmtFinishReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_With_Observation")) {
            Intent intent = new Intent(this, V1_HomeActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "Finish_Fabric_With_Observation");
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Line_Wise_Tab_Config")) {
            Intent intent = new Intent(this, V1_ConfigActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bag_keeping_v1")) {
            Intent intent = new Intent(this, V1_BagKeepingActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("finishing_v1")) {
            Intent intent = new Intent(this, V1_FinishingForPdaActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("finishing_qc_v1")) {
            Intent intent = new Intent(this, V1_FabricFinishingQCActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("fabric_bag_print_v1")) {
            Intent intent = new Intent(this, V1_BagKeepingPrintActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("aop_fabric_bag_keeping_v1")) {
            Intent intent = new Intent(this, V1_AOPBagKeepingActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("fabri_ empty_bag_receive")) {
            Intent intent = new Intent(this, V1_EmptyBagReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("aop_dept_bag_receive_v1")) {
            Intent intent = new Intent(this, V1_AOPDeptBagReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("finish_fabric_store_dyed_aop_fabric")) {
            Intent intent = new Intent(this, V1_DyedAOPDeptBagReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("grey_store_dyeing_reject_fabric_bag")) {
            Intent intent = new Intent(this, V1_GreyStoreRejectFabricBagActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Quality_For_Fabric_Bag_Entry_v1")) {
            Intent intent = new Intent(this, V1_BagKeepingQcActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bag_delivery_v1")) {
            Intent intent = new Intent(this, V1_BagDeliveryActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bag_receive_v1")) {
            Intent intent = new Intent(this, V1_BagReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bag_issue_v1")) {
            Intent intent = new Intent(this, V1_BagIssueActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bag_return_v1")) {
            Intent intent = new Intent(this, V1_BagReturnActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Roll_Receive")) {
            Intent intent = new Intent(this, V1_FinishFabricReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Sewing_Input_v1")) {
            Intent intent = new Intent(this, V1_SewingInputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("bundle_wise_sewing_input_pcs_v1")) {
            Intent intent = new Intent(this, V1_BundleWiseSewingInput_PCSActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Gray_Fabric_Delivery_To_Store -V1")) {
            Intent intent = new Intent(this, V1_RollWiseGreyFabricDeliveryToStoreActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Gray_Fabric_Delivery_To_StorePda -V1")) {
            Intent intent = new Intent(this, V1_RollWiseGreyFabricDeliveryToStorePDAActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Sewing_Output_V1")) {
            Intent intent = new Intent(this, V1_StyleWiseSewingActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Sewing_Output_V3")) {
            Intent intent = new Intent(this, V1_StyleWiseSewingActivity_v3.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Sewing_Output_V4")) {
            Intent intent = new Intent(this, V1_StyleWiseSewingActivity_v4.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Sewing_Output_V5")) {
            Intent intent = new Intent(this, V1_StyleWiseSewingActivity_v6.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Sewing_Output_V6")) {
            Intent intent = new Intent(this, V1_StyleWiseSewingActivity_v8.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Yarn_RFID_Receive")) {
            Intent intent = new Intent(this, V1_YarnRfidReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("rfid_tag_for_yarn_issue_return_v1")) {
            Intent intent = new Intent(this, V1_RFIDTagForYarnIssueReturnActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Yarn_Transfer_Store")) {
            Intent intent = new Intent(this, V1_YarnTransferEntryActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("dyeing_production_v1")) {
            Intent intent = new Intent(this, V1_DyeingProductionPDAActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Cutting_Store_Receive_v1")) {
            Intent intent = new Intent(this, V1_CuttingStoreReceiveActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("cutting_store_issue_v1")) {
            Intent intent = new Intent(this, V1_CuttingStoreIssueActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Sewing_Output_v1")) {
            Intent intent = new Intent(this, V1_SewingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Sewing_Output_v3")) {
            Intent intent = new Intent(this, V1_SewingOutputActivity_v1.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        } else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Tab_Config")) {
            Intent intent = new Intent(this, V1_StyleWiseLineConfigActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Style_Wise_Tab_Config_V3")) {
            Intent intent = new Intent(this, V1_StyleWiseLineConfigActivity_v3.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Line_Wise_Sewing_Input")) {
            Intent intent = new Intent(this, V1_LineWiseSewingInputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if (listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Line_Wise_Sewing_Output")) {
            Intent intent = new Intent(this, V1_LineWiseSewingOutputActivity.class);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Schedule_Management")){
            Intent intent = new Intent(this, V1_ShipmentScheduleManagementActivity.class);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Shipment_Pending")){
            Intent intent = new Intent(this, V1_ShipmentPendingReportActivity.class);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Consolidated_Order_Summery")){
            Intent intent = new Intent(this, V1_ConsolitatedOrderSummeryctivity.class);
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Plan_vs_Booked")){
            Intent intent = new Intent(this, V1_PlanVsBookedReportActivity.class);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Production_QC_Print")){
            Intent intent = new Intent(this, FinishFabricQrCodeActivity.class);
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Plan_vs_Booked_vs_Capacity")){
            Intent intent = new Intent(this, V1_PlanVsBookedVsCapacityActivity.class);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("device_satues_v1")){
            Intent intent = new Intent(this, V1_ActiveDeviceStatusActivity.class);
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Capacity_vs_Plan")){
            Intent intent = new Intent(this, V1_CapacityVsPlanActivity.class);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Capacity_vs_Allocated")){
            Intent intent = new Intent(this, V1_CapacityVsAllocatedReportActivity.class);
            startActivity(intent);
        } else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Bundle_Tracking_Report")){
            Intent intent = new Intent(this, V1_ReportHomeActivity.class);
            intent.putExtra("status", 2);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Roll_Id_Tracking")){
            Intent intent = new Intent(this, V1_ReportHomeActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Electronic_Approval_V1")){
            Intent intent = new Intent(this, V1_ElectronicApprovalActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Compacting")){
            Intent intent = new Intent(this, CompactingActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Bundle_Wise_Compacting")){
            Intent intent = new Intent(this, CompactingRollWiseActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Stentering")){
            Intent intent = new Intent(this, StenteringActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Bundle_Wise_Stentering")){
            Intent intent = new Intent(this, StenteringRollWiseActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Slitting_Squeezing")){
            Intent intent = new Intent(this, SlittingSqueezingActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Bundle_Wise_Slitting_Sqeezing")){
            Intent intent = new Intent(this, SlittingSqeezingRollWiseActivity.class);
            intent.putExtra("status", 1);
            intent.putExtra("userId", userID);
            intent.putExtra("url", urladdress);
            startActivity(intent);
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Roll_Receive")){
            processList.clear();
            processList.add(new Process(R.drawable.grey_roll_receive, "Grey Roll Receive", "Receive",
                    new Process.DataParam("grey_roll", "receive")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Roll_Issue")){
            processList.clear();
            processList.add(new Process(R.drawable.grey_roll_issue, "Grey Roll Issue", "Issue",
                    new Process.DataParam("grey_roll", "issue")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }
//        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Slitting_Squeezing")){
//            processList.clear();
//            processList.add(new Process(R.drawable.grey_roll_issue, "Slitting Squeezing", "code",
//                    new Process.DataParam("result", "slitting")));
//            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
//        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Stentering")){
//            processList.clear();
//            processList.add(new Process(R.drawable.grey_roll_issue, "Stentering", "code",
//                    new Process.DataParam("result", "stentering")));
//            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
//        }
//        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Compacting")){
//            processList.clear();
//            processList.add(new Process(R.drawable.grey_roll_issue, "Compacting", "code",
//                    new Process.DataParam("result", "compacting")));
//            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
//        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Cutting_QC")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Cutting Qc", "Cutting Qc",
                    new Process.DataParam("cutting_qc", "input")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Print_Issue")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Print Issue","Print Issue",
                    new Process.DataParam("print", "issue")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Print_Receive")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Print Receive","Print Receive",
                    new Process.DataParam("print", "receive")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Embroidery_Issue")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Embroidery Issue","Embroidery Issue",
                    new Process.DataParam("embroidery", "issue")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Embroidery_Receive")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Embroidery Receive","Embroidery Receive",
                    new Process.DataParam("embroidery", "receive")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Special_Work_Issue")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Special Work Issue","Special Work Issue",
                    new Process.DataParam("special_work", "issue")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Special_Work_Receive")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Special Work Receive","Special Work Receive",
                    new Process.DataParam("special_work", "receive")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Sewing_Input_v2")){
            processList.clear();
            processList.add(new Process(R.drawable.sewing, "Sewing Input", "Sewing",
                    new Process.DataParam("sewing", "input")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Sewing_Output_v2")){
            processList.clear();
            processList.add(new Process(R.drawable.sewing, "Sewing Output", "Sewing",
                    new Process.DataParam("sewing", "output")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Production_QC")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Finish Fabric Production QC", "Fabric",
                    new Process.DataParam("finish", "fabric")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Production_Result_Entry")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Finish Fabric Production Result Entry", "result",
                    new Process.DataParam("result", "fabric")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Roll_Issue_Barcode_Scan")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Finish Fabric Roll Issue Barcode Scan ", "result",
                    new Process.DataParam("issue", "fabric")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Roll_Issue")){
            processList.clear();
            processList.add(new Process(R.drawable.grey_roll_issue, "Grey Roll Issue", "Issue",
                    new Process.DataParam("grey_roll", "issue")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Roll_Receive_By_Store")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Finish Fabric Roll Receive by Store", "result",
                    new Process.DataParam("store", "fabric")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_QC_Entry_V2")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Knitting QC Result Entry", "result",
                    new Process.DataParam("store", "knitting")));
            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
        }
//        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Finish_Fabric_Production_QC_Print")){
//            processList.clear();
//            processList.add(new Process(R.drawable.process, "Finish Fabric Production QC Print", "code",
//                    new Process.DataParam("print", "fabric")));
//            startActivity( ScannerActivity.getStartIntent(getApplicationContext(), processList.get(0)));
//        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Dyeing_Production")){
            processList.clear();
            processList.add(new Process(R.drawable.process, "Dyeing Production", "code",
                    new Process.DataParam("code", "fabric")));
            startActivity( new Intent(getApplicationContext(), DyeingProductionActivity.class));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("BI_1")){
            Intent intent = new Intent(getApplicationContext(), V1_BI_01_Activity.class);
            intent.putExtra("html", "<iframe title=\"Report Section\" width=\"1280\" height=\"720\" src=\"https://app.powerbi.com/view?r=eyJrIjoiZTNhZDJlYWQtMzI5Mi00OTkwLWIyZDctZThmZWRkZDA2MGQ1IiwidCI6Ijg0OTJiOWYwLTMyZGYtNDAxZC05OGU2LWI5YTc3Zjk2NmI5YiIsImMiOjEwfQ%3D%3D\" frameborder=\"0\" allowFullScreen=\"true\"></iframe>");
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("BI_2")){
            Intent intent = new Intent(getApplicationContext(), V1_BI_01_Activity.class);
            intent.putExtra("html", "<iframe title=\"Report Section\" width=\"1280\" height=\"720\" src=\"https://app.powerbi.com/view?r=eyJrIjoiZmVkMzg1ZjktYmMyYy00MmY0LTg0YzQtNzk0ODI2N2I4MWY0IiwidCI6Ijg0OTJiOWYwLTMyZGYtNDAxZC05OGU2LWI5YTc3Zjk2NmI5YiIsImMiOjEwfQ%3D%3D\" frameborder=\"0\" allowFullScreen=\"true\"></iframe>");
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("meeting_archive_v1")){
            Intent intent = new Intent(getApplicationContext(), V1_MeetingArchiveActivity.class);
            startActivity(intent);
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_Transfer_Out")){
            startActivity(new Intent(getApplicationContext(), V1_TransferoutActivity.class));
        } else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_Transfer_OUT_PDA")){
            startActivity(new Intent(getApplicationContext(), V1_TransferoutForPDADeviceActivity.class));
        }
        else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_Transfer_IN")){
            startActivity(new Intent(getApplicationContext(), V1_TransferInActivity.class));
        }else if(listDataChildLink.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Grey_Fabric_Transfer_IN_PDA")){
            startActivity(new Intent(getApplicationContext(), V1_TransferInForPDADeviceActivity.class));
        }
        else {
            Toast.makeText(this, "User not permitted", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void piechart() {
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, BLUE).setLabel(""));
        pieData.add(new SliceValue(25, GRAY).setLabel(""));
        pieData.add(new SliceValue(10, RED).setLabel(""));
        pieData.add(new SliceValue(30, YELLOW).setLabel(""));
        pieData.add(new SliceValue(15, BLACK).setLabel(""));
        pieData.add(new SliceValue(15, GREEN).setLabel(""));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Logic Software Ltd.").setCenterText1FontSize(20).setCenterText1Color(parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.v1_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.notify);

        View actionView = menuItem.getActionView();
        _textNotificationItemCount = (TextView) actionView.findViewById(R.id.notify_badge);

        setupBadge();
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }

    private void setupBadge() {
        if (_textNotificationItemCount != null) {
            if (totalBadge == 0) {
                if (_textNotificationItemCount.getVisibility() != View.GONE) {
                    _textNotificationItemCount.setVisibility(View.GONE);
                }
            } else {
                _textNotificationItemCount.setText(String.valueOf(totalBadge));
                if (_textNotificationItemCount.getVisibility() != View.VISIBLE) {
                    _textNotificationItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                logoutAlert();
                break;
            case R.id.refresh:
                Intent intent = V1_MenuActivity.getStartIntent(V1_MenuActivity.this);
                startActivity(intent);
                break;
            case R.id.notify:
                Intent intent_notify = new Intent(this, V1_ElectronicApprovalActivity.class);
                startActivity(intent_notify);
                break;
            case R.id.settings:
                Intent playQuiz1 = new Intent(this, V1_SettingsActivity.class);
                startActivity(playQuiz1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutAlert() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", (dialog, id) -> logoutApiCall());
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.show();
    }
    private void logoutApiCall() {
        RequestBody _user_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(userID));
        RequestBody _device_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(macAddress));

        progressBar.setVisibility(View.VISIBLE);
        apiInterface.postLogoutCall(_user_id, _device_id).enqueue(new Callback<V1_ApprovalResponseModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalResponseModel> call, Response<V1_ApprovalResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    if(response.code() == 200){
                        session.setLogin(false);
                        dbAdapter.deleteUsers();
                        Intent intent = V1_LoginActivity.getStartIntent(V1_MenuActivity.this);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    session.setLogin(false);
                    dbAdapter.deleteUsers();
                    Intent intent = V1_LoginActivity.getStartIntent(V1_MenuActivity.this);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalResponseModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private static String getMacAddr() {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_MenuActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        Intent intent = V1_ConfigActivity.getStartIntent(V1_MenuActivity.this);
                        startActivity(intent);
                    }else {
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBar.setVisibility(View.GONE);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("custom-event");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }
}