package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.Result;
import cn.com.itcast.sport.entry.SalaryMonth;
import cn.com.itcast.sport.entry.User;
import cn.com.itcast.sport.service.CapitalService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 资金管理
 * @author: JIAWEI
 * @date: Created in 2020/3/23 21:25
 * @version: 1.0
 * @modified By:
 */
@Controller
public class CapitalController {
    @Autowired
    private CapitalService capitalService;
    /*盈利统计*/
    @RequestMapping("/capital/profitStatisticalInit")
    public String profitStatisticalInit(){
        return "web/capital-manage/profitStatistical";
    }

    /*收入统计*/
    @RequestMapping("/capital/queryIncome")
    @ResponseBody
    public Result queryIncome(String startTime,String endTime){
        Map<String, Object> maps = capitalService.queryIncome(startTime, endTime);
        return Result.success(maps);
    }
    /*支出统计*/
    @RequestMapping("/capital/querySpending")
    @ResponseBody
    public Result querySpending(String startTime,String endTime){
        Map<String, Object> maps = capitalService.querySpending(startTime, endTime);
        return Result.success(maps);
    }



    /*工资管理init*/
    @RequestMapping("/capital/salaryManageInit")
    public String salaryManageInit(){
        return "web/capital-manage/salaryManage";
    }

    /*工资管理查询*/
    @RequestMapping("/capital/salaryManageFindAll")
    @ResponseBody
    public String salaryManageFindAll(String name, @RequestParam(required=false,defaultValue="1") int page,
                                        @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<SalaryMonth> salarys = capitalService.findSalaryByName(name);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(salarys,limit);

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

    /*添加工资init*/
    @RequestMapping("/capital/addSalaryManageInit")
    public String addSalaryManageInit(Model model){
        List<User> teacher = capitalService.findTeacher();
        model.addAttribute("teacher",teacher);
        return "web/capital-manage/addSalaryManage";
    }
    /*添加工资*/
    @RequestMapping("/capital/addSalaryManage")
    @ResponseBody
    public Result addSalaryManage(SalaryMonth salaryMonth){
        CodeMsg codeMsg = capitalService.addSalary(salaryMonth);
        return Result.success(codeMsg);
    }
    /*修改工资init*/
    @RequestMapping("/capital/editSalaryManageInit")
    public String editSalaryManageInit(Model model,Integer id){
        List<User> teacher = capitalService.findTeacher();
        SalaryMonth salary = capitalService.findSalaryById(id);
        model.addAttribute("teacher",teacher);
        model.addAttribute("salary",salary);
        return "web/capital-manage/editSalaryManage";
    }
    /*添加工资*/
    @RequestMapping("/capital/editSalaryManage")
    @ResponseBody
    public Result editSalaryManage(SalaryMonth salaryMonth){
        CodeMsg codeMsg = capitalService.editSalary(salaryMonth);
        return Result.success(codeMsg);
    }
}
