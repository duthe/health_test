package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetMealDao {
    /**
     * 根据条件查询套餐
     * @param queryString
     * @return
     */
    Page findCondition(String queryString);

    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐和检查组关联信息
     * @param id
     * @param checkGroupId
     */
    void addSetMealCheckGroup(@Param("setmeal_id") Integer id, @Param("checkgroup_id")Integer checkGroupId);

    /**
     * 根据id查找套餐
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 添加套餐和检查组的关联
     * @param setMeaId
     * @return
     */
    List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId);

    /**
     * 更新套餐
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 根据套餐id删除关联的检查组
     * @param id
     */
    void deleteSetMealCheckGroupBySetMealId(Integer id);

    /**
     * 根据套餐id查询是否被订单使用
     * @param id
     * @return
     */
    int findCountOrderBySetMealId(int id);

    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 获取数据库中存储的所有图片名字 删除垃圾图片使用
     * @return
     */
    List<String> findImgs();


    /**
     * 查询所有套餐信息
     * @return
     */
    List<Setmeal> findAll();


    /**
     * 根据套餐id查询套餐详细信息 包括检查组 检查项
     * @return
     */
    Setmeal findSetMealDetailById(Integer id);
}
