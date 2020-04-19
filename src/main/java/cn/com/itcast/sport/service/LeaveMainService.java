package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.LeaveMain;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/30 22:34
 * @version: 1.0
 * @modified By:
 */
public interface LeaveMainService {
    List<LeaveMain> findAll(String startUserName, String status, Date startTime,Date endTime);
    LeaveMain reviewLeave(Integer id);
    CodeMsg editLeaveMain(Integer id,String checkComment,String status);
}
