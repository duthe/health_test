package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;


    /**
     * 手机快速登录
     * @param loginInfo
     * @param response
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map<String,String> loginInfo, HttpServletResponse response){
        String telephone = loginInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        Jedis jedis = jedisPool.getResource();
        //获取redis中存储的验证码
        String code = jedis.get(key);

        if (code == null) {
            //redis中没有获取到验证码 则表示验证码过期或者是用户未获取验证码
            return new Result(false, "验证码已过期，请重新获取验证码");
        }

        //校验验证码是否正确
        if (!code.equals(loginInfo.get("validateCode"))) {
            //不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //校验成功 删除redis中的验证码 防止重复验证
        jedis.del(key);
        jedis.close();

        //根据手机号查询是否是会员
        Member member = memberService.findByTelephone(telephone);

        if (member == null) {
            //不是会员 添加会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("手机快速登录");
            memberService.add(member);
        }

        //设置cookie
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

}
