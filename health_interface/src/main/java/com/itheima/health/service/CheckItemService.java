package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    /**
     * 查找所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查找检查项
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findByPage(QueryPageBean queryPageBean);

    /**
     * 根据id查找检查项
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新检查项
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 根据id删除检查项
     * @param id
     * @throws HealthException
     */
    void deleteById(int id) throws HealthException;
}
