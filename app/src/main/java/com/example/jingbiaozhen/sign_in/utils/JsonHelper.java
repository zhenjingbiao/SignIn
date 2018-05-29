package com.example.jingbiaozhen.sign_in.utils;
/*
 * Created by jingbiaozhen on 2018/5/22.
 **/

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;


public class JsonHelper
{
    public static BaseLocalModel parseJson(String json)
    {
        BaseLocalModel model = new BaseLocalModel();
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            model.errorNo = jsonObject.optString("error_no");
            model.errorInfo = jsonObject.optString("error_info");
            model.data = jsonObject.optJSONArray("data");
        }
        catch (JSONException e)
        {
            model.exceptionInfo = e.getMessage();
        }
        return model;
    }
    public static BaseLocalModel parseJson(String TAG,String json){
        Log.d(TAG, "onResponse: json"+json);
       return parseJson( json);
    }
}
