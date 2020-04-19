package cn.com.itcast.sport.controller;

import cn.com.itcast.sport.entry.FtpProperties;
import cn.com.itcast.sport.entry.Result;
import cn.com.itcast.sport.util.FileNameUtil;
import cn.com.itcast.sport.util.FtpUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: JIAWEI
 * @date: Created in 2020/3/28 15:51
 * @version: 1.0
 * @modified By:
 */
@Controller
public class UploadController {
    @Autowired
    public FtpProperties ftpProperties;

    @RequestMapping("/uploadFile")
    @ResponseBody
    public Result uploadFile(@RequestParam(value = "file" , required = false) MultipartFile multipartFile){
        String head = null;
        try {
            //老文件名
            String oldFileName = multipartFile.getOriginalFilename();
            //根据id调用工具类生成新文件名
            String newFileName = FileNameUtil.getFileName(2);
            //截取老文件名的后缀
            String substring = oldFileName.substring(oldFileName.lastIndexOf("."));
            //将后缀放在新文件名的后面
            newFileName = newFileName + substring;
            //生成路径
            String filePath = new DateTime().toString("yyyy/MM/dd");
            //上传
            String upload_file = FtpUtil.upload(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "upload_file", newFileName, multipartFile.getInputStream());
            //判断是否上传成功
            if (upload_file !=null) {
                //上传成功
                head = ftpProperties.getUrl()  + upload_file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  Result.success(head);
    }
}
