package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

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
     * 根据条件查找检查项
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 根据id查找检查项
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 跟新检查项
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 根据检查项id查找检查项是否被检查组使用
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 根据id删除检查项
     * @param id
     */
    void deleteById(int id);
}
