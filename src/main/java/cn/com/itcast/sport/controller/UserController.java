package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.*;
import cn.com.itcast.sport.service.UserService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/15 22:36
 * @version: 1.0
 * @modified By:
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    /*学员管理*/
    @RequestMapping("/student/init")
    public String studentInit() {
        return "web/user-manage/studentManage";
    }
    @RequestMapping("/student/findAll")
    @ResponseBody
    public String studentManage(String name, @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit) {
        //分页初始化
        PageHelper.startPage(page, limit);
        List<User> users = userService.findUsers(name,"0");
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(users,limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg", "");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        // return JsonConversion.writeMapJSON(map);
        return json;
    }
    /*教师管理*/
    @RequestMapping("/teacher/init")
    public String teacherInit() {
        return "web/user-manage/teacherManage";
    }
    @RequestMapping("/teacher/findAll")
    @ResponseBody
    public String teacherManage(String name, @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit) {
        PageHelper.startPage(page, limit);
        List<User> users = userService.findUsers(name,"1");
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(users,limit);
        String msg = null;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg", msg);
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        // return JsonConversion.writeMapJSON(map);
        return json;
    }
    /*添加教练，学员*/
    @RequestMapping("/user/addInit")
    public String userAddInit(Model model ,String type) {
        model.addAttribute("type",type);
        return "web/user-manage/addUser";
    }
    @RequestMapping("/user/addUser")
    @ResponseBody
    public Result addUser(User user){
        CodeMsg codeMsg = userService.addUser(user);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }
    @RequestMapping("/user/editInit")
    public String
    userEditInit(Model model ,Integer id){
        User user = userService.findUserById(id);
        model.addAttribute("user",user);
        return "web/user-manage/editUser";
    }

    @RequestMapping("/user/editUser")
    @ResponseBody
    public Result editUser(User user){
        CodeMsg codeMsg = userService.editUser(user);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }
    /*账号管理*/
    @RequestMapping("/account/init")
    public String accountInit(Model model, HttpSession httpSession) {
        return "web/user-manage/accountManage";
    }
    @RequestMapping("/account/findAll")
    @ResponseBody
    public String accountManage(String userName, @RequestParam(required=false,defaultValue="1") int page,
                                @RequestParam(required=false,defaultValue="10") int limit) {
        PageHelper.startPage(page, limit);
        List<UserAndAccountAndRole> allUserInfo = userService.findAllUserInfo(userName);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(allUserInfo,limit);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg", "");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        // return JsonConversion.writeMapJSON(map);
        return json;
    }
    /*添加账号*/
    @RequestMapping("/account/addInit")
    public String accountAddInit(Model model) {
        List<User> users = userService.findHasNoAccountUsers();
        List<Role> roles = userService.findRoles(null);
        List<Account> accounts = userService.findAccounts(null);
        model.addAttribute("users",users);
        model.addAttribute("roles",roles);
        return "web/user-manage/addAccount";
    }
    @RequestMapping("/account/addAccount")
    @ResponseBody
    public Result addAccount(Account account){
        CodeMsg codeMsg = userService.addAccounts(account);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }
    /*修改账号*/
    @RequestMapping("/account/editInit")
    public String accountEditInit(Model model,Integer id) {
        Account account = userService.findAccountById(id);
        List<Role> roles = userService.findRoles(null);
        model.addAttribute("account",account);
        model.addAttribute("roles",roles);
        return "web/user-manage/editAccount";
    }

    @RequestMapping("/account/editAccount")
    @ResponseBody
    public Result editAccount(Account account){
        CodeMsg codeMsg = userService.editAccount(account);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }

    @RequestMapping("/role/findAll")
    @ResponseBody
    public String roleManage(String roleName, @RequestParam(required=false,defaultValue="1") int page,
                             @RequestParam(required=false,defaultValue="10") int limit) {
        PageHelper.startPage(page, limit);
        List<Role> roles = userService.findRoles(roleName);
        //用PageInfo对结果进行包装,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(roles,limit);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg", "");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        String json= JSON.toJSONString(map);
        //将map转换为json格式
        // return JsonConversion.writeMapJSON(map);
        return json;
    }
    /*添加账号*/
    @RequestMapping("/role/addInit")
    public String roleAddInit(Model model) {
        return "web/user-manage/addRole";
    }
    @RequestMapping("/role/addRole")
    @ResponseBody
    public Result addRole(Role role){
        CodeMsg codeMsg = userService.addRole(role);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }
    @RequestMapping("/role/editInit")
    public String editRoleInit(Model model,Integer id){
        Role role = userService.findRoleById(id);
        model.addAttribute("role",role);
        return "web/user-manage/editRole";
    }
    @RequestMapping("/role/editRole")
    @ResponseBody
    public Result editRole(Role role){
        CodeMsg codeMsg = userService.editRole(role);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }

    /*更改密码页面*/
    @RequestMapping("/editAccountPwd")
    public String editAccountPwd(Model model, HttpSession httpSession){
        Account account = userService.findUserInfo(httpSession);
        model.addAttribute("account",account);
        return "web/login/editAccountPwd";
    }
    /*更改密码*/
    @RequestMapping("/submitAccountPwd")
    @ResponseBody
    public Result submitAccountPwd(EditAccountPwd editAccount){
        CodeMsg codeMsg = userService.editAccountPwd(editAccount);
        Result<CodeMsg> success = Result.success(codeMsg);
        return success;
    }

}
