package com.mansourappdevelopment.androidapp.dailyquote;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.media.RingtoneManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.NotificationCompat;
//import android.app.Notification;
//import android.app.NotificationManager;
//import static android.content.Context.MODE_WORLD_READABLE;
//import static android.content.Context.NOTIFICATION_SERVICE;

import java.util.Calendar;


/**
 * Created by Mansour on 9/1/2018.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    //public static final int NOTIFICATION_ID = 123;
    int mHour;
    int mMinute;
    AlarmManager mAlarmManager;
    Intent mIntent;
    PendingIntent mPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            //Log.e("normal", "true");
            SharedPreferences mSharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
            mHour = mSharedPreferences.getInt("hour", 0);
            mMinute = mSharedPreferences.getInt("minute", 0);
            mIntent = new Intent(context, SendNotificationAlarmReceiver.class);
            mPendingIntent = PendingIntent.getBroadcast(context, SendNotificationAlarmReceiver.REQUEST_CODE,
                    mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, mPendingIntent);
            //showQuote(context);
        }
    }

    /*public void showQuote(Context context) {
        Intent mIntent = new Intent(context, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT <= 19) {
            NotificationCompat.Builder mNotification = new NotificationCompat.Builder(context);
            mNotification.setAutoCancel(true);
            mNotification.setSmallIcon(R.drawable.baseline_format_quote_white_18dp);
            mNotification.setWhen(System.currentTimeMillis());
            mNotification.setContentTitle("Daily Quote Notification");
            mNotification.setContentText("Restart the service!");
            mNotification.setContentIntent(mPendingIntent);
            mNotification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification.build());
        } else {
            Notification.Builder mNotification = new Notification.Builder(context);
            mNotification.setAutoCancel(true);
            mNotification.setSmallIcon(R.drawable.baseline_format_quote_white_18dp);
            mNotification.setWhen(System.currentTimeMillis());
            mNotification.setContentTitle("Daily Quote Notification");
            mNotification.setContentText("Restart the service!");
            mNotification.setContentIntent(mPendingIntent);
            mNotification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification.build());
        }
    }*/
}
