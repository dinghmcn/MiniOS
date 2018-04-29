package com.dinghmcn.minios;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by dinghmcn on 2016/11/10.
 */

public class MainMenuListActivity extends ListActivity {
    private final String TAG = "MainMenuListActivity";
    private ListView mListView;
    private FooSuperAdapter mFooSuperAdapter;
    private static int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_menu_activity);
        mListView = getListView();
        mListView.setOnKeyListener(mListViewOnKeyListener);
        mFooSuperAdapter = new FooSuperAdapter(this);
        TypedArray mImageArray = getResources().obtainTypedArray(R.array.main_menu_image_array);
        for (int i = 0; i < mImageArray.length(); i++) {
            mFooSuperAdapter.addData(new FooBean(mImageArray.getResourceId(i, 0)));
        }
        setListAdapter(mFooSuperAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setSelection(mPosition);
    }

    private View.OnKeyListener mListViewOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (mPosition > 0) {
                            mListView.setSelection(--mPosition);
                        } else if (mPosition <=0) {
                            mListView.setSelection(mPosition = mListView.getCount() - 1);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (mPosition < mListView.getCount() - 1) {
                            mListView.setSelection(++mPosition);
                        } else if (mPosition >= mListView.getCount() - 1) {
                            mListView.setSelection(mPosition = 0);
                        }
                        return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "position =" + position);
        switch (position) {
            case 0: //Contacts
                startActivity(new Intent("android.intent.action.MAIN")
                        .setClassName("com.android.contacts", "com.android.contacts.ContactsMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Log.d("dhm", "startActivity(nContactsMenuListActivity)");
                break;
            case 1: //Mms
                startActivity(new Intent("android.intent.action.MAIN")
                        .setClassName("com.android.mms", "com.android.mms.MmsMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 2: //CallLog
                startActivity(new Intent("com.android.dialer.action.CALL_LOG")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 3: //Music
                startActivity(new Intent("android.intent.action.MUSIC_PLAYER")
                        .setClassName("com.android.music", "com.android.music.MusicMainMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 4: //Recorder
                startActivity(new Intent()
                        .setClassName("com.android.soundrecorder", "com.android.soundrecorder.SoundRecorderMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 5: //Bluetooth
                startActivity(new Intent("com.android.settings.action.BLUETOOTH_MENU")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 6: //Alarm Clock
                startActivity(new Intent("android.intent.action.MAIN")
                        .setClassName("com.android.deskclock", "com.android.deskclock.alarms.AlarmMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 7: //Settings
                startActivity(new Intent("com.android.settings.action.SETTINGS_MENU")
                        .setClassName("com.android.settings", "com.android.settings.SettingsMenuListActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:

        }
        super.onListItemClick(l, v, position, id);
    }

    private class FooBean {
        private int mResId;

        public FooBean(int resId) {
            mResId = resId;
        }

        public int getContent() {
            return mResId;
        }

    }

    private class FooSuperAdapter extends LBaseAdapter<FooBean, LBaseAdapter.BaseViewHolder> {

        public FooSuperAdapter(Context context) {
            super(context);
        }

        @Override
        protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
            return new BaseViewHolder(View.inflate(getContext(), R.layout.main_menu_item,null));
        }

        @Override
        protected void bindViewHolder(BaseViewHolder holder, int position, FooBean data) {
            ImageView imageView = holder.getView(R.id.main_menu_image);
            imageView.setImageResource(data.getContent());
        }
    }
}
