package com.logicsoftbd.lsl.ui.process.scanprocess;

public class ScannerActivity1  {

   /* private static final String cameraPerm = Manifest.permission.CAMERA;
    public static final String EXTRA_BUNDLE_ID = "extra_bundle_id";

    @Inject
    ScannerMvpPresenter<ScannerMvpView, ScannerMvpInteractor> mPresenter;

    public static Intent getStartIntent(Context context, Process process) {
        Intent intent = new Intent(context, ScannerActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BUNDLE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }

    // UI
    @BindView(R.id.camera_view)
    SurfaceView mySurfaceView;

    @BindView(R.id.et_qr_code)
    EditText editTextQrCode;

    @BindView(R.id.image_button_go)
    ImageView imageButtonGo;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private QREader qrEader;

    boolean hasCameraPermission = false;

    private Process mProcess;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
       // getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setUp();
        mPresenter.onAttach(ScannerActivity1.this);
    }

    @Override
    protected void setUp() {
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_BUNDLE_ID);

        // in case cost id is not sent
        if (mProcess == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(R.string.scanner);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(this, cameraPerm);
        // change of reader state in dynamic
        if (hasCameraPermission) {
            // Setup QREader
            setupQREader();
        } else {
            RuntimePermissionUtil.requestPermission(ScannerActivity1.this, cameraPerm, 100);
        }
    }

    void restartActivity() {
        startActivity(new Intent(ScannerActivity1.this, ScannerActivity1.class));
        finish();
    }

    void setupQREader() {
        // Init QREader
        // ------------
        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                editTextQrCode.post(new Runnable() {
                    @Override
                    public void run() {
                        editTextQrCode.setText(data);
                        onNextStep();
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @OnClick(R.id.image_button_go)
    void onGo() {
       onNextStep();
    }

    private void onNextStep() {
        mProcess.getDataParam().setBarcode(editTextQrCode.getText().toString());
        mPresenter.onNextClick(mProcess.getDataParam());
        imageButtonGo.setClickable(false);
        imageButtonGo.setImageAlpha(32);
        qrEader.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (hasCameraPermission) {

            // Cleanup in onPause()
            // --------------------
            qrEader.releaseAndCleanup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasCameraPermission) {
            // Init and Start with SurfaceView
            // -------------------------------
            qrEader.initAndStart(mySurfaceView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(ScannerActivity1.this, cameraPerm)) {
                        restartActivity();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void bundleResponse(BundleResponse bundleResponse) {
        bundleResponse.setTitle(mProcess.getTitle());
        showMessage(bundleResponse.getMessage());
        startActivity(IssueActivity.getStartIntent(this, bundleResponse));
        finish();
    }

    @Override
    public void bundleErrorResponse() {
        imageButtonGo.setClickable(true);
        imageButtonGo.setImageAlpha(255);
        qrEader.start();
    }*/
}