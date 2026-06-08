package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET, mobileET, emailET, passwordET, fbLinkET;
    private Button registerBtn;
    private TextView loginTV;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        nameET = findViewById(R.id.nameET);
        mobileET = findViewById(R.id.mobileET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        fbLinkET = findViewById(R.id.fbLinkET);
        registerBtn = findViewById(R.id.registerBtn);
        loginTV = findViewById(R.id.loginTV);
        progressBar = findViewById(R.id.progressBar);

        registerBtn.setOnClickListener(v -> registerUser());
        loginTV.setOnClickListener(v -> finish()); // Go back to LoginActivity
    }

    private void registerUser() {
        String name = nameET.getText().toString().trim();
        String mobile = mobileET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String fbLink = fbLinkET.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Determine role based on email and proceed with registration
        determineRoleAndRegister(name, mobile, email, password, fbLink);
    }

    private void determineRoleAndRegister(String name, String mobile, String email, String password, String fbLink) {
        DatabaseReference adminEmailsRef = FirebaseDatabase.getInstance().getReference("admin_emails");
        String encodedEmail = email.replace(".", ",");

        adminEmailsRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = "user"; // Default role
                if (snapshot.exists() && snapshot.getValue(Boolean.class) == Boolean.TRUE) {
                    role = "admin"; // Set role to admin if email is in the list
                }
                
                // Proceed with registration
                String finalRole = role;
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            User user = new User(userId, name, mobile, email, finalRole, fbLink);

                            mDatabase.child(userId).setValue(user).addOnCompleteListener(dbTask -> {
                                progressBar.setVisibility(View.GONE);
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "অস্থির করে", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 