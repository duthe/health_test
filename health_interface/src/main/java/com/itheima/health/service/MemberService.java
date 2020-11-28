package com.itheima.health.service;

import com.itheima.health.pojo.Member;

public interface MemberService {

    /**
     * 根据手机号查询会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);
}
