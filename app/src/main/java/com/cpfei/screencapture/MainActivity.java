package com.cpfei.screencapture;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    WebView wv_imgweb;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_main);

        wv_imgweb = (WebView) findViewById(R.id.h5_wv_imgweb);


        WebSettings webSettings = wv_imgweb.getSettings();
//此处可更加具体的H5界面功能进行相应的WebSettings设置，本文只是演示基本效果
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        wv_imgweb.requestFocusFromTouch();
        wv_imgweb.setDrawingCacheEnabled(true);
        wv_imgweb.setVerticalScrollBarEnabled(false);
        wv_imgweb.setHorizontalScrollBarEnabled(false);
        wv_imgweb.setVerticalScrollbarOverlay(false);
        wv_imgweb.setHorizontalScrollbarOverlay(false);


        wv_imgweb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载逻辑的处理
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //加载逻辑的处理
            }
        });

        //添加用户信息参数，加载H5分享地址
        wv_imgweb.loadUrl("https://hs.xueersi.com/highSimulationExam/#/appTicket");

        image = findViewById(R.id.image);

        findViewById(R.id.screenBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenShotUtils.screenWeb(wv_imgweb);
            }
        });


        ContentObserverUtils contentObserverUtils = new ContentObserverUtils(this, new MediaContentObserver.OnScreenShotListener() {
            @Override
            public void onScreenShot(String path) {

                Log.d("Tag", Thread.currentThread().getName());

            }
        });
        contentObserverUtils.register();


    }


}
