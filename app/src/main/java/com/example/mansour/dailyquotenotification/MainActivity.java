package com.example.mansour.dailyquotenotification;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static final int TIME_CONDITION = 8888;
    Button btnSetTime;
    TextView mTextViewChoosenTime;
    TextView mTextViewStartQuote;
    FragmentTransaction mFragmentManager;
    AlarmManager mAlarmManager;
    Intent mIntent;
    PendingIntent mPendingIntent;
    Animation mButtonAnimation;
    Animation mTextAnimation;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mSharedPreferencesEditor;
    int mHour = TIME_CONDITION;
    int mMinute = TIME_CONDITION;
    String mHourConvention;
    boolean mSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mTextAnimation = AnimationUtils.loadAnimation(this, R.anim.text_transition);
        mTextAnimation.setDuration(1000);
        mButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_bottom_to_up);
        mButtonAnimation.setDuration(800);
        mTextViewChoosenTime = (TextView) findViewById(R.id.textViewTime);
        mTextViewChoosenTime.setAnimation(mTextAnimation);
        mTextViewStartQuote = (TextView) findViewById(R.id.textViewStartQuote);
        mTextViewStartQuote.setAnimation(mTextAnimation);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSetTime.setAnimation(mButtonAnimation);
        btnSetTime.setAnimation(mButtonAnimation);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSet) {
                    cancelAlarm();
                    isSet(mSet);
                } else {
                    mFragmentManager = getSupportFragmentManager().beginTransaction();
                    TimePickerFragment mTimePickerFragment = new TimePickerFragment();
                    mTimePickerFragment.show(mFragmentManager, null);
                }
            }
        });

        mHour = mSharedPreferences.getInt("hour", 0);
        mMinute = mSharedPreferences.getInt("minute", 0);
        mSet = mSharedPreferences.getBoolean("set", false);
        Log.e("normal", String.valueOf(mHour));
        Log.e("normal",String.valueOf(mMinute));
        Log.e("normal",String.valueOf(mSet));
        isSet(mSet);

    }


    public void setTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPendingIntent);
        }
        mSet = true;
        mSharedPreferencesEditor.putInt("hour", mHour);
        mSharedPreferencesEditor.putInt("minute", mMinute);
        mSharedPreferencesEditor.putBoolean("set", mSet);
        mSharedPreferencesEditor.apply();
        isSet(mSet);
        checkTime(mHour);
        scheduleNotificationAlarm();
    }

    public void scheduleNotificationAlarm() {
        mIntent = new Intent(this, SendNotificationAlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(this, SendNotificationAlarmReceiver.REQUEST_CODE,
                mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);
        //to prevent immediate starting if the time choose is less than the current time, increment the current day by 1.
        if((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > mHour)){
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        Log.e("normal","Current Hour" + String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        Log.e("normal", String.valueOf(mCalendar.getTime()));
        mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, mPendingIntent);
    }


    public void cancelAlarm() {
        mSet = false;
        mHour = TIME_CONDITION;
        mMinute = TIME_CONDITION;
        mSharedPreferencesEditor.putInt("hour", mHour);
        mSharedPreferencesEditor.putInt("minute", mMinute);
        mSharedPreferencesEditor.putBoolean("set", mSet);
        mSharedPreferencesEditor.apply();
        mIntent = new Intent(getApplicationContext(), SendNotificationAlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(this, SendNotificationAlarmReceiver.REQUEST_CODE,
                mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(mPendingIntent);
    }

    public String checkTime(int hour) {
        if (hour < 12) {
            mHourConvention = "AM";
        } else {
            mHourConvention = "PM";
        }
        return mHourConvention;
    }

    public void isSet(boolean set) {
        if (set) {
            btnSetTime.setText("Cancel Service");
            if (Build.VERSION.SDK_INT < 21) {
                btnSetTime.setBackground(ContextCompat.getDrawable(this, R.drawable.button_stop));
            } else {
                btnSetTime.setBackground(getDrawable(R.drawable.button_stop));
            }
            if(mHour == 12 || mHour == 0){
                mTextViewChoosenTime.setText("Time is set to 12 : " + String.valueOf(mMinute) + " " + checkTime(mHour));
            }else {
                mTextViewChoosenTime.setText("Time is set to " + String.valueOf(mHour % 12) + " : " + String.valueOf(mMinute) + " " + checkTime(mHour));
            }
        } else {
            mTextViewChoosenTime.setText("Time isn't set yet!");
            btnSetTime.setText("Start Service");
            if (Build.VERSION.SDK_INT < 21) {
                btnSetTime.setBackground(ContextCompat.getDrawable(this, R.drawable.button_start));
            } else {
                btnSetTime.setBackground(getDrawable(R.drawable.button_start));
            }
        }

    }
}