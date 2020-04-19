package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.CoursePackageMapper;
import cn.com.itcast.sport.dao.LeaveMainMapper;
import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.LeaveMainService;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/30 22:34
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class LeaveMainServiceImpl implements LeaveMainService {
    @Autowired
    private LeaveMainMapper leaveMainMapper;
    @Autowired
    private CoursePackageMapper coursePackageMapper;
    @Override
    public List<LeaveMain> findAll(String startUserName, String status, Date startTime, Date endTime) {
        List<LeaveMain>  leaveMains = new ArrayList<>();
        LeaveMainExample example = new LeaveMainExample();
        LeaveMainExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(startUserName)){
            criteria.andStartUserNameLike("%"+startUserName+"%");
        }
        if(!StringUtils.isEmpty(status)){
            criteria.andStatusEqualTo(status);
        }
        if(startTime != null){
            criteria.andPackageBeforeStartTimeEqualTo(startTime);
        }
        if(endTime != null){
            criteria.andPackageBeforeStartTimeEqualTo(endTime);
        }
        criteria.andIsDeletedEqualTo("0");
        leaveMains = leaveMainMapper.selectByExampleWithBLOBs(example);
        return leaveMains;
    }

    @Override
    public LeaveMain reviewLeave(Integer id) {
        if(id == null){
            return null;
        }
        LeaveMain leaveMain = leaveMainMapper.selectByPrimaryKey(id);
        return leaveMain;
    }


    @Override
    public CodeMsg editLeaveMain(Integer id,String checkComment,String status) {
        CodeMsg codeMsg = new CodeMsg();
        if(id == null ){
            codeMsg.setCode(500);
            codeMsg.setMsg("id不能为空 ");
            return codeMsg;
        }
        if(status == null ){
            codeMsg.setCode(500);
            codeMsg.setMsg("status不能为空 ");
            return codeMsg;
        }
        LeaveMain leaveMain = leaveMainMapper.selectByPrimaryKey(id);
        /*审批通过更新时间*/
        if("1".equals(status)){
            CoursePackageExample example = new CoursePackageExample();
            CoursePackageExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andPackageCodeEqualTo(leaveMain.getPackageCode());
            List<CoursePackage> coursePackages = coursePackageMapper.selectByExample(example);
            if(!"0".equals(coursePackages.get(0).getStatus())){
                codeMsg.setCode(500);
                codeMsg.setMsg("课程已经开始，不能请假了！ ");
                return codeMsg;
            }
            coursePackages.get(0).setStartTime(leaveMain.getPackageAdjustStartTime());
            coursePackages.get(0).setEndTime(leaveMain.getPackageAdjustEndTime());
            coursePackageMapper.updateByPrimaryKeySelective(coursePackages.get(0));
        }
        leaveMain.setStatus(status);
        leaveMain.setCheckComment(checkComment);
        leaveMain.setCheckTime(new Date());
        leaveMainMapper.updateByPrimaryKeySelective(leaveMain);
        codeMsg.setCode(200);
        codeMsg.setMsg("操作成功！ ");
        return codeMsg;
    }
}
