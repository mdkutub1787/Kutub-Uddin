package com.logicsoftbd.lsl.utils.Validator;

import com.google.android.material.textfield.TextInputLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.logicsoftbd.lsl.utils.Validator.validations.Validation;

import java.util.LinkedList;
import java.util.List;


public class Field {

    private List<Validation> mValidations = new LinkedList<Validation>();
    private EditText mTextView;

    private TextInputLayout mTextInputLayout;
    private TextView mETextView;
    private Spinner mSpinner;
    private boolean isSpinner = false;

    private Field(EditText textView) {
        this.mTextView = textView;
    }

    private Field(TextView textView) {
        this.mETextView = textView;
    }

    private Field(Spinner spinner) {
        this.mSpinner = spinner;
    }

    private Field(EditText textView, TextInputLayout TextInputLayout) {
        this.mTextView = textView;
        this.mTextInputLayout = TextInputLayout;
    }


    public static Field using(EditText textView, TextInputLayout TextInputLayout) {
        return new Field(textView, TextInputLayout);
    }


    public static Field using(EditText textView) {
        return new Field(textView);
    }

    public static Field using(TextView textView) {
        return new Field(textView);
    }

    public static Field using(Spinner spinner) {
        return new Field(spinner);
    }

    public Field validate(Validation what) {
        mValidations.add(what);
        return this;
    }

    public EditText getTextView() {
        return mTextView;
    }

    public TextInputLayout getTextInputView() {
        return mTextInputLayout;
    }

    public boolean isValid() throws FieldValidationException {
        for (Validation validation : mValidations) {
            if (!validation.isValid(mTextView.getText().toString())) {
                String errorMessage = validation.getErrorMessage();
                throw new FieldValidationException(errorMessage, mTextView, mTextInputLayout);
            }
        }
        return true;
    }

    public boolean isValidTextView() throws FieldValidationException {
        for (Validation validation : mValidations) {
            if (!validation.isValid(mETextView.getText().toString())) {
                String errorMessage = validation.getErrorMessage();
                throw new FieldValidationException(errorMessage, mETextView);
            }
        }
        return true;
    }

    public boolean isValidSpinner() throws FieldValidationException {
        for (Validation validation : mValidations) {
            if (mSpinner.getSelectedItem() != null) {
                if (!validation.isValid(mSpinner.getSelectedItem().toString())) {
                    String errorMessage = validation.getErrorMessage();
                    throw new FieldValidationException(errorMessage, mSpinner);
                }
            }
        }
        return true;
    }

}