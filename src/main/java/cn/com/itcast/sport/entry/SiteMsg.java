package cn.com.itcast.sport.entry;

import java.util.Date;

public class SiteMsg {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.id
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.accept_code
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private String acceptCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.is_read
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private String isRead;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.title
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.send_time
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private Date sendTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.is_deleted
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private String isDeleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column site_msg.content
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.id
     *
     * @return the value of site_msg.id
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.id
     *
     * @param id the value for site_msg.id
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.accept_code
     *
     * @return the value of site_msg.accept_code
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public String getAcceptCode() {
        return acceptCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.accept_code
     *
     * @param acceptCode the value for site_msg.accept_code
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setAcceptCode(String acceptCode) {
        this.acceptCode = acceptCode == null ? null : acceptCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.is_read
     *
     * @return the value of site_msg.is_read
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public String getIsRead() {
        return isRead;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.is_read
     *
     * @param isRead the value for site_msg.is_read
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setIsRead(String isRead) {
        this.isRead = isRead == null ? null : isRead.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.title
     *
     * @return the value of site_msg.title
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.title
     *
     * @param title the value for site_msg.title
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.send_time
     *
     * @return the value of site_msg.send_time
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.send_time
     *
     * @param sendTime the value for site_msg.send_time
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.is_deleted
     *
     * @return the value of site_msg.is_deleted
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.is_deleted
     *
     * @param isDeleted the value for site_msg.is_deleted
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column site_msg.content
     *
     * @return the value of site_msg.content
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column site_msg.content
     *
     * @param content the value for site_msg.content
     *
     * @mbggenerated Sat Apr 04 15:11:11 CST 2020
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}