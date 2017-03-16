package com.angcyo.demo.webview;

import android.os.Bundle;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.rsen.base.RBaseActivity;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/20 18:12
 * 修改人员：Robi
 * 修改时间：2016/10/20 18:12
 * 修改备注：
 * Version: 1.0.0
 */
//此项目为跑通...
class WebViewTest extends RBaseActivity {
    public static String KEY_ID = "key_id";
    WebView webView;
    WebView webview;
    FrameLayout container;
    private int id = 1;//文章的id
//
//    @Override
//    protected void initView() {
//        id = Integer.valueOf(getIntent().getStringExtra(KEY_ID));
////        setContentView(R.layout.activity_content);
////        ButterKnife.bind(this);
////        webView = (WebView) findViewById(R.id.webview);
//        initWebView(webView);
////        initWindow(R.color.action_bar_bg);
//
//        SlidrConfig config = new SlidrConfig.Builder().edge(true).build();
//        Slidr.attach(this, config);
//    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initAfter() {
        webView.loadUrl(getContentUrl());
        webView.zoomOut();
    }

    private WebView initWebView(WebView webView) {
//        WebView webView = new WebView(this);
        final WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("gbk");
        settings.setJavaScriptEnabled(true);//注意此处
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setBuiltInZoomControls(true);//支持缩放手势
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);//?
        settings.setDisplayZoomControls(false);//不显示缩放控件
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //缩放网页,以便显示整个网页
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
//        webView.setInitialScale(1);

        settings.setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; zh-tw) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16");

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                if (progress >= 100)
                    setTitle("用户:");
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setDefaultTextEncodingName("utf8");
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                setTitle("用户:" + OaApplication.getUserInfo().tname);
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Logger.e(url + "  " + mimetype + "\n" + contentDisposition);
//                String fileName = "未知文件名";
//                if (!Util.isEmpty(contentDisposition) && contentDisposition.contains("filename")) {
//                    fileName = contentDisposition.substring(contentDisposition.indexOf("=") + 1);
//                }
//                BaseFragment fragment = new SaveFileFragment();
//                Bundle args = new Bundle();
//                args.putString(SaveFileFragment.KEY_URL, url);
//                args.putString(SaveFileFragment.KEY_FILE_NAME, fileName);
//                args.putString(SaveFileFragment.KEY_FILE_MIME_TYPE, mimetype);
//                args.putString(SaveFileFragment.KEY_FILE_SIZE, Formatter.formatFileSize(ContentActivity.this, contentLength));
//                fragment.setArguments(args);
//                ContentActivity.this.add(fragment);
            }
        });
        return webView;
    }

    private String getContentUrl() {
//        String url = String.format("http://%s/APP/TongZhiview.asp?APPID=%s&id=%d", RConstant.SER_IP, OaApplication.getUserInfo().appid, id);
//        return url;
        return "";
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_content, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
////        if (id == R.id.quit) {
//            onBackPressed();
//            return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }
}
