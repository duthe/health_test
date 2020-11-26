package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;


    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Setmeal> setMealList = setMealService.findAll();
        setMealList.forEach(setMeal -> {
            //设置图片完整路径
            setMeal.setImg(QiNiuUtils.DOMAIN + setMeal.getImg());
        });
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setMealList);
    }


    /**
     * 根据套餐id查询套餐详细信息  包括检查组 检查项
     * @param id
     * @return
     */
    @RequestMapping("/findSetMealDetailById")
    public Result findSetMealDetailById(Integer id) {
        Setmeal setMeal = setMealService.findSetMealDetailById(id);
        //设置图片完整路径
        setMeal.setImg(QiNiuUtils.DOMAIN + setMeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setMeal);
    }




}
