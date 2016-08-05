package com.cqpress.book.ui.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.cqpress.book.BookApplication;
import com.cqpress.book.api.Apis;
import com.cqpress.book.base.BaseView;
import com.cqpress.book.netstatus.NetUtils;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.RetrofitUtils;

import retrofit.Retrofit;

public abstract class BaseActivity extends BaseFragmentActivity implements BaseView {

    protected Apis getApis() {
        String baseUrl = TextUtils.isEmpty(AppPreferences.getString("BASE_URL")) ? Apis.BASE_URL : AppPreferences.getString("BASE_URL");
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(baseUrl);
        return retrofit.create(Apis.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected BookApplication getBaseApplication() {
        return (BookApplication) getApplication();
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }
}
