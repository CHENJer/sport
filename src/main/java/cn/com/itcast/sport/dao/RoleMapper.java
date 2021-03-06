package cn.com.itcast.sport.dao;

import cn.com.itcast.sport.entry.Role;
import cn.com.itcast.sport.entry.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int countByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int deleteByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    List<Role> selectByExampleWithBLOBs(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    List<Role> selectByExample(RoleExample example);

    List<Role> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    Role selectByPrimaryKey(Integer id);
    /*根据code查询*/
    Role selectByRoleCode(String roleCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByExampleWithBLOBs(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    int updateByPrimaryKey(Role record);
}