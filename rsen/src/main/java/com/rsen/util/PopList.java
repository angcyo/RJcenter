package com.rsen.util;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/25 10:03
 * 修改人员：Robi
 * 修改时间：2016/11/25 10:03
 * 修改备注：
 * Version: 1.0.0
 */
public class PopList {

    private static ListPopupWindow popList;
    private static ArrayList<String> allDatas = new ArrayList<>();
    private static ArrayAdapter<String> sArrayAdapter;

    public static void show(View view, ArrayList<String> datas, AdapterView.OnItemClickListener clickListener) {
        allDatas.clear();
        allDatas.addAll(datas);
        if (allDatas.size() == 0) {
            hide();
            return;
        }
        if (popList == null) {
            Context context = view.getContext();
            popList = new ListPopupWindow(context);
            sArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allDatas);
            popList.setAdapter(sArrayAdapter);
            popList.setWidth(LayoutParams.WRAP_CONTENT);
            popList.setHeight(LayoutParams.WRAP_CONTENT);
            popList.setAnchorView(view);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
            //popList.setModal(true);//设置是否是模式, 设置此模式会拦截焦点和输入事件
            popList.setOnItemClickListener(clickListener);
            popList.show();
        } else {
            sArrayAdapter.notifyDataSetChanged();
            if (!popList.isShowing()) {
                popList.show();
            }
        }
    }

    public static void hide() {
        if (popList != null) {
            popList.dismiss();
            allDatas.clear();
            popList = null;
        }
    }
}
