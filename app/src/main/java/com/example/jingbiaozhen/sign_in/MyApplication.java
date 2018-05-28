package com.example.jingbiaozhen.sign_in;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/*
 * Created by jingbiaozhen on 2018/5/28.
 **/

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
}
