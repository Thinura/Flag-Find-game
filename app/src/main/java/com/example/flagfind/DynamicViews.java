package com.example.flagfind;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

public class DynamicViews {
    Context context;

    public DynamicViews(Context context) {
        this.context = context;
    }

    public TextView createEditText(Context context, char answer, int indexAns) {
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView editText = new TextView(context);
        editText.setLayoutParams(layoutParams);
        editText.setPadding(5, 0, 5, 0);
        editText.setId(indexAns);
        editText.setTypeface(editText.getTypeface(), Typeface.BOLD);
        if (Character.toString(answer).equals(" ") || (Character.toString(answer).equals("(") || Character.toString(answer).equals(")")) || Character.toString(answer).equals(",")) {
            editText.setText(" ");
        } else {
            editText.setText("-");
        }
        editText.setTextSize(12);
        return editText;

    }
}