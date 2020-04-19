package cn.com.itcast.sport.service;

import cn.com.itcast.sport.entry.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/17 17:44
 * @version: 1.0
 * @modified By:
 */
public interface UserService {
    /*查询个人用户*/
    Account findUserInfo(HttpSession httpSession);
    // 查询所有账号信息
    List<Account> findAccounts(String userName);
    // 查询账号信息
    Account findAccountById(Integer id);
    /*添加账号*/
    CodeMsg addAccounts(Account account);
    /*修改账号*/
    CodeMsg editAccount(Account account);
    // 查询所有用户信息
    List<User> findUsers(String name,String type);
    // 查询单个用户信息
    User findUserById(Integer id);
    /*查询还没有账号的用户信息*/
    List<User> findHasNoAccountUsers();
    /*添加用户信息*/
    CodeMsg addUser(User user);
    /*修改，编辑和删除用户信息*/
    CodeMsg editUser(User user);
    // 查询所有权限信息
    List<Role> findRoles(String roleName);
    // 查找角色
    Role findRoleById(Integer id);
    /*添加角色*/
    CodeMsg addRole(Role role);
    /*修改角色*/
    CodeMsg editRole(Role role);
    /*查询所有用户的账号权限信息*/
    List<UserAndAccountAndRole> findAllUserInfo(String userName);
    /*修改密码*/
    CodeMsg editAccountPwd(EditAccountPwd editAccount);


}
