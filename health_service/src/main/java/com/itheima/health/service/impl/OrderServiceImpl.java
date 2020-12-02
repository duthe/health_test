package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAEncTSEnc;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 提交预约
     * @param paramMap
     * @return
     */
    @Override
    @Transactional
    public Integer submit(Map<String, String> paramMap) {
        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date(paramMap.get("orderDate"));
        } catch (Exception e) {
            throw new HealthException("预约日期格式不正确！");
        }

        //根据预约日期查询当天的预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);

        if (orderSetting == null) {
            //没有当天预约设置 不可预约
            throw new HealthException("当前选择日期不能预约,请选择其他日期");
        }

        //有当天预约设置 判断是否预约满
        if (orderSetting.getNumber() <= orderSetting.getReservations()) {
//            预约已满
            throw new HealthException("当前选择日期已经预约满，请选择其他日期");
        }

        //设置订单信息 Order实体类与数据库属性对应不上 ！！
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(paramMap.get("setmealId")));

        //根据手机号查询会员
        Member member = memberDao.findByTelephone(paramMap.get("telephone"));
        if (member != null) {
            //存在该会员 判断是否当天已经预约相同套餐
            order.setMemberId(member.getId());
            //根据会员id 套餐id 预约日期查询订单
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList !=null && orderList.size() > 0) {
                throw new HealthException("当前选择日期已经预约该套餐，请勿重复预约！");
            }
        } else {
            //会员不存在则注册
            String idCard = paramMap.get("idCard");
            member = new Member();
            member.setName(paramMap.get("name"));
            member.setSex(paramMap.get("sex"));
            member.setIdCard(idCard);
            member.setPhoneNumber(paramMap.get("telephone"));
            member.setRegTime(new Date());
            member.setPassword(idCard.substring(idCard.length() - 6));
            member.setRemark("微信预约注册");
            memberDao.add(member);
            order.setMemberId(member.getId());
        }

        //更新预约人数 并发 利用数据库行锁 where number > reservations
        int row = orderSettingDao.updateReservationsByOrderDate(orderSetting);

        if (row == 0) {
            //更新失败 预约已满
            throw new HealthException("当前选择日期已经没有名额，请选择其他日期");
        }

        //预约人数更新成功 保存预约订单

        order.setOrderStatus("未到诊");
        order.setOrderType(paramMap.get("orderType"));
        orderDao.add(order);
        return order.getId();
    }

    /**
     * 根据预约id查询预约信息
     * @param id 预约订单id
     * @return 包含预约人姓名 套餐名称 预约日期 预约类型
     */
    @Override
    public Map<String, Object> findDetailById(int id) {
        return orderDao.findDetailById(id);
    }
}
