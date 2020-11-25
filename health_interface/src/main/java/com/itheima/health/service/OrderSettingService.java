package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {


    /**
     * 批量添加预约数据
     * @param orderSettingList
     * @throws HealthException
     */
    void batchAdd(List<OrderSetting> orderSettingList) throws HealthException;


    /**
     * 根据月份获取预约信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> findOrderSettingByMonth(String month);


    /**
     * 根据日期设置当天预约信息
     * @param orderSetting
     */
    void updateOrderSetting(OrderSetting orderSetting) throws HealthException;
}
