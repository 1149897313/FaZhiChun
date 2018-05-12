package com.zgkj.fazhichun.entity.user;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/10.
 * Descr:  个人中心
 */
public class PersonalCenter {

    private String nickname;//用户昵称
    private String image_path;//用户头像
    private String  member_level;//用户会员等级-1普通、2vip
    private String cash_amount;//用户代金券金额

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    @Override
    public String toString() {
        return "PersonalCenter{" +
                "nickname='" + nickname + '\'' +
                ", image_path='" + image_path + '\'' +
                ", member_level='" + member_level + '\'' +
                ", cash_amount='" + cash_amount + '\'' +
                '}';
    }
}
