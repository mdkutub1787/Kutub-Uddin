package com.kutub.youngorganization.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.youngorganization.R;
import com.kutub.youngorganization.model.Bill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillUserAdapter extends RecyclerView.Adapter<BillUserAdapter.BillViewHolder> {
    private Context context;
    private List<Bill> originalList;
    private List<Bill> filteredList;

    public BillUserAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.originalList = new ArrayList<>(billList);
        this.filteredList = billList;
    }

    public void setData(List<Bill> billList) {
        this.originalList = new ArrayList<>(billList);
        this.filteredList = new ArrayList<>(this.originalList);
        notifyDataSetChanged();
    }

    public void applyFilters(String month, String year, String amount) {
        List<Bill> tempFiltered = new ArrayList<>(originalList);
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
            if (bill.month != null && !bill.month.trim().isEmpty()) {
                months.add(bill.month);
            }
        }
        return months;
    }
    public Set<String> getUniqueYears() {
        Set<String> years = new HashSet<>();
        for (Bill bill : originalList) {
            if (bill.year != null && !bill.year.trim().isEmpty()) {
                years.add(bill.year);
            }
        }
        return years;
    }
    public Set<String> getUniqueAmounts() {
        Set<String> amounts = new HashSet<>();
        for (Bill bill : originalList) {
            if (bill.amount != null && !bill.amount.trim().isEmpty()) {
                amounts.add(bill.amount);
            }
        }
        return amounts;
    }



    private int getMonthIndex(String month) {
        if (month == null) return -1;
        String[] months = context.getResources().getStringArray(R.array.months_array);
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) return i;
        }
        return -1; // Not found
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_user, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = filteredList.get(position);
        holder.monthTV.setText(bill.month);
        holder.yearTV.setText(bill.year);
        holder.amountTV.setText(bill.amount);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView monthTV, yearTV, amountTV;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTV = itemView.findViewById(R.id.userMonthTV);
            yearTV = itemView.findViewById(R.id.userYearTV);
            amountTV = itemView.findViewById(R.id.billAmountTV);
        }
    }
} 