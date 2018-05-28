package com.example.jingbiaozhen.sign_in.utils;
/*
 * 访问服务器的地址
 **/

public interface Constants {
    String BASE_URL="http://111.231.98.192:9000/signer/";
    String USER_LOGIN=BASE_URL+"student/login";//登录
    String USER_SIGN=BASE_URL+"sign/addSign";//签到
    String UPLOAD_LEAVE_INFO=BASE_URL+"holiday/addHoliday";//上传请假信息
    String QUERY_SIGN_INFO=BASE_URL+"sign/querySign";//查询签到信息
    String QUERY_LEAVE_LIST=BASE_URL+"holiday/queryHoliday";//查询请假列表
    String QUERY_SCORE_LIST=BASE_URL+"score/queryScore";//查询成绩列表
    String QUERY_COURSE=BASE_URL+"course/queryCourse";//查询成绩列表

    String CODE_OK="0";
}
