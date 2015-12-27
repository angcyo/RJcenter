package com.angcyo.demo.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by angcyo on 15-12-27-027.
 */
public class WebViewDemo extends AppCompatActivity {
    static final String URL = "http://kefu.easemob.com/webim/im.html?tenantId=10488";
    ValueCallback<Uri> uploadFile; //保存一个这样的成员变量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebView(webView);
        setContentView(webView);

        webView.loadUrl(URL);
    }

    private void initWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new RsenWebChromeClient() {
            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile) {
                WebViewDemo.this.uploadFile = uploadFile;
                getPhotoFromPhotos();
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                openFileChooser(uploadFile);
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                openFileChooser(uploadFile);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (uploadFile != null) {
            if (data == null) {
                uploadFile.onReceiveValue(null);
            } else {
                uploadFile.onReceiveValue(data.getData());
            }
            uploadFile = null;
        }
    }

    public void getPhotoFromPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "选择图片"), 100);
        startActivityForResult(intent, 100);
    }

    public abstract class RsenWebChromeClient extends WebChromeClient {
        /**
         * 兼容3.0以前的版本
         */
        public abstract void openFileChooser(ValueCallback<Uri> uploadFile);

        /**
         * 兼容4.0以前的版本
         */
        public abstract void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType);

        /**
         * 兼容4.0以后的版本
         */
        public abstract void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture);
    }
}
