package com.kutub.youngorganization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewsPaperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_paper);

        setupLink(R.id.linkProthomAlo, "https://www.prothomalo.com");
        setupLink(R.id.linkJugantor, "https://www.jugantor.com");
        setupLink(R.id.linkKalerKontho, "https://www.kalerkantho.com");
        setupLink(R.id.linkIttefaq, "https://www.ittefaq.com.bd");
        setupLink(R.id.linkSamakal, "https://samakal.com");
        setupLink(R.id.linkBdNews24, "https://bdnews24.com");
        setupLink(R.id.linkBangladeshPratidin, "https://www.bd-pratidin.com");
        setupLink(R.id.linkManabZamin, "https://mzamin.com");
        setupLink(R.id.linkInqilab, "https://www.dailyinqilab.com");
        setupLink(R.id.linkJanakantha, "https://www.dailyjanakantha.com");
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