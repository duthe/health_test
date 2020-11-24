package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    //@RequestParam List<Integer> checkgroupIds 可以获取
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        setMealService.add(setmeal, checkgroupIds);
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    @RequestMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setMealService.findById(id);
        Map resultMap = new HashMap();
        resultMap.put("domain", QiNiuUtils.DOMAIN);
        resultMap.put("setMeal", setmeal);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
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


    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String imgName = UUID.randomUUID().toString() + suffix;
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), imgName);
            Map resultMap = new HashMap();
            resultMap.put("domain", QiNiuUtils.DOMAIN);
            resultMap.put("imgName", imgName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, resultMap);
        } catch (IOException e) {
            //e.printStackTrace();
            //抛给全局异常处理类处理 是否可行???
            throw new RuntimeException(e.getMessage());

        }

    }




}
