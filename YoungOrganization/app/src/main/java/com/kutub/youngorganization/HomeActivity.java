package com.kutub.youngorganization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.youngorganization.adapter.CarouselAdapter;
import com.kutub.youngorganization.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private ImageButton logoutButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, billsRef, carouselRef;
    private TextView homeProfileNameTV, homeProfileBalanceTV;
    private ViewPager2 carouselViewPager;
    private CarouselAdapter carouselAdapter;
    private final List<String> carouselItems = new ArrayList<>();
    private Timer carouselTimer;
    private final Handler carouselHandler = new Handler(Looper.getMainLooper());
    private ProgressBar homeProgressBar;
    private int dataLoadCount = 0;
    private final int TOTAL_LOADS = 3; // profile, balance, carousel
    private LinearLayout eventCalendarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        logoutButton = findViewById(R.id.logout_button);
        homeProfileNameTV = findViewById(R.id.homeProfileNameTV);
        homeProfileBalanceTV = findViewById(R.id.homeProfileBalanceTV);
        billsRef = FirebaseDatabase.getInstance().getReference("bills");
        carouselViewPager = findViewById(R.id.carouselViewPager);
        carouselRef = FirebaseDatabase.getInstance().getReference("carousel_messages");
        homeProgressBar = findViewById(R.id.homeProgressBar);
        homeProgressBar.setVisibility(View.VISIBLE);

        eventCalendarLayout = findViewById(R.id.eventCalendarLayout);
        eventCalendarLayout.setVisibility(View.GONE);

        ImageView nomineeImage = findViewById(R.id.nomineeImage);
        nomineeImage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RullsActivity.class);
            startActivity(intent);
        });

        ImageView ministryImage = findViewById(R.id.ministryImage);
        if (ministryImage == null) {
            // Try to find by src if id is missing
            for (int i = 0; i < ((ViewGroup) findViewById(android.R.id.content)).getChildCount(); i++) {
                View v = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(i);
                if (v instanceof ImageView && ((ImageView) v).getDrawable() != null) {
                    // Optionally check for government drawable
                }
            }
        } else {
            ministryImage.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, OrganizationTotalIncomeActivity.class);
                startActivity(intent);
            });
        }

        LinearLayout tvChannelLayout = findViewById(R.id.tvChannelLayout);
        tvChannelLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TVChannelActivity.class);
            startActivity(intent);
        });

        LinearLayout newsPaperLayout = findViewById(R.id.newsPaperLayout);
        newsPaperLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NewsPaperActivity.class);
            startActivity(intent);
        });

        LinearLayout bankLayout = findViewById(R.id.bankLayout);
        bankLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BankListActivity.class);
            startActivity(intent);
        });

        LinearLayout prominentPersonLayout = findViewById(R.id.prominentPersonLayout);
        prominentPersonLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProminentPersonActivity.class);
            startActivity(intent);
        });

        if (currentUser == null) {
            // Not logged in, redirect to LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        logoutButton.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("লগআউট নিশ্চিত করুন")
                .setMessage("আপনি কি লগআউট করতে চান?")
                .setPositiveButton("হ্যাঁ", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("না", null)
                .show();
        });

        loadUserProfile(currentUser.getUid());
        loadUserBalance(currentUser.getUid());
        setupCarousel();
        loadCarouselData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set Home as selected by default
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already on home, do nothing
                return true;
            } else if (id == R.id.nav_users) {
                startActivity(new Intent(HomeActivity.this, UserListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_menu) {
                startActivity(new Intent(HomeActivity.this, OrganizationInfoActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupCarousel() {
        carouselAdapter = new CarouselAdapter(carouselItems);
        carouselViewPager.setAdapter(carouselAdapter);

        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(carouselTimer != null) {
                    carouselTimer.cancel();
                    startCarouselAutoScroll();
                }
            }
        });
    }

    private void loadCarouselData() {
        carouselRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carouselItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String message = itemSnapshot.getValue(String.class);
                    if (message != null) {
                        carouselItems.add(message);
                    }
                }
                carouselAdapter.notifyDataSetChanged();
                if (carouselItems.size() > 1) {
                    startCarouselAutoScroll();
                }
                onDataLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataLoaded();
            }
        });
    }
    
    private void startCarouselAutoScroll() {
        if (carouselTimer != null) {
            carouselTimer.cancel();
        }
        carouselTimer = new Timer();
        carouselTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                carouselHandler.post(() -> {
                    int currentItem = carouselViewPager.getCurrentItem();
                    int nextItem = (currentItem + 1) % carouselItems.size();
                    carouselViewPager.setCurrentItem(nextItem, true);
                });
            }
        }, 3000, 3000); // Auto scroll every 3 seconds
    }

    private void loadUserProfile(String uid) {
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        homeProfileNameTV.setText(user.name);
                        if ("admin".equalsIgnoreCase(user.getRole())) {
                            eventCalendarLayout.setVisibility(View.VISIBLE);
                            eventCalendarLayout.setOnClickListener(v ->
                                    startActivity(new Intent(HomeActivity.this, AdminDashboardActivity.class)));
                        } else {
                            eventCalendarLayout.setVisibility(View.VISIBLE);
                            eventCalendarLayout.setOnClickListener(v ->
                                    startActivity(new Intent(HomeActivity.this, UserDashboardActivity.class)));
                        }
                    }
                }
                onDataLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataLoaded();
            }
        });
    }

    private void loadUserBalance(String uid) {
        billsRef.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalBalance = 0.0;
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    com.kutub.youngorganization.model.Bill bill = billSnapshot.getValue(com.kutub.youngorganization.model.Bill.class);
                    if (bill != null && bill.amount != null) {
                        try {
                            totalBalance += Double.parseDouble(bill.amount);
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                }
                homeProfileBalanceTV.setText(String.format("মোট জমা: %.2f টাকা", totalBalance));
                onDataLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataLoaded();
            }
        });
    }

    private void onDataLoaded() {
        dataLoadCount++;
        if (dataLoadCount >= TOTAL_LOADS && homeProgressBar != null) {
            homeProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (carouselTimer != null) {
            carouselTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set Home as selected when returning to this activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        if (carouselItems.size() > 1) {
            startCarouselAutoScroll();
        }
        // Always hide progress bar on resume
        if (homeProgressBar != null) {
            homeProgressBar.setVisibility(View.GONE);
        }
        // Always refresh balance on resume
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadUserBalance(currentUser.getUid());
        }
    }
} 