package fr.xtof.alwaysSendIP;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.graphics.Color;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.app.NotificationManager;
import android.app.NotificationChannel;

public class MyService extends Service {
    private String TAG = "MyService_AlwaysSendIP";
    public static boolean isServiceRunning;
    // private ScreenLockReceiver screenLockReceiver;
    private Timer timer;

    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
        // screenLockReceiver = new ScreenLockReceiver();
        timer = new Timer();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        isServiceRunning = true;

        // register receiver to listen for screen on events
        // IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        // filter.addAction(Intent.ACTION_USER_PRESENT);
        // filter.addAction(Intent.ACTION_SCREEN_OFF);
        // registerReceiver(screenLockReceiver, filter);

        /*
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run called inside scheduleAtFixedRate");
            }
        }, 0, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS);
        */
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "xtofchannel";
        String name = "xtofchanelname";
        String description = "xtof channelapp";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name,importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(false);
        // mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Service is Running")
                .setContentText("Listening for Xtof")
                .setSmallIcon(R.drawable.dlicon)
                .setContentIntent(pendingIntent)
                .setChannelId(id)
                .setColor(Color.RED)
                .build();
        /*
         * A started service can use the startForeground API to put the service in a foreground state,
         * where the system considers it to be something the user is actively aware of and thus not
         * a candidate for killing when low on memory.
         */
        startForeground(1, notification);

        // does not work as expected though, even START_NOT_STICKY gives same result
        // device specific issue?
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;
        stopForeground(true);

        // unregister receiver
        // unregisterReceiver(screenLockReceiver);

        // cancel the timer
        if (timer != null) {
            timer.cancel();
        }

        // call MyReceiver which will restart this service via a worker
        // Intent broadcastIntent = new Intent(this, MyReceiver.class);
        // sendBroadcast(broadcastIntent);

        super.onDestroy();
    }

    // Not getting called on Xiaomi Redmi Note 7S even when autostart permission is granted
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved called");
        super.onTaskRemoved(rootIntent);
    }
}
