package com.zgkj.fazhichun.fragments.account;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/9.
 * Descr:
 */
public class A {


    /**
     * code : 200
     * data : {"shop_name":"123","shop_type":"3","merchant_name":"123","merchant_telphone":"1233","score_quantity":"0","shop_service_score":"5.00","shop_telphone":"18323463377","shop_status":"2","aproval_memo":"","opening_hour":"12:00-12:00","address":"重庆市沙坪坝区西永街道大龙井","shop_banner":["http://pic.fzc.com/seller/20180322/20180322132957_76903991.jpeg","http://pic.fzc.com/seller/20180322/20180322132957_25347900.jpeg","http://pic.fzc.com/seller/20180322/20180322132957_23702697.jpeg"],"shop_image":"seller/20180322/20180322132957_98522338.jpeg","shop_photo":"seller/20180322/20180322132957_91741027.jpeg","shop_detail":"123","shop_services":"10"}
     * message : OK
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * shop_name : 123
         * shop_type : 3
         * merchant_name : 123
         * merchant_telphone : 1233
         * score_quantity : 0
         * shop_service_score : 5.00
         * shop_telphone : 18323463377
         * shop_status : 2
         * aproval_memo :
         * opening_hour : 12:00-12:00
         * address : 重庆市沙坪坝区西永街道大龙井
         * shop_banner : ["http://pic.fzc.com/seller/20180322/20180322132957_76903991.jpeg","http://pic.fzc.com/seller/20180322/20180322132957_25347900.jpeg","http://pic.fzc.com/seller/20180322/20180322132957_23702697.jpeg"]
         * shop_image : seller/20180322/20180322132957_98522338.jpeg
         * shop_photo : seller/20180322/20180322132957_91741027.jpeg
         * shop_detail : 123
         * shop_services : 10
         */

        private String shop_name;
        private String shop_type;
        private String merchant_name;
        private String merchant_telphone;
        private String score_quantity;
        private String shop_service_score;
        private String shop_telphone;
        private String shop_status;
        private String aproval_memo;
        private String opening_hour;
        private String address;
        private String shop_image;
        private String shop_photo;
        private String shop_detail;
        private String shop_services;
        private List<String> shop_banner;

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }

        public String getMerchant_telphone() {
            return merchant_telphone;
        }

        public void setMerchant_telphone(String merchant_telphone) {
            this.merchant_telphone = merchant_telphone;
        }

        public String getScore_quantity() {
            return score_quantity;
        }

        public void setScore_quantity(String score_quantity) {
            this.score_quantity = score_quantity;
        }

        public String getShop_service_score() {
            return shop_service_score;
        }

        public void setShop_service_score(String shop_service_score) {
            this.shop_service_score = shop_service_score;
        }

        public String getShop_telphone() {
            return shop_telphone;
        }

        public void setShop_telphone(String shop_telphone) {
            this.shop_telphone = shop_telphone;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
        }

        public String getAproval_memo() {
            return aproval_memo;
        }

        public void setAproval_memo(String aproval_memo) {
            this.aproval_memo = aproval_memo;
        }

        public String getOpening_hour() {
            return opening_hour;
        }

        public void setOpening_hour(String opening_hour) {
            this.opening_hour = opening_hour;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getShop_image() {
            return shop_image;
        }

        public void setShop_image(String shop_image) {
            this.shop_image = shop_image;
        }

        public String getShop_photo() {
            return shop_photo;
        }

        public void setShop_photo(String shop_photo) {
            this.shop_photo = shop_photo;
        }

        public String getShop_detail() {
            return shop_detail;
        }

        public void setShop_detail(String shop_detail) {
            this.shop_detail = shop_detail;
        }

        public String getShop_services() {
            return shop_services;
        }

        public void setShop_services(String shop_services) {
            this.shop_services = shop_services;
        }

        public List<String> getShop_banner() {
            return shop_banner;
        }

        public void setShop_banner(List<String> shop_banner) {
            this.shop_banner = shop_banner;
        }
    }
}
