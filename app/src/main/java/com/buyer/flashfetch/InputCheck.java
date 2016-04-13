package com.buyer.flashfetch;

/**
 * Created by bikash on 10-03-2016.
 */
import android.text.InputFilter;
import android.text.Spanned;

public class InputCheck implements InputFilter {

    private int min, max;

    public InputCheck(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputCheck(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}