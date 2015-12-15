package com.rsen.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.StringDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.angcyo.rsen.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 * Created by angcyo on 15-12-15 015 10:49 上午.
 */
public class ControlEditText extends LinearLayout implements View.OnClickListener {
    private static final String TAG_LEFT = "left";
    private static final String TAG_RIGHT = "right";
    private static final String TAG_CENTER = "center";
    private static final String TAG_THIS = "this";
    private static int ADD_VALUE = 1;//添加值的步长
    private static int SUB_VALUE = -1;//减去值的步长
    private Button leftButton, rightButton;//左右2个按钮
    private EditText centerEditText;//中间的输入框
    private float buttonWeight = 0.2f;//按钮所占的比重
    private float mRadii = 5;//圆角,像素
    private float mBorderWidth = 2;//边框的宽度,像素
    private int mMaxNum = 99999;//最大显示数
    private int mMinNum = 100;//最小显示数
    private boolean mErrorTip = true;//错误提示,开关
    private String mErrorTipText = "不在限定值范围内";//错误提示文本
    @ColorInt
    private int mBorderColor = Color.BLUE;//边框的颜色
    @ColorInt
    private int mBorderColorPress = Color.GREEN;//边框按下状态的颜色
    private float mWrapWidth = 120;//默认宽度,dp单位
    private float mWrapHeight = 40;//默认高度,dp单位

    /**
     * Instantiates a new Control edit text.
     *
     * @param context the context
     */
    public ControlEditText(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Control edit text.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ControlEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Control edit text.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public ControlEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ControlEditText);

        ADD_VALUE = typedArray.getInt(R.styleable.ControlEditText_add_step, ADD_VALUE);
        SUB_VALUE = typedArray.getInt(R.styleable.ControlEditText_sub_step, SUB_VALUE);
        mMaxNum = typedArray.getInt(R.styleable.ControlEditText_max_value, mMaxNum);
        mMinNum = typedArray.getInt(R.styleable.ControlEditText_min_value, mMinNum);
        buttonWeight = typedArray.getFloat(R.styleable.ControlEditText_button_weight, buttonWeight);
        mRadii = typedArray.getDimension(R.styleable.ControlEditText_border_radii, mRadii);
        mBorderWidth = typedArray.getDimension(R.styleable.ControlEditText_border_width, mBorderWidth);
        mBorderColor = typedArray.getColor(R.styleable.ControlEditText_border_color, mBorderColor);
        mBorderColorPress = typedArray.getColor(R.styleable.ControlEditText_border_color_press, mBorderColorPress);
        mErrorTip = typedArray.getBoolean(R.styleable.ControlEditText_show_error, mErrorTip);
        String text = typedArray.getString(R.styleable.ControlEditText_error_text);
        if (!TextUtils.isEmpty(text)) {
            mErrorTipText = text;
        }

        typedArray.recycle();

        init();
        initDefaultData();
    }

    @SuppressLint("NewApi")
    private static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Reflect field.
     *
     * @param editText the edit text
     */
    public static void reflectField(EditText editText) {
        //getField 只能获取 public声明的字段
        //getDeclaredField 获取 所有声明的字段

        try {
            Field mCursorDrawableRes = editText.getClass().getSuperclass().getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.setInt(editText, 0);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate text color color state list.
     *
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the color state list
     */
    public static ColorStateList generateTextColor(@ColorInt int pressColor, @ColorInt int defaultColor) {
        ColorStateList stateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {}}, new int[]{pressColor, defaultColor});
        return stateList;
    }

    /**
     * Generate bg drawable drawable.
     *
     * @param tag          the tag
     * @param radii        the radii
     * @param borderWidth  the border width
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the drawable
     */
    public static Drawable generateBgDrawable(@Tag String tag, float radii, float borderWidth, @ColorInt int pressColor, @ColorInt int defaultColor) {

        //外环的圆角矩形
        float[] outRadii;
        if (tag.equalsIgnoreCase(TAG_LEFT)) {
            outRadii = new float[]{radii, radii, 0, 0, 0, 0, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个
        } else if (tag.equalsIgnoreCase(TAG_RIGHT)) {
            outRadii = new float[]{0, 0, radii, radii, radii, radii, 0, 0};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个
        } else if (tag.equalsIgnoreCase(TAG_THIS)) {
            outRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个
        } else {
            outRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个
        }

        //与内环的距离
        RectF inset = new RectF(borderWidth, borderWidth, borderWidth, borderWidth);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, inset, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    /**
     * Amend edit text.
     *
     * @param editText the edit text
     * @param text     the text
     */
    public static void amendEditText(EditText editText, String text) {
        int selStart = editText.getSelectionStart();
        int selEnd = editText.getSelectionEnd();
        int length = text.length();
        if (selStart > length) {
            selStart = selEnd = length;
        }
        if (selEnd > length) {
            selEnd = length;
        }
        editText.setText(text);
        editText.setSelection(selStart, selEnd);
    }

    private void initDefaultData() {
        centerEditText.setText(mMinNum + "");
    }

    private void init() {
        setOrientation(HORIZONTAL);

        //左按钮
        LayoutParams leftParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        leftParams.weight = buttonWeight;
        leftButton = new Button(getContext());
        leftButton.setId(R.id.left_button);
        leftButton.setTag(TAG_LEFT);
        leftButton.setLayoutParams(leftParams);
        leftButton.setGravity(Gravity.CENTER);
        leftButton.setText("-");
        leftButton.setTextColor(generateTextColor(mBorderColorPress, mBorderColor));
        setBgDrawable(leftButton, generateBgDrawable(TAG_LEFT, mRadii, mBorderWidth, mBorderColorPress, mBorderColor));

        //右按钮
        LayoutParams rightParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        rightParams.weight = buttonWeight;
        rightButton = new Button(getContext());
        rightButton.setId(R.id.right_button);
        rightButton.setTag(TAG_RIGHT);
        rightButton.setLayoutParams(rightParams);
        rightButton.setGravity(Gravity.CENTER);
        rightButton.setText("+");
        rightButton.setTextColor(generateTextColor(mBorderColorPress, mBorderColor));
        setBgDrawable(rightButton, generateBgDrawable(TAG_RIGHT, mRadii, mBorderWidth, mBorderColorPress, mBorderColor));

        //中间输入框
        centerEditText = new EditText(getContext());
        centerEditText.setId(R.id.center_edit_text);
        LayoutParams centerParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        centerParams.weight = 1f - 2 * buttonWeight;
        centerEditText.setTag(TAG_CENTER);
        centerEditText.setTextColor(mBorderColor);
        centerEditText.setSingleLine();
        centerEditText.setSelectAllOnFocus(true);
        centerEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(String.valueOf(mMaxNum).length())});
        centerEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        centerEditText.setGravity(Gravity.CENTER);
        reflectField(centerEditText);
        centerEditText.setLayoutParams(centerParams);
//        centerEditText.addTextChangedListener(new ControlTextWatcher());
        centerEditText.setOnFocusChangeListener(new ControlFocusListener());
        setBgDrawable(centerEditText, null);

        //父布局
        setBgDrawable(this, generateBgDrawable(TAG_THIS, mRadii, mBorderWidth, mBorderColorPress, mBorderColor));

        //添加到布局
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        addView(leftButton);
        addView(centerEditText);
        addView(rightButton);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {//wrap_content
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWrapWidth, getResources().getDisplayMetrics());
        }

        if (heightMode == MeasureSpec.AT_MOST) {//wrap_content
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWrapHeight, getResources().getDisplayMetrics());
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.left_button) {
            modifyValue(SUB_VALUE);
        } else if (id == R.id.right_button) {
            modifyValue(ADD_VALUE);
        }

    }

    private void modifyValue(int newValue) {
        String numText = centerEditText.getText().toString();
        int num = mMinNum;
        if (TextUtils.isEmpty(numText)) {
            num += newValue;
        } else {
            num = Integer.valueOf(numText) + newValue;
        }
        safeSetValue(num);
    }

    /**
     * Safe set value.
     *
     * @param value the value
     */
    public void safeSetValue(int value) {
        boolean show = false;

        if (value < mMinNum) {
            value = mMinNum;
            show = true;
        } else if (value > mMaxNum) {
            value = mMaxNum;
            show = true;
        }
        centerEditText.setError(null);
        amendEditText(centerEditText, value + "");
        if (show && mErrorTip) {
            centerEditText.setError(null);
            centerEditText.setError(mErrorTipText);
            centerEditText.requestFocus();
        }
    }

    /**
     * The interface Tag.
     */
    @StringDef({TAG_LEFT, TAG_RIGHT, TAG_CENTER, TAG_THIS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tag {
    }

    private class ControlTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null || Integer.valueOf(s.toString()) < mMinNum) {
                safeSetValue(mMinNum);
                return;
            }
            if (Integer.valueOf(s.toString()) > mMaxNum) {
                safeSetValue(mMaxNum);
            }
        }
    }

    private class ControlFocusListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                //焦点离开,验证值
                int value = mMinNum;
                String numText = centerEditText.getText().toString();
                if (!TextUtils.isEmpty(numText)) {
                    value = Integer.valueOf(numText);
                }
                safeSetValue(value);
            }
        }
    }
}
