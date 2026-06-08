package com.mrahmed.myocr.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mrahmed.myocr.firestore_helper.FirestoreHelper;
import com.mrahmed.myocr.R;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    private EditText _editTextView;
    private FirestoreHelper firestoreHelper;
    private String documentId;
    private Button _saveOnlineBtnEt, _saveWordBtnEt, _saveExcelBtnEt, _loadWordBtn, _loadExcelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Your OCR");

        _editTextView = findViewById(R.id.editTextView);
        _saveOnlineBtnEt = findViewById(R.id.saveOnlineBtnEt);
        _saveWordBtnEt = findViewById(R.id.saveWordBtnEt);
        _saveExcelBtnEt = findViewById(R.id.saveExcelBtnEt);
        _loadWordBtn = findViewById(R.id.loadWordBtn);
        _loadExcelBtn = findViewById(R.id.loadExcelBtn);
        firestoreHelper = new FirestoreHelper();

        documentId = getIntent().getStringExtra("documentId");

        if (documentId != null) {
            loadTextFromFirestore(documentId);
        } else {
            Toast.makeText(this, "Document ID is null", Toast.LENGTH_SHORT).show();
            finish();
        }

        _saveOnlineBtnEt.setOnClickListener(v -> {
            String updatedText = _editTextView.getText().toString();
            updateData(updatedText);
        });

        _saveWordBtnEt.setOnClickListener(v -> saveToWordFile(getExternalFilesDir(null) + "/ocr_text.docx", _editTextView.getText().toString()));
        _saveExcelBtnEt.setOnClickListener(v -> saveToExcelFile(getExternalFilesDir(null) + "/ocr_text.xlsx", _editTextView.getText().toString()));
        _loadWordBtn.setOnClickListener(v -> _editTextView.setText(loadFromWordFile(getExternalFilesDir(null) + "/ocr_text.docx")));
        _loadExcelBtn.setOnClickListener(v -> _editTextView.setText(loadFromExcelFile(getExternalFilesDir(null) + "/ocr_text.xlsx")));
    }

    private void loadTextFromFirestore(String documentId) {
        firestoreHelper.getOCRText(documentId, new FirestoreHelper.FirestoreTextCallback() {
            @Override
            public void onSuccess(String text) {
                _editTextView.setText(text);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EditActivity.this, "Failed to load text: " + error, Toast.LENGTH_LONG).show();
                Log.e("EditActivity", "Firestore Error: " + error);
            }
        });
    }

    private void updateData(String text) {
        firestoreHelper.updateOCRText(documentId, text, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(EditActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EditActivity.this, "Failed to update text: " + error, Toast.LENGTH_LONG).show();
                Log.e("EditActivity", "Firestore Error: " + error);
            }
        });
    }

    public void saveToWordFile(String filePath, String text) {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            document.write(out);
            Toast.makeText(this, "Saved to Word file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save Word file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String loadFromWordFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (FileInputStream in = new FileInputStream(filePath)) {
            XWPFDocument document = new XWPFDocument(in);
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load Word file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return text.toString();
    }

    public void saveToExcelFile(String filePath, String text) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OCR Data");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(text);

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
            Toast.makeText(this, "Saved to Excel file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save Excel file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String loadFromExcelFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (FileInputStream in = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                text.append(row.getCell(0).getStringCellValue()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load Excel file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return text.toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}