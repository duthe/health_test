package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface SetMealService {

    PageResult<Setmeal> findByPage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    Setmeal findById(int id);

    List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(int id) throws HealthException;
}
