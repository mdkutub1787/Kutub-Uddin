package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.RollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.DatePickerFragment;
import com.logicsoftbd.lsl.ui.process.greyroll.dialog.HourDialog;
import com.logicsoftbd.lsl.ui.process.quantityactivity.CuttingQcDialog;
import com.logicsoftbd.lsl.ui.process.quantityactivity.CuttingQcQuantityActivity;
import com.logicsoftbd.lsl.ui.process.quantityactivity.QuantityModel;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.DateUtils;
import com.logicsoftbd.lsl.utils.Validator.Field;
import com.logicsoftbd.lsl.utils.Validator.Form;
import com.logicsoftbd.lsl.utils.Validator.validations.InRange;
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuttingQcActivity_test extends BaseActivity implements ReceiveMvpView, CuttingQcAdapter.Callback, DatePickerDialog.OnDateSetListener  {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private  static final int PICK_QUANTITY_REQUEST = 2;
    private String userId = "0";

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    CuttingQcAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, CuttingQcBarcodeResponse process) {
        Intent intent = new Intent(context, CuttingQcActivity_test.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, CuttingQcBarcodeResponse process, boolean isFirst) {
        process.setFirst(isFirst);
        Intent intent = new Intent(context, CuttingQcActivity_test.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.spinner_location)
    Spinner mSpinnerLocation;

    @BindView(R.id.spinner_floor)
    Spinner mSpinnerFloor;

    @BindView(R.id.tvHour)
    TextView mtvHour;


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
    private List<FloorResponse.Challan.MasterPart> mLocationList;
    private ArrayList<String> mHourList;

    private RollReceiveRequest rollReceiveRequest;

    private List<CuttingQcBarcodeResponse.Result.DetailsPart.BundleData> productBarcodes = new ArrayList<>();


    private CuttingQcBarcodeResponse mBarcodeResponse;

    private Date mSelectedDate;
    private  StringBuilder strBuilder;

    private int currentHour = 0;
    private HourDialog.OnTimeListener mTimeListener;

    Calendar rightNow;

    @BindView(R.id.text_view_1)
    TextView text_view_1;

    @BindView(R.id.text_view_2)
    TextView text_view_2;

    @BindView(R.id.edit_text_qc_pass)
    EditText mEditTextBundleQty;

    @BindView(R.id.edit_text_reject)
    EditText mEditTextReject;

    @BindView(R.id.edit_text_replace)
    EditText mEditTextReplace;
    @BindView(R.id.btn_defect)
    Button mButtonDefect;
    @BindView(R.id.btn_submit)
    Button mButtonSubmit;


    @BindView(R.id.text_layout_qc_pass)
    TextInputLayout mTextInputLayoutQcQty;

    @BindView(R.id.text_layout_reject)
    TextInputLayout mTextInputLayoutReject;

    @BindView(R.id.text_layout_replace)
    TextInputLayout mTextInputLayoutReplace;

    private int mRequestedPOs = 0;
    private int mRequestedBundlePos = 0;
    private Form mForm;

    private CuttingQcBarcodeResponse mItemObj;

    private CuttingQcBarcodeResponse.Result.DetailsPart.BundleData bundleData;

    public interface OnRejectListener{
        void onRejectSubmit(String result, int rejectQuantity);
    }

    private  int qcQuantity = 0;
    private int qunantity = 0;
    private  int reject = 0;
    private  int replace = 0;

    private CuttingQcQuantityActivity.OnRejectListener rejectListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_qc_test);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = _preferences.getString("login_userid", "");

        setUp();
    }

    @Override
    protected void setUp() {
        strBuilder = new StringBuilder("");
        mEditTextBundleQty.setText("");
        mEditTextReplace.setText("");
        mEditTextReject.setText("");

        mBarcodeResponse = (CuttingQcBarcodeResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setTitle("Cutting Qc");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(mBarcodeResponse.isFirst()) {
            mPresenter.getLocation(mBarcodeResponse.getData().getMasterPart().getSERVING_COMPANY());
        }
        setHourSpinner();
        int detailsPos = 0;
        int bundlePos = 0;
        if(mBarcodeResponse.getData().getDetailsPart() != null && mBarcodeResponse.getData().getDetailsPart().size() > 0) {
            for(CuttingQcBarcodeResponse.Result.DetailsPart detailsPart : mBarcodeResponse.getData().getDetailsPart()) {
                for(CuttingQcBarcodeResponse.Result.DetailsPart.BundleData bundleData: detailsPart.getBundleDataList()) {
                    bundleData.setDetailsPos(detailsPos);
                    bundleData.setBundlePos(bundlePos);
                    strBuilder.append(bundleData.getBundleNo());
                   if(bundlePos != detailsPart.getBundleDataList().size()-1) {
                       strBuilder.append(",");
                   }
                    productBarcodes.add(bundleData);
                    bundlePos++;
                }
                detailsPos++;
            }
        }
        CuttingQcBarcodeResponse.Result.BundleNos bundleNos = new CuttingQcBarcodeResponse.Result.BundleNos();
        bundleNos.setBundleString(strBuilder.toString());
        mBarcodeResponse.getData().setBundleNos(bundleNos);
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

        rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);

        mtvHour.setText(hour+":"+minute);

        mForm = new Form(this);

        if(mBarcodeResponse != null) {
            mItemObj = mBarcodeResponse;
            // initValidationForm();
            bundleData = mItemObj.getData().getDetailsPart().get(mRequestedPOs).getBundleDataList().get(mRequestedBundlePos);
            bundleData.setQuantity(productBarcodes.get(mRequestedPOs).getQcPassQty());
//        qcQuantity =bundleData.getQcPassQty();
            qcQuantity =bundleData.getQuantity();
            text_view_1.setText( bundleData.getBundleNo());
//        text_view_2.setText(bundleData.getQcPassQty()+"");
            text_view_2.setText(bundleData.getQuantity()+"");

            if(bundleData.getRejectQty() != null ) {
                reject = bundleData.getRejectQty();
                mEditTextReject.setText(reject+"");
            }


            if(bundleData.getReplaceQty() != null ) {
                replace = bundleData.getReplaceQty();
                mEditTextReplace.setText(replace+"");
            }
        }


        rejectListener = new CuttingQcQuantityActivity.OnRejectListener() {
            @Override
            public void onRejectSubmit(String result, int rejectQuantity) {
                bundleData.setDefectStr(result);

            }
        };

        setQcQuantity();
        changeReject();
        changeReplace();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.findItem(R.id.action_save);
        if (saveItem != null) {
            try {
                saveItem.setTitle(mBarcodeResponse.getData().getMasterPart().getUpdateId() != 0 ? "Update" : "Save");
            }catch (Exception e){
                Log.d("TAG", "onPrepareOptionsMenu: "+e.getMessage()+e);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isValidForm()) {
                extractFormData();
                // pass the data onto the presenter
                Log.d("TAG", "onOptionsItemSelected: "+new Gson().toJson(mBarcodeResponse));
                mPresenter.onQcSave(mBarcodeResponse);
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

        if(mSpinnerLocation.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a location.");
            return false;
        }

        if(mSpinnerFloor.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a floor.");
            return false;
        }
       /* if(mSpinnerHour.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select hour.");
            return false;
        }
*/

        return true;
    }

    private void extractFormData() {
        try {
            mBarcodeResponse.getData().getMasterPart().setUserId(Integer.parseInt(userId));
            mBarcodeResponse.getData().getMasterPart().setFloorId(mFloorList.get(mSpinnerFloor.getSelectedItemPosition()-1).getId());
            mBarcodeResponse.getData().getMasterPart().setLocationId(mLocationList.get(mSpinnerLocation.getSelectedItemPosition()-1).getId());
            mBarcodeResponse.getData().getMasterPart().setEntryDate(DateUtils.formatDate(new Date()));
            mBarcodeResponse.getData().getMasterPart().setQcDate(tvDate.getText().toString());
            mBarcodeResponse.getData().getMasterPart().setQcHour(mtvHour.getText().toString());
            initValidationForm();
            if(mForm.isValid()) {
                extractFormData1();
            }
        } catch (Exception e) {
            Log.d("TAG", "extractFormData: "+e.getMessage()+e);
            Toast.makeText(this, "Location & Floor not found.", Toast.LENGTH_SHORT).show();
        }
        
      //  rollReceiveRequest = mPresenter.convertToRollIssue(mBarcodeResponse, productBarcodes);
      //  rollReceiveRequest.getData().getMasterPart().setIssuePurpose( mPurposeList.get( mSpinnerPurpose.getSelectedItemPosition()-1).getId());
    }

   /* @OnClick(R.id.fab)
    void onFabClick () {
        Process process = new Process(R.drawable.sewing, "Sewing Input", "Input",
                new Process.DataParam("sewing", "input"));
        startActivityForResult(ScannerActivity.getStartIntent(this, process, true), PICK_BARCODE_REQUEST);
    }
*/


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



    @Override
    public void onSuccess(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {
                startActivity( ScannerActivity.getStartIntent(CuttingQcActivity_test.this, new Process(R.drawable.process, "Cutting Qc", "Cutting Qc",
                        new Process.DataParam("cutting_qc", "input"))));

                finish();
            }
        }, msg);

    }

    @Override
    public void onFailed(String msg) {

    }


    @Override
    public void onPurposeListResponse(PurposeResponse purposeResponse) {
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
            //mSpinnerFloor.setEnabled(false);
        } else {
            ViewUtils.prepareSpinner(this, mSpinnerFloor, storeNames);
        }


    }

    @Override
    public void onMachineResponses(MachineResponses machineResponses) {

    }

    @Override
    public void onShiftResponses(ShiftResponses shiftResponses) {

    }

    private void setHourSpinner(){

    }

    @OnClick(R.id.tvHour)
    public void onHourClick() {
        HourDialog.newInstance(this, mTimeListener).show(getSupportFragmentManager());
    }


    @Override
    public void onLocationListResponse(FloorResponse purposeResponse) {
        mLocationList = purposeResponse.getData().getMasterPart();
        String mSelectedItem = "";
        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mLocationList != null && mLocationList.size() > 0) {
            for(FloorResponse.Challan.MasterPart masterPart : mLocationList) {
                storeNames.add(masterPart.getLocationName());
                if(masterPart.getId() == mBarcodeResponse.getData().getMasterPart().getLocationId()) {
                    mSelectedItem = masterPart.getLocationName();
                }
            }
        }
        if(mSelectedItem != "") {
            ViewUtils.prepareSpinner(this, mSpinnerLocation, storeNames, mSelectedItem);
            mPresenter.getFloorResponse(mLocationList.get(mSpinnerLocation.getSelectedItemPosition()-1).getId(), 1);
          //  mSpinnerLocation.setEnabled(false);
        } else {
            ViewUtils.prepareSpinner(this, mSpinnerLocation, storeNames);
        }
        mSpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerFloor.setAdapter(null);
                if(i > 0) {
                    mPresenter.getFloorResponse(mLocationList.get(mSpinnerLocation.getSelectedItemPosition()-1).getId(), 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onLineListResponse(LineResponse purposeResponse) {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_QUANTITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                mBarcodeResponse = (CuttingQcBarcodeResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);

            }
        }
    }
    @Override
    public void onItemForwardClick(int position) {
//        CuttingQcBarcodeResponse.Result.DetailsPart.BundleData bundleData = productBarcodes.get(position);
//        bundleData.setQuantity(productBarcodes.get(position).getQcPassQty());
//        Intent intent = CuttingQcQuantityActivity.getStartIntent(this, mBarcodeResponse, bundleData.getDetailsPos(), bundleData.getBundlePos());
//        startActivityForResult(intent, PICK_QUANTITY_REQUEST);
//        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

      //  CuttingQcDialog.newInstance(this, productBarcodes.get(position)).show(getSupportFragmentManager());
    }

    private void changeReject() {
        mEditTextReject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    reject = Integer.parseInt(charSequence.toString());
                } else {
                    reject  = 0;
                }
                mTextInputLayoutReject.setError(null);
                if( reject > bundleData.getQcPassQty()) {

                    reject  = 0;
                    mTextInputLayoutReject.setError("Reject quantity can not be greater than QC quantity");
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeReplace() {
        mEditTextReplace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    replace = Integer.parseInt(charSequence.toString());
                } else {
                    replace  = 0;
                }
                mTextInputLayoutReplace.setError(null);
                if( replace > reject) {
                    replace  = 0;

                    mTextInputLayoutReplace.setError("Replace quantity can not be greater than reject quantity");
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btn_defect)
    void onClickReject() {
        CuttingQcDialog.newInstance(this, rejectListener, bundleData.getDefectStr()).show(getSupportFragmentManager());;
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(){
        initValidationForm();
        if(mForm.isValid()) {
            extractFormData1();
        }
    }
    @OnClick(R.id.btn_later)
    void later() {
        finish();
    }

    protected void initValidationForm() {
        mForm.addField(Field.using(mEditTextBundleQty, mTextInputLayoutReject).
                validate(InRange.build(this, 0, bundleData.getQcPassQty())));

        mForm.addField(Field.using(mEditTextReject, mTextInputLayoutReject).
                validate(InRange.build(this, 0, bundleData.getQcPassQty())));


        mForm.addField(Field.using(mEditTextReplace, mTextInputLayoutReplace).
                validate(InRange.build(this, 0, reject)));
    }

    private void extractFormData1() {

        mRejectQTy = CommonUtils.getEditTextValue(mEditTextReject);
        mQcQTy = CommonUtils.getEditTextValue(mEditTextBundleQty);
        mReplaceQTy = CommonUtils.getEditTextValue(mEditTextReplace);

        if(mQcQTy != "" && !mQcQTy.isEmpty()) {
            bundleData.setQcPassQty(Integer.parseInt(mQcQTy));
//            bundleData.setQcPassQty(Integer.parseInt(qcQuantity+""));
        }
        if(mRejectQTy != "" && !mRejectQTy.isEmpty()) {
            bundleData.setRejectQty(Integer.parseInt(mRejectQTy));
        }

        if(mReplaceQTy != "" && !mReplaceQTy.isEmpty()) {
            bundleData.setReplaceQty(Integer.parseInt(mReplaceQTy));
        }

        mItemObj.getData().getDetailsPart().get(mRequestedPOs).getBundleDataList().set(mRequestedBundlePos, bundleData);
    }

    private String mRejectQTy = "";
    private String mQcQTy = "";
    private String mReplaceQTy = "";


    private int getQcQuantity() {
        return qcQuantity+replace-reject;
    }

    private void  setQcQuantity() {
        mEditTextBundleQty.setText(getQcQuantity()+"");
    }
}
