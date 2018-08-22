package com.kzingsdksample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.kzingsdk.core.KzingException;
import com.kzingsdk.core.WebViewHelper;
import com.kzingsdk.requests.EnterGameAPI;
import com.kzingsdk.requests.KzingAPI;

public class WebviewActivity extends Activity {
    private final String TAG = "WebviewActivity";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webview);
        webView = WebViewHelper.gameWebViewSetup(webView);
        KzingAPI.enterGame()
                .setGamePlatform(null)
                .setPlayable(null)
                .addEnterGameCallBack(new EnterGameAPI.EnterGameCallBack() {
                    @Override
                    public void onSuccess(String url) {
                        Log.d(TAG, "enterGame = " + url);
                        webView.loadUrl(url);
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {
                        Log.d(TAG, "enterGame Exception = " + kzingException.toString());
                    }
                })
                .request(this);
    }

}

