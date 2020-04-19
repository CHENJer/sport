package cn.com.itcast.sport.controller;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/10 21:23
 * @version: 1.0
 * @modified By:
 */

import cn.com.itcast.sport.dao.UserMapper;
import cn.com.itcast.sport.entry.FtpProperties;
import cn.com.itcast.sport.util.FileNameUtil;
import cn.com.itcast.sport.util.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class TestController {
    //替换成自己生成的mapper
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/test")
    @ResponseBody
    public Object test(){
        //查询该表的所有数据
        return userMapper.selectByExample(null);
    }

    @PostMapping("/import")
    public String importData(HttpServletRequest req) throws IOException {
        List<String> filePaths= null;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < filePaths.size(); i++) {
            System.out.println("filePath:"+filePaths.get(i));
        }
        return  "ok";
    }


    @RequestMapping("/getThymeleaf")
    public String Welcome(){
        return "login";
        //此处返回值，对应templates的文件名，SpringBoot根据它找到对应Html
    }


}
