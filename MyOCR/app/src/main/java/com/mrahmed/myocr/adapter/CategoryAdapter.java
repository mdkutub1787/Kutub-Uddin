package com.mrahmed.myocr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mrahmed.myocr.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.FileViewHolder> {

    private List<String> documentIds;
    private List<String> fileContents;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String documentId);
    }

    public CategoryAdapter(List<String> documentIds, List<String> fileContents, OnItemClickListener listener) {
        this.documentIds = documentIds;
        this.fileContents = fileContents;
        this.listener = listener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_file_item, parent, false);
        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        String fileContent = fileContents.get(position);
        holder.bind(documentId, fileContent, listener);
    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileTextView;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileTextView = itemView.findViewById(R.id.fileTextView);
        }

        public void bind(String documentId, String fileContent, OnItemClickListener listener) {
            fileTextView.setText(fileContent);
            itemView.setOnClickListener(v -> listener.onItemClick(documentId));
        }
    }
}