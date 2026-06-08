package com.logicsoftbd.lsl.ui.process.quantityactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcActivity;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.Validator.Field;
import com.logicsoftbd.lsl.utils.Validator.Form;
import com.logicsoftbd.lsl.utils.Validator.validations.InRange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuttingQcQuantityActivity extends BaseActivity {
    public static final String EXTRA_RECEIVE_ID = "extra_quantity_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_id";
    public static Intent getStartIntent(Context context, CuttingQcBarcodeResponse masterPart,  int pos, int bundlePos) {
        Intent intent = new Intent(context, CuttingQcQuantityActivity.class);
        intent.putExtra("position", pos);
        intent.putExtra("bundle_position", bundlePos);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, masterPart);
        intent.putExtras(bundle);
        return intent;
    }
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

    private int mRequestedPOs;
    private int mRequestedBundlePos;
    private Form mForm;

    private QuantityModel mQuantityModel;


    private CuttingQcBarcodeResponse mItemObj;

    private CuttingQcBarcodeResponse.Result.DetailsPart.BundleData bundleData;

    public interface OnRejectListener{
        void onRejectSubmit(String result, int rejectQuantity);
    }

    private  int qcQuantity = 0;
    private int qunantity = 0;
    private  int reject = 0;
    private  int replace = 0;

    private OnRejectListener rejectListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cutting_qc);
        setUnBinder(ButterKnife.bind(this));

        mRequestedPOs = getIntent().getIntExtra("position", 0);
        mRequestedBundlePos = getIntent().getIntExtra("bundle_position", 0);
        mItemObj = (CuttingQcBarcodeResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        setUp();
    }

    @Override
    protected void setUp() {
        mEditTextBundleQty.setText("");
        mEditTextReplace.setText("");
        mEditTextReject.setText("");

        mForm = new Form(this);
       // initValidationForm();
        bundleData = mItemObj.getData().getDetailsPart().get(mRequestedPOs).getBundleDataList().get(mRequestedBundlePos);
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
        rejectListener = new OnRejectListener() {
            @Override
            public void onRejectSubmit(String result, int rejectQuantity) {
                bundleData.setDefectStr(result);

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
            extractFormData();

            setResult(Activity.RESULT_OK, CuttingQcActivity.getStartIntent(this, mItemObj, false));
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
        mForm.addField(Field.using(mEditTextBundleQty, mTextInputLayoutReject).
                validate(InRange.build(this, 0, bundleData.getQcPassQty())));

        mForm.addField(Field.using(mEditTextReject, mTextInputLayoutReject).
                validate(InRange.build(this, 0, bundleData.getQcPassQty())));


        mForm.addField(Field.using(mEditTextReplace, mTextInputLayoutReplace).
                validate(InRange.build(this, 0, reject)));
    }

    private void extractFormData() {

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
