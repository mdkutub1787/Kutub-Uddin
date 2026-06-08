package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


public class NotEmptyTextView extends BaseValidation {
    private String mDefaultText;
    public static Validation build(Context context, String defaultText) {
        return new NotEmptyTextView(context,  defaultText);
    }

    private NotEmptyTextView(Context context, String defaultText) {
        super(context);
        mDefaultText = defaultText;
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_empty_text, mDefaultText);
    }

    @Override
    public boolean isValid(String text) {
        return !text.equals(mDefaultText);
    }
}