package com.angcyo.demo.qq;

import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by angcyo on 15-12-22-022.
 */
public class QqDemo {
    public static final String URL = "http://www.cx800.com/version.xml";

    public static void demo() {
        Observable.just(URL).observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).build();
                GetVersion version = retrofit.create(GetVersion.class);
                Call<ResponseBody> bodyCall = version.get();
                try {
                    Response<ResponseBody> bodyResponse = bodyCall.execute();
                    String string = bodyResponse.body().string();
                    Log.e("RSen", string + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public interface GetVersion {
        @GET(URL)
        Call<ResponseBody> get();
    }


}


