package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result submitOrder(Map map) throws Exception;
}
