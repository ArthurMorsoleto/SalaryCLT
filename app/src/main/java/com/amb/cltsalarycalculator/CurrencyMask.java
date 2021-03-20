package com.amb.cltsalarycalculator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public class CurrencyMask implements TextWatcher {

    final EditText editText;

    public CurrencyMask(EditText editText) {
        super();
        this.editText = editText;
    }

    private boolean isUpdating = false;
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {

        if (isUpdating) {
            isUpdating = false;
            return;
        }

        isUpdating = true;
        String str = s.toString();

        boolean hasMask = ((str.contains("R$") || str.contains("$")) &&
                (str.contains(".") || str.contains(",")));

        if (hasMask) {
            str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                    .replaceAll("[.]", "");
        }

        try {
            str = nf.format(Double.parseDouble(str) / 100);
            editText.setText(str);
            editText.setSelection(editText.getText().length());
        } catch (NumberFormatException e) {
            s = "";
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}