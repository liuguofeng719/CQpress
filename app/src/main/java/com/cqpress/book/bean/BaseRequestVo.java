package com.cqpress.book.bean;

import com.cqpress.book.utils.MD5Util;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class BaseRequestVo {

    @SerializedName("AppKey")
    private String appKey = "ap185fcc9cbeaed";
    @SerializedName("AppSecret")
    private String appSecret = "6fec2526b636b8147d8682de6e7875b9";
    private String reqMethod;
    @SerializedName("RegisterCode")
    private String registerCode = "6feac2526b636b";
    @SerializedName("Sign")
    private String sign;
    @SerializedName("Timestamp")
    private long timestamp;

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String sign() {
        this.sign = MD5Util.MD5Encode(appKey + appSecret + registerCode + getReqMethod() + getTimestamp(), "utf8");
        return this.sign;
    }

    public long getTimestamp() {
        long timespan = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timespan = sdf.parse(sdf.format(new Date())).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.timestamp = timespan;
        return timespan;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }


}
