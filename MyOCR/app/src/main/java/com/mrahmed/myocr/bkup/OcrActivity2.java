package com.mrahmed.myocr.bkup;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.mrahmed.myocr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OcrActivity2 extends AppCompatActivity {

    private Button cameraBtn, galleryBtn, copyBtn, saveBtn, shareBtn;
    private ImageView imageIv;
    private TextView recognizedTextTv;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private Uri imageUri = null;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private TextRecognizer textRecognizer;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        initializeViews();
        initializePermissions();
        initializeTextRecognizer();
        initializeActivityResultLaunchers();
        setupClickListeners();
    }

    private void initializeViews() {
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        imageIv = findViewById(R.id.imageIv);
        recognizedTextTv = findViewById(R.id.recognizedTextTv);
        copyBtn = findViewById(R.id.copyBtn);
        saveBtn = findViewById(R.id.saveBtn);
        shareBtn = findViewById(R.id.shareBtn);
    }

    private void initializePermissions() {
        cameraPermissions = new String[]{Manifest.permission.CAMERA};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void initializeTextRecognizer() {
//        textRecognizer = TextRecognition.getClient(new LatinTextRecognizerOptions.Builder().build());
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    }

    private void initializeActivityResultLaunchers() {
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageIv.setImageURI(imageUri);
                        recognizeText();
                    } else {
                        Toast.makeText(OcrActivity2.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            imageIv.setImageURI(imageUri);
                            recognizeText();
                        } else {
                            Toast.makeText(OcrActivity2.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OcrActivity2.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupClickListeners() {
        cameraBtn.setOnClickListener(v -> {
            if (checkCameraHardware()) {
                if (checkCameraPermissions()) {
                    pickImageCamera();
                } else {
                    requestCameraPermissions();
                }
            } else {
                Toast.makeText(OcrActivity2.this, "No camera available on this device", Toast.LENGTH_SHORT).show();
                cameraBtn.setEnabled(false);
            }
        });

        galleryBtn.setOnClickListener(v -> {
            if (checkStoragePermissions()) {
                pickImageGallery();
            } else {
                requestStoragePermissions();
            }
        });

        copyBtn.setOnClickListener(v -> copyTextToClipboard());
        saveBtn.setOnClickListener(v -> saveTextToFile());
        shareBtn.setOnClickListener(v -> shareText());
    }

    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private boolean checkCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return true;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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

    private void recognizeText() {
        if (imageUri == null) {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String recognizedText = text.getText();
                            recognizedTextTv.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OcrActivity2.this, "Failed to recognize text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity", "Text recognition failed", e);
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to prepare image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Image preparation failed", e);
        }
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

    private void copyTextToClipboard() {
        String textToCopy = recognizedTextTv.getText().toString();
        if (textToCopy.isEmpty()) {
            Toast.makeText(this, "No text to copy", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", textToCopy);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void saveTextToFile() {
        String textToSave = recognizedTextTv.getText().toString();
        if (textToSave.isEmpty()) {
            Toast.makeText(this, "No text to save", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, "recognized_text.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(textToSave.getBytes());
            fos.close();
            Toast.makeText(this, "Text saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Failed to save text", e);
        }
    }

    private void shareText() {
        String textToShare = recognizedTextTv.getText().toString();
        if (textToShare.isEmpty()) {
            Toast.makeText(this, "No text to share", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        startActivity(Intent.createChooser(shareIntent, "Share text via"));
    }
}


//package com.mrahmed.myocr;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class OcrActivity extends AppCompatActivity {
//    private static final int REQUEST_STORAGE_PERMISSION = 100;
//    private TextView recognizedTextTv;
//    private FirebaseFirestore firestore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ocr);
//
//        recognizedTextTv = findViewById(R.id.recognizedText);
//        firestore = FirebaseFirestore.getInstance();
//
//        checkStoragePermission();
//    }
//
//    private void checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_STORAGE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void saveTextToFirestore(String recognizedText) {
//        String documentId = firestore.collection("scanned_texts").document().getId();
//        Map<String, Object> data = new HashMap<>();
//        data.put("text", recognizedText);
//        data.put("timestamp", System.currentTimeMillis());
//
//        firestore.collection("scanned_texts").document(documentId)
//                .set(data)
//                .addOnSuccessListener(aVoid -> Toast.makeText(OcrActivity.this, "Text saved", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(OcrActivity.this, "Error saving text", Toast.LENGTH_SHORT).show());
//    }
//}