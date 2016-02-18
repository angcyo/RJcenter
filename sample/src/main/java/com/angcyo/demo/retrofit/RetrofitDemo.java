package com.angcyo.demo.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;


/**
 * Created by angcyo on 15-12-18 018 12:31 下午.
 */
public class RetrofitDemo {

    public static final String URL = "http://www.baidu.com/";

    public static final String TAG = RetrofitDemo.class.getSimpleName();

    public static void demo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getBaidu();

            }
        }.start();
    }

    public static String getBaidu() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).build();
        GetBaidu getBaidu = retrofit.create(GetBaidu.class);
        Call<ResponseBody> call = getBaidu.get();

        try {
            Response<ResponseBody> bodyResponse = call.execute();
            String body = bodyResponse.body().string();
            Log.e(TAG, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        call.enqueue(new Callback<ResponseBody>() {//异步
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                try {
//                    String body = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.e(TAG, "");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e(TAG, "");
//            }
//        });

        return "";
    }

    public interface GetBaidu {
        @GET(URL)
        Call<ResponseBody> get();
    }
}
