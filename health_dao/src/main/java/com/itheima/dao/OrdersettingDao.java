package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingDao {

    OrderSetting findByOrderDate(String orderDate);

    //查询是否已存在日期
    int findCountByOrderDate(Date orderDate);

    //修改已存在日期
    void editNumberByOrderDate(OrderSetting orderSetting);

    //
    void add(OrderSetting orderSetting);

    /**
     * xxx
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String,String> map);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
