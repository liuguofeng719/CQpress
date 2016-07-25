package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockOutVo {

    @SerializedName("CarLicense")
    private String carLicense;
    @SerializedName("CustomerID")
    private String customerID;
    @SerializedName("DiverName")
    private String diverName;
    @SerializedName("DiverPhone")
    private String diverPhone;
    @SerializedName("RegisterCode")
    private String registerCode;
    @SerializedName("StockOutDate")
    private String stockOutDate;
    @SerializedName("StockOutID")
    private String stockOutID;
    @SerializedName("StockOutNumber")
    private String stockOutNumber;
    @SerializedName("StockOutState")
    private int stockOutState;

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDiverName() {
        return diverName;
    }

    public void setDiverName(String diverName) {
        this.diverName = diverName;
    }

    public String getDiverPhone() {
        return diverPhone;
    }

    public void setDiverPhone(String diverPhone) {
        this.diverPhone = diverPhone;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(String stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    public String getStockOutID() {
        return stockOutID;
    }

    public void setStockOutID(String stockOutID) {
        this.stockOutID = stockOutID;
    }

    public String getStockOutNumber() {
        return stockOutNumber;
    }

    public void setStockOutNumber(String stockOutNumber) {
        this.stockOutNumber = stockOutNumber;
    }

    public int getStockOutState() {
        return stockOutState;
    }

    public void setStockOutState(int stockOutState) {
        this.stockOutState = stockOutState;
    }
}
