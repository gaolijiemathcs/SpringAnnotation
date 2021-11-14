package com.gao.spring.config;

import com.gao.spring.bean.Color;
import com.gao.spring.bean.ColorFactoryBean;
import com.gao.spring.bean.Person;
import com.gao.spring.bean.Red;
import com.gao.spring.condition.LinuxCondition;
import com.gao.spring.condition.MyImportBeanDefinitionRegistrar;
import com.gao.spring.condition.MyImportSeletor;
import com.gao.spring.condition.WindowsCondition;
import org.springframework.context.annotation.*;

// 满足当前条件 这个类中配置的所有bean才能生效
//@Conditional({WindowsCondition.class})
@Configuration
@Import({Color.class, Red.class, MyImportSeletor.class, MyImportBeanDefinitionRegistrar.class})
// 导入组件id 默认是全类名
public class MainConfig2 {

    // 默认是单实例的

    /**  @Scope调整作用域
     *   * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE prototype
     * 	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON singleton
     * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST request
     * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION session
     *
     * 	 prototype : 多实例 可以创建多个统一类型的实例对象 当ioc容器启动不会掉用方法创建对象在容器中
     * 	                                            而是每次创建对象的时候就调用一次方法
     * 	 singleton ： 单实例 默认值 同一个类型只能篡改就一个对象 ioc容器启动就会调用方法创建对象到ioc容器中。 以后每次获取就从容器中拿 map.get()
     * 	 request ： 同一次请求创建一个实例
     * 	 session ： 同一个session创建一个实例
     *
     *  @Lazy
     * 	 懒加载：
     * 	    单实例bean：默认在容器启动时会就创建对象
     * 	    懒加载：容器启动不创建对象 第一次使用(获取)Bean创建对象并且初始化
     *
     */
//    @Scope("prototype")
    @Lazy
    @Bean("person")
    public Person person() {
        System.out.println("给容器中添加Person....");
        return new Person("张三", 25);
    }

    /**
     * @Conditional{Condition} : 按照一定的条件判断 满足条件则给容器中注册bean
     * 如果系统是linux就注册linux
     * 如果是windows系统注册bill
     */
    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01() {
        return new Person("Bill Gates", 62);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linux")
    public Person person02() {
        return new Person("Linux", 48);
    }
    @Bean
    public ColorFactoryBean colorFactoryBean() {
        return new ColorFactoryBean();
    }
}
