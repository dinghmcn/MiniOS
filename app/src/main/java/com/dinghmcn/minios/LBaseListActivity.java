package com.dinghmcn.minios;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by dinghmcn on 2016/11/21.
 */

public abstract class LBaseListActivity extends ListActivity {
    private final String TAG = "LBaseListActivity";
    private ListView mListView;
    private LBaseListActivity.FooSuperAdapter mFooSuperAdapter;

    public static void setmPosition(int mPosition) {
        LBaseListActivity.mPosition = mPosition;
    }

    private static int mPosition = 1;

    protected abstract int getArrayId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = getListView();
        mListView.setBackgroundResource(R.drawable.lbase_list_bg);
        mListView.setOnKeyListener(mListViewOnKeyListener);
        initData();
    }

    protected void initData() {
        setListAdapter(null);
        mFooSuperAdapter = new FooSuperAdapter(this);
        String[] mTitleArray = getResources().getStringArray(getArrayId());
        for (int i = 0; i < mTitleArray.length; i++) {
            mFooSuperAdapter.addData(new FooBean(mTitleArray[i]));
        }
        setListAdapter(mFooSuperAdapter);
        mListView.setSelectionFromTop(mPosition, 21);
    }

    private View.OnKeyListener mListViewOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            TextView oldTextView = (TextView) mListView.getSelectedView().findViewById(R.id.lbase_list_title);
            oldTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            oldTextView.setTextSize(12);
            oldTextView.setSelected(false);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (mPosition > 1) {
                            mListView.setSelectionFromTop(--mPosition, 21);
                        } else if (mPosition <= 1) {
                            mListView.setSelectionFromTop(mPosition = mListView.getCount() - 2, 21);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (mPosition < mListView.getCount() - 2) {
                            mListView.setSelectionFromTop(++mPosition, 21);
                        } else if (mPosition >= mListView.getCount() - 2) {
                            mListView.setSelectionFromTop(mPosition = 1, 21);
                        }
                        return true;
                }
            }
            TextView textView = (TextView) mListView.getSelectedView().findViewById(R.id.lbase_list_title);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setTextSize(14);
            textView.setSelected(true);
            return false;
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "position =" + position);
        setResult(position, null);
        finish();
    }


    private class FooBean {
        private String mTitle;

        public FooBean(String title) {
            mTitle = title;
        }
        public String getTitle() {
            return mTitle;
        }
    }

    private class FooSuperAdapter extends LBaseAdapter<FooBean, LBaseAdapter.BaseViewHolder> {

        public FooSuperAdapter(Context context) {
            super(context);
        }

        @Override
        protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
            return new BaseViewHolder(View.inflate(getContext(), R.layout.lbase_list_item, null));
        }

        @Override
        protected void bindViewHolder(BaseViewHolder holder, int position, FooBean data) {
            TextView textView = holder.getView(R.id.lbase_list_title);
            textView.setText(data.getTitle());
            if (position == mPosition) {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextSize(14);
            }  else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setTextSize(12);
            }
        }
    }
}
