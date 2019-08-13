package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member1);

    Map<String,Object> getMemberReport();

    //动态展示选中月份会员折线图
    Map<String,Object> countUserDynamic(String startDate, String endDate);


}
