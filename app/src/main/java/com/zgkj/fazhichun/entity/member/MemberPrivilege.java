package com.zgkj.fazhichun.entity.member;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/18.
 * Descr: 会员特权
 */
public class MemberPrivilege {

    private int imageUrl;
    private boolean state;
    private String name;
    private String content;

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MemberPrivilege{" +
                "imageUrl=" + imageUrl +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
