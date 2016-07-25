package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.cqpress.book.R;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.ExitDoubleClick;

import butterknife.Bind;
import butterknife.OnClick;

public class IndexActivity extends BaseActivity {

    @Bind(R.id.ly_book_out)
    LinearLayout ly_book_out;

    @Bind(R.id.ly_book_in)
    LinearLayout ly_book_in;
    @Bind(R.id.ly_layout)
    LinearLayout ly_layout;

    @Bind(R.id.ly_book_setting)
    LinearLayout ly_book_setting;

//    @OnClick(R.id.ly_book_uhf)
//    public void ly_book_uhf() {
//        readyGo(MainActivity.class);
//    }

    private Bundle extras;

    /**
     * 出库
     */
    @OnClick(R.id.ly_book_out)
    public void bookOut() {
        readyGo(StockOutDocumentsActivity.class);
    }

    /**
     * 入库
     */
    @OnClick(R.id.ly_book_in)
    public void bookIn() {
        readyGo(StockInDocumentsActivity.class);
    }

    /**
     * 设置
     */
    @OnClick(R.id.ly_book_setting)
    public void bookSetting() {
        readyGo(SettingsActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        ly_book_out.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ExitDoubleClick.getInstance(this).doDoubleClick(2000, null);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.v("MyKeyDown", "onkeydown=left");
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.v("MyKeyDown", "onkeydown=up");
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.v("MyKeyDown", "onkeydown=right");
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.v("MyKeyDown", "onkeydown=down");
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.v("MyKeyDown", "onkeydown=center");
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
