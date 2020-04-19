package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.LeaveMain;
import cn.com.itcast.sport.entry.Result;
import cn.com.itcast.sport.service.LeaveMainService;
import cn.com.itcast.sport.util.StringToDate;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/30 22:33
 * @version: 1.0
 * @modified By:
 */
@Controller
@RequestMapping("leave")
public class LeaveMainController {

    @Autowired
    private LeaveMainService leaveMainService;
    /*未审批管理*/
    @RequestMapping("/noApprovalInit")
    public String noAttendanceInit(){
        return "web/leave-manage/noApprovalManage";
    }

    /*请假管理*/
    @RequestMapping("/attendanceManageInit")
    public String attendanceManageInit(){
        return "web/leave-manage/attendanceManage";
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public String findAll(String startUserName, String status, String startTime, String endTime, @RequestParam(required=false,defaultValue="1") int page,
                          @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        Date startDate = null;
        Date endDate = null;
        if(startTime != null){
            startDate = StringToDate.changeData(startTime);
        }
        if(endTime != null){
            endDate = StringToDate.changeData(endTime);
        }
        List<LeaveMain> leaveMains = leaveMainService.findAll(startUserName, status,startDate,endDate);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(leaveMains,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg", "");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        // return JsonConversion.writeMapJSON(map);
        return json;
    }
    @RequestMapping("/reviewLeave")
    public String reviewLeave(Model model , Integer id){
        LeaveMain leaveMain= leaveMainService.reviewLeave(id);
        model.addAttribute("leaveMain",leaveMain);
        return "web/leave-manage/reviewLeave";
    }

    @RequestMapping("/submitLeave")
    @ResponseBody
    public Result submitLeave(Integer id,String checkComment,String status){
        CodeMsg codeMsg = leaveMainService.editLeaveMain(id,checkComment,status);
        return Result.success(codeMsg);
    }
}
