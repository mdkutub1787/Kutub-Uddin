package com.logicsoftbd.lsl.utils.Validator;

import android.app.Activity;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class Form {


    private List<Field> mFields = new ArrayList<Field>();

    private Activity mActivity;

    public Form(Activity activity) {
        this.mActivity = activity;
    }

    public void addField(Field field) {
        mFields.add(field);
    }

    public boolean isValid() {
        boolean result = true;
        try {
            for (Field field : mFields) {
                result &= field.isValid();
            }
        } catch (FieldValidationException e) {
            result = false;

            TextInputLayout focusInputLayout = e.getTextInputLayout();
            EditText textView = e.getTextView();
            textView.requestFocus();
            textView.selectAll();

            FormUtils.showKeyboard(mActivity, textView);

            textView.addTextChangedListener(new MyTextWatcher(focusInputLayout));

            if (focusInputLayout == null) {
                showErrorMessage(e.getMessage());
            } else {

                showErrorMessage(e.getMessage(), focusInputLayout);

            }
        }
        return result;
    }

    public boolean isValidTextView() {
        boolean result = true;
        try {
            for (Field field : mFields) {
                result &= field.isValidTextView();
            }
        } catch (FieldValidationException e) {
            result = false;

            TextView textView = e.getTextView();
            showErrorMessage(e.getMessage());
        }
        return result;
    }

    public boolean isValidSpinner() {
        boolean result = true;
        try {
            for (Field field : mFields) {
                result &= field.isValidSpinner();
            }
        } catch (FieldValidationException e) {
            result = false;
            Spinner textView = e.getSpinnerText();
            textView.requestFocus();
            showErrorMessage(e.getMessage());
        }
        return result;
    }


    protected void showErrorMessage(String message) {
        // Crouton.makeText(mActivity, message, Style.ALERT).show();
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    protected void showErrorMessage(String message, TextInputLayout inputLayoutName) {
        // Crouton.makeText(mActivity, message, Style.ALERT).show();
       // Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        inputLayoutName.setErrorEnabled(true);
        inputLayoutName.setError(message);
    }


    private class MyTextWatcher implements TextWatcher {

        private TextInputLayout view;

        private MyTextWatcher(View view) {
            this.view = (TextInputLayout) view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            view.setError(null);
        }
    }


}