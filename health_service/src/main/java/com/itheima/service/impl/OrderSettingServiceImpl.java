package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预约设置
 */
@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 批量预约设置
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //根据预约日期来判断是否已经设置预约
                /*int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                //如果已经设置预约则进行修改
                if(count>0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //如果没有设置预约调用dao保存数据
                    orderSettingDao.add(orderSetting);
                }*/
                this.orderSettingByOrderDate(orderSetting);
            }
        }
    }

    /**
     * 日历组件显示数据 (根据月份获取预约设置数据)
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //select * from t_order_setting where orderDate between startDate and endDate
        String startDate = date+"-01"; //起始时间
        String endDate = date +"-31";//结束时间
        Map<String,String> parmMap = new HashMap<>();
        parmMap.put("startDate",startDate);
        parmMap.put("endDate",endDate);
        //根据起始时间和结束时间查询当前月份预约设置所有数据
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(parmMap);
        List<Map> listRsMap = new ArrayList<>();///定义list<Map>结构 跟 页面保持一致
        if(orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("date",orderSetting.getOrderDate().getDate());//.getDate()当前月几号 getDay()当前周第几天
                rsMap.put("number",orderSetting.getNumber());//可预约人数
                rsMap.put("reservations",orderSetting.getReservations());//已经预约人数
                listRsMap.add(rsMap);
            }
        }
        return listRsMap;
    }

    /**
     * 单个预约设置
     * @param orderDate
     * @param number
     */
    @Override
    public void editNumberByDate(Date orderDate, int number) {
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setOrderDate(orderDate);
        orderSetting.setNumber(number);
        this.orderSettingByOrderDate(orderSetting);
    }


    /**
     * 预约设置代码抽取
     */
    public void orderSettingByOrderDate(OrderSetting orderSetting){
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        //如果已经设置预约则进行修改
        if(count>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //如果没有设置预约调用dao保存数据
            orderSettingDao.add(orderSetting);
        }
    }
}
