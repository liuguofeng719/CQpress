package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class PagingStoreVo {

    @SerializedName("PageIndex")
    private int pageIndex;
    @SerializedName("PageSize")
    private int pageSize = 2147483647;// 不需要分页 PageSize传2147483647

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
