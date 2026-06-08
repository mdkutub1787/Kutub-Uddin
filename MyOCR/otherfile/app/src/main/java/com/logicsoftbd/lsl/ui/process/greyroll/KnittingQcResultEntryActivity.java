package com.logicsoftbd.lsl.ui.process.greyroll;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.KnittingResponse;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.ResultEntryRequest;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.SpinnerModel;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.DatePickerFragment;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.AppConstants;
import com.logicsoftbd.lsl.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KnittingQcResultEntryActivity extends BaseActivity implements ReceiveMvpView, DefectListAdapter.Callback, ButtonListAdapter.Callback, DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_process_id";
    @BindView(R.id.linear)
    LinearLayout linear;

    @Inject
    DefectListAdapter mAdapter;
    @Inject
    ButtonListAdapter mButtonAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.rv_defect)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_button)
    RecyclerView mRecyclerViewButton;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_grade)
    EditText edit_grade;

    @BindView(R.id.edit_inspected_point)
    EditText edit_inspected_point;

    @BindView(R.id.edit_date)
    EditText edit_date;

    @BindView(R.id.edit_barcode)
    EditText edit_barcode;
    @BindView(R.id.edit_roll_weight)
    EditText edit_roll_weight;
    @BindView(R.id.edit_qc_name)
    EditText edit_qc_name;
    @BindView(R.id.edit_ac_roll_width)
    EditText edit_ac_roll_width;
    @BindView(R.id.edit_roll_length)
    EditText edit_roll_length;

    @BindView(R.id.edit_reject)
    EditText edit_reject;


    private DefectInchModel defectInchModels;
    private DefectListModel defectListModels;
    @BindView(R.id.spinner)
    Spinner spinner;
    private Date mSelectedDate;
    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;
    private KnittingResponse mBarcodeResponse;
    ArrayList<SpinnerModel> mArrayList = new ArrayList<>();
    ArrayAdapter<SpinnerModel> mArrayAdapter;
    String status = "";

    public static Intent getStartIntent(Context context, KnittingResponse process) {
        Intent intent = new Intent(context, KnittingQcResultEntryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knitting_qc_result_entry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppConstants.STATUS_CODE_FAILED="knitting";
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        mButtonAdapter.setCallback(this);
        setUp();
        invalidateOptionsMenu();
        mPresenter.getKnitDefectInch();
        mPresenter.getKnitDefectList();
        mPresenter.getFabricGradeList();
        //Log.e("range","range"+inRange(15,0,10));
    }

    List<ResultEntryRequest.DetailsPart> productBarcodes = new ArrayList<>();

    private boolean getData() {
        Log.e("sfs", "SDF" + linear.getChildCount());
        if (linear.getChildCount()> 0)
        {
            for (int j = 0; j < linear.getChildCount(); j++) {
                Log.e("ok", "ok");
                View cr = linear.getChildAt(j);

                EditText edit_hole = cr.findViewById(R.id.edit_hole);
                EditText edit_defect_found_inch = cr.findViewById(R.id.edit_defect_found_inch);
                EditText edit_penalty = cr.findViewById(R.id.edit_penalty);
                EditText edit_count = cr.findViewById(R.id.edit_count);
                Spinner spinner_count = cr.findViewById(R.id.spinner);
                int pos= spinner_count.getSelectedItemPosition()+1;
                if (edit_defect_found_inch.getText().toString().equals("")) {

                    return false;
                } else if (edit_penalty.getText().toString().equals("")) {

                    return false;
                } else {
                    ResultEntryRequest.DetailsPart detailsPart = new ResultEntryRequest.DetailsPart();
                    detailsPart.setDEFECT_COUNT(String.valueOf(pos));
                    detailsPart.setDEFECT_NAME(edit_hole.getText().toString());
                    detailsPart.setFOUND_IN_INCH(edit_defect_found_inch.getText().toString());

                    detailsPart.setPENALTY_POINT(edit_penalty.getText().toString());

                    for (DefectListModel.Result data : defectListModels.getData()) {
                        if (edit_hole.getText().toString().equals(data.getDEFECT_NAME())) {
                            detailsPart.setDEFECT_ID(data.getDEFECT_ID());
                        }
                    }
                    for (DefectInchModel.Result data : defectInchModels.getData()) {
                        if (edit_defect_found_inch.getText().toString().equals(data.getDEFECT_INCH_ID())) {
                            detailsPart.setFOUND_IN_INCH_POINT(data.getDEFECT_INCH_NAME());
                        }
                    }
                    productBarcodes.add(detailsPart);

                }

            }
            return true;
        }
        else{
            return false;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (status.equals("0")) {
                Toast.makeText(this, "Please Select roll Status", Toast.LENGTH_SHORT).show();
            } else {
                if (edit_qc_name.getText().toString().equals("")) {
                    Toast.makeText(this, "Enter QC Name", Toast.LENGTH_SHORT).show();
                } else if (edit_ac_roll_width.getText().toString().equals("")) {
                    Toast.makeText(this, "Enter AC. Roll Width", Toast.LENGTH_SHORT).show();
                } else {
                    if (getData()) {
                        ResultEntryRequest resultEntryRequest = new ResultEntryRequest();
                        ResultEntryRequest.Result result = new ResultEntryRequest.Result();
                        ResultEntryRequest.MasterPart masterPart = new ResultEntryRequest.MasterPart();
                        masterPart.setBARCODE_NO(mBarcodeResponse.getData().getBARCODE_NO());
                        masterPart.setCompanyId(mBarcodeResponse.getData().getCOMPANY_ID());
                        masterPart.setDTLS_ID(mBarcodeResponse.getData().getDTLS_ID());
                        masterPart.setPROD_ID(mBarcodeResponse.getData().getPROD_ID());
                        masterPart.setROLL_ID(mBarcodeResponse.getData().getROLL_ID());

                        masterPart.setROLL_NO(mBarcodeResponse.getData().getROLL_NO());
                        masterPart.setQNTY(mBarcodeResponse.getData().getQNTY());

                        if (edit_reject.getText().toString().equals("")) {
                            masterPart.setREJECT_QNTY("0");
                        } else {
                            masterPart.setREJECT_QNTY(edit_reject.getText().toString());
                        }

                        masterPart.setQC_DATE(edit_date.getText().toString());
                        masterPart.setQC_NAME(edit_qc_name.getText().toString());
                        masterPart.setAC_ROLL_WIDTH(edit_ac_roll_width.getText().toString());
                        masterPart.setROLL_WGT(edit_roll_weight.getText().toString());
                        masterPart.setROLL_STATUS(status);
                        masterPart.setROLL_LENGTH(edit_roll_length.getText().toString());

                        int l = 0;
                        for (int k = 0; k < linear.getChildCount(); k++) {
                            View cric1 = linear.getChildAt(k);
                            EditText edit_penaltys = cric1.findViewById(R.id.edit_penalty);
                            if (edit_penaltys.getText().toString().equals("")) {
                                l += 0;
                            } else {
                                l += Integer.parseInt(edit_penaltys.getText().toString());
                            }

                        }
                        double width = Double.parseDouble(mBarcodeResponse.getData().getWIDTH());
                        double inspect = ((l * 1) * 36 * 100) / ((weight * 1) * (width * 1));
                        double ins = Double.parseDouble(new DecimalFormat("##.####").format(inspect));
                        int grade = (int) ins;
                        String grade_for = "";
                        if (grade > 40) {
                            grade_for = "Reject";

                        } else if (grade < 40 && grade > 30) {
                            edit_grade.setText("C");
                            grade_for = "C";
                        } else if (grade < 30 && grade > 20) {
                            grade_for = "B";
                        } else if (grade < 20 && grade > 0) {
                            grade_for = "A";
                        }

                        masterPart.setFABRIC_GRADE(grade_for);
                        masterPart.setTOTAL_PANALTY(String.valueOf(l));
                        masterPart.setTOTAL_POINT(String.valueOf(ins));
                        result.setMasterPart(masterPart);
                        result.setDetailsPart(productBarcodes);
                        resultEntryRequest.setData(result);

                         mPresenter.onKnittingResultSetSave(resultEntryRequest);
                        Log.e("json", "json" + new Gson().toJson(resultEntryRequest));

                    } else {
                        Toast.makeText(this, "Please Enter Fabric Defect Data", Toast.LENGTH_SHORT).show();
                    }

                }
            }


            return true;
        } else if (id == R.id.action_new) {
//            startActivity(new Intent(KnittingQcResultEntryActivity.this, MainActivity.class));
            startActivity(new Intent(KnittingQcResultEntryActivity.this, V1_MenuActivity.class));
            finish();

        }
        return super.onOptionsItemSelected(item);

    }

    boolean inRange(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }

    private ArrayList<SpinnerModel> getList() {

        ArrayList<SpinnerModel> SpinnerModelArrayList = new ArrayList<>();
        SpinnerModel SpinnerModelIslam5 = new SpinnerModel("Select", 0);
        SpinnerModel SpinnerModelIslam4 = new SpinnerModel("QC Pass", 1);
        SpinnerModel SpinnerModelIslam = new SpinnerModel("Help Up", 2);
        SpinnerModel SpinnerModelHindu = new SpinnerModel("Reject", 3);
        SpinnerModelArrayList.add(SpinnerModelIslam5);
        SpinnerModelArrayList.add(SpinnerModelIslam4);
        SpinnerModelArrayList.add(SpinnerModelIslam);
        SpinnerModelArrayList.add(SpinnerModelHindu);
        return SpinnerModelArrayList;

    }
    private ArrayList<SpinnerModel> getSpinnerList() {

        ArrayList<SpinnerModel> SpinnerModelArrayList = new ArrayList<>();
        SpinnerModel SpinnerModelIslam5 = new SpinnerModel("1", 1);
        SpinnerModel SpinnerModelIslam4 = new SpinnerModel("2", 2);
        SpinnerModel SpinnerModelIslam = new SpinnerModel("3", 3);
        SpinnerModel SpinnerModelHindu = new SpinnerModel("4", 4);
        SpinnerModel SpinnerModelHindu44 = new SpinnerModel("5", 5);
        SpinnerModel SpinnerModelHindu343 = new SpinnerModel("6", 6);
        SpinnerModel SpinnerModelHindu56 = new SpinnerModel("7", 7);
        SpinnerModel SpinnerModelHindu65 = new SpinnerModel("8", 8);
        SpinnerModel SpinnerModelHindu652 = new SpinnerModel("9", 9);
        SpinnerModel SpinnerModelHindu6435 = new SpinnerModel("10", 10);
        SpinnerModel SpinnerModelHindu6455 = new SpinnerModel("11", 11);
        SpinnerModel SpinnerModelHindu6452 = new SpinnerModel("12", 12);
        SpinnerModel SpinnerModelHindu1645 = new SpinnerModel("13", 13);
        SpinnerModel SpinnerModelHindu8645 = new SpinnerModel("14", 14);
        SpinnerModel SpinnerModelHindu6405 = new SpinnerModel("15", 15);
        SpinnerModel SpinnerModelHindu64235 = new SpinnerModel("16", 16);
        SpinnerModel SpinnerModelHindu6425 = new SpinnerModel("17", 17);
        SpinnerModel SpinnerModelHindu6ee425 = new SpinnerModel("18", 18);
        SpinnerModel SpinnerModelHindu600425 = new SpinnerModel("19", 19);
        SpinnerModel SpinnerModelHindu64252 = new SpinnerModel("20", 20);
        SpinnerModelArrayList.add(SpinnerModelIslam5);
        SpinnerModelArrayList.add(SpinnerModelIslam4);
        SpinnerModelArrayList.add(SpinnerModelIslam);
        SpinnerModelArrayList.add(SpinnerModelHindu);
        SpinnerModelArrayList.add(SpinnerModelHindu44);
        SpinnerModelArrayList.add(SpinnerModelHindu343);
        SpinnerModelArrayList.add(SpinnerModelHindu56);
        SpinnerModelArrayList.add(SpinnerModelHindu65);
        SpinnerModelArrayList.add(SpinnerModelHindu652);
        SpinnerModelArrayList.add(SpinnerModelHindu6435);
        SpinnerModelArrayList.add(SpinnerModelHindu6455);
        SpinnerModelArrayList.add(SpinnerModelHindu6452);
        SpinnerModelArrayList.add(SpinnerModelHindu1645);
        SpinnerModelArrayList.add(SpinnerModelHindu8645);
        SpinnerModelArrayList.add(SpinnerModelHindu6405);
        SpinnerModelArrayList.add(SpinnerModelHindu64235);
        SpinnerModelArrayList.add(SpinnerModelHindu6425);
        SpinnerModelArrayList.add(SpinnerModelHindu6ee425);
        SpinnerModelArrayList.add(SpinnerModelHindu600425);
        SpinnerModelArrayList.add(SpinnerModelHindu64252);
        return SpinnerModelArrayList;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    double weight = 0;

    @Override
    protected void setUp() {
        mArrayList = getList();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayList);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mArrayAdapter);
        mBarcodeResponse = (KnittingResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        try {
            edit_barcode.setText(mBarcodeResponse.getData().getBARCODE_NO());
            edit_roll_weight.setText(mBarcodeResponse.getData().getQNTY());
        } catch (Exception e) {
            e.printStackTrace();
        }


        edit_ac_roll_width.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    double roleWeight = Double.parseDouble(edit_roll_weight.getText().toString());
                    double gsm = Double.parseDouble(mBarcodeResponse.getData().getGSM());
                    double width = Double.parseDouble(s.toString());
                    double d = ((roleWeight * 1000) / (gsm * width * 0.0254)) * 1.09361;
                    Log.e("roleWeight", "dta" + roleWeight);
                    Log.e("gsm", "gsm" + gsm);
                    Log.e("width", "width" + width);
                    Log.e("d", "d" + d);

                    weight = Double.parseDouble(new DecimalFormat("##.####").format(d));
                    edit_roll_length.setText(String.valueOf(weight));
                } else {
                    edit_roll_length.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mToolbar.setTitle("Knitting QC Result Entry");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 8));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewButton.setLayoutManager(mLayoutManager);
        mRecyclerViewButton.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewButton.setAdapter(mButtonAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_water", "" + mArrayList.get(position).getId());
                status = String.valueOf(mArrayList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.edit_date)
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedDate = DateUtils.getToday();
        edit_date.setText(DateUtils.formatDate(mSelectedDate));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mSelectedDate = DateUtils.createDate(year, monthOfYear, dayOfMonth);
        edit_date.setText(DateUtils.formatDate(mSelectedDate));
    }

    void onFab2Click() {//
        color = position - 1;
        View cric = linear.getChildAt(position - 1);
        EditText editDefectFoundInch = cric.findViewById(R.id.edit_defect_found_inch);
        LinearLayout linearLayout = cric.findViewById(R.id.linear);
        editDefectFoundInch.setText("1");
        linearLayout.setBackgroundColor(getResources().getColor(R.color.black));

        Toast.makeText(this, String.valueOf(color), Toast.LENGTH_SHORT).show();
//        View cric1= linear.getChildAt(color-1);
//        LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
//        linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
        for (int i = 0; i < linear.getChildCount(); i++) {

            if (color != i) {
                View cric1 = linear.getChildAt(i);
                LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
            }


//            View cricketerView = linear.getChildAt(i);
//
//           EditText editTextName = cricketerView.findViewById(R.id.edit_hole);
//          // EditText editDefectFoundInch = cricketerView.findViewById(R.id.edit_defect_found_inch);
//            Toast.makeText(this, editTextName.getText().toString()+i, Toast.LENGTH_SHORT).show();
////            AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);
//
//            Cricketer cricketer = new Cricketer();
//
//            if(!editTextName.getText().toString().equals("")){
//                cricketer.setCricketerName(editTextName.getText().toString());
//            }else {
//                result = false;
//                break;
//            }
//
//            if(spinnerTeam.getSelectedItemPosition()!=0){
//                cricketer.setTeamName(teamList.get(spinnerTeam.getSelectedItemPosition()));
//            }else {
//                result = false;
//                break;
//            }
//
//            cricketersList.add(cricketer);

        }
    }

    int test = 0;
    int position = 1;
    int color = 0;
    String text = "52";

    public void onAddField(String name, boolean current) {
        ArrayList<SpinnerModel> mArrayLists = new ArrayList<>();
        ArrayAdapter<SpinnerModel> mArrayAdapters;
        mArrayLists = getSpinnerList();
        mArrayAdapters = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayLists);
        mArrayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.info_layout, null, false);
        final EditText editTextName = rowView.findViewById(R.id.edit_hole);
        final EditText edit_count = rowView.findViewById(R.id.edit_count);
        final Spinner spinners = rowView.findViewById(R.id.spinner);
        final EditText edit_sl = rowView.findViewById(R.id.edit_sl);
        final EditText edit_penalty = rowView.findViewById(R.id.edit_penalty);
        final EditText edit_defect_found_inch = rowView.findViewById(R.id.edit_defect_found_inch);
        edit_count.setVisibility(View.GONE);
        spinners.setVisibility(View.VISIBLE);
        spinners.setAdapter(mArrayAdapters);
        ArrayList<SpinnerModel> finalMArrayLists = mArrayLists;
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_water", "" + finalMArrayLists.get(position).getId());
              //  status = String.valueOf(finalMArrayLists.get(position).getId());
                if (!edit_defect_found_inch.getText().toString().equals("")) {
                    int i = 0;
                    int j = 0;
                    int total = 0;
                    i = finalMArrayLists.get(position).getId();
                    j = Integer.parseInt(edit_defect_found_inch.getText().toString());
                    total = i * j;
                    edit_penalty.setText(String.valueOf(total));
                    int l = 0;
                    for (int k = 0; k < linear.getChildCount(); k++) {
                        View cric1 = linear.getChildAt(k);
                        EditText edit_penalty = cric1.findViewById(R.id.edit_penalty);
                        if (edit_penalty.getText().toString().equals("")) {
                            l += 0;
                        } else {
                            l += Integer.parseInt(edit_penalty.getText().toString());
                        }

                    }
                    Log.e("data", "ddata" + l);
//                            double leg=(2*1000)/((2*5*2)*1);
                    double width = Double.parseDouble(mBarcodeResponse.getData().getWIDTH());
                    double inspect = ((l * 1) * 36 * 100) / ((weight * 1) * (width * 1));
                    double ins = Double.parseDouble(new DecimalFormat("##.####").format(inspect));

                    int grade = (int) ins;
                    if (grade > 40) {
                        edit_grade.setText("Reject");
                    } else if (grade < 40 && grade > 30) {
                        edit_grade.setText("C");
                    } else if (grade < 30 && grade > 20) {
                        edit_grade.setText("B");
                    } else if (grade < 20 && grade > 0) {
                        edit_grade.setText("A");
                    }
                    edit_inspected_point.setText(String.valueOf(ins));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (current)
        {
            test = test + 1;
            String value = String.valueOf(test);
//            for (int j = 0; j < linear.getChildCount(); j++) {
//                View cr = linear.getChildAt(j);
//
//                EditText edit_slw = cr.findViewById(R.id.edit_sl);
//
//                int l=j+1;
//                edit_slw.setText(String.valueOf(l));
//            }
            edit_sl.setText(value);
            editTextName.setText(name);
            edit_penalty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });
            edit_defect_found_inch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });
            edit_sl.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });
            editTextName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });
            edit_count.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });
            spinners.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                    return false;
                }
            });

            edit_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        if (!edit_defect_found_inch.getText().toString().equals("")) {
                            int i = 0;
                            int j = 0;
                            int total = 0;
                            i = Integer.parseInt(s.toString());
                            j = Integer.parseInt(edit_defect_found_inch.getText().toString());
                            total = i * j;
                            edit_penalty.setText(String.valueOf(total));
                            int l = 0;
                            for (int k = 0; k < linear.getChildCount(); k++) {
                                View cric1 = linear.getChildAt(k);
                                EditText edit_penalty = cric1.findViewById(R.id.edit_penalty);
                                if (edit_penalty.getText().toString().equals("")) {
                                    l += 0;
                                } else {
                                    l += Integer.parseInt(edit_penalty.getText().toString());
                                }

                            }
                            Log.e("data", "ddata" + l);
//                            double leg=(2*1000)/((2*5*2)*1);
                            double width = Double.parseDouble(mBarcodeResponse.getData().getWIDTH());
                            double inspect = ((l * 1) * 36 * 100) / ((weight * 1) * (width * 1));
                            double ins = Double.parseDouble(new DecimalFormat("##.####").format(inspect));

                            int grade = (int) ins;
                            if (grade > 40) {
                                edit_grade.setText("Reject");
                            } else if (grade < 40 && grade > 30) {
                                edit_grade.setText("C");
                            } else if (grade < 30 && grade > 20) {
                                edit_grade.setText("B");
                            } else if (grade < 20 && grade > 0) {
                                edit_grade.setText("A");
                            }
                            edit_inspected_point.setText(String.valueOf(ins));


                        }
                    } else {
                        edit_penalty.setText("");

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                }
            });
            edit_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                }
            });
            edit_defect_found_inch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                }
            });
            edit_sl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                }
            });
            edit_penalty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = Integer.parseInt(edit_sl.getText().toString());
                    color = position - 1;
                    View cric = linear.getChildAt(position - 1);
                    LinearLayout linearLayout = cric.findViewById(R.id.linear);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.click));
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        if (color != i) {
                            View cric1 = linear.getChildAt(i);
                            LinearLayout linearLayout1 = cric1.findViewById(R.id.linear);
                            linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }

                }
            });
            linear.addView(rowView);
        } else {

            for (int i = 0; i < linear.getChildCount(); i++) {
                View cric1 = linear.getChildAt(i);
                EditText edit_hole = cric1.findViewById(R.id.edit_hole);

                if (edit_hole.getText().toString().equals(name)) {
                    linear.removeView(cric1);
                    test = test - 1;
                    int l = 0;
                    for (int k = 0; k < linear.getChildCount(); k++) {
                        View cr = linear.getChildAt(k);
                        EditText edit_penaltys = cr.findViewById(R.id.edit_penalty);
                        if (edit_penaltys.getText().toString().equals("")) {
                            l += 0;
                        } else {
                            l += Integer.parseInt(edit_penaltys.getText().toString());
                        }

                    }
                    double leg = (2 * 1000) / ((2 * 5 * 2) * 1);
                    double width = Double.parseDouble(mBarcodeResponse.getData().getWIDTH());
                    double inspect = ((l * 1) * 36 * 100) / ((weight * 1) * (width * 1));
                    double ins = Double.parseDouble(new DecimalFormat("##.####").format(inspect));

                    int grade = (int) ins;
                    if (grade > 40) {
                        edit_grade.setText("Reject");
                    } else if (grade < 40 && grade > 30) {
                        edit_grade.setText("C");
                    } else if (grade < 30 && grade > 20) {
                        edit_grade.setText("B");
                    } else if (grade < 20 && grade > 0) {
                        edit_grade.setText("A");
                    }
                    edit_inspected_point.setText(String.valueOf(ins));
                    for (int j = 0; j < linear.getChildCount(); j++) {
                        View cr = linear.getChildAt(j);

                        EditText edit_slw = cr.findViewById(R.id.edit_sl);

                        int l1 = j + 1;
                        edit_slw.setText(String.valueOf(l1));
                    }
                }
            }
        }


    }


    @Override
    public void onSuccess(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {
                //  finish();
                startActivity(ScannerActivity.getStartIntent(KnittingQcResultEntryActivity.this, new Process(R.drawable.process, "Finish Fabric Production Result Entry", "result",
                        new Process.DataParam("result", "fabric"))));

                finish();
            }
        }, msg);

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
        Log.e("defectInchModel", "defectInchModel" + new Gson().toJson(defectInchModel));
        mButtonAdapter.addItems(defectInchModel.getData());
        defectInchModels = defectInchModel;
    }

    @Override
    public void issuePurpose(IssuePurposeModel issuePurposeModel) {

    }

    @Override
    public void issueStore(IssueStoreModel issueStoreModel) {

    }

    @Override
    public void defectList(DefectListModel defectListModel) {
        Log.e("defectListModel", "defectListModel" + new Gson().toJson(defectListModel));
        mAdapter.addItems(defectListModel.getData());
        defectListModels = defectListModel;
    }

    @Override
    public void fabricGrade(FabricGradeModel fabricGradeModel) {
        Log.e("fabricGradeModel", "fabricGradeModel" + new Gson().toJson(fabricGradeModel));
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
    public void onItemForwardClick(int position, String name, boolean current) {
        Log.e("Result", "Set" + name + " , " + current);
        if (!edit_ac_roll_width.getText().toString().equals("")) {
            int data = Integer.parseInt(edit_ac_roll_width.getText().toString());
            if (data > 0) {
                onAddField(name, true);
            }

        }

        if (linear.getChildCount() == 0) {
            edit_inspected_point.setText("");
            edit_grade.setText("");
        }
    }

    @Override
    public void onItemClick(String name) {

        Log.e("count", "count" + linear.getChildCount());

        if (linear.getChildCount() > 0) {
            View cric = linear.getChildAt(position - 1);
            EditText editDefectFoundInch = cric.findViewById(R.id.edit_defect_found_inch);
            EditText edit_penalty = cric.findViewById(R.id.edit_penalty);
            EditText edit_count = cric.findViewById(R.id.edit_count);
            Spinner spinner_count = cric.findViewById(R.id.spinner);
            editDefectFoundInch.setText(name);
            if (editDefectFoundInch.getText().toString().length() > 0) {
                if (!editDefectFoundInch.getText().toString().equals("")) {
                    int i = 0;
                    int j = 0;
                    int total = 0;
                    i = spinner_count.getSelectedItemPosition()+1;

                    j = Integer.parseInt(editDefectFoundInch.getText().toString());
                    total = i * j;
                    if (total == 0) {
                        edit_penalty.setText("");
                    } else {
                        edit_penalty.setText(String.valueOf(total));
                    }

                    int l = 0;
                    for (int k = 0; k < linear.getChildCount(); k++) {
                        View cric1 = linear.getChildAt(k);
                        EditText edit_penaltys = cric1.findViewById(R.id.edit_penalty);
                        if (edit_penaltys.getText().toString().equals("")) {
                            l += 0;
                        } else {
                            l += Integer.parseInt(edit_penaltys.getText().toString());
                        }

                    }
                    Log.e("data", "ddata" + l);
//                            double leg=(2*1000)/((2*5*2)*1);
                    double width = Double.parseDouble(mBarcodeResponse.getData().getWIDTH());
                    double inspect = ((l * 1) * 36 * 100) / ((weight * 1) * (width * 1));
                    double ins = Double.parseDouble(new DecimalFormat("##.####").format(inspect));

                    int grade = (int) ins;
                    if (grade > 40) {
                        edit_grade.setText("Reject");
                    } else if (grade < 40 && grade > 30) {
                        edit_grade.setText("C");
                    } else if (grade < 30 && grade > 20) {
                        edit_grade.setText("B");
                    } else if (grade < 20 && grade > -1) {
                        edit_grade.setText("A");
                    }
                    edit_inspected_point.setText(String.valueOf(ins));


                }
            } else {
                edit_penalty.setText("");

            }
        }

    }
}