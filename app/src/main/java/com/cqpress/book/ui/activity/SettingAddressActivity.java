package com.cqpress.book.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/17.
 */
public class SettingAddressActivity extends BaseActivity {


    @Bind(R.id.edit_register)
    EditText editRegister;
    @Bind(R.id.tv_complete)
    TextView tvComplete;
    private Dialog mDialog;

    @OnClick(R.id.tv_complete)
    public void tvComplete() {
        if (TextUtils.isEmpty(editRegister.getText())) {
            CommonUtils.make(this, "请输入服务器地址");
            return;
        }
        AppPreferences.putString("BASE_URL", "http://" + editRegister.getText().toString() + "/SubDeviceService.svc/rest/");
        AppPreferences.putString("BASE_URL_LOCATION", editRegister.getText().toString());
        CommonUtils.make(this, "服务器地址设置成功");
        readyGoThenKill(LoginActivity.class);
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
        return R.layout.setting_address_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!TextUtils.isEmpty(AppPreferences.getString("BASE_URL"))) {
            editRegister.setText(AppPreferences.getString("BASE_URL_LOCATION"));
        }
    }
}
