package com.dinghmcn.minios.fota;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.dinghmcn.minios.R;

/**
 * Created by dinghmcn on 2017/4/27.
 */

public class FotaNewVersionActivity extends Activity {
    private final String TAG = "FotaNewVersionActivity";

    private boolean isNew;
    private TextView mMessageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fota_activity);

        isNew = getIntent().getBooleanExtra("isNew", true);
        mMessageTV = (TextView) findViewById(R.id.message);
        String message;
        if (isNew) {
            message = getString(R.string.fota_new_version);
        } else {
            message = getString(R.string.fota_update_result);
        }
        mMessageTV.setText(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (isNew) {
                    Log.e(TAG, "sendBroadcast : com.adups.fota.custom_update,");
                    sendBroadcast(new Intent("com.adups.fota.custom_update"),
                            "com.android.permission.custom_update");
                }
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
            default:
                return true;
        }
    }
}
