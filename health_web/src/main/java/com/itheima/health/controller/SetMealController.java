package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/setmeal")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HEALTH_MANAGER')")
public class SetMealController {

    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;


    /**
     * 分页查找套餐信息
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findByPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean) {
       PageResult<Setmeal> setMealPageResult = setMealService.findByPage(queryPageBean);
       return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setMealPageResult);
    }


    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    //@RequestParam List<Integer> checkgroupIds 可以获取
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Integer setMealId = setMealService.add(setmeal, checkgroupIds);

        //添加静态化任务
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setMeal:static:html",setMealId + "|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setMealService.findById(id);
        Map resultMap = new HashMap();
        resultMap.put("domain", QiNiuUtils.DOMAIN);
        resultMap.put("setMeal", setmeal);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
    }


    /**
     * 根据套餐id查询该套餐关联的检查组
     * @param setMeaId
     * @return
     */
    @RequestMapping("/findSetMealCheckGroupBySetMeaId")
    public Result findSetMealCheckGroupBySetMeaId(int setMeaId) {
        List<Integer> integers = setMealService.findSetMealCheckGroupBySetMeaId(setMeaId);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, integers);
    }


    /**
     * 更新套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        setMealService.update(setmeal, checkgroupIds);

        //添加静态化任务
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setMeal:static:html",setmeal.getId() + "|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "编辑套餐成功");
    }


    /**
     * 根据id删除套餐
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result deleteById(int id) {
        setMealService.deleteById(id);
        //添加静态化任务
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setMeal:static:html",id + "|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "删除套餐成功！");
    }


    /**
     * 接收上传套餐图片
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename(); //获取文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); //获取文件后缀名
        String imgName = UUID.randomUUID().toString() + suffix; //生成随机文件名 防止文件冲突
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), imgName); //上传到七牛
            Map resultMap = new HashMap();
            resultMap.put("domain", QiNiuUtils.DOMAIN); //域名
            resultMap.put("imgName", imgName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, resultMap);
        } catch (IOException e) {
            //e.printStackTrace();
            //TODO:抛给全局异常处理类处理 是否可行???  或者直接方法上抛
            throw new RuntimeException(e);

        }

    }




}
