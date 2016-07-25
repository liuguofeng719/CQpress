package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/17.
 */
public class RegisterAppRequestVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private String requestData;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }
}
