package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findByPage(QueryPageBean queryPageBean);

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    Integer add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 根据套餐id查询该套餐关联的检查组
     * @param setMeaId
     * @return
     */
    List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId);

    /**
     * 跟新套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据id删除套餐
     * @param id
     * @throws HealthException
     */
    void deleteById(int id) throws HealthException;

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
     * 根据套餐id查询套餐详细信息  包括检查组 检查项
     * @param id
     * @return
     */
    Setmeal findSetMealDetailById(Integer id);

    /**
     * 获取套餐占比集合 包含套餐名字 和预约占比
     * @return
     */
    List<Map<String, Object>> getSetMealReport();
}
