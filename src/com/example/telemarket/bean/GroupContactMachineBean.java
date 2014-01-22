package com.example.telemarket.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-14
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class GroupContactMachineBean implements Serializable {
    private int id;
    private String name;
    private ArrayList<ContactMachineBean> contact;
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ContactMachineBean> getContact() {
        return contact;
    }

    public void setContact(ArrayList<ContactMachineBean> contact) {
        this.contact = contact;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
