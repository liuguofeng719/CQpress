package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by liuguofeng719 on 2016/7/7.
 */
public class LoginResultVo extends BaseResultVo {

    @SerializedName("UserID")
    private String UserID;
    @SerializedName("Name")
    private String name;
    @SerializedName("LoginName")
    private String loginName;
    @SerializedName("MobilePhone")
    private String mobilePhone;
    @SerializedName("RoleNames")
    private String roleNames;

    @SerializedName("PermissionCodeList")
    private String[] permissionCodeList;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String[] getPermissionCodeList() {
        return permissionCodeList;
    }

    public void setPermissionCodeList(String[] permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }

    @Override
    public String toString() {
        return "LoginResultVo{" +
                "UserID='" + UserID + '\'' +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", roleNames='" + roleNames + '\'' +
                ", permissionCodeList=" + Arrays.toString(permissionCodeList) +
                '}';
    }
}
