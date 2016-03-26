package com.angcyo.sample.DialogDemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseDialogFragment;

public class DialogFragmentActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_dialog_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        new TestDialogFragment().show(getSupportFragmentManager(), "DialogFragment");

        findViewById(R.id.editText).setFocusable(false);
//        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.activity_dialog_fragment).create();
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
//        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        attributes.gravity = Gravity.BOTTOM;
//        alertDialog.getWindow().setAttributes(attributes);
//        alertDialog.show();

//        new TestDialog().show(getSupportFragmentManager(), "Dialog");
    }


    public static class TestDialogFragment extends RBaseDialogFragment {

        @Override
        protected int getContentView() {
            return R.layout.activity_dialog_fragment;
        }
    }

    public static class TestDialog extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Window window = getDialog().getWindow();
//            window.requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.activity_dialog_fragment, (ViewGroup) window.findViewById(android.R.id.content), false);
//            View view = inflater.inflate(R.layout.activity_dialog_fragment, container, false);

            window.setBackgroundDrawable(new ColorDrawable(Color.RED));
//            window.setLayout(-1,-2);

            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            attributes.gravity = Gravity.BOTTOM;
            window.setAttributes(attributes);


//            window.findViewById(android.R.id.content).setBackgroundColor(Color.RED);

            return view;
//            return inflater.inflate(R.layout.activity_dialog_fragment, container);
//            return inflater.inflate(R.layout.activity_dialog_fragment, (ViewGroup) window.findViewById(android.R.id.content), false);
        }
    }

}
