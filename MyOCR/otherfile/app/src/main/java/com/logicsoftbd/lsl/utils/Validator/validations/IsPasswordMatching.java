package com.logicsoftbd.lsl.utils.Validator.validations;

import android.content.Context;

import com.logicsoftbd.lsl.R;


/**
 * Created by Jahid on 3/2/2017
 * @copyright Data-soft.
 */

public class IsPasswordMatching extends BaseValidation {

    private static String mPassword;


    private IsPasswordMatching(Context context, String password) {
        super(context);
        mPassword = password;
    }

    public static Validation build(Context context,String password) {
        return new IsPasswordMatching(context, password);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validations_password_match);
    }

    @Override
    public boolean isValid(String text) {
        //Log.e("isValid", text+"------"+mPassword);
        if (!text.equals(mPassword)) {
            return false;
        }
        return true;
    }
}