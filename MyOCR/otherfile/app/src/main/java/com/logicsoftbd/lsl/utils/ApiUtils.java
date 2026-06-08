package com.logicsoftbd.lsl.utils;

import static com.logicsoftbd.lsl.utils.Constants.PREF_NAME_CART_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtils {

    private Context context;
    private static ApiInterface apiInterface;

    public ApiUtils() {
    }

    public static synchronized ApiInterface getInterface(String baseUrl) {
        if (apiInterface == null) {
//            apiInterface = APIClient.getClient().create(ApiInterface.class);
            apiInterface = RetrofitApiClient.getApiClient(baseUrl);
        }
        return apiInterface;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public String getExt(String filePath) {
        int strLength = filePath.lastIndexOf(".");
        if (strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }

    public String getDeviceID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public ApiUtils(Context context) {
        this.context = context;
    }

    public void showToastShort(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void printLog(String logKey, String logValue) {
        Log.d(logKey, logValue);
    }

    public void clearCartItems() {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_CART_STORAGE, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public String convertToBangla(String priceInEnglishNumeric) {
        String convertedString = "";
        char[] arr = priceInEnglishNumeric.toCharArray();
        if (arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == '0') {
                    convertedString += "০";
                } else if (arr[i] == '1') {
                    convertedString += "১";
                } else if (arr[i] == '2') {
                    convertedString += "২";
                } else if (arr[i] == '3') {
                    convertedString += "৩";
                } else if (arr[i] == '4') {
                    convertedString += "৪";
                } else if (arr[i] == '5') {
                    convertedString += "৫";
                } else if (arr[i] == '6') {
                    convertedString += "৬";
                } else if (arr[i] == '7') {
                    convertedString += "৭";
                } else if (arr[i] == '8') {
                    convertedString += "৮";
                } else if (arr[i] == '9') {
                    convertedString += "৯";
                } else if (arr[i] == '.') {
                    convertedString += ".";
                }
            }
        }

        return convertedString;
    }

    public static boolean isValidEmail(String email) {
        boolean isFormatOk = true;
        if (email.indexOf('@') == -1) {
            isFormatOk = false;
        }
        if (email.indexOf('.') == -1) {
            isFormatOk = false;
        }
        if (email.length() > 0) {
            if (email.charAt(0) == '.' || email.charAt(0) == '@'
                    || email.charAt(email.length() - 1) == '.'
                    || email.charAt(email.length() - 1) == '@') {
                isFormatOk = false;
            }
        }
        if (email.indexOf('@') != email.lastIndexOf('@')) {
            isFormatOk = false;
        } else if (isFormatOk == false) {
            return false;
        }

        return true;
    }

    public void clearCardData(){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_CART_STORAGE, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public boolean isValidPhone(String phone) {
        String expression = "^(?:\\+?88|88)?01[15-9]\\d{8}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
