package com.kutub.youngorganization.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.youngorganization.R;
import com.kutub.youngorganization.model.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTV.setText(user.getName());
        holder.userMobileTV.setText(user.getMobileNumber());

        holder.callUserBtn.setOnClickListener(v -> {
            String mobileNumber = user.getMobileNumber();
            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Mobile number not available", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton fbIconBtn = holder.itemView.findViewById(R.id.userFbIconBtn);
        if (user.fbLink != null && !user.fbLink.trim().isEmpty()) {
            fbIconBtn.setVisibility(View.VISIBLE);
            fbIconBtn.setOnClickListener(v -> {
                String fbLink = user.fbLink;
                if (!fbLink.startsWith("http://") && !fbLink.startsWith("https://")) {
                    fbLink = "https://facebook.com/" + fbLink.replaceAll("^@", "");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbLink));
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Facebook link", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            fbIconBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTV, userMobileTV;
        TextView callUserBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.userNameTV);
            userMobileTV = itemView.findViewById(R.id.userMobileTV);
            callUserBtn = itemView.findViewById(R.id.callUserBtn);
        }
    }
} 