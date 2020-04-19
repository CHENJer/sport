package cn.com.itcast.sport.dao;

import cn.com.itcast.sport.entry.Account;
import cn.com.itcast.sport.entry.AccountExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int countByExample(AccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int deleteByExample(AccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int insert(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int insertSelective(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    List<Account> selectByExample(AccountExample example);

    List<Account> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    Account selectByPrimaryKey(Integer id);
    /*按照userCode来查*/
    Account selectByUserCode(String userCode);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int updateByExample(@Param("record") Account record, @Param("example") AccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int updateByPrimaryKeySelective(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    int updateByPrimaryKey(Account record);
}