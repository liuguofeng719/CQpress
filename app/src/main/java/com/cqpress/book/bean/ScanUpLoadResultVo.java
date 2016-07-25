package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class ScanUpLoadResultVo extends BaseResultVo {

    @SerializedName("ScanResult")
    private boolean scanResult; //扫描上传结果
    @SerializedName("BookStockState")
    private boolean bookStockState; //当前书籍出库状态0:未完成扫描，1 完成
    @SerializedName("SurplusAmount")
    private int surplusAmount; //当前书籍剩余扫描量

    public boolean isScanResult() {
        return scanResult;
    }

    public void setScanResult(boolean scanResult) {
        this.scanResult = scanResult;
    }

    public boolean isBookStockState() {
        return bookStockState;
    }

    public void setBookStockState(boolean bookStockState) {
        this.bookStockState = bookStockState;
    }

    public int getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(int surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
