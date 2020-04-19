package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.CourseMain;
import cn.com.itcast.sport.entry.CoursePackage;
import cn.com.itcast.sport.entry.CourseType;

import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/24 22:26
 * @version: 1.0
 * @modified By:
 */
public interface CourseService {

    List<CourseType> findSourceType(String typeName);

    CourseType findSourceTypeById(Integer id);

    CodeMsg addSourceType(CourseType courseType);

    CodeMsg editSourceType(CourseType courseType);

    /*查看全部课程*/
    List<CourseMain> findCourseMain(String courseName,String courseType,String isApplyEnd);
    /*查看一个课程*/
    CourseMain findCourseMainById(Integer id);
    /*设置课程状态*/
    CodeMsg editCourseMainStatus(Integer id,String status,String isRecommended);
    /*审核是否通过申请下架*/
    CodeMsg reviewCourseMainStatus(Integer id,String isApplyEnd);
    /*查看课程小节*/
    List<CoursePackage> findCoursePackage(String courseCode);
}
