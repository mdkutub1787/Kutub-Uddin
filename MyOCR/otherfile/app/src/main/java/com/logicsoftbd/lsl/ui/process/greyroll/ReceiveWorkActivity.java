package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpRequest;
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
import com.logicsoftbd.lsl.ui.process.quantityactivity.EmbSpQuantityActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveWorkActivity extends BaseActivity implements ReceiveMvpView, GmtsAdapter.Callback, DatePickerDialog.OnDateSetListener  {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_issue_work_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private  static final int PICK_QUANTITY_REQUEST = 2;

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    GmtsAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, EmbSpBarcodeResponse barcodeResponse, Process process) {
        Intent intent = new Intent(context, ReceiveWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, barcodeResponse);
        bundle.putSerializable(EXTRA_PROCESS_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, EmbSpBarcodeResponse barcodeResponse, Process process, boolean isFirst) {
        barcodeResponse.setFirst(isFirst);
        Intent intent = new Intent(context, ReceiveWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, barcodeResponse);
        bundle.putSerializable(EXTRA_PROCESS_ID, process);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.tvDate)
    TextView tvDate;


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

    private RollReceiveRequest rollReceiveRequest;

    private List<EmbSpBarcodeResponse.BodyPart.DetailsPart> productBarcodes = new ArrayList<>();


    private EmbSpBarcodeResponse mBarcodeResponse;
    private Process mProcess;

    private Date mSelectedDate;

    private EmbSpRequest embSpRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_work);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
    }

    @Override
    protected void setUp() {
        mBarcodeResponse = (EmbSpBarcodeResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setTitle(mProcess.getTitle());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData().getDetails());
           // mTextView1.setText(mBarcodeResponse.getData().getMasterPart().getKnittingCompany());
          //  mTextView2.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           // mTextView3.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           // mPresenter.getFloorResponse();
        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isValidForm()) {
                extractFormData();
                // pass the data onto the presenter
                mPresenter.onEmbSpIoRcvSave(embSpRequest);
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

       /* if(mSpinnerPurpose.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a purpose.");
            return false;
        }
*/

        return true;
    }

    private void extractFormData() {
        EmbSpRequest.Result result = new EmbSpRequest.Result();
        EmbSpRequest.MasterPart masterPart = new EmbSpRequest.MasterPart();
        /*masterPart.setCompanyId(1);
        masterPart.setEmbelId(2);
        masterPart.setEmbNameId(2);
        masterPart.setEmbTypeId(1);
        masterPart.setFloorId(20);
        masterPart.setBodyPartId(84);
        masterPart.setLocationId(1);*/
        masterPart.setEmbelId(mBarcodeResponse.getData().getMaster().getEmblId());
        masterPart.setEmbTypeId(mBarcodeResponse.getData().getMaster().getEmblId());
        masterPart.setLocationId(mBarcodeResponse.getData().getMaster().getLocationId());
        masterPart.setFloorId(mBarcodeResponse.getData().getMaster().getFloorId());
        masterPart.setProductionType(mBarcodeResponse.getData().getMaster().getProductionSource());
        masterPart.setCompanyId(mBarcodeResponse.getData().getDetails().getCompanyId());
        masterPart.setDeliveryDate(tvDate.getText().toString());
        masterPart.setProductionType(mBarcodeResponse.getData().getDetails().getProductionType());
        result.setMasterPart(masterPart);
        result.setDetailsPart(productBarcodes);
        embSpRequest = new EmbSpRequest();
        embSpRequest.setData(result);
        embSpRequest.setStatus("true");
    }

    @OnClick(R.id.fab)
    void onFabClick () {
        startActivityForResult(ScannerActivity.getStartIntent(this, mProcess, true), PICK_BARCODE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                EmbSpBarcodeResponse mBarcodeResponse = (EmbSpBarcodeResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if(!hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.add(mBarcodeResponse.getData().getDetails());
                } else {
                    showMessage("Item already on the list");
                }
            }
        }  else if (requestCode == PICK_QUANTITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                EmbSpBarcodeResponse mBarcodeResponse = (EmbSpBarcodeResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if(hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.set(hasItemPos, mBarcodeResponse.getData().getDetails());
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

    private boolean hasItem(EmbSpBarcodeResponse.BodyPart productBarcode) {
        int pos  = 0;
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (EmbSpBarcodeResponse.BodyPart.DetailsPart barcode: productBarcodes) {
                if(barcode.getBarcodeNo().endsWith(productBarcode.getDetails().getBarcodeNo())) {
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
        mBarcodeResponse.getData().setDetails(productBarcodes.get(position));
        Intent intent = EmbSpQuantityActivity.getStartIntent(this, mBarcodeResponse, mProcess, position);
        startActivityForResult(intent, PICK_QUANTITY_REQUEST);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
       // ReceiveDialog.newInstance(this, productBarcodes.get(position)).show(getSupportFragmentManager());
    }
}
