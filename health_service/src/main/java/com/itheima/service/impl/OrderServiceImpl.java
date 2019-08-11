package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约体检业务处理层
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     *
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map)throws Exception {
        //获取预约时间
        String orderDate = (String)map.get("orderDate");
        String telephone = (String)map.get("telephone");
        String name = (String)map.get("name");//姓名
        String sex = (String)map.get("sex");//性别
        String orderType = (String)map.get("orderType");//预约类型
        String idCard = (String)map.get("idCard");//身份证
        Integer setmealId = Integer.parseInt((String)map.get("setmealId"));

        //将用户选择的日期转date 设置到预约对象中
        Date tableOrderDate = DateUtils.parseString2Date(orderDate);
        System.out.println("业务处理成功了。。。。");
        //判断用户选择时间是否可以进行预约  条件：预约时间 查询哪个表？order_settting
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //不可以预约
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //获取已经预约人数
        int number = orderSetting.getNumber();
        //获取可预约人数
        int reservations = orderSetting.getReservations();
        //可以预约 查看是否已经约满，已经预约人数>=可预约人数 返回错误信息
        if(reservations >=number){
            //不可以预约 返回错误信息
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断是否是会员 条件：手机号码  查询哪个表：会员表
        Member member = memberDao.findByTelephone(telephone);
        Integer memberId = null;
        if(member != null){
            //是会员获取会员id
            memberId = member.getId();
        }else
        {
            //不是会员自动注册成功会员
            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
            memberId = member.getId();
        }
        //需求：重复预约 条件：会员id 套餐id 预约时间 查询哪个表order预约表
        Order orderSel = new Order();
        orderSel.setMemberId(memberId);
        orderSel.setSetmealId(setmealId);
        orderSel.setOrderDate(tableOrderDate);
        List<Order> byCondition = orderDao.findByCondition(orderSel);
        if(byCondition != null && byCondition.size()>0){
            return new Result(false, MessageConstant.HAS_ORDERED);
        }
        //能够获取到预约设置表中需要的会员id
        //将预约的数据保存预约体检表中t_order表
        Order order = new Order(memberId,tableOrderDate,orderType,"待体检",setmealId);
        orderDao.add(order);
        //预约成功后预约数量+1 条件：预约日期  修改哪个表order_settting 哪个字段：已经预约数量
        orderSetting.setReservations(orderSetting.getReservations()+1);//为已经预约数量+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据order表主键id查询预约成功信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
