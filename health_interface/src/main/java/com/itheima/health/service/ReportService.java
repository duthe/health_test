package com.itheima.health.service;

import java.util.Map;

public interface ReportService {

    /**
     * 获取运营统计数据
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
