package com.logicsoftbd.lsl.ui.process.scanprocess;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notbytes.barcode_reader.BarcodeReaderFragment;

import com.google.android.gms.vision.barcode.Barcode;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.ui.base.BaseFragment;

import java.util.List;

public class BarcodeFragment extends BaseFragment implements BarcodeReaderFragment.BarcodeReaderListener{

    private static final String TAG = BarcodeFragment.class.getSimpleName();

    private BarcodeReaderFragment barcodeReader;

    private BarcodeReaderFragment.BarcodeReaderListener mListener;

    public static BarcodeFragment newInstance() {
        Bundle args = new Bundle();
        BarcodeFragment fragment = new BarcodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(BarcodeReaderFragment.BarcodeReaderListener barcodeReaderListener) {
        mListener = barcodeReaderListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        barcodeReader = (BarcodeReaderFragment) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);

        return view;
    }
    @Override
    protected void setUp(View view) {

    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        mListener.onScanned(barcode);
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        mListener.onScannedMultiple(barcodes);
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        mListener.onBitmapScanned(sparseArray);
    }

    @Override
    public void onScanError(String errorMessage) {
        mListener.onScanError(errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        showMessage("Camera permission denied!");
    }


}
