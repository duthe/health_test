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

    /**
     * 查询指定日期之前的会员数量
     * @param regTime
     * @return
     */
    Integer findMemberCountBeforeRegTime(String regTime);

    /**
     * 获取指定日期当天的会员数量
     * @param regTime
     * @return
     */
    Integer findMemberCountByDate(String regTime);

    /**
     * 获取指定日期之后的会员数量
     * @param regTime
     * @return
     */
    Integer findMemberCountAfterRegTime(String regTime);
}
