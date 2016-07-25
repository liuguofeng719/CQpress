package com.cqpress.book.ui.activity;

import android.text.TextUtils;
import android.view.View;

import com.cqpress.book.R;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;

/**
 * Created by liuguofeng719 on 2016/7/17.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.splash_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!TextUtils.isEmpty(AppPreferences.getString("registerCode"))) {
            readyGoThenKill(LoginActivity.class);
        } else {
            readyGoThenKill(RegisterActivity.class);
        }
    }
}
