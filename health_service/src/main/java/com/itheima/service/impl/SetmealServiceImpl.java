package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckItemService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Transactional
@Service(interfaceClass = SetmealService.class)//指定创建服务的包
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 增加套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐数据
        setmealDao.add(setmeal);
        //获取主键id,调用方法往中间表中添加数据
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }

    }

    /**
     * 套餐分页查询
     *
     * @return
     */
    @Override
    public Result findPage(Integer currentPage, Integer pageSize, String queryString) {
        //传入两个参数,第一个为当前页,第二个为每页显示总条数
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> Setmeal = setmealDao.selectByCondition(queryString);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, new PageResult(Setmeal.getTotal(), Setmeal.getResult()));
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //根据用户名查询权限表
    @Override
    public Integer queryJurisdictionByUser(String username) {
        return setmealDao.queryJurisdictionByUser(username);
    }

    //根据权限id查询显示列表
    @Override
    public List<Map> JurisdictionById(Integer roleId) {
        List<Menu> menus = setmealDao.JurisdictionById(roleId);
        List<Map> list = new ArrayList<>();
        for (Menu menu : menus) {
            Integer level = menu.getLevel();
            if (level == 1) {
                Map<String, Object> map = new HashMap<>();
                map.put("path", menu.getPath());
                map.put("title", menu.getName());
                map.put("icon", menu.getIcon());
                map.put("children", menu.getChildren());
                map.put("id", menu.getId());
                list.add(map);
            } else {
                for (Map map : list) {
                    Integer id = (Integer) map.get("id");
                    Integer parentMenuId = menu.getParentMenuId();
                    if (id == parentMenuId) {
                        List<Map> children = (List<Map>) map.get("children");
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("path", menu.getPath());
                        map1.put("title", menu.getName());
                        map1.put("linkUrl", menu.getLinkUrl());
                        map1.put("children", menu.getChildren());
                        children.add(map1);
                    }
                }
            }
        }
        System.out.println(list);
        return list;
    }


    //查询会员男女占比
    @Override
    public List<Map> getSetmealReportMembership() {
        List<Map> maps = setmealDao.getSetmealReportMembership();
        maps.get(0).put("name", "男");
        maps.get(1).put("name", "女");


        return maps;
    }

    //查询对应年龄段的会员人数
    @Override
    public List<Map> getSetmealReportMembershipByAge() {
        List<Map> list = new ArrayList<>();

        List<Member> list1 = setmealDao.getSetmealReportMembershipByAge1();
        List<Integer> ageList = new ArrayList<>();
        for (Member member : list1) {
            Date birthday = member.getBirthday();
            int age = getAge(birthday);
            ageList.add(age);
        }

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        for (Integer integer : ageList) {
            if (integer > 0 && integer <= 18) {
                a++;
            } else if (integer > 18 && integer <= 30) {
                b++;
            } else if (integer > 30 && integer <= 45) {
                c++;
            } else {
                d++;
            }
        }

        Map<String, Object> map = new HashMap<>();
        String name = "name";
        String asd = "0-18岁";
        String value = "value";
        map.put(name, asd);
        map.put(value, a);
        list.add(map);

        Map<String, Object> map1 = new HashMap<>();
        String asd1 = "18-30岁";
        map1.put(name, asd1);
        map1.put(value, b);
        list.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        String asd2 = "30-45岁";
        map2.put(name, asd2);
        map2.put(value, c);
        list.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        String asd3 = "大于45岁";
        map3.put(name, asd3);
        map3.put(value, d);
        list.add(map3);

//        int[] x = {0, 18, 30, 45};
//        for (int i = 0; i < 3; i++) {
//            Map<String, Object> map1 = new HashMap<>();
//            int count = setmealDao.getSetmealReportMembershipByAge(x[i],x[i+1]);
//            String name = "name";
//            String asd = x[i]+"-"+x[i+1]+"岁";
//            String value = "value";
//            map1.put(name, asd);
//            map1.put(value, count);
//            list.add(map1);
//        }
//        int count = setmealDao.getSetmealReportMembershipByAgemax(x[3]);
//        Map<String, Object> map = new HashMap<>();
//        String name = "name";
//        String asd = "大于"+x[3]+"岁";
//        String value = "value";
//        map.put(name, asd);
//        map.put(value, count);
//        list.add(map);

        return list;
    }


    public void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);

        }

    }

    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一

            }
        }
        return age;
    }
}
