package com.logicsoftbd.lsl.ui.process.quantityactivity;

import static com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient.getUnsafeOkHttpClient;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.ApiEndPoint;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.RejectResponse;
import com.logicsoftbd.lsl.ui.base.BaseDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuttingQcDialog extends BaseDialog {
    private static final String TAG = "CuttingQcDialog";
    public static final String EXTRA_RECEIVE_ID = "extra_bundle_barcode";
    private CuttingQcBarcodeResponse.Result.DetailsPart.BundleData gmatsbarcode;
    private static CuttingQcQuantityActivity.OnRejectListener mRejectListener;
    public static String mBundleData;
    public static int mType = 0;

    public static int DEFECT = 0;
    public static int ALTER = 1;
    public static int SPOT = 2;
    public static int REJECT = 3;

    public static CuttingQcDialog newInstance(Context context, CuttingQcQuantityActivity.OnRejectListener  rejectListener, String bundleData) {
        CuttingQcDialog fragment = new CuttingQcDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        mRejectListener = rejectListener;
        mBundleData = bundleData;
        mType = 0;
        return fragment;
    }

    public static CuttingQcDialog newInstance(Context context, CuttingQcQuantityActivity.OnRejectListener  rejectListener, String bundleData, int type) {
        CuttingQcDialog fragment = new CuttingQcDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        mRejectListener = rejectListener;
        mBundleData = bundleData;
        mType = type;
        return fragment;
    }

    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    @BindView(R.id.linearLayoutView)
    LinearLayout linearLayout;

  /*  @BindView(R.id.linearLayoutTextView)
    LinearLayout linearLayoutTextView;

    @BindView(R.id.linearLayoutEditView)
    LinearLayout linearLayoutEditText;*/
    private EditText[] editTexts;
    private TextView[] textViews;
    ViewGroup.LayoutParams params;
    LayoutInflater inflater;

    private String mDefectString = "";
    private int mReject = 0 ;

    private List<RejectResponse.Challan.MasterPart> rejectList;
    private  StringBuilder strBuilder = new StringBuilder("");

    private Map<Integer, Integer> rejectMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_reject, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
    @Override
    protected void setUp(View view) {
       // mTextView2.setText(gmatsbarcode.getQty()+"");

        String apiUrl = ApiEndPoint.ENDPOINT_REJECT;

        if(mType == ALTER) {
            apiUrl = ApiEndPoint.ENDPOINT_ALTER_DEFECT;
        } else if(mType == SPOT ) {
            apiUrl = ApiEndPoint.ENDPOINT_SPOT_DEFECT;
        } else {
            apiUrl = ApiEndPoint.ENDPOINT_REJECT;
        }

        showLoading();
        AndroidNetworking.get(apiUrl)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(RejectResponse.class, new ParsedRequestListener<RejectResponse>() {
                    @Override
                    public void onResponse(RejectResponse response) {
                        hideLoading();
                       if(response != null) {
                           rejectList = response.getData().getMasterPart();
                          // addTextViews();
                           //addEditTexts();
                           inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                           params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT);
                           linearLayout.removeAllViews();
                           editTexts = new EditText[rejectList.size()];
                           textViews = new TextView[rejectList.size()];
                           if(mBundleData != null && mBundleData!= "") {
                               setRejectMap();
                           }
                           addItem();
                       }
                    }
                    @Override
                    public void onError(ANError anError) {
                        dismissDialog();
                    }
                });


    }




    public void addItem() {
        for (int i = 0; i < rejectList.size(); i++) {
            View dynamicEntryView = inflater.inflate(R.layout.inflate_reject, null);
            linearLayout.addView(dynamicEntryView, params);
            TextView playerName = (TextView) dynamicEntryView.findViewById(R.id.text_view_name);
            playerName.setText(rejectList.get(i).getRejectName()+": ");

            editTexts[i] = (EditText) dynamicEntryView.findViewById(R.id.edit_text_name);
            if(rejectMap.containsKey(rejectList.get(i).getId())) {
                editTexts[i].setId(i);
                editTexts[i].setText(rejectMap.get(rejectList.get(i).getId())+"");
            }
        }
    }


    public void hideSubmitButton() {
        mSubmitButton.setVisibility(View.GONE);
    }

    public void dismissDialog() {
        super.dismissDialog(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        extractData();
            mRejectListener.onRejectSubmit(strBuilder.toString(), mReject);
            dismissDialog();

    }

    public void extractData() {
        int i = 0;
        for(EditText editText: editTexts) {
            String val = editText.getText().toString().trim();

                if(!val.isEmpty() && val != null) {
                    if(strBuilder.toString() != "") {
                        strBuilder.append(",");
                    }
                   // mReject+=Integer.parseInt(val);
                    strBuilder.append(rejectList.get(i).getId()+"*"+val);
                }
            i++;
        }
    }

    @OnClick(R.id.btn_later)
    void onLater() {
        dismissDialog();
    }


    private void setRejectMap() {
            String[] arrSplit = mBundleData.split(",");
            for (int i=0; i < arrSplit.length; i++)
            {
                String[] productSplit = arrSplit[i].split("\\*");

                rejectMap.put(Integer.parseInt(productSplit[0]), Integer.parseInt(productSplit[1]));
            }

    }


}
