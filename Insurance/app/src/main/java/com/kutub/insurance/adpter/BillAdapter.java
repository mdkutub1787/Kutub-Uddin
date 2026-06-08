package com.kutub.insurance.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kutub.insurance.R;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<PolicyResponse> policyList;
    private List<BillResponse> billList;

    // Constructor
    public BillAdapter() {
        this.policyList = new ArrayList<>();
        this.billList = new ArrayList<>();
    }

    public void updateData(List<PolicyResponse> policyList, List<BillResponse> newBillList) {
        this.policyList = policyList != null ? policyList : new ArrayList<>();
        this.billList = newBillList != null ? newBillList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (position < policyList.size() && position < billList.size()) {
            PolicyResponse policy = policyList.get(position);
            BillResponse bill = billList.get(position);

            // Set data from PolicyResponse
            holder.tvPolicyholder.setText("Policyholder: " + policy.getPolicyholder());
            holder.tvBankName.setText("Bank: " + policy.getBankname());
            holder.tvSumInsured.setText("Sum Insured: " + policy.getSumInsured());

            // Set data from BillResponse
            holder.tvFire.setText("Fire: " + bill.getFire());
            holder.tvRsd.setText("RSD: " + bill.getRsd());
            holder.tvTax.setText("Tax: " + bill.getTax());
            holder.tvNetPremium.setText("Net Premium: " + bill.getNetPremium());
            holder.tvGrossPremium.setText("Gross Premium: " + bill.getGrossPremium());
        }
    }

    @Override
    public int getItemCount() {
        // Return the minimum size of the two lists
        return Math.min(policyList.size(), billList.size());
    }

    // ViewHolder class
    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvPolicyholder, tvBankName, tvSumInsured, tvFire, tvRsd, tvTax, tvNetPremium, tvGrossPremium;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPolicyholder = itemView.findViewById(R.id.tvPolicyholder);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvSumInsured = itemView.findViewById(R.id.tvSumInsured);
            tvFire = itemView.findViewById(R.id.tvFire);
            tvRsd = itemView.findViewById(R.id.tvRsd);
            tvTax = itemView.findViewById(R.id.tvTax);
            tvNetPremium = itemView.findViewById(R.id.tvNetPremium);
            tvGrossPremium = itemView.findViewById(R.id.tvGrossPremium);
        }
    }
}