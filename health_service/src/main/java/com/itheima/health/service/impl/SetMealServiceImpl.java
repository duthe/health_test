package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetMealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;



    @Override
    public PageResult<Setmeal> findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (StringUtil.isNotEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page page = setMealDao.findCondition(queryPageBean.getQueryString());

        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.add(setmeal);
        if (checkGroupIds != null) {
            for (Integer checkGroupId : checkGroupIds) {
                setMealDao.addSetMealCheckGroup(setmeal.getId() ,checkGroupId);
            }
        }
    }

    @Override
    public Setmeal findById(int id) {
        return setMealDao.findById(id);
    }

    @Override
    public List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId) {
        return setMealDao.findSetMealCheckGroupBySetMeaId(setMeaId);
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.update(setmeal);


        setMealDao.deleteSetMealCheckGroupBySetMealId(setmeal.getId());

        if (checkGroupIds != null) {
            for (Integer checkGroupId : checkGroupIds) {
                setMealDao.addSetMealCheckGroup(setmeal.getId() ,checkGroupId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
//        检查该套餐是否被订单使用 如果被使用则不能删除
        int count = setMealDao.findCountOrderBySetMealId(id);
        if (count>0) {
            throw new HealthException("该套餐被订单使用，不能删除！");
        }

        setMealDao.deleteSetMealCheckGroupBySetMealId(id);
        setMealDao.deleteById(id);
    }

    @Override
    public List<String> findImgs() {
        return setMealDao.findImgs();
    }


}
