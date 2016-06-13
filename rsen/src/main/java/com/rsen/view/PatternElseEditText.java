package com.rsen.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by robi on 2016-06-13 11:18.
 */

public class PatternElseEditText extends EditText {
    static final char CHAR_SPACE = ' ';
    static final char CHAR_NUMBER_FLAG = '#';
    private String mPattern;
    private OnPatternComparedListener l;
    private final TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String beforeText = s.toString();
            // 删除倒数第二个空格
            if (start == s.length() - 1 && count == 1 && after == 0) {
                if (!TextUtils.isEmpty(mPattern)) {
                    if (beforeText.length() > 1) {
                        if (CHAR_SPACE == beforeText.charAt(beforeText.length() - 2)) {
                            setTextAvoidCallback(beforeText.substring(0, beforeText.length() - 2));
                        }
                    }
                }
                // 删除中间空格
            } else if (count == 1 && after == 0) {
                if (!TextUtils.isEmpty(mPattern)) {
                    if (CHAR_SPACE == beforeText.charAt(start)) {
                        int cursorStart = getSelectionStart();
                        setTextAvoidCallback(beforeText);
                        setSelection(cursorStart - 1);
                    }
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String afterText = s.toString();
            // 补充末尾空格
            if (start == afterText.length() - 1 && before == 0 && count == 1) {
                if (!TextUtils.isEmpty(mPattern)) {
                    int spaceIdx = afterText.length() - 1;
                    if (mPattern.length() > spaceIdx) {
                        if (CHAR_SPACE == mPattern.charAt(spaceIdx)) {
                            setTextAvoidCallback(afterText.substring(0, afterText.length() - 1) + CHAR_SPACE + afterText.substring(afterText.length() - 1, afterText.length()));
                        }
                    }
                }
                // 内容重设
            } else if (start != afterText.length() || before * count != 0 || before > 1 || count > 1) {
//                LogUtils.d("PatternEditText content changed ! reset by mPattern:" + mPattern);
                if (!TextUtils.isEmpty(mPattern)) {
                    int cursorStart = getSelectionStart();
                    String fixedText = afterText.replaceAll(String.valueOf(CHAR_SPACE), "");
                    StringBuilder sb = new StringBuilder();
                    int sbIdx = 0;
                    for (int i = 0; i < mPattern.length(); i++) {
                        if (mPattern.charAt(i) == CHAR_SPACE) {
                            sb.append(CHAR_SPACE);
                        } else {
                            if (sbIdx + 1 >= fixedText.length()) {
                                break;
                            }
                            sb.append(fixedText.charAt(sbIdx++));
                        }
                    }
                    if (sbIdx < fixedText.length()) {
                        sb.append(fixedText.substring(sbIdx));
                    }
                    setTextAvoidCallback(sb.toString());
                    setSelection(cursorStart);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (l != null) {
                if (!TextUtils.isEmpty(mPattern)) {
                    if (getText().length() > mPattern.length()) {
                        l.onCompared(false);
                        return;
                    }

                    for (int i = 0; i < getText().length(); i++) {
                        char c = mPattern.charAt(i);
                        if (c != CHAR_NUMBER_FLAG && c != CHAR_SPACE) {
                            if (c != getText().charAt(i)) {
                                l.onCompared(false);
                                return;
                            }
                        }
                    }
                }
                l.onCompared(true);
            }
        }
    };

    public PatternElseEditText(Context context) {
        this(context, null);
    }

    public PatternElseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(tw);

        setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public boolean patternCompare() {
        if (TextUtils.isEmpty(mPattern)) {
            return true;
        }
        if (getText().length() != mPattern.length()) {
            return false;
        }
        for (int i = 0; i < getText().length(); i++) {
            char c = mPattern.charAt(i);
            if (c != CHAR_NUMBER_FLAG && c != CHAR_SPACE) {
                if (c != getText().charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setPattern(String pattern, OnPatternComparedListener callback) {
        mPattern = pattern;
        l = callback;
    }

    public void setTextAvoidCallback(CharSequence text) {
        removeTextChangedListener(tw);
        setText(text);
        setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        addTextChangedListener(tw);
    }

    public interface OnPatternComparedListener {
        void onCompared(boolean isExpected);
    }
}
