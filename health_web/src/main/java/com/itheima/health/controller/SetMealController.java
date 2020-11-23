package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;


    @RequestMapping("/findByPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean) {
       PageResult<Setmeal> setMealPageResult = setMealService.findByPage(queryPageBean);
       return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setMealPageResult);
    }


    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        setMealService.add(setmeal, checkgroupIds);
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    @RequestMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setMealService.findById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }


    @RequestMapping("/findSetMealCheckGroupBySetMeaId")
    public Result findSetMealCheckGroupBySetMeaId(int setMeaId) {
        List<Integer> integers = setMealService.findSetMealCheckGroupBySetMeaId(setMeaId);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, integers);
    }


    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        setMealService.update(setmeal, checkgroupIds);
        return new Result(true, "编辑套餐成功");
    }


    @RequestMapping("/deleteById")
    public Result deleteById(int id) {
        setMealService.deleteById(id);
        return new Result(true, "删除套餐成功！");
    }




}
