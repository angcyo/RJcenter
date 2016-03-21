package com.rsen.net.service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by angcyo on 16-03-20-020.
 */
public interface RApiService {

    @GET("/api")
    Call<ResponseBean> getApi(@Query("pa1") String pa1, @Query("ba1") String ba1);

    @POST("/api")
    Call<ResponseBean> postApi(@Body RequestBean requestBean);

    @GET("/api")
    Call<ResponseBody> getApiString(@QueryMap Map<String, String> params);

    @POST("/api")
    Call<ResponseBody> postApiString(@Body RequestBean requestBean);


    class RequestBean {
        public String key1;
        public String key2;
        public String key3;
        public String key4;
    }

    class ResponseBean {

        /**
         * name_char : 马化腾
         * name_float : 0
         * name_int : 0
         * name_time : 1478833860000
         * name_tinyint : 0
         * name_varchar : 埃里巴巴
         * rid : 12
         */

        private String name_char;
        private int name_float;
        private int name_int;
        private long name_time;
        private int name_tinyint;
        private String name_varchar;
        private int rid;

        public String getName_char() {
            return name_char;
        }

        public void setName_char(String name_char) {
            this.name_char = name_char;
        }

        public int getName_float() {
            return name_float;
        }

        public void setName_float(int name_float) {
            this.name_float = name_float;
        }

        public int getName_int() {
            return name_int;
        }

        public void setName_int(int name_int) {
            this.name_int = name_int;
        }

        public long getName_time() {
            return name_time;
        }

        public void setName_time(long name_time) {
            this.name_time = name_time;
        }

        public int getName_tinyint() {
            return name_tinyint;
        }

        public void setName_tinyint(int name_tinyint) {
            this.name_tinyint = name_tinyint;
        }

        public String getName_varchar() {
            return name_varchar;
        }

        public void setName_varchar(String name_varchar) {
            this.name_varchar = name_varchar;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }
    }
}
