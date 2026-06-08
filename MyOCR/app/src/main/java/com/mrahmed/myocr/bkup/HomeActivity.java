package com.mrahmed.myocr.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mrahmed.myocr.R;
import com.mrahmed.myocr.adapter.CategoryAdapter;
import com.mrahmed.myocr.model.CategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton addCategoryButton;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        addCategoryButton = findViewById(R.id.addCategoryButton);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryAdapter);

        loadCategories();

        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Category");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText categoryInput = view.findViewById(R.id.categoryInput);
        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String categoryName = categoryInput.getText().toString().trim();
            if (!TextUtils.isEmpty(categoryName)) {
                saveCategoryToFirebase(categoryName);
            } else {
                Toast.makeText(HomeActivity.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void saveCategoryToFirebase(String categoryName) {
        String categoryId = UUID.randomUUID().toString();
        Map<String, Object> category = new HashMap<>();
        category.put("id", categoryId);
        category.put("name", categoryName);

        db.collection("categories").document(categoryId)
                .set(category)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HomeActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Refresh List
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadCategories() {
        db.collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                categoryList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getString("id");
                    String name = document.getString("name");
                    categoryList.add(new CategoryModel(id, name));
                }
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }
}
