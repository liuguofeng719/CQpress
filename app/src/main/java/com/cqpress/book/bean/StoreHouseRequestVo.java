package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class StoreHouseRequestVo extends BaseRequestVo {

    @SerializedName("RequestData")
    private String requestData;;
    @SerializedName("Paging")
    private PagingStoreVo pagingStoreVo;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public PagingStoreVo getPagingStoreVo() {
        return pagingStoreVo;
    }

    public void setPagingStoreVo(PagingStoreVo pagingStoreVo) {
        this.pagingStoreVo = pagingStoreVo;
    }
}
