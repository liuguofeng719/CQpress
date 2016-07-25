package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class ScanUpLoadRequestVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private RequestData requestData;

    public static class RequestData {
        @SerializedName("StockType")
        private String stockType;   //出入库类型，枚举（0：入库扫描，1：出库扫描）
        @SerializedName("PackageCode")
        private String packageCode;  // 捆标（如扫描到是捆标，赋值）
        @SerializedName("EPC")
        private String epc; // EPC码（如扫描到是EPC，赋值）
        @SerializedName("DeviceID")
        private String deviceID;// 当前设备ID
        @SerializedName("LocationID")
        private String locationId;// 当前设备ID
        @SerializedName("UserID")
        private String userID;  // 当前登录手持机用户ID
        @SerializedName("StockOutDetailID")
        private String stockOutDetailID; //出库单详情ID（从出库单数据获取）
        @SerializedName("StockInDetailID")
        private String stockInDetailID;//入库单详情ID（从入库单数据获取）

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getStockType() {
            return stockType;
        }

        public void setStockType(String stockType) {
            this.stockType = stockType;
        }

        public String getPackageCode() {
            return packageCode;
        }

        public void setPackageCode(String packageCode) {
            this.packageCode = packageCode;
        }

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getStockOutDetailID() {
            return stockOutDetailID;
        }

        public void setStockOutDetailID(String stockOutDetailID) {
            this.stockOutDetailID = stockOutDetailID;
        }

        public String getStockInDetailID() {
            return stockInDetailID;
        }

        public void setStockInDetailID(String stockInDetailID) {
            this.stockInDetailID = stockInDetailID;
        }

    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }
}
