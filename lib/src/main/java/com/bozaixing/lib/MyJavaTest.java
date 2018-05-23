package com.bozaixing.lib;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyJavaTest {

    public static void main(String[] args) {


//        String value="\\],\"[a-zA-Z]\":\\[";
//        String start="{\"A\":";
//        String data="region_name],\"B\":[";
//        String data="\"region_name\":\"阿勒泰地区 \"],\"B\":[{\"region_code\":\"11,01\"";
//        data= data.replaceAll(value,",");
        BigDecimal bigDecimal = new BigDecimal("1225.00");
        BigDecimal money = bigDecimal.multiply(new BigDecimal(String.valueOf(0.5)));
        //原价
        DecimalFormat df1 = new DecimalFormat("0.00");
        String str = df1.format(money);
        System.out.print(str);
        System.out.println("----:" + money.toString());

//        List<A> list = new ArrayList<>();
//        List<A> listList=new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            A a = new A();
//            a.setId(i + "");
//            a.setName("测试" + i);
//            list.add(a);
//        }
//        System.out.println("---1:"+list.toString());
//        listList.addAll(0,list);
//        System.out.println("---2:"+listList.subList(0,1));
//
//        System.out.println("---2:"+listList.subList(2,3));
    }
}
