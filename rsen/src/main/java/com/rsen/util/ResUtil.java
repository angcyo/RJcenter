package com.rsen.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.TypedValue;
import android.view.View;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 15-12-31 031 10:52 上午.
 */
public class ResUtil {

    /**
     * Dp to px float.
     *
     * @param res the res
     * @param dp  the dp
     * @return the float
     */
    public static float dpToPx(Resources res, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());

        return px;
    }

    /**
     * Generate text color color state list.
     *
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the color state list
     */
    public static ColorStateList generateTextColor(int pressColor, int defaultColor) {
        ColorStateList stateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {}},
                new int[]{pressColor, defaultColor});
        return stateList;
    }

    /**
     * Generate bg drawable drawable.
     *
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the drawable
     */
    public static Drawable generateBgDrawable(int pressColor, int defaultColor) {
        //按下状态
        Shape roundRectShape = new RoundRectShape(null, null, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShape);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    /**
     * Generate bg drawable drawable.
     *
     * @param radii        the radii
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the drawable
     */
    public static Drawable generateBgDrawable(Resources res, float radii, int pressColor, int defaultColor) {

        radii = dpToPx(res, radii);

        //外环的圆角矩形
        float[] outRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, null, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, null, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }


    /**
     * Generate bg drawable drawable.
     *
     * @param radii        the radii
     * @param borderWidth  the border width
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the drawable
     */
    public static Drawable generateBgDrawable(Resources res, float radii, float borderWidth, int pressColor, int defaultColor) {

        radii = dpToPx(res, radii);
        borderWidth = dpToPx(res, borderWidth);

        //外环的圆角矩形
        float[] outRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

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

    public static Drawable generateBgDrawable(float rL1, float rL2, float rT1, float rT2,
                                              float rR1, float rR2, float rB1, float rB2,
                                              int pressColor, int defaultColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{rL1, rL2, rT1, rT2, rR1, rR2, rB1, rB2};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

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

    public static Drawable generateBgDrawable(float radiiL, float radiiR, int pressColor, int defaultColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{radiiL, radiiL, radiiR, radiiR, radiiR, radiiR, radiiL, radiiL};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

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
     * Generate bg drawable drawable.
     *
     * @param radii       圆角角度
     * @param borderWidth 厚度
     * @param color       颜色
     * @return the drawable
     */
    public static Drawable generateBgDrawable(float radii, float borderWidth, int color) {
        //外环的圆角矩形
        float[] radiiF = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个
        RectF rectF = new RectF(borderWidth, borderWidth, borderWidth, borderWidth);
        //按下状态
        Shape roundRectShape = new RoundRectShape(radiiF, rectF, radiiF);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(color);//设置颜色

        return shopDrawablePress;
    }

    public static Drawable generateCircleBgDrawable(float width, int color) {
        Shape arcShape = new ArcShape(0, 360);
        ShapeDrawable shopDrawablePress = new ShapeDrawable(arcShape);//圆形shape
        shopDrawablePress.getPaint().setColor(color);//设置颜色
        shopDrawablePress.getPaint().setStyle(Paint.Style.STROKE);//设置颜色
        shopDrawablePress.getPaint().setStrokeWidth(width);//设置颜色
        return shopDrawablePress;
    }

    @SuppressLint("NewApi")
    public static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取ActionBar的高度
     */
    public static float getActionBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float h = actionbarSizeTypedArray.getDimension(0,
                context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        return h;
    }

    /**
     * 获取 colorAccent 颜色
     */
    public static
    @ColorRes
    int getThemeColorAccent(Context context) {
        return getThemeColor(context, "colorAccent");
    }

    /**
     * 获取主题声明的颜色
     */
    public static
    @ColorRes
    int getThemeColor(Context context, String name) {
//        TypedArray array = getTheme().obtainStyledAttributes(new int[] {
//                R.color.colorAccent,
//                android.R.attr.textColorPrimary,
//        });
//        int backgroundColor = array.getColor(0, 0xFF00FF);
//        int textColor = array.getColor(1, 0xFF00FF);
//        array.recycle();
        return getThemeIdentifier(context, name, "color");
    }

    public static float getThemeDimen(Context context, String name) {
        int id = getThemeIdentifier(context, name, "dimen");
        return context.getResources().getDimension(id);
    }

    /**
     * 根据name, 在主题中 寻找资源id
     */
    public static int getThemeIdentifier(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
