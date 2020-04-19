package cn.com.itcast.sport.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.UUID;

/**
 * @Author JW
 * @DateTime 2019/9/2 8:16
 * @Project_Name 2019-9-1-ftp
 */
public class FtpUtil {
    //FTP文件上传
    public static String upload(String hostname, int port,String username,String password,
                                String targetPath,String suffix,InputStream inputStream) throws SocketException, IOException {
        //实例化ftpClient
        FTPClient ftpClient = new FTPClient();
        //设置登陆超时时间,默认是20s
        ftpClient.setDataTimeout(12000);
        //1.连接服务器
        ftpClient.connect(hostname,21);
        //2.登录（指定用户名和密码）
        boolean b = ftpClient.login(username,password);
        if(!b) {
            System.out.println("登陸超時");
            if (ftpClient.isConnected()) {
                // 断开连接
                ftpClient.disconnect();
            }
        }
        // 设置字符编码
        ftpClient.setControlEncoding("UTF-8");
        //基本路径，一定存在
        String basePath="/";
        String[] pathArray = targetPath.split("/");
        for(String path:pathArray){
            basePath+=path+"/";
            //3.指定目录 返回布尔类型 true表示该目录存在
            boolean dirExsists = ftpClient.changeWorkingDirectory(basePath);
            //4.如果指定的目录不存在，则创建目录
            if(!dirExsists){
                //此方式，每次，只能创建一级目录
                boolean flag=ftpClient.makeDirectory(basePath);
                if (flag){
                    System.out.println("创建成功！");
                }
            }
        }
        //重新指定上传文件的路径
        ftpClient.changeWorkingDirectory(targetPath);
        //5.设置上传文件的方式
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //使用uuid，保存文件名唯一性
        String uuid= UUID.randomUUID().toString();
        /**
         * 6.执行上传
         * remote 上传服务后，文件的名称
         * local 文件输入流
         * 上传文件时，如果已经存在同名文件，会被覆盖
         */
        boolean uploadFlag = ftpClient.storeFile(uuid+suffix,inputStream);
        if(uploadFlag)
            System.out.println("上传成功！");
        return uuid+suffix;
    }


    public static Boolean uploadFile(String host, int port, String username, String password, String basePath,
                                     String filePath, String fileName, InputStream inputStream) throws IOException {
        //1、创建临时路径
        String tempPath="";
        //2、创建FTPClient对象（对于连接ftp服务器，以及上传和上传都必须要用到一个对象）
        FTPClient ftp=new FTPClient();
        try{
            //3、定义返回的状态码
            int reply;
            //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(host,port);
            //5、输入账号和密码进行登录
            ftp.login(username,password);
            ftp.enterLocalPassiveMode();
            //6、接受状态码(如果成功，返回230，如果失败返回503)
            reply=ftp.getReplyCode();
            //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if(!FTPReply.isPositiveCompletion(reply)){
                //说明连接失败，需要断开连接
                ftp.disconnect();
                return false;
            }
            //8、changWorkingDirectory(linux上的文件夹)：检测所传入的目录是否存在，如果存在返回true，否则返回false
            //basePath+filePath-->home/ftp/www/2019/09/02
            if(!ftp.changeWorkingDirectory(basePath+filePath)){
                //9、截取filePath:2019/09/02-->String[]:2019,09,02
                String[] dirs=filePath.split("/");
                //10、把basePath(/home/ftp/www)-->tempPath
                tempPath=basePath;
                for (String dir:dirs){
                    //11、循环数组(第一次循环-->2019)
                    if(null==dir||"".equals(dir))
                        continue;//跳出本地循环，进入下一次循环
                    //12、更换临时路径：/home/ftp/www/2019
                    tempPath += "/" + dir;
                    //13、再次检测路径是否存在(/home/ftp/www/2019)-->返回false，说明路径不存在
                    if(!ftp.changeWorkingDirectory(tempPath)){
                        //14、makeDirectory():创建目录  返回Boolean雷类型，成功返回true
                        if(!ftp.makeDirectory(tempPath)){
                            return false;
                        }else {
                            //15、严谨判断（重新检测路径是否真的存在(检测是否创建成功)）
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //16.把文件转换为二进制字符流的形式进行上传
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //17、这才是真正上传方法storeFile(filename,input),返回Boolean雷类型，上传成功返回true
            if (!ftp.storeFile(fileName, inputStream)) {
                return false;
            }
            // 18.关闭输入流
            inputStream.close();
            // 19.退出ftp
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return true;
    }
}