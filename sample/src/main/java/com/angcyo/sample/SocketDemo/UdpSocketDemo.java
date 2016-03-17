package com.angcyo.sample.SocketDemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.angcyo.sample.R;

import java.net.SocketException;

public class UdpSocketDemo extends Activity {

    AppCompatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_socket_demo);

        editText = (AppCompatEditText) findViewById(R.id.editText);
    }

    public void send(View view) {
        try {
            UdpSendThread.sendData(getData());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send2(View view) {

    }

    private byte[] getData() {
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            s = "--";
        }
        return s.getBytes();
    }
}
