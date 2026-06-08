package com.kutub.youngorganization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BankListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);

        setupLink(R.id.linkABBank, "http://www.abbl.com");
        setupLink(R.id.linkAgraniBank, "http://www.agranibank.org");
        setupLink(R.id.linkAlArafah, "http://www.al-arafahbank.com/");
        setupLink(R.id.linkBCBL, "http://bcblbd.com/");
        setupLink(R.id.linkBDBL, "http://www.bdbl.com.bd");
        setupLink(R.id.linkKrishiBank, "http://www.krishibank.org.bd");
        setupLink(R.id.linkFSIBL, "https://www.fsiblbd.com");
        setupLink(R.id.linkIFIC, "https://www.ificbank.com.bd");
        setupLink(R.id.linkIBBL, "https://www.islamibankbd.com");
        setupLink(R.id.linkJamuna, "https://jamunabankbd.com");
        setupLink(R.id.linkMeghna, "https://www.meghnabank.com.bd");
        setupLink(R.id.linkNRBC, "http://www.nrbcommercialbank.com");
        setupLink(R.id.linkNRBGlobal, "http://www.nrbglobalbank.com");
        setupLink(R.id.linkOneBank, "http://www.onebank.com.bd");
        setupLink(R.id.linkPrime, "http://www.primebank.com.bd");
        setupLink(R.id.linkPubali, "http://www.pubalibangla.com");
        setupLink(R.id.linkRupali, "http://www.rupalibank.org");
        setupLink(R.id.linkSBAC, "http://www.sbacbank.com");
        setupLink(R.id.linkSoutheast, "http://www.southeastbank.com.bd");
        setupLink(R.id.linkStandard, "http://www.standardbankbd.com");
        setupLink(R.id.linkSCB, "https://www.sc.com/bd");
        setupLink(R.id.linkTrust, "http://www.tblbd.com");
        setupLink(R.id.linkUCB, "http://www.ucb.com.bd");
        setupLink(R.id.linkUttara, "http://www.uttarabank-bd.com");
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