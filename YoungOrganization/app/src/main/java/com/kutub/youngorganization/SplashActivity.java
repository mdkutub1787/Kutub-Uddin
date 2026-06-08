package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar splashProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashProgressBar = findViewById(R.id.splashProgressBar);
        splashProgressBar.setVisibility(View.VISIBLE);

        // Glide GIF loading for splash_loading
        ImageView splashLoadingImage = findViewById(R.id.splashLoadingImage);
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_loading)
            .into(splashLoadingImage);

        new Handler().postDelayed(() -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser == null) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 2000); // 2 seconds delay

        loadSplashData();
    }

    private void checkUserRole(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if ("admin".equals(role)) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void loadSplashData() {
        // Data loading logic
        // ...
        // Data loaded:
        splashProgressBar.setVisibility(View.GONE);
    }
} 