package com.angcyo.net;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.net.RRetrofit;
import com.rsen.net.service.RApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends RBaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }


    public void button(View view) {
        RApiService service = RRetrofit.create(RApiService.class);

        /*get请求*/
        Call<RApiService.ResponseBean> api = service.getApi("aaa=2&bbb=3");
        api.enqueue(new Callback<RApiService.ResponseBean>() {
            @Override
            public void onResponse(Call<RApiService.ResponseBean> call, Response<RApiService.ResponseBean> response) {
                Log.e("", "");
            }

            @Override
            public void onFailure(Call<RApiService.ResponseBean> call, Throwable t) {
                Log.e("", "");
            }
        });

        /*post请求*/
        RApiService.RequestBean requestBean = new RApiService.RequestBean();
        Call<RApiService.ResponseBean> postApi = service.postApi(requestBean);
        postApi.enqueue(new Callback<RApiService.ResponseBean>() {
            @Override
            public void onResponse(Call<RApiService.ResponseBean> call, Response<RApiService.ResponseBean> response) {

            }

            @Override
            public void onFailure(Call<RApiService.ResponseBean> call, Throwable t) {

            }
        });
    }
}
