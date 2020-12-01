package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    /**
     * 获取运营统计数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        //设置当前日期为报告日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String reportDate = sdf.format(today);
        //获取本周周一的日期
        String thisWeekMonday = sdf.format(DateUtils.getThisWeekMonday(today));
//        获取本周周日的日期
        String SundayOfThisWeek = sdf.format(DateUtils.getSundayOfThisWeek());
//        获取本月第一天
        String firstDay4ThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
//        获取本月最后天
        String lastDay4ThisMonth = sdf.format(DateUtils.getLastDay4ThisMonth());

        /*会员统计*/
        //获取今天的会员数量
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
//        获取会员总数
        Integer totalMember = memberDao.findMemberCountBeforeRegTime(reportDate);
//        获取本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterRegTime(thisWeekMonday);
//        本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterRegTime(firstDay4ThisMonth);

        /*预约统计*/
//        今日预约数
        Integer todayOrderNumber = orderDao.findOrderNumberByDate(reportDate);
//        今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsNumberByDate(reportDate);
//        本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(thisWeekMonday, SundayOfThisWeek);
//        本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
//        本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDay4ThisMonth,lastDay4ThisMonth);
//        本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

//        获取热门套餐
        List<Map<String,Object>> hotSetMealList = orderDao.findHotSetMeal();

//        构造前端需求数据格式
        Map<String,Object> reportData = new HashMap<String,Object>(12);
        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetMealList);

        return reportData;
    }
}
