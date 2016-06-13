package com.rsen.view;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by angcyo on 2016-06-13 00:07.
 */
public class PatternEditText extends EditText {

    public static final String TAG = "PatternEditText";
    /**
     * 用来分割字符
     */
    private char separatorChar = ' ';
    /**
     * 需要分割的位置
     */
    private List<Integer> separatorPosition;
    Runnable delayCheck = this::checkPattern;
    /**
     * 分割模版
     */
    private String patternString = "###,####,####";

    /**
     * 切割模版的分割字符
     */
    private List<Character> splitList = Arrays.asList(' ', ',');

    public PatternEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setInputType(InputType.TYPE_CLASS_NUMBER);

        if (separatorPosition == null) {
            separatorPosition = new ArrayList<>();
        } else {
            separatorPosition.clear();
        }

        if (!TextUtils.isEmpty(patternString)) {
            for (int i = 0; i < patternString.length(); i++) {
                if (splitList.contains(patternString.charAt(i))) {
                    separatorPosition.add(i);
                }
            }
        }
    }

    /**
     * 设置分割模版
     */
    public void setPatternString(String patternString) {
        this.patternString = patternString;
        initView();
        checkPattern();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            //解决长按删除键无法删除的BUG
            removeCallbacks(delayCheck);
            postDelayed(delayCheck, getResources().getInteger(android.R.integer.config_shortAnimTime));
        } else {
            checkPattern();
        }
        return super.onKeyUp(keyCode, event);
    }

    private void checkPattern() {
        StringBuffer rawText = new StringBuffer(getText());
        int selectionStart = getSelectionStart();
        for (int i = 0; i < rawText.length(); i++) {
            char charAt = rawText.charAt(i);
            if (charAt == separatorChar) {
                rawText.delete(i, i + 1);
                if (i < selectionStart) {
                    selectionStart--;
                }
                i--;
            } else if (isKeyPosition(i)) {
                rawText.insert(i, separatorChar);
                if (i < selectionStart) {
                    selectionStart++;
                }
            }
        }
        setText(rawText);
        setSelection(Math.min(rawText.length(), selectionStart));
    }

    private boolean isKeyPosition(int position) {
        return separatorPosition.contains(position);
    }

}
