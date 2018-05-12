package com.zgkj.fazhichun.entity.shop;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/7.
 * Descr:  店铺详细信息
 */
public class StoreInfo {

    private String shop_name;//店铺名称
    private int shop_type;//店铺类型（1个人，2个体，3企业）
    private String merchant_name;//店主姓名
    private int merchant_telphone;//店主手机号
    private int core_quantity;//评分人数
    private float shop_service_score;//商家星级评分(共5星 5.00)
    private int shop_telphone;//店铺电话
    private int shop_status;//商家状态(0未启用，1启用，2停用)
    private String aproval_memo;//审核状况
    private String opening_hour;//营业时间
    private float shop_lng;//经度
    private float shop_lat;//纬度
    private String address;//店铺详细地址
    private String shop_banner;//店铺banner图
    private String shop_image;//商家形象图
    private String shop_photo;//店铺头像
    private String shop_detail;//店铺介绍
    private String shop_service;// array 商家服务----
    private int service_id;//商家服务-服务id
    private String service_name;//商家服务-服务说明
    private String service_url;//商家服务-服务图片
    private float persin_pay_money;//人均消费
    private int talk_num;//	评价数量
    private String talk_list;// array评价列表(2条)------
    private int is_love;//是否收藏(1是，0否)


}
