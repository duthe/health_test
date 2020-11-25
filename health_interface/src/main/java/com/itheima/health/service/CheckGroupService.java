package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {


    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findByPage(QueryPageBean queryPageBean);

    /**
     * 根据id查找检查组
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 根据检查组id查找关联的检查项
     * @param checkGroupId
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    /**
     * 更新检查组信息
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 根据id删除检查组
     * @param id
     * @throws HealthException
     */
    void deleteById(int id) throws HealthException;

    /**
     * 查找所有检查组
     * @return
     */
    List<CheckGroup> findAll();

}
