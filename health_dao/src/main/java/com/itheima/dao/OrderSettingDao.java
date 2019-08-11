package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 预约设置接口
 */
public interface OrderSettingDao {
    /**
     * 根据预约日期查询是否已经设置预约
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期修改预约人数number
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 新增预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据起始时间和结束时间查询当前月份预约设置所有数据
     * @param parmMap
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> parmMap);

    /**
     * 根据预约日期查询预约数据
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(String orderDate);

    /**
     * 根据预约日期修改已经预约数量 每次+1
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
