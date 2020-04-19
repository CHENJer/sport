package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.service.ReportService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 报表统计
 * @author: JIAWEI
 * @date: Created in 2020/3/23 21:26
 * @version: 1.0
 * @modified By:
 */
@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;
    /*用户消费*/
    @RequestMapping("/report/userConsumptionInit")
    public String userConsumptionInit(){
        return "web/report-manage/userConsumption";
    }

    /*用户消费*/
    @RequestMapping("/report/queryConsumption")
    @ResponseBody
    public String queryConsumption(String userName, @RequestParam(required=false,defaultValue="1") int page,
                                                      @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<Map<String, Object>> maps = reportService.queryConsumption(userName);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(maps,limit);

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


    /*课程统计*/
    @RequestMapping("/report/courseStatisticsInit")
    public String courseStatisticsInit(){
        return "web/report-manage/courseStatistics";
    }

    @RequestMapping("/report/queryCourseStatistics")
    @ResponseBody
    public String queryCourseStatistics(String courseName, @RequestParam(required=false,defaultValue="1") int page,
                                          @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<Map<String, Object>> maps = reportService.queryCourseStatistics(courseName);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(maps,limit);
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
}
