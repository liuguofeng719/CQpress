package com.cqpress.book.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.bean.BaseRequestVo;
import com.cqpress.book.bean.BaseResultVo;
import com.cqpress.book.bean.LoginRequestVo;
import com.cqpress.book.bean.LoginResultVo;
import com.cqpress.book.netstatus.NetUtils;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;
import com.cqpress.book.utils.TLog;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class LoginActivity extends BaseActivity {

    private Dialog mDialog;
    @Bind(R.id.tv_login_button)
    TextView tv_login_button;
    @Bind(R.id.edit_uname)
    EditText edit_uname;
    @Bind(R.id.edit_password)
    EditText edit_password;

    @OnClick(R.id.tv_setting_button)
    public void settingButton() {
        Dialog dialog = CommonUtils.createDialog(this);
        dialog.setContentView(R.layout.setting_dialog);
        TextView tv_register_code = (TextView) dialog.findViewById(R.id.tv_register_code);
        tv_register_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(RegisterActivity.class);
            }
        });
        TextView tv_service_address = (TextView) dialog.findViewById(R.id.tv_service_address);
        tv_service_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(SettingAddressActivity.class);
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @OnClick(R.id.tv_login_button)
    public void login() {
        if (TextUtils.isEmpty(AppPreferences.getString("registerCode"))) {
            CommonUtils.make(LoginActivity.this, "注册码不能为空");
            return;
        }
        if (TextUtils.isEmpty(AppPreferences.getString("BASE_URL"))) {
            CommonUtils.make(LoginActivity.this, "服务器地址不能为空");
            return;
        }

        mDialog = CommonUtils.showDialog(this, getString(R.string.common_loading_message));
        mDialog.show();
        if (NetUtils.isNetworkAvailable(LoginActivity.this)) {
            if (validate()) {
                BaseRequestVo baseVo = new BaseRequestVo();
                baseVo.setReqMethod("AppGet");
                baseVo.sign();
                Call<BaseResultVo> resultVoCall = getApis().appGet(baseVo).clone();
                resultVoCall.enqueue(new Callback<BaseResultVo>() {
                    @Override
                    public void onResponse(Response<BaseResultVo> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            BaseResultVo resultVo = response.body();
                            if (!resultVo.isError()) {
                                LoginRequestVo loginVo = new LoginRequestVo();
                                final LoginRequestVo.RequestData requestData = new LoginRequestVo.RequestData();
                                requestData.setLoginName(edit_uname.getText().toString());
                                requestData.setPwd(edit_password.getText().toString());
                                loginVo.setRequestData(requestData);
                                loginVo.setReqMethod("AppUserLogin");
                                Call<LoginResultVo> resultVoLogin = getApis().appUserLogin(loginVo).clone();
                                resultVoLogin.enqueue(new Callback<LoginResultVo>() {
                                    @Override
                                    public void onResponse(Response<LoginResultVo> response, Retrofit retrofit) {
                                        CommonUtils.dismiss(mDialog);
                                        if (response.isSuccess() && response.body() != null) {
                                            if (!response.body().isError()) {
                                                AppPreferences.putString("userId", response.body().getUserID());
                                                AppPreferences.putObject(response.body());
                                                readyGoThenKill(IndexActivity.class);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        CommonUtils.dismiss(mDialog);
                                    }
                                });
                            } else {
                                CommonUtils.make(LoginActivity.this, response.message());
                                CommonUtils.dismiss(mDialog);
                            }
                        } else {
                            TLog.e(TAG_LOG, response.message());
                            CommonUtils.dismiss(mDialog);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        CommonUtils.dismiss(mDialog);
                    }
                });

            }
        } else {
            CommonUtils.dismiss(mDialog);
            CommonUtils.make(LoginActivity.this, getString(R.string.no_network));
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(edit_uname.getText())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_uname_empty));
            return false;
        }
        if (TextUtils.isEmpty(edit_password.getText())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_pwd_empty));
            return false;
        }
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGoThenKill(IndexActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
