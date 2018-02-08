package com.varunjoshi.notesy.activity.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.MainActivity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Notesy
 * Created by Varun Joshi on Thu, {1/2/18}.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private final static AtomicInteger NOTIFICATION_ID = new AtomicInteger(0);

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setLights(Color.BLUE, 500, 500);

        builder.setContentTitle(intent.getStringExtra("note_title"));
        builder.setContentText(intent.getStringExtra("note_description"));
        builder.setSmallIcon(R.drawable.ic_noti_logo);
        builder.setAutoCancel(false);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        notificationCompat.defaults |= Notification.DEFAULT_SOUND;
        notificationCompat.defaults |= Notification.DEFAULT_VIBRATE;
        managerCompat.notify(getID(), notificationCompat);
    }

    public static int getID() {
        return NOTIFICATION_ID.incrementAndGet();
    }
}
