package com.example.jingbiaozhen.sign_in.utils;
/*
 * 访问服务器的地址
 **/

public interface Constants {
    String BASE_URL="http://111.231.98.192:8084/Manager2/";
    String USER_LOGIN=BASE_URL+"user/login";//登录
    String USER_SIGN=BASE_URL+"";//查询签到
    String UPDATE_SIGN=BASE_URL+"";//上传签到信息
    String UPLOAD_LEAVE_INFO=BASE_URL+"";//上传请假信息
    String QUERY_LEAVE_LIST=BASE_URL+"";//查询请假列表
    String QUERY_SCORE_LIST=BASE_URL+"";//查询成绩列表

    String CODE_OK="0";
}
