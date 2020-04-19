package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.SalaryMonth;
import cn.com.itcast.sport.entry.User;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/16 16:34
 * @version:
 * @modified By:
 */
public interface CapitalService {
    /*查询收入报表*/
    Map<String,Object> queryIncome(String startTime,String endTime);
    /*查询支出报表*/
    Map<String,Object> querySpending(String startTime,String endTime);
    /*查询所有的资金*/
    public List<SalaryMonth> findSalaryByName(String name);
    public SalaryMonth findSalaryById(Integer id);
    public List<User> findTeacher();
    public CodeMsg addSalary(SalaryMonth salaryMonth);
    public CodeMsg editSalary(SalaryMonth salaryMonth);
}
