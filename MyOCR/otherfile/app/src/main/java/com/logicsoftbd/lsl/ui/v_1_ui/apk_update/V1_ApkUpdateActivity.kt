package com.logicsoftbd.lsl.ui.v_1_ui.apk_update

import DownloadController
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.logicinfotech.raberp.ui.apkUpdate.checkSelfPermissionCompat
import com.logicinfotech.raberp.ui.apkUpdate.requestPermissionsCompat
import com.logicinfotech.raberp.ui.apkUpdate.shouldShowRequestPermissionRationaleCompat
import com.logicinfotech.raberp.ui.apkUpdate.showSnackbar
import com.logicsoftbd.lsl.R

class V1_ApkUpdateActivity : AppCompatActivity() {
    private  val TAG = "ApkUpdateMainActivity"
    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }

    lateinit var mainLayout: LinearLayout

    lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v1_apk_update)

        val apkUrl:String = intent.getStringExtra("apkUrl").toString()
        Log.d(TAG, "onCreate: ############ " + apkUrl)
        downloadController = DownloadController(this, apkUrl)
        var buttonDownload = findViewById<Button>(R.id.buttonDownload)
        mainLayout = findViewById<LinearLayout>(R.id.mainLayout)
        checkStoragePermission()
        buttonDownload.setText("Downloading....")
        buttonDownload.setOnClickListener {
            // check storage permission granted if yes then start downloading file
            checkStoragePermission()
        }

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ), 1
        )

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
            } else {
                // Permission request was denied.
                mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }
    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading
            downloadController.enqueueDownload()
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission()
        }
    }
    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mainLayout.showSnackbar(
                R.string.storage_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val i = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:$packageName")
                    )
                    finish()
                    startActivity(i)

                }


            }
        } else {
            requestPermissionsCompat(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
    }
}