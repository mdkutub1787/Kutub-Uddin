package com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.logicsoftbd.lsl.R;
public class PdfWebViewActivity extends AppCompatActivity {
    private static final String TAG = "PdfWebViewActivity";
    private WebView webView;
    private ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_popup);

        webView = findViewById(R.id.webview);
        closeButton = findViewById(R.id.closeButton);

        Intent intent = getIntent();
        String url  = intent.getStringExtra("pdf");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient());

        Log.d(TAG, "onCreate: "+url);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

        closeButton.setOnClickListener(v -> {
            finish();
        });
    }
}

