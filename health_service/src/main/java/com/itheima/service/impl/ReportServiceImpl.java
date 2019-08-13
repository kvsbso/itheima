package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass =ReportService.class )
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        //{
        //  "data":{
        //  "todayVisitsNumber":0,  今日到诊数
        //  "reportDate":"2019-04-25",
        //  "todayNewMember":0, 新增会员数
        //  "thisWeekVisitsNumber":0, 本周到诊数
        //  "thisMonthNewMember":2,   本月新增会员数
        //  "thisWeekNewMember":0,  本周新增会员数
        //  "totalMember":10, 总会员数
        //  "thisMonthOrderNumber":2, 本月预约数
        //  "thisMonthVisitsNumber":0,  本月到诊数
        //  "todayOrderNumber":0,   今日预约数
        //  "thisWeekOrderNumber":0,  本周预约数
        //  "hotSetmeal":[    热门套餐
        //  {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套餐","setmeal_count":5},
        //  {"proportion":0.1818,"name":"阳光爸妈升级肿瘤12项筛查体检套餐","setmeal_count":2},
        //  {"proportion":0.1818,"name":"珍爱高端升级肿瘤12项筛查","setmeal_count":2},
        //  {"proportion":0.0909,"name":"孕前检查套餐","setmeal_count":1}
        //                  ],
        //}


        //会员数据统计
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday(), "yyyy-MM-dd");

        //获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //新增会员数
        Integer todayNewMember=memberDao.findMemberCountByDate(reportDate);
        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //预约数据统计
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",reportDate);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}
