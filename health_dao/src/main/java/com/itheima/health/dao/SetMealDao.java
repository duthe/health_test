package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetMealDao {
    Page findCondition(String queryString);

    void add(Setmeal setmeal);

    void addSetMealCheckGroup(@Param("setmeal_id") Integer id, @Param("checkgroup_id")Integer checkGroupId);

    Setmeal findById(int id);

    List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId);

    void update(Setmeal setmeal);

    void deleteSetMealCheckGroupBySetMealId(Integer id);

    int findCountOrderBySetMealId(int id);

    void deleteById(int id);

    List<String> findImgs();
}
