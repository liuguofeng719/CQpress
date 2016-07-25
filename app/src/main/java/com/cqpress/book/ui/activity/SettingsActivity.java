package com.cqpress.book.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class SettingsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_logout)
    TextView tv_logout;
    @Bind(R.id.tv_header_title)
    TextView tv_header_title;

    @OnClick(R.id.iv_back)
    public void ivBack() {
        finish();
    }

    @OnClick(R.id.tv_logout)
    public void tv_logout() {
        AppPreferences.putString("userId", "");
        CommonUtils.make(this, getString(R.string.logout_message));
        readyGoThenKill(LoginActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.settings_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.settings_title));
    }
}
