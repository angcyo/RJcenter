package com.rsen.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by angcyo on 2016-03-20 23:53.
 */
public class RRetrofit {
    public static String BASE_URL = "http://192.168.1.12:8082";

    public static <T> T create(final Class<T> cls) {
        Converter.Factory factory = getFactory();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        if (factory != null) {
            builder.addConverterFactory(factory);
        }

        Retrofit retrofit = builder.build();
        return retrofit.create(cls);
    }

    public static Converter.Factory getFactory() {
        Converter.Factory gsonFactory = checkFactory("retrofit2.converter.gson.GsonConverterFactory");
        Converter.Factory jacksonFactory = checkFactory("retrofit2.converter.jackson.JacksonConverterFactory");

        //优先使用jackson
        if (jacksonFactory != null) {
            return jacksonFactory;
        }
        return gsonFactory;
    }

    public static Converter.Factory checkFactory(String className) {
        Converter.Factory factory = null;
        try {
            final Class<?> factoryName = Class.forName(className);
            final Method create = factoryName.getMethod("create");
            factory = (Converter.Factory) create.invoke(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return factory;
    }
}
