package com.rsen.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by angcyo on 2016-03-27 01:33.
 */
public class RSimpleDialogFragment extends RBaseDialogFragment {

    private BuilderListener builderListener;

    public static void showDialog(FragmentManager fragmentManager, Bundle args, BuilderListener builderListener) {
        RSimpleDialogFragment dialogFragment = new RSimpleDialogFragment();
        dialogFragment.setBuilderListener(builderListener);
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, RSimpleDialogFragment.class.getSimpleName());
    }

    @Override
    protected int getContentView() {
        return builderListener.getLayoutId();
    }

    @Override
    protected void initArguments(Bundle arguments) {
        super.initArguments(arguments);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        builderListener.initView(this, mViewHolder, getArguments());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        builderListener.onDialogDismiss(mViewHolder);
    }

    private void setBuilderListener(BuilderListener listener) {
        builderListener = listener;
    }

    public interface BuilderListener {
        int getLayoutId();

        void initView(RSimpleDialogFragment dialogFragment, RBaseViewHolder viewHolder, Bundle args);

        void onDialogDismiss(RBaseViewHolder viewHolder);
    }
}
