package com.kutub.billcreate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kutub.billcreate.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Check if a user is already logged in
        if (auth.getCurrentUser() != null) {
            // Redirect to HomeActivity if logged in
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(view -> {
            String email = etEmailLogin.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(view -> {
            // Prevent navigation to RegisterActivity if logged in
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "You must log out to create a new account.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}