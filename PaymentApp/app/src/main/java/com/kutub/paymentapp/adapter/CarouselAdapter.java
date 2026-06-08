package com.kutub.paymentapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.paymentapp.R;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private final List<Integer> imageList;
    private final List<String> titleList; // List of Bangla titles

    public CarouselAdapter(List<String> titleList, List<Integer> imageList) {
        this.imageList = imageList;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        // Set the title first
        holder.titleView.setText(titleList.get(position));
        holder.titleView.setVisibility(View.VISIBLE);

        // Set the image after the title
        holder.imageView.setImageResource(imageList.get(position));

        // Log the title for debugging
        Log.d("CarouselAdapter", "Title: " + titleList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImage);
            titleView = itemView.findViewById(R.id.carouselTitle);
        }
    }
}