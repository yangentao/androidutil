package dev.entao.ui.util;

import android.text.Editable;

/**
 * Created by yet on 2015/10/15.
 */
public abstract class XTextWatcher implements android.text.TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextChanged(s.toString());
    }

    public abstract void afterTextChanged(String text);
}
