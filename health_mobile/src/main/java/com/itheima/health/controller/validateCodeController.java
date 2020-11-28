package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@RestController
@RequestMapping("/validateCode")
public class validateCodeController {

    @Autowired
    private JedisPool jedisPool;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 发送预约验证码
     * @param telephone 手机号码
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        return sendCode(telephone, key);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        return sendCode(telephone, key);
    }

    public Result sendCode(String telephone, String key) {
        Jedis jedis = jedisPool.getResource();
        //获取redis中的验证码
        String codeInRedis = jedis.get(key);
        logger.debug("redis中的验证码{}，{}", telephone, codeInRedis);
        if (codeInRedis != null) {
            //如果存在验证码 则已经发送过了
            throw new HealthException("已经发送过验证码，请注意查收！");
        }
        //不存在 生成验证码
        //String code = ValidateCodeUtils.generateValidateCode4String(6);
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));

        try {
            logger.debug("发送验证码{}，{}", telephone, code);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
        } catch (ClientException e) {
            logger.error("验证码发送失败", e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        logger.debug("验证码发送成功{}，{}", telephone, code);
        //验证码存入redis 并设置有效期10分钟
        jedis.setex(key, 10 * 60, code);
        jedis.close();
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
