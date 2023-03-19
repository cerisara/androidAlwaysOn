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
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.lang.Integer;
import android.app.DownloadManager;
import android.net.Uri;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.VideoView;

/*
 * yt-dlp_linux -f hls-237-0 -o fr3_vid.mp4 'https://www.france.tv/france-3/direct.html'
 * yt-dlp_linux -f hls-audio_0-2_Audio_Description-0 -o fr3_aud.mp3 'https://www.france.tv/france-3/direct.html'
 * ffmpeg -i fr3_vid.mp4 -i fr3_aud.mp3 -c:v copy -c:a aac cur.mp4
 * 
 */

public class MainActivity extends Activity {

    private String TAG = "MainActivity";
	public static MainActivity main;
    public Context context;
    File curvid;
    File wd;
    long downloadID;
    VideoView videoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("TOTOESTLA");
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.main);
        main = this;
        context = getApplicationContext();
        videoHolder = (VideoView)findViewById(R.id.videoview);
        wd = getCacheDir();

        // TODO: call downloadAtNight() at 22h
        // TODO: play video every day at 9h

        // debug
        curvid = new File("/data/local/tmp/cur.mp4");
    }
    
    public void playvid(View v) {
        System.out.println("TOTOESTLA playvid "+Integer.toString((int)curvid.length()));

        try {
            MediaPlayer mp = new MediaPlayer();
            // mp.setSurface(videoHolder);
            FileInputStream inputStream = new FileInputStream(curvid);
            mp.setDataSource(inputStream.getFD());
            mp.prepare();
            System.out.println("TOTOESTLA after prep");
            mp.start();
        } catch (Exception e) {
            System.out.println("TOTOESTLA "+e);
            e.printStackTrace();
        }
    }

    void downloadAtNight() {

        // String url = "http://152.81.128.16/cerisara/cur.mp4";
        String url = "http://194.214.124.107/lexres/cur.mp4";
        String fileName = "cur.mp4";
        // debug
        wd = new File("/mnt/sdcard/");
        curvid = new File(wd,fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(curvid))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
            .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);
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
