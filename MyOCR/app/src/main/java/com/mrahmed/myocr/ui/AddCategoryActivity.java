package com.mrahmed.myocr.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mrahmed.myocr.R;

public class AddCategoryActivity extends AppCompatActivity {

    private TextInputEditText categoryNameEditText;
    private Button addCategoryButton;
    private LinearLayout colorPaletteLayout;
    private String selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        colorPaletteLayout = findViewById(R.id.colorPaletteLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                if (!categoryName.isEmpty() && selectedColor != null) {
                    Toast.makeText(AddCategoryActivity.this, "Category Name: " + categoryName + "\nColor: " + selectedColor, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCategoryActivity.this, "Please enter a category name and select a color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupColorPalette();
    }

    private void setupColorPalette() {
        String[] colors = getResources().getStringArray(R.array.category_colors);

        for (String color : colors) {
            View colorView = createColorView(color);
            colorPaletteLayout.addView(colorView);
        }
    }

    private View createColorView(String color) {
        View colorView = new View(this);
        int size = getResources().getDimensionPixelSize(R.dimen.color_view_size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(8, 0, 8, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        colorView.setLayoutParams(params);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.parseColor(color));
        colorView.setBackground(drawable);
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = color;
                selectColorView(v);
            }
        });
        return colorView;
    }

    private void selectColorView(View selectedView) {
        for (int i = 0; i < colorPaletteLayout.getChildCount(); i++) {
            View child = colorPaletteLayout.getChildAt(i);
            GradientDrawable drawable = (GradientDrawable) child.getBackground();
            drawable.setStroke(0, ContextCompat.getColor(this, android.R.color.transparent));
        }
        GradientDrawable drawable = (GradientDrawable) selectedView.getBackground();
        drawable.setStroke(4, ContextCompat.getColor(this, R.color.black));
    }
}