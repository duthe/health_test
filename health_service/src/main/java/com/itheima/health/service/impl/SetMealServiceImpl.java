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


    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (StringUtil.isNotEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page page = setMealDao.findCondition(queryPageBean.getQueryString());

        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    /**
     * 添加套餐
     * @param setmeal
     * @param checkGroupIds
     */
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

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setMealDao.findById(id);
    }

    /**
     * 根据套餐id查询该套餐关联的检查组
     * @param setMeaId
     * @return
     */
    @Override
    public List<Integer> findSetMealCheckGroupBySetMeaId(int setMeaId) {
        return setMealDao.findSetMealCheckGroupBySetMeaId(setMeaId);
    }

    /**
     * 更新套餐信息
     * @param setmeal
     * @param checkGroupIds
     */
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

    /**
     * 根据id删除套餐
     * @param id
     * @throws HealthException
     */
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

    /**
     * 获取数据库中存储的所有图片名字 删除垃圾图片使用
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setMealDao.findImgs();
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    /**
     * 根据套餐id查询套餐详细信息 包括检查组 检查项
     * @param id
     * @return
     */
    @Override
    public Setmeal findSetMealDetailById(Integer id) {
        return setMealDao.findSetMealDetailById(id);
    }


}
