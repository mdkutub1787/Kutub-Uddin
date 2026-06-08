package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleTrackingReportModelClass;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class V1_BundleTrackingReportActivity extends AppCompatActivity implements View.OnClickListener {


    private Retrofit retrofit;
    private String urlString, urladdressChk, urladdress, barcodeNumber;
    private Integer userId = 0;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private int status = 0;
    private ProgressDialog pDialog;


    private String base_url = "", o_job_no, O_order_no, o_style_no, o_color_name, o_order_qty,
            c_cut_no, c_date_and_time, c_color, c_size, c_rmg_number, c_qty,
            cq_cutting_qc_id, cq_date_and_time, cq_bundle_qty, cq_qc_pass_qty,cq_reject_qty, cq_replace_qty,
            p_issue_id, p_date_and_time, p_issue_qty,
            pr_issue_id, pr_date_and_time, pr_issue_qty, pr_reject_qty,
            e_issue_id, e_date_and_time, e_issue_qty,
            er_issue_id, er_date_and_time, er_issue_qty, er_reject_qty,
            s_input_id, s_date_and_time, s_input_qty, s_line_no,
            so_output_id, so_date_and_time, so_output_qty, so_Alter_spot_reject_qty,
            l_line_input_id, l_date_and_time, l_line_no,
            lo_line_output_id, lo_date_and_time, lo_Qty;

    private TextView allinfo, order_info, cut_and_lay_info, cutting_qc_info, print_issue_info, print_receive_info,
            embroidery_issue_info, embroidery_receive_info, sewing_input_info, sewing_output_info, line_input_info, line_output_info;
    private  TextView bundleNo;

    private TextView allinfoTV, order_infoTV, cut_and_lay_infoTV, cutting_qc_infoTV, print_issue_infoTV, print_receive_infoTV, embroidery_issue_infoTV,
            embroidery_receive_infoTV, sewing_input_infoTV, sewing_output_infoTV, line_input_infoTV, line_output_infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bundle_tracking_report);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        initialization();
    }

    private void initialization() {
        allinfo = findViewById(R.id.allinfoBtn);
        order_info = findViewById(R.id.order_info);
        cut_and_lay_info = findViewById(R.id.cut_and_lay_info);
        cutting_qc_info = findViewById(R.id.cutting_qc_info);
        print_issue_info = findViewById(R.id.print_issue_info);
        print_receive_info = findViewById(R.id.print_receive_info);
        embroidery_issue_info = findViewById(R.id.embroidery_issue_info);
        embroidery_receive_info = findViewById(R.id.embroidery_receive_info);
        sewing_input_info = findViewById(R.id.sewing_issue_info);
        sewing_output_info = findViewById(R.id.sewing_receive_info);
        line_input_info = findViewById(R.id.line_input_info);
        line_output_info = findViewById(R.id.line_output_info);
        bundleNo = findViewById(R.id.bundleNoText);

        allinfo.setOnClickListener(this);
        order_info.setOnClickListener(this);
        cut_and_lay_info.setOnClickListener(this);
        cutting_qc_info.setOnClickListener(this);
        print_issue_info.setOnClickListener(this);
        print_receive_info.setOnClickListener(this);
        embroidery_issue_info.setOnClickListener(this);
        embroidery_receive_info.setOnClickListener(this);
        sewing_input_info.setOnClickListener(this);
        sewing_output_info.setOnClickListener(this);
        line_input_info.setOnClickListener(this);
        line_output_info.setOnClickListener(this);

        allinfoTV = findViewById(R.id.allinfoTV);
        order_infoTV = findViewById(R.id.order_infoTV);
        cut_and_lay_infoTV = findViewById(R.id.cut_and_lay_infoTV);
        cutting_qc_infoTV = findViewById(R.id.cutting_qc_infoTV);
        print_issue_infoTV = findViewById(R.id.print_issue_infoTV);
        print_receive_infoTV = findViewById(R.id.print_receive_infoTV);
        embroidery_issue_infoTV = findViewById(R.id.embroidery_issue_infoTV);
        embroidery_receive_infoTV = findViewById(R.id.embroidery_receive_infoTV);
        sewing_input_infoTV = findViewById(R.id.sewing_issue_infoTV);
        sewing_output_infoTV = findViewById(R.id.sewing_receive_infoTV);
        line_input_infoTV = findViewById(R.id.line_input_infoTV);
        line_output_infoTV = findViewById(R.id.line_output_infoTV);


        Intent intent = getIntent();
        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        status = intent.getIntExtra("status", 0);

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

        barcodeNumber = resultS;
        bundleNo.setText("Bundle No "+barcodeNumber);

        pDialog.setMessage("Processing on going in ....");
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        showDialog();
        apiInterface.getBundleTrackingReportActivityCall(barcodeNumber).enqueue(new Callback<V1_BundleTrackingReportModelClass>() {
            @Override
            public void onResponse(Call<V1_BundleTrackingReportModelClass> call, Response<V1_BundleTrackingReportModelClass> response) {
                if(response.isSuccessful()){

                    hideDialog();

                    o_job_no = response.body().getData().getOrderInfo().getJobNo();
                    O_order_no = response.body().getData().getOrderInfo().getOrderNo();
                    o_style_no = response.body().getData().getOrderInfo().getStyleNo();
//                    o_color_name = response.body().getData().getOrderInfo().getColorName();
                    o_order_qty = response.body().getData().getOrderInfo().getOrderQty();

                    c_cut_no = response.body().getData().getCutAndLayInfo().getCutNo();
                    c_date_and_time = response.body().getData().getCutAndLayInfo().getDateAndTime();
                    c_color = response.body().getData().getCutAndLayInfo().getColor();
                    c_size = response.body().getData().getCutAndLayInfo().getSize();
                    c_rmg_number = response.body().getData().getCutAndLayInfo().getRmgNumber();
                    c_qty = response.body().getData().getCutAndLayInfo().getQty();

                    cq_cutting_qc_id = response.body().getData().getCuttingQcInfo().getCuttingQcId();
                    cq_date_and_time = response.body().getData().getCuttingQcInfo().getDateAndTime();
                    cq_bundle_qty = response.body().getData().getCuttingQcInfo().getBundleQty();
                    cq_qc_pass_qty = response.body().getData().getCuttingQcInfo().getQcPassQty();
                    cq_reject_qty = response.body().getData().getCuttingQcInfo().getRejectQty();
                    cq_replace_qty = response.body().getData().getCuttingQcInfo().getReplaceQty();

                    p_issue_id = response.body().getData().getPrintIssueInfo().getIssueId();
                    p_date_and_time = response.body().getData().getPrintIssueInfo().getDateAndTime();
                    p_issue_qty = response.body().getData().getPrintIssueInfo().getIssueQty();

                    pr_issue_id = response.body().getData().getPrintReceiveInfo().getIssueId();
                    pr_date_and_time = response.body().getData().getPrintReceiveInfo().getDateAndTime();
                    pr_issue_qty = response.body().getData().getPrintReceiveInfo().getIssueQty();
                    pr_reject_qty = response.body().getData().getPrintReceiveInfo().getRejectQty();

                    e_issue_id = response.body().getData().getEmbroideryIssueInfo().getIssueId();
                    e_date_and_time = response.body().getData().getEmbroideryIssueInfo().getDateAndTime();
                    e_issue_qty = response.body().getData().getEmbroideryIssueInfo().getIssueQty();

                    er_issue_id = response.body().getData().getEmbroideryReceiveInfo().getIssueId();
                    er_date_and_time = response.body().getData().getEmbroideryReceiveInfo().getDateAndTime();
                    er_issue_qty = response.body().getData().getEmbroideryReceiveInfo().getIssueQty();
                    er_reject_qty = response.body().getData().getEmbroideryReceiveInfo().getRejectQty();

                    s_input_id = response.body().getData().getSewingInputInfo().getInputId();
                    s_date_and_time = response.body().getData().getSewingInputInfo().getDateAndTime();
                    s_input_qty = response.body().getData().getSewingInputInfo().getInputQty();
                    s_line_no = response.body().getData().getSewingInputInfo().getLineNo();

                    so_output_id = response.body().getData().getSewingOutputInfo().getOutputId();
                    so_date_and_time = response.body().getData().getSewingOutputInfo().getDateAndTime();
                    so_output_qty = response.body().getData().getSewingOutputInfo().getOutputQty();
                    so_Alter_spot_reject_qty = response.body().getData().getSewingOutputInfo().getAlterSpotRejectQty();

                    l_line_input_id = response.body().getData().getLineInputInfo().getLineInputId();
                    l_date_and_time = response.body().getData().getLineInputInfo().getDateAndTime();
                    l_line_no = response.body().getData().getLineInputInfo().getLineNo();

                    lo_line_output_id = response.body().getData().getLineOutputInfo().getLineOutputId();
                    lo_date_and_time = response.body().getData().getLineOutputInfo().getDateAndTime();
                    lo_Qty = response.body().getData().getLineOutputInfo().getQty();

                    order_infoTV.setText(new StringBuilder().append("JOB NO: ").append(o_job_no).
                            append("\n").append("ORDER NO: ").append(O_order_no).
                            append("\n").append("STYLE NO: ").append(o_style_no).
                            append("\n").append("COLOR NAME: ").append(o_color_name).
                            append("\n").append("ORDER QTY: ").append(o_order_qty));

                    cut_and_lay_infoTV.setText(new StringBuilder().append("CUT NO: ").append(c_cut_no).
                            append("\n").append("JOB NO: ").append(o_job_no).
                            append("\n").append("ORDER NO: ").append(O_order_no).
                            append("\n").append("STYLE NO: ").append(o_style_no).
//                            append("\n").append("COLOR NAME: ").append(o_color_name).
                            append("\n").append("ORDER QTY: ").append(o_order_qty).
                            append("\n").append("DATE AND TIME: ").append(c_date_and_time).
                            append("\n").append("COLOR NAME: ").append(c_color).
                            append("\n").append("SIZE: ").append(c_size).
                            append("\n").append("RMG NUMBER: ").append(c_rmg_number).
                            append("\n").append("QTY: ").append(c_qty));

                    cutting_qc_infoTV.setText(new StringBuilder().append("CUTTTING QC ID: ").append(cq_cutting_qc_id).
                            append("\n").append("DATE AND TIME: ").append(cq_date_and_time).
                            append("\n").append("BUNDLE QTY: ").append(cq_bundle_qty).
                            append("\n").append("QC PASS QTY: ").append(cq_bundle_qty).
                            append("\n").append("REJECT QTY: ").append(cq_reject_qty).
                            append("\n").append("REPLACE QTY: ").append(cq_replace_qty));

                    print_issue_infoTV.setText(new StringBuilder().append("ISSUE ID: ").append(p_issue_id).
                            append("\n").append("DATE AND TIME: ").append(p_date_and_time).
                            append("\n").append("ISSUE QTY: ").append(p_issue_qty));

                    print_receive_infoTV.setText(new StringBuilder().append("ISSUE ID: ").append(pr_issue_id).
                            append("\n").append("DATE AND TIME: ").append(pr_date_and_time).
                            append("\n").append("ISSUE QTY: ").append(pr_issue_qty).
                            append("\n").append("REJECT QTY: ").append(pr_reject_qty));

                    embroidery_issue_infoTV.setText(new StringBuilder().append("ISSUE ID: ").append(e_issue_id).
                            append("\n").append("DATE AND TIME: ").append(e_date_and_time).
                            append("\n").append("ISSUE QTY: ").append(e_issue_qty));

                    embroidery_receive_infoTV.setText(new StringBuilder().append("ISSUE ID: ").append(er_issue_id).
                            append("\n").append("DATE AND TIME: ").append(er_date_and_time).
                            append("\n").append("ISSUE QTY: ").append(er_issue_qty).
                            append("\n").append("REJECT QTY: ").append(er_reject_qty));

                    sewing_input_infoTV.setText(new StringBuilder().append("INPUT ID: ").append(s_input_id).
                            append("\n").append("DATE AND TIME: ").append(s_date_and_time).
                            append("\n").append("INPUT QTY: ").append(s_input_qty).
                            append("\n").append("LINE NO: ").append(s_line_no));

                    sewing_output_infoTV.setText(new StringBuilder().append("OUTPUT ID: ").append(so_output_id).
                            append("\n").append("DATE AND TIME: ").append(so_date_and_time).
                            append("\n").append("OUTPUT QTY: ").append(so_output_qty).
                            append("\n").append("ALTER SPOT REJECT QTY: ").append(so_Alter_spot_reject_qty));

                    line_input_infoTV.setText(new StringBuilder().append("LINE INPUT ID: ").append(l_line_input_id).
                            append("\n").append("DATE AND TIME: ").append(l_date_and_time).
                            append("\n").append("LINE NO: ").append(l_line_no));

                    line_output_infoTV.setText(new StringBuilder().append("LINE OUTPUT ID: ").append(lo_line_output_id).
                            append("\n").append("DATE AND TIME: ").append(lo_date_and_time).
                            append("\n").append("QTY: ").append(lo_Qty));

                }
            }

            @Override
            public void onFailure(Call<V1_BundleTrackingReportModelClass> call, Throwable t) {
                hideDialog();
                Toast.makeText(V1_BundleTrackingReportActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.allinfoBtn:
                allinfoTV.setVisibility(View.VISIBLE);
//                order_infoTV.setVisibility(View.GONE);
//                cut_and_lay_infoTV.setVisibility(View.GONE);
//                cutting_qc_infoTV.setVisibility(View.GONE);
//                print_issue_infoTV.setVisibility(View.GONE);
//                print_receive_infoTV.setVisibility(View.GONE);
//                embroidery_issue_infoTV.setVisibility(View.GONE);
//                embroidery_receive_infoTV.setVisibility(View.GONE);
//                sewing_input_infoTV.setVisibility(View.GONE);
//                sewing_output_infoTV.setVisibility(View.GONE);
//                line_input_infoTV.setVisibility(View.GONE);
//                line_output_infoTV.setVisibility(View.GONE);
                allinfoTV.setText(new StringBuilder().append("JOB NO: ").append(o_job_no).
                        append("\n").append("ORDER NO: ").append(O_order_no).
                        append("\n").append("STYLE NO: ").append(o_style_no).
                        append("\n").append("COLOR NAME: ").append(o_color_name).
                        append("\n").append("ORDER QTY: ").append(o_order_qty).
                        append("CUT NO: ").append(c_cut_no).
                        append("\n").append("DATE AND TIME: ").append(c_date_and_time).
                        append("\n").append("COLOR NAME: ").append(c_color).
                        append("\n").append("SIZE: ").append(c_size).
                        append("\n").append("RMG NUMBER: ").append(c_rmg_number).
                        append("\n").append("QTY: ").append(c_qty).
                        append("CUTTTING QC ID: ").append(cq_cutting_qc_id).
                        append("\n").append("DATE AND TIME: ").append(cq_date_and_time).
                        append("\n").append("BUNDLE QTY: ").append(cq_bundle_qty).
                        append("\n").append("QC PASS QTY: ").append(cq_bundle_qty).
                        append("\n").append("REJECT QTY: ").append(cq_reject_qty).
                        append("\n").append("REPLACE QTY: ").append(cq_replace_qty).
                        append("ISSUE ID: ").append(p_issue_id).
                        append("\n").append("DATE AND TIME: ").append(p_date_and_time).
                        append("\n").append("ISSUE QTY: ").append(p_issue_qty).
                        append("ISSUE ID: ").append(pr_issue_id).
                        append("\n").append("DATE AND TIME: ").append(pr_date_and_time).
                        append("\n").append("ISSUE QTY: ").append(pr_issue_qty).
                        append("\n").append("REJECT QTY: ").append(pr_reject_qty).
                        append("ISSUE ID: ").append(e_issue_id).
                        append("\n").append("DATE AND TIME: ").append(e_date_and_time).
                        append("\n").append("ISSUE QTY: ").append(e_issue_qty).
                        append("ISSUE ID: ").append(er_issue_id).
                        append("\n").append("DATE AND TIME: ").append(er_date_and_time).
                        append("\n").append("ISSUE QTY: ").append(er_issue_qty).
                        append("\n").append("REJECT QTY: ").append(er_reject_qty).
                        append("INPUT ID: ").append(s_input_id).
                        append("\n").append("DATE AND TIME: ").append(s_date_and_time).
                        append("\n").append("INPUT QTY: ").append(s_input_qty).
                        append("\n").append("LINE NO: ").append(s_line_no).
                        append("OUTPUT ID: ").append(so_output_id).
                        append("\n").append("DATE AND TIME: ").append(so_date_and_time).
                        append("\n").append("OUTPUT QTY: ").append(so_output_qty).
                        append("\n").append("ALTER SPOT REJECT QTY: ").append(so_Alter_spot_reject_qty).
                        append("LINE INPUT ID: ").append(l_line_input_id).
                        append("\n").append("DATE AND TIME: ").append(l_date_and_time).
                        append("\n").append("LINE NO: ").append(l_line_no).
                        append("LINE OUTPUT ID: ").append(lo_line_output_id).
                        append("\n").append("DATE AND TIME: ").append(lo_date_and_time).
                        append("\n").append("QTY: ").append(lo_Qty).
                        append("\n").toString());
                break;
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_BundleTrackingReportActivity.this, V1_ReportHomeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
        intent.putExtra("status", status);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
