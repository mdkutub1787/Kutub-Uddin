package com.logicsoftbd.lsl.utils.runtimepermission;

public interface RPResultListener {
    void onPermissionGranted();

    void onPermissionDenied();
}
