package com.angcyo.sample.viewdemo;

import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.view.LinkBar;

import java.util.ArrayList;
import java.util.List;

public class LinkBarActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_link_bar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LinkBar linkBar = (LinkBar) mViewHolder.v("linkBar");
        LinkBar linkBar2 = (LinkBar) mViewHolder.v("linkBar2");
        LinkBar linkBar3 = (LinkBar) mViewHolder.v("linkBar3");
        LinkBar linkBar4 = (LinkBar) mViewHolder.v("linkBar4");

        List<String> texts = new ArrayList<>();
        List<String> texts2 = new ArrayList<>();
        texts.add("文");
        texts.add("文本");
        texts.add("文本本");
        texts.add("文本本本");

        linkBar.setTexts(texts);
        linkBar2.setTexts(texts);

        texts2.addAll(texts);
        texts2.add("文本");
        linkBar3.setTexts(texts2);
        linkBar4.setTexts(texts2);
    }

    public void dec(View view) {
        LinkBar linkBar3 = (LinkBar) mViewHolder.v("linkBar3");
        linkBar3.setLinkIndex(linkBar3.getLinkIndex() - 1);
    }

    public void add(View view) {
        LinkBar linkBar3 = (LinkBar) mViewHolder.v("linkBar3");
        int linkIndex = linkBar3.getLinkIndex();
        int index = linkIndex + 1;
        linkBar3.setLinkIndex(index);
    }
}
