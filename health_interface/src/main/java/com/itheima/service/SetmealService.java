package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    /**
     * 增加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 套餐分页查询
     * @return
     */
    Result findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

    //根据用户名查询权限表
    Integer queryJurisdictionByUser(String username);

    //根据权限id查询显示列表
    List<Map> JurisdictionById(Integer roleId);

    //查询会员男女占比
    List<Map> getSetmealReportMembership();

    //查询对应年龄段的会员人数
    List<Map> getSetmealReportMembershipByAge();
}
