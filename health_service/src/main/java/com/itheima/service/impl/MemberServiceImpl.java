package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import com.itheima.utils.MonthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member1) {
        if (member1.getPassword() != null) {
            member1.setPassword(MD5Utils.md5(member1.getPassword()));
        }
        memberDao.add(member1);
    }

    @Override
    public Map<String, Object> getMemberReport() {
        //获取当前时间12个月之前的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);

        //获取每月会员数量
        List<Integer> integerList = new ArrayList<>();
        for (String ym : list) {
            String newym = ym + "-31";
            Integer memberCount = memberDao.findMemberCountBeforeDate(newym);
            integerList.add(memberCount);
        }
        map.put("count",integerList );

        return map;
    }



    //动态展示选中月份会员折线图
    @Override
    public Map<String, Object> countUserDynamic(String startDate, String endDate) {
        List<String> list = MonthUtils.getRangeSet(startDate, endDate);
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);


        //获取每月会员数量
        List<Integer> integerList = new ArrayList<>();
        for (String ym : list) {
            Integer memberCount = memberDao.findMemberCountBeforeDate1(ym);
            integerList.add(memberCount);
        }
        map.put("count",integerList );

        return map;
    }



}
