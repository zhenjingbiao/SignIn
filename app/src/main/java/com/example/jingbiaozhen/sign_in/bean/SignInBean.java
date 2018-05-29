package com.example.jingbiaozhen.sign_in.bean;
/*
 * Created by jingbiaozhen on 2018/5/24.
 **/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SignInBean implements Serializable
{

    public List<Seat> signedList ;// 已签到座位列表

    public List<Seat> getInitSignList()
    {
        List<Seat>initSignList=new ArrayList<>(150);
        for (int i = 0; i < 150; i++)
        {
            Seat seat = new Seat();
            seat.location = i;
            seat.isSigned = false;
            initSignList.add(seat);
        }
        return initSignList;
    }

    public class Seat
    {
        public int location;

        public boolean isSigned;
    }

    @Override
    public String toString() {
        return "SignInBean{" +
                ", signedList=" + signedList +
                '}';
    }
}
