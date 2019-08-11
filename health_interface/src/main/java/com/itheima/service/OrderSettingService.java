package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务接口
 */
public interface OrderSettingService {
    /**
     * 批量预约设置
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 日历组件显示数据 (根据月份获取预约设置数据)
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 单个预约设置
     * @param orderDate
     * @param number
     */
    void editNumberByDate(Date orderDate, int number);
}
