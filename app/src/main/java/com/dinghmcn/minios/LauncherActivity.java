package com.dinghmcn.minios;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class LauncherActivity extends Activity {
    private final String TAG = "LauncherActivity";

    private ImageView mTimeHour0;
    private ImageView mTimeHour1;
    private TextView mTime_12_24;
    private ImageView mTimeMinute0;
    private ImageView mTimeMinute1;
    private TextView mWeek;
    private TextView mDate;
    private boolean lockLongPress = false;
    private boolean is24Hour = true;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    private ContentObserver mFormatChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            is24Hour = "24".equals(Settings.System.getString(getContentResolver(), Settings.System.TIME_12_24));
            updateView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mTimeHour0 = (ImageView) findViewById(R.id.time_hour_0);
        mTimeHour1 = (ImageView) findViewById(R.id.time_hour_1);
        mTime_12_24 = (TextView) findViewById(R.id.time_12_24);
        mTimeMinute0 = (ImageView) findViewById(R.id.time_minute_0);
        mTimeMinute1 = (ImageView) findViewById(R.id.time_minute_1);
        mWeek = (TextView) findViewById(R.id.textWeek);
        mDate = (TextView) findViewById(R.id.textDate);
        is24Hour = "24".equals(Settings.System.getString(getContentResolver(), Settings.System.TIME_12_24));
        updateView();
        init();
    }

    private void init() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);

        getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.DATE_FORMAT), true, mFormatChangeObserver);
        getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.TIME_12_24), true, mFormatChangeObserver);
    }

    private void updateView(){
        int hour;
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getDefault());
        if (is24Hour) {
            hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            mTime_12_24.setVisibility(View.INVISIBLE);
        } else {
            hour = mCalendar.get(Calendar.HOUR);
            mTime_12_24.setText((mCalendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM");
            mTime_12_24.setVisibility(View.VISIBLE);
        }
        setTime(hour, mCalendar.get(Calendar.MINUTE));
        mWeek.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(mCalendar.getTime()));
        mDate.setText(DateUtils.formatDateTime(this, mCalendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_NUMERIC_DATE));
    }

    private void setTime(int hour, int minute) {
        mTimeHour0.setImageResource(getDrawableResourceId(hour/10));
        mTimeHour1.setImageResource(getDrawableResourceId(hour%10));
        mTimeMinute0.setImageResource(getDrawableResourceId(minute/10));
        mTimeMinute1.setImageResource(getDrawableResourceId(minute%10));
    }

    private int getDrawableResourceId(int name) {
        return getResources().getIdentifier("clock_time_" + name, "drawable", getPackageName());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyDown: keyCode = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER :
            case KeyEvent.KEYCODE_DPAD_DOWN :
            case KeyEvent.KEYCODE_DPAD_UP :
                startActivity(new Intent(this, MainMenuListActivity.class));
                return true;
            case KeyEvent.KEYCODE_BACK :
                return true;
            case KeyEvent.KEYCODE_0 :
                startDialActivity(Integer.toString(keyCode - 7));
                return true;
            case KeyEvent.KEYCODE_1 :
            case KeyEvent.KEYCODE_2 :
            case KeyEvent.KEYCODE_3 :
            case KeyEvent.KEYCODE_4 :
            case KeyEvent.KEYCODE_5 :
            case KeyEvent.KEYCODE_6 :
            case KeyEvent.KEYCODE_7 :
            case KeyEvent.KEYCODE_8 :
            case KeyEvent.KEYCODE_9 :
                if (event.getRepeatCount() == 0) {
                    event.startTracking();
                }
                return true;
            case KeyEvent.KEYCODE_STAR :
                startDialActivity("*");
                return true;
            case KeyEvent.KEYCODE_POUND :
                startDialActivity("#");
                return true;
            case KeyEvent.KEYCODE_FOCUS :
                startActivity(new Intent("android.intent.action.ON_OFF_WIFIAP")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
        }

        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyLongPress: keyCode = " + keyCode);
        switch (keyCode) {
            //case KeyEvent.KEYCODE_1 :
            case KeyEvent.KEYCODE_2 :
            case KeyEvent.KEYCODE_3 :
            case KeyEvent.KEYCODE_4 :
            case KeyEvent.KEYCODE_5 :
            case KeyEvent.KEYCODE_6 :
            case KeyEvent.KEYCODE_7 :
            case KeyEvent.KEYCODE_8 :
            case KeyEvent.KEYCODE_9 :
                lockLongPress = true;
                startLongClickDialActivity(keyCode - 7);
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyUp: keyCode = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_1 :
            case KeyEvent.KEYCODE_2 :
            case KeyEvent.KEYCODE_3 :
            case KeyEvent.KEYCODE_4 :
            case KeyEvent.KEYCODE_5 :
            case KeyEvent.KEYCODE_6 :
            case KeyEvent.KEYCODE_7 :
            case KeyEvent.KEYCODE_8 :
            case KeyEvent.KEYCODE_9 :
                if (lockLongPress) {
                    lockLongPress = false;
                } else {
                    startDialActivity(Integer.toString(keyCode - 7));
                }
                return true;
        }
        return false;
    }

    private void startDialActivity(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL));
    }

    private void startLongClickDialActivity(int number) {
        startActivity(new Intent("com.android.phone.action.LONG_CLICK_DIAL").putExtra("key", number));
    }
}
