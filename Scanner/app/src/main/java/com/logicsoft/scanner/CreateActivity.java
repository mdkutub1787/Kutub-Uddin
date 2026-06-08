package com.logicsoft.scanner;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class CreateActivity extends AppCompatActivity {
    private TextInputEditText etInputData;
    private Button btnGenerateQr,btnRefresh;
    private ImageView ivQrCodeResult;
    public static final String KEY_PREFILL_DATA = "SCAN_RESULT_TO_CREATE";
    private static final String TAG = "CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        etInputData = findViewById(R.id.etInputData);
        btnGenerateQr = findViewById(R.id.btnGenerateQr);
        ivQrCodeResult = findViewById(R.id.ivQrCodeResult);
        btnRefresh = findViewById(R.id.btnRefresh);
        String prefillText = getIntent().getStringExtra(KEY_PREFILL_DATA);

        if (prefillText != null && !prefillText.isEmpty()) {
            Log.d(TAG, "Received data to prefill: " + prefillText);
            etInputData.setText(prefillText);
            generateQrCode(prefillText);
        } else {
            Log.d(TAG, "No data prefilled from ResultActivity.");
            ivQrCodeResult.setImageResource(R.drawable.scanner);
            btnRefresh.setVisibility(View.GONE);
        }

        btnGenerateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etInputData.getText().toString().trim();
                if (data.isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Please enter some data", Toast.LENGTH_SHORT).show();
                    ivQrCodeResult.setImageResource(R.drawable.scanner);
                    btnRefresh.setVisibility(View.GONE);
                    return;
                }
                generateQrCode(data);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputAndQrCode();
            }
        });
    }

    private void generateQrCode(String data) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, "Cannot generate QR for empty data", Toast.LENGTH_SHORT).show();
            ivQrCodeResult.setImageResource(R.drawable.scanner);
            btnRefresh.setVisibility(View.GONE);
            return;
        }
        Log.d(TAG, "Generating QR for data: " + data);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            int width = 1000;
            int height = 1000;

            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ivQrCodeResult.setImageBitmap(bitmap);
            btnRefresh.setVisibility(View.VISIBLE);

        } catch (WriterException e) {
            e.printStackTrace();
            Log.e(TAG, "Error generating QR Code", e);
            Toast.makeText(CreateActivity.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
            ivQrCodeResult.setImageResource(R.drawable.scanner);
            btnRefresh.setVisibility(View.GONE);
        }
    }

    private void clearInputAndQrCode() {
        Log.d(TAG, "Clearing input and QR code.");
        etInputData.setText("");
        etInputData.setError(null);
        ivQrCodeResult.setImageResource(R.drawable.scanner);
        btnRefresh.setVisibility(View.GONE);
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }
}
