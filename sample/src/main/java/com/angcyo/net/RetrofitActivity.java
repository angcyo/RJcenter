package com.angcyo.net;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.net.RRetrofit;
import com.rsen.net.service.RApiService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitActivity extends RBaseActivity {


    StringBuilder builder = new StringBuilder();

    @Override
    protected int getContentView() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    private void append(String string) {
        builder.append(string);
        builder.append("\n");
        mViewHolder.tV(R.id.context).setText(builder.toString());
    }

    public void button(View view) {
        RApiService service = RRetrofit.create(RApiService.class);

        /*get请求*/
        Call<RApiService.ResponseBean> api = service.getApi("pa1_value", "ba1_value");
        api.enqueue(new Callback<RApiService.ResponseBean>() {
            @Override
            public void onResponse(Call<RApiService.ResponseBean> call, Response<RApiService.ResponseBean> response) {
                append("getApi-->\n" + response.body().toString());
            }

            @Override
            public void onFailure(Call<RApiService.ResponseBean> call, Throwable t) {
                Log.e("", "");
                append("getApi error-->\n" + t.toString());
            }
        });

        Map<String, String> params = new HashMap<>();
        params.put("bbb", "bbb_v");
        params.put("aaa", "aaa_v");
        Call<ResponseBody> apiString = service.getApiString(params);
        apiString.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("", "");
                try {
                    append("getApiString-->\n" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("", "");
                append("getApiString error-->\n" + t.toString());
            }
        });



        /*post请求*/
        RApiService.RequestBean requestBean = new RApiService.RequestBean();
        requestBean.key1 = "KEY1";
        requestBean.key2 = "KEY2";
        requestBean.key3 = "KEY3";
        requestBean.key4 = "KEY4";
        Call<RApiService.ResponseBean> postApi = service.postApi(requestBean);
        postApi.enqueue(new Callback<RApiService.ResponseBean>() {
            @Override
            public void onResponse(Call<RApiService.ResponseBean> call, Response<RApiService.ResponseBean> response) {
                Log.e("", "");
                append("postApi-->\n" + response.body().toString());
            }

            @Override
            public void onFailure(Call<RApiService.ResponseBean> call, Throwable t) {
                Log.e("", "");
                append("postApi error-->\n" + t.toString());
            }
        });

        Call<ResponseBody> postApiString = service.postApiString(requestBean);
        postApiString.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("", "");
                try {
                    append("postApiString-->\n" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("", "");
                append("postApiString error-->\n" + t.getMessage());
            }
        });

        /*rx的使用*/
        service.getRxApiString(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        append("getRxApiString onCompleted-->\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        append("getRxApiString onError-->\n" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            append("getRxApiString onNext-->\n" + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        service.postRxApiString(requestBean).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RApiService.ResponseBean>() {
                    @Override
                    public void onCompleted() {
                        append("postRxApiString onCompleted-->\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        append("postRxApiString onError-->\n" + e.getMessage());
                    }

                    @Override
                    public void onNext(RApiService.ResponseBean responseBody) {
                        append("postRxApiString onNext-->\n" + responseBody.toString());
                    }
                });
    }
}
