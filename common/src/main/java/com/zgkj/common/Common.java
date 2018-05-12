package com.zgkj.common;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/26.
 * Descr:   公有类的定义
 */

public class Common {

    /**
     * 定义常量
     */
    public interface Constant {

        // API接口的根网址的定义
        String API_URL = "http://api2.fazhichun.com";
        //        String API_URL = "http://api.fzc.com";


        /**  判断字符串是否符合手机号码格式
         * * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * * 电信号段: 133,149,153,170,173,177,180,181,189 */
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        //String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 微信APP_ID和AppSecret
        String WEIXIN_APP_ID = "wxba9d71907053717e";
        String WEIXIN_APP_SECRET = "e3a068d57cce2a311479d4c1ef987cfd";

        // bugly
        String BUGLY_APP_ID = "15ceb75897";

        // 城市名，经度和纬度
        String CITY_NAME = "city_name";
        String LONGITUDE_ID = "longitude_id";
        String LATITUDE_ID = "latitude_id";

        // 支付订单的id
        String PAY_ORDER_ID = "pay_order_id";

    }

    /**
     * 状态码
     */
    public interface Code {
        /**
         * 成功
         */
        int SUCCESS = 200;
        /**
         * 失败
         */
        int ERORR = 201;
    }


}
