package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.SewingRequest;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.DatePickerFragment;
import com.logicsoftbd.lsl.ui.process.greyroll.dialog.HourDialog;
import com.logicsoftbd.lsl.ui.process.quantityactivity.SewingIOQuantityActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.DateUtils;
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SewingInputActivity extends BaseActivity implements ReceiveMvpView, SewingAdapter.Callback, DatePickerDialog.OnDateSetListener  {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    public static final String EXTRA_PROCESS_ID = "extra_bundle_process_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private  static final int PICK_QUANTITY_REQUEST = 2;

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    SewingAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, Process model, SewingResponse process) {
        Intent intent = new Intent(context, SewingInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context,Process model, SewingResponse process, boolean isFirst) {
        process.setFirst(isFirst);
        Intent intent = new Intent(context, SewingInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.spinner_floor)
    Spinner mSpinnerFloor;
    @BindView(R.id.spinner_line)
    Spinner mSpinnerLine;
    @BindView(R.id.tvHour)
    TextView mtvHour;
    @BindView(R.id.lay_hour)
    LinearLayout mLayHour;


   /* @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_view_1)
    TextView mTextView1;

    @BindView(R.id.text_view_2)
    TextView mTextView2;

   *//* @BindView(R.id.text_view_3)
    TextView mTextView3;*//*

    @BindView(R.id.spinner_issue_purpose)
    Spinner mSpinnerPurpose;*/

    private List<FloorResponse.Challan.MasterPart> mFloorList;
    private List<LineResponse.Challan.MasterPart> mLineList;

    private SewingRequest sewingRequest;

    private List<SewingResponse.Result.MasterPart> productBarcodes = new ArrayList<>();

    private ArrayList<String> mHourList;
    private SewingResponse mBarcodeResponse;
    private Process mProcess;

    private Date mSelectedDate;

    private HourDialog.OnTimeListener mTimeListener;
    private String mTime, userId;

    Calendar rightNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewing);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
    }

    @Override
    protected void setUp() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = _preferences.getString("login_userid", "");
        mBarcodeResponse = (SewingResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        if(mBarcodeResponse.getData().getMasterPart().getProductionType() == 4) {
            mToolbar.setTitle(R.string.sewing_input);
        }
        else {
            mToolbar.setTitle(R.string.sewing_output);
        }
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData().getMasterPart());
           // mTextView1.setText(mBarcodeResponse.getData().getMasterPart().getKnittingCompany());
          //  mTextView2.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           // mTextView3.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
            mPresenter.getFloorResponse(mBarcodeResponse.getData().getMasterPart().getLocationId(), 5);
        }
       // setHourSpinner();
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mTimeListener  = new HourDialog.OnTimeListener() {
            @Override
            public void onTimeSubmit(String hour, String minute) {
                mtvHour.setText(hour+":"+minute);
            }
        };

        if(mProcess.getDataParam().getTypeParam().equalsIgnoreCase("input")) {
            mLayHour.setVisibility(View.GONE);
        }

        if(mProcess.getDataParam().getTypeParam().equalsIgnoreCase("output")) {
            rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int minute = rightNow.get(Calendar.MINUTE);

            mtvHour.setText(hour+":"+minute);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedDate = DateUtils.getToday();
        tvDate.setText(DateUtils.formatDate(mSelectedDate));
        mAdapter.addItems(productBarcodes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_save) {
//            if(isValidForm()) {
//                extractFormData();
//                // pass the data onto the presenter
//                mPresenter.onSewingIoSave(sewingRequest);
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

    private boolean isSaving = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (!isSaving && isValidForm()) {
                isSaving = true;
                extractFormData();
                mPresenter.onSewingIoSave(sewingRequest);
                isSaving = false; // Reset this in a callback for asynchronous operations
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidForm() {
        if(productBarcodes == null || productBarcodes.size() == 0) {
            showAlertDialog("Please add barcode.");
            return false;
        }

       if(mSpinnerFloor.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a Floor.");
            return false;
        }
       if(mSpinnerLine.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a Line.");
            return false;
        }


        return true;
    }

    private void extractFormData() {
        SewingRequest.Result result = new SewingRequest.Result();
        SewingRequest.MasterPart masterPart = new SewingRequest.MasterPart();
        masterPart.setCompanyId(productBarcodes.get(0).getCompanyId());
        masterPart.setServingCompanyId(productBarcodes.get(0).getServingCompany());
        masterPart.setCutNo(productBarcodes.get(0).getcUTNO());
        masterPart.setProductionType(productBarcodes.get(0).getProductionType());
        masterPart.setLocationId(productBarcodes.get(0).getLocationId());
        masterPart.setFloorId(mFloorList.get(mSpinnerFloor.getSelectedItemPosition()-1).getId());
        masterPart.setSewingLine(mLineList.get(mSpinnerLine.getSelectedItemPosition()-1).getId());
        masterPart.setEntryDate(tvDate.getText().toString());
        masterPart.setUserId(userId);
        if(mProcess.getDataParam().getTypeParam().equalsIgnoreCase("output")) {
            masterPart.setHour(mtvHour.getText().toString());
        }
        result.setMasterPart(new SewingRequest.MasterPart());
        result.setDetailsPart(productBarcodes);
        result.setMasterPart(masterPart);


        sewingRequest = new SewingRequest();
        sewingRequest.setData(result);
        sewingRequest.setStatus("true");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(sewingRequest);
        Log.d("TAG", "SewingRequest JSON: " + json);
      //  rollReceiveRequest = mPresenter.convertToRollIssue(mBarcodeResponse, productBarcodes);
      //  rollReceiveRequest.getData().getMasterPart().setIssuePurpose( mPurposeList.get( mSpinnerPurpose.getSelectedItemPosition()-1).getId());
    }

    @OnClick(R.id.fab)
    void onFabClick () {
        startActivityForResult(ScannerActivity.getStartIntent(this, mProcess, true), PICK_BARCODE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                SewingResponse mBarcodeResponse = (SewingResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (!hasItem(mBarcodeResponse.getData().getMasterPart())) {
                    productBarcodes.add(mBarcodeResponse.getData().getMasterPart());
                } else {
                    showMessage("Item already on the list");
                }
            }
        } else if (requestCode == PICK_QUANTITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                SewingResponse mBarcodeResponse = (SewingResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (hasItem(mBarcodeResponse.getData().getMasterPart())) {
                    productBarcodes.set(hasItemPos, mBarcodeResponse.getData().getMasterPart());
                }
            }
        }
    }

    @Override
    public void onRepoEmptyViewRetryClick() {

    }

    @Override
    public void onItemDelete(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            productBarcodes.remove(position);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        mAdapter.addItems(productBarcodes);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to Remove this Barcode?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

  /*  @Override
    public void onSuccess() {
        showMessage("Item Saved Successfully");
        finish();
    }*/

    private boolean hasItem(SewingResponse.Result.MasterPart productBarcode) {
        int pos  = 0;
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (SewingResponse.Result.MasterPart barcode: productBarcodes) {
                if(barcode.getBarcodeNo().endsWith(productBarcode.getBarcodeNo())) {
                    hasItemPos = pos;
                    return true;
                }
                pos++;
            }
        }

        return false;
    }

    private int hasItemPos = 0;

    @Override
    public void onSuccess(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {
                finish();
            }
        }, msg);

    }

    @Override
    public void onFailed(String msg) {

    }


    @Override
    public void onPurposeListResponse(PurposeResponse purposeResponse) {


        //ViewUtils.prepareSpinner(this, mSpinnerPurpose, storeNames );
    }

    @OnClick(R.id.tvHour)
    public void onHourClick() {
        HourDialog.newInstance(this, mTimeListener).show(getSupportFragmentManager());
    }

    @Override
    public void onFloorListResponse(FloorResponse purposeResponse) {
        mFloorList = purposeResponse.getData().getMasterPart();
        String mSelectedItem = "";
        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mFloorList != null && mFloorList.size() > 0) {
            for(FloorResponse.Challan.MasterPart masterPart : mFloorList) {
                storeNames.add(masterPart.getFloorName());
                if(masterPart.getId() == mBarcodeResponse.getData().getMasterPart().getFloorId()) {
                    mSelectedItem = masterPart.getFloorName();
                }
            }
        }

        if(mSelectedItem != "") {
            ViewUtils.prepareSpinner(this, mSpinnerFloor, storeNames, mSelectedItem);
            //ViewUtils.prepareSpinner(this, mSpinnerLine, storeNames, mBarcodeResponse.getData().getMasterPart().getsEWINGLINE() );
           // mSpinnerFloor.setEnabled(false);
            if(mProcess.getDataParam().getTypeParam().equalsIgnoreCase("output")) {
                mSpinnerFloor.setEnabled(false);
            }

            if(mBarcodeResponse.getData().getMasterPart().getsEWINGLINE() > 0 ) {
                mPresenter.getLineResponse(mBarcodeResponse.getData().getMasterPart().companyId, mBarcodeResponse.getData().getMasterPart().getLocationId(), mFloorList.get(mSpinnerFloor.getSelectedItemPosition()-1).getId());
            }
        } else {
            ViewUtils.prepareSpinner(this, mSpinnerFloor, storeNames);
        }

        mSpinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerLine.setAdapter(null);
                if(i > 0) {
                    mPresenter.getLineResponse(1,mBarcodeResponse.getData().getMasterPart().getLocationId(), mFloorList.get(mSpinnerFloor.getSelectedItemPosition()-1).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onMachineResponses(MachineResponses machineResponses) {

    }

    @Override
    public void onShiftResponses(ShiftResponses shiftResponses) {

    }

    @Override
    public void onLocationListResponse(FloorResponse purposeResponse) {

    }

    @Override
    public void onLineListResponse(LineResponse purposeResponse) {
        mLineList = purposeResponse.getData().getMasterPart();
        String mSelectedItem = "";
        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mLineList != null && mLineList.size() > 0) {
            for(LineResponse.Challan.MasterPart masterPart : mLineList) {
                storeNames.add(masterPart.getLineNumber());
                if(mBarcodeResponse.getData().getMasterPart().getsEWINGLINE() > 0 ) {
                    if (masterPart.getId() == mBarcodeResponse.getData().getMasterPart().getsEWINGLINE()) {
                        mSelectedItem = masterPart.getLineNumber();
                    }
                }
            }
        }

        if(mSelectedItem != "") {
             ViewUtils.prepareSpinner(this, mSpinnerLine, storeNames, mSelectedItem );
            if(mProcess.getDataParam().getTypeParam().equalsIgnoreCase("output")) {
                mSpinnerLine.setEnabled(false);
            }
        } else {
            ViewUtils.prepareSpinner(this, mSpinnerLine, storeNames );
        }
    }

    @Override
    public void onReferenceListResponse(int type, ReferenceDataResponse referenceDataResponse) {

    }

    @Override
    public void finishFabricQrCodeTwoResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void finishFabricQrCodeBatchNoResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void finishFabricQrCodeBarCodeResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void defectInch(DefectInchModel defectInchModel) {

    }

    @Override
    public void issuePurpose(IssuePurposeModel issuePurposeModel) {

    }

    @Override
    public void issueStore(IssueStoreModel issueStoreModel) {

    }

    @Override
    public void defectList(DefectListModel defectListModel) {

    }

    @Override
    public void fabricGrade(FabricGradeModel fabricGradeModel) {

    }

    @Override
    public void onLogged(String onLog) {

    }

    @Override
    public void onLocation(LocationModel locationModel) {

    }

    @Override
    public void onFabricShade(FabricShade fabricGrade) {

    }


    @Override
    public void onStoreListResponse(StoreResponse storeResponse) {

    }

    @OnClick(R.id.tvDate)
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mSelectedDate = DateUtils.createDate(year, monthOfYear, dayOfMonth);
        tvDate.setText(DateUtils.formatDate(mSelectedDate));
    }

    @Override
    public void onItemForwardClick(int position) {
        mBarcodeResponse.getData().setMasterPart(productBarcodes.get(position));
        Intent intent = SewingIOQuantityActivity.getStartIntent(this, mProcess, mBarcodeResponse, position);
        startActivityForResult(intent, PICK_QUANTITY_REQUEST);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        //setResult(Activity.RESULT_OK, SewingIOQuantityActivity.getStartIntent(this));
       // SewingDialog.newInstance(this, productBarcodes.get(position)).show(getSupportFragmentManager());
    }


}
