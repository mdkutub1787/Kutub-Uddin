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
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveWorkActivity;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.Validator.Field;
import com.logicsoftbd.lsl.utils.Validator.Form;
import com.logicsoftbd.lsl.utils.Validator.validations.InRange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmbSpQuantityActivity extends BaseActivity {
    public static final String EXTRA_RECEIVE_ID = "extra_quantity_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_id";
    public static Intent getStartIntent(Context context, EmbSpBarcodeResponse masterPart,Process process,  int pos) {
        Intent intent = new Intent(context, EmbSpQuantityActivity.class);
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

    @BindView(R.id.edit_text_replace)
    EditText mEditTextReplace;


    @BindView(R.id.text_layout_qc_qty)
    TextInputLayout mTextInputLayoutQcQty;

    @BindView(R.id.text_layout_reject)
    TextInputLayout mTextInputLayoutReject;

    @BindView(R.id.text_layout_replace)
    TextInputLayout mTextInputLayoutReplace;

    private int mRequestedPOs;
    private Form mForm;

    private QuantityModel mQuantityModel;


    private EmbSpBarcodeResponse mItemObj;

    private Process mProcess;

    private  int qcQuantity = 0;
    private  int reject = 0;
    private  int replace = 0;

    private CuttingQcQuantityActivity.OnRejectListener rejectListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_emb_sp);
        setUnBinder(ButterKnife.bind(this));

        mRequestedPOs = getIntent().getIntExtra("position", 0);

        mItemObj = (EmbSpBarcodeResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        setUp();
    }

    @Override
    protected void setUp() {

        mForm = new Form(this);
       // initValidationForm();
        qcQuantity = mItemObj.getData().getDetails().getQty();
        text_view_1.setText(mItemObj.getData().getDetails().getBarcodeNo());
        text_view_2.setText(mItemObj.getData().getDetails().getQty()+"");

        if(mItemObj.getData().getDetails().getReject() != null ) {
            reject = mItemObj.getData().getDetails().getReject();
            mEditTextReject.setText(reject+"");
        }


        if(mItemObj.getData().getDetails().getReplace() != null ) {
            replace = mItemObj.getData().getDetails().getReplace();
            mEditTextReplace.setText(replace+"");
        }

        rejectListener = new CuttingQcQuantityActivity.OnRejectListener() {
            @Override
            public void onRejectSubmit(String result, int rejectQuantity) {
                mItemObj.getData().getDetails().setDefectStr(result);

            }
        };
        setQcQuantity();

        changeReject();
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
                if( reject >  mItemObj.getData().getDetails().getQty()) {

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
        CuttingQcDialog.newInstance(this, rejectListener, mItemObj.getData().getDetails().getDefectStr()).show(getSupportFragmentManager());;
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(){
        initValidationForm();
        if(mForm.isValid()) {
            extractFormData();

            setResult(Activity.RESULT_OK, ReceiveWorkActivity.getStartIntent(this, mItemObj,mProcess, false));
            finish();
            // pass the data onto the presenter
            // mPresenter.editDeposit(mEditedDeposit, savingId, memberId, memberPrimaryProductId, savingProductId, branchId, samityId, transactionDate, depositAmount, Values.REGULAR_PROCESS, Values.NEW);
        }
    }
    @OnClick(R.id.btn_later)
    void later() {
        finish();
    }

    protected void initValidationForm() {
        mForm.addField(Field.using(mEditTextBundleQty, mTextInputLayoutQcQty).
                validate(InRange.build(this, 0, mItemObj.getData().getDetails().getQty())));

        mForm.addField(Field.using(mEditTextReject, mTextInputLayoutReject).
                validate(InRange.build(this, 0,  mItemObj.getData().getDetails().getQty())));


        mForm.addField(Field.using(mEditTextReplace, mTextInputLayoutReplace).
                validate(InRange.build(this, 0, reject)));
    }

    private void extractFormData() {

        mRejectQTy = CommonUtils.getEditTextValue(mEditTextReject);
        mQcQTy = CommonUtils.getEditTextValue(mEditTextBundleQty);
        mReplaceQTy = CommonUtils.getEditTextValue(mEditTextReplace);

        if(mQcQTy != "" && !mQcQTy.isEmpty()) {
            mItemObj.getData().getDetails().setQcQuantity(Integer.parseInt(mQcQTy));
        }
        if(mRejectQTy != "" && !mRejectQTy.isEmpty()) {
            mItemObj.getData().getDetails().setReject(Integer.parseInt(mRejectQTy));
        }

        if(mReplaceQTy != "" && !mReplaceQTy.isEmpty()) {
            mItemObj.getData().getDetails().setReplace(Integer.parseInt(mReplaceQTy));
        }

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
