package com.example.jingbiaozhen.sign_in.bean;
/*
 * Created by jingbiaozhen on 2018/5/24.
 **/

import java.io.Serializable;
import java.util.List;

public class SignInBean implements Serializable
{
    public String courseId;

    public String courseName;

    public String deadline;// 截至签到时间

    public boolean isSigned;// 是否已经签到

    public List<Seat> signedList;// 已签到座位列表

    public class Seat
    {
        public int location;

        public boolean isSigned;
    }

}
