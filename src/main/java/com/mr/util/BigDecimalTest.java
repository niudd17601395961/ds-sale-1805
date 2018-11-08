package com.mr.util;


import java.math.BigDecimal;

/**
 * Created by niudd on 2018/11/8.
 */
public class BigDecimalTest {

    public static void main(String[] args) {
        BigDecimal a1 = new BigDecimal("4");
        BigDecimal a2 = new BigDecimal("8");
        //加
        System.out.println(a1.add(a2));
        //减
        System.out.println(a1.subtract(a2));
        //乘
        System.out.println(a1.multiply(a2));
        //除
        System.out.println(a2.divide(a1));
    }

}
