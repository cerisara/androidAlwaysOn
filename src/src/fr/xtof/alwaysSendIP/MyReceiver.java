package fr.xtof.alwaysSendIP;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("xtofalways: got boot intent");
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startForegroundService(serviceIntent);
    }
}

