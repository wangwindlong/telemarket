package com.example.telemarket.bean;

import java.io.Serializable;

public class AnnouncementBean implements Serializable {
    String type;//公告类型
    String date;//发布时间
    String title;//标题
    String person;//发布人
    String id;   //公告id
    String fbrId;   //发布人id
    String content;  //内容

    public AnnouncementBean() {
    }

    public AnnouncementBean(String date, String title, String person, String id) {
        this.date = date;
        this.title = title;
        this.person = person;
        this.id = id;
    }

    public String getFbrId() {
        return fbrId;
    }

    public void setFbrId(String fbrId) {
        this.fbrId = fbrId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getPerson() {
        return person;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
