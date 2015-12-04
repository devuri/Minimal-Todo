package com.example.avjindersinghsekhon.minimaltodo;

import android.content.Context;
import android.content.res.TypedArray;

import java.security.InvalidParameterException;


public class Utils {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    /** Ensure the given string is not longer than the given maximal length.
     * If the string has a greater length, shorten it and replace the last 3 characters
     * with "...". This is why maxLength must be greater than 3. */
    public static String LimitString(String s, int maxLength) {
        if (maxLength <= 3) {
            throw new InvalidParameterException(String.format("maxLength %d must be greater than 3", maxLength));
        }
        if (s.length() > maxLength) {
            return s.substring(0, maxLength-3)+"...";
        }
        return s;
    }
}
