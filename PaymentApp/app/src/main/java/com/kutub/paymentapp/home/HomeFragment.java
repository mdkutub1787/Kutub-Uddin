package com.kutub.paymentapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kutub.paymentapp.R;
import com.kutub.paymentapp.adapter.CarouselAdapter;
import com.kutub.paymentapp.databinding.FragmentHomeBinding;
import com.kutub.paymentapp.ui.SendMoneyActivity;  // Add this import

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Handler carouselHandler;
    private Runnable carouselRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize ViewModel (if needed for future use)
        new ViewModelProvider(this).get(HomeViewModel.class);

        // Set up the carousel
        setupCarousel();

        // Set up OnClickListener for "Send Money" button
        binding.btnAddActivity.setOnClickListener(v -> {
            // Intent to open the SendMoneyActivity when clicked
            Intent intent = new Intent(getActivity(), SendMoneyActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void setupCarousel() {
        // List of Bangla titles
        List<String> titleList = new ArrayList<>();
        titleList.add("আমাদের সেরা পণ্য");
        titleList.add("বিশেষ অফার");
        titleList.add("নতুন কালেকশন");
        titleList.add("আজই কিনুন");
        titleList.add("বিশ্বস্ত সেবা");

        // List of images for the carousel
        List<Integer> imageList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            imageList.add(R.drawable.logo);
        }

        // Set up the adapter
        CarouselAdapter adapter = new CarouselAdapter(titleList, imageList);
        binding.viewPagerCarousel.setAdapter(adapter);

        // Auto-scroll the carousel every 5 seconds
        carouselHandler = new Handler();
        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPagerCarousel.getCurrentItem();
                int nextItem = (currentItem + 1) % imageList.size();
                binding.viewPagerCarousel.setCurrentItem(nextItem, true);
                carouselHandler.postDelayed(this, 5000);
            }
        };
        carouselHandler.postDelayed(carouselRunnable, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (carouselHandler != null && carouselRunnable != null) {
            carouselHandler.removeCallbacks(carouselRunnable);
        }
        binding = null;
    }
}
