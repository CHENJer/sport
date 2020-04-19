package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.SalaryMonthMapper;
import cn.com.itcast.sport.dao.ShopOrderMapper;
import cn.com.itcast.sport.dao.UserMapper;
import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.CapitalService;
import cn.com.itcast.sport.util.StringToDate;
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
 * @date: Created in 2020/3/29 11:30
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class CapitalServiceImpl implements CapitalService {

    @Autowired
    private SalaryMonthMapper salaryMonthMapper;
    @Autowired
    private ShopOrderMapper shopOrderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String,Object> queryIncome(String startTime, String endTime){
        if(startTime == null || endTime == null){
            return null;
        }
        Map<String,Object> incomeMap = new HashMap<>();
        List<String> sumList = new ArrayList<>();
        List<String> month = StringToDate.getMonth(startTime, endTime);
        for (int i = 0; i < month.size(); i++) {
            List<ShopOrder> shopOrders = shopOrderMapper.queryIncomeBytime(month.get(i));
            BigDecimal sum = new BigDecimal("0.00");
            if(shopOrders!=null && shopOrders.size()>0){
                for (int j = 0; j < shopOrders.size(); j++) {
                    sum = sum.add(shopOrders.get(j).getPayPrice());
                }
            }
            sumList.add(sum+"");
        }
        incomeMap.put("month",month.stream().collect(Collectors.joining(",")));
        incomeMap.put("sum",sumList.stream().collect(Collectors.joining(",")));
        return incomeMap;
    }
    @Override
    public Map<String,Object> querySpending(String startTime, String endTime){
        if(startTime == null || endTime == null){
            return null;
        }
        Map<String,Object> spendingMap = new HashMap<>();
        List<String> sumList = new ArrayList<>();
        List<String> month = StringToDate.getMonth(startTime, endTime);
        for (int i = 0; i < month.size(); i++) {
            List<SalaryMonth> salaryMonths = salaryMonthMapper.querySpendingBytime(month.get(i));
            BigDecimal sum = new BigDecimal("0.00");
            if(salaryMonths!=null && salaryMonths.size()>0){
                for (int j = 0; j < salaryMonths.size(); j++) {
                    sum = sum.add(salaryMonths.get(j).getRealMoney());
                }
            }
            sumList.add(sum+"");
        }
        spendingMap.put("month",month.stream().collect(Collectors.joining(",")));
        spendingMap.put("sum",sumList.stream().collect(Collectors.joining(",")));
        return spendingMap;
    }
    @Override
    public List<SalaryMonth> findSalaryByName(String name) {
        List<SalaryMonth> salaryMonths = new ArrayList<>();
        SalaryMonthExample example = new SalaryMonthExample();
        SalaryMonthExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }
        criteria.andIsDeletedEqualTo("0");
        salaryMonths = salaryMonthMapper.selectByExampleWithBLOBs(example);
        return salaryMonths;
    }
    @Override
    public SalaryMonth findSalaryById(Integer id){
        SalaryMonth salaryMonth = salaryMonthMapper.selectByPrimaryKey(id);
        return salaryMonth;
    }

    @Override
    public List<User> findTeacher() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo("1");
        criteria.andIsDeletedEqualTo("0");
        List<User> userList = userMapper.selectByExampleWithBLOBs(example);
        return userList;
    }

    @Override
    public CodeMsg addSalary(SalaryMonth salaryMonth) {
        CodeMsg codeMsg = new CodeMsg();
        if(salaryMonth==null || salaryMonth.getUserCode()==null){
            codeMsg.setCode(500);
            codeMsg.setMsg("必须选择一位教练发放工资！");
            return codeMsg;
        }
        User user = userMapper.selectByUserCode(salaryMonth.getUserCode());
        salaryMonth.setName(user.getName());
        salaryMonth.setIsDeleted("0");
        salaryMonth.setCreateTime(new Date());
        salaryMonth.setSendTime(new Date());
        int insertResult = salaryMonthMapper.insertSelective(salaryMonth);
        if (insertResult>0){
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功！");
        }else{
           codeMsg.setCode(500);
           codeMsg.setMsg("添加数据库失败！");
        }
        return codeMsg;
    }

    @Override
    public CodeMsg editSalary(SalaryMonth salaryMonth) {
        CodeMsg codeMsg = new CodeMsg();
        if(salaryMonth.getId() == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("id不能为空！");
            return codeMsg;
        }
        int updateResult = salaryMonthMapper.updateByPrimaryKeySelective(salaryMonth);
        if (updateResult>0){
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功！");
        }else{
            codeMsg.setCode(500);
            codeMsg.setMsg("修改数据库失败！");
        }
        return codeMsg;
    }
}
