package com.dinghmcn.minios;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dinghmcn on 2016/11/18.
 */


public class KeyguardActivity extends Activity {
    private final String TAG = "KeyguardActivity";

    private TextView mKeyguardMessage;
    private ImageView mKeyguardOK;
    private ImageView mKeyguardPlus;

    private int mKeyguardStep = -1;

    private PowerManager mPowerManager;
    private KeyguardManager mKeyguardManager;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        setContentView(R.layout.keyguard_activity);
        mKeyguardMessage = (TextView) findViewById(R.id.keyguard_message);
        mKeyguardOK = (ImageView) findViewById(R.id.keyguard_ok);
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
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 5*1000);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        finish();
    }

}
