package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingService {
    /**
     * 添加预约
     * @param orderSettings
     */
    void add(List<OrderSetting> orderSettings);


    List<Map> getOrderSettingByMonth(String date);

    /**
     * 单个预约设置
     */
    void editNumberByDate(Date orderDate, int number);
}
