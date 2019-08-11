package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map; /**
 * 套餐接口
 */
public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 往套餐和检查组中间表插入数据
     * @param map
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);
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
