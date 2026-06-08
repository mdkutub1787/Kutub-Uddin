package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ColorBreakDownResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigSewingOperationModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectListOfRectifiedModel_v5;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectedRectifiedModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GrossSewingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingRejectModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V2_ColorWisePOModel;
import com.logicsoftbd.lsl.data.network.v1_model.V2_POColorWiseSizeModel;
import com.logicsoftbd.lsl.data.network.v1_model.V2_POWiseColorModel;
import com.logicsoftbd.lsl.data.network.v1_model.V2_TypeWiseInOutModel;
import com.logicsoftbd.lsl.data.network.v1_model.V2_TypeWiseInOutResponseModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_StyleWiseSewingActivity_v5 extends AppCompatActivity implements View.OnClickListener,
        V1_SewingOutputAlterDefectRecyclerAdapter.OnAlterDefectSelectListener,
        V1_SewingOutputSpotDefectRecyclerAdapter.OnSpotDefectSelectListener,
        V1_SewingOutputRejectDefectRecyclerAdapter.OnRejectDefectSelectListener,
        V1_StyleWiseSewingOperationRecyclerAdapter.OnOperationSelectListener,
        V1_StyleWiseSewingColorRecyclerAdapter.OnColorSelectListener,
        V2_StyleWiseSewingGoodQualitySizeRecyclerAdapter.OnSizeSelectListener,
        V2_StyleWiseSewingSizeRecyclerAdapter.OnSizeSelectListener,
        V1_DefectedRectifiedRecyclerViewAdapter.OnRectifiedHeadListener{
    private static final String TAG = "V1_StyleWiseSewingActiv";
    private ProgressDialog pDialog;
    private RecyclerView operationRecyclerView, defectRecyclerView, colorRecyclerView, sizeRecyclerView, defectRectifiedRecyclerView;
    private SharedPreferences _preferences;

    private List<V1_StyleWiseOperationResponse.Color> colorArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.Size> sizeArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.PoId> poIdArrayList = new ArrayList<>();
    private List<V2_POWiseColorModel> poWiseColorArrayList = new ArrayList<>();
    private List<V2_ColorWisePOModel> colorWisePOModels = new ArrayList<>();
    private List<V2_POColorWiseSizeModel> poColorWiseSizeArrayList = new ArrayList<>();
    private List<V2_TypeWiseInOutModel> typeWiseInOutModelsArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.Operation> operationArrayList = new ArrayList<>();
    public String[] colorName, colorId;

    private static ArrayList<V1_SewingAlterModel> sewingAlterModels = new ArrayList<>();
    private static ArrayList<V1_SewingSpotModel> sewingSpotModels = new ArrayList<>();
    private static ArrayList<V1_SewingRejectModel> sewingRejectModels = new ArrayList<>();
    public static ArrayList<V1_DefectedRectifiedModel> defectedRectifiedModels = new ArrayList<>();
    public static ArrayList<V1_DefectedRectifiedModel> uniqueDefectedRectifiedModels = new ArrayList<>();

    public static ArrayList<V1_SewingOutputModelClass> modelArrayList;
    private V1_SewingOutputModelClass inputModelClass;

    private V1_StyleWiseSewingOperationRecyclerAdapter sewingOutputOperationRecyclerAdapter;
    private V1_SewingOutputAlterDefectRecyclerAdapter sewingOutputAlterDefectRecyclerAdapter;
    private V1_SewingOutputSpotDefectRecyclerAdapter sewingOutputSpotDefectRecyclerAdapter;
    private V1_SewingOutputRejectDefectRecyclerAdapter sewingOutputRejectDefectRecyclerAdapter;
    private V1_DefectedRectifiedRecyclerViewAdapter defectedRectifiedRecyclerViewAdapter;
    private V1_StyleWiseSewingColorRecyclerAdapter sewingColorRecyclerAdapter;
    private V2_StyleWiseSewingSizeRecyclerAdapter sewingSizeRecyclerAdapter;
    private V2_StyleWiseSewingGoodQualitySizeRecyclerAdapter sewingGoodQualitySizeRecyclerAdapter;
    private ArrayAdapter<String> adapterColor;
    private V3_ColorSpinnerAdapter colorSpinnerAdapter;
    private V2_POSpinnerAdapter POSpinnerAdapter;
    private List<V1_ConfigSewingOperationModel> operationItemModelList = new ArrayList<>();
    private Set<String> idsSeen = new HashSet<>();
    private List<V1_StyleWiseOperationResponse.Color> uniqueColorList = new ArrayList<>();
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Button backButton, nextButton, _operationButton, _saveBT;
    private ImageView _imgBack, _undoButton;
    private TextView _lineTV, _styleTV, _internalRefTV, _itemTV, _countryTV, _inputQtyTV, _outputQtyTv, _checkQtyTV, _colorWiseOutputTV, _buyerInfoTV, _jobInfoTV;
    private RadioGroup _radioGroup;
    private RadioButton _goodRB, _rejectRB, _alterRB, _spotRB, _rectifiedRB;
    private CheckBox _rectifiedSelectCheckbox;
    private TextView _goodTV, _rejectTV, _alterTV, _spotTV, _rectifiedTV, _operationNameTV, _itemCountTV;
    private RelativeLayout _imageLayout;
    private CardView operationListCard, defectListCard, rectifiedListCard;
    private int Resource, qty = 0, qc_qty = 0, reject_number = 0, alter_number = 0, spot_number = 0, good_number = 0, rectified_number = 0,
            locationId = 0, floorId = 0, lineId = 0, isOrganic = 0, updatedID = 0, rescan = 0, color_type_id = 0, sewing_input_line = 0,
            companyId = 0, shiftId = 0, sewingcompanyId = 0, sizeSelection = -1, savedSizeSelection = -1, colorSelection = -1, poWiseColorSelection = -1, inputQnty = 0, outputQty = 0;
    private EditText reject, alter, spot;
    private String reject_defect = "";
    private String jobId = "";
    private String alter_defect = "";
    private String spot_defect= "";
    private String currentDate="", selectedColorID = "";
    private String saved_color_wise_check_qty="0", savedColorSizeBreakDownId="";
    private String dtls_id="";
    private String coloSizeBreakDownId="";
    private String userId;
    private String date;
    private String currentTime;
    private String base_url = "", macAddress;
    private String defectType = "G";
    private String action_type = "GOOD";
    private String operationJson = "", UPDATE_ID, UPDATE_DTLS_ID, UPDATE_DTLS_PIECE_ID, DELETE_DATA_TYPE, undo_type = "";
    private String po_breakDownId, colorWisePo_breakDownId, item_numberId, countryId = "", selected_operation_id="0";
    private Integer selectedColor = 0;
    private Spinner colorSpinner, poSpinner;
    private boolean undoStatus = false, allSelectPosition = false;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleTimeFormat;
    private LinearLayout _operationLayout, _defectLayout, _sizeLayout, _rectifiedLinearLayout, _errorRectifiedDataSetLayout, _operationLinearLayout;
    private LinearLayoutManager linearLayoutManager, linearLayoutManagerDefect, linearLayoutManagerOperation;
    private VerticalSpacingItemDecorator itemDecorator, itemDecoratorDefect;
    private ImageView _logoImg;
    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_style_wise_sewing_v5);
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        //set Time
        simpleTimeFormat = new SimpleDateFormat("HH-mm");
        currentTime = simpleTimeFormat.format(calendar.getTime());

        init_ui();
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        operationRecyclerView = findViewById(R.id.operationRecyclerView);
        defectRecyclerView = findViewById(R.id.defectRecyclerView);
        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        sizeRecyclerView = findViewById(R.id.sizeRecyclerView);
        defectRectifiedRecyclerView = findViewById(R.id.defectRectifiedRecyclerView);
        _operationLayout = findViewById(R.id.operationLayout);
        _defectLayout = findViewById(R.id.defectLayout);
        _sizeLayout = findViewById(R.id.sizeLayout);
        _errorRectifiedDataSetLayout = findViewById(R.id.errorRectifiedDataSetLayout);
        _operationLinearLayout = findViewById(R.id.operationLinearLayout);
        _rectifiedLinearLayout = findViewById(R.id.rectifiedLinearLayout);
        _operationButton = findViewById(R.id.operationButton);
        _operationButton.setOnClickListener(this);
        _lineTV = findViewById(R.id.lineTV);
        _styleTV = findViewById(R.id.styleTV);
        _internalRefTV = findViewById(R.id.internalRefTV);
        _itemTV = findViewById(R.id.itemTV);
        _inputQtyTV = findViewById(R.id.inputQtyTV);
        _outputQtyTv = findViewById(R.id.outputQtyTV);
        _checkQtyTV = findViewById(R.id.checkQtyTV);
        _colorWiseOutputTV = findViewById(R.id.colorWiseOutputTV);
        _buyerInfoTV = findViewById(R.id.buyerInfoTV);
        _jobInfoTV = findViewById(R.id.jobInfoTV);
//        _countryTV = findViewById(R.id.countryTV);

        _imageLayout = findViewById(R.id.imageLayout);

        reject = findViewById(R.id.rejectET);
        alter = findViewById(R.id.alterET);
        spot = findViewById(R.id.spotET);

        _rejectRB = findViewById(R.id.rejectRB);
        _alterRB = findViewById(R.id.alterRB);
        _spotRB = findViewById(R.id.spotRB);
        _rectifiedRB = findViewById(R.id.rectifiedRB);
        _rectifiedRB = findViewById(R.id.rectifiedRB);
        _goodRB = findViewById(R.id.goodRB);
        _rectifiedSelectCheckbox = findViewById(R.id.rectifiedSelectCheckbox);

        _rejectTV = findViewById(R.id.rejectTV);
        _alterTV = findViewById(R.id.alterTV);
        _spotTV = findViewById(R.id.spotTV);
        _rectifiedTV = findViewById(R.id.rectifiedTV);
        _goodTV = findViewById(R.id.goodTV);
        _operationNameTV = findViewById(R.id.operationNameTV);
        _itemCountTV = findViewById(R.id.itemCountTV);
        setTextViewGradientColor(_itemCountTV);

        operationListCard = findViewById(R.id.operationListCard);
        defectListCard = findViewById(R.id.defectListCard);
        rectifiedListCard = findViewById(R.id.rectifiedListCard);
        colorSpinner = findViewById(R.id.colorSpinner);
        poSpinner = findViewById(R.id.POSpinner);

        _goodRB.setChecked(true);
        good_number = 1;

        _imgBack = findViewById(R.id.back);
        _imgBack.setOnClickListener(this);
        _undoButton = findViewById(R.id.undoButton);
        _undoButton.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);

        _logoImg = findViewById(R.id.logoImg);
        imageArray = new int[2];
        imageArray[0] = R.drawable.logic;
        imageArray[1] = R.drawable.logic;
//        imageArray[1] = R.drawable.micro_fiber;
//        imageArray[1] = R.drawable.evince;
//        imageArray[1] = R.drawable.logo_jk;
//        imageArray[1] = R.drawable.kac;
//        imageArray[1] = R.drawable.asrotex;
//        imageArray[1] = R.drawable.hams;

        startIndex = 0;
        endIndex = 1;
        nextImage();

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        jobId = (_preferences.getString("jobId", ""));
        macAddress = _preferences.getString("mac", "");
        userId = _preferences.getString("login_userid", "");
        operationJson = _preferences.getString("operationJson", "");

        _lineTV.setText(_preferences.getString("lineName", "").toUpperCase());
        setTextViewGradientColor(_lineTV);
        _styleTV.setText(_preferences.getString("style", "").toUpperCase());
        setTextViewGradientColor(_styleTV);
        _internalRefTV.setText(_preferences.getString("irNumber", "").toUpperCase());
        setTextViewGradientColor(_internalRefTV);
        _itemTV.setText(_preferences.getString("itemName", "").toUpperCase());
        setTextViewGradientColor(_itemTV);
        _buyerInfoTV.setText("BUYER NAME: "+_preferences.getString("buyerName", "").toUpperCase());
        setTextViewGradientColor(_buyerInfoTV);
        _jobInfoTV.setText("JOB NO: "+_preferences.getString("jobNo", "").toUpperCase());
        setTextViewGradientColor(_jobInfoTV);
//        po_breakDownId = _preferences.getString("poBreakDownId", "");
        item_numberId = _preferences.getString("itemNumber", "");
        countryId = _preferences.getString("country", "");
//        _countryTV.setText(_preferences.getString("countryName", ""));

        modelArrayList = new ArrayList<>();
        inputModelClass = new V1_SewingOutputModelClass();
        inputModelClass.setCountry_id(_preferences.getString("country", ""));
//        inputModelClass.setOrder_id(Integer.parseInt(_preferences.getString("poBreakDownId", "")));
        Log.d("TAG", "init_ui: "+_preferences.getString("itemNumber", ""));
        inputModelClass.setItem_id(_preferences.getString("itemNumber", ""));
        modelArrayList.add(inputModelClass);

        if(!operationJson.equals("")){
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            operationItemModelList = Arrays.asList(gson.fromJson(operationJson, V1_ConfigSewingOperationModel[].class));
        }

        requestStyleWiseOperation();
        operationPopup();

        if(undoStatus == false){
            _undoButton.setEnabled(false);
        }

        _rectifiedSelectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                _rectifiedSelectCheckbox.setText("UNSELECT ALL");
                for(int i=0; i<defectedRectifiedModels.size(); i++){
                    defectedRectifiedModels.get(i).setSelect(true);
                }
                allSelectPosition = false;
            }else{
                if(!allSelectPosition){
                    _rectifiedSelectCheckbox.setText("SELECT ALL");
                    for(int i=0; i<defectedRectifiedModels.size(); i++){
                        defectedRectifiedModels.get(i).setSelect(false);
                    }
                }
            }
            defectedRectifiedRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    public void nextImage(){
        _logoImg.setImageResource(imageArray[currentIndex]);
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        _logoImg.startAnimation(rotateimage);
        currentIndex++;
        new Handler().postDelayed(() -> {
            if(currentIndex>endIndex){
                currentIndex--;
                previousImage();
            }else{
                nextImage();
            }

        },2000);

    }

    public void previousImage(){
        _logoImg.setImageResource(imageArray[currentIndex]);
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        _logoImg.startAnimation(rotateimage);
        currentIndex--;
        new Handler().postDelayed(() -> {
            if(currentIndex<startIndex){
                currentIndex++;
                nextImage();
            }else{
                previousImage();
            }
        },2000);

    }

    private void setTextViewGradientColor(TextView textView) {
        Shader shader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                new int[]{Color.BLUE,  Color.parseColor("#810366")},
                null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        textView.getPaint().setShader(shader);
    }

    private void requestStyleWiseOperation() {
        showDialog();
        apiInterface.getStyleWiseWithoutPOOperationCall(_preferences.getString("jobNo", ""),
                        _preferences.getString("itemNumber", ""), _preferences.getString("style", ""),
                        String.valueOf(lineId), userId)
                .enqueue(new Callback<V1_StyleWiseOperationResponse>() {
                    @Override
                    public void onResponse(Call<V1_StyleWiseOperationResponse> call, Response<V1_StyleWiseOperationResponse> response) {
                        hideDialog();
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+response.toString());
                            if(response.body().getResultset().getColor() != null && response.body().getResultset().getColor().size() > 0){
                                colorArrayList =  response.body().getResultset().getColor();
                                colorName = new String[colorArrayList.size()];
                                colorId = new String[colorArrayList.size()];
                                for(int i = 0; i < colorArrayList.size(); i++){
                                    colorName[i] = colorArrayList.get(i).getColourName() +" - "+colorArrayList.get(i).getColor_output_qnty();
                                    colorId[i] = colorArrayList.get(i).getColourId();
                                }
                                initColorSpinnerView();
                            }

                            if(response.body().getResultset().getSize() != null && response.body().getResultset().getSize().size() > 0){
                                sizeArrayList =  response.body().getResultset().getSize();
                            }

                            if(response.body().getResultset().getPoIds() != null && response.body().getResultset().getPoIds().size() > 0){
                                poIdArrayList =  response.body().getResultset().getPoIds();
//                                initPOSpinnerView();
                            }

                            if(response.body().getResultset().getAlterList() != null && response.body().getResultset().getAlterList().size() > 0){
                                sewingAlterModels.clear();
                                for(int i=0; i<response.body().getResultset().getAlterList().size(); i++){
                                    V1_SewingAlterModel sewingAlterModel = new V1_SewingAlterModel();
                                    String sentence = response.body().getResultset().getAlterList().get(i).getName();
                                    boolean isNumber = sentence.matches("\\d+");
                                    if(isNumber){
                                        sewingAlterModel.setDefectName(response.body().getResultset().getAlterList().get(i).getName());
                                    }else{
                                        String[] words = sentence.split("\\s+");
                                        StringBuilder sb = new StringBuilder();
                                        for (String word : words) {
                                            sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                        }
                                        String capitalizedSentence = sb.toString().trim();
                                        sewingAlterModel.setDefectName(capitalizedSentence);
                                    }
                                    sewingAlterModel.setId(response.body().getResultset().getAlterList().get(i).getId());

//                            sewingAlterModel.setDefectName(response.body().getResultset().getAlterList().get(i).getName());
                                    sewingAlterModel.setDefectCount(String.valueOf(0));
                                    sewingAlterModel.setDefectSelect(false);
                                    sewingAlterModels.add(sewingAlterModel);
                                }
                            }

                            if(response.body().getResultset().getRejectList() != null && response.body().getResultset().getRejectList().size() > 0){
                                sewingRejectModels.clear();
                                for(int i=0; i<response.body().getResultset().getRejectList().size(); i++){
                                    V1_SewingRejectModel sewingRejectModel = new V1_SewingRejectModel();
                                    String sentence = response.body().getResultset().getRejectList().get(i).getName();
                                    boolean isNumber = sentence.matches("\\d+");
                                    if(isNumber){
                                        sewingRejectModel.setDefectName(response.body().getResultset().getRejectList().get(i).getName());
                                    }else{
                                        String[] words = sentence.split("\\s+");
                                        StringBuilder sb = new StringBuilder();
                                        for (String word : words) {
                                            sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                        }
                                        String capitalizedSentence = sb.toString().trim();
                                        sewingRejectModel.setDefectName(capitalizedSentence);
                                    }
                                    sewingRejectModel.setId(response.body().getResultset().getRejectList().get(i).getId());
//                            sewingRejectModel.setDefectName(response.body().getResultset().getRejectList().get(i).getName());
                                    sewingRejectModel.setDefectCount(String.valueOf(0));
                                    sewingRejectModel.setDefectSelect(false);
                                    sewingRejectModels.add(sewingRejectModel);
                                }
                            }

                            if(response.body().getResultset().getSpotList() != null && response.body().getResultset().getSpotList().size() > 0){
                                sewingSpotModels.clear();
                                for(int i=0; i<response.body().getResultset().getSpotList().size(); i++){
                                    V1_SewingSpotModel sewingSpotModel = new V1_SewingSpotModel();
                                    String sentence = response.body().getResultset().getSpotList().get(i).getName();
                                    boolean isNumber = sentence.matches("\\d+");
                                    if(isNumber){
                                        sewingSpotModel.setDefectName(response.body().getResultset().getSpotList().get(i).getName());
                                    }else{
                                        String[] words = sentence.split("\\s+");
                                        StringBuilder sb = new StringBuilder();
                                        for (String word : words) {
                                            sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                        }
                                        String capitalizedSentence = sb.toString().trim();
                                        sewingSpotModel.setDefectName(capitalizedSentence);
                                    }
                                    sewingSpotModel.setId(response.body().getResultset().getSpotList().get(i).getId());
//                            sewingSpotModel.setDefectName(response.body().getResultset().getSpotList().get(i).getName());
                                    sewingSpotModel.setDefectCount(String.valueOf(0));
                                    sewingSpotModel.setDefectSelect(false);
                                    sewingSpotModels.add(sewingSpotModel);
                                }
                            }

                            initOperationRecyclerView();
                        }
                    }

                    @Override
                    public void onFailure(Call<V1_StyleWiseOperationResponse> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void initOperationRecyclerView() {
        sewingOutputOperationRecyclerAdapter = new V1_StyleWiseSewingOperationRecyclerAdapter(operationItemModelList, this,  this);
        operationRecyclerView.setAdapter(sewingOutputOperationRecyclerAdapter);
        operationRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void initPOSpinnerView() {
        POSpinnerAdapter = new V2_POSpinnerAdapter(this, colorWisePOModels);
        poSpinner.setAdapter(POSpinnerAdapter);
        poSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                po_breakDownId = colorWisePOModels.get(position).getPoId();
                colorSelectForSize(position);
                _inputQtyTV.setText("0");
                _checkQtyTV.setText("0");
                _outputQtyTv.setText("0");
                typeWiseInOut("GOOD");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initColorSpinnerView() {
        for (V1_StyleWiseOperationResponse.Color obj : colorArrayList) {
            if (!idsSeen.contains(obj.getColourId())) {
                uniqueColorList.add(obj);
                idsSeen.add(obj.getColourId());
            }
        }

        colorSpinnerAdapter = new V3_ColorSpinnerAdapter(this, uniqueColorList);
        colorSpinner.setAdapter(colorSpinnerAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorSelection = position;
                selectedColorID = uniqueColorList.get(position).getColourId();
                colorWisePOModels.clear();

                for(int i=0; i<colorArrayList.size(); i++){
                    if(colorArrayList.get(i).getColourId().equals(selectedColorID)){
                        V2_ColorWisePOModel v2_colorWisePOModel = new V2_ColorWisePOModel();
                        v2_colorWisePOModel.setPoId(String.valueOf(colorArrayList.get(i).getPo_break_down_id()));
                        for(V1_StyleWiseOperationResponse.PoId item: poIdArrayList) {
                            if(item.getPoId().equals(colorArrayList.get(i).getPo_break_down_id())){
                                v2_colorWisePOModel.setPoNumber(item.getPoNumber());
                            }
                        }
                        colorWisePOModels.add(v2_colorWisePOModel);
                    }
                }
                initPOSpinnerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSizeRecyclerView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _sizeLayout.getLayoutParams();
        if(!defectType.equals("G")){
            if(poColorWiseSizeArrayList.size() <= 5){
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }else{
                params.height = 300;
            }
        }else{
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        _sizeLayout.setLayoutParams(params);

        Log.d(TAG, "initSizeRecyclerView: "+poColorWiseSizeArrayList.size());
        if(defectType.equals("G")){
            sewingGoodQualitySizeRecyclerAdapter = new V2_StyleWiseSewingGoodQualitySizeRecyclerAdapter(poColorWiseSizeArrayList, this,  this);
            sizeRecyclerView.setAdapter(sewingGoodQualitySizeRecyclerAdapter);
            sizeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
        }else{
            sewingSizeRecyclerAdapter = new V2_StyleWiseSewingSizeRecyclerAdapter(poColorWiseSizeArrayList, this,  this);
            sizeRecyclerView.setAdapter(sewingSizeRecyclerAdapter);
            sizeRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.saveBT:
                saveDataAction();
                break;
            case R.id.operationButton:
                if(reject_number == 1) {
                    _defectLayout.setVisibility(View.GONE);
                    _operationLayout.setVisibility(View.VISIBLE);
                    _operationLinearLayout.setVisibility(View.GONE);
                }else{
                    _defectLayout.setVisibility(View.GONE);
                    _operationLayout.setVisibility(View.VISIBLE);
                    _operationLinearLayout.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.undoButton:
                undoStatus = true;
                saveDataAction();
                break;
        }
    }

    private void saveDataAction(){
        int totalAlterSpot = Integer.parseInt(_alterTV.getText().toString()) + Integer.parseInt(_spotTV.getText().toString());
        int totalRectified = Integer.parseInt(_rectifiedTV.getText().toString());

        boolean alterDefectSelectStatus = false;
        boolean spotDefectSelectStatus = false;
        Log.d(TAG, "saveDataAction: after save"+alterDefectSelectStatus+" "+spotDefectSelectStatus);
        if(locationId != 0 && floorId != 0 && lineId != 0)
        {
            if(defectType.equals("A")) {
                spotDefectSelectStatus = true;
                for(int i = 0; i < sewingAlterModels.size(); i++)
                {
                    if(sewingAlterModels.get(i).getDefectCount() != null && !sewingAlterModels.get(i).getDefectCount().equals("") && !sewingAlterModels.get(i).getDefectCount().equals("0")){
                        alter_defect += selected_operation_id+"*"+sewingAlterModels.get(i).getId()+"*"+ sewingAlterModels.get(i).getDefectCount()+"__";
                        alterDefectSelectStatus = true;
                    }
                }
            }

            if(defectType.equals("S")) {
                alterDefectSelectStatus = true;
                for(int i = 0; i < sewingSpotModels.size(); i++)
                {
                    if(sewingSpotModels.get(i).getDefectCount() != null && !sewingSpotModels.get(i).getDefectCount().equals("") && !sewingSpotModels.get(i).getDefectCount().equals("0")){
                        spot_defect += selected_operation_id+"*"+sewingSpotModels.get(i).getId()+"*"+ sewingSpotModels.get(i).getDefectCount()+"__";
                        spotDefectSelectStatus = true;
                    }
                }
            }

            if(defectType.equals("R")) {
                alterDefectSelectStatus = true;
                spotDefectSelectStatus = true;
                for(int i = 0; i < sewingRejectModels.size(); i++)
                {
                    if(sewingRejectModels.get(i).getDefectCount() != null && !sewingRejectModels.get(i).getDefectCount().equals("") && !sewingRejectModels.get(i).getDefectCount().equals("0")){
                        reject_defect += selected_operation_id+"*"+sewingRejectModels.get(i).getId()+"*"+ sewingRejectModels.get(i).getDefectCount()+"__";
                    }
                }
            }

            if(defectType.equals("G")){
                alterDefectSelectStatus = true;
                spotDefectSelectStatus = true;
            }

            if(colorSelection == -1){
                _defectLayout.setVisibility(View.GONE);
                _operationLayout.setVisibility(View.VISIBLE);
                showAlertMessage("Color not selected", 0);
            }
//            else if(sizeSelection == -1){
//                _defectLayout.setVisibility(View.GONE);
//                _operationLayout.setVisibility(View.VISIBLE);
//                showAlertMessage("Size not selected", 0);
//            }
            else {
                if(rectified_number == 1){
                    try {
                        boolean selectedRectifiedStatus = false;
                        for(int i=0; i < defectedRectifiedModels.size(); i++){
                            if(defectedRectifiedModels.get(i).getSelect()){
                                selectedRectifiedStatus = true;
                                break;
                            }else{
                                selectedRectifiedStatus = false;
                            }
                        }
                        if(selectedRectifiedStatus){
                            postDataToServer_sewing_input_rectified();
                        }else{
                            showAlertMessage("Select Rectified item.", 0);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if(sizeSelection == -1){
                        _defectLayout.setVisibility(View.GONE);
                        _operationLayout.setVisibility(View.VISIBLE);
                        showAlertMessage("Size not selected", 0);
                    }
                    else if(!alterDefectSelectStatus ){
                        showAlertMessage("Please enter minimum 1 defect count.", 0);
                    } else if(!spotDefectSelectStatus ){
                        showAlertMessage("Please enter minimum 1 defect count.", 0);
                    }
                    else {
                        try {
                            postDataToServer_sewing_input();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if(qty==0) {
            showAlertMessage("Quantity doesn't zero",0);
        }
    }

    private void postDataToServer_sewing_input() throws JSONException  {
        JSONObject jsonObject = buildJsonObject();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();
        apiInterface.saveUpdateBundleSewingGrossOutputCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
            @Override
            public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                hideDialog();
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: #######"+response.body().getResultset().getStatus());
//                    Toast.makeText(V1_StyleWiseSewingActivity_v3.this, response.body().getResultset().getStatus(), Toast.LENGTH_SHORT).show();
//                    if(!response.body().getResultset().getStatus().equals("দুঃখিত! আউটপুট কোয়ানটিটি ইনপুট কোয়ানটিটি এর থেকে বেশি দেওয়া যাবেনা ।")){
                    if(!response.body().getResultset().getStatus().equals("দুঃখিত! আউটপুট কোয়ানটিটি ইনপুট কোয়ানটিটি এর থেকে বেশি দেওয়া যাবেনা ।")){
                        _inputQtyTV.setText(String.valueOf(response.body().getResultset().getInputQnty()));
                        _checkQtyTV.setText(String.valueOf(response.body().getResultset().getOutputQnty()));
                        _outputQtyTv.setText(String.valueOf(response.body().getResultset().getTotalOutQnty()));
                        _goodTV.setText(response.body().getResultset().getOk());
                        _rejectTV.setText(response.body().getResultset().getRej());
                        _alterTV.setText(response.body().getResultset().getAlter());
                        _spotTV.setText(response.body().getResultset().getSpot());
                        _rectifiedTV.setText(response.body().getResultset().getRectified());
                        UPDATE_ID = String.valueOf(response.body().getResultset().getUpdateId());
                        UPDATE_DTLS_ID = String.valueOf(response.body().getResultset().getUpdateDtlsId());
                        UPDATE_DTLS_PIECE_ID = String.valueOf(response.body().getResultset().getUpdateDtlsPieceId());
                        DELETE_DATA_TYPE = action_type;

                        saved_color_wise_check_qty = String.valueOf(response.body().getResultset().getColorWiseChkQnty());
                        _colorWiseOutputTV.setText(response.body().getResultset().getColorWiseChkQnty());

                        if(undoStatus){
                            undoStatus = false;
                            _undoButton.setEnabled(false);
                            _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
//                            saved_color_wise_check_qty = String.valueOf(Integer.valueOf(colorArrayList.get(poWiseColorSelection).getColor_output_qnty())-1);
//                            saved_color_wise_check_qty = String.valueOf(Integer.valueOf(uniqueColorList.get(colorSelection).getColor_output_qnty())-1);
                            badgeDecrement();
                        } else {
                            _undoButton.setEnabled(true);
                            _undoButton.setImageResource(R.drawable.ic_baseline_undo_24);
//                            saved_color_wise_check_qty = String.valueOf(Integer.valueOf(colorArrayList.get(poWiseColorSelection).getColor_output_qnty())+1);
                            badgeIncrement();
                        }
                        colorSpinnerBadge();
                        defauldGoodSelect(action_type);
                        showSuccessToastMessage(response.body().getResultset().getStatus());
                        defectedRectifiedModels.clear();
                    }else{
                        Toast toast= Toasty.error(getApplicationContext(),
                                response.body().getResultset().getStatus(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }

                }else{
                    Log.d("TAG", "onResponse: "+response.toString());
                    _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
                    undoStatus = false;
                }
            }

            @Override
            public void onFailure(Call<V1_GrossSewingSaveResponse> call, Throwable t) {
                hideDialog();
                _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
                undoStatus = false;

                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast toast= Toasty.error(getApplicationContext(),
                        "Failed.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
    }


    private void colorSpinnerBadge() {
//        colorArrayList.get(poWiseColorSelection).setColor_output_qnty(saved_color_wise_check_qty);
//        poWiseColorArrayList.get(colorSelection).setColor_output_qnty(saved_color_wise_check_qty);
        uniqueColorList.get(colorSelection).setColor_output_qnty(saved_color_wise_check_qty);
        colorSpinnerAdapter.notifyDataSetChanged();
    }
    private void postDataToServer_sewing_input_rectified() throws JSONException {
        JSONObject jsonObject = buildJsonObject_rectified();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();
        apiInterface.saveUpdateBundleSewingGrossOutputRectifiedCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
            @Override
            public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                hideDialog();
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: #######"+response.body().getResultset().getStatus());
                    if(!response.body().getResultset().getStatus().equals("দুঃখিত! আউটপুট কোয়ানটিটি ইনপুট কোয়ানটিটি এর থেকে বেশি দেওয়া যাবেনা ।")){
                        _inputQtyTV.setText(String.valueOf(response.body().getResultset().getInputQnty()));
                        _checkQtyTV.setText(String.valueOf(response.body().getResultset().getOutputQnty()));
                        _outputQtyTv.setText(String.valueOf(response.body().getResultset().getTotalOutQnty()));
                        _goodTV.setText(response.body().getResultset().getOk());
                        _rejectTV.setText(response.body().getResultset().getRej());
                        _alterTV.setText(response.body().getResultset().getAlter());
                        _spotTV.setText(response.body().getResultset().getSpot());
                        _rectifiedTV.setText(response.body().getResultset().getRectified());
                        UPDATE_ID = String.valueOf(response.body().getResultset().getUpdateId());

                        saved_color_wise_check_qty = String.valueOf(response.body().getResultset().getColorWiseChkQnty());
                        _colorWiseOutputTV.setText(response.body().getResultset().getColorWiseChkQnty());

                        _undoButton.setEnabled(false);
                        _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
                        if(undoStatus){
                            undoStatus = false;
                            badgeDecrement();
                        } else {
                            badgeIncrement();
                        }

                        colorSpinnerBadge();
                        defauldGoodSelect(action_type);
                        showSuccessToastMessage(response.body().getResultset().getStatus());
                        defectedRectifiedModels.clear();
                    }else{
                        Toast toast= Toasty.error(getApplicationContext(),
                                response.body().getResultset().getStatus(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }

                }else{
                    Log.d("TAG", "onResponse: "+response.toString());
                    _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
                    undoStatus = false;
                }
            }

            @Override
            public void onFailure(Call<V1_GrossSewingSaveResponse> call, Throwable t) {
                hideDialog();
                _undoButton.setImageResource(R.drawable.ic_baseline_undo_grey_24);
                undoStatus = false;

                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast toast= Toasty.error(getApplicationContext(),
                        "Failed.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

    }

    private void badgeIncrement() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int index = IntStream.range(0, sizeArrayList.size())
                    .filter(i -> sizeArrayList.get(i).getColorSizeBreakdownId().equals(coloSizeBreakDownId))
                    .findFirst()
                    .orElse(-1);
//            sizeArrayList.get(index).setGood(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
        }
        savedColorSizeBreakDownId = String.valueOf(modelArrayList.get(0).getColor_size());
        savedSizeSelection = sizeSelection;
        if(good_number==1){
            poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
        }else if(rectified_number==1){
            for(int i=0; i<uniqueDefectedRectifiedModels.size(); i++){
                if(uniqueDefectedRectifiedModels.get(i).getSelect()){
                    int index = -1;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        int finalI = i;
                        index = IntStream.range(0, poColorWiseSizeArrayList.size())
                                .filter(j -> poColorWiseSizeArrayList.get(j).getColorSizeBreakdownId().equals(uniqueDefectedRectifiedModels.get(finalI).getColorSizedId()))
                                .findFirst()
                                .orElse(-1);
                    }
                    Log.d(TAG, "badgeIncrement: "+uniqueDefectedRectifiedModels.get(i).getColorSizedId());
                    Log.d(TAG, "badgeIncrement Index: "+index);
                    try {
                        poColorWiseSizeArrayList.get(index).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(index).getBadgeQuantity())+1));
                    }catch (Exception e){
                        Log.d(TAG, "badgeIncrement: "+e.getMessage());
                    }
                }
            }
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }
//        else if(alter_number==1){
//            poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }else if(spot_number==1){
//            poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }else if(rectified_number==1){
//            poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    private void badgeDecrement() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int index = IntStream.range(0, sizeArrayList.size())
                    .filter(i -> sizeArrayList.get(i).getColorSizeBreakdownId().equals(coloSizeBreakDownId))
                    .findFirst()
                    .orElse(-1);
//            sizeArrayList.get(index).setGood(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
        }
        if(undo_type.equals("GOOD")){
            poColorWiseSizeArrayList.get(savedSizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(savedSizeSelection).getBadgeQuantity())-1));
            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
        }
//        else if(rectified_number==1){
//            poColorWiseSizeArrayList.get(savedSizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(savedSizeSelection).getBadgeQuantity())-1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }else if(alter_number==1){
//            poColorWiseSizeArrayList.get(savedSizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(savedSizeSelection).getBadgeQuantity())-1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }else if(spot_number==1){
//            poColorWiseSizeArrayList.get(savedSizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(savedSizeSelection).getBadgeQuantity())-1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }else if(rectified_number==1){
////            poColorWiseSizeArrayList.get(savedSizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(savedSizeSelection).getBadgeQuantity())-1));
//            sewingSizeRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    private JSONObject buildJsonObject() throws JSONException{
        JSONObject save_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();
        JSONArray rectified_arr = new JSONArray();

        save_obj.put("status",true);

        save_obj.put("production_type", 5);
        save_obj.put("action_type", action_type);

        if(undoStatus == true){
            save_obj.put("mode", "delete");
            save_obj.put("UPDATE_ID", UPDATE_ID);
            save_obj.put("UPDATE_DTLS_ID", UPDATE_DTLS_ID);
            save_obj.put("UPDATE_DTLS_PIECE_ID", UPDATE_DTLS_PIECE_ID);
            save_obj.put("DELETE_DATA_TYPE", DELETE_DATA_TYPE);
        }else{
            save_obj.put("mode", "save");
            save_obj.put("UPDATE_ID", 0);
        }

        index_obj.put("company_id", companyId);
        index_obj.put("shift_id", 1);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", 1);
        index_obj.put("serving_company", companyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        index_obj.put("organic", "");

        index_obj.put("user_id", userId);
        index_obj.put("production_date", currentDate);
        index_obj.put("hour", currentTime);
        index_obj.put("remarks", "no");
        index_obj.put("txt_system_id", "");
        index_obj.put("mac", macAddress);

        data_obj.put("index", index_obj);
        if(rectified_number == 1){
            int rectifiedCount = 0;
            for(int i = 0; i < defectedRectifiedModels.size(); i++)
            {
                if(defectedRectifiedModels.get(i).getSelect()){
                    rectifiedCount += 1;
                    JSONObject dtls_obj = new JSONObject();
                    dtls_obj.put("order_id", po_breakDownId);
                    dtls_obj.put("job_id", jobId);
                    dtls_obj.put("item_id", modelArrayList.get(0).getItem_id());
                    dtls_obj.put("country_id", modelArrayList.get(0).getCountry_id());
                    dtls_obj.put("color_id", modelArrayList.get(0).getColor_id());
                    dtls_obj.put("size_id", modelArrayList.get(0).getSize_id());
                    dtls_obj.put("color_size_id", defectedRectifiedModels.get(i).getColorSizedId());
                    dtls_obj.put("qnty", 1);
                    dtls_obj.put("reject", reject_number);
                    dtls_obj.put("alter", alter_number);
                    dtls_obj.put("spot", spot_number);
                    dtls_obj.put("good", good_number);
                    dtls_obj.put("rectified", rectified_number);
                    dtls_obj.put("qc_qnty", 0);
                    dtls_obj.put("color_type_id", color_type_id);
                    dtls_obj.put("sewing_input_line", lineId);
                    dtls_obj.put("operation_id", selected_operation_id);
                    dtls_obj.put("dtls_id", defectedRectifiedModels.get(i).getDtlsId());
                    dtls_arr.put(dtls_obj);
                }
            }
            save_obj.put("total_qnty", rectifiedCount);
        }else{
            save_obj.put("total_qnty", 1);
            for(int i = 0; i < 1; i++)
            {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("order_id", po_breakDownId);
                dtls_obj.put("job_id", jobId);
                dtls_obj.put("item_id", modelArrayList.get(i).getItem_id());
                dtls_obj.put("country_id", modelArrayList.get(i).getCountry_id());
                dtls_obj.put("color_id", modelArrayList.get(i).getColor_id());
                dtls_obj.put("size_id", modelArrayList.get(i).getSize_id());
                dtls_obj.put("color_size_id", modelArrayList.get(i).getColor_size());
                dtls_obj.put("qnty", 1);
                dtls_obj.put("reject", reject_number);
                dtls_obj.put("alter", alter_number);
                dtls_obj.put("spot", spot_number);
                dtls_obj.put("good", good_number);
                dtls_obj.put("rectified", rectified_number);
                dtls_obj.put("qc_qnty", 0);
                dtls_obj.put("color_type_id", color_type_id);
                dtls_obj.put("sewing_input_line", lineId);
                dtls_obj.put("operation_id", selected_operation_id);
                dtls_obj.put("dtls_id", dtls_id);
                dtls_arr.put(dtls_obj);
            }
        }


        data_obj.put("actual_reject", reject_defect.equals("")? "" : reject_defect.substring(0, reject_defect.length() - 2));
        data_obj.put("actual_alter", alter_defect.equals("")? "" : alter_defect.substring(0, alter_defect.length() - 2));
        data_obj.put("actual_spot", spot_defect.equals("")? "" : spot_defect.substring(0, spot_defect.length() - 2));

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d("TAG", "buildJsonObject: ######"+save_obj);
        return save_obj;
    }

    private JSONObject buildJsonObject_rectified() throws JSONException{
        JSONObject save_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();
        JSONArray rectified_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("production_type", 5);
        save_obj.put("action_type", action_type);

        if(undoStatus == true){
            save_obj.put("mode", "delete");
            save_obj.put("UPDATE_ID", UPDATE_ID);
        }else{
            save_obj.put("mode", "save");
        }

        index_obj.put("company_id", companyId);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", 1);
        index_obj.put("serving_company", companyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        index_obj.put("user_id", userId);
        index_obj.put("production_date", currentDate);
        index_obj.put("hour", currentTime);
        index_obj.put("mac", macAddress);

        data_obj.put("index", index_obj);
        int rectifiedCount = 0;

        Set set = new TreeSet((o1, o2) -> {
            if (((V1_DefectedRectifiedModel) o1).getDtls_piece_id().equalsIgnoreCase(((V1_DefectedRectifiedModel) o2).getDtls_piece_id())) {
                return 0;
            }
            return 1;
        });
        set.addAll(defectedRectifiedModels);

        uniqueDefectedRectifiedModels = new ArrayList(set);

        for(int i = 0; i < uniqueDefectedRectifiedModels.size(); i++)
        {
            if(uniqueDefectedRectifiedModels.get(i).getSelect()){
                rectifiedCount += 1;
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("order_id", po_breakDownId);
                dtls_obj.put("job_id", jobId);
                dtls_obj.put("item_id", modelArrayList.get(0).getItem_id());
                dtls_obj.put("country_id", modelArrayList.get(0).getCountry_id());
                dtls_obj.put("color_id", uniqueDefectedRectifiedModels.get(i).getColorId());
                dtls_obj.put("size_id", uniqueDefectedRectifiedModels.get(i).getSizeId());
                dtls_obj.put("color_size_id", uniqueDefectedRectifiedModels.get(i).getColorSizedId());
                dtls_obj.put("dtls_id", uniqueDefectedRectifiedModels.get(i).getDtlsId());
                dtls_obj.put("dtls_piece_id", uniqueDefectedRectifiedModels.get(i).getDtls_piece_id());
                dtls_obj.put("mst_id", uniqueDefectedRectifiedModels.get(i).getMstId());
                dtls_obj.put("operation_id", uniqueDefectedRectifiedModels.get(i).getOperationId());
                dtls_obj.put("color_type_id", uniqueDefectedRectifiedModels.get(i).getColor_type_id());
                dtls_arr.put(dtls_obj);
            }
        }

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d("TAG", "buildJsonObject: rectified ######"+save_obj);
        return save_obj;
    }

    private void showAlertMessage(String msg, Integer i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_StyleWiseSewingActivity_v5.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    private void showSuccessToastMessage(String status) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast, (ViewGroup)       findViewById(R.id.custom_toast_layout_id));
        TextView textView;
        textView = layout.findViewById(R.id.text);
        textView.setText("✔ "+status);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    private void showDialog() {
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onColorHeadClick(int position, View v) {
    }

    private void colorSelectForSize(int position) {
        _colorWiseOutputTV.setText(colorArrayList.get(position).getColor_output_qnty());
        sizeSelection = -1;
        defectedRectifiedModels.clear();
        coloSizeBreakDownId = "";
        modelArrayList.get(0).setColor_id(Integer.parseInt(selectedColorID));
        rectifiedListCard.setVisibility(View.GONE);
        defauldGoodSelect(action_type);
        Log.d(TAG, "onColorHeadClick: "+sizeArrayList.size());
        poColorWiseSizeArrayList.clear();
        for(int i=0; i<sizeArrayList.size(); i++){
            if(selectedColorID.equals(sizeArrayList.get(i).getColourId()) && po_breakDownId.equals(sizeArrayList.get(i).getPo_break_down_id())){
                Log.d(TAG, "onColorHeadClick: ###"+sizeArrayList.get(position).getColorSizeBreakdownId());
                V2_POColorWiseSizeModel colorWiseSizeItemModel = new V2_POColorWiseSizeModel();
                colorWiseSizeItemModel.setPo_break_down_id(String.valueOf(sizeArrayList.get(i).getPo_break_down_id()));
                colorWiseSizeItemModel.setColorSizeBreakdownId(String.valueOf(sizeArrayList.get(i).getColorSizeBreakdownId()));
                colorWiseSizeItemModel.setSizeId(String.valueOf(sizeArrayList.get(i).getSizeId()));
                colorWiseSizeItemModel.setSizeName(String.valueOf(sizeArrayList.get(i).getSizeName()));
                colorWiseSizeItemModel.setColourId(String.valueOf(sizeArrayList.get(i).getColourId()));
                colorWiseSizeItemModel.setInputQnty(String.valueOf(sizeArrayList.get(i).getInputQnty()));
                colorWiseSizeItemModel.setOutputQnty(String.valueOf(sizeArrayList.get(i).getOutputQnty()));
                colorWiseSizeItemModel.setGood(String.valueOf(sizeArrayList.get(i).getGood()));
                colorWiseSizeItemModel.setReject(String.valueOf(sizeArrayList.get(i).getReject()));
                colorWiseSizeItemModel.setAlter(String.valueOf(sizeArrayList.get(i).getAlter()));
                colorWiseSizeItemModel.setSpot(String.valueOf(sizeArrayList.get(i).getSpot()));
                colorWiseSizeItemModel.setRectified(String.valueOf(sizeArrayList.get(i).getRectified()));
                if(good_number == 1)
                    colorWiseSizeItemModel.setBadgeQuantity(String.valueOf(sizeArrayList.get(i).getGood()));
                if(reject_number == 1)
                    colorWiseSizeItemModel.setBadgeQuantity(String.valueOf(sizeArrayList.get(i).getReject()));
                if(alter_number == 1)
                    colorWiseSizeItemModel.setBadgeQuantity(String.valueOf(sizeArrayList.get(i).getAlter()));
                if(spot_number == 1)
                    colorWiseSizeItemModel.setBadgeQuantity(String.valueOf(sizeArrayList.get(i).getSpot()));
                if(rectified_number == 1)
                    colorWiseSizeItemModel.setBadgeQuantity(String.valueOf(sizeArrayList.get(i).getRectified()));
                colorWiseSizeItemModel.setSelectedItem(false);
                poColorWiseSizeArrayList.add(colorWiseSizeItemModel);
            }
        }
        initSizeRecyclerView();
    }

    private void defauldGoodSelect(String type) {
        defectType = "G";
        action_type = "GOOD";
        undo_type = type;
        good_number = 1;
        spot_number = 0;
        alter_number = 0;
        reject_number = 0;
        rectified_number = 0;
        selected_operation_id = "0";
        _goodRB.setChecked(true);
        dataResetMethod();
        typeWiseInOut("GOOD");
    }

    @Override
    public void onOperationHeadClick(int position, View v) {
        if(!selected_operation_id.equals(operationItemModelList.get(position).getOperationId())){
            for(int i=0; i<sewingAlterModels.size(); i++){
                sewingAlterModels.get(i).setDefectCount("0");
            }
            for(int i=0; i<sewingSpotModels.size(); i++){
                sewingSpotModels.get(i).setDefectCount("0");
            }
            try {
                sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
                sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
            }catch (Exception e){

            }

        }
        if (sizeSelection != -1){
            _defectLayout.setVisibility(View.VISIBLE);
            _operationLayout.setVisibility(View.GONE);
            if(operationItemModelList.get(position).getStatus()){
                operationItemModelList.get(position).setStatus(false);
            }else{
                operationItemModelList.get(position).setStatus(true);
                selected_operation_id = operationItemModelList.get(position).getOperationId();
            }
            _operationNameTV.setText("Operation Name: "+operationItemModelList.get(position).getOperationName());
            sewingOutputOperationRecyclerAdapter.notifyDataSetChanged();
        }else {
            showAlertMessage("Size not selected", 0);
            operationItemModelList.get(position).setStatus(false);
            sewingOutputOperationRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSizeHeadClick(int position, View v) {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            int index = IntStream.range(0, sizeArrayList.size())
//                    .filter(i -> sizeArrayList.get(i).getColourId().equals(selectedColorID) && sizeArrayList.get(i).getPo_break_down_id().equals(po_breakDownId))
//                    .findFirst()
//                    .orElse(-1);
//            coloSizeBreakDownId = poColorWiseSizeArrayList.get(index).getColorSizeBreakdownId();
//        }

        sizeSelection = position;
        coloSizeBreakDownId = poColorWiseSizeArrayList.get(position).getColorSizeBreakdownId();
        modelArrayList.get(0).setSize_id(Integer.parseInt(String.valueOf(poColorWiseSizeArrayList.get(position).getSizeId())));
        modelArrayList.get(0).setColor_size(Integer.parseInt(coloSizeBreakDownId));
        savedColorSizeBreakDownId = String.valueOf(modelArrayList.get(0).getColor_size());
        for(int i =0; i<poColorWiseSizeArrayList.size(); i++){
            if(i == position){
                poColorWiseSizeArrayList.get(i).setSelectedItem(true);
            }else{
                poColorWiseSizeArrayList.get(i).setSelectedItem(false);
            }
        }
        if(defectType.equals("G")){
            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
        }else{
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }

        if(rectified_number == 1) {
            defectedRectifiedModels.clear();
            requestForRectifiedData();
        }

        if(reject_number == 1){
            if (sizeSelection != -1){
                _defectLayout.setVisibility(View.VISIBLE);
                _operationLayout.setVisibility(View.GONE);
                operationPopup();
            }
        }

        Log.d(TAG, "onSizeHeadClick: "+poColorWiseSizeArrayList.get(position).getColorSizeBreakdownId()+" "+coloSizeBreakDownId);
        apiInterface.getColorBreakDownClassCall(coloSizeBreakDownId, String.valueOf(lineId), po_breakDownId, item_numberId, countryId, userId ).enqueue(new Callback<V1_ColorBreakDownResponse>() {
            @Override
            public void onResponse(Call<V1_ColorBreakDownResponse> call, Response<V1_ColorBreakDownResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: onSize click"+response.toString());
                if(response.isSuccessful()){
                    if(poColorWiseSizeArrayList != null){
                        _inputQtyTV.setText(String.valueOf(response.body().getResultset().getInQnty()));
                        _checkQtyTV.setText(String.valueOf(response.body().getResultset().getOutQnty()));
                        _outputQtyTv.setText(String.valueOf(response.body().getResultset().getTotal_out_qnty()));

                        _goodTV.setText(response.body().getResultset().getGood());
                        _rejectTV.setText(response.body().getResultset().getReject());
                        _alterTV.setText(response.body().getResultset().getAlter());
                        _spotTV.setText(response.body().getResultset().getSpot());
                        _rectifiedTV.setText(response.body().getResultset().getRectified());

                        if(defectType.equals("G")){
                            saveDataAction();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_ColorBreakDownResponse> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(V1_StyleWiseSewingActivity_v5.this, "Can't get color size data.", Toast.LENGTH_SHORT).show();
            }
        });

//        if(!defectType.equals("G")){
//            Log.d(TAG, "onSizeHeadClick: "+poColorWiseSizeArrayList.get(position).getColorSizeBreakdownId()+" "+coloSizeBreakDownId);
//            apiInterface.getColorBreakDownClassCall(coloSizeBreakDownId, String.valueOf(lineId), po_breakDownId, item_numberId, countryId, userId ).enqueue(new Callback<V1_ColorBreakDownResponse>() {
//                @Override
//                public void onResponse(Call<V1_ColorBreakDownResponse> call, Response<V1_ColorBreakDownResponse> response) {
//                    hideDialog();
//                    Log.d(TAG, "onResponse: onSize click"+response.toString());
//                    if(response.isSuccessful()){
//                        if(poColorWiseSizeArrayList != null){
//                            _inputQtyTV.setText(String.valueOf(response.body().getResultset().getInQnty()));
//                            _checkQtyTV.setText(String.valueOf(response.body().getResultset().getOutQnty()));
//                            _outputQtyTv.setText(String.valueOf(response.body().getResultset().getTotal_out_qnty()));
//
//                            _goodTV.setText(response.body().getResultset().getGood());
//                            _rejectTV.setText(response.body().getResultset().getReject());
//                            _alterTV.setText(response.body().getResultset().getAlter());
//                            _spotTV.setText(response.body().getResultset().getSpot());
//                            _rectifiedTV.setText(response.body().getResultset().getRectified());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<V1_ColorBreakDownResponse> call, Throwable t) {
//                    hideDialog();
//                    Log.d(TAG, "onFailure: "+t.getMessage());
//                    Toast.makeText(V1_StyleWiseSewingActivity_v3.this, "Can't get color size data.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }else if(defectType.equals("G")){
//            saveDataAction();
//        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.rejectRB:
                if(checked){
                    defectType = "R";
                    action_type = "REJECT";
                    reject_number = 1;
                    _operationButton.setText("SIZE LIST");
                    alter_number = 0;
                    spot_number = 0;
                    good_number = 0;
                    rectified_number = 0;
                    operationPopup();
                    typeWiseInOut("REJECT");
                }
                break;
            case R.id.alterRB:
                if(checked){
                    defectType = "A";
                    action_type = "ALTER";
                    alter_number = 1;
                    _operationButton.setText("OPERATION LIST");
                    _operationLinearLayout.setVisibility(View.VISIBLE);
                    reject_number = 0;
                    spot_number = 0;
                    good_number = 0;
                    rectified_number = 0;
                    operationPopup();
                    typeWiseInOut("ALTER");
                }
                break;
            case R.id.spotRB:
                if(checked){
                    defectType = "S";
                    action_type = "SPOT";
                    spot_number = 1;
                    _operationButton.setText("OPERATION LIST");
                    _operationLinearLayout.setVisibility(View.VISIBLE);
                    alter_number = 0;
                    reject_number = 0;
                    good_number = 0;
                    rectified_number = 0;
                    operationPopup();
                    typeWiseInOut("SPOT");
                }
                break;
            case R.id.goodRB:
                if(checked){
                    defectType = "G";
                    action_type = "GOOD";
                    good_number = 1;
                    spot_number = 0;
                    alter_number = 0;
                    reject_number = 0;
                    rectified_number = 0;
                    operationPopup();
                    typeWiseInOut("GOOD");
                }
                break;
            case R.id.rectifiedRB:
                if(checked){
                    defectType = "RE";
                    action_type = "RECTIFIED";
                    good_number = 0;
                    spot_number = 0;
                    alter_number = 0;
                    reject_number = 0;
                    rectified_number = 1;
                    sizeSelection = -1;
                    operationPopup();
                    typeWiseInOut("RECTIFIED");
                }
                break;
        }
    }

    private void typeWiseInOut(String type) {
        if(type.equals("GOOD")){
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
        } else if(type.equals("REJECT")) {
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        } else if(type.equals("ALTER")) {
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }else if(type.equals("SPOT")){
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }else if(type.equals("RECTIFIED")){
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            sewingSizeRecyclerAdapter.notifyDataSetChanged();
        }
        apiInterface.getTypeWiseInOutModelCall(po_breakDownId, item_numberId, String.valueOf(lineId), userId, String.valueOf(selectedColorID), type, countryId).enqueue(new Callback<V2_TypeWiseInOutResponseModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<V2_TypeWiseInOutResponseModel> call, Response<V2_TypeWiseInOutResponseModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    if(response.body().getResultset().getSizeQnty() != null && response.body().getResultset().getSizeQnty().size() > 0) {
                        typeWiseInOutModelsArrayList.clear();
                        for(int i = 0; i < response.body().getResultset().getSizeQnty().size(); i++){
                            V2_TypeWiseInOutModel typeWiseInOutModel = new V2_TypeWiseInOutModel();
                            //                            typeWiseInOutModel.setPo_break_down_id(colorArrayList.get(colorSelection).getPo_break_down_id());
                            typeWiseInOutModel.setPo_break_down_id(po_breakDownId);
//                            typeWiseInOutModel.setColor_id(colorArrayList.get(colorSelection).getColourId());
                            typeWiseInOutModel.setColor_id(selectedColorID);
                            typeWiseInOutModel.setSizeId(response.body().getResultset().getSizeQnty().get(i).getSizeId());
                            typeWiseInOutModel.setGood(response.body().getResultset().getSizeQnty().get(i).getGood());
                            typeWiseInOutModel.setReject(response.body().getResultset().getSizeQnty().get(i).getReject());
                            typeWiseInOutModel.setAlter(response.body().getResultset().getSizeQnty().get(i).getAlter());
                            typeWiseInOutModel.setSpot(response.body().getResultset().getSizeQnty().get(i).getSpot());
                            typeWiseInOutModel.setRectified(response.body().getResultset().getSizeQnty().get(i).getRectified());
                            typeWiseInOutModelsArrayList.add(typeWiseInOutModel);
                        }

                        if(type.equals("GOOD")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getGood());
                                    }
                                }
                            }
//                            for(V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList){
//                                Log.d(TAG, "onResponse P2: "+p2.getGood());
//                            }
//                            for(V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList){
//                                Log.d(TAG, "onResponse P1: "+p1.getBadgeQuantity());
//                            }
                            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
                        } else if(type.equals("REJECT")) {
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getReject());
                                    }
                                }
                            }
//                            for(V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList){
//                                Log.d(TAG, "onResponse P2: "+p2.getReject());
//                            }
//                            for(V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList){
//                                Log.d(TAG, "onResponse P1: "+p1.getBadgeQuantity());
//                            }
                            sewingSizeRecyclerAdapter.notifyDataSetChanged();
                        } else if(type.equals("ALTER")) {
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getAlter());
                                    }
                                }
                            }
//                            for(V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList){
//                                Log.d(TAG, "onResponse P2: "+p2.getAlter());
//                            }
//                            for(V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList){
//                                Log.d(TAG, "onResponse P1: "+p1.getBadgeQuantity());
//                            }

                            sewingSizeRecyclerAdapter.notifyDataSetChanged();
                        }else if(type.equals("SPOT")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getSpot());
                                    }
                                }
                            }
                            sewingSizeRecyclerAdapter.notifyDataSetChanged();
                        }else if(type.equals("RECTIFIED")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getRectified());
                                    }
                                }
                            }
                            sewingSizeRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<V2_TypeWiseInOutResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void requestForRectifiedData() {
        _inputQtyTV.setText("0");
        _outputQtyTv.setText("0");
        modelArrayList.get(0).setColor_size(0);
        for(int i =0; i<poColorWiseSizeArrayList.size(); i++){
            poColorWiseSizeArrayList.get(i).setSelectedItem(false);
        }
        sewingSizeRecyclerAdapter.notifyDataSetChanged();

        Log.d(TAG, "requestForRectifiedData: "+selectedColorID+" :"+ lineId +" : "+userId +" : "+po_breakDownId +" : "+item_numberId+" : "+jobId);
        apiInterface.getDefectedListForRectifiedModelCall_V5(selectedColorID, String.valueOf(lineId), userId, po_breakDownId, item_numberId, jobId).enqueue(new Callback<V1_DefectListOfRectifiedModel_v5>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<V1_DefectListOfRectifiedModel_v5> call, Response<V1_DefectListOfRectifiedModel_v5> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    defectedRectifiedModels.clear();
                    if(response.body().getData() != null){
                        for(int i=0; i<response.body().getData().size(); i++){
                            if(response.body().getData().get(0).getMstId().equals("0")){
                                defectedRectifiedModels.clear();
                                break;
                            }else{
                                V1_DefectedRectifiedModel defectedRectifiedModel = new V1_DefectedRectifiedModel();
                                defectedRectifiedModel.setMstId(response.body().getData().get(i).getMstId());
                                defectedRectifiedModel.setDtlsId(response.body().getData().get(i).getDtlsId());
                                defectedRectifiedModel.setOperationName(response.body().getData().get(i).getOperationName());
                                defectedRectifiedModel.setOperationId(response.body().getData().get(i).getOperationId());
                                defectedRectifiedModel.setAlterQty(response.body().getData().get(i).getAlterQty());
                                defectedRectifiedModel.setSpotQty(response.body().getData().get(i).getSpotQty());
//                                defectedRectifiedModel.setRectifiedQty(response.body().getData().get(i).getRectifiedQty());
                                defectedRectifiedModel.setProductionDate(response.body().getData().get(i).getProductionDate());
                                defectedRectifiedModel.setColorSizedId(response.body().getData().get(i).getColSizeId());
                                defectedRectifiedModel.setSizeId(response.body().getData().get(i).getSizeId());
                                defectedRectifiedModel.setColorId(response.body().getData().get(i).getColorId());
                                defectedRectifiedModel.setDefectNames(response.body().getData().get(i).getDefectNames());
//                                defectedRectifiedModel.setOperationId(response.body().getData().get(i).getOperationName());
                                defectedRectifiedModel.setDtls_piece_id(response.body().getData().get(i).getDtlsPieceId());
                                defectedRectifiedModel.setColorName(response.body().getData().get(i).getColorName());
                                defectedRectifiedModel.setSizeName(response.body().getData().get(i).getSizeName());
                                int alterSpot = Integer.parseInt(response.body().getData().get(i).getAlterQty()) +  Integer.parseInt(response.body().getData().get(i).getSpotQty());
                                int rectified = Integer.parseInt(response.body().getData().get(i).getRectifiedQty());
                                int diff = alterSpot - rectified;
//                                defectedRectifiedModel.setDefectCount(String.valueOf(diff));
                                defectedRectifiedModel.setSelect(false);
                                defectedRectifiedModels.add(defectedRectifiedModel);
                            }
                        }
                        _rectifiedSelectCheckbox.setText("SELECT ALL");
                        _rectifiedSelectCheckbox.setChecked(false);
                    }
                    initRectifiedDefectRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<V1_DefectListOfRectifiedModel_v5> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(V1_StyleWiseSewingActivity_v5.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRectifiedDefectRecyclerView() {
        if(defectedRectifiedModels.size() > 0){
            _rectifiedSelectCheckbox.setVisibility(View.VISIBLE);
            _rectifiedLinearLayout.setVisibility(View.VISIBLE);
            _errorRectifiedDataSetLayout.setVisibility(View.GONE);
        }else{
            _rectifiedSelectCheckbox.setVisibility(View.VISIBLE);
            _errorRectifiedDataSetLayout.setVisibility(View.VISIBLE);
            _rectifiedLinearLayout.setVisibility(View.VISIBLE);
        }

        Collections.sort(defectedRectifiedModels, V1_DefectedRectifiedModel.nameComparator);
        defectedRectifiedRecyclerViewAdapter = new V1_DefectedRectifiedRecyclerViewAdapter( this, defectedRectifiedModels,  this);
        defectRectifiedRecyclerView.setAdapter(defectedRectifiedRecyclerViewAdapter);
        defectRectifiedRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        defectedRectifiedRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void operationPopup() {
        initSizeRecyclerView();
        reject_defect = "";
        alter_defect = "";
        spot_defect = "";
        if(good_number == 1){
            operationListCard.setVisibility(View.GONE);
            defectListCard.setVisibility(View.GONE);
            rectifiedListCard.setVisibility(View.GONE);
            dataResetMethod();
        } else if(rectified_number == 1){
            _defectLayout.setVisibility(View.GONE);
            _operationLayout.setVisibility(View.VISIBLE);
            defectListCard.setVisibility(View.GONE);
            operationListCard.setVisibility(View.GONE);
            _operationLinearLayout.setVisibility(View.GONE);
            rectifiedListCard.setVisibility(View.VISIBLE);
            requestForRectifiedData();
        } else if(reject_number == 1) {
            if (sizeSelection != -1){
                _operationLayout.setVisibility(View.GONE);
                defectListCard.setVisibility(View.VISIBLE);
                _defectLayout.setVisibility(View.VISIBLE);
                _operationNameTV.setText("");
            } else {
                showAlertMessage("Size not selected", 0);
            }
        } else {
            rectifiedListCard.setVisibility(View.GONE);
            _defectLayout.setVisibility(View.GONE);
            _operationLayout.setVisibility(View.VISIBLE);
            defectListCard.setVisibility(View.VISIBLE);
            operationListCard.setVisibility(View.VISIBLE);
            _operationLinearLayout.setVisibility(View.VISIBLE);
        }
        defectRecyclerView.setLayoutManager(linearLayoutManagerDefect);

        if(defectType.equals("A")) {
            sewingOutputAlterDefectRecyclerAdapter = new V1_SewingOutputAlterDefectRecyclerAdapter(sewingAlterModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputAlterDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("S")){
            sewingOutputSpotDefectRecyclerAdapter = new V1_SewingOutputSpotDefectRecyclerAdapter(sewingSpotModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputSpotDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("R")) {
            sewingOutputRejectDefectRecyclerAdapter = new V1_SewingOutputRejectDefectRecyclerAdapter(sewingRejectModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputRejectDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputRejectDefectRecyclerAdapter.notifyDataSetChanged();
        }
        initOperationRecyclerView();
    }

    private void dataResetMethod() {
        _rectifiedSelectCheckbox.setText("SELECT ALL");
        for(int i=0; i<operationItemModelList.size(); i++){
            operationItemModelList.get(i).setStatus(true);
        }

//        if(colorSelection != -1){
//            Toast.makeText(V1_StyleWiseSewingActivity_v2.this, String.valueOf(poWiseColorSelection), Toast.LENGTH_SHORT).show();
//            if(poWiseColorSelection != -1){
//                colorArrayList.get(poWiseColorSelection).setColor_output_qnty(saved_color_wise_check_qty);
//                poWiseColorArrayList.get(colorSelection).setColor_output_qnty(colorArrayList.get(poWiseColorSelection).getColor_output_qnty());
//                Toast.makeText(this, String.valueOf(poWiseColorSelection) + colorArrayList.get(poWiseColorSelection).getColor_output_qnty(), Toast.LENGTH_SHORT).show();
////                poWiseColorArrayList.get(colorSelection).setColor_output_qnty(colorArrayList.get(poWiseColorSelection).getColor_output_qnty());
//            }
//
//            if(colorArrayList.get(colorSelection).getPo_break_down_id().equals(po_breakDownId) && colorArrayList.get(colorSelection).getColourId().equals(selectedColorID)) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    int index = IntStream.range(0, colorArrayList.size())
//                            .filter(i -> colorArrayList.get(i).getColourId().equals(selectedColorID) && colorArrayList.get(i).getPo_break_down_id().equals(po_breakDownId))
//                            .findFirst()
//                            .orElse(-1);
////                    poWiseColorArrayList.get(colorSelection).setColor_output_qnty(colorArrayList.get(poWiseColorSelection).getColor_output_qnty());
//                }
//            }
//            for(int i = 0; i < colorArrayList.size(); i++){
//                colorName[i] = colorArrayList.get(i).getColourName() +" - "+colorArrayList.get(i).getColor_output_qnty();
//                colorId[i] = colorArrayList.get(i).getColourId();
//            }
//            colorSpinnerAdapter.notifyDataSetChanged();
//        }

        rectifiedListCard.setVisibility(View.GONE);
        initSizeRecyclerView();
        initOperationRecyclerView();

        for(int i=0; i<sewingAlterModels.size(); i++){
            sewingAlterModels.get(i).setDefectCount("0");
        }
        for(int i=0; i<sewingRejectModels.size(); i++){
            sewingRejectModels.get(i).setDefectCount("0");
        }
        for(int i=0; i<sewingSpotModels.size(); i++){
            sewingSpotModels.get(i).setDefectCount("0");
        }

        if(defectType.equals("A")) {
            sewingOutputAlterDefectRecyclerAdapter = new V1_SewingOutputAlterDefectRecyclerAdapter(sewingAlterModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputAlterDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("S")){
            sewingOutputSpotDefectRecyclerAdapter = new V1_SewingOutputSpotDefectRecyclerAdapter(sewingSpotModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputSpotDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("R")) {
            sewingOutputRejectDefectRecyclerAdapter = new V1_SewingOutputRejectDefectRecyclerAdapter(sewingRejectModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputRejectDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            sewingOutputRejectDefectRecyclerAdapter.notifyDataSetChanged();
        }

        if(operationItemModelList.size() > 0 && sewingAlterModels.size() > 0 && sewingRejectModels.size() > 0 && sewingSpotModels.size() > 0){
            _defectLayout.setVisibility(View.GONE);
            _operationLayout.setVisibility(View.VISIBLE);
            operationListCard.setVisibility(View.GONE);
            defectListCard.setVisibility(View.GONE);
            for(int i=0; i<operationItemModelList.size(); i++){
                operationItemModelList.get(i).setStatus(false);
            }
        }
        alter_defect = "";
        spot_defect = "";
        reject_defect = "";
    }

    @Override
    public void onAlterDefectHeadClick(int position, View v) {

    }

    @Override
    public void onRejectDefectHeadClick(int position, View v) {

    }

    @Override
    public void onSpotDefectHeadClick(int position, View v) {

    }

    @Override
    public void onRectifiedHeadClick(int position, View v) {
        String dtls_peace_id = defectedRectifiedModels.get(position).getDtls_piece_id();
        if(defectedRectifiedModels.get(position).getSelect()){
            defectedRectifiedModels.get(position).setSelect(false);
            if(defectedRectifiedModels.get(position).getDtls_piece_id().equals(dtls_peace_id)){
                for(int i=0; i<defectedRectifiedModels.size(); i++){
                    if(defectedRectifiedModels.get(i).getDtls_piece_id().equals(dtls_peace_id)){
                        defectedRectifiedModels.get(i).setSelect(false);
                    }
                }
            }
        }else{
            defectedRectifiedModels.get(position).setSelect(true);
            if(defectedRectifiedModels.get(position).getDtls_piece_id().equals(dtls_peace_id)){
                for(int i=0; i<defectedRectifiedModels.size(); i++){
                    if(defectedRectifiedModels.get(i).getDtls_piece_id().equals(dtls_peace_id)){
                        defectedRectifiedModels.get(i).setSelect(true);
                    }
                }
            }
        }

        defectedRectifiedRecyclerViewAdapter.notifyDataSetChanged();
        int status = 0;
        for(int i=0; i<defectedRectifiedModels.size(); i++){
            if(defectedRectifiedModels.get(i).getSelect()){
                status += 1;
            }
        }
        if(defectedRectifiedModels.size() == status){
            _rectifiedSelectCheckbox.setText("UNSELECT ALL");
            _rectifiedSelectCheckbox.setChecked(true);
        }else{
            allSelectPosition = true;
            _rectifiedSelectCheckbox.setText("SELECT ALL");
            _rectifiedSelectCheckbox.setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        colorArrayList.clear();
        poColorWiseSizeArrayList.clear();
        hideDialog();
        finish();
    }
}