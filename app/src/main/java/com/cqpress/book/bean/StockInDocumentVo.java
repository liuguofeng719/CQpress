package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/11.
 */
public class StockInDocumentVo {

    @SerializedName("RegisterCode")
    private String registerCode;
    @SerializedName("StockInAmount")
    private int stockInAmount;
    // 入库时间
    @SerializedName("StockInDate")
    private String stockInDate;
    //入库单ID
    @SerializedName("StockInID")
    private String stockInID;
    //入库单号
    @SerializedName("StockInNumber")
    private String stockInNumber;
    //出库状态0:未完成， 1：完成
    @SerializedName("StockInState")
    private int stockInState;
    @SerializedName("SupplierID")
    private String supplierID;

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public int getStockInAmount() {
        return stockInAmount;
    }

    public void setStockInAmount(int stockInAmount) {
        this.stockInAmount = stockInAmount;
    }

    public String getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(String stockInDate) {
        this.stockInDate = stockInDate;
    }

    public String getStockInID() {
        return stockInID;
    }

    public void setStockInID(String stockInID) {
        this.stockInID = stockInID;
    }

    public String getStockInNumber() {
        return stockInNumber;
    }

    public void setStockInNumber(String stockInNumber) {
        this.stockInNumber = stockInNumber;
    }

    public int getStockInState() {
        return stockInState;
    }

    public void setStockInState(int stockInState) {
        this.stockInState = stockInState;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
