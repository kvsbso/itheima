package com.itheima.service;

import java.util.Map;

/**
 * 报表服务接口
 */
public interface ReportService {
    /**
     * 获取运营数据统计报表（会员数据 预约数据 热门套餐数据）
     * @return
     */
    Map getBusinessReportData() throws Exception;
}
