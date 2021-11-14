package com.gao.spring.config;

import com.gao.spring.bean.Person;
import com.gao.spring.service.BookService;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Configuration  // 告诉是一个配置类
//@ComponentScan(value="com.gao.spring")
//ComponentScan value 指定要扫描的包 excludeFilters是排除
//@ComponentScan(value="com.gao.spring",excludeFilters = {
//        @ComponentScan.Filter(type= FilterType.ANNOTATION, classes = {Controller.class} )
//})
//includeFilters = Filter[] 扫描指定的组件
//@ComponentScan(value = "com.gao.spring", includeFilters = {
//        @ComponentScan.Filter(type=FilterType.ANNOTATION,classes = {Controller.class,Service.class})
//}, useDefaultFilters = false)
@ComponentScans(
        value = {
                @ComponentScan(value="com.gao.spring", includeFilters = {
//                        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Service.class}),
//                        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BookService.class}),
                        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class})
                }, useDefaultFilters = false)
        }
)
// @FilterType.ANNOTATION 按照注解
// @FilterType.ASSIGNABLE_TYPE 照给定的类型
// @FilterType.ASPECTJ 使用ASPECTJ表达式
// @FilterType.REGEX 按照正则表达式
// @FilterType.CUSTOM
public class MainConfig {

    @Bean("person")
    public Person person01() {
        return new Person("lisi", 20);
    }
}
