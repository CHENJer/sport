package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.CodeMsg;
import cn.com.itcast.sport.entry.Result;
import cn.com.itcast.sport.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/16 14:45
 * @version: 1.0
 * @modified By:
 */
@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("/loginPage")
    public String login(){
        return "web/login/loginPage";
    }
    @RequestMapping("/isLogin")
    @ResponseBody
    public Result isLogin(HttpSession session, String userName, String passWord){
        CodeMsg codeMsg = loginService.checkLogin(session, userName, passWord);
        Result<CodeMsg> resultMsf =  Result.success(codeMsg);;
        return resultMsf;
    }
    @RequestMapping("/outLogin")
    public Result outLogin(HttpSession session,String userCode){
        Boolean isOut = loginService.outLogin(session, userCode);
        Result resultMsf = null;
        if(isOut){
            resultMsf = Result.success("退出成功！");
        }else{
            resultMsf = Result.error(new CodeMsg(404,"未知用户！"));

        }
        return resultMsf;
    }
    @RequestMapping("/index")
    public String loginIndex(){
        return "web/login/index";
    }


}
