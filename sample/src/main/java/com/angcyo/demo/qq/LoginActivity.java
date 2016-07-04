package com.angcyo.demo.qq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.github.common.L;

public class LoginActivity extends RBaseActivity implements IRegisterListener {

    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    RegisterFragment registerNextFragment;
    RegisterFragment registerOkFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initBefore() {
        loginFragment = (LoginFragment) Fragment.instantiate(this, LoginFragment.class.getName());
        registerFragment = (RegisterFragment) RegisterFragment.instantiate(this, RegisterFragment.class.getName(), getArg(0));
        registerNextFragment = (RegisterFragment) RegisterFragment.instantiate(this, RegisterFragment.class.getName(), getArg(1));
        registerOkFragment = (RegisterFragment) RegisterFragment.instantiate(this, RegisterFragment.class.getName(), getArg(2));

        L.i("loginFragment : " + loginFragment.hashCode());
        L.i("registerFragment : " + registerFragment.hashCode());
        L.i("registerNextFragment : " + registerNextFragment.hashCode());
        L.i("registerOkFragment : " + registerOkFragment.hashCode());
    }

    private Bundle getArg(int i) {
        Bundle arg = new Bundle();
        arg.putInt("pos", i);
        return arg;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewHolder.v(R.id.loginView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });

        mViewHolder.v(R.id.registerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });
        mViewHolder.v(R.id.registerView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.container, registerNextFragment);
            }
        });
        mViewHolder.v(R.id.registerView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.container, registerOkFragment);
            }
        });
    }

    @Override
    protected boolean enableWindowAnim() {
        return false;
    }

    private void showRegister() {
        replaceFragment(R.id.container, registerFragment);
    }

    private void showLogin() {
        replaceFragment(R.id.container, loginFragment);
    }

    @Override
    public void onNext(int position) {
        switch (position) {
            case 0:
                L.i("注册");
                showRegister();
                break;
            case 1:
                L.i("注册下一步");
                replaceFragment(R.id.container, registerNextFragment);
                break;
            case 2:
                L.i("注册完成");
                replaceFragment(R.id.container, registerOkFragment);
                break;
        }
    }

    @Override
    public void onOk() {
        showLogin();
    }
}
