package com.varunjoshi.notesy.activity.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.varunjoshi.notesy.BuildConfig;
import com.varunjoshi.notesy.activity.Service.AlarmReceiver;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Notesy
 * Created by Varun Joshi on Thu, {1/2/18}.
 */

public class Util {
    private static final String TAG = "Util";
    private final static AtomicInteger ALARM_ID = new AtomicInteger(0);
    public static String ConvertDateIntoSimpleFormat(String dateTime) {
        String result = "";

        String date = dateTime;//"2013-09-06'T'14:15:11.557'Z'";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        df = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");

        result = df.format(d);

        return result;
    }

    public static Uri getMediaOutputUri(Context context) {
        // check for external storage
        if (isExternalStorageAvailable()) {
            // get URI
            // 1. get external directory
            File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // 2. create file name
            String fileName = "";
            String fileType = "";
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fileName = "IMG_" + timestamp;
            fileType = ".jpg";
            // 3. create the file
            File mediaFile = null;
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "getMediaOutputUri: " + Uri.fromFile(mediaFile));
                }
                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 4. return file's URI
            return null;
        }
        // something went wrong
        return null;
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static int getAlarmId() {
        return ALARM_ID.incrementAndGet();
    }

    public static boolean setAlarm(Context context, long reminderTimestamp, int alarmId,
                             String note_title, String note_description) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        if (note_title.length() == 0) {
            note_title = "";
        }
        if (note_description.length() == 0) {
            note_description="";
        }
        if (note_title.length() > 0)
            intent.putExtra("note_title", note_title);
        else
            intent.putExtra("note_title", "");
        intent.putExtra("note_description", note_description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarmId, intent, 0);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
        }

        return true;

    }
}
