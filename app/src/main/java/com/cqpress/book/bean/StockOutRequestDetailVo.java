package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockOutRequestDetailVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private RequestData requestData;

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public static class RequestData {

        @SerializedName("IsAduit")
        private boolean isAduit;
        @SerializedName("StockOutDocmentID")
        private String stockOutDocmentID;

        public boolean isAduit() {
            return isAduit;
        }

        public void setAduit(boolean aduit) {
            isAduit = aduit;
        }

        public String getStockOutDocmentID() {
            return stockOutDocmentID;
        }

        public void setStockOutDocmentID(String stockOutDocmentID) {
            this.stockOutDocmentID = stockOutDocmentID;
        }
    }

}
