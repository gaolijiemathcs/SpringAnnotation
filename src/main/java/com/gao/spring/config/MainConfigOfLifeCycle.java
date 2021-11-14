package com.gao.spring.config;

import com.gao.spring.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * bean的生命周期
 * bean创建---初始化---销毁的过程
 * 容器管理bean的生命周期
 * 我们可以自定义初始化和销毁方法 容器在bean进行到当前生命周期的时候 来调用我们自定义的初始化和销毁方法
 *
 * 构造（对象创建）
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 * (初始化前)BeanPostProcessor.postProcessBeforeInitialization
 *  初始化：
 *      对象创建完成并且赋值好 调用初始化方法
 * (初始化后)BeanPostProcessor.postProcessAfterInitialization
 *  销毁：
 *      单实例singleton：当容器关闭的时候销毁
 *      多实例prototype： 容器不会管理这个bean 容器不会调用销毁方法
 *
 *
 * 遍历得到容器中所有的BeanPostProcessor 挨个执行beforeInitialization
 * 一旦返回null 跳出for循环 不会执行后面的BeanPostProcessor.
 * BeanPostProcessor原理
 * populateBean(beanName, mbd, instanceWrapper) 给bean进行属性赋值
 * {
 * applyBeanPostProcessorsBeforeInitialization
 * invokeInitMethods 执行初始化
 * applyBeanPostProcessorsAfterInitialization
 * }
 * 1）指定初始化和销毁方法：通过@Bean(initMethod = "init", destroyMethod = "destory")
 * init 和　destory为在对象里面自定义的方法的名称
 * 2）通过Bean实现  InitializingBeand 定义初始化逻辑，
 *                DisposableBean 定义销毁逻辑
 * 3）可以使用JSR250:
 *      @PostConstruct：在bean创建完成并且属性赋值完成 来执行初始化方法
 *      @PreDestroy：在容器销毁bean之前通知我们进行清理操作
 * 4）@BeanPostProcessor： bean的后置处理器
 *      bean在初始化前后进行一些处理工作
 *      postProcessBeforeInitialization : 在初始化之前进行操作
 *      postProcessAfterInitialization  ： 在初始化之后工作
 *
 *  Spring底层对BeanPostProcessor的使用
 *      bean赋值 注入其他组件 @Autowared 生命周期注解功能 @Async  BeanPostProcessor ....
 */

@ComponentScan("com.gao.spring.bean")
@Configuration
public class MainConfigOfLifeCycle {

    //@Scope("prototype") // 多实例情况
    @Bean(initMethod = "init", destroyMethod = "destory")
    public Car car() {
        return new Car();
    }
}
