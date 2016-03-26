package com.angcyo.sample.DialogDemo;

import android.content.DialogInterface;
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
import android.widget.EditText;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.base.RBaseViewHolder;
import com.rsen.base.RSimpleDialogFragment;
import com.rsen.util.T;

public class DialogFragmentActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_dialog_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        new TestDialogFragment().show(getSupportFragmentManager(), "DialogFragment");

        findViewById(R.id.editText).setFocusable(false);
//        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.activity_dialog_fragment).create();
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
//        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        attributes.gravity = Gravity.BOTTOM;
//        alertDialog.getWindow().setAttributes(attributes);
//        alertDialog.show();

//        new TestDialog().show(getSupportFragmentManager(), "Dialog");

        RSimpleDialogFragment.showDialog(getSupportFragmentManager(), null, new RSimpleDialogFragment.BuilderListener() {
            @Override
            public int getLayoutId() {
                return R.layout.activity_dialog_fragment;
            }

            @Override
            public void initView(RSimpleDialogFragment dialogFragment, RBaseViewHolder viewHolder, Bundle args) {
                viewHolder.v(R.id.cancel).setOnClickListener(v -> {
                    dialogFragment.dismiss();
                });
                viewHolder.v(R.id.ok).setOnClickListener(v -> {
                    T.show(DialogFragmentActivity.this, "ok");
                });
                ((EditText) viewHolder.v(R.id.editText)).setHint("你可以在此输入内容");
            }

            @Override
            public void onDialogDismiss(RBaseViewHolder viewHolder) {
                T.show(DialogFragmentActivity.this, "Dismiss");
            }
        });
    }


    public static class TestDialogFragment extends RBaseDialogFragment {

        @Override
        protected int getContentView() {
            return R.layout.activity_dialog_fragment;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
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
