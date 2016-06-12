package com.rsen.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 2016-06-13 00:07.
 */
public class PatternEditText extends EditText {

    public static final String TAG = "PatternEditText";

    /**
     * 分割字符
     */
    private char separatorChar = ' ';

    /**
     * 需要分割的位置
     */
    private List<Integer> separatorPosition;

    /**
     * 分割模版
     */
    private String patternString = "###,####,####";

    public PatternEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
//        setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        setInputType(InputType.TYPE_CLASS_NUMBER);

        if (separatorPosition == null) {
            separatorPosition = new ArrayList<>();
        } else {
            separatorPosition.clear();
        }

        if (!TextUtils.isEmpty(patternString)) {
            for (int i = 0; i < patternString.length(); i++) {
                if (patternString.charAt(i) == ',') {
                    separatorPosition.add(i);
                }
            }
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "beforeTextChanged: s." + s + " start." + start + " count." + count + " after." + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: s." + s + " start." + start + " count." + count + " before." + before);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: s." + s.toString());
            }
        });
    }


    /**
     * 设置分割模版
     */
    public void setPatternString(String patternString) {
        this.patternString = patternString;
        initView();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean keyUp = super.onKeyUp(keyCode, event);
//        getText().append(separatorChar);
        getText().append("T");
        Log.e(TAG, "onKeyUp: keyCode." + keyCode + " keyUp." + keyUp);
        return keyUp;
    }

    private void checkPattern() {
        Editable editable = getText();
        if (!TextUtils.isEmpty(editable)) {
            editable.length();
        }
    }

}
