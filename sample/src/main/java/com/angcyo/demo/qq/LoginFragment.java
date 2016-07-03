package com.angcyo.demo.qq;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseFragment;

/**
 * Created by angcyo on 2016-07-03 16:08.
 */
public class LoginFragment extends RBaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View rootView) {
        mViewHolder.v(R.id.buttonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello Login.", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
