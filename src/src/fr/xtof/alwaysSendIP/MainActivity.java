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
import java.lang.Integer;
import android.app.DownloadManager;
import android.net.Uri;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.PixelFormat;

/*
 * yt-dlp_linux -f hls-237-0 -o fr3_vid.mp4 'https://www.france.tv/france-3/direct.html'
 * yt-dlp_linux -f hls-audio_0-2_Audio_Description-0 -o fr3_aud.mp3 'https://www.france.tv/france-3/direct.html'
 * ffmpeg -i fr3_vid.mp4 -i fr3_aud.mp3 -c:v copy -c:a aac cur.mp4
 * 
 */

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    private String TAG = "MainActivity";
	public static MainActivity main;
    public Context context;
    File curvid;
    MediaPlayer mediaPlayer = null;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    File wd;
    long downloadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("TOTOESTLA");
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.main);
        main = this;
        context = getApplicationContext();
        wd = getCacheDir();

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();


        // TODO: call downloadAtNight() at 22h
        // TODO: play video every day at 9h

        // debug
        curvid = new File("/mnt/sdcard/cur.mp4");
    }
    
    public void playvid(View v) {
        System.out.println("TOTOESTLA playvid");
        if (mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(context, Uri.fromFile(curvid));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            System.out.println("TOTOESTLA "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    void downloadAtNight() {
        mediaPlayer.release();
        mediaPlayer = null;

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
