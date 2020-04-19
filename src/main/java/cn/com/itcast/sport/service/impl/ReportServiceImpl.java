package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.ShopOrderMapper;
import cn.com.itcast.sport.dao.UserMapper;
import cn.com.itcast.sport.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/4/6 20:50
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ShopOrderMapper shopOrderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Map<String, Object>> queryConsumption(String userName) {
        List<Map<String, Object>> maps = shopOrderMapper.queryConsumption(userName);
        return maps;
    }

    @Override
    public List<Map<String, Object>> queryCourseStatistics(String courseName) {
        List<Map<String, Object>> maps = userMapper.queryCourseStatistics(courseName);
        return maps;
    }
}
