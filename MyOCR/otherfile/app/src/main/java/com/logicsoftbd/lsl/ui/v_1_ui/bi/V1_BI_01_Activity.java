package com.logicsoftbd.lsl.ui.v_1_ui.bi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.logicsoftbd.lsl.R;

public class V1_BI_01_Activity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bi01);

        Intent intent = getIntent();
        String html = intent.getStringExtra("html");

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(html, "text/html", "utf-8");

        webView.setWebViewClient(new WebViewClient());
    }
}