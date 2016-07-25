package com.cqpress.book.ui.base;


import com.cqpress.book.api.Apis;
import com.cqpress.book.base.BaseView;
import com.cqpress.book.utils.RetrofitUtils;

import retrofit.Retrofit;

public abstract class BaseFragment extends BaseLazyFragment implements BaseView {
    protected Apis getApis(){
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(Apis.BASE_URL);
        return retrofit.create(Apis.class);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        toggleShowLoading(true, null);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }
}
