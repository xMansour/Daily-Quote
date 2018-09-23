package com.mansourappdevelopment.androidapp.dailyquote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by Mansour on 8/30/2018.
 */

public class SendNotificationAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 1234;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, SendNotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(mIntent);
        } else {
            context.startService(mIntent);
        }
    }
}
