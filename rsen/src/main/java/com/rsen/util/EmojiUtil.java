package com.rsen.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.angcyo.rsen.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
    public static final int[] EmojiResArray = {
            R.drawable.f_000,
            R.drawable.f_001,
            R.drawable.f_002,
            R.drawable.f_003,
            R.drawable.f_004,
            R.drawable.f_005,
            R.drawable.f_006,
            R.drawable.f_007,
            R.drawable.f_008,
            R.drawable.f_009,
            R.drawable.f_010,
            R.drawable.f_011,
            R.drawable.f_012,
            R.drawable.f_013,
            R.drawable.f_014,
            R.drawable.f_015,
            R.drawable.f_016,
            R.drawable.f_017,
            R.drawable.f_018,
            R.drawable.f_019,
            R.drawable.f_020,
            R.drawable.f_021,
            R.drawable.f_022,
            R.drawable.f_023,
            R.drawable.f_024,
            R.drawable.f_025,
            R.drawable.f_026,
            R.drawable.f_027,
            R.drawable.f_028,
            R.drawable.f_029,
            R.drawable.f_030,
            R.drawable.f_031,
            R.drawable.f_032,
            R.drawable.f_033,
            R.drawable.f_034,
            R.drawable.f_035,
            R.drawable.f_036,
            R.drawable.f_037,
            R.drawable.f_038,
            R.drawable.f_039,
            R.drawable.f_040,
            R.drawable.f_041,
            R.drawable.f_042,
            R.drawable.f_043,
            R.drawable.f_044,
            R.drawable.f_045,
            R.drawable.f_046,
            R.drawable.f_047,
            R.drawable.f_048,
            R.drawable.f_049,
            R.drawable.f_050,
            R.drawable.f_051,
            R.drawable.f_052,
            R.drawable.f_053,
            R.drawable.f_054,
            R.drawable.f_055,
            R.drawable.f_056,
            R.drawable.f_057,
            R.drawable.f_058,
            R.drawable.f_059,
            R.drawable.f_060,
            R.drawable.f_061,
            R.drawable.f_062,
            R.drawable.f_063,
            R.drawable.f_064,
            R.drawable.f_065,
            R.drawable.f_066,
            R.drawable.f_067,
            R.drawable.f_068,
            R.drawable.f_069,
            R.drawable.f_070,
            R.drawable.f_071,
            R.drawable.f_072,
            R.drawable.f_073,
            R.drawable.f_074,
            R.drawable.f_075,
            R.drawable.f_076,
            R.drawable.f_077,
            R.drawable.f_078,
            R.drawable.f_079,
            R.drawable.f_080,
            R.drawable.f_081,
            R.drawable.f_082,
            R.drawable.f_083,
            R.drawable.f_084,
            R.drawable.f_085,
            R.drawable.f_086,
            R.drawable.f_087,
            R.drawable.f_088,
            R.drawable.f_089,
            R.drawable.f_090,
            R.drawable.f_091,
            R.drawable.f_092,
            R.drawable.f_093,
            R.drawable.f_094,
            R.drawable.f_095,
            R.drawable.f_096,
            R.drawable.f_097,
            R.drawable.f_098,
            R.drawable.f_099,
            R.drawable.f_100,
            R.drawable.f_101,
            R.drawable.f_102,
            R.drawable.f_103,
            R.drawable.f_104,
            R.drawable.f_105,
            R.drawable.f_106,
            R.drawable.f_107,
            R.drawable.f_108,
            R.drawable.f_109,
            R.drawable.f_110,
            R.drawable.f_111,
            R.drawable.f_112,
            R.drawable.f_113,
            R.drawable.f_114,
            R.drawable.f_115,
            R.drawable.f_116,
            R.drawable.f_117,
            R.drawable.f_118,
            R.drawable.f_119,
            R.drawable.f_120,
            R.drawable.f_121,
            R.drawable.f_122,
            R.drawable.f_123,
            R.drawable.f_124,
            R.drawable.f_125,
            R.drawable.f_126,
            R.drawable.f_127,
            R.drawable.f_128,
            R.drawable.f_129,
            R.drawable.f_130,
            R.drawable.f_131,
            R.drawable.f_132,
            R.drawable.f_133,
            R.drawable.f_134,
            R.drawable.f_135,
            R.drawable.f_136,
            R.drawable.f_137,
            R.drawable.f_138,
            R.drawable.f_139,
    };
    public static final String[] EmojiTextArray = {
            "[微笑]",
            "[撇嘴]",
            "[色]",
            "[发呆]",
            "[得意]",
            "[流泪]",
            "[害羞]",
            "[闭嘴]",
            "[睡]",
            "[大哭]",
            "[尴尬]",
            "[发怒]",
            "[调皮]",
            "[呲牙]",
            "[惊讶]",
            "[难过]",
            "[严肃]",
            "[冷汗]",
            "[抓狂]",
            "[吐]",
            "[偷笑]",
            "[可爱]",
            "[白眼]",
            "[傲慢]",
            "[饥饿]",
            "[困]",
            "[惊恐]",
            "[流汗]",
            "[憨笑]",
            "[大兵]",
            "[奋斗]",
            "[咒骂]",
            "[疑问]",
            "[嘘]",
            "[晕]",
            "[折磨]",
            "[衰]",
            "[骷髅]",
            "[敲打]",
            "[再见]",
            "[擦汗]",
            "[抠鼻]",
            "[鼓掌]",
            "[糗大了]",
            "[坏笑]",
            "[左哼哼]",
            "[右哼哼]",
            "[哈欠]",
            "[鄙视]",
            "[委屈]",
            "[快哭了]",
            "[阴险]",
            "[亲嘴]",
            "[吓]",
            "[可怜]",
            "[菜刀]",
            "[西瓜]",
            "[啤酒]",
            "[篮球]",
            "[乒乓]",
            "[咖啡]",
            "[饭]",
            "[猪头]",
            "[玫瑰]",
            "[凋谢]",
            "[示爱]",
            "[爱心]",
            "[心碎]",
            "[蛋糕]",
            "[闪电]",
            "[炸弹]",
            "[刀]",
            "[足球]",
            "[瓢虫]",
            "[便便]",
            "[月亮]",
            "[太阳]",
            "[礼物]",
            "[拥抱]",
            "[强]",
            "[弱]",
            "[握手]",
            "[胜利]",
            "[抱拳]",
            "[勾引]",
            "[拳头]",
            "[差劲]",
            "[爱你]",
            "[NO]",
            "[OK]",
            "[爱情]",
            "[飞吻]",
            "[跳跳]",
            "[发抖]",
            "[怄火]",
            "[转圈]",
            "[磕头]",
            "[回头]",
            "[跳绳]",
            "[挥手]",
            "[激动]",
            "[街舞]",
            "[献吻]",
            "[左太极]",
            "[右太极]",
            "[小女孩]",
            "[人民币]",
            "[招财猫]",
            "[双喜]",
            "[鞭炮]",
            "[灯笼]",
            "[发财]",
            "[K歌]",
            "[购物]",
            "[邮件]",
            "[帅]",
            "[喝彩]",
            "[祈祷]",
            "[爆筋]",
            "[棒棒糖]",
            "[喝奶]",
            "[下面]",
            "[香蕉]",
            "[飞机]",
            "[开车]",
            "[左车头]",
            "[车厢]",
            "[右车头]",
            "[多云]",
            "[下雨]",
            "[钞票]",
            "[熊猫]",
            "[灯泡]",
            "[风车]",
            "[闹钟]",
            "[雨伞]",
            "[气球]",
            "[钻戒]",
            "[沙发]",
            "[纸巾]"
    };
    private static ArrayList<Emoji> emojiList;

    static {
        emojiList = generateEmojis();
    }

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageUri(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void handlerEmojiText(EditText comment) throws IOException {
        handlerEmojiText(comment, comment.getText().toString(), comment.getContext());
    }

    public static void handlerEmojiText(EditText comment, String content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                            , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
        comment.setSelection(sb.length());
    }

    public static void handlerEmojiText(TextView comment) throws IOException {
        handlerEmojiText(comment, comment.getText().toString(), comment.getContext());
    }

    public static void handlerEmojiText(TextView comment, String content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                            , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
    }


    public static class Emoji implements Serializable {
        int imageUri;
        String content;

        public int getImageUri() {
            return imageUri;
        }

        public void setImageUri(int imageUri) {
            this.imageUri = imageUri;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
