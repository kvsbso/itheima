package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result submitOrder(Map map)throws Exception;

    /**
     * 根据order表主键id查询预约成功信息
     * @param id
     * @return
     */
    Map findById(Integer id);
}
