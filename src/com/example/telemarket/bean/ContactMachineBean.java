package com.example.telemarket.bean;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-14
 * Time: 下午7:19
 * To change this template use File | Settings | File Templates.
 */
public class ContactMachineBean implements Serializable {
    //排序的字符
    private String sortKey;
    private String name;
    private String phoneNumber = null;
    private String email = "";
    private String note = "";  //备注,尊称
    private String nickName = "";//昵称
    private String rowcontactId;
    private boolean check;
    private String id;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ContactMachineBean() {
    }

    public ContactMachineBean(String sortKey, String name, String phoneNumber, boolean check, String id, String rowcontactId) {
        this.sortKey = sortKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.check = check;
        this.id = id;
        this.rowcontactId = rowcontactId;
    }

    public String getRowcontactId() {
        return rowcontactId;
    }

    public void setRowcontactId(String rowcontactId) {
        this.rowcontactId = rowcontactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
