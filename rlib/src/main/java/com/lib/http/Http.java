package com.lib.http;

import android.content.Context;

import com.lib.http.cookie.CookieJarImpl;
import com.lib.http.cookie.store.PersistentCookieStore;
import com.rlib.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/24 11:00
 * 修改人员：Robi
 * 修改时间：2016/10/24 11:00
 * 修改备注：
 * Version: 1.0.0
 */
public class Http {
    private static final String BASE_URL = "http://192.168.1.66:8007/";
    static Retrofit retrofit;

    public static Context context;

    public static void init(Context context) {
        Http.context = context.getApplicationContext();
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(context)))
                .build();
    }

    private static synchronized Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Http.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .client(getClient())
                            .baseUrl(BASE_URL)
                            .addConverterFactory(Factory.getFactory())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }

        return retrofit;
    }

    public static <T> T create(final Class<T> cls) {
        return getRetrofit().create(cls);
    }
}
