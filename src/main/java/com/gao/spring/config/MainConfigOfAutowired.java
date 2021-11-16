package com.gao.spring.config;

import com.gao.spring.bean.Car;
import com.gao.spring.bean.Color;
import com.gao.spring.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 自动装配;
 *  Spring利用依赖注入(DI) 完成对IOC容器中各个组件的依赖关系赋值
 *
 *  1) @Autowired: 自动注入
 *      1) 默认有限按照类型去容器中找对应的组件 applicationContext.getBean(BookDao.class);  找到就赋值
 *      2) 如果友多个同一个类型的的Dao 如果找到多个相同类型的组件 再将属性的名称作为组件id去容器中查找
 *      3) @Qualifier("bookDao") 来指定需要装配的组件的id 而不是使用默认的属性名
 *      4) 自动装配默认一定要将属性赋值好，没有就会报错
 *      5) @Primary: 让Spring进行自动装配的时候 默认使用首选的bean
 *              也可以继续使用@Qualifier指定需要装配的bean的名字
 *      BookService {
 *          @Autowired
 *          BookDao bookDao
 *      }
 *  2) Spring还支持使用@Resource [JSR250规范] 和 @Inject注解 [JSR330规范] [java规范的注解 JSR的规范 上面的注解是spring的注解 Spring支持java规范]
 *      @Resource:
 *          可以和@Autowired 一样可以实现自动装配功能 默认是按照组件名称进行装配的。
 *          没有能支持@Primary功能，没有支持@Autowired(required=false)
 *      @Inject:
 *          需要导入javax.inject的包 和Autowired的功能一样 支持@Primary
 *          但是Inject没有属性可以设置
 * @Autowired: Spring定义的 @Resource\@Inject 都是java规范
 *
 * AutowiredAnnotationBeanPostProcessor ： 解析完成自动装配功能
 *
 *  3）@Autowired:构造器\参数\方法\属性 都可以标注这个注解
 *      1) [标注在方法位置]： @Bean+方法参数 参数从容器中获取； 默认不写Autowired 效果是一样的
 *                  则调用的时候会 Spring容器创建当前对象 就会调用这个方法完成赋值 这个方法用的参数Car car 自定义的值将会从ioc容器中获取
 *      2）[标注在构造器上]：如果组件只有一个有参构造器 这个有参构造器参数的Autowired可以省略 参数位置组件可以自动从容器中获取
 *      3）标注在参数位置：
 *
 *  4) 自定义组件想要使用Spring容器底层的一些组件(ApplicationContext, BeanFactory, xxx)
 *      自定义组件实现xxxAware；在创建对象的时候 会调用接口规定的方法注入相关组件:Aware;
 *      把Spring底层一些组件注入到自己定义的Bean中
 *      xxxAware:功能使用xxxProcessor
 *          ApplicationContextAware ==> ApplicationContextAwareProcessor;
 */
@Configuration
@ComponentScan({"com.gao.spring.service","com.gao.spring.dao", "com.gao.spring.controller", "com.gao.spring.bean"})
public class MainConfigOfAutowired {

    // @Primary 标注以后 自动装配的时候首选这个bean对象 此时@Qualifier要去掉
    @Primary
    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

    /**
     * @Bean标注的方法 创建对象的时候 方法参数值从容器中获取(默认) 也可以标注@Autowired
     * @param car
     * @return
     */
    @Bean
    public Color color(Car car) {
        Color color = new Color();
        color.setCar(car);
        return color;
    }

}
