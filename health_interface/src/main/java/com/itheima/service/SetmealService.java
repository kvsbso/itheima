package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐服务接口
 */
public interface SetmealService {
    /**
     * 新增套餐
     * @param setmeal 套餐对象
     * @param checkgroupIds 检查组ids
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    Result findPage(String queryString, Integer currentPage, Integer pageSize);

    /**
     * 查询所有套餐列表数据
     * @return
     */
    List<Setmeal> findAll();
    /**
     * 根据套餐id查询（套餐数据+检查组数据+检查项数据）
     *
     */
    Setmeal findById(Integer id);

    /**
     * 查询套餐以及套餐预约数量 List<Map<String,Object>>
     * @return
     */
    List<Map<String,Object>> getSetmealReport();
}
