package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockOutResultVo<T> extends BaseResultVo {
    @SerializedName("Data")
    private List<T> data;
    @SerializedName("TotalCount")
    private int totalCount;
    @SerializedName("PageIndex")
    private int pageIndex;
    @SerializedName("PageSize")
    private int pageSize;
    @SerializedName("PageCount")
    private int pageCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
