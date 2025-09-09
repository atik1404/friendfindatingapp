package com.friendfinapp.dating.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.friendfinapp.dating.R;

public class ProgressCustomDialog extends Dialog {
    public ProgressCustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.custom_loader);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        setCancelable(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Request focus on an invisible view within the dialog to keep the soft keyboard open
        View dummyView = findViewById(R.id.dummyview);
        dummyView.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
