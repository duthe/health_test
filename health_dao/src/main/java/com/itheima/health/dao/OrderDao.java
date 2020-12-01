package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

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


    /**
     * 获取指定日期当天预约数
     * @param orderDate
     * @return
     */
    Integer findOrderNumberByDate(String orderDate);

    /**
     * 获取指定日期当天到诊数
     * @param orderDate
     * @return
     */
    Integer findVisitsNumberByDate(String orderDate);

    /**
     * 获取指定日期之间的预约数 包含
     * @param startDate
     * @param endDate
     * @return
     */
    Integer findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取指定日期之后的到诊数 包含
     * @param orderDate
     * @return
     */
    Integer findVisitsCountAfterDate(String orderDate);

    /**
     * 获取前5条热门套餐
     * @return
     */
    List<Map<String, Object>> findHotSetMeal();
}
