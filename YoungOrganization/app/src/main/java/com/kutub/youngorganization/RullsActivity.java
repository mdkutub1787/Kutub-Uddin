package com.kutub.youngorganization;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kutub.youngorganization.adapter.DynamicEditAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RullsActivity extends AppCompatActivity {

    private RecyclerView carouselRecyclerView, objectivesRecyclerView, rulesRecyclerView;
    private Button addCarouselBtn, addObjectiveBtn, addRuleBtn;
    private MaterialButton saveContentBtn;
    private DynamicEditAdapter carouselAdapter, objectivesAdapter, rulesAdapter;
    private final List<String> carouselList = new ArrayList<>();
    private final List<String> objectivesList = new ArrayList<>();
    private final List<String> rulesList = new ArrayList<>();
    private DatabaseReference carouselRef, orgInfoRef;
    private ProgressBar adminEditProgressBar;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulls);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        carouselRef = FirebaseDatabase.getInstance().getReference("carousel_messages");
        orgInfoRef = FirebaseDatabase.getInstance().getReference("organization_info");

        carouselRecyclerView = findViewById(R.id.carouselRecyclerView);
        objectivesRecyclerView = findViewById(R.id.objectivesRecyclerView);
        rulesRecyclerView = findViewById(R.id.rulesRecyclerView);
        addCarouselBtn = findViewById(R.id.addCarouselBtn);
        addObjectiveBtn = findViewById(R.id.addObjectiveBtn);
        addRuleBtn = findViewById(R.id.addRuleBtn);
        saveContentBtn = findViewById(R.id.saveContentBtn);
        adminEditProgressBar = findViewById(R.id.adminEditProgressBar);
        adminEditProgressBar.setVisibility(View.VISIBLE);

        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        objectivesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rulesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        carouselAdapter = new DynamicEditAdapter(carouselList, pos -> {
            carouselList.remove(pos);
            carouselAdapter.notifyItemRemoved(pos);
        });
        objectivesAdapter = new DynamicEditAdapter(objectivesList, pos -> {
            objectivesList.remove(pos);
            objectivesAdapter.notifyItemRemoved(pos);
        });
        rulesAdapter = new DynamicEditAdapter(rulesList, pos -> {
            rulesList.remove(pos);
            rulesAdapter.notifyItemRemoved(pos);
        });

        carouselRecyclerView.setAdapter(carouselAdapter);
        objectivesRecyclerView.setAdapter(objectivesAdapter);
        rulesRecyclerView.setAdapter(rulesAdapter);

        addCarouselBtn.setOnClickListener(v -> {
            carouselList.add("");
            carouselAdapter.notifyItemInserted(carouselList.size() - 1);
        });
        addObjectiveBtn.setOnClickListener(v -> {
            objectivesList.add("");
            objectivesAdapter.notifyItemInserted(objectivesList.size() - 1);
        });
        addRuleBtn.setOnClickListener(v -> {
            rulesList.add("");
            rulesAdapter.notifyItemInserted(rulesList.size() - 1);
        });

        saveContentBtn.setOnClickListener(v -> saveContent());

        loadExistingData();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
                        isAdmin = true;
                        setEditMode(true);
                    } else {
                        isAdmin = false;
                        setEditMode(false);
                    }
                }
                @Override public void onCancelled(@NonNull DatabaseError error) { setEditMode(false); }
            });
        } else {
            setEditMode(false);
        }
    }

    private void loadExistingData() {
        carouselRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carouselList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String msg = item.getValue(String.class);
                    if (msg != null) carouselList.add(msg);
                }
                if (carouselList.isEmpty()) carouselList.add("");
                carouselAdapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
        orgInfoRef.child("objectives").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                objectivesList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String obj = item.getValue(String.class);
                    if (obj != null) objectivesList.add(obj);
                }
                if (objectivesList.isEmpty()) objectivesList.add("");
                objectivesAdapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
        orgInfoRef.child("rules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rulesList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String rule = item.getValue(String.class);
                    if (rule != null) rulesList.add(rule);
                }
                if (rulesList.isEmpty()) rulesList.add("");
                rulesAdapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
        adminEditProgressBar.setVisibility(View.GONE);
    }

    private void saveContent() {
        // Save Carousel
        Map<String, Object> carouselUpdates = new HashMap<>();
        for (int i = 0; i < carouselList.size(); i++) {
            String msg = carouselList.get(i).trim();
            if (!msg.isEmpty()) carouselUpdates.put(String.valueOf(i), msg);
        }
        carouselRef.setValue(carouselUpdates);
        // Save Objectives
        Map<String, Object> objUpdates = new HashMap<>();
        for (int i = 0; i < objectivesList.size(); i++) {
            String obj = objectivesList.get(i).trim();
            if (!obj.isEmpty()) objUpdates.put(String.valueOf(i), obj);
        }
        orgInfoRef.child("objectives").setValue(objUpdates);
        // Save Rules
        Map<String, Object> ruleUpdates = new HashMap<>();
        for (int i = 0; i < rulesList.size(); i++) {
            String rule = rulesList.get(i).trim();
            if (!rule.isEmpty()) ruleUpdates.put(String.valueOf(i), rule);
        }
        orgInfoRef.child("rules").setValue(ruleUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RullsActivity.this, "Content saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RullsActivity.this, "Failed to save content.", Toast.LENGTH_SHORT).show());
    }

    private void setEditMode(boolean editable) {
        addCarouselBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
        addObjectiveBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
        addRuleBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
        saveContentBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
        carouselAdapter.setEditable(editable);
        objectivesAdapter.setEditable(editable);
        rulesAdapter.setEditable(editable);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
} 