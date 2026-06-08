package com.logicsoftbd.lsl.ui.process.quantityactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.SewingInputActivity;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.Validator.Field;
import com.logicsoftbd.lsl.utils.Validator.Form;
import com.logicsoftbd.lsl.utils.Validator.validations.InRange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SewingIOQuantityActivity extends BaseActivity {
    public static final String EXTRA_RECEIVE_ID = "extra_quantity_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_id";
    public static Intent getStartIntent(Context context, Process process, SewingResponse masterPart, int pos) {
        Intent intent = new Intent(context, SewingIOQuantityActivity.class);
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

    @BindView(R.id.edit_text_reject)
    EditText mEditTextReject;

    @BindView(R.id.edit_text_alter)
    EditText mEditTextAlter;

    @BindView(R.id.edit_text_spot)
    EditText mEditTextSpot;

    @BindView(R.id.edit_text_replace)
    EditText mEditTextReplace;


    @BindView(R.id.text_layout_qc_qty)
    TextInputLayout mTextInputLayoutQcQty;

    @BindView(R.id.text_layout_reject)
    TextInputLayout mTextInputLayoutReject;

    @BindView(R.id.text_layout_alter)
    TextInputLayout mTextInputLayoutAlter;

    @BindView(R.id.text_layout_spot)
    TextInputLayout mTextInputLayoutSpot;

    @BindView(R.id.text_layout_replace)
    TextInputLayout mTextInputLayoutReplace;

    private int mRequestedPOs;
    private Form mForm;

    private QuantityModel mQuantityModel;
    private CuttingQcQuantityActivity.OnRejectListener rejectListener;

    private SewingResponse mItemObj;
    private Process mProcess;

    private  int qcQuantity = 0;
    private  int reject = 0;
    private  int alter = 0;
    private  int spot = 0;
    private  int replace = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sewing);
        setUnBinder(ButterKnife.bind(this));

        mRequestedPOs = getIntent().getIntExtra("position", 0);

        mItemObj = (SewingResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        setUp();
    }

    @Override
    protected void setUp() {

        mForm = new Form(this);
        //initValidationForm();
        qcQuantity = mItemObj.getData().getMasterPart().getqTY();
        text_view_1.setText(mItemObj.getData().getMasterPart().getBarcodeNo());
        text_view_2.setText(mItemObj.getData().getMasterPart().getqTY()+"");

        if(mItemObj.getData().getMasterPart().getReject() != null ) {
            reject = mItemObj.getData().getMasterPart().getReject();
            mEditTextReject.setText(reject+"");
        }


        if(mItemObj.getData().getMasterPart().getAlter() != null ) {
            alter = mItemObj.getData().getMasterPart().getAlter();
            mEditTextAlter.setText(alter+"");
        }


        if(mItemObj.getData().getMasterPart().getSpot() != null ) {
            spot = mItemObj.getData().getMasterPart().getSpot();
            mEditTextSpot.setText(spot+"");
        }


        if(mItemObj.getData().getMasterPart().getReplace() != null ) {
            replace = mItemObj.getData().getMasterPart().getReplace();
            mEditTextReplace.setText(replace+"");
        }

        rejectListener = new CuttingQcQuantityActivity.OnRejectListener() {
            @Override
            public void onRejectSubmit(String result, int rejectQuantity) {
                if(CuttingQcDialog.mType == CuttingQcDialog.REJECT) {
                    mItemObj.getData().getMasterPart().setRejectStr(result);
                }
                if(CuttingQcDialog.mType == CuttingQcDialog.ALTER) {
                    mItemObj.getData().getMasterPart().setAlterStr(result);
                }
                if(CuttingQcDialog.mType == CuttingQcDialog.SPOT) {
                    mItemObj.getData().getMasterPart().setSpotStr(result);
                }

            }
        };

        setQcQuantity();

        changeReject();
        changeAlter();
        changeSpot();
        changeReplace();
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
                if(reject > mItemObj.getData().getMasterPart().getqTY()) {
                    reject  = 0;
                    mTextInputLayoutReject.setError("Reject quantity can not be greater than QC quantity");
                }
                if(reject == 0){
                    mItemObj.getData().getMasterPart().setReject(null);
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeAlter() {
        mEditTextAlter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    alter = Integer.parseInt(charSequence.toString());
                } else {
                    alter  = 0;
                }
                mTextInputLayoutAlter.setError(null);
                if(alter > mItemObj.getData().getMasterPart().getqTY()) {
                    alter  = 0;
                    mTextInputLayoutAlter.setError("Alter quantity can not be greater than QC quantity");
                }
                if(alter == 0){
                    mItemObj.getData().getMasterPart().setAlter(null);
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void changeSpot() {
        mEditTextSpot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    spot = Integer.parseInt(charSequence.toString());
                } else {
                    spot  = 0;
                }
                mTextInputLayoutSpot.setError(null);
                if(spot > mItemObj.getData().getMasterPart().getqTY()) {
                    spot  = 0;
                    mTextInputLayoutSpot.setError("Spot quantity can not be greater than QC quantity");
                }
                if(spot == 0){
                    mItemObj.getData().getMasterPart().setSpot(null);
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
                if(replace > getRSA()) {
                    replace  = 0;
                    mTextInputLayoutReplace.setError("Replace quantity can not be greater than (reject+alter+spot) quantity");
                }
                if(replace == 0){
                    mItemObj.getData().getMasterPart().setReplace(null);
                }
                setQcQuantity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btn_defect_reject)
    void onClickReject() {
        CuttingQcDialog.newInstance(this, rejectListener, mItemObj.getData().getMasterPart().getRejectStr(), CuttingQcDialog.REJECT).show(getSupportFragmentManager());
    }

    @OnClick(R.id.btn_defect_alter)
    void onClickAlter() {
        mAlterQTy = CommonUtils.getEditTextValue(mEditTextAlter);
        if(mAlterQTy.equals("") || mAlterQTy.equals("0")){
            showAlertDialog("Please enter alter value.");
        }else{
            CuttingQcDialog.newInstance(this, rejectListener, mItemObj.getData().getMasterPart().getAlterStr(), CuttingQcDialog.ALTER).show(getSupportFragmentManager());
        }
    }

    @OnClick(R.id.btn_defect_spot)
    void onClickSpot() {
        mSpotQTy = CommonUtils.getEditTextValue(mEditTextSpot);
        if(mSpotQTy.equals("") || mSpotQTy.equals("0")){
            showAlertDialog("Please enter spot value.");
        }else{
            CuttingQcDialog.newInstance(this, rejectListener, mItemObj.getData().getMasterPart().getSpotStr(), CuttingQcDialog.SPOT).show(getSupportFragmentManager());
        }
    }
    @OnClick(R.id.btn_submit)
    void onSubmit(){
        initValidationForm();
        if(mForm.isValid()) {
            extractFormData();
            if(!mRejectQTy.equals("") && (mItemObj.getData().getMasterPart().getRejectStr() == null || mItemObj.getData().getMasterPart().getRejectStr().equals(""))){
                showAlertDialog("Please enter minimum 1 reject & defect count.");
            }else if(!mAlterQTy.equals("") && (mItemObj.getData().getMasterPart().getAlterStr() == null || mItemObj.getData().getMasterPart().getAlterStr().equals(""))){
                showAlertDialog("Please enter minimum 1 alter defect count.");
            }else if(!mSpotQTy.equals("") && (mItemObj.getData().getMasterPart().getSpotStr() == null || mItemObj.getData().getMasterPart().getSpotStr().equals(""))){
                showAlertDialog("Please enter minimum 1 spot defect count.");
            }else{
                setResult(Activity.RESULT_OK, SewingInputActivity.getStartIntent(this, mProcess, mItemObj, false));
                finish();
            }
//            setResult(Activity.RESULT_OK, SewingInputActivity.getStartIntent(this,mProcess, mItemObj, false));
//            finish();
            // pass the data onto the presenter
            // mPresenter.editDeposit(mEditedDeposit, savingId, memberId, memberPrimaryProductId, savingProductId, branchId, samityId, transactionDate, depositAmount, Values.REGULAR_PROCESS, Values.NEW);
        }
    }
    @OnClick(R.id.btn_later)
    void later() {
        finish();
    }

    protected void initValidationForm() {
        mForm.addField(Field.using(mEditTextReject, mTextInputLayoutReject).
                validate(InRange.build(this, 0, mItemObj.getData().getMasterPart().getqTY())));

        mForm.addField(Field.using(mEditTextAlter, mTextInputLayoutAlter).
                validate(InRange.build(this, 0,mItemObj.getData().getMasterPart().getqTY())));

        mForm.addField(Field.using(mEditTextSpot, mTextInputLayoutSpot).
                validate(InRange.build(this, 0, mItemObj.getData().getMasterPart().getqTY())));

        mForm.addField(Field.using(mEditTextReplace, mTextInputLayoutReplace).
                validate(InRange.build(this, 0, getRSA())));
    }

    private void extractFormData() {
        mRejectQTy = CommonUtils.getEditTextValue(mEditTextReject);
        mQcQTy = CommonUtils.getEditTextValue(mEditTextBundleQty);
        mAlterQTy = CommonUtils.getEditTextValue(mEditTextAlter);
        mSpotQTy = CommonUtils.getEditTextValue(mEditTextSpot);
        mReplaceQTy = CommonUtils.getEditTextValue(mEditTextReplace);

        if(mQcQTy != "" && !mQcQTy.isEmpty()) {
            mItemObj.getData().getMasterPart().setQcQuantity(Integer.parseInt(mQcQTy));
        }
        if(mRejectQTy != "" && !mRejectQTy.isEmpty()) {
            mItemObj.getData().getMasterPart().setReject(Integer.parseInt(mRejectQTy));
        }
        if(mAlterQTy != "" && !mAlterQTy.isEmpty()) {
            mItemObj.getData().getMasterPart().setAlter(Integer.parseInt(mAlterQTy));
        }
        if(mSpotQTy != "" && !mSpotQTy.isEmpty()) {
            mItemObj.getData().getMasterPart().setSpot(Integer.parseInt(mSpotQTy));
        }
        if(mReplaceQTy != "" && !mReplaceQTy.isEmpty()) {
            mItemObj.getData().getMasterPart().setReplace(Integer.parseInt(mReplaceQTy));
        }

    }

    private String mRejectQTy = "";
    private String mQcQTy = "";
    private String mAlterQTy = "";
    private String mSpotQTy = "";
    private String mReplaceQTy = "";


    private int getQcQuantity() {
        return qcQuantity+replace-reject-spot-alter;
    }

    private int getRSA() {
        return reject+alter+spot;
    }

    private void  setQcQuantity() {
        mEditTextBundleQty.setText(getQcQuantity()+"");
    }
}
