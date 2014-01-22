package com.example.telemarket.bean.user;

import java.io.Serializable;

public class RoleBean implements Serializable {
    //15 1 超级管理员，最高领导 28 区域负责人  29 省负责人  30 市负责人  31 县/区 负责人
    //32  县/区巡查员
    //roleIds 值的格式 15@16@28
    private boolean bossRole = false;//超级管理员，最高领导
    private boolean domainRole = false;
    private String domainId = "-2";
    private boolean provinceRole = false;
    private String provinceId = "-2";
    private boolean cityRole = false;
    private String cityId = "-2";
    private boolean countyRole = false;
    private String countyId = "-2";
    private boolean inspectorRole = false;//巡查员
    private boolean shopownerRole = false;//店长

    public boolean isBossRole() {
        return bossRole;
    }

    public void setBossRole(boolean bossRole) {
        this.bossRole = bossRole;
    }

    public boolean isDomainRole() {
        return domainRole;
    }

    public void setDomainRole(boolean domainRole) {
        this.domainRole = domainRole;
    }

    public boolean isProvinceRole() {
        return provinceRole;
    }

    public void setProvinceRole(boolean provinceRole) {
        this.provinceRole = provinceRole;
    }

    public boolean isCityRole() {
        return cityRole;
    }

    public void setCityRole(boolean cityRole) {
        this.cityRole = cityRole;
    }

    public boolean isCountyRole() {
        return countyRole;
    }

    public void setCountyRole(boolean countyRole) {
        this.countyRole = countyRole;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public boolean isInspectorRole() {
        return inspectorRole;
    }

    public void setInspectorRole(boolean inspectorRole) {
        this.inspectorRole = inspectorRole;
    }

    public boolean isShopownerRole() {
        return shopownerRole;
    }

    public void setShopownerRole(boolean shopownerRole) {
        this.shopownerRole = shopownerRole;
    }
}
