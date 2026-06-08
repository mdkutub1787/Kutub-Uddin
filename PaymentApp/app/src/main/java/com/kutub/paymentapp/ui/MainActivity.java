package com.kutub.paymentapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.paymentapp.R;
import com.kutub.paymentapp.databinding.ActivityMainBinding;
import com.kutub.paymentapp.model.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference billDatabaseReference;
    private FirebaseAuth auth;
    private String userId;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar Setup
        Toolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);

        // Drawer Layout and Navigation Setup
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Firebase Initialization
        auth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        currentUser = auth.getCurrentUser();
        userId = currentUser != null ? currentUser.getUid() : null;

        if (userId != null) {
            billDatabaseReference = FirebaseDatabase.getInstance().getReference("Bill").child(userId);
            updateNavHeader(navigationView, userId);
        } else {
            Log.e(TAG, "Error no user found.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser != null) {
            updateNavHeader(binding.navView, userId);
        }
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public void updateNavHeader(NavigationView navigationView, String userId) {
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);
        TextView navHeaderPhone = headerView.findViewById(R.id.nav_header_phone);
        TextView navHeaderBalance = headerView.findViewById(R.id.nav_header_balence);

        if (userId == null) {
            // Reset fields if no user is logged in
            setDefaultValue(navHeaderName, navHeaderEmail, navHeaderPhone, navHeaderBalance);
            return;
        }

        userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use the DataSnapshot to get the values directly
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String userPhone = dataSnapshot.child("phoneNumber").getValue(String.class);
                    //Retrieve as object and then check the type
                    Object balanceObject = dataSnapshot.child("balance").getValue();
                    String userBalance;
                    if (balanceObject instanceof Long) {
                        userBalance = String.valueOf(balanceObject);
                    } else if (balanceObject instanceof String){
                        userBalance = (String) balanceObject;
                    } else {
                        userBalance = "Error";
                        Log.e(TAG,"The user balance is neither a String nor a Long");
                    }

                    if (userName != null && userEmail != null && userPhone != null) {
                        navHeaderName.setText(userName);
                        navHeaderEmail.setText(userEmail);
                        navHeaderPhone.setText(userPhone);
                        updateBalance(navHeaderBalance, userBalance);
                    } else {
                        Log.e(TAG, "User data is incomplete");
                        setDefaultValue(navHeaderName, navHeaderEmail, navHeaderPhone, navHeaderBalance);
                    }
                } else {
                    Log.e(TAG, "User not found in database.");
                    setDefaultValue(navHeaderName, navHeaderEmail, navHeaderPhone, navHeaderBalance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load user data", databaseError.toException());
                setDefaultValue(navHeaderName, navHeaderEmail, navHeaderPhone, navHeaderBalance);
            }
        });
    }

    private void setDefaultValue(TextView navHeaderName, TextView navHeaderEmail, TextView navHeaderPhone, TextView navHeaderBalance) {
        navHeaderName.setText("Error");
        navHeaderEmail.setText("Error");
        navHeaderPhone.setText("Error");
        navHeaderBalance.setText("Balance: Error");
    }

    private void updateBalance(TextView navHeaderBalance, String initialBalance) {
        if (billDatabaseReference == null) {
            navHeaderBalance.setText("Balance: " + initialBalance);
            return;
        }

        billDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalBalance = 0.0;

                try {
                    totalBalance = Double.parseDouble(initialBalance);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid balance format: " + initialBalance, e);
                }

                // Loop through bill records and adjust balance
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String type = snapshot.child("type").getValue(String.class);
                    String amountStr = snapshot.child("amount").getValue(String.class);
                    if (type != null && amountStr != null) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            if ("add".equals(type)) {
                                totalBalance += amount;
                            } else if ("sub".equals(type)) {
                                totalBalance -= amount;
                            }
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Invalid amount format: " + amountStr, e);
                        }
                    }
                }

                navHeaderBalance.setText("Balance: " + totalBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load bill data", databaseError.toException());
                navHeaderBalance.setText("Balance: Error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}