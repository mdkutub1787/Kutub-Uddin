package com.logicsoftbd.lsl.utils.Validator;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;



public class FormUtils {

    public static void showKeyboard(Context context, TextView textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            // only will trigger it if no physical keyboard is open
            inputMethodManager.showSoftInput(textView, 0);
        }
    }

    public static void hideKeyboard(Context context, TextView textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            // only will trigger it if no physical keyboard is open
            inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
    }

    /*public static  boolean hasCircularImage(@NonNull CircularImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }*/
}