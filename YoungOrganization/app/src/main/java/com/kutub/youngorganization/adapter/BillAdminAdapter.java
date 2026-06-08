package com.kutub.youngorganization.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.youngorganization.BillDetailsActivity;
import com.kutub.youngorganization.R;
import com.kutub.youngorganization.model.Bill;
import com.kutub.youngorganization.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BillAdminAdapter extends RecyclerView.Adapter<BillAdminAdapter.BillViewHolder> implements Filterable {
    private Context context;
    private List<Bill> originalList;
    private List<Bill> filteredList;
    private Map<String, User> userCache = new HashMap<>();
    private DatabaseReference usersRef;

    public BillAdminAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.originalList = new ArrayList<>(billList);
        this.filteredList = new ArrayList<>(billList);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void setData(List<Bill> billList) {
        this.originalList = new ArrayList<>(billList);
        this.filteredList = new ArrayList<>(billList);
        notifyDataSetChanged();
    }

    public void applyFilters(String name, String month, String year, String amount) {
        List<Bill> tempFiltered = new ArrayList<>(originalList);
        // Filter by name
        if (name != null && !name.equals("সব")) {
            tempFiltered.removeIf(bill -> {
                String userName = userCache.get(bill.userId) != null ? userCache.get(bill.userId).name : "";
                return !name.equals(userName);
            });
        }
        // Filter by month
        if (month != null && !month.equals("সব")) {
            tempFiltered.removeIf(bill -> !month.equals(bill.month));
        }
        // Filter by year
        if (year != null && !year.equals("সব")) {
            tempFiltered.removeIf(bill -> !year.equals(bill.year));
        }
        // Filter by amount
        if (amount != null && !amount.equals("সব")) {
            tempFiltered.removeIf(bill -> !amount.equals(bill.amount));
        }
        filteredList = tempFiltered;
        notifyDataSetChanged();
    }

    public Set<String> getUniqueMonths() {
        Set<String> months = new HashSet<>();
        for (Bill bill : originalList) {
            if (bill.month != null) months.add(bill.month);
        }
        return months;
    }
    public Set<String> getUniqueYears() {
        Set<String> years = new HashSet<>();
        for (Bill bill : originalList) {
            if (bill.year != null) years.add(bill.year);
        }
        return years;
    }
    public Set<String> getUniqueAmounts() {
        Set<String> amounts = new HashSet<>();
        for (Bill bill : originalList) {
            if (bill.amount != null) amounts.add(bill.amount);
        }
        return amounts;
    }
    public Set<String> getUniqueNames() {
        Set<String> names = new HashSet<>();
        for (Bill bill : originalList) {
            if (userCache.containsKey(bill.userId)) {
                String name = userCache.get(bill.userId).name;
                if (name != null) names.add(name);
            }
        }
        return names;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_admin, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = filteredList.get(position);
        holder.userMonthTV.setText(bill.month);
        holder.userYearTV.setText(bill.year);
        holder.billDetailsTV.setText(bill.amount);
        holder.userNameTV.setText("লোড হচ্ছে...");

        if (userCache.containsKey(bill.userId)) {
            holder.userNameTV.setText(userCache.get(bill.userId).name);
        } else {
            usersRef.child(bill.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userCache.put(bill.userId, user);
                        holder.userNameTV.setText(user.name);
                    } else {
                        holder.userNameTV.setText("অজানা");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    holder.userNameTV.setText("ত্রুটি হয়েছে");
                }
            });
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BillDetailsActivity.class);
            intent.putExtra("billId", bill.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    String filterString = constraint.toString().toLowerCase();
                    List<Bill> filtered = new ArrayList<>();
                    for (Bill bill : originalList) {
                        String userName = "";
                        if (userCache.containsKey(bill.userId)) {
                            userName = userCache.get(bill.userId).name.toLowerCase();
                        }
                        if (userName.contains(filterString) ||
                            (bill.month != null && bill.month.toLowerCase().contains(filterString)) ||
                            (bill.amount != null && bill.amount.toLowerCase().contains(filterString))) {
                            filtered.add(bill);
                        }
                    }
                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Bill>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTV, userMonthTV, userYearTV, billDetailsTV;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.userNameTV);
            userMonthTV = itemView.findViewById(R.id.userMonthTV);
            userYearTV = itemView.findViewById(R.id.userYearTV);
            billDetailsTV = itemView.findViewById(R.id.billDetailsTV);
        }
    }

    public Map<String, User> getUserCache() {
        return userCache;
    }
}