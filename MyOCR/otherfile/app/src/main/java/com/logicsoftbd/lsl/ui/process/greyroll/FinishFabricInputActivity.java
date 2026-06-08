package com.logicsoftbd.lsl.ui.process.greyroll;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRequest;
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
import com.logicsoftbd.lsl.data.network.model.SpinnerModel;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.DatePickerFragment;
import com.logicsoftbd.lsl.ui.process.quantityactivity.FinishFabricIOQuantityActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.DateUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@SuppressLint({ "HandlerLeak", "NewApi" })
public class FinishFabricInputActivity extends BaseActivity implements ReceiveMvpView, FinishFabricAdapter.Callback, DatePickerDialog.OnDateSetListener  {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    public static final String EXTRA_PROCESS_ID = "extra_bundle_process_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private  static final int PICK_QUANTITY_REQUEST = 2;

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    FinishFabricAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, Process model, FinishFabricResponse process) {
        Intent intent = new Intent(context, FinishFabricInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, Process model, FinishFabricResponse process, boolean isFirst) {
        process.setFirst(isFirst);
        Intent intent = new Intent(context, FinishFabricInputActivity.class);
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

    @BindView(R.id.text_service_source)
    TextView mtvServiceSource;
    @BindView(R.id.text_service_company)
    TextView mtvServiceCompany;
    @BindView(R.id.text_company)
    TextView mtvCompany;

    BixolonLabelPrinter mBixolonLabelPrinter;

    Boolean forTest=false;
    private FinishFabricRequest sewingRequest;

    private List<FinishFabricResponse.ResultSet> productBarcodes = new ArrayList<>();

    private ArrayList<String> mHourList;
    private FinishFabricResponse mBarcodeResponse;
    private Process mProcess;

    private Date mSelectedDate;
    CheckBox checkbox;
    ArrayList<SpinnerModel> mArrayList = new ArrayList<>();
    ArrayAdapter<SpinnerModel> mArrayAdapter;
    private String userId;
    private Boolean singleClick = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_input);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
        invalidateOptionsMenu();
        requestPermission();
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = _preferences.getString("login_userid", "");
    }

    @Override
    protected void setUp() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mArrayList = getList();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayList);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBixolonLabelPrinter = new BixolonLabelPrinter(this, mHandler, Looper.getMainLooper());
        mBarcodeResponse = (FinishFabricResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }


        mToolbar.setTitle(R.string.finish_fabric);
        setSupportActionBar(mToolbar);
        final Drawable backArrow = getResources().getDrawable(R.drawable.back); // assuming your back button icon is ic_arrow_back
        backArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData());

        }
        // setHourSpinner();
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mtvCompany.setText(mBarcodeResponse.getData().getCOMPANY_NAME());
        mtvServiceSource.setText(mBarcodeResponse.getData().getSERVICE_SOURCE());
        mtvServiceCompany.setText(mBarcodeResponse.getData().getSERVICE_COMPANY());
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
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isValidForm() && singleClick) {
                extractFormData();
                singleClick = false;
                Log.e("data","data"+new Gson().toJson(sewingRequest));

                // pass the data onto the presenter
                if (!forTest){
                    singleClick = true;
                    item.setTitle("PRINT");
                    mPresenter.onFinishFabricIoSave(sewingRequest);
                }
                else{
                    extractFormData();
                    showWifiDialog(FinishFabricInputActivity.this, mBixolonLabelPrinter);
                    Log.e("data","data"+productBarcodes.size());
                }

            }
            return true;
        }
        else if (id == R.id.action_new){
            finish();

        }
        return super.onOptionsItemSelected(item);

    }
    void showWifiDialog(Context context, final BixolonLabelPrinter printer) {
        AlertDialog dialog = null;
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_wifi, null);
            Spinner spinner =layout.findViewById(R.id.spinner_ip);
            spinner.setAdapter(mArrayAdapter);

            dialog = new AlertDialog.Builder(context).setView(layout).setTitle("Wi-Fi Connect")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
// EditText editText = (EditText) layout.findViewById(R.id.editText1);
// String ip = editText.getText().toString();

                            String ip = spinner.getSelectedItem().toString();

                            EditText editText = (EditText) layout.findViewById(R.id.editText2);
                            int port = Integer.parseInt(editText.getText().toString());

                            printer.connect(ip, port, 5000);
                            connectPrinter();

                        }
                    }).create();
        }
        dialog.show();
    }
    private void connectPrinter() {

        for(FinishFabricResponse.ResultSet detailsPart : productBarcodes){
            File file = new File(FinishFabricInputActivity.this.getFilesDir(), "text");
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM hh:mm aa");
                Date date1 = new Date(System.currentTimeMillis());
                String currentDate = formatter.format(date1);
                double qc;
                qc=detailsPart.getQcQuantity();
                if (qc<0){
                    qc=detailsPart.getQNTY();
                }

                String bodyPart = "";
                if (detailsPart.getBODY_PART_NAME().length() > 13) {
                    bodyPart = detailsPart.getBODY_PART_NAME().substring(0, 13);
                }else{
                    bodyPart = detailsPart.getBODY_PART_NAME();
                }


                String s5 = "SW600\n" +
                        "SL1230,0,G\n" +
                        "SM20,20\n" +
                        "SOB\n" +
                        "\n" +
                        "B215,70,Q,2,M,6,0,'"+detailsPart.getBARCODE_NO()+"' \n" +
                        "\n" +
                        "T160,70,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
                        "T15,230,2,1,1,0,0,N,N,'"+"F-"+detailsPart.getFILE_NO()+",Ref-"+detailsPart.getINTERNAL_REF()+", R.Roll: "+detailsPart.getROLL_NO()+" ,"+detailsPart.getYARN_LOT()+",'\n" +
                        "T15,265,2,1,1,0,0,N,N,'"+"R.Dia-"+detailsPart.getWIDTH()+",R.GSM-"+detailsPart.getGSM()+"," +bodyPart+",'\n" +
                        "T15,300,2,1,1,0,0,N,N,'"+detailsPart.getCOLOR_NAME()+" ,"+detailsPart.getITEM_DESCRIPTION()+"'\n" +
                        "T160,105,2,1,1,0,0,N,N,'"+detailsPart.getBARCODE_NO()+"'\n" +
                        "T160,10,4,1,1,0,0,N,N,'"+detailsPart.getBUYER_NAME()+"'\n" +
                        "T160,140,2,1,1,0,0,N,N,'"+detailsPart.getBATCH_NO()+"'\n" +
                        "T360,130,4,1,1,0,0,N,N,'"+qc+"Kg'\n" +
                        "P1,1";

                Log.d("TAG", "connectPrinter: "+s5);
                File gpxfile = new File(file, "sample" + detailsPart.getBARCODE_NO());
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(s5);
                writer.flush();
                writer.close();
//                    output.setText(readFile());

            } catch (Exception e) {
            }
        }

    }
    private ArrayList<SpinnerModel> getList() {
        ArrayList<SpinnerModel> SpinnerModelArrayList = new ArrayList<>();
        SpinnerModel model2 = new SpinnerModel("192.168.0.51", 1);
        SpinnerModel model3 = new SpinnerModel("192.168.0.52", 2);
        SpinnerModel model4 = new SpinnerModel("192.168.0.53", 3);
        SpinnerModel model5 = new SpinnerModel("192.168.0.54", 4);
        SpinnerModel model6 = new SpinnerModel("192.168.0.55", 5);
        SpinnerModel model7 = new SpinnerModel("10.10.10.115", 6);
        SpinnerModel model8 = new SpinnerModel("192.168.11.165", 7);

        SpinnerModelArrayList.add(model2);
        SpinnerModelArrayList.add(model3);
        SpinnerModelArrayList.add(model4);
        SpinnerModelArrayList.add(model5);
        SpinnerModelArrayList.add(model6);
        SpinnerModelArrayList.add(model7);
        SpinnerModelArrayList.add(model8);
        return SpinnerModelArrayList;

    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            try {
                switch (msg.what) {
                    case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BixolonLabelPrinter.STATE_CONNECTED:
                                Toast.makeText(FinishFabricInputActivity.this, "Device is connected", Toast.LENGTH_SHORT).show();
                                readFile();
                                break;
                            case BixolonLabelPrinter.STATE_CONNECTING:
                                Toast.makeText(FinishFabricInputActivity.this, "Device is connecting", Toast.LENGTH_SHORT).show();
                                break;
                            case BixolonLabelPrinter.STATE_NONE:
                                Toast.makeText(FinishFabricInputActivity.this, "connect is failed or disconnected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                }
            } catch (Exception e) {
                Toast.makeText(FinishFabricInputActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private boolean isValidForm() {
        if(productBarcodes == null || productBarcodes.size() == 0) {
            showAlertDialog("Please add barcode.");
            return false;
        }




        return true;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void extractFormData() {
        FinishFabricRequest.Result result = new FinishFabricRequest.Result();
        FinishFabricRequest.MasterPart masterPart = new FinishFabricRequest.MasterPart();
        masterPart.setCompanyId(Integer.parseInt(mBarcodeResponse.getData().getCOMPANY_ID()));
        masterPart.setServiceCompany(Integer.valueOf(mBarcodeResponse.getData().getSERVICE_COMPANY_ID()));
        masterPart.setServiceSource(Integer.valueOf(mBarcodeResponse.getData().getSERVICE_SOURCE_ID()));
        masterPart.setUserId(Integer.valueOf(userId));
        masterPart.setReceiveDate(tvDate.getText().toString());
        result.setMasterPart(new FinishFabricRequest.MasterPart());
        result.setDetailsPart(productBarcodes);
        result.setMasterPart(masterPart);

        sewingRequest = new FinishFabricRequest();
        sewingRequest.setData(result);
        sewingRequest.setStatus("true");
        //  rollReceiveRequest = mPresenter.convertToRollIssue(mBarcodeResponse, productBarcodes);
        //  rollReceiveRequest.getData().getMasterPart().setIssuePurpose( mPurposeList.get( mSpinnerPurpose.getSelectedItemPosition()-1).getId());
    }

    @OnClick(R.id.fab)
    void onFabClick () {
        startActivityForResult(ScannerActivity.getStartIntent(this, mProcess, true), PICK_BARCODE_REQUEST);
        singleClick = true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                FinishFabricResponse mBarcodeResponse = (FinishFabricResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (!hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.add(mBarcodeResponse.getData());
                } else {
                    showMessage("Item already on the list");
                }
            }
        } else if (requestCode == PICK_QUANTITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                FinishFabricResponse mBarcodeResponse = (FinishFabricResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.set(hasItemPos, mBarcodeResponse.getData());
                }
            }
        }
    }

    @Override
    public void onRepoEmptyViewRetryClick() {

    }
    private void readFile() {

        for (FinishFabricResponse.ResultSet detailsPart : productBarcodes) {
            File fileEvents = new File(FinishFabricInputActivity.this.getFilesDir() + "/text/sample" + detailsPart.getBARCODE_NO());
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileEvents));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
            }
            String result = text.toString();
            mBixolonLabelPrinter.executeDirectIo(result, false, 0);
            fileEvents.delete();
        }
        mBixolonLabelPrinter.disconnect();
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


    private boolean hasItem(FinishFabricResponse.ResultSet productBarcode) {
        int pos  = 0;
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (FinishFabricResponse.ResultSet barcode: productBarcodes) {
                if(barcode.getBARCODE_NO().endsWith(productBarcode.getBARCODE_NO())) {
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
              //  finish();
                forTest=true;
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
        mBarcodeResponse.setData(productBarcodes.get(position));
        Intent intent = FinishFabricIOQuantityActivity.getStartIntent(this, mProcess, mBarcodeResponse, position);
        startActivityForResult(intent, PICK_QUANTITY_REQUEST);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        singleClick = true;
        //setResult(Activity.RESULT_OK, SewingIOQuantityActivity.getStartIntent(this));
        // SewingDialog.newInstance(this, productBarcodes.get(position)).show(getSupportFragmentManager());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdfFile(String path) {
        if (new File(path).exists())
            new File(path).delete();
        Rectangle pagesize = new Rectangle(512, 861);
        Document document=new Document(pagesize,10,0,10,0);
        try {
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            //  document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Evan");
            document.addCreator("Khan");
            document.setMargins(0,0,50,0);
            //  Chunk glue = new Chunk(new VerticalPositionMark());

            int size=sewingRequest.getData().getDetailsPart().size();
            for(FinishFabricResponse.ResultSet detailsPart:sewingRequest.getData().getDetailsPart()){
                addNewItem(document,detailsPart, Element.ALIGN_LEFT);
                Paragraph paragraph1 = new Paragraph(" ");
                Paragraph paragraph2 = new Paragraph(" ");
                Paragraph paragraph3 = new Paragraph(" ");
                Paragraph paragraph4 = new Paragraph(" ");
                Paragraph paragraph5 = new Paragraph(" ");
                document.add(paragraph1);
                document.add(paragraph2);
                document.newPage();
//                document.add(paragraph3);
//                document.add(paragraph4);
//                document.add(paragraph5);
            }

//            Paragraph p = new Paragraph();
//            p.add("Text to the left");
//            p.add(glue);
//            p.add("Text to the right");
//
//            document.add(p);
            document.setMargins(-100,0,200,0);
            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            printPDF();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter=new PDFDocumentAdapter(FinishFabricInputActivity.this,Common.getAppPath(FinishFabricInputActivity.this)+"test_pdf.pdf");
        printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());

    }

    private void addLineSeparator(Document document) {

        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        try {
            document.add(new Chunk(lineSeparator));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addLineSpace(document);
    }

    private void addLineSpace(Document document) {
        try {
            document.add(new Paragraph(""));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem(Document document, FinishFabricResponse.ResultSet finish, int alignCenter) throws com.google.zxing.WriterException {

        PdfPTable table1 = new PdfPTable(2);
        PdfPTable table = new PdfPTable(1);

        table1.setWidthPercentage(50);
        table1.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table1.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        Image myImg=null;
        Bitmap bitmap=null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        table1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(finish.getBARCODE_NO(), BarcodeFormat.QR_CODE, 1080, 912);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            myImg = Image.getInstance(stream.toByteArray());
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM hh:mm aa");
        Date date1 = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date1);
        PdfPCell reportWorking = new PdfPCell(getParaReportFormat(currentDate+"\n"+finish.getBARCODE_NO()+"\n"+finish.getBUYER_NAME()+"\n"+finish.getBATCH_NO()+"\n"+finish.getQNTY()+" Kg", 12));
        reportWorking.setBorderColor(BaseColor.WHITE);
        table1.addCell(myImg);
        table1.addCell(reportWorking);
        try {
            table.addCell(getParaReportFormat(finish.getFILE_NO()+" and "+finish.getINTERNAL_REF()+",R. Dia-"+finish.getWIDTH()+"R. GSM-"+finish.getGSM(), 12));
            table.addCell(getParaReportFormat(finish.getCOLOR_NAME(), 12));
            table.addCell(getParaReportFormat("Roll: "+finish.getROLL_NO()+",Lot "+finish.getYARN_LOT(), 12));
            table.addCell(getParaReportFormat(finish.getBODY_PART_NAME(), 12));
            table.addCell(getParaReportFormat(finish.getITEM_DESCRIPTION(), 12));

            document.add(table1);
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private Paragraph getParaReportFormat(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }


}
