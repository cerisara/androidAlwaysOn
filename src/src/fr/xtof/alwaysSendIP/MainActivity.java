package fr.xtof.alwaysSendIP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Integer;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";
	public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("TOTOESTLA");
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.main);
        main = this;
        File wd = getCacheDir();

        try {
            InputStream ins = this.getResources().getAssets().open("yt-dlp");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            File ytf = new File(wd,"yt-dlp");
            FileOutputStream fos = new FileOutputStream(ytf);
            fos.write(buffer);
            fos.close();
            ytf.setExecutable(true);
            String yt = ytf.getPath();
            System.out.println("TOTOESTLAa check "+Integer.toString((int)ytf.length()));

            System.out.println("TOTOESTLAa call yt");
            Process pvid = Runtime.getRuntime().exec(yt+" -f hls-237-0 -o fr3_vid.mp4 'https://www.france.tv/france-3/direct.html'");
            Process paud = Runtime.getRuntime().exec(yt+" -f hls-audio_0-2_Audio_Description-0 -o fr3_aud.mp4 'https://www.france.tv/france-3/direct.html'");
            Thread.sleep(3);
            System.out.println("TOTOESTLAa killall "+Integer.toString((int)ytf.length()));
            pvid.destroy();
            paud.destroy();

        } catch (Exception e) {
            System.out.println("TOTOESTLAA err");
            e.printStackTrace();
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
