package com.mrahmed.myocr.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
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

import javax.annotation.Nullable;

public class OcrActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 201;
    private Uri imageUri;
    private SubsamplingScaleImageView imageIv;
    private androidx.appcompat.widget.AppCompatButton cameraBtn, galleryBtn, copyBtn, saveBtn, shareBtn;
    private android.widget.TextView recognizedTextEt;
    private FirebaseFirestore firestore;
    private String documentId;
    private boolean isEditing = false;
    private FirestoreHelper firestoreHelper;
    private Toolbar toolbar;
    private TextRecognizer latinRecognizer;
    private TextRecognizer bengaliRecognizer;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private androidx.appcompat.widget.AppCompatButton saveWordBtn, loadWordBtn, saveExcelBtn, loadExcelBtn;
    String defaultPath = getExternalFilesDir(null).getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Scan Your OCR");

        imageIv = findViewById(R.id.imageIv);
        recognizedTextEt = findViewById(R.id.recognizedTextEt);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        copyBtn = findViewById(R.id.copyBtn);
        saveBtn = findViewById(R.id.saveOnlineBtn);
        shareBtn = findViewById(R.id.shareBtn);
        saveWordBtn = findViewById(R.id.saveWordBtn);
        saveExcelBtn = findViewById(R.id.saveExcelBtn);

        firestore = FirebaseFirestore.getInstance();
        firestoreHelper = new FirestoreHelper();

        initializePermissions();
        initializeTextRecognizers();
        initializeActivityResultLaunchers();
        cameraBtn.setOnClickListener(v -> openCamera());
        galleryBtn.setOnClickListener(v -> openGallery());
        copyBtn.setOnClickListener(v -> copyTextToClipboard());
        saveBtn.setOnClickListener(v -> saveToFirestore());
        shareBtn.setOnClickListener(v -> shareText());
//        saveWordBtn.setOnClickListener(v -> saveToWordFile("path/to/your/file.docx", recognizedTextEt.getText().toString()));
//        loadWordBtn.setOnClickListener(v -> recognizedTextEt.setText(loadFromWordFile("path/to/your/file.docx")));
//        saveExcelBtn.setOnClickListener(v -> saveToExcelFile("path/to/your/file.xlsx", recognizedTextEt.getText().toString()));
//        loadExcelBtn.setOnClickListener(v -> recognizedTextEt.setText(loadFromExcelFile("path/to/your/file.xlsx")));

        saveWordBtn.setOnClickListener(v -> saveToWordFile(getExternalFilesDir(null) + "/ocr_text.docx", recognizedTextEt.getText().toString()));
        loadWordBtn.setOnClickListener(v -> recognizedTextEt.setText(loadFromWordFile(getExternalFilesDir(null) + "/ocr_text.docx")));
        saveExcelBtn.setOnClickListener(v -> saveToExcelFile(getExternalFilesDir(null) + "/ocr_text.xlsx", recognizedTextEt.getText().toString()));
        loadExcelBtn.setOnClickListener(v -> recognizedTextEt.setText(loadFromExcelFile(getExternalFilesDir(null) + "/ocr_text.xlsx")));

        documentId = getIntent().getStringExtra("documentId");
        if (documentId != null) {
            isEditing = true;
            loadExistingText();
        }
    }

    private void initializePermissions() {
        cameraPermissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
    }
    private void initializeTextRecognizers() {
        latinRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        bengaliRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
    }
    private void initializeActivityResultLaunchers() {
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        imageIv.setImage(ImageSource.uri(imageUri));
                        recognizeText();
                    } else {
                        Toast.makeText(OcrActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            imageIv.setImage(ImageSource.uri(imageUri));
                            recognizeText();
                        } else {
                            Toast.makeText(OcrActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OcrActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void loadExistingText() {
        firestore.collection("scanned_texts").document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String existingText = document.getString("text");
                            recognizedTextEt.setText(existingText);
                        } else {
                            Toast.makeText(OcrActivity.this, "Document not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OcrActivity.this, "Failed to load document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openCamera() {
        if (!checkCameraPermissions()) {
            requestCameraPermissions();
        } else {
            pickImageCamera();
        }
    }

    private void openGallery() {
        if (!checkStoragePermissions()) {
            requestStoragePermissions();
        } else {
            pickImageGallery();
        }
    }
    private boolean checkCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickImageGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void recognizeText() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            latinRecognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String resultText = text.getText();
                            if (isEditing) {
                                String existingText = recognizedTextEt.getText().toString();
                                recognizedTextEt.setText(existingText + "\n" + resultText);
                            } else {
                                recognizedTextEt.setText(resultText);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OcrActivity.this, "Failed to recognize Latin text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            bengaliRecognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String resultText = text.getText();
                            if (!resultText.isEmpty()) {
                                if (isEditing) {
                                    String existingText = recognizedTextEt.getText().toString();
                                    recognizedTextEt.setText(existingText + "\n" + resultText);
                                } else {
                                    String currentText = recognizedTextEt.getText().toString();
                                    recognizedTextEt.setText(currentText + "\n" + resultText);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OcrActivity.this, "Failed to recognize Bengali text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to prepare image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("OcrActivity", "Image preparation failed", e);
        }
    }

    private void saveToFirestore() {
        String text = recognizedTextEt.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "No text to save", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEditing) {
            firestoreHelper.updateOCRText(documentId, text, new FirestoreHelper.FirestoreCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(OcrActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(OcrActivity.this, "Error updating text: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String newDocumentId = java.util.UUID.randomUUID().toString();
            firestoreHelper.saveOCRText(newDocumentId, text, new FirestoreHelper.FirestoreMessageCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(OcrActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(OcrActivity.this, "Error saving text: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void copyTextToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Recognized Text", recognizedTextEt.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show();
    }

    private void shareText() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, recognizedTextEt.getText().toString());
        startActivity(Intent.createChooser(intent, "Share using"));
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