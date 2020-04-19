package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.*;
import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.ApiService;
import cn.com.itcast.sport.util.RandomNum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/4/4 16:40
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class ApiServiceImpl implements ApiService {
    public final static Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private CourseMainMapper courseMainMapper;
    @Autowired
    private CoursePackageMapper coursePackageMapper;
    @Autowired
    private CourseTypeMapper courseTypeMapper;
    @Autowired
    private LeaveMainMapper leaveMainMapper;
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private ShopOrderMapper shopOrderMapper;
    @Autowired
    private OrderCourseMapper orderCourseMapper;
    @Autowired
    private SalaryMonthMapper salaryMonthMapper;
    @Autowired
    private SiteMsgMapper siteMsgMapper;
    @Autowired
    private PackageUserMapper packageUserMapper;

    @Override
    public Result checkLogin(String userName, String password) {
        CodeMsg codeMsg = new CodeMsg();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            codeMsg.setMsg("账号密码不能为空");
            codeMsg.setCode(500);
            return Result.error(codeMsg);
        }
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserNameEqualTo(userName);
        criteria.andPasswordEqualTo(password);
        List<Account> accounts = accountMapper.selectByExample(example);
        if(accounts == null || accounts.size() == 0){
            codeMsg.setMsg("账号或密码错误");
            codeMsg.setCode(500);
            return Result.error(codeMsg);
        }
        if("1".equals(accounts.get(0).getStatus())){
            codeMsg.setMsg("账号被冻结");
            codeMsg.setCode(500);
            return Result.error(codeMsg);
        }
        User user = userMapper.selectByUserCode(accounts.get(0).getUserCode());
        if("2".equals(user.getType())){
            codeMsg.setMsg("账号不能为管理员");
            codeMsg.setCode(500);
            return Result.error(codeMsg);
        }
        return  Result.success(user);
    }

    @Override
    public List<CourseMain> queryCourses(String courseName,String typeCode) {
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andStatusNotEqualTo("3");
        if(!StringUtils.isEmpty(courseName) && !"''".equals(courseName)){
           criteria.andCourseNameLike("%"+courseName+"%");
        }
        if(!StringUtils.isEmpty(typeCode) && !"''".equals(typeCode)){
            criteria.andCourseTypeCodeEqualTo(typeCode);
        }
        example.setOrderByClause("'start_time' ASC");
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        return courseMains;
    }
    @Override
    public  List<CourseMain> queryTeacherCourses(String courseName,String typeCode,String userCode) {
        if(StringUtils.isEmpty(userCode) && "''".equals(userCode)){
            return null;
        }
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(courseName) && !"''".equals(courseName)){
            criteria.andCourseNameLike("%"+courseName+"%");
        }
        if(!StringUtils.isEmpty(typeCode) && !"''".equals(typeCode)){
            criteria.andCourseTypeCodeEqualTo(typeCode);
        }
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);
        example.setOrderByClause("'start_time' ASC,'status' ASC");
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        return courseMains;
    }

    @Override
    public List<CourseMain> queryRecommendedCourses(String courseName) {
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andStatusNotEqualTo("3");
        if(!StringUtils.isEmpty(courseName) && !"''".equals(courseName)){
            criteria.andCourseNameLike("%"+courseName+"%");
        }
        criteria.andIsRecommendedEqualTo("1");
        criteria.andStatusBetween("0","1");
        example.setOrderByClause("'start_time' ASC");
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        return courseMains;
    }

    @Override
    public CourseMain queryCourse(String userCode,String courseCode){
        if( StringUtils.isEmpty(courseCode) || "''".equals(courseCode) ){
            return null;
        }
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        if(userCode!=null && !"''".equals(userCode)){
            courseMains.get(0).setIsHas(checkIsHasCourse(userCode,courseCode));
        }
        if(courseMains.get(0)!=null){
            User user = this.queryUserInfo(courseMains.get(0).getUserCode());
            courseMains.get(0).setTeachPic(user.getTeachPic());
        }
        OrderCourseExample ocEx = new OrderCourseExample();
        OrderCourseExample.Criteria ocCr = ocEx.createCriteria();
        ocCr.andIsDeletedEqualTo("0");
        ocCr.andCourseCodeEqualTo(courseCode);
        ocCr.andCourseCommentIsNotNull();
        List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(ocEx);
        if(orderCourses == null || orderCourses.size() == 0){
            courseMains.get(0).setCourseCommend(new ArrayList<>());
        }else{
            List<HashMap<String,String>> commendList = new ArrayList<>();
            for (int i = 0; i < orderCourses.size(); i++) {
                User user = queryUserInfo(orderCourses.get(i).getUserCode());
                HashMap<String,String> userAndCommendInfo = new HashMap<>();
                userAndCommendInfo.put("userName",user.getName());
                userAndCommendInfo.put("userCode",user.getUserCode());
                userAndCommendInfo.put("courseCommend",orderCourses.get(i).getCourseComment());
                commendList.add(userAndCommendInfo);
            }
            courseMains.get(0).setCourseCommend(commendList);
        }
        return courseMains.get(0);
    }

    public String checkIsHasCourse(String userCode,String courseCode){
        OrderCourseExample orderCourseExample = new OrderCourseExample();
        OrderCourseExample.Criteria criteria1 = orderCourseExample.createCriteria();
        criteria1.andIsDeletedEqualTo("0");
        criteria1.andCourseCodeEqualTo(courseCode);
        criteria1.andUserCodeEqualTo(userCode);
        List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(orderCourseExample);
        if(orderCourses!=null && orderCourses.size()>0){
            return "1";
        }
        return "0";
    }
    @Override
    public int addCourse(CourseMain courseMain) {
        if(courseMain == null){
            return -1;
        }
        courseMain.setCourseCode(RandomNum.getRandomNum(RandomNum.SOURCE_CODE));
        courseMain.setClickNum(0);
        courseMain.setBuyNum(0);
        courseMain.setStatus("0");
        courseMain.setIsRecommended("0");
        courseMain.setIsDeleted("0");
        courseMain.setCreateTime(new Date());
        return courseMainMapper.insertSelective(courseMain);
    }

    @Override
    public int editCourse(CourseMain courseMain) {
        if(courseMain == null || StringUtils.isEmpty(courseMain.getCourseCode())){
            return -1;
        }
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseMain.getCourseCode());
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        if(courseMains == null){
            return -1;
        }
        if(courseMain.getStatus()!=null && "1".equals(courseMain.getStatus())){
            /*开课发送消息给老师*/
            SiteMsg siteMsg = new SiteMsg();
            siteMsg.setAcceptCode(courseMain.getUserCode());
            siteMsg.setTitle("课程【"+courseMain.getCourseName()+"】开课中！");
            siteMsg.setContent("您负责的课程【"+courseMain.getCourseName()+"】正在开课！");
            this.sendSiteMsg(siteMsg);
            /*学生*/
            OrderCourseExample example2 = new OrderCourseExample();
            OrderCourseExample.Criteria criteria2 = example2.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andCourseCodeEqualTo(courseMain.getCourseCode());
            List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example2);
            if(orderCourses != null || orderCourses.size() > 0){
                for (int i = 0; i < orderCourses.size(); i++) {
                    SiteMsg toStudentMsg = new SiteMsg();
                    toStudentMsg.setAcceptCode(courseMain.getUserCode());
                    toStudentMsg.setTitle("课程【"+courseMain.getCourseName()+"】开课中！");
                    toStudentMsg.setContent("您参加的课程【"+courseMain.getCourseName()+"】正在开课中，请及时参加！");
                    this.sendSiteMsg(toStudentMsg);
                }
            }
        }
        courseMain.setId(courseMains.get(0).getId());
        return courseMainMapper.updateByPrimaryKeySelective(courseMain);
    }
    @Override
    public  int openOrEndCourse(String courseCode,String status) {
        if(courseCode == null || StringUtils.isEmpty(courseCode)|| "''".equals(courseCode) ){
            return -1;
        }
        if(status == null || StringUtils.isEmpty(status)|| "''".equals(status) ){
            return -1;
        }
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        if(courseMains == null){
            return -1;
        }
        if(courseMains!=null && courseMains.size()>0){
            if("1".equals(status)){
                /*开课发送消息给老师*/
                SiteMsg siteMsg = new SiteMsg();
                siteMsg.setAcceptCode(courseMains.get(0).getUserCode());
                siteMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】开课中！");
                siteMsg.setContent("您负责的课程【"+courseMains.get(0).getCourseName()+"】正在开课！");
                this.sendSiteMsg(siteMsg);
                /*学生*/
                OrderCourseExample example2 = new OrderCourseExample();
                OrderCourseExample.Criteria criteria2 = example2.createCriteria();
                criteria2.andIsDeletedEqualTo("0");
                criteria2.andCourseCodeEqualTo(courseMains.get(0).getCourseCode());
                List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example2);
                if(orderCourses != null || orderCourses.size() > 0){
                    for (int i = 0; i < orderCourses.size(); i++) {
                        SiteMsg toStudentMsg = new SiteMsg();
                        toStudentMsg.setAcceptCode(courseMains.get(0).getUserCode());
                        toStudentMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】开课中！");
                        toStudentMsg.setContent("您参加的课程【"+courseMains.get(0).getCourseName()+"】正在开课中，请及时参加！");
                        this.sendSiteMsg(toStudentMsg);
                    }
                }
            }
            CourseMain courseMain = courseMains.get(0);
            courseMain.setStatus(status);
            courseMain.setUserName("1111");
            int update = courseMainMapper.updateByPrimaryKey(courseMain);
            return update;
        }
        return 1;
    }
    @Override
    public int applyEnd(String courseCode){
        if(courseCode == null || StringUtils.isEmpty(courseCode)|| "''".equals(courseCode) ){
            return -1;
        }
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        if(courseMains == null){
            return -1;
        }
        if(courseMains!=null && courseMains.size()>0){
            /*申请下架发送消息给老师*/
            SiteMsg siteMsg = new SiteMsg();
            siteMsg.setAcceptCode(courseMains.get(0).getUserCode());
            siteMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】申请下架中！");
            siteMsg.setContent("您负责的课程【"+courseMains.get(0).getCourseName()+"】下架申请已发送到后台，请等待管理员审核！");
            this.sendSiteMsg(siteMsg);
            CourseMain courseMain = courseMains.get(0);
            courseMain.setIsApplyEnd("1");
            int update = courseMainMapper.updateByPrimaryKey(courseMain);
            return update;
        }
        return 1;
    }
    @Override
    public int courseCommend(String userCode, String courseCode, String courseComment){
        if(userCode == null || StringUtils.isEmpty(userCode)|| "''".equals(userCode) ){
            return -1;
        }
        if(courseCode == null || StringUtils.isEmpty(courseCode)|| "''".equals(courseCode) ){
            return -1;
        }
        if(courseComment == null || StringUtils.isEmpty(courseComment)|| "''".equals(courseComment) ){
            return -1;
        }
        OrderCourseExample example = new OrderCourseExample();
        OrderCourseExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);
        criteria.andCourseCodeEqualTo(courseCode);
        List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example);
        if(orderCourses == null || orderCourses.size() == 0){
            return 0;
        }
        OrderCourse orderCourse = orderCourses.get(0);
        orderCourse.setCourseComment(courseComment);
        orderCourseMapper.updateByPrimaryKeySelective(orderCourse);
        return 1;
    }
    @Override
    public int addClickNum(String courseCode){
        CourseMainExample example = new CourseMainExample();
        CourseMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example);
        if(courseMains == null){
            return -1;
        }
        int clickNum =courseMains.get(0).getClickNum() == null ? 0 : courseMains.get(0).getClickNum();
        courseMains.get(0).setClickNum(clickNum+1);
        return courseMainMapper.updateByPrimaryKeySelective(courseMains.get(0));
    }

    /*查看课程分类*/
    @Override
    public List<CourseType> queryCourseTypes(){
        CourseTypeExample example = new CourseTypeExample();
        CourseTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        List<CourseType> courseTypes = courseTypeMapper.selectByExample(example);
        return courseTypes;
    }
    /*根据分类的code查具体一个的信息*/
    @Override
    public CourseType queryCourseTypeByCode(String typeCode){
        if(StringUtils.isEmpty(typeCode) || "''".equals(typeCode)){
            return null;
        }
        CourseTypeExample example = new CourseTypeExample();
        CourseTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andTypeCodeEqualTo(typeCode);
        List<CourseType> courseTypes = courseTypeMapper.selectByExample(example);
        return courseTypes.get(0);
    }

    @Override
    public List<CoursePackage> queryPackages(String courseCode) {
        if(StringUtils.isEmpty(courseCode) || "''".equals(courseCode)){
            return null;
        }
        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andCourseCodeEqualTo(courseCode);
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        if(coursePackages!=null){
            for (int i = 0; i < coursePackages.size(); i++) {
                String isSign = checkPutCardStatus(coursePackages.get(i).getPackageCode());
                coursePackages.get(i).setIsPutCard(isSign);
            }
        }

        return coursePackages;
    }
    @Override
    public List<CoursePackage> queryNearPackages(String userCode) {
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return null;
        }
        List<CoursePackage> coursePackages = coursePackageMapper.queryNearPackages(userCode);
        return coursePackages;
    }
    public String checkPutCardStatus(String packageCode){
        if(StringUtils.isEmpty(packageCode) || "''".equals(packageCode)) {
            return "0";
        }
        PackageUserExample example = new PackageUserExample();
        PackageUserExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(packageCode);
        criteria.andIsSignInEqualTo("1");
        List<PackageUser> packageUsers = packageUserMapper.selectByExample(example);
        if(packageUsers!=null && packageUsers.size()>0){
            return "1";
        }
        return "0";
    }
    @Override
    public CoursePackage queryPackage(String packageCode) {
        if(StringUtils.isEmpty(packageCode) || "''".equals(packageCode)){
            return null;
        }
        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(packageCode);
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        if(coursePackages == null || coursePackages.size() == 0){
            return null;
        }
        return coursePackages.get(0);
    }

    @Override
    public int addPackage(CoursePackage coursePackage) {
        if(coursePackage == null){
            return  -1;
        }
        coursePackage.setPackageCode(RandomNum.getRandomNum(RandomNum.PACKAGE_CODE));
        coursePackage.setStatus("0");
        coursePackage.setIsDeleted("0");
        return coursePackageMapper.insertSelective(coursePackage);
    }

    @Override
    public int editPackage(CoursePackage coursePackage) {
        if(coursePackage == null || coursePackage.getPackageCode() == null){
            return  -1;
        }
        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(coursePackage.getPackageCode());
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        if(coursePackages == null || coursePackages.size() == 0){
            return -1;
        }
        coursePackage.setId(coursePackages.get(0).getId());

        return coursePackageMapper.updateByPrimaryKeySelective(coursePackage);
    }
    @Override
    public int openOrEndPackage(String packageCode,String status){
        if(packageCode == null || StringUtils.isEmpty(packageCode)|| "''".equals(packageCode) ){
            return -1;
        }
        if(status == null || StringUtils.isEmpty(status)|| "''".equals(status) ){
            return -1;
        }
        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(packageCode);
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        if(coursePackages == null){
            return -1;
        }
        if(coursePackages!=null && coursePackages.size()>0){
            /*更新小节状态*/
            coursePackages.get(0).setStatus(status);
            coursePackageMapper.updateByPrimaryKeySelective(coursePackages.get(0));

            /*查询小节关联的课程*/
            CourseMainExample example2 = new CourseMainExample();
            CourseMainExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andIsDeletedEqualTo("0");
            criteria2.andCourseCodeEqualTo(coursePackages.get(0).getCourseCode());
            List<CourseMain> courseMains = courseMainMapper.selectByExampleWithBLOBs(example2);
            if("1".equals(status)){
                /*判断课程是否未开启*/
                if("0".equals(courseMains.get(0).getStatus())){
                    courseMains.get(0).setStatus("1");
                    courseMainMapper.updateByPrimaryKeySelective(courseMains.get(0));
                    /*开课发送消息给老师*/
                    SiteMsg siteMsg = new SiteMsg();
                    siteMsg.setAcceptCode(courseMains.get(0).getUserCode());
                    siteMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】开课中！");
                    siteMsg.setContent("您负责的课程【"+courseMains.get(0).getCourseName()+"】的"+coursePackages.get(0).getPackageName()+"小节已开始！");
                    this.sendSiteMsg(siteMsg);
                    /*学生*/
                    OrderCourseExample example3 = new OrderCourseExample();
                    OrderCourseExample.Criteria criteria3 = example3.createCriteria();
                    criteria3.andIsDeletedEqualTo("0");
                    criteria3.andCourseCodeEqualTo(courseMains.get(0).getCourseCode());
                    List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example3);
                    if(orderCourses != null || orderCourses.size() > 0){
                        SiteMsg toStudentMsg = new SiteMsg();
                        toStudentMsg.setAcceptCode(courseMains.get(0).getUserCode());
                        toStudentMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】开课中！");
                        toStudentMsg.setContent("您参加的课程【"+courseMains.get(0).getCourseName()+"】的"+coursePackages.get(0).getPackageName()+"小节已开始，请及时参加！");
                        this.sendSiteMsg(toStudentMsg);
                    }
                }

            }else if("2".equals(status)){
                /*如果小节结束，检查是否存在未开课的小节，如果没有，则课程结束*/
                CoursePackageExample endEx = new CoursePackageExample();
                CoursePackageExample.Criteria endCr = endEx.createCriteria();
                endCr.andIsDeletedEqualTo("0");
                endCr.andCourseCodeEqualTo(coursePackages.get(0).getCourseCode());
                List<CoursePackage> endPackages = coursePackageMapper.selectByExample(endEx);
                List<CoursePackage> noEndpackages = endPackages.stream().filter(e -> e.getStatus() != null && "0".equals(e.getStatus())).collect(Collectors.toList());
                /*说明不存在未开课的小节，则更新课程状态为结束*/
                if(noEndpackages == null || noEndpackages.size()==0){
                    courseMains.get(0).setStatus("2");
                    courseMainMapper.updateByPrimaryKeySelective(courseMains.get(0));
                    /*开课发送消息给老师*/
                    SiteMsg siteMsg = new SiteMsg();
                    siteMsg.setAcceptCode(courseMains.get(0).getUserCode());
                    siteMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】已结束！");
                    siteMsg.setContent("您负责的课程【"+courseMains.get(0).getCourseName()+"】已顺利结束！");
                    this.sendSiteMsg(siteMsg);
                    /*学生*/
                    OrderCourseExample example3 = new OrderCourseExample();
                    OrderCourseExample.Criteria criteria3 = example3.createCriteria();
                    criteria3.andIsDeletedEqualTo("0");
                    criteria3.andCourseCodeEqualTo(courseMains.get(0).getCourseCode());
                    List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example3);
                    if(orderCourses != null || orderCourses.size() > 0){
                        for (int i = 0; i < orderCourses.size(); i++) {
                            SiteMsg toStudentMsg = new SiteMsg();
                            toStudentMsg.setAcceptCode(courseMains.get(0).getUserCode());
                            toStudentMsg.setTitle("课程【"+courseMains.get(0).getCourseName()+"】已结课！");
                            toStudentMsg.setContent("您参加的课程【"+courseMains.get(0).getCourseName()+"】已结课！");
                            this.sendSiteMsg(toStudentMsg);
                        }
                    }
                }

            }

        }
        return 1;
    }
    @Override
    public int delPackage(String packageCode){
        if(packageCode == null || StringUtils.isEmpty(packageCode)|| "''".equals(packageCode) ){
            return -1;
        }

        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(packageCode);
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        if(coursePackages == null){
            return -1;
        }
        coursePackages.get(0).setIsDeleted("1");
        coursePackageMapper.updateByPrimaryKeySelective(coursePackages.get(0));
        return 1;
    }
    @Override
    public List<ShopCart> queryShopChart(String userCode) {
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return null;
        }
        ShopCartExample example = new ShopCartExample();
        ShopCartExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);
        List<ShopCart> shopCarts = shopCartMapper.selectByExample(example);
        if(shopCarts!=null){
            for (int i = 0; i < shopCarts.size(); i++) {
                CourseMain courseMain = queryCourse(null, shopCarts.get(i).getCourseCode());
                shopCarts.get(i).setCourseDesc(courseMain.getCourseDesc());
            }
        }

        return shopCarts;
    }

    @Override
    public int addShopChart(ShopCart shopCart) {
        if(shopCart == null || shopCart.getUserCode() == null || shopCart.getCourseCode() == null){
            return -1;
        }
        CourseMain courseMain = queryCourse(null, shopCart.getCourseCode());
        shopCart.setCoursePic(courseMain.getCoursePic());
        shopCart.setCoursePrice(courseMain.getCoursePrice());
        shopCart.setCreateTime(new Date());
        shopCart.setIsDeleted("0");
        return shopCartMapper.insertSelective(shopCart);
    }
    @Override
    public int delShopChart(Integer[] ids){
        if(ids == null || ids.length == 0){
            return -1;
        }
        for (Integer id: ids) {
            ShopCart shopCart = shopCartMapper.selectByPrimaryKey(id);
            shopCart.setIsDeleted("1");
            shopCartMapper.updateByPrimaryKeySelective(shopCart);
        }

        return 1;
    }
    @Override
    public Map createOrderByShopChartIds(Integer[] ids){
        if(ids == null || ids.length == 0){
            return null;
        }
        BigDecimal sumPrice = new BigDecimal("0.00");
        String userCode = null;
        List<OrderCourse> orderCourseList = new ArrayList<>();
        for (Integer id: ids) {
            ShopCart shopCart = shopCartMapper.selectByPrimaryKey(id);
            sumPrice = sumPrice.add(shopCart.getCoursePrice());
            userCode = shopCart.getUserCode();
        }
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setOrderCode(RandomNum.getRandomNum(RandomNum.ORDER_CODE));
        shopOrder.setStatus("0");
        shopOrder.setCreateTime(new Date());
        shopOrder.setIsDeleted("0");
        shopOrder.setPayPrice(sumPrice);
        shopOrder.setUserCode(userCode);
        shopOrderMapper.insertSelective(shopOrder);

        for (Integer id: ids) {
            ShopCart shopCart = shopCartMapper.selectByPrimaryKey(id);
            shopCart.setIsDeleted("1");
            shopCartMapper.updateByPrimaryKeySelective(shopCart);

            OrderCourse orderCourse = new OrderCourse();
            orderCourse.setCourseCode(shopCart.getCourseCode());
            orderCourse.setCourseName(shopCart.getCourseName());
            orderCourse.setCoursePic(shopCart.getCoursePic());
            orderCourse.setCoursePrice(shopCart.getCoursePrice());
            orderCourse.setCreateTime(new Date());
            orderCourse.setIsDeleted("0");
            orderCourse.setStatus("0");
            orderCourse.setOrderCode(shopOrder.getOrderCode());
            orderCourse.setUserCode(userCode);
            orderCourseMapper.insertSelective(orderCourse);
            orderCourseList.add(orderCourse);
        }
        HashMap<String,Object> orderObejct = new HashMap<>();
        orderObejct.put("order",shopOrder);
        orderObejct.put("course",orderCourseList);

        return orderObejct;
    }

    @Override
    public int payOrder(String orderCode,String status){
        if(orderCode == null || "''".equals(orderCode) || status == null || "''".equals(status)){
            return -1;
        }
        ShopOrderExample example = new ShopOrderExample();
        ShopOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andOrderCodeEqualTo(orderCode);
        List<ShopOrder> shopOrders = shopOrderMapper.selectByExample(example);
        ShopOrder shopOrder = shopOrders.get(0);
        shopOrder.setStatus(status);
        if(status.equals("1")){
            shopOrder.setPayTime(new Date());
        }
        shopOrderMapper.updateByPrimaryKeySelective(shopOrder);

        OrderCourseExample orderCourseExample = new OrderCourseExample();
        OrderCourseExample.Criteria criteria1 = orderCourseExample.createCriteria();
        criteria1.andIsDeletedEqualTo("0");
        criteria1.andOrderCodeEqualTo(shopOrder.getOrderCode());
        List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(orderCourseExample);
        String orderName = "";
        for (int i = 0; i < orderCourses.size(); i++) {
            orderCourses.get(i).setStatus(status);

            orderCourseMapper.updateByPrimaryKeySelective(orderCourses.get(i));
            orderName = orderName+" 【"+orderCourses.get(i).getCourseName()+"】 ";
        }
        if(status != null && "1".equals(status) ){
            SiteMsg siteMsg = new SiteMsg();
            siteMsg.setAcceptCode(shopOrder.getUserCode());
            siteMsg.setTitle("订单已付款成功");
            siteMsg.setContent("您的订单:"+shopOrder.getOrderCode()+"已付款成功!其中包含课程"+orderName);
            this.sendSiteMsg(siteMsg);
            for (int i = 0; i < orderCourses.size(); i++) {
                CourseMain courseMain = queryCourse(null, orderCourses.get(i).getCourseCode());
                Integer buyNum = courseMain.getBuyNum();
                if(buyNum == null){
                    buyNum = 1;
                }else{
                    buyNum = buyNum+1;
                }
                courseMain.setBuyNum(buyNum);
                courseMainMapper.updateByPrimaryKeySelective(courseMain);
            }
        }
        if(status != null && "0".equals(status) ){
            SiteMsg siteMsg = new SiteMsg();
            siteMsg.setAcceptCode(shopOrder.getUserCode());
            siteMsg.setTitle("订单已成功取消");
            siteMsg.setContent("您的订单:"+shopOrder.getOrderCode()+"已成功取消!");
            this.sendSiteMsg(siteMsg);
        }
        return 1;
    }
    @Override
    public List<Map<String,Object>> queryShopOrders(String userCode) {
        if(userCode == null || "''".equals(userCode)){
            return null;
        }
        ShopOrderExample example = new ShopOrderExample();
        example.setOrderByClause("'create_time' DESC");
        ShopOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);

        List<ShopOrder> shopOrders = shopOrderMapper.selectByExample(example);

        List<Map<String,Object>> shopOrderList = new ArrayList<>();
        for (ShopOrder shopOrder:shopOrders) {
            Map<String,Object> map = new HashMap<>();
            OrderCourseExample orderCourseExample = new OrderCourseExample();
            OrderCourseExample.Criteria criteria1 = orderCourseExample.createCriteria();
            criteria1.andIsDeletedEqualTo("0");
            criteria1.andOrderCodeEqualTo(shopOrder.getOrderCode());
            List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(orderCourseExample);
            for (int i = 0; i < orderCourses.size(); i++) {
                if(orderCourses.get(i).getCourseComment() == null){
                    orderCourses.get(i).setCourseComment("");
                }
            }
            map.put("order",shopOrder);
            map.put("course",orderCourses);
            shopOrderList.add(map);
        }

        return shopOrderList;
    }

    @Override
    public List<CourseMain> queryUserCourse(String userCode) {
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return null;
        }
        OrderCourseExample example = new OrderCourseExample();
        OrderCourseExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);
        List<OrderCourse> orderCourses = orderCourseMapper.selectByExample(example);
        if(orderCourses == null || orderCourses.size() == 0){
            return null;
        }
        List<CourseMain> courseMains = new ArrayList<>();
        for (OrderCourse orderCourse: orderCourses) {
            CourseMainExample courseMainExample = new CourseMainExample();
            CourseMainExample.Criteria criteria1 = courseMainExample.createCriteria();
            criteria1.andIsDeletedEqualTo("0");
            criteria1.andCourseCodeEqualTo(orderCourse.getCourseCode());
            List<CourseMain> userCourses = courseMainMapper.selectByExampleWithBLOBs(courseMainExample);
            if(userCourses != null && userCourses.size() > 0){
                courseMains.add(userCourses.get(0));
            }
        }
        return courseMains;
    }

    @Override
    public int putCard(String packageCode,String userCode) {
        if(StringUtils.isEmpty(packageCode) || "''".equals(packageCode) || StringUtils.isEmpty(userCode) || "''".equals(userCode) ) {
            return -1;
        }
        PackageUser packageUser = new PackageUser();
        packageUser.setIsSignIn("1");
        packageUser.setIsDeleted("0");
        packageUser.setSignTime(new Date());
        packageUser.setUserCode(userCode);
        packageUser.setPackageCode(packageCode);
        packageUser.setRealStartTime(new Date());
        return packageUserMapper.insertSelective(packageUser);
    }

    @Override
    public User queryUserInfo(String userCode) {
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return null;
        }
        User user = userMapper.selectByUserCode(userCode);
        return user;
    }

    @Override
    public List<SalaryMonth> querySalarys(String userCode) {
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return null;
        }
        SalaryMonthExample example = new SalaryMonthExample();
        SalaryMonthExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andUserCodeEqualTo(userCode);
        example.setOrderByClause("'salaryTime' desc");
        List<SalaryMonth> salaryMonths = salaryMonthMapper.selectByExample(example);
        return salaryMonths;
    }
    @Override
    public List<LeaveMain> queryLeaveMains(String userCode){
        if(StringUtils.isEmpty(userCode) || "''".equals(userCode)){
            return new ArrayList<>();
        }
        LeaveMainExample example = new LeaveMainExample();
        LeaveMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andStartUserCodeEqualTo(userCode);
        example.setOrderByClause("'id' asc");
        List<LeaveMain> leaveMains = leaveMainMapper.selectByExample(example);
        return leaveMains;
    }
    @Override
    public int addLeaveMain(LeaveMain leaveMain) {
        if(leaveMain == null || leaveMain.getPackageCode() == null || leaveMain.getStartUserCode() == null){
            return -1;
        }

        CoursePackageExample example = new CoursePackageExample();
        CoursePackageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andPackageCodeEqualTo(leaveMain.getPackageCode());
        List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
        CoursePackage coursePackage = coursePackages.get(0);
        if(!"0".equals(coursePackage.getStatus())){
            return -2;
        }

        CourseMainExample mainExample = new CourseMainExample();
        CourseMainExample.Criteria mainCriteria = mainExample.createCriteria();
        mainCriteria.andIsDeletedEqualTo("0");
        mainCriteria.andCourseCodeEqualTo(coursePackage.getCourseCode());
        List<CourseMain> courseMains = courseMainMapper.selectByExample(mainExample);
        CourseMain courseMain = courseMains.get(0);

        leaveMain.setCourseName(courseMain.getCourseName());
        leaveMain.setCourseCode(courseMain.getCourseCode());
        leaveMain.setPackageName(coursePackage.getPackageName());
        leaveMain.setPackageCode(leaveMain.getPackageCode());
        leaveMain.setPackageBeforeStartTime(coursePackage.getStartTime());
        leaveMain.setPackageBeforeEndTime(coursePackage.getEndTime());
        leaveMain.setIsDeleted("0");
        leaveMain.setStatus("0");

        SiteMsg siteMsg = new SiteMsg();
        siteMsg.setAcceptCode(leaveMain.getStartUserCode());
        siteMsg.setTitle("请假审核已提交！");
        siteMsg.setContent("小节【"+leaveMain.getPackageName()+"】的请假审批已提交，请等待管理员审批!");
        this.sendSiteMsg(siteMsg);
        return leaveMainMapper.insertSelective(leaveMain);
    }

    @Override
    public int cancelLeaveMain(String packageCode) {
        if(StringUtils.isEmpty(packageCode) || "''".equals(packageCode)){
            return -1;
        }
        LeaveMainExample example = new LeaveMainExample();
        LeaveMainExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andStatusEqualTo("0");
        criteria.andPackageCodeEqualTo(packageCode);
        List<LeaveMain> leaveMains = leaveMainMapper.selectByExample(example);
        leaveMains.get(0).setStatus("-1");

        return leaveMainMapper.updateByPrimaryKeySelective(leaveMains.get(0));
    }

    @Override
    public List<SiteMsg> querySiteMsgs(String acceptCode) {
        if(StringUtils.isEmpty(acceptCode) || "''".equals(acceptCode)){
            return null;
        }
        SiteMsgExample example = new SiteMsgExample();
        SiteMsgExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        criteria.andAcceptCodeEqualTo(acceptCode);
        example.setOrderByClause("'is_read' asc,'send_time' desc");
        List<SiteMsg> siteMsgs = siteMsgMapper.selectByExampleWithBLOBs(example);
        return siteMsgs;
    }

    @Override
    public int readSiteMsg(Integer id) {
        if(id == null){
            return -1;
        }
        SiteMsg siteMsg = siteMsgMapper.selectByPrimaryKey(id);
        siteMsg.setIsRead("1");
        return siteMsgMapper.updateByPrimaryKeySelective(siteMsg);
    }

    public void sendSiteMsg(SiteMsg siteMsg) {
        if(siteMsg.getAcceptCode() == null){
            logger.error("接收人code不能为空！");
        }
        siteMsg.setIsDeleted("0");
        siteMsg.setIsRead("0");
        siteMsg.setSendTime(new Date());
        siteMsgMapper.insertSelective(siteMsg);
        logger.info("发送信息成功！");
    }
}
