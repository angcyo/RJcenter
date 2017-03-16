package com.rsen.widget;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class EmojiFilter implements InputFilter {
    Pattern emojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private int mMax = -1;//默认不过滤长度

    public EmojiFilter() {
    }

    public EmojiFilter(int max) {
        mMax = max;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(CharSequence source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        if (this.emojiPattern.matcher(source).find() || containsEmoji(source)) {
            return "";
        }

        if (mMax != -1) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        return source;
    }
}
