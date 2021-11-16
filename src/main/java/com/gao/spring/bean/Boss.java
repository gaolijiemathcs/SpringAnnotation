package com.gao.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//默认加载ioc容器中的组件 容器启动会调用无参构造器创建对象 再进行初始化赋值等操作
@Component
public class Boss {

//    @Autowired
    private Car car;

    //构造器要用的组件 都是从容器中获取的
//    @Autowired  标注在方法上
//    public Boss(@Autowired Car car) 标注在参数上
    public Boss(Car car) {
        this.car = car;
        System.out.println("Boss...有参构造器");
    }

    public Car getCar() {
        return car;
    }

//    @Autowired
    //标注方法 Spring容器创建当前对象 就会调用这个方法完成赋值
    // 这个方法用的参数Car car，自定义的值将会从ioc容器中获取
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }
}
