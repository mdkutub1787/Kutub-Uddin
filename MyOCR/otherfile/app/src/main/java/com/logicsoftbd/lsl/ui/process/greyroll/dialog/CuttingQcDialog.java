package com.logicsoftbd.lsl.ui.process.greyroll.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.ui.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuttingQcDialog extends BaseDialog {
    private static final String TAG = "CuttingQcDialog";
    public static final String EXTRA_RECEIVE_ID = "extra_bundle_barcode";
    private CuttingQcBarcodeResponse.Result.DetailsPart.BundleData gmatsbarcode;

    public static CuttingQcDialog newInstance(Context context, CuttingQcBarcodeResponse.Result.DetailsPart.BundleData bundleMap) {
        CuttingQcDialog fragment = new CuttingQcDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, bundleMap);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    @BindView(R.id.text_view_1)
    TextView mTextView1;

    @BindView(R.id.text_view_2)
    TextView mTextView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_cutting_qc, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        Bundle bundle = getArguments();
        gmatsbarcode = (CuttingQcBarcodeResponse.Result.DetailsPart.BundleData) bundle.getSerializable(EXTRA_RECEIVE_ID);
        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
    @Override
    protected void setUp(View view) {
        mTextView1.setText(gmatsbarcode.getBundleNo());
       // mTextView2.setText(gmatsbarcode.getQty()+"");
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

    @OnClick(R.id.btn_later)
    void onLater() {
        dismissDialog();
    }
}
