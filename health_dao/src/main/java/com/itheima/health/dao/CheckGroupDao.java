package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    void addCheckGroupCheckItem(@Param("checkGroupId") Integer id, @Param("checkitemId") Integer checkitemId);

    /**
     * 按条件查找
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

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
     */
    void update(CheckGroup checkGroup);


    /**
     * 根据检查组id删除关联的检查项
     * @param id
     */
    void deleteCheckGroupCheckItemByCheckGroupId(Integer id);


    /**
     * 根据检查组id查找该检查组是否被套餐使用
     * @param id
     * @return
     */
    int findCountSetMealCheckGroupByCheckGroupId(int id);


    /**
     * 根据id删除检查组
     * @param id
     */
    void deleteById(int id);

    List<CheckGroup> findAll();
}
