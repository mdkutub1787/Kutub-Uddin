package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


public class NotEmptySpinner extends BaseValidation {
    private String mDefaultText;
    public static Validation build(Context context, String defaultText) {
        return new NotEmptySpinner(context, defaultText);
    }

    private NotEmptySpinner(Context context, String defaultText) {
        super(context);
        mDefaultText = defaultText;
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_empty_spinner, mDefaultText);
    }

    @Override
    public boolean isValid(String text) {
        return !text.equals(mDefaultText);
    }
}