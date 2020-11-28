package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    /**
     * 根据条件查询预约订单信息
     * @param order 查询条件
     * @return  符和条件的订单集合
     */
    List<Order> findByCondition(Order order);

    /**
     * 添加预约订单
     * @param order 预约订单信息
     */
    void add(Order order);

    /**
     * 根据预约id查询预约信息
     * @param id 预约订单id
     * @return 包含预约人姓名 套餐名称 预约日期 预约类型
     */
    Map<String, String> findDetailById(int id);
}
