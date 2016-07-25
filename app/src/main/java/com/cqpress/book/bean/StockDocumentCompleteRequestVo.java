package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/16.
 */
public class StockDocumentCompleteRequestVo extends BaseRequestVo {


    @SerializedName("RequestData")
    private RequestData requestData;

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public static class RequestData {

        @SerializedName("StockType")
        private int stockType;
        @SerializedName("DocumentID")
        private String documentID;

        public int getStockType() {
            return stockType;
        }

        public void setStockType(int stockType) {
            this.stockType = stockType;
        }

        public String getDocumentID() {
            return documentID;
        }

        public void setDocumentID(String documentID) {
            this.documentID = documentID;
        }
    }
}
