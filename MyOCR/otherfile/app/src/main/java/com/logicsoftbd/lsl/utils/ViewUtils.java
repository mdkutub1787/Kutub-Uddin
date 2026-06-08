/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.logicsoftbd.lsl.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by janisharali on 27/01/17.
 */

public final class ViewUtils {

    private ViewUtils() {
        // This utility class is not publicly instantiable
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void changeIconDrawableToGray(Context context, Drawable drawable) {
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(ContextCompat
                    .getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static SpannableStringBuilder makeSectionOfTextBold(String text, String textToBold){

        SpannableStringBuilder builder=new SpannableStringBuilder();

        if(textToBold.length() > 0 && !textToBold.trim().equals("")){

            //for counting start/end indexes
            String testText = text.toLowerCase(Locale.US);
            String testTextToBold = textToBold.toLowerCase(Locale.US);
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();
            //for counting start/end indexes

            if(startingIndex < 0 || endingIndex <0){
                return builder.append(text);
            }
            else if(startingIndex >= 0 && endingIndex >=0){

                builder.append(text);
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#6EABD1")), startingIndex, endingIndex, 0);
            }
        }else{
            return builder.append(text);
        }

        return builder;
    }

    public static void prepareSpinner(Context context, Spinner spinner,
                                      ArrayList<String> data) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.item_spinner, data);
        spinner.setAdapter(adapter);

    }

    public static void prepareSpinner(Context context, Spinner spinner,
                                      ArrayList<String> data, String selectedItem) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.item_spinner, data);
        spinner.setAdapter(adapter);

        int selectedPosition = adapter.getPosition(selectedItem);

        spinner.setSelection(selectedPosition);

    }
}
