package com.mansourappdevelopment.androidapp.dailyquote;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Mansour on 8/30/2018.
 */

public class SendNotificationService extends IntentService {
    public static final int NOTIFICATION_ID = 123;
    JSONArray mJSONArray;
    String line;
    String results = "";
    Random mRandom = new Random();
    int quoteNumber = 0;

    public SendNotificationService() {
        super("SendNotificationService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.e("normal", "Service restarted");
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.quotes)));
        try {
            while ((line = reader.readLine()) != null)
                results += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mJSONArray = new JSONArray(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            quoteNumber = mRandom.nextInt(mJSONArray.length());
            JSONObject mJSONObject = (JSONObject) mJSONArray.get(quoteNumber);
            String quoteAuthor = mJSONObject.getString("quoteAuthor");
            String quoteText = mJSONObject.getString("quoteText");
            showQuote(quoteAuthor, quoteText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showQuote(String quoteAuthor, String quoteText) {
        if (Build.VERSION.SDK_INT <= 19) {
            NotificationCompat.Builder mNotification = new NotificationCompat.Builder(this);
            mNotification.setAutoCancel(true);
            mNotification.setSmallIcon(R.drawable.baseline_format_quote_white_18dp);
            mNotification.setWhen(System.currentTimeMillis());
            mNotification.setContentTitle(quoteAuthor);
            mNotification.setStyle(new NotificationCompat.BigTextStyle().bigText(quoteText));
            mNotification.addAction(R.drawable.baseline_share_white_18dp, "Share",
                    PendingIntent.getActivity(this, 0,
                            Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, "\"" + quoteText + "\" -" + quoteAuthor), "Share via"),
                            PendingIntent.FLAG_UPDATE_CURRENT));
            mNotification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mNotification.setPriority(Notification.PRIORITY_MAX);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification.build());
        } else {
            Notification.Builder mNotification = new Notification.Builder(this);
            mNotification.setAutoCancel(true);
            mNotification.setSmallIcon(R.drawable.baseline_format_quote_white_18dp);
            mNotification.setWhen(System.currentTimeMillis());
            mNotification.setContentTitle(quoteAuthor);
            mNotification.setStyle(new Notification.BigTextStyle().bigText(quoteText));
            mNotification.addAction(R.drawable.baseline_share_white_18dp, "Share",
                    PendingIntent.getActivity(this, 0,
                            Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, "\"" + quoteText + "\" -" + quoteAuthor), "Share via"),
                            PendingIntent.FLAG_UPDATE_CURRENT));
            mNotification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mNotification.setPriority(Notification.PRIORITY_MAX);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification.build());

        }
    }
}
