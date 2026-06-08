package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
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
import com.logicsoftbd.lsl.data.network.model.RollIssueRequest;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssueActivity extends BaseActivity implements ReceiveMvpView, IssueAdapter.Callback {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private String userID = "";

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    IssueAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, BarcodeIssueResponse process) {
        Intent intent = new Intent(context, IssueActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, BarcodeIssueResponse process, boolean isFirst) {
        process.setFirst(isFirst);
        Intent intent = new Intent(context, IssueActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_view_1)
    TextView mTextView1;

    @BindView(R.id.text_view_2)
    TextView mTextView2;

   /* @BindView(R.id.text_view_3)
    TextView mTextView3;*/

    @BindView(R.id.spinner_issue_purpose)
    Spinner mSpinnerPurpose;

    private List<PurposeResponse.Challan.MasterPart> mPurposeList;

    private RollIssueRequest rollIssueRequest;

    private List<BarcodeIssueResponse.Challan.ProductBarcode> productBarcodes = new ArrayList<>();


    private BarcodeIssueResponse mBarcodeResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
    }

    @Override
    protected void setUp() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = _preferences.getString("login_userid", "");
        mBarcodeResponse = (BarcodeIssueResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setTitle(R.string.grey_roll_issue);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData().getProductBarcodes());
            mTextView1.setText(mBarcodeResponse.getData().getMasterPart().getKnittingCompany());
            mTextView2.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           // mTextView3.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
            mPresenter.getPurposeResponse();
        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.addItems(productBarcodes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isValidForm()) {
                extractFormData();
                // pass the data onto the presenter
                for(int i=0; i<rollIssueRequest.getData().getProductBarcodes().size(); i++){
                    rollIssueRequest.getData().getProductBarcodes().get(i).setUserId(userID);
                }
                mPresenter.onRollIssueSave(rollIssueRequest);

                System.out.println("#####"+new Gson().toJson(rollIssueRequest));
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

        if(mSpinnerPurpose.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a purpose.");
            return false;
        }


        return true;
    }

    private void extractFormData() {
        rollIssueRequest = new RollIssueRequest();
        rollIssueRequest = mPresenter.convertToRollIssue(mBarcodeResponse, productBarcodes);
        rollIssueRequest.getData().getMasterPart().setIssuePurpose( mPurposeList.get( mSpinnerPurpose.getSelectedItemPosition()-1).getId());
    }

    @OnClick(R.id.fab)
    void onFabClick () {
        Process process = new Process(R.drawable.grey_roll_issue, "Grey Roll", "Issue",
                new Process.DataParam("grey_roll", "issue"));
        startActivityForResult(ScannerActivity.getStartIntent(this, process, true), PICK_BARCODE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                BarcodeIssueResponse mBarcodeResponse = (BarcodeIssueResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (!hasItem(mBarcodeResponse.getData().getProductBarcodes())) {
                    productBarcodes.add(mBarcodeResponse.getData().getProductBarcodes());
                } else {
                    showMessage("Item already on the list");
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

    private boolean hasItem(BarcodeIssueResponse.Challan.ProductBarcode productBarcode) {
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (BarcodeIssueResponse.Challan.ProductBarcode barcode: productBarcodes) {
                if(barcode.getBarcodeNo().endsWith(productBarcode.getBarcodeNo())) {
                    return true;
                }
            }
        }

        return false;
    }

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
        mPurposeList = purposeResponse.getData().getMasterPart();

        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mPurposeList != null && mPurposeList.size() > 0) {
            for(PurposeResponse.Challan.MasterPart masterPart : mPurposeList) {
                storeNames.add(masterPart.getPurpose());
            }
        }

        ViewUtils.prepareSpinner(this, mSpinnerPurpose, storeNames );
    }

    @Override
    public void onFloorListResponse(FloorResponse purposeResponse) {

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
}
