package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员业务层实现类
 */
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
    public void add(Member member) {
        memberDao.add(member);
    }
    /**
     * 获取会员折线图数据
     * @return
     */
    @Override
    public Map getMemberReport() {
        Map<String,Object> map = new HashMap<>();
        //1.获取年月数据
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//减少12个月
        List<String> stringList = new ArrayList<>();
        for(int i = 1;i<=12;i++){
            stringList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            calendar.add(Calendar.MONTH,+1);
        }
        map.put("months",stringList);

        //2.获取年月对应数量  select count(*) from t_member where regTime(2018-07-03) <=  2018-07-31
        List<Integer> listCount = new ArrayList<>();
        for (String ym : stringList) {
            String newYM = ym + "-31";//2018-07-31
            Integer memberCount = memberDao.findMemberCountBeforeDate(newYM);
            listCount.add(memberCount);
        }
        map.put("memberCount",listCount);
        return map;
    }


    public static void main(String[] args) {


    }
}
