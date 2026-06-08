package com.logicsoftbd.lsl.ui.v_1_ui.sewing_defect.base

import android.R
import android.app.ProgressDialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity() {
    private var mProgressDialog: ProgressDialog? = null
    private var mPermission: String? = null

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isPermissionGranted(it, mPermission)
    }

    fun requestPermission(permission: String): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
        if (!isGranted) {
            mPermission = permission
            permissionLauncher.launch(permission)
        }
        return isGranted
    }

    open fun isPermissionGranted(isGranted: Boolean, permission: String?) {}

    fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    protected fun showLoading(message: String) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.run {
            setMessage(message)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setCancelable(false)
            show()
        }
    }

    protected fun hideLoading() {
        mProgressDialog?.dismiss()
    }

    protected fun showSnackbar(message: String) {
        val view = findViewById<View>(R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}