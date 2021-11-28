package com.gao.spring.aop;

/**
 * 业务逻辑类
 */
public class MathCalculator {
    public int div(int i, int j) {
        System.out.println("业务逻辑类 MathCalculator...div...");
        return i / j;
    }
}
