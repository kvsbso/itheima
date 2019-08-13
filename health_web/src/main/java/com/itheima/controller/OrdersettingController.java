package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 检查组控制层管理
 */
@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrdersettingService ordersettingService;


    /**
     * 添加预约
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            //解析excel
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //将list换成list<ordersetting>
            if (strings != null && strings.size() > 0) {
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] row : strings) {
                    String date = row[0];
                    String number = row[1];
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(date));
                    orderSetting.setNumber(Integer.parseInt(number));
                    orderSettings.add(orderSetting);
                }
                ordersettingService.add(orderSettings);
            }
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /* *//**
     * 检查组分页查询
     * @return
     *//*
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            Result result = checkGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }*/

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
                List<Map> maps = ordersettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            ordersettingService.editNumberByDate(orderSetting.getOrderDate(),orderSetting.getNumber());
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
