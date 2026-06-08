package com.kutub.youngorganization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText nameET, emailET, mobileET;
    private MaterialButton saveBtn;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        nameET = findViewById(R.id.editNameET);
        emailET = findViewById(R.id.editEmailET);
        mobileET = findViewById(R.id.editMobileET);
        saveBtn = findViewById(R.id.saveProfileBtn);

        LinearLayout fbEditLayout = findViewById(R.id.fbEditLayout);
        ImageButton fbIconBtn = findViewById(R.id.profileEditFbIconBtn);
        TextView fbLinkTV = findViewById(R.id.profileEditFbLinkTV);
        EditText fbLinkET = findViewById(R.id.editFbLinkET);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        // Load current profile data from intent extras if needed (optional)
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String mobile = snapshot.child("mobileNumber").getValue(String.class);
                String fbLink = snapshot.child("fbLink").getValue(String.class);
                nameET.setText(name);
                emailET.setText(email);
                mobileET.setText(mobile);
                if (fbLink != null && !fbLink.trim().isEmpty()) {
                    String fbDisplay = fbLink
                        .replace("https://facebook.com/", "")
                        .replace("http://facebook.com/", "")
                        .replace("facebook.com/", "")
                        .replace("@", "")
                        .trim();
                    fbLinkET.setText(fbDisplay);
                } else {
                    fbLinkET.setText("");
                }
            }
        });

        saveBtn.setOnClickListener(v -> {
            String newName = nameET.getText().toString().trim();
            String newEmail = emailET.getText().toString().trim();
            String newMobile = mobileET.getText().toString().trim();
            String newFbLink = fbLinkET.getText().toString().trim();

            userRef.child("name").setValue(newName);
            userRef.child("email").setValue(newEmail);
            userRef.child("mobileNumber").setValue(newMobile);
            userRef.child("fbLink").setValue(newFbLink);

            Toast.makeText(this, "প্রোফাইল আপডেট হয়েছে", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });

        fbLinkET.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fbLink = s.toString().trim();
                if (!fbLink.isEmpty()) {
                    fbEditLayout.setVisibility(View.VISIBLE);
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
                    fbEditLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
} 