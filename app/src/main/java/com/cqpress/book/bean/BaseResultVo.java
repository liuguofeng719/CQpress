package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class BaseResultVo {

    @SerializedName("ErrCode")
    private int errCode;
    @SerializedName("IsError")
    private boolean isError;
    @SerializedName("Message")
    private String message;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResultVo{" +
                "errCode=" + errCode +
                ", isError=" + isError +
                ", message='" + message + '\'' +
                '}';
    }
}
