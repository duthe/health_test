package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    /**
     * 根据日期查询预约数据
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 根据日期更新可预约人数
     * @param orderSetting
     */
    void updateNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 添加一条预约设置信息
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据月份获取预约信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> findOrderSettingByMonth(String month);


    /**
     * 根据日期更新已预约人数
     * @param orderSetting 预约设置信息
     * @return 受影响的行数
     */
    int updateReservationsByOrderDate(OrderSetting orderSetting);
}
