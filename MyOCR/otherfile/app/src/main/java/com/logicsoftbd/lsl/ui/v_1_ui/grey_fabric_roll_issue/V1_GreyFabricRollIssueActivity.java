package com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_issue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_Issue_Purpose.V1_GreyRollIssuePurposeModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssueItemModel;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.GrayProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_GreyFabricRollIssueActivity extends AppCompatActivity implements View.OnClickListener,  V1_GreyRollIssueRecyclerViewAdapter.OnMoreHeadListener, V1_GreyRollIssueRecyclerViewAdapter.OnRemoveHeadListener{
    private static final String TAG = "V1_Grey_Fabric_Roll_Iss";
    private ProgressDialog _pdialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private ProgressBar _progressBar;
    private TextView _barcodeScanTV, _totalWeightTV, _totalRollTV;
    private Button _barcodeScanBT, _submitBT, _saveBT, _refreshBT, _issueDateBT;
    private ImageButton  _barcodeRefreshBT;
    private Spinner _companySpinner, _dyeingSourceSpinner, _dyeingCompanySpinner, _purposeSpinner;
    private RecyclerView _greyRollIssueRecyclerView;
    private V1_GreyRollIssueRecyclerViewAdapter greyRollIssueRecyclerViewAdapter;
    private ArrayList<V1_GreyRollIssueItemModel> greyRollIssueItemModels = new ArrayList<>();
    private ArrayList<V1_GreyRollIssueItemModel> dataList = new ArrayList<>();
    private String base_url = "", grey_roll_barcodeScan, currentDate, user_id, grey_barcode_scan, company, knittingCompany, knittingSource;
    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> sourceListName = new ArrayList<>();
    public final ArrayList<Integer> sourceListId = new ArrayList<>();
    public ArrayList<String> _dyingCompanyListName = new ArrayList<>();
    public ArrayList<Integer> _dyingCompanyListId = new ArrayList<>();
    public ArrayList<String> dyingCompanyListName = new ArrayList<>();
    public ArrayList<Integer> dyingCompanyListId = new ArrayList<>();
    public final ArrayList<String> purposeListName = new ArrayList<>();
    public final ArrayList<Integer> purposeListId = new ArrayList<>();
    private String[] companyNameArray, dyingCompanyArray, _dyingCompanyArray, sourceArray, purposeArray;
    private int scan_op = 0, company_id = 0, sourch_id = 0, _company_id = 0, knittingCompany_id = 0, knittingSource_id = 0, purpose_id = 0, sourceSelectionPosition = 0, dyeingCompanySelectionPosition = 0;

    private GrayProductionViewModel grayProductionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grey_fabric_roll_issue);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        grayProductionViewModel = new ViewModelProvider(this).get(GrayProductionViewModel.class);

        init_ui();
        initRecyclerView();
        getDefaultData();
        sendRequestForIssuePurpose();
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        grey_roll_barcodeScan = intent.getStringExtra("grey_roll_barcodeScan");
        grey_barcode_scan = intent.getStringExtra("barcode_scan");
        dataList = (ArrayList<V1_GreyRollIssueItemModel>) intent.getSerializableExtra("grey_roll_data");


        Log.d(TAG, "getDefaultData: "+_company_id +" "+knittingCompany_id+" "+knittingSource_id);

        _barcodeScanTV.setText(grey_barcode_scan);

        if(dataList != null){
            greyRollIssueItemModels = dataList;
            initRecyclerView();
            calculateTotalRollWeight();
        }

        if(grey_roll_barcodeScan != null && !grey_roll_barcodeScan.equals("")){
            _barcodeScanTV.setText(grey_roll_barcodeScan);
            requestForBarcode(grey_roll_barcodeScan);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void requestForBarcode(String barcodeScan) {
        progressBarState();
        grayProductionViewModel.getGrayRollIssuesResponse(barcodeScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                boolean s = false;
                if(apiResponse.getResultset().getStatus().equals("Failed")){
                    _barcodeScanTV.setText("");
                    if(dataList.size() > 0){
                        setPreviousListData();
                        sendRequestToServer();
                    }
                    DialogHelper.showWarningDialog(this, "Message", apiResponse.getResultset().getMsg());
                }else {
                    _barcodeScanTV.setText("");
                    if(dataList.size() > 0){
                        setPreviousListData();
                    }else{
                        _company_id = Integer.parseInt(apiResponse.getResultset().getMasterPart().getCompanyId());
                        knittingCompany = apiResponse.getResultset().getMasterPart().getKnittingCompany();
                        knittingCompany_id = Integer.parseInt(apiResponse.getResultset().getMasterPart().getKnittingCompanyId());
                        knittingSource = apiResponse.getResultset().getMasterPart().getKnittingSource();
                        knittingSource_id = Integer.parseInt(apiResponse.getResultset().getMasterPart().getKnittingSourceId());
                    }

                    if (greyRollIssueItemModels.size() > 0) {
                        String validationNewBarcode = apiResponse.getResultset().getDtlsPart().getBookingWithoutOrder() + "***" + apiResponse.getResultset().getDtlsPart().getPoBreakdownId() + "***" + apiResponse.getResultset().getDtlsPart().getIsSales();
                        String validationPrevBarcode = greyRollIssueItemModels.get(0).getBookingWithoutOrder() + "***" + greyRollIssueItemModels.get(0).getPoBreakDownId() + "***" + greyRollIssueItemModels.get(0).getIsSales();
                        if (!validationPrevBarcode.equals(validationNewBarcode)) {
                            s = true;
                            showAlertMessage("Sorry! Job mixed not allowed.", 0, 0);
                        }
                        if (!knittingCompany.equals(greyRollIssueItemModels.get(0).getKnittingCompany())) {
                            s = true;
                            showAlertMessage("Sorry! Job mixed not allowed.", 0, 0);
                        }
                        for (int j = 0; j < greyRollIssueItemModels.size(); j++) {
                            if (greyRollIssueItemModels.get(j).getBarcodeNo().equals(grey_roll_barcodeScan)) {
                                showAlertMessage("This barcode is already scanned.", 0, 0);
                                s = true;
                                break;
                            }
                        }
                    }
                    if (!s) {
                        V1_GreyRollIssueItemModel greyRollIssueItemModel = new V1_GreyRollIssueItemModel();
                        greyRollIssueItemModel.setKnittingCompanyName(apiResponse.getResultset().getMasterPart().getKnittingCompany());
                        greyRollIssueItemModel.setCompanyId(String.valueOf(apiResponse.getResultset().getMasterPart().getCompanyId()));
                        greyRollIssueItemModel.setKnittingCompanyId(String.valueOf(apiResponse.getResultset().getMasterPart().getKnittingCompanyId()));
                        greyRollIssueItemModel.setKnittingSourceId(String.valueOf(apiResponse.getResultset().getMasterPart().getKnittingSourceId()));
                        greyRollIssueItemModel.setKnittingSourceName(apiResponse.getResultset().getMasterPart().getKnittingSource());
                        greyRollIssueItemModel.setBarcodeNo(apiResponse.getResultset().getDtlsPart().getBarcodeNo());
                        greyRollIssueItemModel.setBinBoxId(apiResponse.getResultset().getDtlsPart().getBinBoxId());
                        greyRollIssueItemModel.setBodyPartId(apiResponse.getResultset().getDtlsPart().getBodyPartId());
                        greyRollIssueItemModel.setBodyPartName(apiResponse.getResultset().getDtlsPart().getBodyPartName());
                        greyRollIssueItemModel.setBookingId(apiResponse.getResultset().getDtlsPart().getBookingId());
                        greyRollIssueItemModel.setBookingNo(apiResponse.getResultset().getDtlsPart().getBookingId());
                        greyRollIssueItemModel.setBookingWithoutOrder(apiResponse.getResultset().getDtlsPart().getBookingWithoutOrder());
                        greyRollIssueItemModel.setBrandId(apiResponse.getResultset().getDtlsPart().getBrandId());
                        greyRollIssueItemModel.setBrandName(apiResponse.getResultset().getDtlsPart().getBrandName());
                        greyRollIssueItemModel.setBuyerId(apiResponse.getResultset().getDtlsPart().getBuyerId());
                        greyRollIssueItemModel.setBuyerName(apiResponse.getResultset().getDtlsPart().getBuyerName());
                        greyRollIssueItemModel.setJobNo(apiResponse.getResultset().getDtlsPart().getJobNo());
                        greyRollIssueItemModel.setColorId(apiResponse.getResultset().getDtlsPart().getColorId());
                        greyRollIssueItemModel.setFso_booking(apiResponse.getResultset().getDtlsPart().getFso_booking());
                        greyRollIssueItemModel.setColorRangeId(apiResponse.getResultset().getDtlsPart().getColorRangeId());
                        greyRollIssueItemModel.setColorRangeName(apiResponse.getResultset().getDtlsPart().getColorRangeName());
                        greyRollIssueItemModel.setComposition(apiResponse.getResultset().getDtlsPart().getComposition());
                        greyRollIssueItemModel.setConstruction(apiResponse.getResultset().getDtlsPart().getConstruction());
                        greyRollIssueItemModel.setDeterId(apiResponse.getResultset().getDtlsPart().getDeterId());
                        greyRollIssueItemModel.setDtlsId(apiResponse.getResultset().getDtlsPart().getDtlsId());
                        greyRollIssueItemModel.setFloorId(apiResponse.getResultset().getDtlsPart().getFloorId());
                        greyRollIssueItemModel.setGsm(apiResponse.getResultset().getDtlsPart().getGsm());
                        greyRollIssueItemModel.setKnittingCompany(apiResponse.getResultset().getDtlsPart().getKnittingCompany());
                        greyRollIssueItemModel.setMachineNoId(apiResponse.getResultset().getDtlsPart().getMachineNoId());
                        greyRollIssueItemModel.setMachineName(apiResponse.getResultset().getDtlsPart().getMachineName());
                        greyRollIssueItemModel.setPoBreakDownId(apiResponse.getResultset().getDtlsPart().getPoBreakdownId());
                        greyRollIssueItemModel.setProdId(apiResponse.getResultset().getDtlsPart().getProdId());
                        greyRollIssueItemModel.setProductionBasis(null);
                        greyRollIssueItemModel.setQnty(apiResponse.getResultset().getDtlsPart().getQnty().trim());
                        greyRollIssueItemModel.setRackId(apiResponse.getResultset().getDtlsPart().getRackId());
                        greyRollIssueItemModel.setRejectQnty(apiResponse.getResultset().getDtlsPart().getRejectQnty());
                        greyRollIssueItemModel.setRollId(apiResponse.getResultset().getDtlsPart().getRollId());
                        greyRollIssueItemModel.setRollNo(apiResponse.getResultset().getDtlsPart().getRollNo());
                        greyRollIssueItemModel.setRoomId(apiResponse.getResultset().getDtlsPart().getRoomId());
                        greyRollIssueItemModel.setColorName(apiResponse.getResultset().getDtlsPart().getColorName());
                        greyRollIssueItemModel.setSampBooking(apiResponse.getResultset().getDtlsPart().getSampBooking());
                        greyRollIssueItemModel.setShelfId(apiResponse.getResultset().getDtlsPart().getShelfId());
                        greyRollIssueItemModel.setShiftName(apiResponse.getResultset().getDtlsPart().getShiftName());
                        greyRollIssueItemModel.setStitchLength(apiResponse.getResultset().getDtlsPart().getStitchLength());
                        greyRollIssueItemModel.setStoreId(apiResponse.getResultset().getDtlsPart().getStoreId());
                        greyRollIssueItemModel.setUom(apiResponse.getResultset().getDtlsPart().getUom());
                        greyRollIssueItemModel.setWidth(apiResponse.getResultset().getDtlsPart().getWidth());
                        greyRollIssueItemModel.setYarnCount(apiResponse.getResultset().getDtlsPart().getYarnCount());
                        greyRollIssueItemModel.setYarnLot(apiResponse.getResultset().getDtlsPart().getYarnLot());
                        greyRollIssueItemModel.setYarnRate(apiResponse.getResultset().getDtlsPart().getYarnRate());
                        greyRollIssueItemModel.setKnitingCharge(apiResponse.getResultset().getDtlsPart().getKnitingCharge());
                        greyRollIssueItemModel.setRollRate(apiResponse.getResultset().getDtlsPart().getRollRate());
                        greyRollIssueItemModel.setIsSales(apiResponse.getResultset().getDtlsPart().getIsSales());
                        greyRollIssueItemModel.setJobMix(apiResponse.getResultset().getDtlsPart().getJobMixingVar());
                        greyRollIssueItemModel.setProgramNo(apiResponse.getResultset().getDtlsPart().getProgramNo());
                        greyRollIssueItemModel.setStatus(false);
                        greyRollIssueItemModels.add(greyRollIssueItemModel);
                    }
                    greyRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                    calculateTotalRollWeight();
                    sendRequestToServer();
                }
            }else{
                if(dataList.size() > 0){
                    setPreviousListData();
                    sendRequestToServer();
                }

                DialogHelper.showErrorDialog(this, "Message", "Data not found");
            }
        });
    }

    private void setPreviousListData() {
        _barcodeScanTV.setText("");
        knittingCompany = greyRollIssueItemModels.get(0).getKnittingCompany();
        _company_id = Integer.parseInt(greyRollIssueItemModels.get(0).getCompanyId());
        knittingCompany_id = Integer.parseInt(greyRollIssueItemModels.get(0).getKnittingCompanyId());
        knittingSource = greyRollIssueItemModels.get(0).getKnittingSourceName();
        knittingSource_id = Integer.parseInt(greyRollIssueItemModels.get(0).getKnittingSourceId());
    }

    private void calculateTotalRollWeight() {
        double totalWeight = 0;
        for(int i=0; i<greyRollIssueItemModels.size(); i++){
            totalWeight += Double.parseDouble(greyRollIssueItemModels.get(i).getQnty());
        }
        _totalWeightTV.setText("Total Roll Weight: "+totalWeight);
        if(greyRollIssueItemModels != null && greyRollIssueItemModels.size() > 0) {
            _totalRollTV.setText("Total No of Roll : " + greyRollIssueItemModels.size());
        }
    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject master_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            master_obj.put("COMPANY_ID", company_id);
            master_obj.put("DELIVERY_ID", 0);
            master_obj.put("ISSUE_PURPOSE", purposeListId.get(purpose_id));
            master_obj.put("KNITTING_COMPANY_ID", dyeingCompanySelectionPosition);
            master_obj.put("KNITTING_SOURCE", knittingSource);
            master_obj.put("KNITTING_SOURCE_ID", knittingSource_id);
            master_obj.put("LOCATION_ID", 0);
            master_obj.put("SYS_NUMBER_PREFIX_NUM", 0);

            data_obj.put("MasterPart",master_obj);
            for (int i = 0; i < greyRollIssueItemModels.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BARCODE_NO", greyRollIssueItemModels.get(i).getBarcodeNo());
                dtls_obj.put("BIN_BOX_ID", greyRollIssueItemModels.get(i).getBinBoxId());
                dtls_obj.put("BODY_PART_ID", greyRollIssueItemModels.get(i).getBodyPartId());
                dtls_obj.put("BODY_PART_NAME", greyRollIssueItemModels.get(i).getBodyPartName());
                dtls_obj.put("BOOKING_ID", greyRollIssueItemModels.get(i).getBookingId());
                dtls_obj.put("BOOKING_NO", greyRollIssueItemModels.get(i).getBookingNo());
                dtls_obj.put("BOOKING_WITHOUT_ORDER", greyRollIssueItemModels.get(i).getBookingWithoutOrder());
                dtls_obj.put("BRAND_ID", greyRollIssueItemModels.get(i).getBrandId());
                dtls_obj.put("BUYER_ID", greyRollIssueItemModels.get(i).getBuyerId());
                dtls_obj.put("BUYER_NAME", greyRollIssueItemModels.get(i).getBuyerName());
                dtls_obj.put("COLOR_ID", greyRollIssueItemModels.get(i).getColorId());
                dtls_obj.put("COLOR_RANGE_ID", greyRollIssueItemModels.get(i).getColorRangeId());
                dtls_obj.put("COLOR_RANGE_NAME", greyRollIssueItemModels.get(i).getColorRangeName());
                dtls_obj.put("COMPOSITION", greyRollIssueItemModels.get(i).getComposition());
                dtls_obj.put("CONSTRUCTION", greyRollIssueItemModels.get(i).getConstruction());
                dtls_obj.put("DETER_ID", greyRollIssueItemModels.get(i).getDeterId());
                dtls_obj.put("JOB_NO", greyRollIssueItemModels.get(i).getJobNo());
                dtls_obj.put("DTLS_ID", greyRollIssueItemModels.get(i).getDtlsId());
                dtls_obj.put("FLOOR_ID", greyRollIssueItemModels.get(i).getFloorId());
                dtls_obj.put("GSM", greyRollIssueItemModels.get(i).getGsm());
                dtls_obj.put("KNITTING_COMPANY", greyRollIssueItemModels.get(i).getKnittingCompany());
                dtls_obj.put("MACHINE_NO_ID", greyRollIssueItemModels.get(i).getMachineNoId());
                dtls_obj.put("PO_BREAKDOWN_ID", greyRollIssueItemModels.get(i).getPoBreakDownId());
                dtls_obj.put("PROD_ID", greyRollIssueItemModels.get(i).getProdId());
                dtls_obj.put("PRODUCTION_BASIS", greyRollIssueItemModels.get(i).getProductionBasis());
                dtls_obj.put("QNTY", greyRollIssueItemModels.get(i).getQnty());
                dtls_obj.put("RACK_ID", greyRollIssueItemModels.get(i).getRackId());
                dtls_obj.put("REJECT_QNTY", greyRollIssueItemModels.get(i).getRejectQnty());
                dtls_obj.put("ROLL_ID", greyRollIssueItemModels.get(i).getRollId());
                dtls_obj.put("ROLL_NO", greyRollIssueItemModels.get(i).getRollNo());
                dtls_obj.put("ROOM_ID", greyRollIssueItemModels.get(i).getRoomId());
                dtls_obj.put("SAMP_BOOKING", greyRollIssueItemModels.get(i).getSampBooking());
                dtls_obj.put("SHELF_ID", greyRollIssueItemModels.get(i).getShelfId());
                dtls_obj.put("SHIFT_NAME", greyRollIssueItemModels.get(i).getShiftName());
                dtls_obj.put("STITCH_LENGTH", greyRollIssueItemModels.get(i).getStitchLength());
                dtls_obj.put("STORE_ID", greyRollIssueItemModels.get(i).getStoreId());
                dtls_obj.put("UOM", greyRollIssueItemModels.get(i).getUom());
                dtls_obj.put("WIDTH", greyRollIssueItemModels.get(i).getWidth());
                dtls_obj.put("YARN_COUNT", greyRollIssueItemModels.get(i).getYarnCount());
                dtls_obj.put("YARN_LOT", greyRollIssueItemModels.get(i).getYarnLot());
                dtls_obj.put("YARN_RATE", greyRollIssueItemModels.get(i).getYarnRate());
                dtls_obj.put("KNITING_CHARGE", greyRollIssueItemModels.get(i).getKnitingCharge());
                dtls_obj.put("ROLL_RATE", greyRollIssueItemModels.get(i).getRollRate());
                dtls_obj.put("IS_SALES", greyRollIssueItemModels.get(i).getIsSales());
                dtls_obj.put("USER_ID", user_id);
                dtls_arr.put(dtls_obj);
            }

            data_obj.put("DtlsPart",dtls_arr);
            save_obj.put("resultset", data_obj);
            save_obj.put("status", true);
            Log.d(TAG, "postDataToServer: ########"+save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBarState();
        grayProductionViewModel.postGrayRollIssueResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null){
                resetButtonState();
                refreshData();
                showAlertMessage(apiResponse.getResultset().getMsg(), 3, 0);
            }else{
                DialogHelper.showErrorDialog(this, "Message", "Something went wrong!");
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _greyRollIssueRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _greyRollIssueRecyclerView.addItemDecoration(itemDecorator);
        greyRollIssueRecyclerViewAdapter = new V1_GreyRollIssueRecyclerViewAdapter(greyRollIssueItemModels, this, this, this);
        _greyRollIssueRecyclerView.setAdapter(greyRollIssueRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true); // This ensures that the last item stays at the bottom
        _greyRollIssueRecyclerView.setLayoutManager(layoutManager);
    }

    private void sendRequestToServer() {
        progressBarState();
        grayProductionViewModel.getGrayRollIssuesDefaultResponse().observe(this, apiResponse -> {
            if(apiResponse != null){
                companyNameList.add(0, "--Company--");
                companyNameId.add(0, 0);
                for(V1_BundleWiseSewingInputClass.Company item : apiResponse.getResultset().getCompany()){
                    companyNameList.add(item.getCompany());
                    companyNameId.add(item.getId());
                }
                companyNameArray = companyNameList.toArray(new String[companyNameList.size()]);

                sourceListName.add(0, "--Source--");
                sourceListId.add(0, 0);
                for(V1_BundleWiseSewingInputClass.Source item : apiResponse.getResultset().getSource()){
                    sourceListName.add(item.getName());
                    sourceListId.add(item.getId());
                }
                sourceArray = sourceListName.toArray(new String[sourceListName.size()]);

                _dyingCompanyListName.add(0, "--Dyeing Company--");
                _dyingCompanyListId.add(0, 0);
                for(V1_BundleWiseSewingInputClass.Supplier item : apiResponse.getResultset().getSupplier()){
                    _dyingCompanyListName.add(item.getName());
                    _dyingCompanyListId.add(item.getId());
                }
                dyingCompanyListName = _dyingCompanyListName;
                dyingCompanyListId = _dyingCompanyListId;

                setCompanyAdapterData();
                setSourceAdapterData();
            }else{
                DialogHelper.showErrorDialog(this, "Message", "Something went wrong!");
            }
        });
    }

    private void sendRequestForIssuePurpose() {
        progressBarState();
        grayProductionViewModel.getGrayRollIssuesPurposeResponse().observe(this, apiResponse -> {
            if(apiResponse!= null){
                purposeListName.add(0, "--Purpose--");
                purposeListId.add(0, 0);
                for(V1_GreyRollIssuePurposeModel.MasterPartItem item : apiResponse.getResultset().getMasterPart()){
                    purposeListName.add(item.getPURPOSE());
                    purposeListId.add(item.getID());
                }
                purposeArray = purposeListName.toArray(new String[purposeListName.size()]);
                setPurposeAdapterData();
            }else{
                DialogHelper.showErrorDialog(this, "Message", "Something went wrong!");
            }
        });
    }

    private void setPurposeAdapterData() {
        ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, purposeArray);
        _purposeSpinner.setAdapter(adapterCompany);
        try {
            _purposeSpinner.setSelection(purposeListId.indexOf(11));
        }catch (Exception e){
            Log.d(TAG, "setPurposeAdapterData: ");
        }

        _purposeSpinner.setEnabled(false);
        _purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int purposePosition = position;
                purpose_id = purposePosition;

                Log.d(TAG, "onItemSelected: "+purpose_id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCompanyAdapterData() {
        ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, companyNameArray);
        _companySpinner.setAdapter(adapterCompany);
        _companySpinner.setSelection(companyNameId.indexOf(_company_id));
        _companySpinner.setEnabled(false);
        _companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int companyPosition = position;
                company_id = companyNameId.get(companyPosition);
                _companySpinner.setSelection(companyNameId.indexOf(knittingCompany_id));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSourceAdapterData() {
        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, sourceArray);
        _dyeingSourceSpinner.setAdapter(adapterSource);
        _dyeingSourceSpinner.setSelection(sourceListId.indexOf(knittingSource_id));
        _dyeingSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                knittingSource = sourceListName.get(position);
                knittingSource_id = sourceListId.get(position);
                sourceSelectionPosition = position;
                if(sourceSelectionPosition == 1){
                    dyingCompanyListName = companyNameList;
                    dyingCompanyListId = companyNameId;
                    setDyeingCompanyAdapterData();
                }else if(sourceSelectionPosition == 2){
                    dyingCompanyListName = _dyingCompanyListName;
                    dyingCompanyListId = _dyingCompanyListId;
                    setDyeingCompanyAdapterData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDyeingCompanyAdapterData() {
        dyingCompanyArray = dyingCompanyListName.toArray(new String[dyingCompanyListName.size()]);
        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, dyingCompanyArray);
        _dyeingCompanySpinner.setAdapter(adapterSource);
        _dyeingCompanySpinner.setSelection(dyingCompanyListId.indexOf(knittingCompany_id));
        if(sourceSelectionPosition == 1){
            _dyeingCompanySpinner.setSelection(companyNameId.indexOf(knittingCompany_id));
        }
        _dyeingCompanySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sourceSelectionPosition == 1){
                    _dyeingCompanySpinner.setSelection(companyNameId.indexOf(knittingCompany_id));
                }
                dyeingCompanySelectionPosition = dyingCompanyListId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _companySpinner = findViewById(R.id.companySpinner);
        _dyeingSourceSpinner = findViewById(R.id.dyeingSourceSpinner);
        _dyeingCompanySpinner = findViewById(R.id.dyeingCompanySpinner);
        _purposeSpinner = findViewById(R.id.purposeSpinner);
        _greyRollIssueRecyclerView = findViewById(R.id.greyRollIssueRecyclerView);
        _barcodeScanTV = findViewById(R.id.barcodeScanTV);
        _totalWeightTV = findViewById(R.id.totalWeightTV);
        _totalRollTV = findViewById(R.id.totalRollTV);
        _issueDateBT = findViewById(R.id.issueDateBtn);
        _issueDateBT.setOnClickListener(this);
        _barcodeScanBT = findViewById(R.id.barcodeScanBT);
        _barcodeScanBT.setOnClickListener(this);
        _submitBT = findViewById(R.id.submitBT);
        _submitBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _barcodeRefreshBT = findViewById(R.id.barcodeRefreshBT);
        _barcodeRefreshBT.setOnClickListener(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        _issueDateBT.setText(currentDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.barcodeScanBT:
                if(_barcodeScanTV.getText().toString().isEmpty()){
                    startScanning(2, 2);
                }
                else
                    showAlertMessage("Please Refresh Barcode first.", 0, 0);
                break;
            case R.id.barcodeRefreshBT:
                _barcodeScanTV.setText("");
                break;
            case R.id.refreshBT:
                refreshData();
                break;
//            case R.id.saveBT:
//                if(sourceSelectionPosition != 0){
//                    if(sourceSelectionPosition != 2 || dyeingCompanySelectionPosition != 0){
//                        if(purpose_id != 0){
//                            if(greyRollIssueItemModels.size() > 0){
//                                postDataToServer();
//                            }
//                        }else {
//                            showAlertMessage("Please select a purpose.", 2, 0);
//                        }
//                    }else{
//                        showAlertMessage("Please select a dyeing company.", 2, 0);
//                    }
//                }else{
//                    showAlertMessage("Please select a dyeing source.", 2, 0);
//                }
//
//                break;
            case R.id.saveBT:
                _saveBT.setEnabled(false);
                _saveBT.setAlpha(0.5f);

                if (sourceSelectionPosition != 0) {
                    if (sourceSelectionPosition != 2 || dyeingCompanySelectionPosition != 0) {
                        if (purpose_id != 0) {
                            if (greyRollIssueItemModels.size() > 0) {
                                postDataToServer();
                            } else {
                                showAlertMessage("No items to process.", 2, 0);
                                resetButtonState();
                            }
                        } else {
                            showAlertMessage("Please select a purpose.", 2, 0);
                            resetButtonState();
                        }
                    } else {
                        showAlertMessage("Please select a dyeing company.", 2, 0);
                        resetButtonState();
                    }
                } else {
                    showAlertMessage("Please select a dyeing source.", 2, 0);
                    resetButtonState();
                }
                break;
            case R.id.issueDateBtn:
                datePicker();
                break;
        }
    }

    private void resetButtonState() {
        _saveBT.setEnabled(true);
        _saveBT.setAlpha(1.0f);
    }

    private void datePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    _issueDateBT.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void refreshData() {
        greyRollIssueItemModels.clear();
        _barcodeScanTV.setText("");
        greyRollIssueRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void startScanning(int op, int grey_roll_status) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "grey_roll_issue_v1");
        intent.putExtra("scan_op", op);
        intent.putExtra("grey_roll_status", grey_roll_status);
        intent.putExtra("grey_roll_data", greyRollIssueItemModels);
        intent.putExtra("barcode_scan", _barcodeScanTV.getText().toString());
        startActivity(intent);
        finish();
    }

    private void showAlertMessage(String msg, int i, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_GreyFabricRollIssueActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        greyRollIssueItemModels.remove(position);
                        greyRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                        calculateTotalRollWeight();
                        dialog.dismiss();
                    } else if(i == 3) {
                        startScanning(2, 2);
                    } else {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public void onMoreHeadClick(int position, View v) {
        if(greyRollIssueItemModels.get(position).getStatus() == true){
            greyRollIssueItemModels.get(position).setStatus(false);
        }else{
            greyRollIssueItemModels.get(position).setStatus(true);
        }
        greyRollIssueRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void progressBarState() {
        grayProductionViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRemoveHeadClick(int position, View v) {
        showAlertMessage("Are you confirm to remove this barcode?", 1, position);
    }
}