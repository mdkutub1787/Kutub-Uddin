package com.logicsoftbd.lsl.utils;

import android.content.Context;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogHelper {

    public static void showSuccessDialog(Context context, String title, String content) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText("OK")
                .show();
    }
    public static void showErrorDialog(Context context, String title, String content) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText("OK")
                .show();
    }

    public static void showWarningDialog(Context context, String title, String content) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText("OK")
                .show();
    }
}