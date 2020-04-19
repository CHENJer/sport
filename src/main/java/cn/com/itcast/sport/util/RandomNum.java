package cn.com.itcast.sport.util;

/**
 * @description: 生成十八位随机位编号
 * @author: JIAWEI
 * @date: Created in 2020/3/14 20:17
 * @version: 1.0
 * @modified By:
 */
public class RandomNum {
    public static String USER_CODE = "YHB";
    public static String ROLE_CODE = "JSB";
    public static String SOURCE_CODE = "KCB";
    public static String PACKAGE_CODE = "XJB";
    public static String Type_CODE = "LXB";
    public static String CHART_CODE = "GWB";
    public static String ORDER_CODE = "DDB";
    public static String COMMENT_CODE = "PLB";
    public static String getRandomNum(String startcode){
        String code = "FY"+startcode+System.currentTimeMillis();
        return code;

    }
    /*调用方法*/
    public static void main(String[] args) {
        String randomNum = getRandomNum(RandomNum.SOURCE_CODE);
        System.out.println(randomNum+": "+randomNum.length());
    }
}
