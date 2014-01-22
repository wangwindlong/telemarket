package com.example.telemarket.bean.user;

import java.io.Serializable;

public class PersistenceObject implements Serializable {
    // // 数据库主键
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
