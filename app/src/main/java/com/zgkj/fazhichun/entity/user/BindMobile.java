package com.zgkj.fazhichun.entity.user;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/23.
 * Descr:  绑定手机号查询
 */
public class BindMobile {

    private int is_bind;//1:已绑定，0:未绑定
    private String mobile;//	绑定手机号，未绑定时为空

    public int getIs_bind() {
        return is_bind;
    }

    public void setIs_bind(int is_bind) {
        this.is_bind = is_bind;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "BindMobile{" +
                "is_bind='" + is_bind + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
