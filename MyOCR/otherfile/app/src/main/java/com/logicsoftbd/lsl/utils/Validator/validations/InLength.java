package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


public class InLength extends BaseValidation {

    private int mMin;
    private int mMax;

    private InLength(Context context, int min, int max) {
        super(context);
        mMin = min;
        mMax = max;
    }

    public static Validation build(Context context, int min, int max) {
        return new InLength(context, min, max);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_not_in_length, mContext.getString(R.string.three), mContext.getString(R.string.two_fifty));
    }

    public boolean isValid(String text) {
        try {
            int value = text.length();
            if ((value >= mMin) && (value <= mMax)) {
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }
}