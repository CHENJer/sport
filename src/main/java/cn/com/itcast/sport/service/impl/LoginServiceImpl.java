package cn.com.itcast.sport.service.impl;

import cn.com.itcast.sport.dao.AccountMapper;
import cn.com.itcast.sport.dao.UserMapper;
import cn.com.itcast.sport.entry.Account;
import cn.com.itcast.sport.entry.AccountExample;
import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.User;
import cn.com.itcast.sport.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/16 16:35
 * @version: 1.0
 * @modified By:
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public CodeMsg checkLogin(HttpSession session, String userName, String passWord) {
        CodeMsg codeMsg = new CodeMsg();
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andUserNameEqualTo(userName);
        criteria.andPasswordEqualTo(passWord);
        criteria.andIsDeletedEqualTo("0");
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if(accounts!=null && accounts.size()>0){
            if(!"FYROLE01".equals(accounts.get(0).getRoleCode())){
                codeMsg.setMsg("账号非管理员，不能登录！");
                codeMsg.setCode(500);
            }else if(accounts.get(0).getStatus()!=null && "0".equals(accounts.get(0).getStatus())){
                User user = userMapper.selectByUserCode(accounts.get(0).getUserCode());
                session.setAttribute("userCode",user.getUserCode());
                session.setAttribute("userName",user.getName());
                codeMsg.setMsg("登录成功！");
                codeMsg.setCode(200);
            } else{
                codeMsg.setMsg("账号被冻结！");
                codeMsg.setCode(500);
            }
            return codeMsg;
        }else{
            codeMsg.setMsg("账号或者密码错误！");
            codeMsg.setCode(500);
        }
        return codeMsg;
    }

    @Override
    public Boolean outLogin(HttpSession session, String userCode) {
        Boolean isOut = false;
        if(userCode == null ){
            return isOut;
        }
        User user = userMapper.selectByUserCode(userCode);
        if(user == null){
            return isOut;
        }
        session.removeAttribute("userCode");
        session.removeAttribute("userName");
        return isOut;
    }
}
