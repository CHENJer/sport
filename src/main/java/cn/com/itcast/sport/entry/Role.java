package cn.com.itcast.sport.entry;

public class Role {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.id
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.role_code
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    private String roleCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.role_name
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    private String roleName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    private String isDeleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.comment
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    private String comment;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.id
     *
     * @return the value of role.id
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.id
     *
     * @param id the value for role.id
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.role_code
     *
     * @return the value of role.role_code
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.role_code
     *
     * @param roleCode the value for role.role_code
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.role_name
     *
     * @return the value of role.role_name
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.role_name
     *
     * @param roleName the value for role.role_name
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.is_deleted
     *
     * @return the value of role.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.is_deleted
     *
     * @param isDeleted the value for role.is_deleted
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.comment
     *
     * @return the value of role.comment
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.comment
     *
     * @param comment the value for role.comment
     *
     * @mbggenerated Sun Mar 15 22:34:42 CST 2020
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}