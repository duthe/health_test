package com.itheima.health.service;

import com.itheima.health.exception.HealthException;

import java.util.Map;


public interface OrderService {

    /**
     * 提交预约
     * @param paramMap
     * @return
     * @throws HealthException
     */
    Integer submit(Map<String, String> paramMap) throws HealthException;

    /**
     * 根据预约id查询预约信息
     * @param id 预约订单id
     * @return 包含预约人姓名 套餐名称 预约日期 预约类型
     */
    Map<String, Object> findDetailById(int id);
}
