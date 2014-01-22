package com.example.telemarket.bean;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-17
 * Time: 上午9:17
 * To change this template use File | Settings | File Templates.
 */
public class ModelResult<T> implements Serializable {
    private boolean returnResult;//返回结果
    private JSONObject jsonObject;//正确返回的获取的json数据
    private int code = 0;//服务器返回的结果类型
    //200 请求成功  400 请求错误  401 为授权 403 禁止访问  404 文件未找到  500  服务器错误
    private String requestClass;// 请求的类，用于log日志
    private T persisObject;  //返回一个对象
    private List<T> persisObjectList;

    public boolean isReturnResult() {
        return returnResult;
    }

    public void setReturnResult(boolean returnResult) {
        this.returnResult = returnResult;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(String requestClass) {
        this.requestClass = requestClass;
    }

    public T getPersisObject() {
        return persisObject;
    }

    public void setPersisObject(T persisObject) {
        this.persisObject = persisObject;
    }

    public List<T> getPersisObjectList(Context context) {
        if (persisObjectList.size() < 1) {
            Toast.makeText(context, "没有相关记录", Toast.LENGTH_SHORT).show();
        }
        return persisObjectList;
    }

    public void setPersisObjectList(List<T> persisObjectList) {
        this.persisObjectList = persisObjectList;
    }

}
