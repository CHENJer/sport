package cn.com.itcast.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 考勤管理
 * @author: JIAWEI
 * @date: Created in 2020/3/23 21:24
 * @version: 1.0
 * @modified By:
 */
@Controller
public class AttendanceController {
    /*未审批管理*/
    @RequestMapping("/attendance/noApprovalInit")
    public String noAttendanceInit(){
        return "web/attendance-manage/noApprovalManage";
    }

    /*请假管理*/
    @RequestMapping("/attendance/attendanceManageInit")
    public String attendanceManageInit(){
        return "web/attendance-manage/attendanceManage";
    }


}
