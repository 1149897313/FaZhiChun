package com.zgkj.factory.model.api;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/4.
 * Descr:
 */
public class TestRspModel {


    /**
     * users : [{"age":1,"job":2},{"age":22,"job":2}]
     * aa : 111
     */

    private int aa;
    private List<UsersBean> users;

    public int getAa() {
        return aa;
    }

    public void setAa(int aa) {
        this.aa = aa;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean {
        /**
         * age : 1
         * job : 2
         */

        private int age;
        private int job;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getJob() {
            return job;
        }

        public void setJob(int job) {
            this.job = job;
        }

        @Override
        public String toString() {
            return "UsersBean{" +
                    "age=" + age +
                    ", job=" + job +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "TestRspModel{" +
                "aa=" + aa +
                ", users=" + users +
                '}';
    }
}
