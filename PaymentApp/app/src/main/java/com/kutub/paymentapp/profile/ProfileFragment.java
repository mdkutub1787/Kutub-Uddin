package com.kutub.paymentapp.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.paymentapp.R;
import com.kutub.paymentapp.model.User;
import com.kutub.paymentapp.ui.MainActivity;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private EditText etProfileName, etProfilePhone, etBalance;
    private TextView tvProfileName, tvProfilePhone, tvProfileEmail, tvBalance;
    private Button btnSaveProfile;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private String userId;
    private FirebaseAuth auth;
    private boolean isNewProfile = false;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews(view);
        initializeFirebase();

        String email = getUserEmail();
        tvProfileEmail.setText("Email: " + email);

        if (userId != null) {
            progressBar.setVisibility(View.VISIBLE);
            loadUserProfile();
        }

        btnSaveProfile.setOnClickListener(v -> saveUserProfile(email));

        return view;
    }

    private void initializeViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        etProfileName = view.findViewById(R.id.etProfileName);
        etProfilePhone = view.findViewById(R.id.etProfilePhone);
        etBalance = view.findViewById(R.id.etBalance);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfilePhone = view.findViewById(R.id.tvProfilePhone);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvBalance = view.findViewById(R.id.tvBalance);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
    }

    private void initializeFirebase() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            showToast("User is not authenticated");
            return;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = auth.getCurrentUser().getUid();
    }

    private String getUserEmail() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "No Email Found";
    }

    private void loadUserProfile() {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    isNewProfile = false;
                    // Retrieve the User data, but don't cast the balance yet
                    Object balanceObject = dataSnapshot.child("balance").getValue();

                    if (balanceObject instanceof Long || balanceObject instanceof String || balanceObject instanceof Double ) {
                        user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            displayUserProfile(user, balanceObject);
                        }
                    } else {
                        Log.e(TAG, "User balance is incorrect");
                        showToast("User balance is incorrect");
                    }

                    toggleProfileView(true);
                } else {
                    isNewProfile = true;
                    toggleProfileView(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                showToast("Failed to load profile: " + databaseError.getMessage());
                Log.e(TAG, "Failed to load profile: ", databaseError.toException());
            }
        });
    }

    private void displayUserProfile(User user, Object balanceObject) {
        tvProfileName.setText("Name: " + user.getName());
        tvProfilePhone.setText("Phone Number: " + user.getPhoneNumber());
        String userBalanceString;
        if (balanceObject instanceof Long) {
            userBalanceString = String.valueOf(balanceObject);
        } else if (balanceObject instanceof String){
            userBalanceString = (String) balanceObject;
        }else if (balanceObject instanceof Double) {
            userBalanceString = String.valueOf(balanceObject);
        }else {
            userBalanceString = "Error";
            Log.e(TAG,"The user balance is neither a String nor a Long");
        }
        tvBalance.setText("Balance: " + userBalanceString);
    }

    private void toggleProfileView(boolean isProfileExists) {
        int visible = isProfileExists ? View.VISIBLE : View.GONE;
        int gone = isProfileExists ? View.GONE : View.VISIBLE;

        tvProfileName.setVisibility(visible);
        tvProfilePhone.setVisibility(visible);
        tvBalance.setVisibility(visible);
        etProfileName.setVisibility(gone);
        etProfilePhone.setVisibility(gone);
        etBalance.setVisibility(gone);
        btnSaveProfile.setVisibility(gone);

        if (!isProfileExists) {
            etProfileName.setVisibility(View.VISIBLE);
            etProfilePhone.setVisibility(View.VISIBLE);
            etBalance.setVisibility(View.VISIBLE);
            btnSaveProfile.setVisibility(View.VISIBLE);
        }
    }

    private void saveUserProfile(String email) {
        String name = etProfileName.getText().toString().trim();
        String phone = etProfilePhone.getText().toString().trim();
        String balanceStr = etBalance.getText().toString().trim();

        if (!isValidInput(name, phone, balanceStr)) return;

        saveProfileToDatabase(name, email, phone, isNewProfile, balanceStr);
    }

    private boolean isValidInput(String name, String phone, String balanceStr) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(balanceStr)) {
            showToast(getString(R.string.fill_all_fields));
            return false;
        }
        return true;
    }

    private void saveProfileToDatabase(String name, String email, String phone, boolean isNewProfile, String balanceStr) {
        String currentBalance = isNewProfile ? balanceStr : (String) user.getBalance();
        if(currentBalance == null) {
            currentBalance = "Error";
        }
        User newUser = new User(userId, name, email, phone);
        newUser.setBalance(currentBalance);

        databaseReference.child(userId).setValue(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Profile Saved Successfully");
                loadUserProfile();
                updateNavHeader();
            } else {
                showToast("Failed to Save Profile: " + task.getException().getMessage());
                Log.e(TAG, "Failed to save profile: ", task.getException());
            }
        });
    }

    private void updateNavHeader() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateNavHeader(mainActivity.getBinding().navView, userId);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}