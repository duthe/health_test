package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;


    /**
     * 批量添加预约数据
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void batchAdd(List<OrderSetting> orderSettingList) {

        /**
         * 遍历集合设置每条预约信息
         */
        for (OrderSetting orderSetting : orderSettingList) {
            updateOrderSetting(orderSetting);

        }

    }


    /**
     * 根据日期设置当天预约信息
     * @param orderSetting
     */
    @Override
    public void updateOrderSetting(OrderSetting orderSetting) {
        //根据日期查询预约信息
        OrderSetting dbOrderSetting = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if (dbOrderSetting != null) {
            //如果数据库有此日期预约数据 可预约人数不能小于已预约人数
            if (orderSetting.getNumber() < dbOrderSetting.getReservations()) {
                throw new HealthException(orderSetting.getOrderDate() + "中可预约人数不能小于已预约人数");
            }
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        } else {
            //不存在此日期预约数据 则添加此条数据
            orderSettingDao.add(orderSetting);
        }
    }

    /**
     * 根据月份获取预约信息
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> findOrderSettingByMonth(String month) {
        month += "-%";
        return orderSettingDao.findOrderSettingByMonth(month);
    }



}
