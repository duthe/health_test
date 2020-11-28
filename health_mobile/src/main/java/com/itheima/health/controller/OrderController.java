package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 体检预约
     * @param paramMap
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paramMap) {
//      校验验证码
        String telephone = paramMap.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        Jedis jedis = jedisPool.getResource();
        //获取redis中存储的验证码
        String code = jedis.get(key);

        if (code == null) {
            //redis中没有获取到验证码 则表示验证码过期或者是用户未获取验证码
            return new Result(false, "验证码已过期，请重新获取验证码");
        }

        //校验验证码是否正确
        if (!code.equals(paramMap.get("validateCode"))) {
            //不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //校验成功 删除redis中的验证码 防止重复验证
        jedis.del(key);
        jedis.close();
        //设置预约方式
        paramMap.put("orderType", "微信预约");
        Integer id = orderService.submit(paramMap);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("id", id + "");
        return new Result(true, MessageConstant.ORDER_SUCCESS, resultMap);
    }

    /**
     * 根据预约id查询预约信息
     * @param id 预约订单id
     * @return 包含预约人姓名 套餐名称 预约日期 预约类型
     */
    @RequestMapping("/findDetailById")
    public Result findDetailById(int id){
       Map<String, String> resultMap = orderService.findDetailById(id);
       return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, resultMap);
    }
}
