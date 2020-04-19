package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.CourseService;
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
 * @description: 课程管理
 * @author: JIAWEI
 * @date: Created in 2020/3/23 21:16
 * @version: 1.0
 * @modified By:
 */
@Controller
public class CourceController {
    @Autowired
    private CourseService courseService;

    /*分类管理*/
    @RequestMapping("/cource/typeInit")
    public String typeInit(){
        return "web/cource-manage/typeManage";
    }


    /*分类管理*/
    @RequestMapping("/cource/findAll")
    @ResponseBody
    public String courcefindAll(String typeName, @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CourseType> sourceType = courseService.findSourceType(typeName);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(sourceType,limit);

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
    @RequestMapping("/cource/addTypeInit")
    public String addTypeInit(){
        return "web/cource-manage/addType";
    }

    @RequestMapping("/cource/addType")
    @ResponseBody
    public Result addType(CourseType courseType){
        CodeMsg codeMsg = courseService.addSourceType(courseType);
        return Result.success(codeMsg);
    }
    @RequestMapping("/cource/editTypeInit")
    public String editTypeInit(Model model , Integer id){
        CourseType courseType = courseService.findSourceTypeById(id);
        model.addAttribute("courseType",courseType);
        return "web/cource-manage/editType";
    }

    @RequestMapping("/cource/editType")
    @ResponseBody
    public Result editType(CourseType courseType){
        CodeMsg codeMsg = courseService.editSourceType(courseType);
        return Result.success(codeMsg);
    }




    /*课程管理*/
    @RequestMapping("/cource/courseManageInit")
    public String courseManageInit(){
        return "web/cource-manage/courseManage";
    }

    /*课程管理 courseType 0 默认全部，1推荐课程 2结束和下架课程*/
    @RequestMapping("/cource/courseManageFindAll")
    @ResponseBody
    public String courseManageFindAll(String courseName,String courseType,String isApplyEnd, @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CourseMain> courseMain = courseService.findCourseMain(courseName, courseType,isApplyEnd);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(courseMain,limit);
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
    /*设置课程状态*/
    @RequestMapping("/cource/setCourse")
    public String setCourse(Model model, Integer id){
        CourseMain courseMain = courseService.findCourseMainById(id);
        model.addAttribute("courseMain",courseMain);
        return "web/cource-manage/editCourseStatus";
    }
    /*申请下架课程*/
    @RequestMapping("/cource/reviewCourse")
    public String reviewCourse(Model model, Integer id){
        CourseMain courseMain = courseService.findCourseMainById(id);
        model.addAttribute("courseMain",courseMain);
        return "web/cource-manage/reviewCourse";
    }
    /*设置课程状态*/
    @RequestMapping("/cource/editCourseMainStatus")
    @ResponseBody
    public Result editCourseMainStatus(Integer id,String status,String isRecommended){
        CodeMsg codeMsg = courseService.editCourseMainStatus(id, status, isRecommended);
        return Result.success(codeMsg);
    }
    /*审核申请下架课程*/
    @RequestMapping("/cource/reviewCourseMainStatus")
    @ResponseBody
    public Result reviewCourseMainStatus(Integer id,String isApplyEnd){
        CodeMsg codeMsg = courseService.reviewCourseMainStatus(id, isApplyEnd);
        return Result.success(codeMsg);
    }

    /*课程查看小节init*/
    @RequestMapping("/cource/findPackageInit")
    public String courseManageFindPackageInit(Model model,String courseCode){
        model.addAttribute("courseCode",courseCode);
        return "web/cource-manage/courcePackage";
    }
    /*课程查看小节*/
    @RequestMapping("/cource/findPackage")
    @ResponseBody
    public String courseManageFindPackage(String courseCode, @RequestParam(required=false,defaultValue="1") int page,
                                      @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CoursePackage> coursePackage = courseService.findCoursePackage(courseCode);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(coursePackage,limit);
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
