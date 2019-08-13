package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrdersettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersettingDao ordersettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result submitOrder(Map map) throws Exception{
        //获取预约时间
        String orderDate = (String) map.get("orderDate");
        String telephone = (String) map.get("telephone");
        String name = (String) map.get("name");
        String sex = (String) map.get("sex");
        String idCard = (String) map.get("idCard");
        String orderType = (String)map.get("orderType");
        Integer setmealId = Integer.parseInt((String)map.get("orderType"));
        Date date = DateUtils.parseString2Date(orderDate);
        //判断用户预约的时间是否可预约
        OrderSetting orderSetting = ordersettingDao.findByOrderDate(orderDate);
        //不可用预约
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //可以预约
        int number = orderSetting.getNumber();
        //已经预约
        int reservations = orderSetting.getReservations();
        if (reservations >= number){
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断是否是会员
        Member member = memberDao.findByTelephone(telephone);
        Integer memberId = null;
        if (member != null) {
            memberId = member.getId();
        }else {
            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
            memberId = member.getId();
        }

        Order order = new Order(memberId, date, orderType, "待体检", setmealId);

        //4.1  预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        ordersettingDao.editReservationsByOrderDate(orderSetting);

        orderDao.add(order);
        return new Result(false, MessageConstant.ORDER_SUCCESS,order);
    }
}
