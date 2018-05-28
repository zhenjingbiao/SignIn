package com.example.jingbiaozhen.sign_in.utils;
/*
 * Created by jingbiaozhen on 2018/5/24.
 **/

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtils
{

    public static String getCurrentTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(System.currentTimeMillis());
    }

    public static String getCurrentData(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }

    public static long getTimeDifference(String givenTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try
        {
            long currentTime = System.currentTimeMillis();
            long setTime = df.parse(givenTime).getTime();
            time = setTime - currentTime;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return time;
    }


    /**
     *
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + "天," + hours + "： " + minutes + "："
                + seconds + "： ";
    }
}
