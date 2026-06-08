//package com.mrahmed.myocr.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.mrahmed.myocr.R;
//
//public class MainActivity2 extends AppCompatActivity {
//
//    private Button ocrScanBtn, viewListBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        viewListBtn = findViewById(R.id.viewListBtn);
//        ocrScanBtn = findViewById(R.id.ocrScanBtn);
//        viewListBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity2.this, HomeActivity.class);
//            startActivity(intent);
//        });
//
//        ocrScanBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity2.this, OcrActivity.class);
//            startActivity(intent);
//        });
//
//    }
//}