package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.CourseMainMapper;
import cn.com.itcast.sport.dao.CoursePackageMapper;
import cn.com.itcast.sport.dao.CourseTypeMapper;
import cn.com.itcast.sport.dao.SiteMsgMapper;
import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.CourseService;
import cn.com.itcast.sport.util.RandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/24 22:27
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseTypeMapper courseTypeMapper;
    @Autowired
    private CourseMainMapper courseMainMapper;
    @Autowired
    private CoursePackageMapper coursePackageMapper;
    @Autowired
    private SiteMsgMapper siteMsgMapper;

    @Override
    public List<CourseType> findSourceType(String typeName) {
        CourseTypeExample example = new CourseTypeExample();
        CourseTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        if(!StringUtils.isEmpty(typeName)){
            criteria.andTypeNameLike("%"+typeName+"%");
        }
        List<CourseType> courseTypes = courseTypeMapper.selectByExample(example);
        return courseTypes;
    }
    @Override
    public CourseType findSourceTypeById(Integer id){
        CourseType courseType = courseTypeMapper.selectByPrimaryKey(id);
        return courseType;
    }
    @Override
    public CodeMsg addSourceType(CourseType courseType) {
        CodeMsg codeMsg = new CodeMsg();
        if(courseType == null){
            codeMsg.setMsg("添加内容不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }
        courseType.setIsDeleted("0");
        courseType.setCreateTime(new Date());
        courseType.setTypeCode(RandomNum.getRandomNum(RandomNum.Type_CODE));
        courseTypeMapper.insertSelective(courseType);
        codeMsg.setMsg("操作成功！");
        codeMsg.setCode(200);
        return codeMsg;
    }



    @Override
    public CodeMsg editSourceType(CourseType courseType) {
        CodeMsg codeMsg = new CodeMsg();
        if(courseType == null){
            codeMsg.setMsg("修改内容不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }else if(courseType.getId() == null){
            codeMsg.setMsg("id不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }
        courseTypeMapper.updateByPrimaryKeySelective(courseType);
        codeMsg.setMsg("操作成功！");
        codeMsg.setCode(200);
        return codeMsg;
    }
    @Override
    public List<CourseMain> findCourseMain(String courseName, String courseType,String isApplyEnd){
        CourseMainExample courseMainExample = new CourseMainExample();
        CourseMainExample.Criteria criteria = courseMainExample.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        if(!StringUtils.isEmpty(courseName)){
            criteria.andCourseNameLike("%"+courseName+"%");
        }
        if(!StringUtils.isEmpty(isApplyEnd)){
            criteria.andIsApplyEndEqualTo(isApplyEnd);
        }
        if(!StringUtils.isEmpty(courseType)){
            switch ( courseType ){
                case "1":
                    criteria.andIsRecommendedEqualTo("1");
                    break;
                case "2":
                    criteria.andStatusEqualTo("2");
                    criteria.andStatusEqualTo("3");
                    break;
                default: break;
            }
        }
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(courseMainExample);
        return courseMains;
    }
    @Override
    public CourseMain findCourseMainById(Integer id){
        CourseMain courseMain = new CourseMain();
        if(id != null){
            courseMain = courseMainMapper.selectByPrimaryKey(id);
        }
        return courseMain;
    }
    @Override
    public CodeMsg editCourseMainStatus(Integer id,String status,String isRecommended){
        CodeMsg codeMsg = new CodeMsg();
        if(id == null){
            codeMsg.setMsg("id不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }
        CourseMain courseMain = courseMainMapper.selectByPrimaryKey(id);
        courseMain.setStatus(status);
        courseMain.setIsRecommended(isRecommended);
        courseMainMapper.updateByPrimaryKeySelective(courseMain);
        codeMsg.setMsg("操作成功！");
        codeMsg.setCode(200);
        return codeMsg;
    }
    @Override
    public CodeMsg reviewCourseMainStatus(Integer id,String isApplyEnd){
        CodeMsg codeMsg = new CodeMsg();
        if(id == null){
            codeMsg.setMsg("id不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }
        if(isApplyEnd == null){
            codeMsg.setMsg("是否同意不能为空！");
            codeMsg.setCode(500);
            return codeMsg;
        }
        CourseMain courseMain = courseMainMapper.selectByPrimaryKey(id);
        courseMain.setIsApplyEnd("0");

        SiteMsg siteMsg = new SiteMsg();
        siteMsg.setAcceptCode(courseMain.getUserCode());
        if("1".equals(isApplyEnd)){
            courseMain.setStatus("3");
            /*审批结果送消息给老师*/
            siteMsg.setTitle("课程【"+courseMain.getCourseName()+"】审批通过！");
            siteMsg.setContent("您发出的课程【"+courseMain.getCourseName()+"】下架申请已通过，课程已下架！");
        }else if("0".equals(isApplyEnd)){
            /*审批结果送消息给老师*/
            siteMsg.setTitle("课程【"+courseMain.getCourseName()+"】审批不通过！");
            siteMsg.setContent("您发出的课程【"+courseMain.getCourseName()+"】下架申请不通过！");
        }
        siteMsg.setIsDeleted("0");
        siteMsg.setIsRead("0");
        siteMsg.setSendTime(new Date());
        siteMsgMapper.insertSelective(siteMsg);
        courseMainMapper.updateByPrimaryKeySelective(courseMain);
        codeMsg.setMsg("操作成功！");
        codeMsg.setCode(200);
        return codeMsg;
    }
    @Override
    public List<CoursePackage> findCoursePackage(String courseCode) {
        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        example.setOrderByClause("'package_num' ASC, 'id' DESC");
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        return coursePackages;
    }
}
