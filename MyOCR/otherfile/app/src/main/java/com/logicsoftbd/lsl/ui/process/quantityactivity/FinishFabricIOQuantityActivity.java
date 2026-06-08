package com.logicsoftbd.lsl.ui.process.quantityactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricInputActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpInteractor;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpPresenter;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpView;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.Validator.Form;
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishFabricIOQuantityActivity extends BaseActivity implements ReceiveMvpView {
    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;
    public static final String EXTRA_RECEIVE_ID = "extra_quantity_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_id";
    public static Intent getStartIntent(Context context, Process process, FinishFabricResponse masterPart, int pos) {
        Intent intent = new Intent(context, FinishFabricIOQuantityActivity.class);
        intent.putExtra("position", pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, masterPart);
        bundle.putSerializable(EXTRA_PROCESS_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    @BindView(R.id.text_view_1)
    TextView text_view_1;

    @BindView(R.id.text_view_2)
    TextView text_view_2;

    @BindView(R.id.edit_text_qc_qty)
    EditText mEditTextBundleQty;

    @BindView(R.id.edit_text_actual_gsm)
    EditText mEditTextBundleActualGsm;

    @BindView(R.id.edit_text_actual_dia)
    EditText mEditTextBundleActualDia;

    @BindView(R.id.edit_text_lengthYds)
    EditText mEditTextBundleLengthYds;

    @BindView(R.id.edit_text_roll_width_inch)
    EditText mEditTextBundleRollWidthInch;

    @BindView(R.id.edit_text_reject)
    EditText mEditTextReject;

    @BindView(R.id.text_batch_no)
    TextView text_batch_no;

    @BindView(R.id.text_roll)
    TextView text_roll;

    @BindView(R.id.text_color_name)
    TextView text_color_name;

    @BindView(R.id.text_body_part_name)
    TextView text_body_part_name;

    @BindView(R.id.text_file_no)
    TextView text_file_no;

    @BindView(R.id.text_ref)
    TextView text_ref;

    @BindView(R.id.spinner_machine)
    Spinner mSpnMachine;

    @BindView(R.id.spinner_shift)
    Spinner mSpnShift;

    @BindView(R.id.spinner_shade)
    Spinner mSpnShade;

    @BindView(R.id.text_layout_qc_qty)
    TextInputLayout mTextInputLayoutQcQty;

    @BindView(R.id.text_layout_actual_gsm)
    TextInputLayout mTextInputLayoutActualGsm;

    @BindView(R.id.text_layout_actual_dia)
    TextInputLayout mTextInputLayoutActualDia;

    @BindView(R.id.text_layout_lengthYds)
    TextInputLayout mTextInputLayoutLengthYds;

    @BindView(R.id.text_layout_roll_width_inch)
    TextInputLayout mTextInputLayoutRollWidthInch;

    @BindView(R.id.text_layout_reject)
    TextInputLayout mTextInputLayoutReject;



    private int mRequestedPOs;
    private Form mForm;

    private QuantityModel mQuantityModel;


    private FinishFabricResponse mItemObj;
    private Process mProcess;

    private  double qcQuantity = 0;
    private  double reject = 0;
    private  double rollLength = 0;
    private  double rollWidth = 0;
    private  int alter = 0;
    private  int spot = 0;
    private  int replace = 0;
    private  int shiftId = 0;
    private  int machineId = 0;
    private  int shadeId = 0;
    private List<MachineResponses.Challan> mMachineList;
    private List<ShiftResponses.Challan> mShiftList;
    private List<FabricShade.Result> mShadeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_i_o_quantity);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mRequestedPOs = getIntent().getIntExtra("position", 0);
        mPresenter.onAttach(this);
        mItemObj = (FinishFabricResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        setUp();
        mPresenter.getMachine();
        mPresenter.getShift();
        mPresenter.getFabricShade();
    }
    @Override
    protected void setUp() {

        mForm = new Form(this);
        //initValidationForm();
        //qcQuantity = mItemObj.getData().getQNTY();
        text_view_1.setText(mItemObj.getData().getBARCODE_NO());
        if (mItemObj.getData().getQcQuantity()!=null){
            mEditTextBundleQty.setText(String.valueOf(mItemObj.getData().getQcQuantity()));
        }
        if (mItemObj.getData().getReject()!=null){
            mEditTextReject.setText(String.valueOf(mItemObj.getData().getReject()));
        }

        if (mItemObj.getData().getActualDia()!=null){
            mEditTextBundleActualDia.setText(String.valueOf(mItemObj.getData().getActualDia()));
        }

        if (mItemObj.getData().getRollWidthInch()!=null){
            mEditTextBundleRollWidthInch.setText(String.valueOf(mItemObj.getData().getRollWidthInch()));
        }

        if (mItemObj.getData().getActualGsm()!=null){
            mEditTextBundleActualGsm.setText(String.valueOf(mItemObj.getData().getActualGsm()));
        }

        if (mItemObj.getData().getLengthYds()!=null){
            mEditTextBundleLengthYds.setText(String.valueOf(mItemObj.getData().getLengthYds()));
        }

        text_view_2.setText(mItemObj.getData().getQNTY()+"");
        text_batch_no.setText(mItemObj.getData().getBATCH_NO()+"");
        text_roll.setText(mItemObj.getData().getROLL_NO()+"");
        text_color_name.setText(mItemObj.getData().getCOLOR_NAME()+"");
        text_body_part_name.setText(mItemObj.getData().getBODY_PART_NAME()+"");
        text_ref.setText(mItemObj.getData().getINTERNAL_REF()+"");
        text_file_no.setText(mItemObj.getData().getFILE_NO()+"");
        mEditTextBundleActualDia.setText(mItemObj.getData().getWIDTH()+"");
        mEditTextBundleActualGsm.setText(mItemObj.getData().getGSM()+"");

        setQcQuantity();

        changeReject();
        changeQc();
        changeFabricWidth();
        mSpnMachine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                machineId=mMachineList.get(i).getId();
                Log.e("spinner","spinner"+mMachineList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpnShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shiftId=  mShiftList.get(i).getId();
                Log.e("spinner","spinner"+mShiftList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpnShade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shadeId=  mShadeList.get(i).getID();
                Log.e("spinner","spinner"+mShadeList.get(i).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void changeReject() {
        mEditTextReject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    reject = Double.parseDouble(charSequence.toString());
                } else {
                    reject  = 0;
                }
                String Qc= CommonUtils.getEditTextValue(mEditTextBundleQty);

                if(!Qc.equals("")){
                    if(charSequence.length() > 0) {
                        qcQuantity = Double.parseDouble(Qc);
                    } else {
                        qcQuantity  = 0;
                    }
                }
                else{
                    qcQuantity  = 0;
                }

                double total=qcQuantity+reject;
                mTextInputLayoutReject.setError(null);
                if(total > mItemObj.getData().getQNTY()) {
                    reject  = 0;
                    qcQuantity  = 0;
                    mTextInputLayoutReject.setError("Reject and QC quantity can not be greater than quantity");
                }
                else{
                    mTextInputLayoutReject.setError(null);
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeQc() {
        mEditTextBundleQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    qcQuantity = Double.parseDouble(charSequence.toString());
                    double release=mItemObj.getData().getQNTY()-qcQuantity;
                    if (release > 0) {
                        mEditTextReject.setText(String.valueOf(release));
                    }
                    else{
                        mEditTextReject.setText("");
                    }

                } else {
                    qcQuantity  = 0;
                    mEditTextReject.setText("");
                }
                String Qc= CommonUtils.getEditTextValue(mEditTextReject);

                if(!Qc.equals("")){
                    if(charSequence.length() > 0) {
                        reject = Double.parseDouble(Qc);

                    } else {
                        reject  = 0;

                    }
                }
                else{
                    qcQuantity  = 0;
                }

                double total=qcQuantity+reject;
                mTextInputLayoutReject.setError(null);
                if(total > mItemObj.getData().getQNTY()) {
                    reject  = 0;
                    qcQuantity  = 0;
                    mTextInputLayoutQcQty.setError("Reject and QC quantity can not be greater than quantity");
                }
                else{
                    mTextInputLayoutQcQty.setError(null);
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeFabricWidth() {
        mEditTextBundleRollWidthInch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    rollWidth = Double.parseDouble(charSequence.toString());

                    if (mEditTextBundleActualGsm.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Actual GSM can not be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        rollLength = ((mItemObj.getData().getQNTY() * 1000) / (Double.parseDouble(mEditTextBundleActualGsm.getText().toString()) * rollWidth * 0.0254) * 1.09361);

                        mEditTextBundleLengthYds.setText(String.format("%.2f", rollLength));
                    }
                } else {
                    rollLength  = 0;
                    mEditTextBundleLengthYds.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }




    @OnClick(R.id.btn_submit)
    void onSubmit(){

        String Rj= CommonUtils.getEditTextValue(mEditTextReject);
        String Qc= CommonUtils.getEditTextValue(mEditTextBundleQty);
        String actualGSM= CommonUtils.getEditTextValue(mEditTextBundleActualGsm);
        String actualDia= CommonUtils.getEditTextValue(mEditTextBundleActualDia);
        double q_c=0;
        double r_j=0;
        if(!Qc.equals("")){
            q_c=Double.parseDouble(Qc);
        }
        if(!Rj.equals("")){
            r_j=Double.parseDouble(Rj);
        }
        double total=q_c+r_j;

        if (total==0){
            Toast.makeText(this, "Reject and quantity can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(total>mItemObj.getData().getQNTY()){
            Toast.makeText(this, "Reject and QC quantity can not be greater than quantity", Toast.LENGTH_SHORT).show();
        }
        else if(actualGSM.isEmpty() || actualDia.isEmpty()){
            Toast.makeText(this, "Actual GSM and Actual Dia can not be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            if(mForm.isValid()) {
            extractFormData();
            setResult(Activity.RESULT_OK, FinishFabricInputActivity.getStartIntent(this,mProcess, mItemObj, false));
            finish();
            // pass the data onto the presenter
            // mPresenter.editDeposit(mEditedDeposit, savingId, memberId, memberPrimaryProductId, savingProductId, branchId, samityId, transactionDate, depositAmount, Values.REGULAR_PROCESS, Values.NEW);
        }
        }

//
    }
    @OnClick(R.id.btn_later)
    void later() {
        finish();
    }

    protected void initValidationForm() {
//        mForm.addField(Field.using(mEditTextReject, mTextInputLayoutReject).
//                validate(InRange.build(this, 0, mItemObj.getData().getQNTY())));


    }

    private void extractFormData() {
        mRejectQTy = CommonUtils.getEditTextValue(mEditTextReject);
        mQcQTy = CommonUtils.getEditTextValue(mEditTextBundleQty);
        mActualGsm = CommonUtils.getEditTextValue(mEditTextBundleActualGsm);
        mActualDia = CommonUtils.getEditTextValue(mEditTextBundleActualDia);
        mRollWidthInch = CommonUtils.getEditTextValue(mEditTextBundleRollWidthInch);
        mLengthYds = CommonUtils.getEditTextValue(mEditTextBundleLengthYds);
        mItemObj.getData().setSHIFT_ID(shiftId);
        mItemObj.getData().setMACHINE_ID(machineId);
        mItemObj.getData().setSHADE_ID(shadeId);

        if(mQcQTy != "" && !mQcQTy.isEmpty()) {
            mItemObj.getData().setQcQuantity(Double.parseDouble(mQcQTy));
        }
        if(mRejectQTy != "" && !mRejectQTy.isEmpty()) {
            mItemObj.getData().setReject(Double.parseDouble(mRejectQTy));
        }

        if(mActualGsm != "" && !mActualGsm.isEmpty()) {
            mItemObj.getData().setActualGsm(Double.parseDouble(mActualGsm));
        }

        if(mActualDia != "" && !mActualDia.isEmpty()) {
            mItemObj.getData().setActualDia(Double.parseDouble(mActualDia));
        }

        if(mLengthYds != "" && !mLengthYds.isEmpty()) {
            mItemObj.getData().setLengthYds(Double.parseDouble(mLengthYds));
        }

        if(mRollWidthInch != "" && !mRollWidthInch.isEmpty()) {
            mItemObj.getData().setRollWidthInch(Double.parseDouble(mRollWidthInch));
        }
    }

    private String mRejectQTy = "", mQcQTy = "", mActualGsm = "", mActualDia = "", mLengthYds = "", mRollWidthInch = "";

    private void  setQcQuantity() {
      //  mEditTextBundleQty.setText(getQcQuantity()+"");
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onStoreListResponse(StoreResponse storeResponse) {

    }

    @Override
    public void onPurposeListResponse(PurposeResponse purposeResponse) {

    }

    @Override
    public void onFloorListResponse(FloorResponse purposeResponse) {

    }

    @Override
    public void onMachineResponses(MachineResponses machineResponses) {
        Log.e("machineResponses","machineResponses"+new Gson().toJson(machineResponses));
        mMachineList=machineResponses.getData();
        ArrayList<String> machineNames = new ArrayList<>();
        if( machineResponses.getData().size() > 0) {
            for(MachineResponses.Challan machine : machineResponses.getData()) {
                machineNames.add(machine.getMachine());
            }
        }
        ViewUtils.prepareSpinner(this, mSpnMachine, machineNames );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItemObj.getData().getSHIFT_ID() != null) {
                    int div = mItemObj.getData().getMACHINE_ID() ;

                    for (int i = 0; i < mMachineList.size(); i++) {
                        if (mMachineList.get(i).getId() == div) {
                            mSpnMachine.setSelection(i);
                        }
                    }
                }
            }
        },300);
    }

    @Override
    public void onShiftResponses(ShiftResponses shiftResponses) {
        Log.e("shiftResponses","shiftResponses"+new Gson().toJson(shiftResponses));
        mShiftList=shiftResponses.getData();
        ArrayList<String> shiftNames = new ArrayList<>();
        if( shiftResponses.getData().size() > 0) {
            for(ShiftResponses.Challan machine : shiftResponses.getData()) {
                shiftNames.add(machine.getShift());
            }
        }
        ViewUtils.prepareSpinner(this, mSpnShift, shiftNames );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItemObj.getData().getSHIFT_ID() != null) {
                    int div = mItemObj.getData().getSHIFT_ID() ;

                    for (int i = 0; i < mShiftList.size(); i++) {
                        if (mShiftList.get(i).getId() == div) {
                            mSpnShift.setSelection(i);
                        }
                    }
                }
            }
        },300);
    }

    @Override
    public void onFabricShade(FabricShade shadeResponses) {
        Log.e("shiftResponses","shiftResponses"+new Gson().toJson(shadeResponses));
        mShadeList=shadeResponses.getData();
        ArrayList<String> shadeNames = new ArrayList<>();
        if( shadeResponses.getData().size() > 0) {
            for(FabricShade.Result machine : shadeResponses.getData()) {
                shadeNames.add(machine.getSTORE_NAME());
            }
        }
        ViewUtils.prepareSpinner(this, mSpnShade, shadeNames );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItemObj.getData().getSHADE_ID() != null) {
                    int div = mItemObj.getData().getSHADE_ID() ;

                    for (int i = 0; i < mShadeList.size(); i++) {
                        if (mShadeList.get(i).getID() == div) {
                            mSpnShade.setSelection(i);
                        }
                    }
                }
            }
        },300);
    }

    @Override
    public void onLocationListResponse(FloorResponse purposeResponse) {

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

}