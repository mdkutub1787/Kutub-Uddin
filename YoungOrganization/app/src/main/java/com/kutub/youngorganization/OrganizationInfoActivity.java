package com.kutub.youngorganization;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class OrganizationInfoActivity extends AppCompatActivity {

    private TextView objectivesTextView, rulesTextView;
    private DatabaseReference orgInfoRef;
    private ProgressBar organizationInfoProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("সংস্থার তথ্য");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        objectivesTextView = findViewById(R.id.objectivesTextView);
        rulesTextView = findViewById(R.id.rulesTextView);
        organizationInfoProgressBar = findViewById(R.id.organizationInfoProgressBar);
        organizationInfoProgressBar.setVisibility(View.VISIBLE);

        orgInfoRef = FirebaseDatabase.getInstance().getReference("organization_info");
        loadOrganizationInfo();
    }

    private void loadOrganizationInfo() {
        orgInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Objectives
                    List<String> objectives = new ArrayList<>();
                    for (DataSnapshot objSnap : snapshot.child("objectives").getChildren()) {
                        String obj = objSnap.getValue(String.class);
                        if (obj != null) objectives.add(obj);
                    }
                    // Rules
                    List<String> rules = new ArrayList<>();
                    for (DataSnapshot ruleSnap : snapshot.child("rules").getChildren()) {
                        String rule = ruleSnap.getValue(String.class);
                        if (rule != null) rules.add(rule);
                    }
                    // Show as joined text (bullet points)
                    objectivesTextView.setText(objectives.isEmpty() ? "তথ্য পাওয়া যায়নি।" : "• " + TextUtils.join("\n• ", objectives));
                    rulesTextView.setText(rules.isEmpty() ? "তথ্য পাওয়া যায়নি।" : "• " + TextUtils.join("\n• ", rules));
                } else {
                    objectivesTextView.setText("অ্যাডমিন এখনও কোনো উদ্দেশ্য যোগ করেননি।");
                    rulesTextView.setText("অ্যাডমিন এখনও কোনো নিয়মাবলী যোগ করেননি।");
                }
                organizationInfoProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrganizationInfoActivity.this, "তথ্য লোড করা যায়নি।", Toast.LENGTH_SHORT).show();
                organizationInfoProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 