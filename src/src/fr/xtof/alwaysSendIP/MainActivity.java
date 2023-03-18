package fr.xtof.alwaysSendIP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";
    WebView wv;
	public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("TOTOESTLA");
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.main);
		wv = (WebView)findViewById(R.id.web1);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.81 Safari/537.36");
        WebViewClientImpl webViewClient = new WebViewClientImpl();
        wv.setWebViewClient(webViewClient);
        main = this;
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true);
        //final String surl = "https://www.france.tv/connexion/";
        final String surl = "https://www.france.tv/france-3/direct.html";
        MainActivity.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.main.wv.loadUrl(surl);
            }
        });
    }

    private class WebViewClientImpl extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }


    public void onStartServiceClick(View v) {
        startService();
    }

    public void onStopServiceClick(View v) {
        stopService();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        stopService();
        super.onDestroy();
    }

    public void startService() {
        Log.d(TAG, "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            Context context = getApplicationContext();
            context.startForegroundService(serviceIntent);
            //startService(serviceIntent); // for API<26
        }
    }

    public void stopService() {
        Log.d(TAG, "stopService called");
        if (MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            stopService(serviceIntent);
        }
    }

}
