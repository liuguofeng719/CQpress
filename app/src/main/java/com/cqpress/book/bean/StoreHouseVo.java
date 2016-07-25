package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class StoreHouseVo {

    @SerializedName("StorehouseID")
    private String storehouseID; //库房ID
    @SerializedName("StorehouseName")
    private String storehouseName; //库房名称,
    @SerializedName("IsPlatformData")
    private boolean isPlatformData;
    @SerializedName("RegisterCode")
    private String registerCode;
    @SerializedName("IsUploadData")
    private boolean isUploadData;

    public String getStorehouseID() {
        return storehouseID;
    }

    public void setStorehouseID(String storehouseID) {
        this.storehouseID = storehouseID;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public boolean isPlatformData() {
        return isPlatformData;
    }

    public void setPlatformData(boolean platformData) {
        isPlatformData = platformData;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public boolean isUploadData() {
        return isUploadData;
    }

    public void setUploadData(boolean uploadData) {
        isUploadData = uploadData;
    }

    @Override
    public String toString() {
        return "StoreHouseVo{" +
                "storehouseID='" + storehouseID + '\'' +
                ", storehouseName='" + storehouseName + '\'' +
                ", isPlatformData=" + isPlatformData +
                ", registerCode='" + registerCode + '\'' +
                ", isUploadData=" + isUploadData +
                '}';
    }
}
