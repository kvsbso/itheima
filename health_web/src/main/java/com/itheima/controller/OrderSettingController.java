package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置控制层管理
 * 批量预约设置（通过导入Excel 解析插入数据库）
 * 单个预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    /**
     * 批量预约设置
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile){
        try {
            //解析Excel得到所有数据
            List<String[]> listStr = POIUtils.readExcel(excelFile);
            //将List<String[]>转成List<OrderSetting>
            if(listStr != null && listStr.size()>0){
                List<OrderSetting> orderSettingList =new ArrayList<>();
                for (String[] row : listStr) {
                    String date = row[0];
                    String number = row[1];
                    //每一次都是新的对象
                    OrderSetting orderSetting = new OrderSetting();
                    //将date 和number==>OrderSetting
                    orderSetting.setOrderDate(new Date(date));//预约日期
                    orderSetting.setNumber(Integer.parseInt(number));//可预约人数
                    orderSettingList.add(orderSetting);
                }
                //调用service服务
                orderSettingService.add(orderSettingList);
            }
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);//实际存储到七牛云文件名
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 日历组件显示数据 (根据月份获取预约设置数据)
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> mapList = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);//实际存储到七牛云文件名
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 单个预约设置
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting.getOrderDate(),orderSetting.getNumber());
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
