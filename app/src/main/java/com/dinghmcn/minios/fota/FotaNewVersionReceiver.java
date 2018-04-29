package com.dinghmcn.minios.fota;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dinghmcn on 2017/4/27.
 */

public class FotaNewVersionReceiver extends BroadcastReceiver {
    private final String TAG = "FotaNewVersionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);
        if (action.equals("com.fota.custom_new_version")) {
            boolean newVersion = intent.getBooleanExtra("new_version", false);
            if (newVersion) {
                context.startActivity(new Intent(context, FotaNewVersionActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("isNew", true));
            }
        } else if (action.equals("com.fota.custom_update_result")) {
            String result = intent.getStringExtra("update_result");
            context.startActivity(new Intent(context, FotaNewVersionActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("isNew", false)
                    .putExtra("result", result));
        }
    }
}
