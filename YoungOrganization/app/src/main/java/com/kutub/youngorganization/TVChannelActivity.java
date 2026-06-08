package com.kutub.youngorganization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TVChannelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_channel);

        setupLink(R.id.linkAsianTV, "https://www.asiantvonline.com");
        setupLink(R.id.linkATNBangla, "https://atnbangla.tv");
        setupLink(R.id.linkATNNews, "https://www.atnnewstv.com");
        setupLink(R.id.linkBTV, "http://www.btv.gov.bd");
        setupLink(R.id.linkChannel24, "https://www.channel24bd.tv");
        setupLink(R.id.linkChanneli, "https://www.channelionline.com");
        setupLink(R.id.linkEkusheyTV, "https://www.ekushey-tv.com");
        setupLink(R.id.linkJamunaTV, "https://www.jamuna.tv");
        setupLink(R.id.linkIndependentTV, "https://www.independent24.com");
        setupLink(R.id.linkSomoyTV, "https://www.somoynews.tv");
        setupLink(R.id.linkGTV, "https://www.gazitv.com");
    }

    private void setupLink(int textViewId, String url) {
        TextView link = findViewById(textViewId);
        if (link == null) return;
        link.setTextColor(Color.parseColor("#1976D2"));
        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link.setClickable(true);
        link.setFocusable(true);
        link.setBackgroundResource(android.R.drawable.list_selector_background);
        link.setOnClickListener(v -> {
            Toast.makeText(this, "ওয়েবসাইট লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        });
    }
} 