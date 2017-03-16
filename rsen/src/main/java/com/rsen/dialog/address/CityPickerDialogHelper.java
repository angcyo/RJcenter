package com.rsen.dialog.address;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/19 10:57
 * 修改人员：Robi
 * 修改时间：2016/10/19 10:57
 * 修改备注：
 * Version: 1.0.0
 */
public class CityPickerDialogHelper {

    private static ArrayList<Province> provinces = new ArrayList<Province>();
    private static boolean isRun = false;

    public static void show(Activity activity, OnCityPickerListener listener) {
        if (!provinces.isEmpty()) {
            showAddressDialog(activity, listener);
        }
    }

    public static void init(Activity activity) {
        if (provinces.isEmpty()) {
            if (!isRun) {
                new InitAreaTask(activity).execute(0);
            }
        }
    }

    /**
     * 显示地址dialog
     */
    private static void showAddressDialog(Activity activity, OnCityPickerListener listener) {
        new CityPickerDialog(activity, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {
                    @Override
                    public void onPicked(Province selectProvince, City selectCity, County selectCounty) {
                        /*省*/
                        String province = selectProvince != null ? selectProvince.getRegion_name() : "";
                        /*市*/
                        String city = selectCity != null ? selectCity.getRegion_name() : "";
                        /*县*/
                        String county = selectCounty != null ? selectCounty.getRegion_name() : "";

                        if (listener != null) {
                            listener.onCityPicker(province, city, county);
                        }
                    }
                }).show();
    }

    public interface OnCityPickerListener {
        void onCityPicker(String province, String city, String county);
    }

    private static class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {

        Activity mContext;

        public InitAreaTask(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (provinces.size() > 0) {
//                showAddressDialog(mContext);
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }

            isRun = false;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            isRun = true;
            String address = "";
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("city_list.json");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                try {
                    address = new String(arrayOfByte, 0, arrayOfByte.length, "UTF-8");
                } catch (final UnsupportedEncodingException e) {
                    address = new String(arrayOfByte, 0, arrayOfByte.length);
                }
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),
                                Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }
    }
}
