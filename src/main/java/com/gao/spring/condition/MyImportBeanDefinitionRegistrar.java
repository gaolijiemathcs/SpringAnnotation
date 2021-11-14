package com.gao.spring.condition;

import com.gao.spring.bean.RainBow;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry BeanDefinition注册类
     *                 把所有需要添加到容器中的bean 调用 BeanDefinitionRegistry.registerBeanDefinition 进行手工注册
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean definition = registry.containsBeanDefinition("com.gao.spring.bean.Red");
        boolean definition1 = registry.containsBeanDefinition("com.gao.spring.bean.Blue");
        if(definition && definition1) {
            // 指定Bean定义信息：Bean的类型
            RootBeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class);
            // 注册一个Bean指定bean的名称
            registry.registerBeanDefinition("rainBow", beanDefinition);
        }
    }
}
