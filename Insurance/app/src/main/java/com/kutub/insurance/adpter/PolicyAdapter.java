package com.kutub.insurance.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.insurance.R;
import com.kutub.insurance.model.PolicyResponse;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder> {

    private List<PolicyResponse> policyResponseList;

    public PolicyAdapter(List<PolicyResponse> policyResponseList) {
        this.policyResponseList = policyResponseList;
    }

    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_item, parent, false);
        return new PolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, int position) {
        PolicyResponse policyResponse = policyResponseList.get(position);
        holder.tvDate.setText("Date: " + policyResponse.getDate());
        holder.tvPolicyholder.setText("Policyholder: " + policyResponse.getPolicyholder());
        holder.tvBankName.setText("Bank Name: " + policyResponse.getBankname());
        holder.tvAddress.setText("Address: " + policyResponse.getAddress());
        holder.tvSumInsured.setText("Sum Insured: " + policyResponse.getSumInsured());
        holder.tvCoverage.setText("Coverage: " + policyResponse.getCoverage());
        holder.tvConstruction.setText("Construction: " + policyResponse.getConstruction());
        holder.tvUsedAs.setText("Used As: " + policyResponse.getUsedAs());
        holder.tvPeriodFrom.setText("Period From: " + policyResponse.getPeriodFrom());
        holder.tvPeriodTo.setText("Period To: " + policyResponse.getPeriodTo());
    }

    @Override
    public int getItemCount() {
        return policyResponseList.size();
    }

    public static class PolicyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvPolicyholder, tvBankName, tvAddress, tvSumInsured, tvCoverage, tvConstruction, tvUsedAs, tvPeriodFrom, tvPeriodTo;

        public PolicyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPolicyholder = itemView.findViewById(R.id.tvPolicyholder);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvSumInsured = itemView.findViewById(R.id.tvSumInsured);
            tvCoverage = itemView.findViewById(R.id.tvCoverage);
            tvConstruction = itemView.findViewById(R.id.tvConstruction);
            tvUsedAs = itemView.findViewById(R.id.tvUsedAs);
            tvPeriodFrom = itemView.findViewById(R.id.tvPeriodFrom);
            tvPeriodTo = itemView.findViewById(R.id.tvPeriodTo);
        }
    }
}