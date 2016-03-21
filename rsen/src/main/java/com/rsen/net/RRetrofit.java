package com.rsen.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by angcyo on 2016-03-20 23:53.
 */
public class RRetrofit {
    public static String BASE_URL = "http://192.168.1.12:8082";

    public static <T> T create(final Class<T> cls) {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(cls);
    }
}
