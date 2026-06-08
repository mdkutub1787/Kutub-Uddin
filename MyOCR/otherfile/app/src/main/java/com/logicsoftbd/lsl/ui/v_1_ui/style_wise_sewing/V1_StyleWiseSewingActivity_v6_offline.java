package com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ColorBreakDownResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigSewingOperationModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectListOfRectifiedModel_v5;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectedRectifiedModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DefectedRectifiedModel_V4;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GrossSewingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ProVariableResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingRejectModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V2_ColorPOWiseCountryModel;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_StyleWiseSewingActivity_v6_offline extends AppCompatActivity implements View.OnClickListener,
        V1_SewingOutputAlterDefectRecyclerAdapter.OnAlterDefectSelectListener,
        V1_SewingOutputSpotDefectRecyclerAdapter.OnSpotDefectSelectListener,
        V1_SewingOutputRejectDefectRecyclerAdapter.OnRejectDefectSelectListener,
        V1_StyleWiseSewingOperationRecyclerAdapter.OnOperationSelectListener,
        V1_StyleWiseSewingColorRecyclerAdapter.OnColorSelectListener,
        V2_StyleWiseSewingGoodQualitySizeRecyclerAdapter.OnSizeSelectListener,
        V2_StyleWiseSewingSizeRecyclerAdapter.OnSizeSelectListener,
        V1_DefectedRectifiedRecyclerViewAdapter.OnRectifiedHeadListener,
        V1_DefectedRectifiedRecyclerViewAdapter_v4.OnGmtsRectifiedHeadListener{
    private static final String TAG = "V1_StyleWiseSewingActiv";
    private ProgressDialog pDialog;
    private RecyclerView operationRecyclerView, defectRecyclerView, colorRecyclerView, sizeRecyclerView, defectRectifiedRecyclerView, defectGmtsRectifiedRecyclerView;
    private SharedPreferences _preferences;

    private List<V1_StyleWiseOperationResponse.Color> colorArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.Size> sizeArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.PoId> poIdArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.Country> countryArrayList = new ArrayList<>();
    private List<V2_POWiseColorModel> poWiseColorArrayList = new ArrayList<>();
    private List<V2_ColorWisePOModel> colorWisePOModels = new ArrayList<>();
    private List<V2_ColorPOWiseCountryModel> colorPOWiseCountryModels = new ArrayList<>();
    private List<V2_POColorWiseSizeModel> poColorWiseSizeArrayList = new ArrayList<>();
    private List<V2_TypeWiseInOutModel> typeWiseInOutModelsArrayList = new ArrayList<>();
    private List<V1_StyleWiseOperationResponse.Operation> operationArrayList = new ArrayList<>();
    public String[] colorName, colorId;

    private static ArrayList<V1_SewingAlterModel> sewingAlterModels = new ArrayList<>();
    private static ArrayList<V1_SewingSpotModel> sewingSpotModels = new ArrayList<>();
    private static ArrayList<V1_SewingRejectModel> sewingRejectModels = new ArrayList<>();
    public static ArrayList<V1_DefectedRectifiedModel> defectedRectifiedModels = new ArrayList<>();
    public static ArrayList<V1_DefectedRectifiedModel_V4> defectedRectifiedModels_v4 = new ArrayList<>();
    public static ArrayList<V1_DefectedRectifiedModel> uniqueDefectedRectifiedModels = new ArrayList<>();

    public static ArrayList<V1_SewingOutputModelClass> modelArrayList;
    private V1_SewingOutputModelClass inputModelClass;

    private V1_StyleWiseSewingOperationRecyclerAdapter sewingOutputOperationRecyclerAdapter;
    private V1_SewingOutputAlterDefectRecyclerAdapter sewingOutputAlterDefectRecyclerAdapter;
    private V1_SewingOutputSpotDefectRecyclerAdapter sewingOutputSpotDefectRecyclerAdapter;
    private V1_SewingOutputRejectDefectRecyclerAdapter sewingOutputRejectDefectRecyclerAdapter;
    private V1_DefectedRectifiedRecyclerViewAdapter defectedRectifiedRecyclerViewAdapter;
    private V1_DefectedRectifiedRecyclerViewAdapter_v4 defectedRectifiedRecyclerViewAdapter_v4;
    private V1_StyleWiseSewingColorRecyclerAdapter sewingColorRecyclerAdapter;
    private V2_StyleWiseSewingSizeRecyclerAdapter sewingSizeRecyclerAdapter;
    private V2_StyleWiseSewingGoodQualitySizeRecyclerAdapter sewingGoodQualitySizeRecyclerAdapter;
    private ArrayAdapter<String> adapterColor;
    private V3_ColorSpinnerAdapter colorSpinnerAdapter;
    private V2_POSpinnerAdapter POSpinnerAdapter;
    private V2_CountrySpinnerAdapter countrySpinnerAdapter;
    private List<V1_ConfigSewingOperationModel> operationItemModelList = new ArrayList<>();
    private Set<String> idsSeen = new HashSet<>();
    private List<V1_StyleWiseOperationResponse.Color> uniqueColorList = new ArrayList<>();
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Button backButton, nextButton, _operationButton, _saveBT;
    private ImageView _imgBack, _undoButton, _onlineStatusIcon;
    private TextView _lineTV, _styleTV, _internalRefTV, _itemTV, _countryTV, _inputQtyTV, _outputQtyTv, _checkQtyTV, _colorWiseOutputTV, _buyerInfoTV, _jobInfoTV, _onlineStatusTV;
    private RadioGroup _radioGroup;
    private RadioButton _goodRB, _rejectRB, _alterRB, _spotRB, _rectifiedRB;
    private CheckBox _rectifiedSelectCheckbox, _gmtsRectifiedSelectCheckbox;
    private TextView _goodTV, _rejectTV, _alterTV, _spotTV, _rectifiedTV, _operationNameTV, _itemCountTV;
    private RelativeLayout _imageLayout;
    private CardView operationListCard, defectListCard, rectifiedListCard, gmtsRectifiedListCard;
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
    private String base_url = "", prefix_url = "", logo_location ="", macAddress;
    private String defectType = "G";
    private String action_type = "GOOD";
    private String operationJson = "", UPDATE_ID, UPDATE_DTLS_ID, UPDATE_DTLS_PIECE_ID, DELETE_DATA_TYPE, undo_type = "";
    private String po_breakDownId, colorWisePo_breakDownId, item_numberId, countryId = "", selected_operation_id = "0";
    private Integer selectedColor = 0;
    private Spinner colorSpinner, poSpinner, countrySpinner;
    private boolean undoStatus = false, allSelectPosition = false, defectEntryStatus = false;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleTimeFormat;
    private LinearLayout _operationLayout, _defectLayout, _sizeLayout, _rectifiedLinearLayout, _errorRectifiedDataSetLayout, _gmtsRectifiedLinearLayout, _errorGmtsRectifiedDataSetLayout, _operationLinearLayout;
    private LinearLayoutManager linearLayoutManager, linearLayoutManagerDefect, linearLayoutManagerOperation;
    private VerticalSpacingItemDecorator itemDecorator, itemDecoratorDefect;
    private ImageView _logoImg;
    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;
    private int pro_company_id, pro_position, pro_v4_status = 0;
    private int proDefectTypeGood = 1;
    private List<V1_ProVariableResponse> proVariables;

    private Map<String, String> mapOfAlterArrayLists = new HashMap<>();
    private Map<String, String> mapOfSpotArrayLists = new HashMap<>();
    Gson gson = new Gson();
    private String  preSelectAlterJsonString_, preSelectSpotJsonString_ = "";
    private Handler handler;
    private Runnable runnable;
    private V1_StyleWiseOperationResponse styleWiseOperationOfflineDataResponse = new V1_StyleWiseOperationResponse();
    private ArrayList<String> jsonObjectArrayOnline = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_style_wise_sewing_v6_offline);
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        logo_location = (_preferences.getString("logo_location", ""));

        String[] spitesBaseUrl = base_url.split("/");
        Log.d(TAG, "initUI: "+spitesBaseUrl.length);

        if(spitesBaseUrl.length == 8){
            prefix_url = spitesBaseUrl[0]+"/"+spitesBaseUrl[1]+"/"+spitesBaseUrl[2]+"/"+spitesBaseUrl[3];
        }else{
            prefix_url = spitesBaseUrl[0]+"/"+spitesBaseUrl[2];
        }

        String proVariableArrayList = _preferences.getString("proVariableArrayList","");
        Log.d(TAG, "onCreate ####: __________"+proVariableArrayList+" "+prefix_url);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            proVariables = objectMapper.readValue(proVariableArrayList, new TypeReference<List<V1_ProVariableResponse>>(){});
            Log.d(TAG, "onCreate ####" + proVariables.get(0).getCompanyId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        //set Time
        simpleTimeFormat = new SimpleDateFormat("HH-mm");
        currentTime = simpleTimeFormat.format(calendar.getTime());

        init_ui();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void checkInternetConnection() throws JSONException {
        if (isInternetAvailable()) {
            _onlineStatusIcon.setImageResource(R.drawable.online_rounded_button);
            _onlineStatusTV.setText("ONLINE");
            _onlineStatusTV.setTextColor(getResources().getColor(R.color.green_500));
            if(loadOfflineSavedArrayList().size() > 0){
                saveOfflineObjectToServerWhenOnline(loadOfflineSavedArrayList());
            }

        } else {
            _onlineStatusIcon.setImageResource(R.drawable.offline_rounded_button);
            _onlineStatusTV.setText("OFFLINE");
            _onlineStatusTV.setTextColor(getResources().getColor(R.color.red));        }
    }

    private ArrayList<String> loadOfflineSavedArrayList() {
        String jsonString  = (_preferences.getString("save_offline_build_object", null));

        if (jsonString != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(jsonString, type);
        } else {
            return new ArrayList<>();
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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
        defectGmtsRectifiedRecyclerView = findViewById(R.id.defectGmtsRectifiedRecyclerView);
        _operationLayout = findViewById(R.id.operationLayout);
        _defectLayout = findViewById(R.id.defectLayout);
        _sizeLayout = findViewById(R.id.sizeLayout);
        _errorRectifiedDataSetLayout = findViewById(R.id.errorRectifiedDataSetLayout);
        _errorGmtsRectifiedDataSetLayout = findViewById(R.id.errorGmtsRectifiedDataSetLayout);
        _operationLinearLayout = findViewById(R.id.operationLinearLayout);
        _rectifiedLinearLayout = findViewById(R.id.rectifiedLinearLayout);
        _gmtsRectifiedLinearLayout = findViewById(R.id.gmtsRectifiedLinearLayout);
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
        _onlineStatusTV = findViewById(R.id.onlineStatusTV);
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
        _gmtsRectifiedSelectCheckbox = findViewById(R.id.gmtsRectifiedSelectCheckbox);

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
        gmtsRectifiedListCard = findViewById(R.id.gmtsRectifiedListCard);
        colorSpinner = findViewById(R.id.colorSpinner);
        poSpinner = findViewById(R.id.POSpinner);
        countrySpinner = findViewById(R.id.countrySpinner);

        _goodRB.setChecked(true);
        good_number = 1;

        _imgBack = findViewById(R.id.back);
        _imgBack.setOnClickListener(this);
        _onlineStatusIcon = findViewById(R.id.onlineStatusIcon);
        _undoButton = findViewById(R.id.undoButton);
        _undoButton.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);

        _logoImg = findViewById(R.id.logoImg);
        imageArray = new int[2];
        imageArray[0] = R.drawable.logic;
//        imageArray[1] = R.drawable.logic;
//        imageArray[1] = R.drawable.micro_fiber;
//        imageArray[1] = R.drawable.evince;
//        imageArray[1] = R.drawable.logo_jk;
//        imageArray[1] = R.drawable.kac;
        imageArray[1] = R.drawable.asrotex;
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

        for(int i=0; i<proVariables.size(); i++){
            if(proVariables.get(i).getCompanyId() == companyId){
                pro_position = i;
                break;
            }
        }

        if(proVariables.get(pro_position).getSpot().getDefect() == 2){
            pro_v4_status = 1;
        }else if(proVariables.get(pro_position).getAlter().getDefect() == 2){
            pro_v4_status = 1;
        }

//        for(int i=0; i<proVariables.size(); i++){
//
//        }
        Log.d(TAG, "init_ui: "+pro_position);

        _lineTV.setText(_preferences.getString("lineName", "").toUpperCase());
        setTextViewGradientColor(_lineTV);
        _styleTV.setText(_preferences.getString("style", "").toUpperCase());
        setTextViewGradientColor(_styleTV);
        _internalRefTV.setText(_preferences.getString("irNumber", "").toUpperCase());
        setTextViewGradientColor(_internalRefTV);
        _itemTV.setText(_preferences.getString("itemName", "").toUpperCase());
        setTextViewGradientColor(_itemTV);
        _buyerInfoTV.setText(_preferences.getString("buyerName", "").toUpperCase());
        setTextViewGradientColor(_buyerInfoTV);
        _jobInfoTV.setText(_preferences.getString("jobNo", "").toUpperCase());
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

        if(isInternetAvailable()){
            requestStyleWiseOperation();
            operationPopup();
        }else{
            requestStyleWiseOperation_offline(_preferences.getString("style_wise_operation", ""));

            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(styleWiseOperationOfflineDataResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "init_ui: ##########"+json);

        }


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

        _gmtsRectifiedSelectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                _gmtsRectifiedSelectCheckbox.setText("UNSELECT ALL");
                for(int i=0; i<defectedRectifiedModels_v4.size(); i++){
                    defectedRectifiedModels_v4.get(i).setSelect(true);
                }
                allSelectPosition = false;
            }else{
                if(!allSelectPosition){
                    _gmtsRectifiedSelectCheckbox.setText("SELECT ALL");
                    for(int i=0; i<defectedRectifiedModels_v4.size(); i++){
                        defectedRectifiedModels_v4.get(i).setSelect(false);
                    }
                }
            }
            defectedRectifiedRecyclerViewAdapter_v4.notifyDataSetChanged();
        });
    }

    public void nextImage(){
        _logoImg.setImageResource(imageArray[currentIndex]);
//        Picasso.get()
//                .load(prefix_url+"/"+logo_location)
//                .placeholder(R.drawable.logic) // Default image placeholder
//                .error(R.drawable.logic) // Error image (same as default for simplicity)
//                .into(_logoImg);
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
                        Log.d(TAG, "onResponse: operation data"+response.toString());
                        if(response.isSuccessful()){

                            SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_StyleWiseSewingActivity_v6_offline.this);
                            SharedPreferences.Editor _editor = _preferences.edit();
                            _editor.putString("style_wise_operation", new Gson().toJson(response.body()));
                            _editor.commit();

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

                            if(response.body().getResultset().getCountry() != null && response.body().getResultset().getCountry().size() > 0){
                                countryArrayList =  response.body().getResultset().getCountry();
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
                                    sewingAlterModel.setDefectSerialNo(response.body().getResultset().getAlterList().get(i).getDefectSerialNo());

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
                                    sewingRejectModel.setDefectSerialNo(response.body().getResultset().getRejectList().get(i).getDefectSerialNo());
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
                                    sewingSpotModel.setDefectSerialNo(response.body().getResultset().getSpotList().get(i).getDefectSerialNo());
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

    private void requestStyleWiseOperation_offline(String styleWiseOperation) {
        styleWiseOperationOfflineDataResponse = new Gson().fromJson(styleWiseOperation, V1_StyleWiseOperationResponse.class);

        Log.d(TAG, "onResponse: "+styleWiseOperationOfflineDataResponse.toString());
        if(styleWiseOperationOfflineDataResponse.getResultset().getColor() != null && styleWiseOperationOfflineDataResponse.getResultset().getColor().size() > 0){
            colorArrayList =  styleWiseOperationOfflineDataResponse.getResultset().getColor();
            colorName = new String[colorArrayList.size()];
            colorId = new String[colorArrayList.size()];
            for(int i = 0; i < colorArrayList.size(); i++){
                colorName[i] = colorArrayList.get(i).getColourName() +" - "+colorArrayList.get(i).getColor_output_qnty();
                colorId[i] = colorArrayList.get(i).getColourId();
            }
            initColorSpinnerView();
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getSize() != null && styleWiseOperationOfflineDataResponse.getResultset().getSize().size() > 0){
            sizeArrayList =  styleWiseOperationOfflineDataResponse.getResultset().getSize();
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getCountry() != null && styleWiseOperationOfflineDataResponse.getResultset().getCountry().size() > 0){
            countryArrayList =  styleWiseOperationOfflineDataResponse.getResultset().getCountry();
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getPoIds() != null && styleWiseOperationOfflineDataResponse.getResultset().getPoIds().size() > 0){
            poIdArrayList =  styleWiseOperationOfflineDataResponse.getResultset().getPoIds();
//                                initPOSpinnerView();
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getAlterList() != null && styleWiseOperationOfflineDataResponse.getResultset().getAlterList().size() > 0){
            sewingAlterModels.clear();
            for(int i=0; i<styleWiseOperationOfflineDataResponse.getResultset().getAlterList().size(); i++){
                V1_SewingAlterModel sewingAlterModel = new V1_SewingAlterModel();
                String sentence = styleWiseOperationOfflineDataResponse.getResultset().getAlterList().get(i).getName();
                boolean isNumber = sentence.matches("\\d+");
                if(isNumber){
                    sewingAlterModel.setDefectName(styleWiseOperationOfflineDataResponse.getResultset().getAlterList().get(i).getName());
                }else{
                    String[] words = sentence.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (String word : words) {
                        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                    }
                    String capitalizedSentence = sb.toString().trim();
                    sewingAlterModel.setDefectName(capitalizedSentence);
                }
                sewingAlterModel.setId(styleWiseOperationOfflineDataResponse.getResultset().getAlterList().get(i).getId());
                sewingAlterModel.setDefectSerialNo(styleWiseOperationOfflineDataResponse.getResultset().getAlterList().get(i).getDefectSerialNo());

                sewingAlterModel.setDefectCount(String.valueOf(0));
                sewingAlterModel.setDefectSelect(false);
                sewingAlterModels.add(sewingAlterModel);
            }
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getRejectList() != null && styleWiseOperationOfflineDataResponse.getResultset().getRejectList().size() > 0){
            sewingRejectModels.clear();
            for(int i=0; i<styleWiseOperationOfflineDataResponse.getResultset().getRejectList().size(); i++){
                V1_SewingRejectModel sewingRejectModel = new V1_SewingRejectModel();
                String sentence = styleWiseOperationOfflineDataResponse.getResultset().getRejectList().get(i).getName();
                boolean isNumber = sentence.matches("\\d+");
                if(isNumber){
                    sewingRejectModel.setDefectName(styleWiseOperationOfflineDataResponse.getResultset().getRejectList().get(i).getName());
                }else{
                    String[] words = sentence.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (String word : words) {
                        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                    }
                    String capitalizedSentence = sb.toString().trim();
                    sewingRejectModel.setDefectName(capitalizedSentence);
                }
                sewingRejectModel.setId(styleWiseOperationOfflineDataResponse.getResultset().getRejectList().get(i).getId());
                sewingRejectModel.setDefectSerialNo(styleWiseOperationOfflineDataResponse.getResultset().getRejectList().get(i).getDefectSerialNo());
                sewingRejectModel.setDefectCount(String.valueOf(0));
                sewingRejectModel.setDefectSelect(false);
                sewingRejectModels.add(sewingRejectModel);
            }
        }

        if(styleWiseOperationOfflineDataResponse.getResultset().getSpotList() != null && styleWiseOperationOfflineDataResponse.getResultset().getSpotList().size() > 0){
            sewingSpotModels.clear();
            for(int i=0; i<styleWiseOperationOfflineDataResponse.getResultset().getSpotList().size(); i++){
                V1_SewingSpotModel sewingSpotModel = new V1_SewingSpotModel();
                String sentence = styleWiseOperationOfflineDataResponse.getResultset().getSpotList().get(i).getName();
                boolean isNumber = sentence.matches("\\d+");
                if(isNumber){
                    sewingSpotModel.setDefectName(styleWiseOperationOfflineDataResponse.getResultset().getSpotList().get(i).getName());
                }else{
                    String[] words = sentence.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (String word : words) {
                        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                    }
                    String capitalizedSentence = sb.toString().trim();
                    sewingSpotModel.setDefectName(capitalizedSentence);
                }
                sewingSpotModel.setId(styleWiseOperationOfflineDataResponse.getResultset().getSpotList().get(i).getId());
                sewingSpotModel.setDefectSerialNo(styleWiseOperationOfflineDataResponse.getResultset().getSpotList().get(i).getDefectSerialNo());
                sewingSpotModel.setDefectCount(String.valueOf(0));
                sewingSpotModel.setDefectSelect(false);
                sewingSpotModels.add(sewingSpotModel);
            }
        }
        initOperationRecyclerView();
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
                colorPOWiseCountryModels.clear();
                for(int i=0; i<countryArrayList.size(); i++){
                    if(countryArrayList.get(i).getColourId().equals(selectedColorID) && countryArrayList.get(i).getPoBreakDownId().equals(po_breakDownId)){
                        V2_ColorPOWiseCountryModel v2ColorPOWiseCountryModel = new V2_ColorPOWiseCountryModel();
                        v2ColorPOWiseCountryModel.setCountryName(String.valueOf(countryArrayList.get(i).getCountryName()));
                        v2ColorPOWiseCountryModel.setPoBreakDownId(String.valueOf(countryArrayList.get(i).getPoBreakDownId()));
                        v2ColorPOWiseCountryModel.setCountryId(String.valueOf(countryArrayList.get(i).getCountryId()));
                        v2ColorPOWiseCountryModel.setCountryOutputQnty(String.valueOf(countryArrayList.get(i).getCountryOutputQnty()));
                        v2ColorPOWiseCountryModel.setCountryId(String.valueOf(countryArrayList.get(i).getCountryId()));
                        colorPOWiseCountryModels.add(v2ColorPOWiseCountryModel);
                    }
                }
                initCountrySpinnerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCountrySpinnerView() {
        countrySpinnerAdapter = new V2_CountrySpinnerAdapter(this, colorPOWiseCountryModels);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = colorPOWiseCountryModels.get(position).getCountryId();

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

        Log.d(TAG, "initSizeRecyclerView: "+poColorWiseSizeArrayList.size() +":"+ proDefectTypeGood);
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
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Leave this Page?");
        builder.setMessage("Do you want to leave this page? Unsaved changes will not be available.");
        builder.setPositiveButton("Yes", (dialog, id) -> finish());
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.show();
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

                    prepareDefectData();
                }
                break;
            case R.id.undoButton:
                undoStatus = true;
                saveDataAction();
                break;
        }
    }

    private void prepareDefectData() {
        if(defectType.equals("A")) {
            preSelectAlterJsonString_ = gson.toJson(sewingAlterModels);

            for(int i=0; i<sewingAlterModels.size(); i++){
                if(Integer.valueOf(sewingAlterModels.get(i).getDefectCount()) > 0)
                {
                    defectEntryStatus = true;
                    break;
                }
            }
            if(defectEntryStatus){
                mapOfAlterArrayLists.put(selected_operation_id, preSelectAlterJsonString_);
                defectEntryStatus = false;
            }
        }
        if(defectType.equals("S")) {
            preSelectSpotJsonString_ = gson.toJson(sewingSpotModels);

            for(int i=0; i<sewingSpotModels.size(); i++){
                if(Integer.valueOf(sewingSpotModels.get(i).getDefectCount()) > 0)
                {
                    defectEntryStatus = true;
                    break;
                }
            }
            if(defectEntryStatus){
                mapOfSpotArrayLists.put(selected_operation_id, preSelectSpotJsonString_);
                defectEntryStatus = false;
            }
        }
    }

    private void saveDataAction(){
        int totalAlterSpot = Integer.parseInt(_alterTV.getText().toString()) + Integer.parseInt(_spotTV.getText().toString());
        int totalRectified = Integer.parseInt(_rectifiedTV.getText().toString());
        prepareDefectData();
        boolean alterDefectSelectStatus = false;
        boolean spotDefectSelectStatus = false;
        Log.d(TAG, "saveDataAction: after save"+alterDefectSelectStatus+" "+spotDefectSelectStatus);
        if(locationId != 0 && floorId != 0 && lineId != 0)
        {
            if(defectType.equals("A")) {
                spotDefectSelectStatus = true;
                String defectedAlter = "";
                ArrayList<String> defectString = new ArrayList<>();
                for (String key : mapOfAlterArrayLists.keySet()) {
                    String jsonString_ =  mapOfAlterArrayLists.get(key);

                    java.lang.reflect.Type type = new TypeToken<ArrayList<V1_SewingAlterModel>>() {}.getType();
                    ArrayList<V1_SewingAlterModel> currentList = gson.fromJson(jsonString_, type);

                    for(int i =0; i<currentList.size(); i++){
                        if(currentList.get(i).getDefectCount() != null && !currentList.get(i).getDefectCount().equals("") && !currentList.get(i).getDefectCount().equals("0")){
                            defectedAlter += key+"*"+currentList.get(i).getId()+"*"+ currentList.get(i).getDefectCount()+"__";
                            alterDefectSelectStatus = true;
                        }
                    }
                    Log.d(TAG, "onClick: ##########____####"+defectedAlter);
                    defectString.add(defectedAlter);
                    defectedAlter = "";
                }
                preSelectAlterJsonString_ = "";

                for(int i = 0; i < defectString.size(); i++){
                    alter_defect += defectString.get(i);
                    if(i<defectString.size()-1){
                        alter_defect += "$$$";
                    }
                }
                Log.d(TAG, "onClick: ##############______%%% "+alter_defect);
            }

            if(defectType.equals("S")) {
                alterDefectSelectStatus = true;
                String defectedSpot = "";
                ArrayList<String> defectString = new ArrayList<>();
                for (String key : mapOfSpotArrayLists.keySet()) {
                    String jsonString_ =  mapOfSpotArrayLists.get(key);

                    java.lang.reflect.Type type = new TypeToken<ArrayList<V1_SewingSpotModel>>() {}.getType();
                    ArrayList<V1_SewingSpotModel> currentList = gson.fromJson(jsonString_, type);

                    for(int i =0; i<currentList.size(); i++){
                        if(currentList.get(i).getDefectCount() != null && !currentList.get(i).getDefectCount().equals("") && !currentList.get(i).getDefectCount().equals("0")){
                            defectedSpot += key+"*"+currentList.get(i).getId()+"*"+ currentList.get(i).getDefectCount()+"__";
                            spotDefectSelectStatus = true;
                        }
                    }
                    Log.d(TAG, "onClick: ##########____####"+defectedSpot);
                    defectString.add(defectedSpot);
                    defectedSpot = "";
                }
                preSelectSpotJsonString_ = "";

                for(int i = 0; i < defectString.size(); i++){
                    spot_defect += defectString.get(i);
                    if(i<defectString.size()-1){
                        spot_defect += "$$$";
                    }
                }
                Log.d(TAG, "onClick: ##############______%%% "+spot_defect);
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
            else {
                if(rectified_number == 1){
                    if(pro_v4_status != 1){
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
                    }else{
                        try {
                            boolean selectedRectifiedStatus = false;
                            for(int i=0; i < defectedRectifiedModels_v4.size(); i++){
                                if(defectedRectifiedModels_v4.get(i).getSelect()){
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
        if(isInternetAvailable()){
            JSONObject jsonObject = buildJsonObject();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            showDialog();
            apiInterface.saveUpdateBundleSewingGrossOutputCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
                @Override
                public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                    hideDialog();
                    Log.d(TAG, "onResponse: "+response.toString());
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: #######"+response.body().getResultset().getStatus());
                        try {
                            countryManagement(response.body().getResultset().getCountry_id(), response.body().getResultset().isCountry_status());
                            orderManagement(response.body().getResultset().getOrder_id(), response.body().getResultset().getOrder_status());
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }
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
                                badgeDecrement();
                            } else {
                                _undoButton.setEnabled(true);
                                _undoButton.setImageResource(R.drawable.ic_baseline_undo_24);
                                badgeIncrement();
                            }

                            colorSpinnerBadge();
                            defauldGoodSelect(action_type);
                            showSuccessToastMessage(response.body().getResultset().getStatus());
                            defectedRectifiedModels.clear();
                            setDataForOffline();
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
        }else{
            setDataForOffline();
            JSONObject jsonObject = buildJsonObject();
            jsonObjectArrayOnline.add(jsonObject.toString());
            Gson gson = new Gson();
            String jsonString = gson.toJson(jsonObjectArrayOnline);
            SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_StyleWiseSewingActivity_v6_offline.this);
            SharedPreferences.Editor _editor = _preferences.edit();
            _editor.putString("save_offline_build_object", jsonString);
            _editor.apply();
            showSuccessToastMessage("ডেটা সফলভাবে অফলাইনে সংরক্ষণ করা হয়েছে।");
        }
    }

    private void saveOfflineObjectToServerWhenOnline(ArrayList<String> loadOfflineSavedArrayList) throws JSONException {
        JSONObject jsonObject = new JSONObject(loadOfflineSavedArrayList.get(loadOfflineSavedArrayList.size() - 1));
        Log.d(TAG, "saveOfflineObjectToServerWhenOnline: "+jsonObject);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();
        apiInterface.saveUpdateBundleSewingGrossOutputCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
            @Override
            public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: #######"+response.body().getResultset().getStatus());
                    try {
                        countryManagement(response.body().getResultset().getCountry_id(), response.body().getResultset().isCountry_status());
                        orderManagement(response.body().getResultset().getOrder_id(), response.body().getResultset().getOrder_status());
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
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
                            badgeDecrement();
                        } else {
                            _undoButton.setEnabled(true);
                            _undoButton.setImageResource(R.drawable.ic_baseline_undo_24);
                            badgeIncrement();
                        }

                        colorSpinnerBadge();
                        defauldGoodSelect(action_type);
                        showSuccessToastMessage(response.body().getResultset().getStatus());
                        defectedRectifiedModels.clear();
                        setDataForOffline();
                        loadOfflineSavedArrayList.remove(loadOfflineSavedArrayList.size() - 1);
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(loadOfflineSavedArrayList);
                        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_StyleWiseSewingActivity_v6_offline.this);
                        SharedPreferences.Editor _editor = _preferences.edit();
                        _editor.putString("save_offline_build_object", jsonString);
                        _editor.apply();

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

    private void setDataForOffline() {
        try {
            poColorWiseSizeArrayList.get(sizeSelection).setGood(_goodTV.getText().toString());
            poColorWiseSizeArrayList.get(sizeSelection).setReject(_rejectTV.getText().toString());
            poColorWiseSizeArrayList.get(sizeSelection).setAlter(_alterTV.getText().toString());
            poColorWiseSizeArrayList.get(sizeSelection).setSpot(_spotTV.getText().toString());
            poColorWiseSizeArrayList.get(sizeSelection).setRectified(_rectifiedTV.getText().toString());

            badgeIncrement_offline();
            defauldGoodSelect(action_type);
        }catch (Exception e){
            Log.d(TAG, "setDataForOffline: "+e.getMessage());
        }

        try {
            JSONObject jsonObject1 = buildJsonObject_style_wise_operation_offline();
            SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_StyleWiseSewingActivity_v6_offline.this);
            SharedPreferences.Editor _editor = _preferences.edit();
            _editor.putString("style_wise_operation", jsonObject1.toString());
            _editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void countryManagement(String countryId, boolean countryStatus) {
        if(!countryStatus){
            int position = -1;
            for (int i = 0; i < colorPOWiseCountryModels.size(); i++) {
                if (colorPOWiseCountryModels.get(i).getCountryId().equals(countryId) ) {
                    position = i;
                    break;
                }
            }
            if(colorPOWiseCountryModels.size() > 1){
                colorPOWiseCountryModels.remove(position);
                initCountrySpinnerView();
            }

            try {
                countrySpinnerAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "orderManagement: "+e.getMessage());
            }

        }
    }

    private void orderManagement(String orderId, Boolean orderStatus) {
        if(!orderStatus){
            int position = -1;
            for (int i = 0; i < colorWisePOModels.size(); i++) {
                if (colorWisePOModels.get(i).getPoId().equals(orderId)) {
                    position = i;
                    break;
                }
            }
            if(colorWisePOModels.size() > 1){
                colorWisePOModels.remove(position);
            }

            try {
                POSpinnerAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "orderManagement: "+e.getMessage());
            }

        }
    }

    private void colorSpinnerBadge() {
        try {
            uniqueColorList.get(colorSelection).setColor_output_qnty(saved_color_wise_check_qty);
            colorSpinnerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG, "colorSpinnerBadge: "+e.getMessage());
        }
    }
    private void postDataToServer_sewing_input_rectified() throws JSONException {
        if(pro_v4_status != 1){
            JSONObject jsonObject = buildJsonObject_rectified();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            showDialog();
            apiInterface.saveUpdateBundleSewingGrossOutputRectifiedCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
                @Override
                public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                    hideDialog();
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: #######"+response.toString());
                        try {
                            countryManagement(response.body().getResultset().getCountry_id(), response.body().getResultset().isCountry_status());
                            orderManagement(response.body().getResultset().getOrder_id(), response.body().getResultset().getOrder_status());
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }

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
        }else{
            JSONObject jsonObject = buildJsonObject_gmts_rectified();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            showDialog();
            apiInterface.saveUpdateBundleSewingGrossOutputGmtsRectifiedCall(body).enqueue(new Callback<V1_GrossSewingSaveResponse>() {
                @Override
                public void onResponse(Call<V1_GrossSewingSaveResponse> call, Response<V1_GrossSewingSaveResponse> response) {
                    hideDialog();
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: #######"+response.toString());
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
            try {
                poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
                sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "badgeIncrement: "+e.getMessage());
            }

        }else if(rectified_number==1){
            if(pro_v4_status != 1){
                for(int i=0; i<uniqueDefectedRectifiedModels.size(); i++){
                    if(uniqueDefectedRectifiedModels.get(i).getSelect()){
                        int index = -1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
            }else{
                for(int i=0; i<defectedRectifiedModels_v4.size(); i++){
                    if(defectedRectifiedModels_v4.get(i).getSelect()){
                        int index = -1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            int finalI = i;
                            index = IntStream.range(0, poColorWiseSizeArrayList.size())
                                    .filter(j -> poColorWiseSizeArrayList.get(j).getColorSizeBreakdownId().equals(defectedRectifiedModels_v4.get(finalI).getColSizeId()))
                                    .findFirst()
                                    .orElse(-1);
                        }
                        Log.d(TAG, "badgeIncrement: "+defectedRectifiedModels_v4.get(i).getColSizeId());
                        Log.d(TAG, "badgeIncrement Index: "+index);
                        try {
                            int count_prev = Integer.parseInt(poColorWiseSizeArrayList.get(index).getBadgeQuantity());
                            int count = Integer.parseInt(defectedRectifiedModels_v4.get(index).getDefectCount());
                            int new_count = count_prev + count;
                            poColorWiseSizeArrayList.get(index).setBadgeQuantity(String.valueOf(new_count));
                        }catch (Exception e){
                            Log.d(TAG, "badgeIncrement: "+e.getMessage());
                        }
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

    private void badgeIncrement_offline() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int index = IntStream.range(0, sizeArrayList.size())
                    .filter(i -> sizeArrayList.get(i).getColorSizeBreakdownId().equals(coloSizeBreakDownId))
                    .findFirst()
                    .orElse(-1);
//            sizeArrayList.get(index).setGood(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
        }

        if(action_type.equals("GOOD")){
            poColorWiseSizeArrayList.get(sizeSelection).setGood(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getGood()) + 1));
        }else if(action_type.equals("REJECT")){
            poColorWiseSizeArrayList.get(sizeSelection).setReject(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getReject()) + 1));
        }else if(action_type.equals("ALTER")){
            poColorWiseSizeArrayList.get(sizeSelection).setAlter(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getAlter()) + 1));
        }else if(action_type.equals("SPOT")){
            poColorWiseSizeArrayList.get(sizeSelection).setSpot(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getSpot()) + 1));
        }else if(action_type.equals("RECTIFIED")){
            poColorWiseSizeArrayList.get(sizeSelection).setRectified(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getRectified()) + 1));
        }

        setSideBadgeCountOffline(sizeSelection);

        savedColorSizeBreakDownId = String.valueOf(modelArrayList.get(0).getColor_size());
        savedSizeSelection = sizeSelection;
        if(good_number==1){
            try {
                poColorWiseSizeArrayList.get(sizeSelection).setBadgeQuantity(String.valueOf(Integer.parseInt(poColorWiseSizeArrayList.get(sizeSelection).getBadgeQuantity())+1));
                sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "badgeIncrement: "+e.getMessage());
            }

        }else if(rectified_number==1){
            if(pro_v4_status != 1){
                for(int i=0; i<uniqueDefectedRectifiedModels.size(); i++){
                    if(uniqueDefectedRectifiedModels.get(i).getSelect()){
                        int index = -1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
            }else{
                for(int i=0; i<defectedRectifiedModels_v4.size(); i++){
                    if(defectedRectifiedModels_v4.get(i).getSelect()){
                        int index = -1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            int finalI = i;
                            index = IntStream.range(0, poColorWiseSizeArrayList.size())
                                    .filter(j -> poColorWiseSizeArrayList.get(j).getColorSizeBreakdownId().equals(defectedRectifiedModels_v4.get(finalI).getColSizeId()))
                                    .findFirst()
                                    .orElse(-1);
                        }
                        Log.d(TAG, "badgeIncrement: "+defectedRectifiedModels_v4.get(i).getColSizeId());
                        Log.d(TAG, "badgeIncrement Index: "+index);
                        try {
                            int count_prev = Integer.parseInt(poColorWiseSizeArrayList.get(index).getBadgeQuantity());
                            int count = Integer.parseInt(defectedRectifiedModels_v4.get(index).getDefectCount());
                            int new_count = count_prev + count;
                            poColorWiseSizeArrayList.get(index).setBadgeQuantity(String.valueOf(new_count));
                        }catch (Exception e){
                            Log.d(TAG, "badgeIncrement: "+e.getMessage());
                        }
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
    }

    private JSONObject buildJsonObject_style_wise_operation_offline() throws JSONException{
        JSONObject save_obj = new JSONObject();
        JSONArray po_arr = new JSONArray();
        JSONArray color_arr = new JSONArray();
        JSONArray country_arr = new JSONArray();
        JSONArray size_arr = new JSONArray();
        JSONArray operation_arr = new JSONArray();
        JSONArray reject_arr = new JSONArray();
        JSONArray alter_arr = new JSONArray();
        JSONArray spot_arr = new JSONArray();
        JSONObject data_obj = new JSONObject();

        save_obj.put("status",true);
        data_obj.put("job_id",jobId);
        for(int i = 0; i < poIdArrayList.size(); i++)
        {
            JSONObject po_obj = new JSONObject();
            po_obj.put("po_id", poIdArrayList.get(i).getPoId());
            po_obj.put("po_number", poIdArrayList.get(i).getPoNumber());
            po_arr.put(po_obj);
        }
        for(int i = 0; i < colorArrayList.size(); i++)
        {
            JSONObject color_obj = new JSONObject();
            color_obj.put("po_break_down_id", colorArrayList.get(i).getPo_break_down_id());
            color_obj.put("colour_id", colorArrayList.get(i).getColourId());
            color_obj.put("colour_name", colorArrayList.get(i).getColourName());
            color_obj.put("color_output_qnty", colorArrayList.get(i).getColor_output_qnty());
            color_arr.put(color_obj);
        }
        for(int i = 0; i < countryArrayList.size(); i++)
        {
            JSONObject country_obj = new JSONObject();
            country_obj.put("po_break_down_id", countryArrayList.get(i).getPoBreakDownId());
            country_obj.put("colour_id", countryArrayList.get(i).getColourId());
            country_obj.put("country_id", countryArrayList.get(i).getCountryId());
            country_obj.put("country_name", countryArrayList.get(i).getCountryName());
            country_obj.put("country_output_qnty", countryArrayList.get(i).getCountryOutputQnty());
            country_arr.put(country_obj);
        }
        for(int i = 0; i < poColorWiseSizeArrayList.size(); i++)
        {
            JSONObject size_obj = new JSONObject();
            size_obj.put("po_break_down_id", poColorWiseSizeArrayList.get(i).getPo_break_down_id());
            size_obj.put("country_id", poColorWiseSizeArrayList.get(i).getCountryId());
            size_obj.put("country_name", poColorWiseSizeArrayList.get(i).getCountryName());
            size_obj.put("color_size_breakdown_id", poColorWiseSizeArrayList.get(i).getColorSizeBreakdownId());
            size_obj.put("colour_id", poColorWiseSizeArrayList.get(i).getColourId());
            size_obj.put("colour_name", poColorWiseSizeArrayList.get(i).getColorName());
            size_obj.put("size_id", poColorWiseSizeArrayList.get(i).getSizeId());
            size_obj.put("size_name", poColorWiseSizeArrayList.get(i).getSizeName());
            size_obj.put("input_qnty", poColorWiseSizeArrayList.get(i).getInputQnty());
            size_obj.put("output_qnty", poColorWiseSizeArrayList.get(i).getOutputQnty());
            size_obj.put("good", poColorWiseSizeArrayList.get(i).getGood());
            size_obj.put("reject", poColorWiseSizeArrayList.get(i).getReject());
            size_obj.put("alter", poColorWiseSizeArrayList.get(i).getAlter());
            size_obj.put("spot", poColorWiseSizeArrayList.get(i).getSpot());
            size_obj.put("rectified", poColorWiseSizeArrayList.get(i).getRectified());
            size_obj.put("rectified", poColorWiseSizeArrayList.get(i).getRectified());
            size_arr.put(size_obj);
        }
        for(int i = 0; i < operationArrayList.size(); i++)
        {
            JSONObject operation_obj = new JSONObject();
            operation_obj.put("ID", operationArrayList.get(i).getId());
            operation_obj.put("NAME", operationArrayList.get(i).getName());
            operation_arr.put(operation_obj);
        }
        for(int i = 0; i < sewingRejectModels.size(); i++)
        {
            JSONObject reject_obj = new JSONObject();
            reject_obj.put("ID", sewingRejectModels.get(i).getId());
            reject_obj.put("NAME", sewingRejectModels.get(i).getDefectName());
            reject_obj.put("DEFECT_SERIAL_NO", sewingRejectModels.get(i).getDefectSerialNo());
            reject_arr.put(reject_obj);
        }
        for(int i = 0; i < sewingAlterModels.size(); i++)
        {
            JSONObject alter_obj = new JSONObject();
            alter_obj.put("ID", sewingAlterModels.get(i).getId());
            alter_obj.put("NAME", sewingAlterModels.get(i).getDefectName());
            alter_obj.put("DEFECT_SERIAL_NO", sewingAlterModels.get(i).getDefectSerialNo());
            alter_arr.put(alter_obj);
        }
        for(int i = 0; i < sewingSpotModels.size(); i++)
        {
            JSONObject spot_obj = new JSONObject();
            spot_obj.put("ID", sewingSpotModels.get(i).getId());
            spot_obj.put("NAME", sewingSpotModels.get(i).getDefectName());
            spot_obj.put("DEFECT_SERIAL_NO", sewingSpotModels.get(i).getDefectSerialNo());
            spot_arr.put(spot_obj);
        }

        data_obj.put("po_ids", po_arr);
        data_obj.put("color", color_arr);
        data_obj.put("country", country_arr);
        data_obj.put("size", size_arr);
        data_obj.put("operation", operation_arr);
        data_obj.put("reject_list", reject_arr);
        data_obj.put("alter_list", reject_arr);
        data_obj.put("spot_list", reject_arr);

        save_obj.put("resultset", data_obj);
        Log.d("TAG", "buildJsonObject_offline: ######"+save_obj);
        return save_obj;
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
                    dtls_obj.put("country_id", countryId);
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
                dtls_obj.put("country_id", countryId);
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
                dtls_obj.put("country_id", countryId);
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

    private JSONObject buildJsonObject_gmts_rectified() throws JSONException{
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
        for(int i = 0; i < defectedRectifiedModels_v4.size(); i++)
        {
            if(defectedRectifiedModels_v4.get(i).getSelect()){
                rectifiedCount += 1;
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("order_id", po_breakDownId);
                dtls_obj.put("job_id", jobId);
                dtls_obj.put("item_id", modelArrayList.get(0).getItem_id());
                dtls_obj.put("country_id", countryId);
                dtls_obj.put("color_id", modelArrayList.get(0).getColor_id());
                dtls_obj.put("size_id", modelArrayList.get(0).getSize_id());
                dtls_obj.put("color_size_id", defectedRectifiedModels_v4.get(i).getColSizeId());
                dtls_obj.put("dtls_id", defectedRectifiedModels_v4.get(i).getDtlsId());
                dtls_obj.put("mst_id", defectedRectifiedModels_v4.get(i).getMstId());
                dtls_obj.put("rectified_qnty", defectedRectifiedModels_v4.get(i).getDefectCount());
                dtls_arr.put(dtls_obj);
            }
        }

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d("TAG", "buildJsonObject: rectified gmts ######"+save_obj);
        return save_obj;
    }

    private void showAlertMessage(String msg, Integer i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_StyleWiseSewingActivity_v6_offline.this);
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
//        _colorWiseOutputTV.setText(colorArrayList.get(colorSelection).getColor_output_qnty());
        sizeSelection = -1;
        defectedRectifiedModels.clear();
        coloSizeBreakDownId = "";
        modelArrayList.get(0).setColor_id(Integer.parseInt(selectedColorID));
        rectifiedListCard.setVisibility(View.GONE);
        gmtsRectifiedListCard.setVisibility(View.GONE);
        defauldGoodSelect(action_type);
        Log.d(TAG, "onColorHeadClick: "+sizeArrayList.size());
        poColorWiseSizeArrayList.clear();
        for(int i=0; i<sizeArrayList.size(); i++){
            if(selectedColorID.equals(sizeArrayList.get(i).getColourId()) && po_breakDownId.equals(sizeArrayList.get(i).getPo_break_down_id()) && countryId.equals(sizeArrayList.get(i).getCountry_id())){
                Log.d(TAG, "onColorHeadClick: ###"+sizeArrayList.get(position).getColorSizeBreakdownId());
                V2_POColorWiseSizeModel colorWiseSizeItemModel = new V2_POColorWiseSizeModel();
                colorWiseSizeItemModel.setPo_break_down_id(String.valueOf(sizeArrayList.get(i).getPo_break_down_id()));
                colorWiseSizeItemModel.setColorSizeBreakdownId(String.valueOf(sizeArrayList.get(i).getColorSizeBreakdownId()));
                colorWiseSizeItemModel.setSizeId(String.valueOf(sizeArrayList.get(i).getSizeId()));
                colorWiseSizeItemModel.setCountryId(String.valueOf(sizeArrayList.get(i).getCountry_id()));
                colorWiseSizeItemModel.setCountryName(String.valueOf(sizeArrayList.get(i).getCountry_name()));
                colorWiseSizeItemModel.setColorName(String.valueOf(sizeArrayList.get(i).getColour_name()));
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
        proDefectTypeGood = 1;
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
                if(defectType.equals("A")){
                    sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
                }
                if(defectType.equals("S")){
                    sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                Log.d(TAG, "onOperationHeadClick: "+e.getMessage());
            }
        }

        if (sizeSelection != -1){
//            _defectLayout.setVisibility(View.VISIBLE);
//            _operationLayout.setVisibility(View.GONE);
            if(operationItemModelList.get(position).getStatus()){
                operationItemModelList.get(position).setStatus(false);
                if(defectType.equals("A")){
                    mapOfAlterArrayLists.remove(operationItemModelList.get(position).getOperationId());
                }
                if(defectType.equals("S")){
                    mapOfSpotArrayLists.remove(operationItemModelList.get(position).getOperationId());
                }

            }else{
                _defectLayout.setVisibility(View.VISIBLE);
                _operationLayout.setVisibility(View.GONE);
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
        try {
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
            if(isInternetAvailable()){
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
                        Toast.makeText(V1_StyleWiseSewingActivity_v6_offline.this, "Can't get color size data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                if(poColorWiseSizeArrayList != null){
                    _inputQtyTV.setText(String.valueOf(poColorWiseSizeArrayList.get(position).getInputQnty()));
                    _checkQtyTV.setText(String.valueOf(poColorWiseSizeArrayList.get(position).getOutputQnty()));
                    _outputQtyTv.setText(String.valueOf(poColorWiseSizeArrayList.get(position).getOutputQnty()));

                    setSideBadgeCountOffline(position);

                    if(defectType.equals("G")){
                        saveDataAction();
                    }
                }
            }

        }catch (Exception e){
            Log.d(TAG, "onSizeHeadClick: "+e.getMessage());
        }
    }

    private void setSideBadgeCountOffline(int position) {
        _goodTV.setText(poColorWiseSizeArrayList.get(position).getGood());
        _rejectTV.setText(poColorWiseSizeArrayList.get(position).getReject());
        _alterTV.setText(poColorWiseSizeArrayList.get(position).getAlter());
        _spotTV.setText(poColorWiseSizeArrayList.get(position).getSpot());
        _rectifiedTV.setText(poColorWiseSizeArrayList.get(position).getRectified());
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.rejectRB:
                try {
                    if(checked){
                        if(proVariables.get(pro_position).getReject().getOperation() == 2 && proVariables.get(pro_position).getReject().getDefect() == 2){
                            defectType = "G";
                            proDefectTypeGood = 1;
                        }
                        else{
                            defectType = "R";
                            proDefectTypeGood = 0;
                        }
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
                }catch (Exception e){
                    showAlertMessage("Provariable not define. Please set provariable.", 0);
                }
                break;
            case R.id.alterRB:
                try {
                    if(checked){
                        if(proVariables.get(pro_position).getAlter().getOperation() == 2 && proVariables.get(pro_position).getAlter().getDefect() == 2){
                            defectType = "G";
                            proDefectTypeGood = 1;
                        }
                        else{
                            defectType = "A";
                            proDefectTypeGood = 0;
                        }
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
                }catch (Exception e){
                    showAlertMessage("Provariable not define. Please set provariable.", 0);
                }

                break;
            case R.id.spotRB:
                try {
                    if(checked){
                        if(proVariables.get(pro_position).getSpot().getOperation() == 2 && proVariables.get(pro_position).getSpot().getDefect() == 2){
                            defectType = "G";
                            proDefectTypeGood = 1;
                        }
                        else{
                            defectType = "S";
                            proDefectTypeGood = 0;
                        }
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
                }catch (Exception e){
                    showAlertMessage("Provariable not define. Please set provariable.", 0);
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
//                    proDefectTypeGood = 1;
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
            if(proDefectTypeGood == 1){
                sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
            }else{
                sewingSizeRecyclerAdapter.notifyDataSetChanged();
            }

        } else if(type.equals("ALTER")) {
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            if(proDefectTypeGood == 1){
                sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
            }else{
                sewingSizeRecyclerAdapter.notifyDataSetChanged();
            }
        }else if(type.equals("SPOT")){
            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                        p1.setBadgeQuantity("0");
                    }
                }
            }
            if(proDefectTypeGood == 1){
                sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
            }else{
                sewingSizeRecyclerAdapter.notifyDataSetChanged();
            }
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
                            typeWiseInOutModel.setPo_break_down_id(po_breakDownId);
                            typeWiseInOutModel.setColor_id(selectedColorID);
                            typeWiseInOutModel.setSizeId(response.body().getResultset().getSizeQnty().get(i).getSizeId());
                            typeWiseInOutModel.setGood(response.body().getResultset().getSizeQnty().get(i).getGood());
                            typeWiseInOutModel.setReject(response.body().getResultset().getSizeQnty().get(i).getReject());
                            typeWiseInOutModel.setAlter(response.body().getResultset().getSizeQnty().get(i).getAlter());
                            typeWiseInOutModel.setSpot(response.body().getResultset().getSizeQnty().get(i).getSpot());
                            typeWiseInOutModel.setRectified(response.body().getResultset().getSizeQnty().get(i).getRectified());
//                            typeWiseInOutModelsArrayList.add(typeWiseInOutModel);
                        }

                        if(type.equals("GOOD")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getGood());
                                    }
                                }
                            }
                        } else if(type.equals("REJECT")) {
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getReject());
                                    }
                                }
                            }
                        } else if(type.equals("ALTER")) {
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getAlter());
                                    }
                                }
                            }
                        }else if(type.equals("SPOT")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getSpot());
                                    }
                                }
                            }
                        }else if(type.equals("RECTIFIED")){
                            for (V2_POColorWiseSizeModel p1 : poColorWiseSizeArrayList) {
                                for (V2_TypeWiseInOutModel p2 : typeWiseInOutModelsArrayList) {
                                    if (p1.getSizeId().equals(p2.getSizeId()) && p1.getPo_break_down_id().equals(p2.getPo_break_down_id()) && p1.getColourId().equals(p2.getColor_id())) {
                                        p1.setBadgeQuantity(p2.getRectified());
                                    }
                                }
                            }
                        }

                        try {
                            sewingGoodQualitySizeRecyclerAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }
                        try {
                            sewingSizeRecyclerAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
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
        if(pro_v4_status != 1){
            Log.d(TAG, "requestForRectifiedData: #######");
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
                                    defectedRectifiedModel.setDefectTypeNames(response.body().getData().get(i).getDefectTypeName());
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
                    Toast.makeText(V1_StyleWiseSewingActivity_v6_offline.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            apiInterface.getDefectedListForGmtsRectifiedModelCall_V5(selectedColorID, String.valueOf(lineId), userId, po_breakDownId, item_numberId, jobId).enqueue(new Callback<V1_DefectListOfRectifiedModel_v5>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<V1_DefectListOfRectifiedModel_v5> call, Response<V1_DefectListOfRectifiedModel_v5> response) {
                    Log.d(TAG, "onResponse: "+response.toString());
                    if(response.isSuccessful()){
                        defectedRectifiedModels_v4.clear();
                        if(response.body().getData() != null){
                            for(int i=0; i<response.body().getData().size(); i++){
                                if(response.body().getData().get(0).getMstId().equals("0")){
                                    defectedRectifiedModels_v4.clear();
                                    break;
                                }else{
                                    V1_DefectedRectifiedModel_V4 defectedRectifiedModel = new V1_DefectedRectifiedModel_V4();
                                    defectedRectifiedModel.setMstId(response.body().getData().get(i).getMstId());
                                    defectedRectifiedModel.setDtlsId(response.body().getData().get(i).getDtlsId());
                                    defectedRectifiedModel.setOperationName(response.body().getData().get(i).getOperationName());
                                    defectedRectifiedModel.setAlterQty(response.body().getData().get(i).getAlterQty());
                                    defectedRectifiedModel.setSpotQty(response.body().getData().get(i).getSpotQty());
                                    defectedRectifiedModel.setRectifiedQty(response.body().getData().get(i).getRectifiedQty());
                                    defectedRectifiedModel.setProductionDate(response.body().getData().get(i).getProductionDate());
                                    defectedRectifiedModel.setColSizeId(response.body().getData().get(i).getColSizeId());
                                    defectedRectifiedModel.setDefectNames(response.body().getData().get(i).getDefectNames());
                                    defectedRectifiedModel.setColorName(response.body().getData().get(i).getColorName());
                                    defectedRectifiedModel.setSizeName(response.body().getData().get(i).getSizeName());
                                    int alterSpot = Integer.parseInt(response.body().getData().get(i).getAlterQty()) +  Integer.parseInt(response.body().getData().get(i).getSpotQty());
                                    int rectified = Integer.parseInt(response.body().getData().get(i).getRectifiedQty());
                                    int diff = alterSpot - rectified;
                                    defectedRectifiedModel.setDefectCount(String.valueOf(diff));
                                    defectedRectifiedModel.setSelect(false);
                                    defectedRectifiedModels_v4.add(defectedRectifiedModel);
                                }
                            }
                            _gmtsRectifiedSelectCheckbox.setText("SELECT ALL");
                            _gmtsRectifiedSelectCheckbox.setChecked(false);
                        }
                        initGmtsRectifiedDefectRecyclerView();
                    }
                }

                @Override
                public void onFailure(Call<V1_DefectListOfRectifiedModel_v5> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText(V1_StyleWiseSewingActivity_v6_offline.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

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

    private void initGmtsRectifiedDefectRecyclerView() {
        if(defectedRectifiedModels_v4.size() > 0){
            _gmtsRectifiedSelectCheckbox.setVisibility(View.VISIBLE);
            _gmtsRectifiedLinearLayout.setVisibility(View.VISIBLE);
            _errorGmtsRectifiedDataSetLayout.setVisibility(View.GONE);
        }else{
            _gmtsRectifiedSelectCheckbox.setVisibility(View.VISIBLE);
            _errorGmtsRectifiedDataSetLayout.setVisibility(View.VISIBLE);
            _gmtsRectifiedLinearLayout.setVisibility(View.VISIBLE);
        }

        Collections.sort(defectedRectifiedModels_v4, V1_DefectedRectifiedModel_V4.nameComparator);
        defectedRectifiedRecyclerViewAdapter_v4 = new V1_DefectedRectifiedRecyclerViewAdapter_v4( this, defectedRectifiedModels_v4,  this);
        defectGmtsRectifiedRecyclerView.setAdapter(defectedRectifiedRecyclerViewAdapter_v4);
        defectGmtsRectifiedRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        defectedRectifiedRecyclerViewAdapter_v4.notifyDataSetChanged();
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
            gmtsRectifiedListCard.setVisibility(View.GONE);
            dataResetMethod();
        } else if(rectified_number == 1){
            if(pro_v4_status == 1){
                _defectLayout.setVisibility(View.GONE);
                _operationLayout.setVisibility(View.VISIBLE);
                defectListCard.setVisibility(View.GONE);
                operationListCard.setVisibility(View.GONE);
                _operationLinearLayout.setVisibility(View.GONE);
                rectifiedListCard.setVisibility(View.GONE);
                gmtsRectifiedListCard.setVisibility(View.VISIBLE);
                requestForRectifiedData();
            }else{
                _defectLayout.setVisibility(View.GONE);
                _operationLayout.setVisibility(View.VISIBLE);
                defectListCard.setVisibility(View.GONE);
                operationListCard.setVisibility(View.GONE);
                _operationLinearLayout.setVisibility(View.GONE);
                rectifiedListCard.setVisibility(View.VISIBLE);
                gmtsRectifiedListCard.setVisibility(View.GONE);
                requestForRectifiedData();
            }

        } else if(reject_number == 1) {
            rectifiedListCard.setVisibility(View.GONE);
            gmtsRectifiedListCard.setVisibility(View.GONE);
            if (sizeSelection != -1){
                if(proDefectTypeGood == 1){
                    _operationLayout.setVisibility(View.VISIBLE);
                    defectListCard.setVisibility(View.GONE);
                    _defectLayout.setVisibility(View.GONE);
                    _operationNameTV.setText("");
                }else{
                    _operationLayout.setVisibility(View.GONE);
                    defectListCard.setVisibility(View.VISIBLE);
                    _defectLayout.setVisibility(View.VISIBLE);
                    _operationNameTV.setText("");
                }

            } else {
                showAlertMessage("Size not selected", 0);
            }
        } else {
            rectifiedListCard.setVisibility(View.GONE);
            gmtsRectifiedListCard.setVisibility(View.GONE);
            _defectLayout.setVisibility(View.GONE);
            _operationLayout.setVisibility(View.VISIBLE);
            defectListCard.setVisibility(View.VISIBLE);
            if(proDefectTypeGood == 1){
                operationListCard.setVisibility(View.GONE);
            }else {
                operationListCard.setVisibility(View.VISIBLE);
            }
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
        mapOfAlterArrayLists.clear();
        mapOfSpotArrayLists.clear();
        _rectifiedSelectCheckbox.setText("SELECT ALL");
        for(int i=0; i<operationItemModelList.size(); i++){
            operationItemModelList.get(i).setStatus(true);
        }

        rectifiedListCard.setVisibility(View.GONE);
        gmtsRectifiedListCard.setVisibility(View.GONE);
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
    public void onGmtsRectifiedHeadClick(int position, View v) {
        if(defectedRectifiedModels_v4.get(position).getSelect()){
            defectedRectifiedModels_v4.get(position).setSelect(false);
        }else{
            defectedRectifiedModels_v4.get(position).setSelect(true);
        }
        defectedRectifiedRecyclerViewAdapter_v4.notifyDataSetChanged();
        int status = 0;
        for(int i=0; i<defectedRectifiedModels_v4.size(); i++){
            if(defectedRectifiedModels_v4.get(i).getSelect()){
                status += 1;
            }
        }
        if(defectedRectifiedModels_v4.size() == status){
            _gmtsRectifiedSelectCheckbox.setText("UNSELECT ALL");
            _gmtsRectifiedSelectCheckbox.setChecked(true);
        }else{
            allSelectPosition = true;
            _gmtsRectifiedSelectCheckbox.setText("SELECT ALL");
            _gmtsRectifiedSelectCheckbox.setChecked(false);
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