package com.logicsoftbd.lsl.ui.v_1_ui.sewing_defect

import FilterListener
import FilterViewAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeStream
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.*
import androidx.annotation.RequiresPermission
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.burhanrashid52.photoediting.*
import com.burhanrashid52.photoediting.EmojiBSFragment.EmojiListener
import com.burhanrashid52.photoediting.StickerBSFragment.StickerListener
import com.burhanrashid52.photoediting.tools.EditingToolsAdapter
import com.burhanrashid52.photoediting.tools.EditingToolsAdapter.OnItemSelected
import com.burhanrashid52.photoediting.tools.ToolType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.logicsoftbd.lsl.R
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingRejectModel
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel
import com.logicsoftbd.lsl.serviceInterface.ApiInterface
import com.logicsoftbd.lsl.serviceInterface.ProfileImageResponse
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_AlterPopUpRecyclerAdapter
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_RejectPopUpRecyclerAdapter
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SpotPopUpRecyclerAdapter
import com.logicsoftbd.lsl.ui.v_1_ui.sewing_defect.base.BaseActivity
import com.logicsoftbd.lsl.utils.ApiUtils
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.Executors.newSingleThreadExecutor
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class EditImageActivity : BaseActivity(), OnPhotoEditorListener, View.OnClickListener,
    PropertiesBSFragment.Properties, ShapeBSFragment.Properties, EmojiListener, StickerListener,
    OnItemSelected, FilterListener, V1_AlterPopUpRecyclerAdapter.OnDefectSelectListener, V1_SpotPopUpRecyclerAdapter.OnDefectSelectListener, V1_RejectPopUpRecyclerAdapter.OnDefectSelectListener {

    var mPhotoEditor: PhotoEditor? = null
    private var mPhotoEditorView: PhotoEditorView? = null
    private var mPropertiesBSFragment: PropertiesBSFragment? = null
    private var mShapeBSFragment: ShapeBSFragment? = null
    private var mShapeBuilder: ShapeBuilder? = null
    private var mEmojiBSFragment: EmojiBSFragment? = null
    private var mStickerBSFragment: StickerBSFragment? = null
    private var mTxtCurrentTool: TextView? = null
    private var mWonderFont: Typeface? = null
    private var mRvTools: RecyclerView? = null
    private var mRvFilters: RecyclerView? = null
    private var errorDataSetLayout: LinearLayout? = null
    private val mEditingToolsAdapter = EditingToolsAdapter(this)
    private val mFilterViewAdapter = FilterViewAdapter(this)
    private var mRootView: ConstraintLayout? = null
    private val mConstraintSet = ConstraintSet()
    private var mIsFilterVisible = false

    @VisibleForTesting
    var mSaveImageUri: Uri? = null
    private var mSaveFileHelper: FileSaveHelper? = null
    private var apiInterface: ApiInterface? = null
    private var apiUtils: ApiUtils? = null

    private var sewingDefectRecyclerView: RecyclerView? = null

    private var alterDataList: ArrayList<V1_SewingAlterModel>? = null
    private var alterPopUpRecyclerAdapter: V1_AlterPopUpRecyclerAdapter? = null

    private var spotDataList: ArrayList<V1_SewingSpotModel>? = null
    private var spotPopUpRecyclerAdapter: V1_SpotPopUpRecyclerAdapter? = null

    private var rejectDataList: ArrayList<V1_SewingRejectModel>? = null
    private var rejectPopUpRecyclerAdapter: V1_RejectPopUpRecyclerAdapter? = null
    private var titleValue: String? = ""
    private var defectDataKey: String? = ""
    private var defectTypeKey: String? = ""
    private var defactPosition: Int = 0
    private var defectType: Int = 0
    private var defectNumberCount: Int = 0
    private var count: Int = 0
    private var base_url: String = ""
    private var selectedColorCode: Int = -1242561
    private var is_text: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_edit_image_test_test)

//        val _preferences = PreferenceManager.getDefaultSharedPreferences(this)
//        base_url = _preferences.getString("base_url", "").toString()
//        apiUtils = ApiUtils(this)
//        apiInterface = getInterface(base_url)

        val intent = getIntent();
        titleValue = intent.getStringExtra("defectKey")
        defectDataKey = intent.getStringExtra("defectDataKey")
        defectTypeKey = intent.getStringExtra("defect_type")

        initViews()
        handleIntentImage(mPhotoEditorView?.source)
        mWonderFont = Typeface.createFromAsset(assets, "beyond_wonderland.ttf")
        mPropertiesBSFragment = PropertiesBSFragment()
//        mEmojiBSFragment = EmojiBSFragment()
//        mStickerBSFragment = StickerBSFragment()
        mShapeBSFragment = ShapeBSFragment()
        mStickerBSFragment?.setStickerListener(this)
//        mEmojiBSFragment?.setEmojiListener(this)
        mPropertiesBSFragment?.setPropertiesChangeListener(this)
        mShapeBSFragment?.setPropertiesChangeListener(this)
        val llmTools = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvTools?.layoutManager = llmTools
        mRvTools?.adapter = mEditingToolsAdapter
        val llmFilters = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvFilters?.layoutManager = llmFilters
        mRvFilters?.adapter = mFilterViewAdapter

        // NOTE(lucianocheng): Used to set integration testing parameters to PhotoEditor
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)

        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditor = mPhotoEditorView?.run {
            PhotoEditor.Builder(this@EditImageActivity, this)
                .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build() // build photo editor sdk
        }
        mPhotoEditor?.setOnPhotoEditorListener(this)

        var mImage: Bitmap?
        val myExecutor = newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

//        var mWebPath ="http://59.152.60.149:3362/platform_erp/file_upload/tshirt_sketch_img.jpg";
//        myExecutor.execute {
//            mImage = mLoad(mWebPath)
//            myHandler.post {
//                mPhotoEditorView?.source?.setImageBitmap(mImage);
//                if(mImage!=null){
//                }
//            }
//        }

        //Set Image Dynamically
//        mPhotoEditorView?.source?.setImageResource(R.drawable.logic)
        mSaveFileHelper = FileSaveHelper(this)

//        initShape();
        alterDataList = V1_SewingOutputActivity.sewingAlterModels
        spotDataList = V1_SewingOutputActivity.sewingSpotModels
        rejectDataList = V1_SewingOutputActivity.sewingRejectModels
        initialization()
    }

    private fun initialization() {
        sewingDefectRecyclerView = findViewById(R.id.alterRecyclerView)
        initAlterRecyclerView()
        findViewById<View>(R.id.alterCloseBtn).setOnClickListener { v: View? -> onBackPressed() }
        findViewById<Button>(R.id.doneButton).setOnClickListener { onBackPressed() }

        if(alterDataList?.size!! <= 0){
            errorDataSetLayout?.visibility = View.VISIBLE
            sewingDefectRecyclerView?.visibility = View.GONE
        }
        if(spotDataList?.size!! <= 0){
            errorDataSetLayout?.visibility = View.VISIBLE
            sewingDefectRecyclerView?.visibility = View.GONE
        }
        if(rejectDataList?.size!! <= 0){
            errorDataSetLayout?.visibility = View.VISIBLE
            sewingDefectRecyclerView?.visibility = View.GONE
        }
    }

    private fun initAlterRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        sewingDefectRecyclerView!!.layoutManager = linearLayoutManager
        if(defectDataKey.equals("alter")){
            alterPopUpRecyclerAdapter = V1_AlterPopUpRecyclerAdapter(alterDataList, applicationContext, this)
            sewingDefectRecyclerView!!.adapter = alterPopUpRecyclerAdapter
        }else if(defectDataKey.equals("spot")){
            spotPopUpRecyclerAdapter = V1_SpotPopUpRecyclerAdapter(spotDataList, applicationContext, this)
            sewingDefectRecyclerView!!.adapter = spotPopUpRecyclerAdapter
        }else if(defectDataKey.equals("reject")){
            rejectPopUpRecyclerAdapter = V1_RejectPopUpRecyclerAdapter(rejectDataList, applicationContext, this)
            sewingDefectRecyclerView!!.adapter = rejectPopUpRecyclerAdapter
        }

    }

    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
    private fun mLoad(string: String): Bitmap? {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }

                override fun checkClientTrusted(
                    certs: Array<X509Certificate>, authType: String
                ) {
                }

                override fun checkServerTrusted(
                    certs: Array<X509Certificate>, authType: String
                ) {
                }
            }
        )
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: GeneralSecurityException) {
        }
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
//            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    private fun handleIntentImage(source: ImageView?) {
        if (intent == null) {
            return;
        }

        when (intent.action) {
            Intent.ACTION_EDIT, ACTION_NEXTGEN_EDIT -> {
                try {
                    val uri = intent.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver, uri
                    )
                    source?.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else -> {
                val intentType = intent.type
                if (intentType != null && intentType.startsWith("image/")) {
                    val imageUri = intent.data
                    if (imageUri != null) {
                        source?.setImageURI(imageUri)
                    }
                }
            }
        }
    }


    private fun initViews() {
        mPhotoEditorView = findViewById(R.id.photoEditorView)
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool)
        mRvTools = findViewById(R.id.rvConstraintTools)
        mRvFilters = findViewById(R.id.rvFilterView)
        errorDataSetLayout = findViewById(R.id.errorDataSetLayout)
        mRootView = findViewById(R.id.rootView)

        val imgUndo: ImageView = findViewById(R.id.imgUndo)
        imgUndo.setOnClickListener(this)
        val imgRedo: ImageView = findViewById(R.id.imgRedo)
        imgRedo.setOnClickListener(this)
        val text_add: FloatingActionButton = findViewById(R.id.text_add)
        text_add.setOnClickListener(this)
        val imgCamera: ImageView = findViewById(R.id.imgCamera)
        imgCamera.setOnClickListener(this)
        val imgGallery: ImageView = findViewById(R.id.imgGallery)
        imgGallery.setOnClickListener(this)
        val imgSave: Button = findViewById(R.id.imgSave)
        imgSave.setOnClickListener(this)
        val imgClose: ImageView = findViewById(R.id.imgClose)
        imgClose.setOnClickListener(this)
        val imgShare: ImageView = findViewById(R.id.imgShare)
        imgShare.setOnClickListener(this)
        val imgBack: ImageView = findViewById(R.id.imgBack)
        imgBack.setOnClickListener(this)

        val defectTitleTV: TextView = findViewById(R.id.defectTitleTV)
        defectTitleTV.text = titleValue

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initShape() {
//        count = defectNumberCount + 1
        count = defactPosition + 1
        Log.d(TAG, "initShape: ########")
//        mPhotoEditor?.setBrushDrawingMode(true)
//        mPhotoEditor?.addText(Typeface.DEFAULT_BOLD,"1", 1)
//        mPhotoEditor?.setBrushDrawingMode(true)
//        mPhotoEditor?.addText("1", )
//        val textBorder = TextBorder(0.2F, R.color.dark_gray, 10, R.color.dark_gray)
//        val textStyle = TextStyleBuilder()
//        textStyle.withTextColor(Color.YELLOW)
//        textStyle.withTextSize(12f)
//        textStyle.withGravity(20)
//        textStyle.withTextBorder(textBorder)
//        textStyle.withBackgroundColor(R.color.dark_gray)
////        textStyle.withBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_circle_24))
////        val textStyleBuilder = TextStyleBuilder().withTextBorder(textStyle)
//        mPhotoEditor?.addText("1", textStyle)
        mShapeBuilder = ShapeBuilder()
        mPhotoEditor?.setShape(mShapeBuilder)
        mTxtCurrentTool?.setText(R.string.label_shape)
        mPhotoEditor?.addImage(mPhotoEditor?.createImageRounded(true, getApplicationContext(), 35, 35,
            selectedColorCode, "$defectTypeKey$count"
        ))
        Log.d(TAG, "initShape: ###"+ defectNumberCount.toString())
//        showBottomSheetDialogFragment(mShapeBSFragment)
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val textEditorDialogFragment = TextEditorDialogFragment.show(this, text.toString(), colorCode)
        textEditorDialogFragment.setOnTextEditorListener (object : TextEditorDialogFragment.TextEditorListener {
            override fun onDone(inputText: String?, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                if (rootView != null) {
                    Log.d(TAG, "onDone: ######"+"click");
                    mPhotoEditor?.editText(rootView, inputText, styleBuilder)
                }
                mTxtCurrentTool?.setText(R.string.label_text)
            }
        })
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]")
        if(!is_text){
            defectNumberCount = numberOfAddedViews
            Log.d(TAG, "onAddViewListener: #######"+defectNumberCount)
            if (defectType == 1){
                alterDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                alterPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
            if (defectType == 2){
                spotDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                spotPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
            if (defectType == 3){
                rejectDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                rejectPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]")
        if(!is_text){
            defectNumberCount = numberOfAddedViews
            if (defectType == 1){
                alterDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                alterPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
            if (defectType == 2){
                spotDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                spotPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
            if (defectType == 3){
                rejectDataList?.get(defactPosition)?.defectCount = numberOfAddedViews.toString()
                rejectPopUpRecyclerAdapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onTouchSourceImage(event: MotionEvent?) {
        Log.d(TAG, "onTouchView() called with: event = [$event]")
    }

    @SuppressLint("NonConstantResourceId", "MissingPermission")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgUndo -> mPhotoEditor?.undo()
            R.id.imgRedo -> mPhotoEditor?.redo()
            R.id.imgSave -> saveImage()
            R.id.imgClose -> onBackPressed()
            R.id.imgShare -> shareImage()
            R.id.imgBack -> onBackPressed()
            R.id.imgCamera -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            R.id.imgGallery -> {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
            }
            R.id.text_add -> {
                is_text = true
                val textEditorDialogFragment = TextEditorDialogFragment.show(this)
                textEditorDialogFragment.setOnTextEditorListener(object : TextEditorDialogFragment.TextEditorListener {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor?.addText(inputText, styleBuilder)
                        mTxtCurrentTool?.setText(R.string.label_text)
                    }
                })
            }
        }
    }

    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        val saveImageUri = mSaveImageUri
        if (saveImageUri == null) {
            showSnackbar(getString(R.string.msg_save_image_to_share))
            return
        }
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(saveImageUri))
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
    }

    private fun buildFileProviderUri(uri: Uri): Uri {
        if (FileSaveHelper.isSdkHigherThan28()) {
            return uri
        }
        val path: String = uri.path ?: throw IllegalArgumentException("URI Path Expected")

        return FileProvider.getUriForFile(
            this,
            FILE_PROVIDER_AUTHORITY,
            File(path)
        )
    }

    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    private fun saveImage() {
//        val fileName = "imageProcessing" + ".png"
        val fileName = System.currentTimeMillis().toString() + ".png"
        val hasStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (hasStoragePermission || FileSaveHelper.isSdkHigherThan28()) {
            showLoading("Saving...")
            mSaveFileHelper?.createFile(fileName, object : FileSaveHelper.OnFileCreateResult {

                @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
                override fun onFileCreateResult(
                    created: Boolean,
                    filePath: String?,
                    error: String?,
                    uri: Uri?
                ) {
//                    hideLoading()
//                    Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@EditImageActivity, V1_MenuMainActivity::class.java)
//                    intent.putExtra("image", filePath)
//                    startActivity(intent)



                    if (created && filePath != null) {
                        val saveSettings = SaveSettings.Builder()
                            .setClearViewsEnabled(true)
                            .setTransparencyEnabled(true)
                            .build()

                        mPhotoEditor?.saveAsFile(
                            filePath,
                            saveSettings,
                            object : PhotoEditor.OnSaveListener {
                                override fun onSuccess(imagePath: String) {
                                    mSaveFileHelper?.notifyThatFileIsNowPubliclyAvailable(
                                        contentResolver
                                    )
                                    hideLoading()
                                    showSnackbar("Image Saved Successfully")
                                    mSaveImageUri = uri
                                    mPhotoEditorView?.source?.setImageURI(mSaveImageUri)

//                                    val intent = Intent(this@EditImageActivity, V1_MenuMainActivity::class.java)
//                                    intent.putExtra("image", filePath)
//                                    startActivity(intent)


//                                    val file = File(filePath)
//
//                                    val requestFile = RequestBody.create(
//                                        MediaType.parse(uri?.let { contentResolver.getType(it) }),
//                                        file
//                                    )
//
//
//
//                                    val body = MultipartBody.Part.createFormData("image", filePath.toString(), requestFile)
                                    showLoading("Loading....")
                                    val stream = uri?.let { contentResolver.openInputStream(it) }
                                    val request = RequestBody.create(MediaType.parse("image/*"), stream?.readBytes()) // read all bytes using kotlin extension
                                    val filePart = MultipartBody.Part.createFormData(
                                        "fabric_image",
                                        "654680989086765.jpg",
                                        request
                                    )

                                    val bundleId = RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        "654680989086765"
                                    )
                                    val defectType = RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        defectTypeKey
                                    )

                                    apiInterface?.putPostImage(bundleId, defectType, filePart)
                                        ?.enqueue(object : Callback<ProfileImageResponse> {
                                            override fun onResponse(
                                                call: Call<ProfileImageResponse>,
                                                response: Response<ProfileImageResponse>
                                            ) {
                                                hideLoading()
                                                Log.d(TAG, "onResponse: " + response.body()?.resultset?.message)
//                                                showBottomSheetDialogFragment(mShapeBSFragment)
                                                count = 0
                                                defectNumberCount = 0
                                            }

                                            override fun onFailure(call: Call<ProfileImageResponse>, t: Throwable) {
                                                hideLoading()
                                                Toast.makeText(this@EditImageActivity, "Failed" + t.message, Toast.LENGTH_SHORT).show()
                                                Log.d(TAG, "Failed: " + t.message)
                                            }
                                        })

                                }

                                override fun onFailure(exception: Exception) {
                                    hideLoading()
                                    showSnackbar("Failed to save Image")
                                }
                            })
                    } else {
                        hideLoading()
                        error?.let { showSnackbar(error) }
                    }
                }
            })
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    // TODO(lucianocheng): Replace onActivityResult with Result API from Google
    //                     See https://developer.android.com/training/basics/intents/result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    mPhotoEditor?.clearAllViews()
                    val photo = data?.extras?.get("data") as Bitmap?
                    mPhotoEditorView?.source?.setImageBitmap(photo)
                }
                PICK_REQUEST -> try {
                    mPhotoEditor?.clearAllViews()
                    val uri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver, uri
                    )
                    mPhotoEditorView?.source?.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor?.setShape(mShapeBuilder?.withShapeColor(colorCode))
        mTxtCurrentTool?.setText(R.string.label_brush)
        selectedColorCode = colorCode
        Log.d(TAG, "onColorChanged: ######"+colorCode.toString())
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor?.setShape(mShapeBuilder?.withShapeOpacity(opacity))
        mTxtCurrentTool?.setText(R.string.label_brush)
    }

    override fun onShapeSizeChanged(shapeSize: Int) {
        mPhotoEditor?.setShape(mShapeBuilder?.withShapeSize(shapeSize.toFloat()))
        mTxtCurrentTool?.setText(R.string.label_brush)
    }

    override fun onShapePicked(shapeType: ShapeType?) {
        mPhotoEditor?.setShape(mShapeBuilder?.withShapeType(shapeType))
    }

    override fun onEmojiClick(emojiUnicode: String?) {
        mPhotoEditor?.addEmoji(emojiUnicode)
        mTxtCurrentTool?.setText(R.string.label_emoji)
    }

    override fun onStickerClick(bitmap: Bitmap?) {
        mPhotoEditor?.addImage(bitmap)
        mTxtCurrentTool?.setText(R.string.label_sticker)
    }

    @SuppressLint("MissingPermission")
    override fun isPermissionGranted(isGranted: Boolean, permission: String?) {
        if (isGranted) {
            saveImage()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton("Save") { _: DialogInterface?, _: Int -> saveImage() }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        builder.setNeutralButton("Discard") { _: DialogInterface?, _: Int -> finish() }
        builder.create().show()
    }

    override fun onFilterSelected(photoFilter: PhotoFilter?) {
        mPhotoEditor?.setFilterEffect(photoFilter)
    }

    override fun onToolSelected(toolType: ToolType?) {
        when (toolType) {
            ToolType.SHAPE -> {
                mPhotoEditor?.setBrushDrawingMode(true)
                mShapeBuilder = ShapeBuilder()
                mPhotoEditor?.setShape(mShapeBuilder)
                mTxtCurrentTool?.setText(R.string.label_shape)
                showBottomSheetDialogFragment(mShapeBSFragment)
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment = TextEditorDialogFragment.show(this)
                textEditorDialogFragment.setOnTextEditorListener(object : TextEditorDialogFragment.TextEditorListener {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor?.addText(inputText, styleBuilder)
                        mTxtCurrentTool?.setText(R.string.label_text)
                    }
                })
            }
//            ToolType.ERASER -> {
//                mPhotoEditor?.brushEraser()
//                mTxtCurrentTool?.setText(R.string.label_eraser_mode)
//            }
//            ToolType.FILTER -> {
//                mTxtCurrentTool?.setText(R.string.label_filter)
//                showFilter(true)
//            }
//            ToolType.EMOJI -> showBottomSheetDialogFragment(mEmojiBSFragment)
//            ToolType.STICKER -> showBottomSheetDialogFragment(mStickerBSFragment)
            else -> {}
        }
    }

    private fun showBottomSheetDialogFragment(fragment: BottomSheetDialogFragment?) {
        if (fragment == null || fragment.isAdded) {
            return
        }
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun showFilter(isVisible: Boolean) {
        mIsFilterVisible = isVisible
        mConstraintSet.clone(mRootView)
        val rvFilterId: Int = mRvFilters?.id ?: throw IllegalArgumentException("RV Filter ID Expected")
        if (isVisible) {
            mConstraintSet.clear(rvFilterId, ConstraintSet.START)
            mConstraintSet.connect(
                rvFilterId, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            mConstraintSet.connect(
                rvFilterId, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
        } else {
            mConstraintSet.connect(
                rvFilterId, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            mConstraintSet.clear(rvFilterId, ConstraintSet.END)
        }
        val changeBounds = ChangeBounds()
        changeBounds.duration = 350
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        mRootView?.let { TransitionManager.beginDelayedTransition(it, changeBounds) }
        mConstraintSet.applyTo(mRootView)
    }

    override fun onBackPressed() {
        val isCacheEmpty = mPhotoEditor?.isCacheEmpty ?: throw IllegalArgumentException("isCacheEmpty Expected")
        Log.d("TAG", "onCreate: " + V1_SewingOutputActivity.sewingRejectModels.size)
        if (mIsFilterVisible) {
            showFilter(false)
            mTxtCurrentTool?.setText(R.string.app_name)
        } else if (!isCacheEmpty) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private val TAG = EditImageActivity::class.java.simpleName
        const val FILE_PROVIDER_AUTHORITY = "com.burhanrashid52.photoediting.fileprovider"
        private const val CAMERA_REQUEST = 52
        private const val PICK_REQUEST = 53
        const val ACTION_NEXTGEN_EDIT = "action_nextgen_edit"
        const val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    }

    @SuppressLint("MissingPermission")
    override fun onAlterDefectHeadClick(position: Int, v: View?) {
        is_text = false
        defectType = 1
        if(defactPosition != position){
//            showAlertMessage(position)
            saveImage()
            selectedColorCode = generateRandomColor()
            alterDataList?.get(defactPosition)?.defectSelect = false
            alterDataList?.get(position)?.defectSelect = true
            alterPopUpRecyclerAdapter?.notifyDataSetChanged()
            defactPosition = position
        }else{
            defactPosition = position
            mPhotoEditorView?.source?.setImageURI(mSaveImageUri)
            initShape();
            alterDataList?.get(position)?.defectSelect = true
            alterPopUpRecyclerAdapter?.notifyDataSetChanged()
        }

        Log.d(TAG, "onDefectHeadClick: ########"+position)
    }

    @SuppressLint("MissingPermission")
    override fun onRejectDefectHeadClick(position: Int, v: View?) {
        is_text = false
        defectType = 3
        if(defactPosition != position){
//            showAlertMessage(position)
            saveImage()
            selectedColorCode = generateRandomColor()
            rejectDataList?.get(defactPosition)?.defectSelect = false
            rejectDataList?.get(position)?.defectSelect = true
            rejectPopUpRecyclerAdapter?.notifyDataSetChanged()
            defactPosition = position
        }else{
            defactPosition = position
            mPhotoEditorView?.source?.setImageURI(mSaveImageUri)
            initShape();
            rejectDataList?.get(position)?.defectSelect = true
            rejectPopUpRecyclerAdapter?.notifyDataSetChanged()
        }

        Log.d(TAG, "onDefectHeadClick: ########"+position)
    }

    @SuppressLint("MissingPermission")
    override fun onSpotDefectHeadClick(position: Int, v: View?) {
        is_text = false
        defectType = 2
        if(defactPosition != position){
//            showAlertMessage(position)
            saveImage()
            selectedColorCode = generateRandomColor()
            spotDataList?.get(defactPosition)?.defectSelect = false
            spotDataList?.get(position)?.defectSelect = true
            spotPopUpRecyclerAdapter?.notifyDataSetChanged()
            defactPosition = position
        }else{
            defactPosition = position
            mPhotoEditorView?.source?.setImageURI(mSaveImageUri)
            initShape();
            spotDataList?.get(position)?.defectSelect = true
            spotPopUpRecyclerAdapter?.notifyDataSetChanged()
        }

        Log.d(TAG, "onDefectHeadClick: ########"+position)
    }

    @SuppressLint("MissingPermission")
    private fun showAlertMessage(position: Int) {
        val builder = android.app.AlertDialog.Builder(this@EditImageActivity)
        builder.setTitle("Message")
            .setMessage("Are you want to save this defect?")
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, which: Int ->
                saveImage()
                selectedColorCode = generateRandomColor()
                if(defectType == 1){
                    alterDataList?.get(defactPosition)?.defectSelect = false
                    alterDataList?.get(position)?.defectSelect = true
                    alterPopUpRecyclerAdapter?.notifyDataSetChanged()
                }
                if(defectType == 2){
                    spotDataList?.get(defactPosition)?.defectSelect = false
                    spotDataList?.get(position)?.defectSelect = true
                    spotPopUpRecyclerAdapter?.notifyDataSetChanged()
                }
                if(defectType == 3){
                    rejectDataList?.get(defactPosition)?.defectSelect = false
                    rejectDataList?.get(position)?.defectSelect = true
                    rejectPopUpRecyclerAdapter?.notifyDataSetChanged()
                }
                defactPosition = position
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    val mRandom: Random = Random(System.currentTimeMillis())
    fun generateRandomColor(): Int {
        // This is the base color which will be mixed with the generated one
        val baseColor = Color.WHITE
        val baseRed = Color.red(baseColor)
        val baseGreen = Color.green(baseColor)
        val baseBlue = Color.blue(baseColor)
        val red: Int = (baseRed + mRandom.nextInt(256))
        val green: Int = (baseGreen + mRandom.nextInt(256))
        val blue: Int = (baseBlue + mRandom.nextInt(256))
        return Color.rgb(red, green, blue)
    }
}
