package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表服务接口
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 获取运营数据统计报表（会员数据 预约数据 热门套餐数据）
     * @return
     */
    @Override
    public Map getBusinessReportData() throws Exception {
        Map map = new HashMap();

        Date today = DateUtils.getToday();//
        String reportDate = DateUtils.parseDate2String(today, "yyyy-MM-dd");//当天时间 2019-08-08
        //会员数据统计
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);

        //获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期
        String firstDay4ThisMonth =
                DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
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
        //预约数据统计
        //热门套餐
        List<Map> hotSetmeal =orderDao.findHotSetmeal();
        //统一返回结果
        map.put("reportDate",reportDate);//报表日志
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);//热门套餐
        return map;
    }
}
