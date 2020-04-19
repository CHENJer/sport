package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.AccountMapper;
import cn.com.itcast.sport.dao.RoleMapper;
import cn.com.itcast.sport.dao.UserMapper;
import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.UserService;
import cn.com.itcast.sport.util.RandomNum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/17 17:48
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AccountMapper accountMapper;

    /*登录：查询账号信息*/
    @Override
    public Account findUserInfo(HttpSession httpSession) {
        UserAndAccountAndRole userInfo= new UserAndAccountAndRole();
        String userCode = (String) httpSession.getAttribute("userCode");
        Account account = null;
        if(userCode!=null){
            User user = userMapper.selectByUserCode(userCode);
            account = accountMapper.selectByUserCode(user.getUserCode());
        }
        return account;
    }

    @Override
    public List<Account> findAccounts(String userName) {
        List<Account> accounts = new ArrayList<>();
        if(userName==null){
            accounts = accountMapper.selectAll();
        }else{
            AccountExample accountExample = new AccountExample();
            AccountExample.Criteria criteria = accountExample.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andUserNameLike("%"+ userName +"%");
            accounts = accountMapper.selectByExample(accountExample);
        }
        return accounts;
    }
    /*查询账号*/
    @Override
    public Account findAccountById(Integer id){
        Account account = new Account();
        if(id != null){
            account = accountMapper.selectByPrimaryKey(id);
        }

        return account;
    }
    /*添加账号*/
    @Override
    public CodeMsg addAccounts(Account account){
        CodeMsg codeMsg = new CodeMsg();
        if(account==null){
            codeMsg.setCode(500);
            codeMsg.setMsg("添加的信息不能为空！");
        }else{
            AccountExample accountExample = new AccountExample();
            AccountExample.Criteria criteria = accountExample.createCriteria();
            criteria.andUserNameEqualTo(account.getUserName());
            criteria.andIsDeletedEqualTo("0");
            List<Account> accounts = accountMapper.selectByExample(accountExample);
            if(accounts!=null && accounts.size()>0){
                codeMsg.setCode(500);
                codeMsg.setMsg("账户名已存在！");
            }else{
                account.setIsDeleted("0");
                account.setCreateTime(new Date());
                accountMapper.insertSelective(account);
                User user = userMapper.selectByUserCode(account.getUserCode());
                if("FYROLE01".equals(account.getRoleCode())){
                    user.setType("2");
                }else if("FYROLE02".equals(account.getRoleCode())){
                    user.setType("1");
                }else if("FYROLE03".equals(account.getRoleCode())){
                    user.setType("0");
                }
                userMapper.updateByPrimaryKeySelective(user);
                codeMsg.setCode(200);
                codeMsg.setMsg("操作成功！");
            }

        }
        return  codeMsg;
    }
    /*修改账号*/
    @Override
    public CodeMsg editAccount(Account account){
        CodeMsg codeMsg = new CodeMsg();
        if(account==null || account.getId() == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("修改的信息不能为空！");
        }else{
            account.setUpdateTime(new Date());
            accountMapper.updateByPrimaryKeySelective(account);
            Account selectAccount = accountMapper.selectByPrimaryKey(account.getId());
            User user = userMapper.selectByUserCode(selectAccount.getUserCode());
            if("FYROLE01".equals(account.getRoleCode())){
                user.setType("2");
            }else if("FYROLE02".equals(account.getRoleCode())){
                user.setType("1");
            }else if("FYROLE03".equals(account.getRoleCode())){
                user.setType("0");
            }
            userMapper.updateByPrimaryKeySelective(user);
            codeMsg.setMsg("操作成功");
            codeMsg.setCode(200);
        }
        return  codeMsg;
    }

    /*账号管理：查询所有账号的信息*/
    @Override
    public List<User> findUsers(String name,String type) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIsDeletedEqualTo("0");
        List<User> userList = new ArrayList<>();
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%"+ name +"%");
        }
        if(!StringUtils.isEmpty(type)){
            criteria.andTypeEqualTo(type);
        }
        userList = userMapper.selectByExampleWithBLOBs(userExample);
        return userList;
    }
    // 查询单个用户信息
    @Override
    public User findUserById(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
    // 查询还没有账号的用户信息
    @Override
    public List<User> findHasNoAccountUsers(){
        List<User> users = userMapper.selectAll();
        List<Account> accounts = accountMapper.selectAll();
        if(accounts!=null && accounts.size()>0){
            for (int i = 0; i < accounts.size(); i++) {
                String userCode = accounts.get(i).getUserCode();
                users = users.stream().filter(user -> !user.getUserCode().equals(userCode)).collect(Collectors.toList());
            }
        }
        return users;
    }


    /*添加学员，或者教练*/
    @Override
    public CodeMsg addUser(User user){
        CodeMsg codeMsg = new CodeMsg();
        if(user== null){
            codeMsg.setCode(500);
            codeMsg.setMsg("添加的信息不能为空！");
        }else if(user.getType() == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("type参数为空，不能确定添加的类型！");
        }else{
            user.setUserCode(RandomNum.getRandomNum(RandomNum.USER_CODE));
            user.setIsDeleted("0");
            user.setCreateTime(new Date());
            userMapper.insertSelective(user);
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功");
        }
        return codeMsg;
    }
    /*添加或者删除学员，或者教练*/
    @Override
    public CodeMsg editUser(User user){
        CodeMsg codeMsg = new CodeMsg();
        if(user== null){
            codeMsg.setCode(500);
            codeMsg.setMsg("修改的信息不能为空！");
        }else if(user.getId() == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("id为空，不能修改！");
        }else{
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            /*删除用户，则把账号一起删除*/
            if(user.getIsDeleted()!=null && "1".equals(user.getIsDeleted())){
                Account account = accountMapper.selectByUserCode(user.getUserCode());
                if(account!=null){
                    account.setIsDeleted("1");
                    account.setUpdateTime(new Date());
                    accountMapper.updateByPrimaryKeySelective(account);
                }
            }
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功");
        }
        return codeMsg;
    }

    @Override
    public List<Role> findRoles(String roleName) {
        List<Role> roles = new ArrayList<>();
        if(roleName==null){
            roles = roleMapper.selectAll();
        }else{
            RoleExample roleExample = new RoleExample();
            RoleExample.Criteria criteria = roleExample.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andRoleNameLike("%"+ roleName +"%");
            roles = roleMapper.selectByExampleWithBLOBs(roleExample);
        }

        return roles;
    }

    public Role findRoleById(Integer id){
        Role role = new Role();
        if(id == null){
            return role;
        }
        role = roleMapper.selectByPrimaryKey(id);
        return role;
    }
    /*添加角色*/
    @Override
    public CodeMsg addRole(Role role){
        CodeMsg codeMsg = new CodeMsg();
        if(role== null){
            codeMsg.setCode(500);
            codeMsg.setMsg("修改的信息不能为空！");
        }else{
            RoleExample roleExample = new RoleExample();
            RoleExample.Criteria criteria = roleExample.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andRoleNameEqualTo(role.getRoleName());
            List<Role> isExitRoles = roleMapper.selectByExample(roleExample);
            if(isExitRoles.size() > 0){
                codeMsg.setCode(500);
                codeMsg.setMsg("角色名已存在");
                return codeMsg;
            }
            role.setRoleCode(getRoleCode());
            role.setIsDeleted("0");
            roleMapper.insertSelective(role);
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功");
        }
        return codeMsg;
    }
    public String getRoleCode(){
        List<Role> roles = roleMapper.selectAll();
        String roleCodeNum = roles.get(0).getRoleCode().substring(6,8);
        int index = Integer.valueOf(roleCodeNum) + 1;
        if(index<10){
            return "FYROLE0"+index;
        }
        return "FYROLE"+index;
    }
    /*修改角色*/
    @Override
    public CodeMsg editRole(Role role){
        CodeMsg codeMsg = new CodeMsg();
        if(role== null){
            codeMsg.setCode(500);
            codeMsg.setMsg("修改的信息不能为空！");
        }else if(role.getId() == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("id为空，不能修改！");
        }else{
            roleMapper.updateByPrimaryKeySelective(role);
            codeMsg.setCode(200);
            codeMsg.setMsg("操作成功");
        }
        return codeMsg;
    }

    @Override
    public List<UserAndAccountAndRole> findAllUserInfo(String userName){
        List<UserAndAccountAndRole> userAndAccountAndRoles = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        if(StringUtils.isEmpty(userName)){
            accounts = accountMapper.selectAll();
        }else{
            AccountExample accountExample = new AccountExample();
            AccountExample.Criteria criteria = accountExample.createCriteria();
            criteria.andIsDeletedEqualTo("0");
            criteria.andUserNameLike("%"+ userName +"%");
            accounts = accountMapper.selectByExample(accountExample);
        }
        for (int i = 0; i < accounts.size(); i++) {
            UserAndAccountAndRole userAndAccountAndRole = new UserAndAccountAndRole();
            String userCode = accounts.get(i).getUserCode();
            String roleCode = accounts.get(i).getRoleCode();
            userAndAccountAndRole.setAccountId(accounts.get(i).getId());
            userAndAccountAndRole.setUserName(accounts.get(i).getUserName());
            userAndAccountAndRole.setPassword(accounts.get(i).getPassword());
            userAndAccountAndRole.setStatus(accounts.get(i).getStatus());
            userAndAccountAndRole.setCreateTime(accounts.get(i).getCreateTime());
            User user = userMapper.selectByUserCode(userCode);
            userAndAccountAndRole.setName(user.getName());
            userAndAccountAndRole.setUserCode(user.getUserCode());
            userAndAccountAndRole.setAge(user.getAge());
            userAndAccountAndRole.setSex(user.getSex());
            userAndAccountAndRole.setTel(user.getTel());
            Role role = roleMapper.selectByRoleCode(roleCode);
            userAndAccountAndRole.setRoleName(role.getRoleName());
            userAndAccountAndRole.setRoleCode(role.getRoleCode());
            userAndAccountAndRoles.add(userAndAccountAndRole);
        }
        return userAndAccountAndRoles;
    }
    /*账号设置：修改账号信息*/
    @Override
    public CodeMsg editAccountPwd(EditAccountPwd editAccount) {
        CodeMsg codeMsg = new CodeMsg();
        if(editAccount == null){
            codeMsg.setCode(500);
            codeMsg.setMsg("传入参数不能为空！");
            return codeMsg;
        }
        if(!editAccount.getUpdatePwd().equals(editAccount.getConfirmPwd())){
            codeMsg.setCode(500);
            codeMsg.setMsg("修改密码和确定密码不一致");
            return codeMsg;
        }
        Account account = accountMapper.selectByPrimaryKey(editAccount.getId());
        if(editAccount.getBeforePwd().equals(editAccount.getUpdatePwd())){
            codeMsg.setCode(500);
            codeMsg.setMsg("两次修改密码不能一样");
            return codeMsg;
        }
        if(!account.getPassword().equals(editAccount.getBeforePwd())){
            codeMsg.setCode(500);
            codeMsg.setMsg("原先密码错误！");
            return codeMsg;
        }else{
            account.setPassword(editAccount.getUpdatePwd());
            account.setUpdateTime(new Date());
            accountMapper.updateByPrimaryKeySelective(account);
            codeMsg.setCode(200);
            codeMsg.setMsg("修改账号密码成功！");
        }

        return codeMsg;
    }

}
