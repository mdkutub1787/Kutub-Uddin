package com.mrahmed.myocr.utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {
    public static int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
