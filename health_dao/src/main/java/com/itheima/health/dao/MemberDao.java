package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

public interface MemberDao {

    /**
     * 根据手机号查询会员
     * @param telephone 手机号
     * @return  会员
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member 要添加的会员信息
     */
    void add(Member member);
}
