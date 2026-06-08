package com.logicsoftbd.lsl.utils.Validator;

import com.google.android.material.textfield.TextInputLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class FieldValidationException extends Exception {

    private EditText mTextView;
    private TextView mETextView;
    private Spinner mSpinner;

    private TextInputLayout mTextInputLayout ;


    public FieldValidationException(String message, EditText textView) {
        super(message);
        mTextView = textView;
    }

    public FieldValidationException(String message, EditText textView, TextInputLayout textInputLayout) {
        super(message);
        mTextView = textView;
        mTextInputLayout = textInputLayout;
    }

    public FieldValidationException(String message, TextView textView) {
        super(message);
        mETextView = textView;
    }
    public FieldValidationException(String message, Spinner spinner) {
        super(message);
        mSpinner = spinner;
    }

    public EditText getTextView() {
        return mTextView;
    }
    public Spinner getSpinnerText() {
        return mSpinner;
    }
    public TextView getETextView() {
        return mETextView;
    }

    public  TextInputLayout getTextInputLayout(){ return mTextInputLayout ;}
}