package com.example.jingbiaozhen.sign_in.bean;
/*
 * Created by jingbiaozhen on 2018/5/21.
 **/

import org.json.JSONArray;

import java.io.Serializable;

public class BaseLocalModel implements Serializable
{
    public static final String CODE_SUCCESS = "0";

    public String errorNo;

    public String errorInfo;

    public String exceptionInfo;

    public JSONArray data;

    public boolean isSucess()
    {
        return CODE_SUCCESS.equals(errorNo);
    }

    @Override
    public String toString() {
        return "BaseLocalModel{" +
                "errorNo='" + errorNo + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", exceptionInfo='" + exceptionInfo + '\'' +
                ", data=" + data +
                '}';
    }
}
