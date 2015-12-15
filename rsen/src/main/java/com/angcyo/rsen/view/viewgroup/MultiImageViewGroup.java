package com.angcyo.rsen.view.viewgroup;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.angcyo.rsen.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by angcyo on 2015/12/3 16:49.
 */
public class MultiImageViewGroup extends LinearLayout implements View.OnClickListener {
    private final static int INVALID_VALUE = -1;
    private int mImageNum = 5;//支持的图片数量
    private Map<Integer, Object> mImages;//保存所有的图片,或者图片资源
    @DrawableRes
    private int mDefaultDrawableRes;//测试使用

    private float mImageWidth = 100;//图片宽度
    private float mImageHeight = 100;//图片高度

    private float mHorSpacing = 10;//图片之间的间隔大小

    private int TAG_ADDVIEW = -1;//添加按钮的tag

    private OnAddViewClickListener mAddListener;//添加图片回调
    private OnImageViewClickListener mImageListener;//图片回调

    private boolean addAnim = true;//激活添加动画
    private boolean removeAnim = true;//激活删除动画

    private int ANIM_TIME = 200;//动画持续时间, < 0 :自动设置


    /**
     * Instantiates a new Multi image view group.
     *
     * @param context the context
     */
    public MultiImageViewGroup(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Multi image view group.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public MultiImageViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Multi image view group.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public MultiImageViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiImageViewGroup);
        mDefaultDrawableRes = typedArray.getResourceId(R.styleable.MultiImageViewGroup_default_img, INVALID_VALUE);
        int num = typedArray.getInteger(R.styleable.MultiImageViewGroup_image_num, INVALID_VALUE);
        if (num > 0) {
            mImageNum = num;
        }
        mHorSpacing = typedArray.getDimension(R.styleable.MultiImageViewGroup_horizontal_spacing, mHorSpacing);
        mImageWidth = typedArray.getDimension(R.styleable.MultiImageViewGroup_image_width, mImageWidth);
        mImageHeight = typedArray.getDimension(R.styleable.MultiImageViewGroup_image_height, mImageHeight);
        typedArray.recycle();
        init();
    }

    /**
     * Is path exist boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public static boolean isPathExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * Gets thumb bitmap.
     *
     * @param pathName  the path name
     * @param refWidth  the ref width
     * @param refHeight the ref height
     * @return the thumb bitmap
     */
    public static Bitmap getThumbBitmap(String pathName, float refWidth, float refHeight) {
        Bitmap retBitmap = null;
        try {
//            float density = getResources().getDisplayMetrics().density;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, options);

            options.inSampleSize = calcSampleSize(options, refWidth, refHeight);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            retBitmap = BitmapFactory.decodeFile(pathName, options);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return retBitmap;
    }

    private static int calcSampleSize(BitmapFactory.Options options, float refWidth, float refHeight) {
        float width = options.outWidth;
        float height = options.outHeight;
        int sample = 1;
        if (width > refWidth || height > refHeight) {
            int widthRatio = Math.round(width / refWidth);
            int heightRatio = Math.round(height / refHeight);
            sample = Math.max(widthRatio, heightRatio);
        }
        return sample;
    }

    private void init() {
        mImages = new HashMap<Integer, Object>(mImageNum);

//        ImageLoader.getInstance().dis
        initImages();

        initAddButton();
//        setClipChildren(false);
        setOrientation(HORIZONTAL);
//        setBackgroundColor(Color.RED);
    }

    private int getAnimTime() {
        if (ANIM_TIME <= 0) {
            return getResources().getInteger(android.R.integer.config_shortAnimTime);
        }
        return ANIM_TIME;
    }

    /**
     * 添加图片的按钮
     */
    private void initAddButton() {
        ImageView addView = getImageView();
        addView.setBackgroundResource(R.drawable.shop_evaluation_add_bt);
        addView.setOnClickListener(this);
        addView.setTag(TAG_ADDVIEW);
        addView(addView);
    }

    /**
     * 移出添加按钮
     */
    private void removeAddButton() {
        View view = findViewWithTag(TAG_ADDVIEW);
        if (view != null) {
            removeView(view);
        }
    }

    /**
     * 返回所有添加的图片
     *
     * @return the images
     */
    public List<String> getImages() {
        List<String> images = new ArrayList<String>();
        for (Map.Entry<Integer, Object> entry : mImages.entrySet()) {
            images.add((String) entry.getValue());
        }
        return images;
    }

    /**
     * 添加一张图片
     *
     * @param imagePath the image path
     * @return the boolean
     */
    public boolean addImageView(String imagePath) {
        if (!isPathExist(imagePath)) {
            return false;
        }

        final ImageView imageView = getImageView();
        Bitmap bitmap = getThumbBitmap(imagePath, mImageWidth, mImageHeight);
        int tag = getChildCount() - 1;

        imageView.setImageBitmap(bitmap);
        imageView.setTag(tag);
        imageView.setOnClickListener(this);

        mImages.put(tag, imagePath);

        //获取动画开始的位置
        Rect r = new Rect();
        View addView = findViewWithTag(TAG_ADDVIEW);
        addView.getGlobalVisibleRect(r);

        removeAddButton();
        addView(imageView);

        if (mImages.size() < mImageNum) {
            initAddButton();
        }

        //开始动画
        if (addAnim) {
            startAddViewAnim(r, bitmap, null);
        }
        return true;
    }

    private void startAddViewAnim(View withView, Bitmap bitmap, Runnable endAction) {
        Rect rect = new Rect();
        withView.getGlobalVisibleRect(rect);
        startAddViewAnim(rect, bitmap, endAction);
    }

    private void startAddViewAnim(Rect rect, Bitmap bitmap, final Runnable endAction) {
        final ImageView animView = new ImageView(getContext());
        animView.setImageBitmap(bitmap);
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(rect.width(), rect.height());
        params.setMargins(rect.left, rect.top, 0, 0);
        ((FrameLayout) getRootView()).addView(animView, params);

        Animation animation = generateScaleAnim(2f, 1.1f, new Runnable() {
            @Override
            public void run() {
                ((FrameLayout) getRootView()).removeView(animView);
                post(endAction);
            }
        });
//        animation.setStartOffset(200);
        animView.startAnimation(animation);
    }

    private Animation generateScaleAnim(float from, float to, final Runnable endAction) {
        ScaleAnimation animation = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(getAnimTime());
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (endAction != null) {
                    post(endAction);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    /**
     * 删除一张图片
     *
     * @param view the view
     */
    public void removeImageView(final View view) {
        int position = (Integer) view.getTag();
        view.setClickable(false);
        mImages.remove(position);
        if (removeAnim) {
            view.startAnimation(generateScaleAnim(1f, 0.1f, new Runnable() {
                @Override
                public void run() {
                    animRemoveView(view);
                }
            }));
        } else {
            animRemoveView(view);
        }
    }

    /**
     * Sets add anim.
     *
     * @param addAnim the add anim
     */
    public void setAddAnim(boolean addAnim) {
        this.addAnim = addAnim;
    }

    /**
     * Sets remove anim.
     *
     * @param removeAnim the remove anim
     */
    public void setRemoveAnim(boolean removeAnim) {
        this.removeAnim = removeAnim;
    }

    private void animRemoveView(View view) {
        view.setVisibility(GONE);
        removeAddButton();
        if (mImages.size() < mImageNum) {
            initAddButton();
        }
    }

    private void initImages() {
        if (mImages == null || mImages.size() == 0)
            return;

        for (int i = 0; i < mImages.size(); i++) {
            Object img = mImages.get(i);
            if (img instanceof IntegerRes) {

            } else if (img instanceof String) {

            }
        }
    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(((int) mImageWidth), ((int) mImageHeight));
        if (getChildCount() > 0) {
            layoutParams.setMargins((int) mHorSpacing, 0, 0, 0);
        }
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER);//图片缩放形式
        if (mDefaultDrawableRes != INVALID_VALUE) {
            imageView.setImageResource(mDefaultDrawableRes);
        }
        return imageView;
    }

    private ImageView getImageView(@DrawableRes int res) {
        ImageView imageView = getImageView();
        imageView.setImageResource(res);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        if (((Integer) v.getTag()) == TAG_ADDVIEW) {
            if (mAddListener != null) {
                mAddListener.onAddViewClick(v);
            }
        } else {
            if (mImageListener != null) {
                mImageListener.onImageViewClick(v);
            }
        }
    }

    /**
     * Sets add listener.
     *
     * @param mAddListener the m add listener
     */
    public void setAddListener(OnAddViewClickListener mAddListener) {
        this.mAddListener = mAddListener;
    }

    /**
     * Sets image view listener.
     *
     * @param listener the listener
     */
    public void setImageViewListener(OnImageViewClickListener listener) {
        this.mImageListener = listener;
    }

    /**
     * The interface On add view click listener.
     */
    public interface OnAddViewClickListener {
        /**
         * 点击添加图片的回调
         *
         * @param v the v
         */
        void onAddViewClick(View v);
    }

    /**
     * The interface On image view click listener.
     */
    public interface OnImageViewClickListener {
        /**
         * 点击图片的回调
         *
         * @param v the v
         */
        void onImageViewClick(View v);
    }
}
