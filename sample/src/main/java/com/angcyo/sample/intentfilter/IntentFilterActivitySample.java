package com.angcyo.sample.intentfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.angcyo.sample.R;

import java.util.Set;

public class IntentFilterActivitySample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_filter_activity_sample);


        Intent intent = getIntent();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("action:");
        stringBuilder.append(intent.getAction());
        stringBuilder.append("\n");
        Set<String> categories = intent.getCategories();
        if (categories != null) {
            stringBuilder.append("category:");
            Object[] categorys = categories.toArray();
            for (int i = 0; i < categorys.length; i++) {
                stringBuilder.append(categorys[i]);
                stringBuilder.append("\n");
            }

//            for (; categories.iterator().hasNext(); ) {
//                stringBuilder.append(categories.iterator().next());
//                stringBuilder.append("\n");
//            }
        }
        stringBuilder.append("data:");
        stringBuilder.append(intent.getDataString());
        stringBuilder.append("\n");
        stringBuilder.append("type:");
        stringBuilder.append(intent.getType());
        stringBuilder.append("\n");
        ((TextView) findViewById(R.id.text_sample)).setText(stringBuilder.toString());
    }
}
