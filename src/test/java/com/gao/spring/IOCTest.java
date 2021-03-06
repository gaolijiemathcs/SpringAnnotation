package com.gao.spring;

import com.gao.spring.bean.Blue;
import com.gao.spring.bean.Person;
import com.gao.spring.config.MainConfig;
import com.gao.spring.config.MainConfig2;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

public class IOCTest {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

    @Test
    public void testImport() {
        printBeans(applicationContext);
        Blue bean = applicationContext.getBean(Blue.class);
        System.out.println(bean);

        // 工厂Bean获取的是调用getObject创建的对象
        Object bean2 = applicationContext.getBean("colorFactoryBean");
        Object bean3 = applicationContext.getBean("colorFactoryBean");
        System.out.println("bean2的类型：" + bean2.getClass());
        System.out.println(bean2==bean3);

        Object bean4 = applicationContext.getBean("&colorFactoryBean");
        System.out.println(bean4.getClass());
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name :beanDefinitionNames) {
            System.out.println(name);
        }
    }

    @Test
    public void test03() {
        String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // 获取环境变量
        String property = environment.getProperty("os.name");
        System.out.println(property);

        for(String name : namesForType) {
            System.out.println(name);
        }
        Map<String, Person> persons = applicationContext.getBeansOfType(Person.class);
        System.out.println(persons);
    }

    @SuppressWarnings("resource")
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }
    }

    @Test
    public void test02() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
//        String[] definitionNames = applicationContext.getBeanDefinitionNames();
//        for(String name : definitionNames) {
//            System.out.println(name);
//        }
        System.out.println("ioc容器创建完成...");
        Object bean = applicationContext.getBean("person");
        Object bean2 = applicationContext.getBean("person");
//        System.out.println(bean == bean2);
    }


}
