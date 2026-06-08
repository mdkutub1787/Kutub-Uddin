package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


public class IsNID extends BaseValidation {

    private static final String NID_PATTERN ="^[0-9]{10}$|^[0-9]{13}$|^[0-9]{17}$";

    private IsNID(Context context) {
        super(context);
    }

    public static Validation build(Context context) {
        return new IsNID(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_not_nid);
    }

    @Override
    public boolean isValid(String text) {
        return text.matches(NID_PATTERN);
    }
}