package com.kutub.billcreate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.kutub.billcreate.R;
import com.kutub.billcreate.model.Bill;
import com.kutub.billcreate.ui.EditBillActivity;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> billList;
    private Context context;
    private DatabaseReference databaseReference;

    public BillAdapter(Context context, List<Bill> billList, DatabaseReference databaseReference) {
        this.context = context;
        this.billList = billList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new BillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill currentBill = billList.get(position);

        if (currentBill != null) {
            holder.billMonth.setText(currentBill.getMonth());
            holder.billAmount.setText(String.valueOf(currentBill.getAmount()));

            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditBillActivity.class);
                intent.putExtra("billId", currentBill.getBillId());
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                if (databaseReference != null && currentBill.getBillId() != null) {
                    // Show confirmation dialog
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Confirmation")
                            .setMessage("Are you sure you want to delete this bill?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                databaseReference.child(currentBill.getBillId()).removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            if (position >= 0 && position < billList.size()) { // Ensure position is valid
                                                billList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, billList.size());
                                                Toast.makeText(context, "Bill deleted successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Failed to delete bill: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    Toast.makeText(context, "Error: Invalid bill or database reference.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        public TextView billMonth;
        public TextView billAmount;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public BillViewHolder(View itemView) {
            super(itemView);
            billMonth = itemView.findViewById(R.id.tvBillMonth);
            billAmount = itemView.findViewById(R.id.tvBillAmount);
            editButton = itemView.findViewById(R.id.btnEditBill);
            deleteButton = itemView.findViewById(R.id.btnDeleteBill);
        }
    }
}