package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class LoginRequestVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private RequestData requestData;

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public static class RequestData {

        @SerializedName("LoginName")
        private String loginName;
        @SerializedName("Pwd")
        private String pwd;
        @SerializedName("IsPlatformData")
        private boolean isPlatformData = false;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public boolean isPlatformData() {
            return isPlatformData;
        }

        public void setPlatformData(boolean platformData) {
            isPlatformData = platformData;
        }
    }
}
