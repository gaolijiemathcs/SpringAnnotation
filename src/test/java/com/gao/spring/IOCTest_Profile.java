package com.gao.spring;

import com.gao.spring.bean.Boss;
import com.gao.spring.bean.Car;
import com.gao.spring.bean.Color;
import com.gao.spring.config.MainConfigOfAutowired;
import com.gao.spring.config.MainConfigOfProfile;
import com.gao.spring.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

public class IOCTest_Profile {

    //1. 使用命令行动态参数：在虚拟机参数位置加载 -Dspring.profiles.active=test
    //2. 使用代码的方式激活某种环境
    @Test
    public void test01() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //1 创建一个applicationContext对象
        //2 设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("test");
        //3 注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        //4 启动刷新器
        applicationContext.refresh();

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for(String str : beanNamesForType) {
            System.out.println(str);
        }
        Object yellow = applicationContext.getBean("yellow");
        System.out.println(yellow);
        applicationContext.close();
    }
}
