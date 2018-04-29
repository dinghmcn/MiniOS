package com.dinghmcn.minios;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * Created by dinghmcn on 2016/11/19.
 */

public class KeyguardFloatActivity extends Activity {

    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mKeyguardManager.inKeyguardRestrictedInputMode() || mKeyguardManager.isKeyguardLocked()
                    || mKeyguardManager.isKeyguardSecure()) {
                return;
            }
            mPowerManager.goToSleep(SystemClock.uptimeMillis());
        }
    };
    private PowerManager mPowerManager;
    private KeyguardManager mKeyguardManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_KEYGUARD);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 5*1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, KeyguardActivity.class);
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            finish();
        } else {
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        finish();
    }
}
