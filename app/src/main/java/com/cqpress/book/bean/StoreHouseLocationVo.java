package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class StoreHouseLocationVo {

    @SerializedName("LocationID")
    private String locationID;//库位ID
    @SerializedName("StorehouseID")
    private String storehouseID;//库房ID
    @SerializedName("LocationName")
    private String locationName;//库位名称
    @SerializedName("StorehouseName")
    private String storehouseName;//库房名称

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getStorehouseID() {
        return storehouseID;
    }

    public void setStorehouseID(String storehouseID) {
        this.storehouseID = storehouseID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }
}
