package com.example.telemarket.bean;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-17
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class ProductInfo implements Serializable{
    private String productName;
    private String productSum;   //保额
    private String cookieTime;   //有效销售期
    private String productId;   //产品id
    private String productNum;   //产品编号
    private String productType;   //产品类型
    private String productInstrument;   //说明
    private String productDeadLine;   //保险期限
    private String productPhoto;   //宣传册

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductInstrument() {
        return productInstrument;
    }

    public void setProductInstrument(String productInstrument) {
        this.productInstrument = productInstrument;
    }

    public String getProductDeadLine() {
        return productDeadLine;
    }

    public void setProductDeadLine(String productDeadLine) {
        this.productDeadLine = productDeadLine;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSum() {
        return productSum;
    }

    public void setProductSum(String productSum) {
        this.productSum = productSum;
    }

    public String getCookieTime() {
        return cookieTime;
    }

    public void setCookieTime(String cookieTime) {
        this.cookieTime = cookieTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
