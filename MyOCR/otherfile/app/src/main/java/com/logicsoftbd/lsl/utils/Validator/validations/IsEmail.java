package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


public class IsEmail extends BaseValidation {

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private IsEmail(Context context) {
        super(context);
    }

    public static Validation build(Context context) {
        return new IsEmail(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_not_email);
    }

    @Override
    public boolean isValid(String text) {
        return text.matches(EMAIL_PATTERN);
    }
}