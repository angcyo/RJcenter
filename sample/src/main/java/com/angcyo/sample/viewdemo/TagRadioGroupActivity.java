package com.angcyo.sample.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;

import com.angcyo.sample.R;
import com.rsen.util.T;
import com.rsen.view.viewgroup.TagRadioGroup;

public class TagRadioGroupActivity extends AppCompatActivity {
    TagRadioGroup tagGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_radio_group);

        tagGroup = (TagRadioGroup) findViewById(R.id.tag_group);

        tagGroup.setChildCount(6);
        tagGroup.setBorderWidth(3f);
        tagGroup.setChildTexts(new String[]{"消息", "新闻", "交友", "未读", "提醒"});

        tagGroup.setListener(new TagRadioGroup.OnCheckListener() {
            @Override
            public void onCheck(TagRadioGroup radioGroup, RadioButton button, int index, String text) {
                T.show(TagRadioGroupActivity.this, text + "--" + index + "---" + button.getId());
            }
        });
    }
}
