package com.kutub.insurance.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kutub.insurance.R;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;
import com.kutub.insurance.viewModel.InsuranceViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrintActivity extends AppCompatActivity {

    private static final String TAG = "PrintActivity";
    private String currentDate;
    private PolicyResponse policyResponse;
    private InsuranceViewModel insuranceViewModel;
    private BillResponse billResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        // Allow network operations on the main thread (temporary measure)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initializeViewModel();
        currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date());

        String billId = getIntent().getStringExtra("billId");
        Log.d(TAG, "Received billId: " + billId);

        loadDataByBillId(billId);
    }

    private void initializeViewModel() {
        insuranceViewModel = new ViewModelProvider(this).get(InsuranceViewModel.class);
    }

    private void loadDataByBillId(String billId) {
        insuranceViewModel.getPolicy().observe(this, policies -> {
            if (policies != null) {
                Log.d(TAG, "Policy API Response: " + policies.toString());
                findPolicyAndBill(policies, billId);
            } else {
                Log.e(TAG, "Failed to fetch policies.");
                showError("Failed to load policies.");
            }
        });
    }

    private void findPolicyAndBill(List<PolicyResponse> policies, String billId) {
        insuranceViewModel.getBill().observe(this, bills -> {
            if (bills != null) {
                Log.d(TAG, "Bill API Response: " + bills.toString());
                policyResponse = null;
                billResponse = null;

                for (PolicyResponse policy : policies) {
                    for (BillResponse bill : bills) {
                    }
                    if (policyResponse != null) {
                        break;
                    }
                }

                if (policyResponse != null && billResponse != null) {
                    createAndPrintPdf();
                } else {
                    showError("Bill or Policy not found for the given Bill ID.");
                }
            } else {
                Log.e(TAG, "Failed to fetch bills.");
                showError("Failed to load bills.");
            }
        });
    }

    private void createAndPrintPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        drawSinglePolicyContent(canvas, pageInfo, policyResponse, billResponse);

        document.finishPage(page);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String pdfFileName = "Policy_" + timeStamp + ".pdf";
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File pdfFile = new File(downloadsDir, pdfFileName);

        try {
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();
            showSuccess("PDF created: " + pdfFileName);
            doPrint(pdfFile); // Pass the pdfFile to doPrint
        } catch (IOException e) {
            Log.e(TAG, "Error creating PDF", e);
            showError("Error creating PDF");
        }
    }

    private void doPrint(File pdfFile) {
        if (pdfFile == null || !pdfFile.exists()) {
            showError("PDF file not found");
            return;
        }

        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
    }

    private void drawSinglePolicyContent(Canvas canvas, PdfDocument.PageInfo pageInfo, PolicyResponse policyResponse, BillResponse billResponse) {
        Paint paint = new Paint();
        paint.setTextSize(12);
        float x = 50f;
        float y = 100f;
        float lineHeight = 20f;

        paint.setTextSize(18);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        drawTextCenter(canvas, "Insurance Policy and Bill Details", x, y, paint, pageInfo);
        y += lineHeight * 2;

        paint.setTextSize(12);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        int headerBackgroundColor = Color.parseColor("#D3D3D3");
        int headerTextColor = Color.BLACK;

        paint.setColor(headerBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x - 5, y - 20, pageInfo.getPageWidth() - 50, y + 5, paint);

        paint.setColor(headerTextColor);
        paint.setStyle(Paint.Style.FILL);
        drawTextCenter(canvas, "Policy Data", x, y, paint, pageInfo);
        y += lineHeight + 10;

        drawTextLeft(canvas, "Date: " + (policyResponse.getDate() != null ? policyResponse.getDate() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Policyholder: " + (policyResponse.getPolicyholder() != null ? policyResponse.getPolicyholder() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Bank Name: " + (policyResponse.getBankname() != null ? policyResponse.getBankname() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Address: " + (policyResponse.getAddress() != null ? policyResponse.getAddress() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Sum Insured: " + policyResponse.getSumInsured(), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Coverage: " + (policyResponse.getCoverage() != null ? policyResponse.getCoverage() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Construction: " + (policyResponse.getConstruction() != null ? policyResponse.getConstruction() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Used As: " + (policyResponse.getUsedAs() != null ? policyResponse.getUsedAs() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Period From: " + (policyResponse.getPeriodFrom() != null ? policyResponse.getPeriodFrom() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "Period To: " + (policyResponse.getPeriodTo() != null ? policyResponse.getPeriodTo() : ""), x, y, paint);
        y += lineHeight;
        drawTextLeft(canvas, "ID: " + (policyResponse.getId() != null ? policyResponse.getId() : ""), x, y, paint);
        y += lineHeight;

        y += lineHeight;
        paint.setColor(headerBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x - 5, y - 20, pageInfo.getPageWidth() - 50, y + 5, paint);
        paint.setColor(headerTextColor);
        drawTextCenter(canvas, "Bill Details", x, y, paint, pageInfo);
        y += lineHeight + 10;

        if (billResponse != null) {
            drawTextLeft(canvas, "Fire: " + billResponse.getFire(), x, y, paint);
            y += lineHeight;
            drawTextLeft(canvas, "RSD: " + billResponse.getRsd(), x, y, paint);
            y += lineHeight;
            drawTextLeft(canvas, "Tax: " + billResponse.getTax(), x, y, paint);
            y += lineHeight;
            drawTextLeft(canvas, "Net Premium: " + billResponse.getNetPremium(), x, y, paint);
            y += lineHeight;
            drawTextLeft(canvas, "Gross Premium: " + billResponse.getGrossPremium(), x, y, paint);
            y += lineHeight;
            y += lineHeight;
            drawTextLeft(canvas, "Policy ID: " + billResponse.getId(), x, y, paint);
            y += lineHeight;
        }
    }


    private void drawTextCenter(Canvas canvas, String text, float x, float y, Paint paint, PdfDocument.PageInfo pageInfo) {
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, pageInfo.getPageWidth() / 2f, y, paint);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    private void drawTextLeft(Canvas canvas, String text, float x, float y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }

    private void drawTextRight(Canvas canvas, String text, float x, float y, Paint paint, PdfDocument.PageInfo pageInfo) {
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(text, pageInfo.getPageWidth() - x, y, paint);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    public static Bitmap generateQRCode(String data, int width, int height) {
        try {
            com.google.zxing.Writer writer = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, width, height);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            Log.e("PrintActivity", "Error generating QR code", e);
            return null;
        }
    }
    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}