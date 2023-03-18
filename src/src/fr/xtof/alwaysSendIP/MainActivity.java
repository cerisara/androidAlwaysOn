package fr.xtof.alwaysSendIP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

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
        main = this;
        final String surl = "https://www.france.tv/france-3/direct.html";
        MainActivity.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.main.wv.loadUrl(surl);
            }
        });
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
