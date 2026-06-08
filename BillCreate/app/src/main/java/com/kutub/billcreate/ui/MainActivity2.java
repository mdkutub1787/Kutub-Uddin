package com.kutub.billcreate.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kutub.billcreate.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.billcreate.adapter.BillAdapter;
import com.kutub.billcreate.model.Bill;

import java.util.ArrayList;
import java.util.List;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity2 extends AppCompatActivity {

    private RecyclerView billRecyclerView;
    private BillAdapter billAdapter;
    private List<Bill> billList;
    private DatabaseReference billDatabase;
    private FloatingActionButton btnAddBill;
    private FirebaseAuth auth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        auth = FirebaseAuth.getInstance();

        TextView tvUsername = findViewById(R.id.tvUsername); // Username TextView
        TextView tvBalance = findViewById(R.id.tvBalance); // Total Balance TextView
        billRecyclerView = findViewById(R.id.billRecyclerView);
        progressBar = findViewById(R.id.progressBar); // Initialize ProgressBar
        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        billList = new ArrayList<>();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            billDatabase = FirebaseDatabase.getInstance().getReference("Bill").child(userId);
            DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            // Show ProgressBar while loading data
            progressBar.setVisibility(View.VISIBLE);

            // Fetch and display the username
            userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("name").getValue(String.class);
                        if (username != null) {
                            tvUsername.setText("Welcome, " + username);
                        }
                    }
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar after loading
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                    Toast.makeText(MainActivity2.this, "Failed to load username: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            billAdapter = new BillAdapter(this, billList, billDatabase);
            billRecyclerView.setAdapter(billAdapter);

            btnAddBill = findViewById(R.id.btnAddBill);
            btnAddBill.setOnClickListener(view -> startActivity(new Intent(MainActivity2.this, AddBillActivity.class)));

            // Load bills and update total balance
            loadBill();
            updateTotalBalance(tvBalance);
        } else {
            Toast.makeText(this, "Please log in to view bill.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void loadBill() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar while loading bills
        billDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bill expense = snapshot.getValue(Bill.class);
                    if (expense != null) {
                        expense.setBillId(snapshot.getKey());
                        billList.add(expense);
                    }
                }
                billAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE); // Hide ProgressBar after loading
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                Toast.makeText(MainActivity2.this, "Error loading bill: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalBalance(TextView tvBalance) {
        billDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalBalance = 0.0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bill bill = snapshot.getValue(Bill.class);
                    if (bill != null && bill.getAmount() != null) {
                        totalBalance += bill.getAmount();
                    }
                }

                // Update the TextView with the total balance
                tvBalance.setText("Total Balance: " + totalBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity2.this, "Failed to load balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}