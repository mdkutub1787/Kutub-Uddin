package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ShipmentPendingModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ShipmentPendingReportActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private String urladdressChk, urladdress, urlString;
    public int userId = 0;

    private String base_url = "", PR_month, PR_PO_QTY, PR_PO_VALUE, PR_CUT_QTY, PR_CUT_BAL_ACCESS, PR_SEWING_QTY, PR_SEWING_BALANCE,
            PR_FINIS_QTY, PR_FINISHING_BALANCE, PR_SHIP_OUT, PR_EXPORT_FOB_VALUE, PR_SHIP_BAL_TO_PO_QTY, PR_SHIP_BAL_TO_PO_FOB_VALUE,
            PR_SEW_TO_SHIP_BALQTY, PR_SEW_TO_SHIP_BAL_FOB_VALUE;


    private String CR_month, CR_PO_QTY, CR_PO_VALUE, CR_CUT_QTY, CR_CUT_BAL_ACCESS, CR_SEWING_QTY, CR_SEWING_BALANCE,
            CR_FINIS_QTY, CR_FINISHING_BALANCE, CR_SHIP_OUT, CR_EXPORT_FOB_VALUE, CR_SHIP_BAL_TO_PO_QTY, CR_SHIP_BAL_TO_PO_FOB_VALUE,
            CR_SEW_TO_SHIP_BALQTY, CR_SEW_TO_SHIP_BAL_FOB_VALUE;

    private TextView mPR_month, mPR_PO_QTY, mPR_PO_VALUE, mPR_CUT_QTY, mPR_CUT_BAL_ACCESS, mPR_SEWING_QTY, mPR_SEWING_BALANCE,
            mPR_FINIS_QTY, mPR_FINISHING_BALANCE, mPR_SHIP_OUT, mPR_EXPORT_FOB_VALUE, mPR_SHIP_BAL_TO_PO_QTY, mPR_SHIP_BAL_TO_PO_FOB_VALUE,
            mPR_SEW_TO_SHIP_BALQTY, mPR_SEW_TO_SHIP_BAL_FOB_VALUE;

    private TextView mCR_month, mCR_PO_QTY, mCR_PO_VALUE, mCR_CUT_QTY, mCR_CUT_BAL_ACCESS, mCR_SEWING_QTY, mCR_SEWING_BALANCE,
            mCR_FINIS_QTY, mCR_FINISHING_BALANCE, mCR_SHIP_OUT, mCR_EXPORT_FOB_VALUE, mCR_SHIP_BAL_TO_PO_QTY, mCR_SHIP_BAL_TO_PO_FOB_VALUE,
            mCR_SEW_TO_SHIP_BALQTY, mCR_SEW_TO_SHIP_BAL_FOB_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_shipment_pending_report);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void initialization() {

        mPR_month = findViewById(R.id.pr_month);
        mPR_PO_QTY = findViewById(R.id.pr_po_qty);
        mPR_PO_VALUE = findViewById(R.id.pr_po_value);
        mPR_CUT_QTY = findViewById(R.id.pr_cut_qty);
        mPR_CUT_BAL_ACCESS = findViewById(R.id.pr_cut_bal_access);
        mPR_SEWING_QTY = findViewById(R.id.pr_sewing_qty);
        mPR_SEWING_BALANCE = findViewById(R.id.pr_sewing_balance);
        mPR_FINIS_QTY = findViewById(R.id.pr_finish_qty);
        mPR_FINISHING_BALANCE = findViewById(R.id.pr_finish_balance);
        mPR_SHIP_OUT = findViewById(R.id.pr_ship_out);
        mPR_EXPORT_FOB_VALUE = findViewById(R.id.pr_export_fob_value);
        mPR_SHIP_BAL_TO_PO_QTY = findViewById(R.id.pr_ship_bal_to_po_qty);
        mPR_SHIP_BAL_TO_PO_FOB_VALUE = findViewById(R.id.pr_ship_bal_to_po_value);
        mPR_SEW_TO_SHIP_BALQTY = findViewById(R.id.pr_sew_to_ship_bal_qty);
        mPR_SEW_TO_SHIP_BAL_FOB_VALUE = findViewById(R.id.pr_sew_to_ship_bal_value);

        mCR_month = findViewById(R.id.cr_month);
        mCR_PO_QTY = findViewById(R.id.cr_po_qty);
        mCR_PO_VALUE = findViewById(R.id.cr_po_value);
        mCR_CUT_QTY = findViewById(R.id.cr_cut_qty);
        mCR_CUT_BAL_ACCESS = findViewById(R.id.cr_cut_bal_access);
        mCR_SEWING_QTY = findViewById(R.id.cr_sewing_qty);
        mCR_SEWING_BALANCE = findViewById(R.id.cr_sewing_balance);
        mCR_FINIS_QTY = findViewById(R.id.cr_finish_qty);
        mCR_FINISHING_BALANCE = findViewById(R.id.cr_finish_balance);
        mCR_SHIP_OUT = findViewById(R.id.cr_ship_out);
        mCR_EXPORT_FOB_VALUE = findViewById(R.id.cr_export_fob_value);
        mCR_SHIP_BAL_TO_PO_QTY = findViewById(R.id.cr_ship_bal_to_po_qty);
        mCR_SHIP_BAL_TO_PO_FOB_VALUE = findViewById(R.id.cr_ship_bal_to_po_value);
        mCR_SEW_TO_SHIP_BALQTY = findViewById(R.id.cr_sew_to_ship_bal_qty);
        mCR_SEW_TO_SHIP_BAL_FOB_VALUE = findViewById(R.id.cr_sew_to_ship_bal_value);

        Intent intent = getIntent();
        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");

        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();

        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userId = intent.getIntExtra("userId", 0);

        }else {
            urladdress = loginData.get(0).getUrl();
            userId = Integer.parseInt(loginData.get(0).getUserId());
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        sendRequestToServer();
    }

    private void sendRequestToServer() {
        apiInterface.getPlanVsBookedVsCapacityModelCall(3, String.valueOf(0), 2).enqueue(new Callback<V1_ShipmentPendingModel>() {
            @Override
            public void onResponse(Call<V1_ShipmentPendingModel> call, Response<V1_ShipmentPendingModel> response) {
                if (response.isSuccessful()){


                    PR_month = response.body().getData().getPREMONTH().getMONTH();
                    PR_PO_QTY = response.body().getData().getPREMONTH().getPOQTY();
                    PR_PO_VALUE = response.body().getData().getPREMONTH().getPOVALUE();
                    PR_CUT_QTY = response.body().getData().getPREMONTH().getCUTQTY();
                    PR_CUT_BAL_ACCESS = response.body().getData().getPREMONTH().getCUTBALACCESS();
                    PR_SEWING_QTY = response.body().getData().getPREMONTH().getSEWINGQTY();
                    PR_SEWING_BALANCE = response.body().getData().getPREMONTH().getSEWINGBALANCE();
                    PR_FINIS_QTY = response.body().getData().getPREMONTH().getFINISQTY();
                    PR_FINISHING_BALANCE = response.body().getData().getPREMONTH().getFINISHINGBALANCE();
                    PR_SHIP_OUT = response.body().getData().getPREMONTH().getSHIPOUT();
                    PR_EXPORT_FOB_VALUE = response.body().getData().getPREMONTH().getEXPORTFOBVALUE();
                    PR_SHIP_BAL_TO_PO_QTY = response.body().getData().getPREMONTH().getSHIPBALTOPOQTY();
                    PR_SEW_TO_SHIP_BAL_FOB_VALUE = response.body().getData().getPREMONTH().getSHIPBALTOPOFOBVALUE();
                    PR_SHIP_BAL_TO_PO_FOB_VALUE = response.body().getData().getPREMONTH().getSHIPBALTOPOFOBVALUE();
                    PR_SEW_TO_SHIP_BALQTY = response.body().getData().getPREMONTH().getSEWTOSHIPBALQTY();
                    PR_SEW_TO_SHIP_BAL_FOB_VALUE = response.body().getData().getPREMONTH().getSEWTOSHIPBALFOBVALUE();

                    CR_month = response.body().getData().getCRRMONTH().getMONTH();
                    CR_PO_QTY = response.body().getData().getCRRMONTH().getPOQTY();
                    CR_PO_VALUE = response.body().getData().getCRRMONTH().getPOVALUE();
                    CR_CUT_QTY = response.body().getData().getCRRMONTH().getCUTQTY();
                    CR_CUT_BAL_ACCESS = response.body().getData().getCRRMONTH().getCUTBALACCESS();
                    CR_SEWING_QTY = response.body().getData().getCRRMONTH().getSEWINGQTY();
                    CR_SEWING_BALANCE = response.body().getData().getCRRMONTH().getSEWINGBALANCE();
                    CR_FINIS_QTY = response.body().getData().getCRRMONTH().getFINISQTY();
                    CR_FINISHING_BALANCE = response.body().getData().getCRRMONTH().getFINISHINGBALANCE();
                    CR_SHIP_OUT = response.body().getData().getCRRMONTH().getSHIPOUT();
                    CR_EXPORT_FOB_VALUE = response.body().getData().getCRRMONTH().getEXPORTFOBVALUE();
                    CR_SHIP_BAL_TO_PO_QTY = response.body().getData().getCRRMONTH().getSHIPBALTOPOQTY();
                    CR_SEW_TO_SHIP_BAL_FOB_VALUE = response.body().getData().getCRRMONTH().getSHIPBALTOPOFOBVALUE();
                    CR_SHIP_BAL_TO_PO_FOB_VALUE = response.body().getData().getCRRMONTH().getSHIPBALTOPOFOBVALUE();
                    CR_SEW_TO_SHIP_BALQTY = response.body().getData().getCRRMONTH().getSEWTOSHIPBALQTY();
                    CR_SEW_TO_SHIP_BAL_FOB_VALUE = response.body().getData().getCRRMONTH().getSEWTOSHIPBALFOBVALUE();

                    setView();


                }else {
                    Toast.makeText(V1_ShipmentPendingReportActivity.this, "No response from server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_ShipmentPendingModel> call, Throwable t) {
                Toast.makeText(V1_ShipmentPendingReportActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        mPR_month.setText(PR_month);
        mPR_PO_QTY.setText(PR_PO_QTY);
        mPR_PO_VALUE.setText(PR_PO_VALUE);
        mPR_CUT_QTY.setText(PR_CUT_QTY);
        mPR_CUT_BAL_ACCESS.setText(PR_CUT_BAL_ACCESS);
        mPR_SEWING_QTY.setText(PR_SEWING_QTY);
        mPR_SEWING_BALANCE.setText(PR_SEWING_BALANCE);
        mPR_FINIS_QTY.setText(PR_FINIS_QTY);
        mPR_FINISHING_BALANCE.setText(PR_FINISHING_BALANCE);
        mPR_SHIP_OUT.setText(PR_SHIP_OUT);
        mPR_EXPORT_FOB_VALUE.setText(PR_EXPORT_FOB_VALUE);
        mPR_SHIP_BAL_TO_PO_QTY.setText(PR_SHIP_BAL_TO_PO_QTY);
        mPR_SHIP_BAL_TO_PO_FOB_VALUE.setText(PR_SHIP_BAL_TO_PO_FOB_VALUE);
        mPR_SEW_TO_SHIP_BALQTY.setText(PR_SEW_TO_SHIP_BALQTY);
        mPR_SEW_TO_SHIP_BAL_FOB_VALUE.setText(PR_SEW_TO_SHIP_BAL_FOB_VALUE);

        mCR_month.setText(CR_month);
        mCR_PO_QTY.setText(CR_PO_QTY);
        mCR_PO_VALUE.setText(CR_PO_VALUE);
        mCR_CUT_QTY.setText(CR_CUT_QTY);
        mCR_CUT_BAL_ACCESS.setText(CR_CUT_BAL_ACCESS);
        mCR_SEWING_QTY.setText(CR_SEWING_QTY);
        mCR_SEWING_BALANCE.setText(CR_SEWING_BALANCE);
        mCR_FINIS_QTY.setText(CR_FINIS_QTY);
        mCR_FINISHING_BALANCE.setText(CR_FINISHING_BALANCE);
        mCR_SHIP_OUT.setText(CR_SHIP_OUT);
        mCR_EXPORT_FOB_VALUE.setText(CR_EXPORT_FOB_VALUE);
        mCR_SHIP_BAL_TO_PO_QTY.setText(CR_SHIP_BAL_TO_PO_QTY);
        mCR_SHIP_BAL_TO_PO_FOB_VALUE.setText(CR_SHIP_BAL_TO_PO_FOB_VALUE);
        mCR_SEW_TO_SHIP_BALQTY.setText(CR_SEW_TO_SHIP_BALQTY);
        mCR_SEW_TO_SHIP_BAL_FOB_VALUE.setText(CR_SEW_TO_SHIP_BAL_FOB_VALUE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_ShipmentPendingReportActivity.this, V1_MenuActivity.class);
        intent.putExtra("url", urladdress);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}