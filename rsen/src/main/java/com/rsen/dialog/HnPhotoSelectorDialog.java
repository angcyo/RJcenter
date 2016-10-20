package com.rsen.dialog;

import android.net.Uri;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.util.BitmapHelper;
import com.rsen.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 选择图片的对话框
 * <p>
 * Created by angcyo on 2016-10-10.
 */

public class HnPhotoSelectorDialog extends RBaseDialogFragment {


    public static final int CODE_CAMERA = 101;
    public static final int CODE_PHOTO = 102;
    public static final int CODE_CROP = 103;

    private static Uri mCameraImageUri;

    public HnPhotoSelectorDialog() {
    }

    public static String createPhotoPath() {
        String path = FileUtil.getPath("angcyo/Photo/");
        return path + UUID.randomUUID();
    }

    public static Uri getCameraImageUri() {
        return mCameraImageUri;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_selector_photo_layout;
    }

    @Override
    protected void initViewEvent() {
        mViewHolder.v(R.id.selector_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapHelper.getPhotoFromPhotos(mBaseActivity, CODE_PHOTO);
                dismiss();
            }
        });
        mViewHolder.v(R.id.take_photo_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(createPhotoPath());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCameraImageUri = Uri.fromFile(file);
                BitmapHelper.getPhotoFromCamera(mBaseActivity, CODE_CAMERA, mCameraImageUri);
                dismiss();
            }
        });
        mViewHolder.v(R.id.cancel_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public String getImagePath() {
        if (mCameraImageUri == null) {
            return "";
        }
        return mCameraImageUri.getPath();
    }

    public String getImagePath(Uri uri) {
        return BitmapHelper.getPath(mBaseActivity, uri);
    }

//    private Uri mCropImageUri;
//    private Uri mImageUri;
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == HnPhotoSelectorDialog.CODE_PHOTO) {
//                Uri data1 = data.getData();
//                mCropImageUri = Uri.fromFile(new File(HnPhotoSelectorDialog.createPhotoPath()));
//                BitmapHelper.cropImageByUri(this, HnPhotoSelectorDialog.CODE_CROP,
//                        Uri.fromFile(new File(BitmapHelper.getPath(this, data1))), mCropImageUri,
//                        200, 200);
//            } else if (requestCode == HnPhotoSelectorDialog.CODE_CAMERA) {
//                mImageUri = HnPhotoSelectorDialog.getCameraImageUri();
//                mCropImageUri = mImageUri;
//                BitmapHelper.cropImageByUri(this, HnPhotoSelectorDialog.CODE_CROP, mImageUri, mCropImageUri, 200, 200);
//            } else if (requestCode == HnPhotoSelectorDialog.CODE_CROP) {
//                mLoveMarkDayViewMode.onSelectorPhoto(mCropImageUri.getPath());
//            }
//        }
//    }
}
