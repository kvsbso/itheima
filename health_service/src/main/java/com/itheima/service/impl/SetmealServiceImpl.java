package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐实现类
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 新增套餐
     * @param setmeal 套餐对象
     * @param checkgroupIds 检查组ids
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.保存套餐
        setmealDao.add(setmeal);
        //2.获取套餐id
        Integer setmealId = setmeal.getId();
        //3.往中间表插入数据 提取一个公共方法
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //完成新增后清空redis缓存
        jedisPool.getResource().del("SetmealCache");
    }

    @Override
    public Result findPage(String queryString, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> setmealPage=setmealDao.selectByCondition(queryString);//返回结果的 插件定义Page<T>
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, new PageResult(setmealPage.getTotal(), setmealPage.getResult()));
    }

    /**
     * 查询所有套餐列表数据
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = new ArrayList<>();

        Jedis jedis = jedisPool.getResource();
        //从redis中获取缓存数据
        String SetmealCacheStr = jedis.get("SetmealAllCache");
        if(SetmealCacheStr == null || SetmealCacheStr == ""){
            //如果没有找到缓存就查询数据库，并将结果保存至redis
            list = setmealDao.findAll();
            SetmealCacheStr = JSON.toJSONString(list);
            jedis.set("SetmealCache",SetmealCacheStr);
        }else {
            list = (List<Setmeal>)JSON.parse(SetmealCacheStr);
        }
        return list;
    }
    /**
     * 根据套餐id查询（套餐数据+检查组数据+检查项数据）
     *
     */
    @Override
    public Setmeal findById(Integer id) {
        //方式一：代码方式
        //1.根据套餐id查询套餐数据
        //2.根据套餐id查询中间表检查组ids
        //3.根据检查组ids查询组数据
        //4.根据检查组id查询中间表检查项ids
        //5.根据检查项ids查询检查项数据
        //6.将将检查项数据set方法设置到检查组中
        //7.将检查组设置到套餐对象中

        //方式二：通过映射文件配置方式来实现多对多的查询（目前推荐此方式）

        Setmeal setmeal = new Setmeal();
        String rediskey = "Setmeal?id=" + id + "Cache";
        Jedis jedis = jedisPool.getResource();
        String SetmealCacheStr = jedis.get(rediskey);
        if(SetmealCacheStr == null || SetmealCacheStr == ""){
            setmeal = setmealDao.findById(id);
            jedis.set(rediskey,JSON.toJSONString(setmeal));
        }else {
            setmeal = (Setmeal)JSON.parse(SetmealCacheStr);
        }
        return setmeal;
    }

    /**
     * 查询套餐以及套餐预约数量 List<Map<String,Object>>
     * @return
     */
    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    /**
     * 公共方法
     */
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds!= null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
