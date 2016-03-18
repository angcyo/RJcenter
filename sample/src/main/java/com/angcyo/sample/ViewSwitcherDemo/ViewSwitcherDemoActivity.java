package com.angcyo.sample.ViewSwitcherDemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.angcyo.sample.R;

public class ViewSwitcherDemoActivity extends AppCompatActivity {

    TextSwitcher textSwitcher;
    ImageSwitcher imageSwitcher;
    TextSwitcher textSwitcher2;
    ImageSwitcher imageSwitcher2;

    int index = 3;
    int index2 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_switcher_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSwitcher.showNext();
                imageSwitcher.showNext();

                textSwitcher2.showNext();
                imageSwitcher2.showNext();

                ((TextView) textSwitcher.getNextView()).setText("I'm Text " + index++);
                ((TextView) textSwitcher2.getNextView()).setText("I'm Text " + index2++);
            }
        });

        initView();
    }

    private void initView() {
        textSwitcher = (TextSwitcher) findViewById(R.id.text_switcher);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);

        textSwitcher.setInAnimation(this, com.angcyo.rsen.R.anim.rotate_0);
        textSwitcher.setOutAnimation(this, com.angcyo.rsen.R.anim.rotate_1);

        imageSwitcher.setInAnimation(this, com.angcyo.rsen.R.anim.rotate_0);
        imageSwitcher.setOutAnimation(this, com.angcyo.rsen.R.anim.rotate_1);

        /*  */
        textSwitcher2 = (TextSwitcher) findViewById(R.id.text_switcher2);
        imageSwitcher2 = (ImageSwitcher) findViewById(R.id.image_switcher2);

        textSwitcher2.setInAnimation(this, com.angcyo.rsen.R.anim.tran_ttot);
        textSwitcher2.setOutAnimation(this, com.angcyo.rsen.R.anim.tran_btob);

        imageSwitcher2.setInAnimation(this, com.angcyo.rsen.R.anim.tran_ttot);
        imageSwitcher2.setOutAnimation(this, com.angcyo.rsen.R.anim.tran_btob);
    }

}
