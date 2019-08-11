package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理控制层
 */
@RestController
@RequestMapping("/setmeal2")
public class Setmeal2Controller {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐列表数据
     */
    @RequestMapping(value = "/getSetmeal",method = RequestMethod.POST)
    public Result getSetmeal(){
        List<Setmeal> list = setmealService.findAll();
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
    }

    /**
     * 根据套餐id查询（套餐数据+检查组数据+检查项数据）
     * 
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(Integer id){
       Setmeal setmeal = setmealService.findById(id);
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
    }
}
