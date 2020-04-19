package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.ApiService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/4/4 16:39
 * @version: 1.0
 * @modified By:
 */
@Controller
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ApiService apiService;
    public static String DATA_HM = "yyyy-MM-dd HH:mm";
    public static String DATA_DAY = "yyyy-MM-dd";
    public Date changeData(String date,String dataType) {
        if(dataType.equals(DATA_HM) ){
            date = date+":00";
            dataType = dataType+":ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(dataType);
        Date parse = null;
        try {
            parse = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    @RequestMapping(path = "/login")
    @ApiOperation("登录校验账号密码")
    @ResponseBody
    public Result login(@Param("userName") String userName, @Param("password") String password){
        return apiService.checkLogin(userName,password);
    }
    @RequestMapping(path = "/getPic")
    @ApiOperation("获取轮播图")
    @ResponseBody
    public Result getPic(){
        List<String> pic = new ArrayList<>();
        pic.add("http://js.xiaoliaofushi.vip/uploads/pic/1.jpg");
        pic.add("http://js.xiaoliaofushi.vip/uploads/pic/2.jpg");
        pic.add("http://js.xiaoliaofushi.vip/uploads/pic/3.jpg");
        return Result.success(pic);
    }


    @RequestMapping(path = "/course/queryCourses")
    @ApiOperation("查询全部课程，带参数则返回相应条件的数据，不带则返回全部")
    @ResponseBody
    public String queryCourses(@Param("courseName") String courseName, @Param("typeCode")String typeCode,
                               @RequestParam(required=false,defaultValue="1") int page,
                               @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CourseMain> courseMains = apiService.queryCourses(courseName, typeCode);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(courseMains,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }
    @RequestMapping(path = "/course/queryTeacherCourses")
    @ApiOperation("查询教练个人的课程，必须带userCode参数")
    @ResponseBody
    public String queryTeacherCourses(@Param("courseName") String courseName, @Param("typeCode")String typeCode,
                                      @Param("userCode")String userCode,
                               @RequestParam(required=false,defaultValue="1") int page,
                               @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CourseMain> courseMains = apiService.queryTeacherCourses(courseName, typeCode,userCode);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(courseMains,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }

    @RequestMapping(path = "/course/queryRecommendedCourses")
    @ApiOperation("查询推荐课程，带参数则返回相应条件的数据，不带则返回全部")
    @ResponseBody
    public Result queryRecommendedCourses(@Param("courseName") String courseName){
        List<CourseMain> courseMains = apiService.queryRecommendedCourses(courseName);
        return Result.success(courseMains);
    }

    @RequestMapping(path = "/course/queryCourse")
    @ApiOperation("查看具体课程，必须带参数courseCode")
    @ResponseBody
    public Result queryCourse(@Param("userCode") String userCode,@Param("courseCode") String courseCode){
        CourseMain courseMain = apiService.queryCourse(userCode,courseCode);
        return Result.success(courseMain);
    }

    @RequestMapping(path = "/course/addCourse")
    @ApiOperation("添加课程，必须带参数")
    @ResponseBody
    public Result addCourse(@Param("courseName") String courseName, @Param("coursePic") String coursePic,
                            @Param("courseVideo") String courseVideo,@Param("courseDesc") String courseDesc, @Param("coursePrice") BigDecimal coursePrice,
                            @Param("startTime") String startTime, @Param("endTime") String endTime,
                            @Param("coueseTypeName") String coueseTypeName, @Param("courseTypeCode") String courseTypeCode,
                            @Param("userCode") String userCode, @Param("userName") String userName){

        CourseMain courseMain = new CourseMain();
        courseMain.setCourseName(courseName);
        courseMain.setCoursePic(coursePic);
        courseMain.setCourseVideo(courseVideo);
        courseMain.setCourseDesc(courseDesc);
        courseMain.setCoursePrice(coursePrice);
        courseMain.setCourseName(courseName);
        if(startTime !=null){
            courseMain.setStartTime(changeData(startTime, DATA_DAY));
        }
        if(endTime !=null){
            courseMain.setEndTime(changeData(endTime, DATA_DAY));
        }
        courseMain.setCoueseTypeName(coueseTypeName);
        courseMain.setCourseTypeCode(courseTypeCode);
        courseMain.setUserCode(userCode);
        courseMain.setUserName(userName);
        int addCourse = apiService.addCourse(courseMain);
        if(addCourse == -1){
            return Result.error(new CodeMsg(500,"插入失败"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/editCourse")
    @ApiOperation("修改课程，必须带courseCode，只修改参数不为空的，状态status：'0未开课，1正在开课，2结束，3下架（教练离职）")
    @ResponseBody
    public Result editCourse(@Param("courseName") String courseName, @Param("courseCode") String courseCode,
                             @Param("coursePic") String coursePic,@Param("courseVideo") String courseVideo,
                             @Param("coursePrice") BigDecimal coursePrice,
                             @Param("startTime") String startTime, @Param("endTime") String endTime,
                            @Param("coueseTypeName") String coueseTypeName, @Param("courseTypeCode") String courseTypeCode,
                            @Param("status") String status,
                            @Param("userCode") String userCode, @Param("userName") String userName){
        CourseMain courseMain = new CourseMain();
        if(startTime !=null && !"''".equals(startTime)){
            courseMain.setStartTime(changeData(startTime, DATA_DAY));
        }
        if(endTime !=null&& !"''".equals(endTime)){
            courseMain.setEndTime(changeData(endTime, DATA_DAY));
        }
        courseMain.setCourseName(courseName);
        courseMain.setCourseCode(courseCode);
        courseMain.setCoursePic(coursePic);
        courseMain.setCourseVideo(courseVideo);
        courseMain.setCoursePrice(coursePrice);
        courseMain.setStatus(status);
        courseMain.setCoueseTypeName(coueseTypeName);
        courseMain.setCourseTypeCode(courseTypeCode);
        courseMain.setUserCode(userCode);
        courseMain.setUserName(userName);
        int editCourse = apiService.editCourse(courseMain);
        if(editCourse == -1){
            return Result.error(new CodeMsg(500,"更新失败"));
        }
        return Result.success("操作成功！");
    }
    @RequestMapping(path = "/course/openOrEndCourse")
    @ApiOperation("教练课程开课或下课，必须带courseCode和状态status：'0未开课，1正在开课，2结束，3下架（教练离职） ")
    @ResponseBody
    public Result openOrEndCourse( @Param("courseCode") String courseCode,@Param("status") String status){

        int editCourse = apiService.openOrEndCourse(courseCode,status);
        if(editCourse == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }
    @RequestMapping(path = "/course/applyEnd")
    @ApiOperation("教练课程申请下架，必须带courseCode")
    @ResponseBody
    public Result applyEnd( @Param("courseCode") String courseCode){

        int editCourse = apiService.applyEnd(courseCode);
        if(editCourse == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/courseCommend")
    @ApiOperation("学生课程评价，必须带用户userCode,课程courseCode，评价courseComment")
    @ResponseBody
    public Result courseCommend( @Param("userCode") String userCode,
                                 @Param("courseCode") String courseCode,
                                 @Param("courseComment") String courseComment){

        int commend = apiService.courseCommend(userCode, courseCode, courseComment);
        if(commend == -1){
            return Result.error(new CodeMsg(500,"参数不能为空"));
        }else if(commend == -0){
            return Result.error(new CodeMsg(500,"该学生没有此课程，不能评价！"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/addClickNum")
    @ApiOperation("添加点击人数，必须带参数")
    @ResponseBody
    public Result addClickNum(@Param("courseCode") String courseCode){
        int clickNum = apiService.addClickNum(courseCode);
        if(clickNum == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功");
    }
    @RequestMapping(path = "/course/queryCourseTypes")
    @ApiOperation("查看课程分类")
    @ResponseBody
    public Result queryCourseTypes(){
        List<CourseType> courseTypes = apiService.queryCourseTypes();
        return Result.success(courseTypes);
    }
    @RequestMapping(path = "/course/queryCourseTypeByCode")
    @ApiOperation("查看某一课程分类信息，必须带参数")
    @ResponseBody
    public Result queryCourseTypeByCode(@Param("typeCode") String typeCode){
        CourseType courseType = apiService.queryCourseTypeByCode(typeCode);
        return Result.success(courseType);
    }

    @RequestMapping(path = "/course/queryPackages")
    @ApiOperation("查询课程全部小节，必须带参数")
    @ResponseBody
    public Result queryPackages(@Param("courseCode") String courseCode){
        List<CoursePackage> coursePackages = apiService.queryPackages(courseCode);
        return Result.success(coursePackages);
    }
    @RequestMapping(path = "/course/queryNearPackages")
    @ApiOperation("查询教练近期课程小节，必须带参数")
    @ResponseBody
    public String queryNearPackages(@Param("userCode") String userCode,@RequestParam(required=false,defaultValue="1") int page,
    @RequestParam(required=false,defaultValue="10") int limit){
        Page<Object> page1 = PageHelper.startPage(page, limit);
        List<CoursePackage> coursePackages = apiService.queryNearPackages(userCode);
        PageInfo pageInfo = new PageInfo(coursePackages,limit);
        pageInfo.setPages(page1.getPages());//总页数
        pageInfo.setTotal(page1.getTotal());//总条数
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }
    @RequestMapping(path = "/course/queryPackage")
    @ApiOperation("查询具体小节的信息，必须带参数")
    @ResponseBody
    public Result queryPackage(@Param("packageCode") String packageCode){
        CoursePackage coursePackage = apiService.queryPackage(packageCode);
        return Result.success(coursePackage);
    }
    @RequestMapping(path = "/course/addPackage")
    @ApiOperation("添加小节，必须带参数")
    @ResponseBody
    public Result addPackage(@Param("packageNum") String packageNum,@Param("packageName") String packageName,
                             @Param("timeLong") String timeLong,@Param("startTime") String startTime,
                             @Param("endTime") String endTime, @Param("courseCode") String courseCode){
        CoursePackage coursePackage = new CoursePackage();
        coursePackage.setPackageName(packageName);
        coursePackage.setPackageNum(packageNum);
        coursePackage.setTimeLong(timeLong);
        coursePackage.setCourseCode(courseCode);
        if(startTime !=null){
            coursePackage.setStartTime(changeData(startTime, DATA_HM));
        }
        if(endTime !=null){
            coursePackage.setEndTime(changeData(endTime, DATA_HM));
        }

        int i = apiService.addPackage(coursePackage);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功");
    }
    @RequestMapping(path = "/course/editPackage")
    @ApiOperation("修改小节，必须带packageCode参数,其他参数带则修改")
    @ResponseBody
    public Result editPackage(@Param("packageNum") String packageNum,@Param("packageName") String packageName,
                             @Param("timeLong") String timeLong,@Param("startTime") String startTime,
                             @Param("endTime") String endTime, @Param("courseCode") String courseCode,
                             @Param("packageCode") String packageCode,@Param("status") String status){
        CoursePackage coursePackage = new CoursePackage();
        coursePackage.setPackageName(packageName);
        coursePackage.setPackageNum(packageNum);
        coursePackage.setTimeLong(timeLong);
        if(startTime !=null && !"''".equals(startTime)){
            coursePackage.setStartTime(changeData(startTime, DATA_HM));
        }
        if(endTime !=null && !"''".equals(endTime)){
            coursePackage.setEndTime(changeData(endTime, DATA_HM));
        }
        coursePackage.setCourseCode(courseCode);
        coursePackage.setStatus(status);
        int i = apiService.editPackage(coursePackage);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功");
    }
    @RequestMapping(path = "/course/openOrEndPackage")
    @ApiOperation("教练小节开课或下课，必须带packageCode和状态status：'0未解锁，1正在进行已解锁，2已完成")
    @ResponseBody
    public Result openOrEndPackage( @Param("packageCode") String packageCode,@Param("status") String status){
        int editPackage = apiService.openOrEndPackage(packageCode,status);
        if(editPackage == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }
    @RequestMapping(path = "/course/delPackage")
    @ApiOperation("教练删除小节，必须带packageCode")
    @ResponseBody
    public Result delPackage( @Param("packageCode") String packageCode){

        int delPackage = apiService.delPackage(packageCode);
        if(delPackage == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/queryShopChart")
    @ApiOperation("查看购物车，必须带参数")
    @ResponseBody
    public String queryShopChart(@Param("userCode") String userCode,
                                 @RequestParam(required=false,defaultValue="1") int page,
                                 @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<ShopCart> shopCarts = apiService.queryShopChart(userCode);
        PageInfo pageInfo = new PageInfo(shopCarts,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }

    @RequestMapping(path = "/course/addShopChart")
    @ApiOperation("添加购物车，必须带参数")
    @ResponseBody
    public Result addShopChart(@Param("userCode") String userCode,@Param("courseName") String courseName,
                               @Param("courseCode") String courseCode){
        ShopCart shopCart = new ShopCart();
        shopCart.setCourseName(courseName);
        shopCart.setCourseCode(courseCode);
        shopCart.setUserCode(userCode);
        int i = apiService.addShopChart(shopCart);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功");
    }

    @RequestMapping(path = "/course/editShopChart")
    @ApiOperation("批量删除购物车，必须带数组ids")
    @ResponseBody
    public Result delShopChart(Integer[] ids){
        int i = apiService.delShopChart(ids);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功");
    }
    @RequestMapping(path = "/course/createOrderByShopChartIds")
    @ApiOperation("通过购物车创建订单，必须带购物车的数组ids")
    @ResponseBody
    public Result createOrderByShopChartIds(Integer[] ids){
        Map<String, Object> order = apiService.createOrderByShopChartIds(ids);
        if(order == null || order.size() == 0){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success(order);
    }
    @RequestMapping(path = "/course/payOrder")
    @ApiOperation("支付或者取消订单，必须带参数orderCode和status，其中状态：-1,已取消，0未付款，1已付款，")
    @ResponseBody
    public Result payOrder(String orderCode,String status){
        int i = apiService.payOrder(orderCode, status);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/queryShopOrders")
    @ApiOperation("查看个人所有订单，必须带参数userCode")
    @ResponseBody
    public String queryShopOrders(String userCode,
                                  @RequestParam(required=false,defaultValue="1") int page,
                                  @RequestParam(required=false,defaultValue="10") int limit){
        Page<Object> page1 = PageHelper.startPage(page, limit);
        List<Map<String, Object>> maps = apiService.queryShopOrders(userCode);
        PageInfo pageInfo = new PageInfo(maps,limit);
        pageInfo.setPages(page1.getPages());//总页数
        pageInfo.setTotal(page1.getTotal());//总条数
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }

    @RequestMapping(path = "/course/queryUserCourse")
    @ApiOperation("查看学员个人课程，必须带参数userCode")
    @ResponseBody
    public String queryUserCourse(String userCode,
                                  @RequestParam(required=false,defaultValue="1") int page,
                                  @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<CourseMain> courseMains = apiService.queryUserCourse(userCode);
        PageInfo pageInfo = new PageInfo(courseMains,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }

    @RequestMapping(path = "/course/putCard")
    @ApiOperation("学员小节打卡，必须带参数packageCode和userCode")
    @ResponseBody
    public Result putCard(String packageCode,String userCode){
        int i = apiService.putCard(packageCode, userCode);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/queryUserInfo")
    @ApiOperation("查看个人信息，必须带参数userCode")
    @ResponseBody
    public Result queryUserInfo(String userCode){
        User user = apiService.queryUserInfo(userCode);
        if(user == null){
            return Result.error(new CodeMsg(500,"个人信息为空！"));
        }
        return Result.success(user);
    }

    @RequestMapping(path = "/course/querySalarys")
    @ApiOperation("查看个人工资，必须带参数userCode")
    @ResponseBody
    public Result querySalarys(String userCode){
        List<SalaryMonth> salaryMonths = apiService.querySalarys(userCode);
        return Result.success(salaryMonths);
    }

    @RequestMapping(path = "/course/queryLeaveMains")
    @ApiOperation("查看教练请假列表，必须带参数")
    @ResponseBody
    public String queryLeaveMains(String userCode,
                                  @RequestParam(required=false,defaultValue="1") int page,
                                  @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<LeaveMain> leaveMains = apiService.queryLeaveMains(userCode);
        PageInfo pageInfo = new PageInfo(leaveMains,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }

    @RequestMapping(path = "/course/addLeaveMain")
    @ApiOperation("教练请假，必须带参数")
    @ResponseBody
    public Result addLeaveMain(String packageCode,String packageAdjustStartTime,String packageAdjustEndTime,
                               String startUserName,String startUserCode){
        LeaveMain leaveMain = new LeaveMain();
        if(packageAdjustStartTime !=null){
            leaveMain.setPackageAdjustStartTime(changeData(packageAdjustStartTime, DATA_HM));
        }
        if(packageAdjustEndTime !=null){
            leaveMain.setPackageAdjustEndTime(changeData(packageAdjustEndTime, DATA_HM));
        }
        leaveMain.setPackageCode(packageCode);
        leaveMain.setStartUserName(startUserName);
        leaveMain.setStartUserCode(startUserCode);
        int i = apiService.addLeaveMain(leaveMain);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败，参数异常！"));
        }else if(i == -2){
            return Result.error(new CodeMsg(500,"课程已开始，不能请假！"));
        }
        return Result.success("操作成功！");
    }

    @RequestMapping(path = "/course/cancelLeaveMain")
    @ApiOperation("教练撤销请假，必须带小节的code参数")
    @ResponseBody
    public Result cancelLeaveMain(String packageCode){
        int i = apiService.cancelLeaveMain(packageCode);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }
    @RequestMapping(path = "/course/querySiteMsgs")
    @ApiOperation("查询站内消息，必须带接收人code参数")
    @ResponseBody
    public String querySiteMsgs(String acceptCode,
                                @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit){
        PageHelper.startPage(page, limit);
        List<SiteMsg> siteMsgs = apiService.querySiteMsgs(acceptCode);
        PageInfo pageInfo = new PageInfo(siteMsgs,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","200");
        map.put("msg", "操作成功！");
        map.put("maxPage",pageInfo.getPages());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        return json;
    }
    @RequestMapping(path = "/course/querySiteMsgNum")
    @ApiOperation("查询站内消息数量，必须带接收人code参数")
    @ResponseBody
    public Result querySiteMsgNum(String acceptCode){
        List<SiteMsg> siteMsgs = apiService.querySiteMsgs(acceptCode);
        int msgNum = 0;
        if(siteMsgs!=null &&siteMsgs.size() > 0){
            List<SiteMsg> collect = siteMsgs.stream().filter(siteMsg -> siteMsg.getIsRead() != null && "0".equals(siteMsg.getIsRead())).collect(Collectors.toList());
            msgNum = collect == null ? 0: collect.size();
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("msgNum",msgNum);
        return Result.success(map);
    }
    @RequestMapping(path = "/course/readSiteMsg")
    @ApiOperation("站内消息变为已读，必须带消息id")
    @ResponseBody
    public Result readSiteMsg(Integer id){
        int i = apiService.readSiteMsg(id);
        if(i == -1){
            return Result.error(new CodeMsg(500,"操作失败"));
        }
        return Result.success("操作成功！");
    }


}
