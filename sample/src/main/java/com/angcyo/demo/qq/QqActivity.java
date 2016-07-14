package com.angcyo.demo.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.angcyo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 15-12-23 023 10:54 上午.
 */
public class QqActivity extends AppCompatActivity {

    private XCFlowLayout xcf_layout;

    private XCFlowLayout xcf_layout2;

    private List<String> list = null;

    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        xcf_layout = (XCFlowLayout) findViewById(R.id.flowlayout);
        xcf_layout2 = (XCFlowLayout) findViewById(R.id.flowlayout2);


        getList();
        load_popularity();//显示不全
        load_popularity2();//显示全，
        //如何拯救

        // tagView.setTags(list);
    }

    /**
     * 加载印象标签
     */
    private void load_popularity() { //有 ScrollView
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        xcf_layout.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(list.get(i).toString());
            view.setTextSize(12);
//            view.setTextColor(getResources().getColor(R.color.comment_line));
            view.setPadding(10, 5, 10, 5);
//            view.setBackgroundResource(R.drawable.fillet_hui_btn);
            xcf_layout.addView(view, lp);
        }
    }


    private void load_popularity2() { //没有ScrollView
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        xcf_layout2.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(list.get(i).toString());
            view.setTextSize(12);
//            view.setTextColor(getResources().getColor(R.color.comment_line));
            view.setPadding(10, 5, 10, 5);
//            view.setBackgroundResource(R.drawable.fillet_hui_btn);
            xcf_layout2.addView(view, lp);
        }
    }


    private void getList() {
        list = new ArrayList<String>();
        list.add("帅气");
        list.add("英俊潇洒");
        list.add("幽默感");
        list.add("开朗");
        list.add("有点逗");
        list.add("帅");
        list.add("王老五");
        list.add("颜值爆表");
        list.add("高富帅");
        list.add("人气旺");
        list.add("瓜娃子");
        list.add("人才");
        list.add("人才1");
        list.add("人才2");
        list.add("人才3");
        list.add("人才4");
        list.add("人才5");
        list.add("王老五");
        list.add("颜值爆表");
        list.add("高富帅");
        list.add("人气旺");
        list.add("瓜娃子");
        list.add("人才");
    }
}
