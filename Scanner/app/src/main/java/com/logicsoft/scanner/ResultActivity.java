package com.logicsoft.scanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private ImageView btnCreate;
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String result = getIntent().getStringExtra("result");
        String type = getIntent().getStringExtra("type");

        TextView tvType = findViewById(R.id.tvType);
        TextView tvResult = findViewById(R.id.tvResult);

        if (tvType != null) {
            tvType.setText(type);
        }
        if (tvResult != null) {
            tvResult.setText(result);
        }

        LinearLayout btnCopy = findViewById(R.id.btnCopy);
        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null && result != null) {
                    ClipData clip = ClipData.newPlainText("Scanned Result", result);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ResultActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                } else if (result == null) {
                    Toast.makeText(ResultActivity.this, "No result to copy.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResultActivity.this, "Clipboard service not available.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        LinearLayout btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                if (result != null && !result.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, result);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                } else {
                    Toast.makeText(ResultActivity.this, "No result to share.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnCreate = findViewById(R.id.btnCreate);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultActivity.this, CreateActivity.class);
                    if (result != null && !result.isEmpty()) {
                        intent.putExtra(CreateActivity.KEY_PREFILL_DATA, result);
                        Log.d(TAG, "Passing data to CreateActivity: " + result);
                    } else {
                        Log.w(TAG, "No result data to pass to CreateActivity.");
                    }
                    startActivity(intent);
                }
            });
        } else {
            Log.e(TAG, "ImageView with ID 'btnCreate' not found in layout R.layout.activity_result.");
        }

        LinearLayout btnOpen = findViewById(R.id.btnOpen);
        if (btnOpen != null) {
            btnOpen.setOnClickListener(v -> {
                if (result != null && (result.toLowerCase().startsWith("http://") || result.toLowerCase().startsWith("https://"))) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(ResultActivity.this, "Could not open URL. No app found.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error opening URL: " + result, e);
                    }
                } else {
                    Toast.makeText(ResultActivity.this, "Not a valid URL!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
