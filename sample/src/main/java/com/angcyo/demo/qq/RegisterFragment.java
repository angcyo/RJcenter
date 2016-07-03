package com.angcyo.demo.qq;

import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseFragment;

/**
 * Created by angcyo on 2016-07-03 16:08.
 */
public class RegisterFragment extends RBaseFragment {

    int pos;

    @Override
    protected int getContentView() {
        return R.layout.fragment_register;
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Bundle arg) {
        if (arg != null) {
            pos = arg.getInt("pos");
        }
    }

    @Override
    protected void initView(View rootView) {
        switch (pos) {
            case 0:
                inflaterLayout("注册的第一步:", "下一步", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBaseActivity instanceof IRegisterListener) {
                            ((IRegisterListener) mBaseActivity).onNext(1);//进入下一个界面
                        }
                    }
                });
                break;
            case 1:
                inflaterLayout("注册的第二步:", "下一步", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBaseActivity instanceof IRegisterListener) {
                            ((IRegisterListener) mBaseActivity).onNext(2);//进入下一个界面
                        }
                    }
                });
                break;
            case 2:
                inflaterLayout("完成", "返回登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBaseActivity instanceof IRegisterListener) {
                            ((IRegisterListener) mBaseActivity).onOk();//返回登录
                        }
                    }
                });
                break;
        }
    }

    private void inflaterLayout(String text, String btText, View.OnClickListener clickListener) {
        mViewHolder.tV(R.id.textView).setText(text);
        mViewHolder.tV(R.id.buttonView).setText(btText);
        mViewHolder.tV(R.id.buttonView).setOnClickListener(clickListener);
    }
}
