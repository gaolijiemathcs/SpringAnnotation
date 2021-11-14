package com.gao.spring.bean;

import org.springframework.beans.factory.FactoryBean;

// 创建一个Spring定义的FactoryBean
public class ColorFactoryBean implements FactoryBean<Color> {
    // 返回一个Color对象 这个对象胡添加到容器当中
    @Override
    public Color getObject() throws Exception {
        System.out.println("ColorFatoryBean...getObject...");
        return new Color();
    }

    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }

    // 控制是不是单利
    // true 则bean是单实例 容器中只保存一份
    // false 则bean 是多实例，每次获取都会创建一个新的对象  创建bean就是通过getObject
    @Override
    public boolean isSingleton() {
        return false;
    }
}
