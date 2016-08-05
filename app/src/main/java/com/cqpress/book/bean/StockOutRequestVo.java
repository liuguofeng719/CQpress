package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockOutRequestVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private RequestData requestData;

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public static class RequestData {

        @SerializedName("IsAllot")
        private boolean isAllot;
        @SerializedName("ScanUserID")
        private String scanUserId;
        @SerializedName("DeviceID")
        private String deviceID;// 当前设备ID

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public boolean isAllot() {
            return isAllot;
        }

        public void setAllot(boolean allot) {
            isAllot = allot;
        }

        public String getScanUserId() {
            return scanUserId;
        }

        public void setScanUserId(String scanUserId) {
            this.scanUserId = scanUserId;
        }
    }
}
