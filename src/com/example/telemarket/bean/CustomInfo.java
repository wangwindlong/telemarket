package com.example.telemarket.bean;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-8
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
public class CustomInfo implements Serializable {
    private String retriAlpha;//姓名首字母
    private String name;      //姓名
    private String position;  //职位
    private String company;   //所属公司
    private String business;  //所属行业
    private String telephone; //电话号码
    private String sex;       //性别
    private String id;        //客户id
    private String guid;      //组编号
    private String easyName;  //助记简称
    private String customType;//客户类型
    private String mailBox;   //电子邮箱
    private String province;  //省份
    private String isResieveMsg;  //是否接受短信
    private String photoPath;  //头像路径


    public String getRetriAlpha() {
        return retriAlpha;
    }

    public void setRetriAlpha(String retriAlpha) {
        this.retriAlpha = retriAlpha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEasyName() {
        return easyName;
    }

    public void setEasyName(String easyName) {
        this.easyName = easyName;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getMailBox() {
        return mailBox;
    }

    public void setMailBox(String mailBox) {
        this.mailBox = mailBox;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getResieveMsg() {
        return isResieveMsg;
    }

    public void setResieveMsg(String resieveMsg) {
        isResieveMsg = resieveMsg;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
