package com.kutub.youngorganization.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.youngorganization.R;

import java.util.List;

public class DynamicEditAdapter extends RecyclerView.Adapter<DynamicEditAdapter.ViewHolder> {
    private final List<String> items;
    private final OnItemRemoveListener removeListener;
    private boolean editable = true;

    public interface OnItemRemoveListener {
        void onRemove(int position);
    }

    public DynamicEditAdapter(List<String> items, OnItemRemoveListener removeListener) {
        this.items = items;
        this.removeListener = removeListener;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.editText.setText(items.get(position));
        holder.editText.setEnabled(editable);
        holder.removeBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                items.set(holder.getAdapterPosition(), s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        holder.removeBtn.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onRemove(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        ImageButton removeBtn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.editText);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
} 