package com.kutub.billcreate.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.billcreate.R;
import com.kutub.billcreate.model.Bill;
import com.kutub.billcreate.model.User;

public class ProfileActivity extends AppCompatActivity {

    private EditText etProfileName, etProfilePhone;
    private TextView tvProfileName, tvProfilePhone, tvProfileEmail, tvBalance;
    private Button btnSaveProfile;

    private DatabaseReference databaseReference, billDatabase;
    private String userId;

    private FirebaseAuth auth;
    private ProgressBar progressBar; // Declare ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        progressBar = findViewById(R.id.progressBar); // Initialize ProgressBar
        etProfileName = findViewById(R.id.etProfileName);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvBalance = findViewById(R.id.tvBalance);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        billDatabase = FirebaseDatabase.getInstance().getReference("Bill").child(userId);

        // Get the logged-in user's email
        String email = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No Email Found";
        tvProfileEmail.setText("Email: " + email);

        // Fetch and display user profile data
        if (userId != null) {
            progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
            loadUserProfile();
        }

        // Save profile button click listener
        btnSaveProfile.setOnClickListener(view -> saveUserProfile(email));

        // Update total balance
        updateTotalBalance();
    }

    private void loadUserProfile() {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar after loading
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        tvProfileName.setText("Name: " + user.getName());
                        tvProfilePhone.setText("Phone Number: " + user.getPhoneNumber());
                        toggleProfileView(true);
                    }
                } else {
                    toggleProfileView(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                Toast.makeText(ProfileActivity.this, "Failed to load profile: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalBalance() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar while loading balance
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

                tvBalance.setText("Total Balance: " + totalBalance);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar after loading
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                Toast.makeText(ProfileActivity.this, "Failed to load balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleProfileView(boolean isProfileExists) {
        if (isProfileExists) {
            tvProfileName.setVisibility(View.VISIBLE);
            tvProfilePhone.setVisibility(View.VISIBLE);
            etProfileName.setVisibility(View.GONE);
            etProfilePhone.setVisibility(View.GONE);
            btnSaveProfile.setVisibility(View.GONE);
            tvBalance.setVisibility(View.VISIBLE);
        } else {
            tvProfileName.setVisibility(View.GONE);
            tvProfilePhone.setVisibility(View.GONE);
            etProfileName.setVisibility(View.VISIBLE);
            etProfilePhone.setVisibility(View.VISIBLE);
            btnSaveProfile.setVisibility(View.VISIBLE);
            tvBalance.setVisibility(View.GONE);
        }
    }

    private void saveUserProfile(String email) {
        String name = etProfileName.getText().toString().trim();
        String phone = etProfilePhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isNameDuplicate = false;
                boolean isPhoneDuplicate = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User existingUser = snapshot.getValue(User.class);
                    if (existingUser != null) {
                        if (existingUser.getName().equals(name)) {
                            isNameDuplicate = true;
                        }
                        if (existingUser.getPhoneNumber().equals(phone)) {
                            isPhoneDuplicate = true;
                        }
                    }
                }

                if (isNameDuplicate) {
                    Toast.makeText(ProfileActivity.this, "A profile with the same name already exists.", Toast.LENGTH_SHORT).show();
                } else if (isPhoneDuplicate) {
                    Toast.makeText(ProfileActivity.this, "A profile with the same phone number already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    saveProfileToDatabase(name, email, phone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileToDatabase(String name, String email, String phone) {
        User user = new User(userId, name, email, phone);

        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                recreate();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to Save Profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}