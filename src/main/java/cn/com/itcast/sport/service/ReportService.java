package cn.com.itcast.sport.service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/4/6 20:48
 * @version: 1.0
 * @modified By:
 */
public interface ReportService {
    List<Map<String,Object>> queryConsumption(String userName);

    List<Map<String,Object>> queryCourseStatistics(String courseName);
}
