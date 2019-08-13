package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Member;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    /**
     * //新增套餐
     *
     * @param setmeal
     */
    void add(Setmeal setmeal);

    //绑定套餐和检查组的多对多关系
    void setSetmealAndCheckGroup(Map<String, Integer> map);

    /**
     * 套餐分页
     *
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

    Integer queryJurisdictionByUser(String username);

    List<Menu> JurisdictionById(Integer roleId);

    List<Map> getSetmealReportMembership();

    int getSetmealReportMembershipByAge(@Param("key_x") int x,@Param("key_x1")int x1);

    int getSetmealReportMembershipByAgemax(int x);

    List<Member> getSetmealReportMembershipByAge1();

}
