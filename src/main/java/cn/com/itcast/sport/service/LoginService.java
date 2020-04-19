package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.CodeMsg;

import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/16 16:34
 * @version:
 * @modified By:
 */
public interface LoginService {
    public CodeMsg checkLogin(HttpSession session, String userName, String passWord);
    public Boolean outLogin(HttpSession session, String userCode);
}
