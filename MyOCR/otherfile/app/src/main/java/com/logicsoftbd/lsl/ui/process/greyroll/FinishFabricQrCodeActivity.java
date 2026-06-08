package com.logicsoftbd.lsl.ui.process.greyroll;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.SpinnerModel;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.utils.CommonUtils;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@SuppressLint({ "HandlerLeak", "NewApi" })
public class FinishFabricQrCodeActivity extends BaseActivity implements ReceiveMvpView ,FinishFabricQrAdapter.Callback{
    private static final int REQUEST_WRITE_PERMISSION = 786;
    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;
    @Inject
    FinishFabricQrAdapter mAdapter;
    BixolonLabelPrinter mBixolonLabelPrinter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;
    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.et_batch_no)
    EditText et_batch_no;

    @BindView(R.id.et_barcode_no)
    EditText et_barcode_no;

    @BindView(R.id.btn_search)
    Button btn_search;

    @BindView(R.id.btn_print)
    Button btn_print;

    @BindView(R.id.checkbox)
    CheckBox checkbox;
    ArrayList<SpinnerModel> mArrayList = new ArrayList<>();
    ArrayAdapter<SpinnerModel> mArrayAdapter;

//    BixolonLabelPrinter mBixolonLabelPrinter;
    ArrayList<FinishFabricQrCodeResponses.ResultSet> finish=new ArrayList<>();
    ArrayList<FinishFabricQrCodeResponses.ResultSet> finishFor=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_qr_code);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
        invalidateOptionsMenu();
        requestPermission();
    }

    @Override
    protected void setUp() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mToolbar.setTitle("Finish Fabric QC Print");
        mArrayList = getList();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayList);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBixolonLabelPrinter = new BixolonLabelPrinter(this, mHandler, Looper.getMainLooper());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // checkbox.setChecked(true);
                if (checkbox.isChecked()){

                    mAdapter.add(true);

                    finish.addAll(finishFor);

                }
                else{
                    finish.removeAll(finishFor);
                    mAdapter.add(false);

                }

            }
        });

    }
    void showWifiDialog(Context context, final BixolonLabelPrinter printer) {
        AlertDialog dialog = null;
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_wifi, null);
            Spinner spinner =layout.findViewById(R.id.spinner_ip);
            spinner.setAdapter(mArrayAdapter);

            dialog = new AlertDialog.Builder(context).setView(layout).setTitle("Wi-Fi Connect")
                    .setPositiveButton("OK", (dialog1, which) -> {
// EditText editText = (EditText) layout.findViewById(R.id.editText1);
// String ip = editText.getText().toString();

                        String ip = spinner.getSelectedItem().toString();

                        EditText editText = (EditText) layout.findViewById(R.id.editText2);
                        int port = Integer.parseInt(editText.getText().toString());

                        printer.connect(ip, port, 5000);
                        connectPrinter();

                    }).create();
        }
        dialog.show();
    }
    private void readFile() {
        for(FinishFabricQrCodeResponses.ResultSet detailsPart:finish){
            File fileEvents = new File(FinishFabricQrCodeActivity.this.getFilesDir()+"/text/sample"+detailsPart.getBARCODE_NO());
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileEvents));
                String line;
                while ((line = br.readLine())!= null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) { }
            String result = text.toString();
            mBixolonLabelPrinter.executeDirectIo(result, false, 0);
            fileEvents.delete();
        }
        mBixolonLabelPrinter.disconnect();


    }
    private void connectPrinter() {

        for(FinishFabricQrCodeResponses.ResultSet detailsPart:finish){
            File file = new File(FinishFabricQrCodeActivity.this.getFilesDir(), "text");
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM hh:mm aa");
                Date date1 = new Date(System.currentTimeMillis());
                String currentDate = formatter.format(date1);


//                String s = "SW600\n" +
//                        "SL1230,0,G\n" +
//                        "SM20,20\n" +
//                        "SOB\n" +
//                        "\n" +
//
//                        "B215,05,Q,2,M,6,0,'"+detailsPart.getBARCODE_NO()+"' \n" +
//                        "\n" +
//                        "T160,05,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
//                        "T15,165,2,1,1,0,0,N,N,'"+"F-"+detailsPart.getFILE_NO()+",Ref-"+detailsPart.getINTERNAL_REF()+",R. Dia-"+detailsPart.getWIDTH()+",R. GSM-"+detailsPart.getGSM()+"'\n" +
//                        "T15,200,2,1,1,0,0,N,N,'"+detailsPart.getCOLOR_NAME()+", Roll: "+detailsPart.getROLL_NO()+",'\n" +
//                        "T15,235,2,1,1,0,0,N,N,'"+"YARN LOT "+detailsPart.getYARN_LOT()+"," +detailsPart.getBODY_PART_NAME()+"'\n" +
//                        "T15,270,2,1,1,0,0,N,N,'"+detailsPart.getITEM_DESCRIPTION()+"'\n" +
//                        "T160,40,2,1,1,0,0,N,N,'"+detailsPart.getBARCODE_NO()+"'\n" +
//                        "T360,35,4,1,1,0,0,N,N,'"+detailsPart.getBUYER_NAME()+"'\n" +
//                        "T160,75,2,1,1,0,0,N,N,'"+detailsPart.getBATCH_NO()+"'\n" +
//                        "T360,80,4,1,1,0,0,N,N,'"+detailsPart.getQNTY()+"Kg'\n" +
//                        "P1,1";

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
                        "T360,130,4,1,1,0,0,N,N,'"+detailsPart.getQNTY()+"Kg'\n" +
                        "P1,1";

//                String s5 = "SW600\n" +
//                        "SL1230,0,G\n" +
//                        "SM20,20\n" +
//                        "SOB\n" +
//                        "\n" +
//                        "B15,60,Q,2,M,6,0,'"+detailsPart.getBARCODE_NO()+"' \n" +
//                        "\n" +
//                        "T160,90,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
//                        "T15,250,2,1,1,0,0,N,N,'"+"F-"+detailsPart.getFILE_NO()+",Ref-"+detailsPart.getINTERNAL_REF()+", R.Roll: "+detailsPart.getROLL_NO()+" ,"+detailsPart.getYARN_LOT()+",'\n" +
//                        "T15,285,2,1,1,0,0,N,N,'"+"R.Dia-"+detailsPart.getWIDTH()+", R.GSM-"+detailsPart.getGSM()+"," +detailsPart.getBODY_PART_NAME()+",'\n" +
//                        "T15,320,2,1,1,0,0,N,N,'"+detailsPart.getCOLOR_NAME()+" ,"+detailsPart.getITEM_DESCRIPTION()+"'\n" +
//                        "T160,125,2,1,1,0,0,N,N,'"+detailsPart.getBARCODE_NO()+"'\n" +
//                        "T160,50,4,1,1,0,0,N,N,'"+detailsPart.getBUYER_NAME()+"'\n" +
//                        "T160,160,2,1,1,0,0,N,N,'"+detailsPart.getBATCH_NO()+"'\n" +
//                        "T360,150,4,1,1,0,0,N,N,'"+detailsPart.getQNTY()+"Kg'\n" +
//                        "P1,1";

//                String s4 = "SW600\n" +
//                        "SL1230,0,G\n" +
//                        "SM20,20\n" +
//                        "SOB\n" +
//                        "\n" +
//
//                        "B215,90,Q,2,M,6,0,'"+detailsPart.getBARCODE_NO()+"' \n" +
//                        "\n" +
//                        "T160,90,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
//                        "T15,250,2,1,1,0,0,N,N,'"+"F-"+detailsPart.getFILE_NO()+",Ref-"+detailsPart.getINTERNAL_REF()+", R.Roll: "+detailsPart.getROLL_NO()+"'\n" +
//                        "T15,285,2,1,1,0,0,N,N,'"+",R.Dia-"+detailsPart.getWIDTH()+", R.GSM-"+detailsPart.getGSM()+"," +detailsPart.getBODY_PART_NAME()+",'\n" +
//                        "T15,320,2,1,1,0,0,N,N,'"+detailsPart.getCOLOR_NAME()+" ,"+detailsPart.getYARN_LOT()+"'\n" +
//                        "T15,355,2,1,1,0,0,N,N,'"+detailsPart.getITEM_DESCRIPTION()+"'\n" +
//                        "T160,125,2,1,1,0,0,N,N,'"+detailsPart.getBARCODE_NO()+"'\n" +
//                        "T360,05,2,1,1,0,0,N,N,'"+detailsPart.getBUYER_NAME()+"'\n" +
//                        "T160,160,2,1,1,0,0,N,N,'"+detailsPart.getBATCH_NO()+"'\n" +
//                        "T360,90,4,1,1,0,0,N,N,'"+detailsPart.getQNTY()+"Kg'\n" +
//                        "P1,1";

//                String s3 = "SW600\n" +
//                        "SL1230,0,G\n" +
//                        "SM20,20\n" +
//                        "SOB\n" +
//                        "\n" +
//
//                        "T360,05,2,1,1,0,0,N,N,'"+detailsPart.getBUYER_NAME()+"'\n" +
//
//                        "B215,70,Q,2,M,6,0,'"+detailsPart.getBARCODE_NO()+"' \n" +
//                        "\n" +
//                        "T160,70,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
//                        "T15,230,2,1,1,0,0,N,N,'"+"F-"+detailsPart.getFILE_NO()+",Ref-"+detailsPart.getINTERNAL_REF()+", Roll: "+detailsPart.getROLL_NO()+"'\n" +
//                        "T15,265,2,1,1,0,0,N,N,'"+",Req. Dia-"+detailsPart.getWIDTH()+",Req. GSM-"+detailsPart.getGSM()+","+detailsPart.getBODY_PART_NAME()+",'\n" +
//                        "T15,300,2,1,1,0,0,N,N,'"+detailsPart.getCOLOR_NAME()+","+detailsPart.getYARN_LOT()+",'\n" +
//                        "T15,335,2,1,1,0,0,N,N,'"+detailsPart.getITEM_DESCRIPTION()+"'\n" +
//                        "T160,105,2,1,1,0,0,N,N,'"+detailsPart.getBARCODE_NO()+"'\n" +
//                        "T160,140,2,1,1,0,0,N,N,'"+detailsPart.getBATCH_NO()+"'\n" +
//                        "T360,100,4,1,1,0,0,N,N,'"+detailsPart.getQNTY()+"Kg'\n" +
//                        "P1,1";

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
                                Toast.makeText(FinishFabricQrCodeActivity.this, "Device is connected", Toast.LENGTH_SHORT).show();
                                readFile();
                                break;
                            case BixolonLabelPrinter.STATE_CONNECTING:
                                Toast.makeText(FinishFabricQrCodeActivity.this, "Device is connecting", Toast.LENGTH_SHORT).show();
                                break;
                            case BixolonLabelPrinter.STATE_NONE:
                                Toast.makeText(FinishFabricQrCodeActivity.this, "connect is failed or disconnected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                }
            } catch (Exception e) {
                Toast.makeText(FinishFabricQrCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onSuccess(String msg) {

    }
    @OnClick(R.id.btn_search)
    void onSearchClick () {

       String batch=et_batch_no.getText().toString();
       String barcode=et_barcode_no.getText().toString();

       if (!batch.equals("")&&!barcode.equals("")){
           finish.clear();
           checkbox.setVisibility(View.GONE);
           checkbox.setChecked(false);
           mPresenter.onNextClickFinishFabricTwoTypes(batch,barcode);
       }
       else if (!batch.equals("")){
           finish.clear();
           checkbox.setChecked(false);
           checkbox.setVisibility(View.GONE);
           mPresenter.onNextClickFinishFabricBatchNo(batch);
       }
       else if (!barcode.equals("")){
           checkbox.setChecked(false);
           finish.clear();
           checkbox.setVisibility(View.GONE);
           mPresenter.onNextClickFinishFabricBarCode(barcode);
       }
       else{
           Toast.makeText(this, "Please Fill data", Toast.LENGTH_SHORT).show();
       }
    }

    @OnClick(R.id.btn_print)
    void onPrintClick () {
        if (finish.size()>0){
//            Intent intent= new Intent(FinishFabricQrCodeActivity.this,FinishFabricQrCodePrintActivity.class);
//            startActivity(intent);

            showWifiDialog(FinishFabricQrCodeActivity.this,mBixolonLabelPrinter);
            Log.e("CommonUtils","CommonUtils"+new Gson().toJson(CommonUtils.sewingRequest));
            ///createPdfFile(Common.getAppPath(FinishFabricQrCodeActivity.this)+"test_pdf.pdf");
        }
        else{
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        Log.e("qrcode","responses"+new Gson().toJson(barcodeResponse));
        if (barcodeResponse.getData().size()>1){
            checkbox.setVisibility(View.VISIBLE);
        }
        else{
            checkbox.setVisibility(View.GONE);
        }
        mAdapter.addItems(barcodeResponse.getData());
        finishFor.addAll(barcodeResponse.getData());
        //mRecyclerView.setItemViewCacheSize(barcodeResponse.getData().size());
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
    public void onRepoEmptyViewRetryClick() {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemForwardClick(FinishFabricQrCodeResponses.ResultSet resultSet,String type) {
        if (type.equals("Add")){
            finish.add(resultSet);
        }
        else{
            finish.remove(resultSet);
        }

       // CommonUtils.sewingRequest.add(resultSet);
        Log.e("resultSet","resultSet"+new Gson().toJson(resultSet));
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
        Document document=new Document(pagesize,0,0,10,0);
        try {
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            //  document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Evan");
            document.addCreator("Khan");
            ;
            //  Chunk glue = new Chunk(new VerticalPositionMark());


            for(FinishFabricQrCodeResponses.ResultSet detailsPart:finish){
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
            //document.setMargins(-100,0,200,0);
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
        PrintDocumentAdapter printDocumentAdapter=new PDFDocumentAdapter(FinishFabricQrCodeActivity.this,Common.getAppPath(FinishFabricQrCodeActivity.this)+"test_pdf.pdf");
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

    private void addNewItem(Document document, FinishFabricQrCodeResponses.ResultSet finish, int alignCenter) throws com.google.zxing.WriterException {

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