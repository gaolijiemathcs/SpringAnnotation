# Spring注解驱动开发这个

## @Configuration 设置配置类

注解方式实现配置类

```java
@Configuration
public class MainConfig2 {
}
```

```java
AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
```

还可以用bean.xml



## @Scope 修改单实例和多实例

用于调整作用域

```
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
     */
```

```java
@Configuration
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
     */
    @Scope("prototype")
    @Bean("person")
    public Person person() {
        System.out.println("给容器中添加Person....");
        return new Person("张三", 25);
    }
}
```

```java
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
```





## @Lazy懒加载

默认是单例模式 所以测试类当中进行创建对象都是同一个对象

```java
@Configuration
public class MainConfig2 {

     /*  @Lazy
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
}
```

```
    @Test
    public void test02() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("ioc容器创建完成...");
//        Object bean = applicationContext.getBean("person");
//        Object bean2 = applicationContext.getBean("person");
//        System.out.println(bean == bean2);
    }
```



此时输出：

```
ioc容器创建完成...
```

没有给容器添加person..的输出



只有当第一次使用Bean创建对象并且初始化才会调用

```java
    @Test
    public void test02() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("ioc容器创建完成...");
        Object bean = applicationContext.getBean("person");
        Object bean2 = applicationContext.getBean("person");
    }
```





## @Conditional 对象bean按条件创建对象

ioc容器 可以放在类上面 也可以放在属性上面

1. 放在类上面 ：满足当前条件 这个类中配置的所有bean才能生效
2. 放在属性上面：按照一定的条件判断 满足条件则给容器中注册bean

```
package com.gao.spring.config;

import com.gao.spring.bean.Person;
import com.gao.spring.condition.LinuxCondition;
import com.gao.spring.condition.WindowsCondition;
import org.springframework.context.annotation.*;

// 满足当前条件 这个类中配置的所有bean才能生效
@Conditional({LinuxCondition.class})
@Configuration
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
}

```



LinuxConfition.java

```java
package com.gao.spring.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

// 判断是否是linux系统
public class LinuxCondition implements Condition {
    /**
     * ConditionContext 判断条件能够使用的上下文 环境
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // TODO是否是linux系统
        // 1. 能够获取到ioc使用的beanfactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 2. 获取类加载其
        ClassLoader classLoader = context.getClassLoader();
        // 3. 获取当前环境信息
        Environment environment = context.getEnvironment();
        // 4. 获取到bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        String property = environment.getProperty("os.name");
        // 可以判断容器中的bean注册情况 也可以给容器中注册bean
        boolean definition = registry.containsBeanDefinition("person");
        if(property.contains("linux")) {
            return true;
        }
        return false;
    }
}

```



windowsCondition.java

```java
package com.gao.spring.condition;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("os.name");
        if(property.contains("Linux")) {
            return true;
        }
        return false;
    }
}

```

测试类

```java
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

```





## 给容器注册组件方式

### 1、包扫描+组件标注注解(自己写的类一般都用这种方式)

自己写的类

@Controller @Service @Respoository @Component

### 2、@Bean 导入的第三方包里面的组件 我们通过这个组件注册

导入第三方包里面的组件 注册bean对象到ioc容器当中

### 3、@Import快速给容器中导入一个组件

新建一个类

```java
package com.gao.spring.bean;

public class Color {
}
```

#### （1）@Import(要导入到容器中的组件)：容器就会自动注册这个组件 id默认是全类名

直接在配置类上面Import之后就会将Color这个组件 bean对象注册到ioc容器当中

```java
@Configuration
@Import(Color.class)
public class MainConfig2 {
}
```



```java
public class IOCTest {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

    @Test
    public void testImport() {
        printBeans(applicationContext);
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name :beanDefinitionNames) {
            System.out.println(name);
        }
    }
}
```

输出

```java
mainConfig2
com.gao.spring.bean.Color
person
linux
```

color对象出现。

// 导入组件id 默认是全类名

还可以以数组方式导入多个：

```java
@Import({Color.class, Red.class})
```

输出：

```java
com.gao.spring.bean.Color
com.gao.spring.bean.Red
```

@Import(要导入到容器中的组件)：容器就会自动注册这个组件 id默认是全类名



#### （2）实现接口ImportSelector返回需要导入的组件的全类名的数组

在配置类上面加入Import的对象

```java
@Configuration
@Import({Color.class, Red.class, MyImportSeletor.class})
// 导入组件id 默认是全类名
public class MainConfig2 {
}
```

创建配置类的Seletor 需要实现selector接口

```java
package com.gao.spring.condition;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSeletor implements ImportSelector {
    // 返回值就是要导入到容器中的组件全类名
    // AnnotationMetadata 当前标注@Import注解的类的所有注解信息
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"com.gao.spring.bean.Blue", "com.gao.spring.bean.Red", "com.gao.spring.bean.Yellow"};
    }
}

```

这样容器中就会自动的获取这些组件

```java
public class IOCTest {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

    @Test
    public void testImport() {
        printBeans(applicationContext);
        Blue bean = applicationContext.getBean(Blue.class);
        System.out.println(bean);
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name :beanDefinitionNames) {
            System.out.println(name);
        }
    }
}

```

```java
mainConfig2
com.gao.spring.bean.Color
com.gao.spring.bean.Red
com.gao.spring.bean.Blue
com.gao.spring.bean.Yellow
person
linux
com.gao.spring.bean.Blue@7920ba90
```

这些bean name默认都是全类名称

#### （3）实现ImportBeanDefinitionRegistrar接口 手动注册bean到容器中

创建方法registerBeanDefitions

```java
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
            // 指定Bean定义信息：Bean的类型(类型 scopre...都可以定义)
            RootBeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class);
            // 注册一个Bean指定bean的名称
            registry.registerBeanDefinition("rainBow", beanDefinition);
        }
    }
}
```

注意这里 我们rainBow 的bean名称已经定义为了rainBow

```java
mainConfig2
com.gao.spring.bean.Color
com.gao.spring.bean.Red
com.gao.spring.bean.Blue
com.gao.spring.bean.Yellow
person
linux
rainBow
com.gao.spring.bean.Blue@5906ebcb
```

所以输出也是rainBow import直接进来的类 是使用全类目





### 4、使用Spring提供的FactoryBean注册组件(称为工厂Bean)

#### 1）默认获取到的是工厂Bean调用getObject创建的对象

创建一个对象重写FatoryBean

```java
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
        return true;  // 此处我们开启单例模式
    }
}

```

在配置文件中创建这个bean对象

```java
public class MainConfig2 {
    @Bean
    public ColorFactoryBean colorFactoryBean() {
        return new ColorFactoryBean();
    }
}
```

测试方法：

```java
    @Test
    public void testImport() {
        printBeans(applicationContext);
        Blue bean = applicationContext.getBean(Blue.class);
        System.out.println(bean);

        // 工厂Bean获取的是调用getObject创建的对象
        Object bean2 = applicationContext.getBean("colorFactoryBean");
        System.out.println("bean2的类型：" + bean2.getClass());
    }
```

输出：

```java
ColorFatoryBean...getObject...
bean2的类型：class com.gao.spring.bean.Color
```

 

上面创建的是单例模式 因此我们测试一下

```java
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

    }
```

输出：

```java
ColorFatoryBean...getObject...
bean2的类型：class com.gao.spring.bean.Color
true
```

单例只获取一次对象。



如果我们改成多实例，在FactoryBean中修改为false

```java
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
        return false;	// 多实例
    }
}
```



运行结果：

```java
com.gao.spring.bean.Blue@6646153
ColorFatoryBean...getObject...
ColorFatoryBean...getObject...
bean2的类型：class com.gao.spring.bean.Color
false
```

获取了两次对象。



并且都是只获取到Color对象。



#### 2）获取工厂FactoryBean本身 则在getBean的时候需要在id前面加上一个&

applicationContext.getBean("&colorFactoryBean")

如果我们非要获取到FactoryBean的具体定义对象ColorFactoryBean对象则我们需要在getBean上加上&

```java
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
}
```

```java
com.gao.spring.bean.Blue@6646153
ColorFatoryBean...getObject...
ColorFatoryBean...getObject...
bean2的类型：class com.gao.spring.bean.Color
false
class com.gao.spring.bean.ColorFactoryBean
```

因为BeanFactory中有定义前缀

```
public interface BeanFactory {

	String FACTORY_BEAN_PREFIX = "&";
```

则加入这个前缀的时候 我们获取对象就是获取这个工厂的对象本身。





## Bean生命周期

### 1、指定初始化和销毁方法：通过@Bean中的标签指定init-method和destroy-method方法

```
/**
 * bean的生命周期
 * bean创建---初始化---销毁的过程
 * 容器管理bean的生命周期
 * 我们可以自定义初始化和销毁方法 容器在bean进行到当前生命周期的时候 来调用我们自定义的初始化和销毁方法
 *
 * 1）指定初始化和销毁方法； 
 * 通过@Bean(initMethod = "init", destroyMethod = "destory")
 * 构造（对象创建）
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 *
 *  初始化：
 *      对象创建完成并且赋值好 调用初始化方法
 *
 *  销毁：
 *      单实例singleton：当容器关闭的时候销毁
 *      多实例prototype： 容器不会管理这个bean 容器不会调用销毁方法
 *
 * 
 *
 */
```



bean的生命周期：bean创建---初始化---销毁的过程

容器管理bean的生命周期

我们可以自定义初始化和销毁方法 容器在bean进行到当前生命周期的时候 来调用我们自定义的初始化和销毁方法

1）指定初始化和销毁方法；

以前使用bean.xml的方式 是通过在\<bean>标签上进行指定init和destory方法

```xml
    <bean id="person" class="com.gao.spring.bean.Person" scope="prototype" init-method="" destroy-method="">
        <property name="age" value="18"/>
        <property name="name" value="zhangsan"/>
    </bean>
```

注解的方式实现方法如下：

创建一个新的Car类

```java
package com.gao.spring.bean;

public class Car {
    public Car() {
        System.out.println("car constructor...");
    }

    public void init() {
        System.out.println("car ... init ...");
    }

    public void destory() {
        System.out.println("car ... destory ...");
    }
}
```

创建一个Config类

```java
package com.gao.spring.config;

import com.gao.spring.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean的生命周期
 * bean创建---初始化---销毁的过程
 * 容器管理bean的生命周期
 * 我们可以自定义初始化和销毁方法 容器在bean进行到当前生命周期的时候 来调用我们自定义的初始化和销毁方法
 *
 * 构造（对象创建）
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 *
 * 1）指定初始化和销毁方法；
 *
 */
@Configuration
public class MainConfigOfLifeCycle {

    @Bean(initMethod = "init", destroyMethod = "destory")
    public Car car() {
        return new Car();
    }
}

```



在此处注册Bean对象的时候 我们要声明 init方法域destory方法是什么

测试类

```java
public class IOCTest_LifeCycle {
    @Test
    public void test01() {
        // 1. 创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");
    }
}
```

输出：

```java
car constructor...
car ... init ...
容器创建完成...
```

在创建ioc容器的时候 会调用构造方法 然后调用init方法 最后创建完成

**问题：为什么没有调用销毁destory方法？**

则我们这里需要家一句关闭容器

```java
public class IOCTest_LifeCycle {
    @Test
    public void test01() {
        // 1. 创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");
        // 关闭容器
        applicationContext.close();
    }
}
```

则会触发容器关闭

```
car constructor...
car ... init ...
容器创建完成...
car ... destory ...
```

可以在初始化和销毁的时候可以做一些操作。

经常用于处理数据源的连接和关闭来修改init与destory方法。

```
 * 构造（对象创建）
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 *
 *  初始化：
 *      对象创建完成并且赋值好 调用初始化方法
 *
 *  销毁：
 *      当容器关闭的时候销毁
```

以上为单实例默认情况下的情况。



**问题：如果设置类scope 为多实例prototype**

我们看什么时候:

```java
@Configuration
public class MainConfigOfLifeCycle {

    @Scope("prototype")
    @Bean(initMethod = "init", destroyMethod = "destory")
    public Car car() {
        return new Car();
    }
}
```

同样执行测试类

```java
public class IOCTest_LifeCycle {
    @Test
    public void test01() {
        // 1. 创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");
        // 关闭容器
        applicationContext.close();
    }
}
```

输出：

```
容器创建完成...
```

这时候没有初始化bean

因为多实例的时候，只有在获取到bean的时候才会调用具体的Init

修改为

```java
public class IOCTest_LifeCycle {
    @Test
    public void test01() {
        // 1. 创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");
        
        applicationContext.getBean("car");
        
        // 关闭容器
        applicationContext.close();
    }
}
```

输出：

```
容器创建完成...
car constructor...
car ... init ...
```

发现多实例的情况下，只会初始化，不会执行销毁。

ioc容器单实例bean是在容器关闭的时候销毁 多实例的情况：容器不会管理这个bean



### 2、通过Bean实现InitializingBeand 定义初始化逻辑， DisposableBean 定义销毁逻辑

```
2）通过Bean实现InitializingBeand 定义初始化逻辑， DisposableBean 定义销毁逻辑
```

创建Cat类 并且实现接口InitializingBean和DisposableBean

```java
package com.gao.spring.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class Cat implements InitializingBean, DisposableBean {
    public Cat() {
        System.out.println("cat constructor...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("cat ... destroy ... ");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat ... afterPropertiesSet ... ");
    }
}
```

我们这里直接使用Componet来注册Bean



再使用扫描的方法对配置文件去扫描指定包的方式去获取bean

就不用写具体的bean注册的过程了

```java
@ComponentScan("com.gao.spring.bean")
@Configuration
public class MainConfigOfLifeCycle {

    //@Scope("prototype") // 多实例情况
    @Bean(initMethod = "init", destroyMethod = "destory")
    public Car car() {
        return new Car();
    }
}
```



### 3、JSR250(Java规范注解)规范定义的两个注解来初始化bean

#### （1）使用@PostConstruct：在bean创建完成并且属性赋值完成，来执行初始化。

#### （2）@PreDestroy 在容器销毁bean之前通知我们进行清理工作



创建一个DOg类演示

```java
package com.gao.spring.bean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Dog {
    public Dog() {
        System.out.println("dog constructor....");
    }

    // 对象创建并且赋值之后调用
    @PostConstruct
    public void init() {
        System.out.println("Dog ... @PostConstruct ...");
    }

    // 容器移除对象之前
    @PreDestroy
    public void destory() {
        System.out.println("Dog ... @PreDestroy");
    }
}

```

输出：

```java
cat constructor...
cat ... afterPropertiesSet ... 
dog constructor....
Dog ... @PostConstruct ...
car constructor...
car ... init ...
容器创建完成...
Nov 15, 2021 1:08:00 AM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Mon Nov 15 01:07:59 CST 2021]; root of context hierarchy
car ... destory ...
Dog ... @PreDestroy
cat ... destroy ... 
```

由此可见 postConstruct是在dog构造器之后调用 PreDestroy是在dog容器销毁之前调用





### 4、BeanPostProcessor：bean的后置处理器

在bean的后置处理器 两种方法postProcessBeforeInitialization、postProcessAfterInitialization

```java
package com.gao.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 后置处理器：初始化前后进行处理工作
 * 将后置处理器加入到容器
 */
@Component
public class MyBeanPostProceessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization ...." + beanName + "=>" + bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization ...." + beanName + "=>" + bean);
        return bean;
    }
}

```

```java
public class IOCTest_LifeCycle {
    @Test
    public void test01() {
        // 1. 创建ioc容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");

//        applicationContext.getBean("car");

        // 关闭容器
        applicationContext.close();
    }
}
```

输出：

```
cat constructor...
postProcessBeforeInitialization ....cat=>com.gao.spring.bean.Cat@4d826d77
cat ... afterPropertiesSet ... 
postProcessAfterInitialization ....cat=>com.gao.spring.bean.Cat@4d826d77
dog constructor....
postProcessBeforeInitialization ....dog=>com.gao.spring.bean.Dog@44a664f2
Dog ... @PostConstruct ...
postProcessAfterInitialization ....dog=>com.gao.spring.bean.Dog@44a664f2
car constructor...
postProcessBeforeInitialization ....car=>com.gao.spring.bean.Car@482cd91f
car ... init ...
postProcessAfterInitialization ....car=>com.gao.spring.bean.Car@482cd91f
容器创建完成...
Nov 15, 2021 1:16:53 AM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Mon Nov 15 01:16:52 CST 2021]; root of context hierarchy
car ... destory ...
Dog ... @PreDestroy
cat ... destroy ... 
```

即使没有调用具体的bean我们也可以完成对容器的各种初始化的过程



原理：

```
* 遍历得到容器中所有的BeanPostProcessor 挨个执行beforeInitialization
* 一旦返回null 跳出for循环 不会执行后面的BeanPostProcessor.
* BeanPostProcessor原理
* populateBean(beanName, mbd, instanceWrapper) 给bean进行属性赋值
* {
* applyBeanPostProcessorsBeforeInitialization
* invokeInitMethods 执行初始化
* applyBeanPostProcessorsAfterInitialization
* }
```

```
Spring底层对BeanPostProcessor的使用
bean赋值 注入其他组件 @Autowared 生命周期注解功能 @Async  BeanPostProcessor ....
```

