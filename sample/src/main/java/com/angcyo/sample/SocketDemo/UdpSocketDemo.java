package com.angcyo.sample.SocketDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.angcyo.sample.R;

import java.net.SocketException;

public class UdpSocketDemo extends AppCompatActivity {

    AppCompatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_socket_demo);

        editText = (AppCompatEditText) findViewById(R.id.editText);
    }

    public void sendButton(View view) {
        try {
            UdpSendThread.sendData(getData());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendSecond(View view) {
        for (int i = 0; i < 100; i++) {
            try {

                UdpSendThread.sendData(getData("__" + i));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getData(String string) {
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            s = "--";
        }
        s += string;
        return s.getBytes();
    }

    private byte[] getData() {
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            s = "--";
        }
        return s.getBytes();
    }
}
