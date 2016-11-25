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

    public static void show(View view, ArrayList<String> datas, AdapterView.OnItemClickListener clickListener) {
        Context context = view.getContext();
        popList = new ListPopupWindow(context);
        popList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datas));
        popList.setWidth(LayoutParams.WRAP_CONTENT);
        popList.setHeight(LayoutParams.WRAP_CONTENT);
        popList.setAnchorView(view);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        popList.setModal(true);//设置是否是模式
        popList.setOnItemClickListener(clickListener);
        popList.show();
    }

    public static void hide() {
        if (popList != null) {
            popList.dismiss();
        }
    }
}
