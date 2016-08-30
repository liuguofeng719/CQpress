package com.cqpress.book.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.bean.RegisterAppRequestVo;
import com.cqpress.book.bean.RegisterAppResultVo;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/7/17.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.edit_register)
    EditText editRegister;
    @Bind(R.id.tv_complete)
    TextView tvComplete;
    private Dialog mDialog;

    @OnClick(R.id.tv_complete)
    public void tvComplete() {
        if (TextUtils.isEmpty(editRegister.getText())) {
            CommonUtils.make(this, "请输入注册码");
            return;
        }

        mDialog = CommonUtils.showDialog(this, getString(R.string.common_loading_message));
        mDialog.show();

        final RegisterAppRequestVo requestVo = new RegisterAppRequestVo();
        requestVo.setRequestData(editRegister.getText().toString());
        requestVo.setReqMethod("RegisterApp");
        requestVo.sign();
        Call<RegisterAppResultVo> registerAppResultVoCall = getApis().registerApp(requestVo).clone();
        registerAppResultVoCall.enqueue(new Callback<RegisterAppResultVo>() {

            @Override
            public void onResponse(Response<RegisterAppResultVo> response, Retrofit retrofit) {
                CommonUtils.dismiss(mDialog);
                if (response.isSuccess() && response.body() != null) {
                    RegisterAppResultVo resultVo = response.body();
                    if (!resultVo.isError()) {
                        CommonUtils.make(RegisterActivity.this, "注册码注册成功");
                        AppPreferences.putString("registerCode", resultVo.getData());
                        AppPreferences.putString("registerCodeNumber", editRegister.getText().toString());
                        AppPreferences.putString("customerType", resultVo.getCustomerType());
                        readyGoThenKill(LoginActivity.class);
                    } else {
                        CommonUtils.make(RegisterActivity.this, resultVo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtils.dismiss(mDialog);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            CommonUtils.dismiss(mDialog);
        }
        super.onDestroy();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.register_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!TextUtils.isEmpty(AppPreferences.getString("registerCode"))) {
            editRegister.setText(AppPreferences.getString("registerCodeNumber"));
        }
    }
}
