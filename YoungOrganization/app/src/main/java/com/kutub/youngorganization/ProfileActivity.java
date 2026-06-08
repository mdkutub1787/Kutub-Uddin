package com.kutub.youngorganization;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.Bill;
import com.kutub.youngorganization.model.User;
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView totalBalanceTV, profileNameTV, profileEmailTV, profileMobileTV;
    private ProgressBar balanceProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference billsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        totalBalanceTV = findViewById(R.id.totalBalanceTV);
        balanceProgressBar = findViewById(R.id.balanceProgressBar);
        profileNameTV = findViewById(R.id.profileNameTV);
        profileEmailTV = findViewById(R.id.profileEmailTV);
        profileMobileTV = findViewById(R.id.profileMobileTV);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        billsRef = FirebaseDatabase.getInstance().getReference("bills");

        loadUserProfile();
        calculateTotalBalance(currentUser.getUid());

        MaterialButton editProfileBtn = findViewById(R.id.editProfileBtn);
        if (editProfileBtn != null) {
            editProfileBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1001);
            });
        }

        LinearLayout fbLayout = findViewById(R.id.fbLayout);
        ImageButton fbIconBtn = findViewById(R.id.profileFbIconBtn);
        TextView fbLinkTV = findViewById(R.id.profileFbLinkTV);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        profileNameTV.setText(user.name);
                        profileEmailTV.setText(user.email);
                        profileMobileTV.setText(user.mobileNumber);
                        if (user.fbLink != null && !user.fbLink.trim().isEmpty()) {
                            fbLayout.setVisibility(View.VISIBLE);
                            String fbLink = user.fbLink.trim();
                            String display = fbLink.replaceAll("^(https?://)?(www\\.)?facebook\\.com/", "").replaceAll("^@", "");
                            fbLinkTV.setText(display);
                            View.OnClickListener openFb = v -> {
                                String url = fbLink;
                                if (!url.startsWith("http")) {
                                    url = "https://facebook.com/" + display;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            };
                            fbIconBtn.setOnClickListener(openFb);
                            fbLinkTV.setOnClickListener(openFb);
                        } else {
                            fbLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
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

    private void loadUserProfile() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        profileNameTV.setText(user.name);
                        profileEmailTV.setText(user.email);
                        profileMobileTV.setText(user.mobileNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateTotalBalance(String uid) {
        balanceProgressBar.setVisibility(View.VISIBLE);
        totalBalanceTV.setVisibility(View.INVISIBLE);

        Query userBillsQuery = billsRef.orderByChild("userId").equalTo(uid);
        userBillsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalBalance = 0.0;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    Bill bill = billSnapshot.getValue(Bill.class);
                    if (bill != null && bill.amount != null) {
                        try {
                            totalBalance += Double.parseDouble(bill.amount);
                        } catch (NumberFormatException e) {
                            // Ignore if amount is not a valid number
                        }
                    }
                }
                totalBalanceTV.setText(String.format(Locale.getDefault(), "৳ %.2f", totalBalance));
                balanceProgressBar.setVisibility(View.GONE);
                totalBalanceTV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                balanceProgressBar.setVisibility(View.GONE);
                totalBalanceTV.setVisibility(View.VISIBLE);
                totalBalanceTV.setText("Error");
                Toast.makeText(ProfileActivity.this, "Failed to calculate balance.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            balanceProgressBar.setVisibility(View.VISIBLE);
            totalBalanceTV.setVisibility(View.INVISIBLE);
            loadUserProfile();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                calculateTotalBalance(currentUser.getUid());
            }
        }
    }
}
 