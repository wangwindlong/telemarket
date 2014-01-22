package com.example.telemarket.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 下午12:04
 * To change this template use File | Settings | File Templates.
 */
public class NewApplyActivity extends BaseActivity {
    ImageView emptyIv;
    ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.market_tool);

        TextView backTv = (TextView)findViewById(R.id.tool_back_tv);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        emptyIv = (ImageView) findViewById(R.id.empty_iv);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        WebView contentWv = (WebView) findViewById(R.id.market_tool_wv);
        WebSettings ws = contentWv.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setJavaScriptEnabled(true);
        contentWv.requestFocus();
        contentWv.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        contentWv.setWebViewClient(new PicWebViewClient());
        contentWv.loadUrl(AppConfig.TOOL_URL);
    }

    private class PicWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            progressBar.setVisibility(View.GONE);
            emptyIv.setVisibility(View.GONE);
        }
    }

}