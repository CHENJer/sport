package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/4/4 16:40
 * @version: 1.0
 * @modified By:
 */
public interface ApiService {
    /*登录验证*/
    Result checkLogin(String userName, String password);

    /*查看全部课程*/
    List<CourseMain> queryCourses(String courseName,String typeCode);
    List<CourseMain> queryTeacherCourses(String courseName,String typeCode,String userCode);
    /*推荐课程*/
    List<CourseMain> queryRecommendedCourses(String courseName);
    /*查看具体课程*/
    CourseMain queryCourse(String userCode,String courseCode);
    /*添加课程*/
    int addCourse(CourseMain courseMain);
    /*修改课程*/
    int editCourse(CourseMain courseMain);
    /*教练课程开课或结束*/
    int openOrEndCourse(String courseCode,String status);
    /*教练申请下架课程*/
    int applyEnd(String courseCode);
    /*学生评价课程*/
    int courseCommend(String userCode, String courseCode, String courseComment);
    /*添加点击人数*/
    int addClickNum(String courseCode);
    /*查看课程分类*/
    List<CourseType> queryCourseTypes();
    /*根据分类的code查具体一个的信息*/
    CourseType queryCourseTypeByCode(String typeCode);
    /*查询小节*/
    List<CoursePackage> queryPackages(String courseCode);
    /*查看教练近期小节*/
    List<CoursePackage> queryNearPackages(String userCode);
    /*查询某一小节*/
    CoursePackage queryPackage(String packageCode);
    /*添加小节*/
    int addPackage(CoursePackage coursePackage);
    /*修改小节*/
    int editPackage(CoursePackage coursePackage);
    /*教练课程开课或结束*/
    int openOrEndPackage(String packageCode,String status);
    /*删除小节*/
    int delPackage(String packageCode);

    /*查看购物车*/
    List<ShopCart> queryShopChart(String userCode);
    /*添加购物车*/
    int addShopChart(ShopCart shopCart);
    /*删除购物车*/
    int delShopChart(Integer[] ids);
    /*通过购物车创建订单*/
    Map<String,Object> createOrderByShopChartIds(Integer[] ids);

    /*支付订单*/
    int payOrder(String orderCode,String status);
    /*查看订单*/
    List<Map<String,Object>> queryShopOrders(String userCode);

    /*查看个人课程*/
    List<CourseMain> queryUserCourse(String userCode);
    /*打卡*/
    int putCard(String packageCode,String userCode);

    /*查看个人信息*/
    User queryUserInfo(String userCode);
    /*查看工资信息*/
    List<SalaryMonth> querySalarys(String userCode);
    /*查询教练请假*/
    List<LeaveMain> queryLeaveMains(String userCode);
    /*请假*/
    int addLeaveMain(LeaveMain leaveMain);
    /*撤销请假*/
    int cancelLeaveMain(String packageCode);
    /*查询站内消息*/
    List<SiteMsg> querySiteMsgs(String acceptCode);
    /*已读站消息*/
    int readSiteMsg(Integer id);


}
