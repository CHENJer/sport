package cn.com.itcast.sport.entry;

import java.util.Date;

public class Account {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.id
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.user_name
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.password
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.role_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String roleCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.user_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String userCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.status
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private String isDeleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.create_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.update_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.id
     *
     * @return the value of account.id
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.id
     *
     * @param id the value for account.id
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.user_name
     *
     * @return the value of account.user_name
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.user_name
     *
     * @param userName the value for account.user_name
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.password
     *
     * @return the value of account.password
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.password
     *
     * @param password the value for account.password
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.role_code
     *
     * @return the value of account.role_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.role_code
     *
     * @param roleCode the value for account.role_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.user_code
     *
     * @return the value of account.user_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.user_code
     *
     * @param userCode the value for account.user_code
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.status
     *
     * @return the value of account.status
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.status
     *
     * @param status the value for account.status
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.is_deleted
     *
     * @return the value of account.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.is_deleted
     *
     * @param isDeleted the value for account.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.create_time
     *
     * @return the value of account.create_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.create_time
     *
     * @param createTime the value for account.create_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.update_time
     *
     * @return the value of account.update_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.update_time
     *
     * @param updateTime the value for account.update_time
     *
     * @mbggenerated Sun Mar 15 22:27:54 CST 2020
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}