package com.logicsoft.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final String TAG = "MainActivity";
    private static final String KEY_SCAN_COUNT = "scanCountState";
    private static final String KEY_UNIQUE_BARCODES = "uniqueBarcodesState";

    private PreviewView previewView;
    private ImageButton btnFlashToggle, btnSwitchCamera;
    private ImageView icHistory, icGallery, icCreate, icSettings;
    private TextView zoomLevelText;

    private boolean isFlashOn = false;
    private boolean isFrontCamera = false;
    private Set<String> uniqueScannedBarcodes = new HashSet<>();
    private int scanCount = 0;

    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;
    private BarcodeScanner mlKitScanner;
    private androidx.camera.core.Camera camera;

    private ScaleGestureDetector scaleGestureDetector;
    private float currentZoomRatio = 1.0f;

    private ActivityResultLauncher<Intent> galleryLauncher;

    private long lastSuccessfulScanTime = 0;
    private String lastScannedBarcodeValue = null;
    private static final long SCAN_COOLDOWN_MS = 2000;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            scanCount = savedInstanceState.getInt(KEY_SCAN_COUNT, 0);
            Serializable savedUniquesSerializable = savedInstanceState.getSerializable(KEY_UNIQUE_BARCODES);
            if (savedUniquesSerializable instanceof Set) {
                uniqueScannedBarcodes = (Set<String>) savedUniquesSerializable;
            } else {
                uniqueScannedBarcodes = new HashSet<>();
            }
        }

        previewView = findViewById(R.id.previewView);
        btnFlashToggle = findViewById(R.id.btnFlashToggle);
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera);
        zoomLevelText = findViewById(R.id.zoomLevelText);

        cameraExecutor = Executors.newSingleThreadExecutor();

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();
        mlKitScanner = BarcodeScanning.getClient(options);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    scanImageFromUri(imageUri);
                } else {
                    Log.e(TAG, "Failed to get image URI from gallery.");
                    Toast.makeText(this, "Could not load image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }

        setupPinchToZoom();

        btnFlashToggle.setOnClickListener(v -> toggleFlash());
        btnSwitchCamera.setOnClickListener(v -> switchCamera());
        icCreate = findViewById(R.id.ic_create);
        icHistory = findViewById(R.id.ic_history);


        findViewById(R.id.ic_gallery).setOnClickListener(v -> {
            lastScannedBarcodeValue = null;
            openGallery();
        });

        icHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        icCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ic_settings).setOnClickListener(v -> Toast.makeText(this, "Settings: Not Implemented", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCAN_COUNT, scanCount);
        if (uniqueScannedBarcodes instanceof Serializable) {
            outState.putSerializable(KEY_UNIQUE_BARCODES, (Serializable) uniqueScannedBarcodes);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera provider future failed.", e);
                Toast.makeText(this, "Cannot start camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindCameraUseCases() {
        if (cameraProvider == null) {
            Log.e(TAG, "Camera provider is null. Cannot bind use cases.");
            return;
        }
        cameraProvider.unbindAll();

        CameraSelector localCameraSelector = new CameraSelector.Builder().requireLensFacing(isFrontCamera ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(cameraExecutor, this::processImageProxy);

        try {
            camera = cameraProvider.bindToLifecycle(this, localCameraSelector, preview, imageAnalysis);
            if (camera != null) {
                updateFlashButtonState();
                LiveData<androidx.camera.core.ZoomState> zoomState = camera.getCameraInfo().getZoomState();
                if (zoomState != null && zoomState.getValue() != null) {
                    currentZoomRatio = zoomState.getValue().getZoomRatio();
                } else {
                    currentZoomRatio = 1.0f;
                }
                updateZoomLevelText(currentZoomRatio);
            }
        } catch (Exception e) {
            Log.e(TAG, "Use case binding failed", e);
            Toast.makeText(this, "Error starting camera features", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPinchToZoom() {
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (camera == null) return true;
                float scale = detector.getScaleFactor();
                currentZoomRatio *= scale;
                LiveData<androidx.camera.core.ZoomState> zoomStateLiveData = camera.getCameraInfo().getZoomState();
                if (zoomStateLiveData != null && zoomStateLiveData.getValue() != null) {
                    float minZoom = zoomStateLiveData.getValue().getMinZoomRatio();
                    float maxZoom = zoomStateLiveData.getValue().getMaxZoomRatio();
                    currentZoomRatio = Math.max(minZoom, Math.min(currentZoomRatio, maxZoom));
                    camera.getCameraControl().setZoomRatio(currentZoomRatio);
                    updateZoomLevelText(currentZoomRatio);
                }
                return true;
            }
        });

        previewView.setOnTouchListener((view, event) -> {
            boolean handledByScaleDetector = scaleGestureDetector.onTouchEvent(event);
            if (!handledByScaleDetector && event.getAction() == MotionEvent.ACTION_DOWN) {
                if (camera != null) {
                    try {
                        MeteringPointFactory factory = previewView.getMeteringPointFactory();
                        MeteringPoint point = factory.createPoint(event.getX(), event.getY());
                        FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF).setAutoCancelDuration(3, TimeUnit.SECONDS).build();
                        camera.getCameraControl().startFocusAndMetering(action);
                    } catch (Exception e) {
                        Log.e(TAG, "Tap to focus failed", e);
                    }
                }
            }
            return true;
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            galleryLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch gallery picker", e);
            Toast.makeText(this, "Cannot open gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanImageFromUri(Uri imageUri) {
        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            if (mlKitScanner == null) {
                Log.e(TAG, "mlKitScanner not initialized for gallery scan.");
                Toast.makeText(this, "Scanner not ready", Toast.LENGTH_SHORT).show();
                return;
            }
            mlKitScanner.process(image).addOnSuccessListener(barcodes -> {
                if (barcodes.isEmpty()) {
                    Log.d(TAG, "No barcodes found in the selected image.");
                    Toast.makeText(MainActivity.this, "No barcode found in image", Toast.LENGTH_SHORT).show();
                    return;
                }
                Barcode barcode = barcodes.get(0);
                String rawValue = barcode.getRawValue();
                Log.d(TAG, "Barcode from gallery: " + rawValue);
                lastScannedBarcodeValue = rawValue;
                lastSuccessfulScanTime = System.currentTimeMillis();


                boolean isNewUniqueScan = !uniqueScannedBarcodes.contains(rawValue);
                if (isNewUniqueScan) {
                    uniqueScannedBarcodes.add(rawValue);
                    scanCount++;
                }
                showResult(rawValue, barcode.getFormat(), scanCount, barcode.getValueType(), !isNewUniqueScan);
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Barcode scanning from gallery failed", e);
                Toast.makeText(MainActivity.this, "Failed to scan image", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error processing image from URI: " + imageUri.toString(), e);
            Toast.makeText(this, "Error loading image for scanning", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFlash() {
        if (camera == null || !camera.getCameraInfo().hasFlashUnit()) {
            Toast.makeText(this, "Flash not available on this camera", Toast.LENGTH_SHORT).show();
            return;
        }
        isFlashOn = !isFlashOn;
        camera.getCameraControl().enableTorch(isFlashOn);
        updateFlashButtonState();
    }

    private void updateFlashButtonState() {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            btnFlashToggle.setEnabled(true);
            btnFlashToggle.setImageResource(isFlashOn ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        } else {
            btnFlashToggle.setEnabled(false);
            btnFlashToggle.setImageResource(R.drawable.ic_flash_off);
            isFlashOn = false;
        }
    }

    private void switchCamera() {
        isFrontCamera = !isFrontCamera;
        isFlashOn = false;
        currentZoomRatio = 1.0f;
        lastScannedBarcodeValue = null;
        startCamera();
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processImageProxy(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null && mlKitScanner != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            mlKitScanner.process(image).addOnSuccessListener(barcodes -> {
                if (!barcodes.isEmpty()) {
                    Barcode barcode = barcodes.get(0);
                    String rawValue = barcode.getRawValue();

                    if (rawValue != null) {
                        long currentTime = System.currentTimeMillis();
                        if (rawValue.equals(lastScannedBarcodeValue) && (currentTime - lastSuccessfulScanTime < SCAN_COOLDOWN_MS)) {
                            imageProxy.close();
                            return;
                        }

                        lastScannedBarcodeValue = rawValue;
                        lastSuccessfulScanTime = currentTime;

                        boolean isNewUniqueScan = !uniqueScannedBarcodes.contains(rawValue);
                        if (isNewUniqueScan) {
                            uniqueScannedBarcodes.add(rawValue);
                            scanCount++;
                        }
                        showResult(rawValue, barcode.getFormat(), scanCount, barcode.getValueType(), !isNewUniqueScan);
                    }
                }
            }).addOnFailureListener(e -> Log.e(TAG, "Live barcode scanning failed", e)).addOnCompleteListener(task -> imageProxy.close());
        } else {
            if (mediaImage == null) Log.w(TAG, "processImageProxy: mediaImage is null");
            if (mlKitScanner == null) Log.w(TAG, "processImageProxy: mlKitScanner is null");
            imageProxy.close();
        }
    }

    private void showResult(String result, int format, int count, int valueType, boolean isDuplicate) {
        String typeName = getFormatName(format);
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", result);
        String displayType;
        if (count > 0) {
            displayType = typeName + " (Scan #" + count + ")";
        } else {
            displayType = typeName;
        }
        intent.putExtra("type", displayType);
        intent.putExtra("valueType", valueType);
        startActivity(intent);
    }

    private String getFormatName(int format) {
        switch (format) {
            case Barcode.FORMAT_QR_CODE:
                return "QR Code";
            case Barcode.FORMAT_AZTEC:
                return "Aztec";
            case Barcode.FORMAT_DATA_MATRIX:
                return "Data Matrix";
            case Barcode.FORMAT_PDF417:
                return "PDF417";
            case Barcode.FORMAT_CODE_128:
                return "Code 128";
            case Barcode.FORMAT_CODE_39:
                return "Code 39";
            case Barcode.FORMAT_CODE_93:
                return "Code 93";
            case Barcode.FORMAT_CODABAR:
                return "Codabar";
            case Barcode.FORMAT_EAN_13:
                return "EAN-13";
            case Barcode.FORMAT_EAN_8:
                return "EAN-8";
            case Barcode.FORMAT_ITF:
                return "ITF";
            case Barcode.FORMAT_UPC_A:
                return "UPC-A";
            case Barcode.FORMAT_UPC_E:
                return "UPC-E";
            default:
                return "Barcode";
        }
    }

    private void updateZoomLevelText(float zoomRatio) {
        if (zoomLevelText != null) {
            zoomLevelText.setText(String.format("%.1fx", zoomRatio));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Log.e(TAG, "Camera permission denied by user.");
                Toast.makeText(this, "ক্যামেরার পারমিশন প্রয়োজন (Camera permission is required to scan codes)", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastScannedBarcodeValue = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (cameraProvider == null && mlKitScanner != null) {
                startCamera();
            } else if (camera == null && cameraProvider != null) {
                bindCameraUseCases();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        if (mlKitScanner != null) {
            mlKitScanner.close();
        }
        Log.d(TAG, "MainActivity destroyed.");
    }
}
