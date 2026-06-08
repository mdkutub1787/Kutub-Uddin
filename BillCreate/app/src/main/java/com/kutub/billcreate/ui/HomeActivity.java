package com.kutub.billcreate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kutub.billcreate.R;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView tvWelcome;
    private Button btnLogin, btnRegister, btnAddActivity, btnViewActivity, btnSaveProfile, btnLogout;

    private Handler sessionHandler;
    private Runnable sessionTimeoutRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnAddActivity = findViewById(R.id.btnAddActivity);
        btnViewActivity = findViewById(R.id.btnViewActivity);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Set welcome message
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "User";
        tvWelcome.setText("              Welcome " + "\n" + userEmail + "!");

        // Initialize session timeout handler
        sessionHandler = new Handler();
        sessionTimeoutRunnable = () -> {
            // Log out the user after 5 minutes
            if (auth.getCurrentUser() != null) {
                auth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        };

        // Start the session timeout countdown (5 minutes = 300,000 ms)
        sessionHandler.postDelayed(sessionTimeoutRunnable, 300000); // 5 minutes

        // Button functionalities
        btnLogin.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, RegisterActivity.class)));
        btnAddActivity.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, AddBillActivity.class)));
        btnViewActivity.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MainActivity2.class)));
        btnSaveProfile.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        btnLogout.setOnClickListener(view -> {
            auth.signOut();
            sessionHandler.removeCallbacks(sessionTimeoutRunnable); // Stop session timeout
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the session timeout callback to prevent memory leaks
        if (sessionHandler != null && sessionTimeoutRunnable != null) {
            sessionHandler.removeCallbacks(sessionTimeoutRunnable);
        }
    }
}