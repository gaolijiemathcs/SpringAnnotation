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



## @Value赋值

对对象进行赋值

原始的方式是在xml配置文件当中进行赋值

```xml
    <bean id="person" class="com.gao.spring.bean.Person" scope="prototype" >
        <property name="age" value="18"/>
        <property name="name" value="zhangsan"/>
    </bean>
```

现在我们通过配置类的方式：

```java
package com.gao.spring.config;

import com.gao.spring.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfigOfPropertyValues {
    @Bean
    public Person person() {
        return new Person();
    }
}

```

测试类：

```java
public class IOCTest_PropertyValue {
    // 1. 创建ioc容器
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValues.class);

    @Test
    public void test01() {
        printBeans(applicationContext);
        System.out.println("=====================");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
        // 关闭容器
        applicationContext.close();
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name :beanDefinitionNames) {
            System.out.println(name);
        }
    }
}
```

得到初始的情况下 是没有值的

```java
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfigOfPropertyValues
person
=====================
Person{name='null', age=null}
```



更改类中使用@Value

    // 使用value赋值
    // 1、基本数值
    // 2、可以写SppEL #{} spring表达式可以运算
    // 3、可以用${} 取出配置文件[properties]中的值(在运行环境中的值)

```java
public class Person {

    // 使用value赋值
    // 1、基本数值
    // 2、可以写SppEL #{} spring表达式可以运算
    // 3、可以用${} 取出配置文件中的值(在运行环境中的值)
    
    @Value("张三")
    private String name;
    @Value("#{20-2}")
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
	.....
}
```

输出：

```java
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfigOfPropertyValues
person
=====================
Person{name='张三', age=18}
```



在配置文件中的设置方式：

在属性中新增一个属性

```java
public class Person {

    // 使用value赋值
    // 1、基本数值
    // 2、可以写SppEL #{} spring表达式可以运算
    // 3、可以用${} 取出配置文件中的值(在运行环境中的值)

    @Value("张三")
    private String name;
    @Value("#{20-2}")
    private Integer age;

    private String nickName;
    
}
```

创建一个person.properties

```
person.nickName=tom
```

在原始的时候我们想通过xml配置文件去获取${}配置文件的信息 需要加上新的\<context:properties>

```xml
<context:property-placeholder location="classpath:person.properties"/>

<bean id="person" class="com.gao.spring.bean.Person" scope="prototype" >
        <property name="age" value="18"/>
        <property name="name" value="zhangsan"/>
        <property name="nickName" value="${person.nickName}"/>
    </bean>
```



在注解的方式下，我们在配置类当中使用@PropertySource 读取外部配置文件中的k/v保存到运行的环境变量中。

```
//使用@PropertySource 读取外部配置文件中的k/v保存到运行的环境变量中
@PropertySource(value = {"classpath:/person.properties"})
@Configuration
public class MainConfigOfPropertyValues {
    @Bean
    public Person person() {
        return new Person();
    }
}
```



就可以在Person属性中直接使用${}来引入遍历

```java
public class Person {

    // 使用value赋值
    // 1、基本数值
    // 2、可以写SppEL #{} spring表达式可以运算
    // 3、可以用${} 取出配置文件中的值(在运行环境中的值)

    @Value("张三")
    private String name;
    @Value("#{20-2}")
    private Integer age;

    @Value("${person.nickName}")
    private String nickName;
 	
    //get set方法
    ....
}
```



我们也可以直接使用

```
ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("person.nickName=");
        System.out.println(property);
```

在测试类中获取环境变量来印证



```java
public class IOCTest_PropertyValue {
    // 1. 创建ioc容器
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValues.class);

    @Test
    public void test01() {
        printBeans(applicationContext);
        System.out.println("=====================");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("person.nickName");
        System.out.println(property);
        // 关闭容器
        applicationContext.close();
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name :beanDefinitionNames) {
            System.out.println(name);
        }
    }
}

```

输出：

```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfigOfPropertyValues
person
=====================
Person{name='张三', age=18, nickName='tom'}
tom
```





## 自动装配

### (1) @Autowired @Qualifier @Primary

#### @Autowired

```
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
 *
 */
```



修改service层：

```java
package com.gao.spring.service;

import com.gao.spring.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
    @Qualifier("bookDao2")
    @Autowired
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
```

#### @Qualifier

注意此处@Qualifier 为指定默认的组件是调用id=bookDao2的组件

修改Dao层：

```java
package com.gao.spring.dao;

import org.springframework.stereotype.Repository;

// 名字默认是类名首字母小写bookDao
@Repository
public class BookDao {
    private String label = "1";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "BookDao{" +
                "label='" + label + '\'' +
                '}';
    }
}
```

注册一个bookDao组件进入ioc容器。

修改MainConfigOfAutowired

```java
package com.gao.spring.config;

import com.gao.spring.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配;
 *  Spring利用依赖注入(DI) 完成对IOC容器中各个组件的依赖关系赋值
 *
 *  1) @Autowired: 自动注入
 *      1) 默认有限按照类型去容器中找对应的组件 applicationContext.getBean(BookDao.class);  找到就赋值
 *      2) 如果友多个同一个类型的的Dao 如果找到多个相同类型的组件 再将属性的名称作为组件id去容器中查找
 *      3) @Qualifier("bookDao") 来指定需要装配的组件的id 而不是使用默认的属性名
 *      BookService {
 *          @Autowired
 *          BookDao bookDao
 *      }
 */
@Configuration
@ComponentScan({"com.gao.spring.service","com.gao.spring.dao", "com.gao.spring.controller"})
public class MainConfigOfAutowired {

    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

}

```



输出：

```java
Nov 16, 2021 11:09:35 PM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Tue Nov 16 23:09:35 CST 2021]; root of context hierarchy
BookService{bookDao=BookDao{label='2'}}
```



如果容器中没有BookDao 

1、将BookDao.class的@Repository 注释掉

2、将MainConfigOfAutowired.class配置类当中的@Bean("bookDao2")注释掉 

则会报错 创建bean对象会报错。期望有个bean可以被找到 但是没有找到这个bean

我们修改BookService

将@Autowired(required = false) 加上false

```java
@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
    @Qualifier("bookDao2")
    @Autowired(required = false)
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
```

则自动装配说明不是必须要自动装配到这个bean对象。则默认是null

```java
public class IOCTest_Autowired {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);

//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);

        applicationContext.close();
    }
}
```

输出：

```
BookService{bookDao=null}
```

可以装上自动装 装不上就拉到。



#### @Primary

@Primary 标注以后 自动装配的时候首选这个bean对象 此时@Qualifier要去掉

```java
@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
//    @Qualifier("bookDao")
    @Autowired(required = false)
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}

```



```java
@Configuration
@ComponentScan({"com.gao.spring.service","com.gao.spring.dao", "com.gao.spring.controller"})
public class MainConfigOfAutowired {

    // @Primary 标注以后 自动装配的时候首选这个bean对象 此时@Qualifier要去掉
    @Primary
    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

}
```



如果我们此时将@Qualifier 加上则明确指定来一个对象 那么此时就会按照@Qualifier指定的对象去使用具体的bean对象



### (2) @Resource @Inject

#### @Resource

```
@Resource:
 *          可以和@Autowired 一样可以实现自动装配功能 默认是按照组件名称进行装配的。
 *          没有能支持@Primary功能，没有支持@Autowired(required=false)
```



这两个是java的规范。是java的注解 不是spring的注解。spring支持java规范的注解。

例如

```java
package com.gao.spring.service;

import com.gao.spring.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
//    @Qualifier("bookDao")
//    @Autowired(required = false)
    @Resource
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
```

使用@Resource注解进行bean注入

```java
@Configuration
@ComponentScan({"com.gao.spring.service","com.gao.spring.dao", "com.gao.spring.controller"})
public class MainConfigOfAutowired {

    // @Primary 标注以后 自动装配的时候首选这个bean对象 此时@Qualifier要去掉
    @Primary
    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

}
```

我们发现最后

没有使用@Primary的注解的bean对象

而是使用label为1的对象。默认是按照属性名称进行装配 也可以使用name来指定id

```java
@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
//    @Qualifier("bookDao")
//    @Autowired(required = false)
//    @Resource  //默认使用 属性名称进行装配
    @Resource(name = "bookDao2")
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
```



#### @Inject

```
@Inject:
 *          需要导入javax.inject的包 和Autowired的功能一样 支持@Primary
 *          但是Inject没有属性可以设置
```



需要导入pom依赖

```
		<dependency>
            <groupId>javax.inject</groupId>
            <artifactId>javax.inject</artifactId>
            <version>1</version>
        </dependency>
```

就可以直接

```java
@Service
public class BookService {

    // @Qualifier("bookDao2") 指定默认的组件id
//    @Qualifier("bookDao")
//    @Autowired(required = false)
//    @Resource  //默认使用 属性名称进行装配
//    @Resource(name = "bookDao2")
    @Inject
    private BookDao bookDao;

    public void print() {
        System.out.println(bookDao);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
```

```java
@Configuration
@ComponentScan({"com.gao.spring.service","com.gao.spring.dao", "com.gao.spring.controller"})
public class MainConfigOfAutowired {

    // @Primary 标注以后 自动装配的时候首选这个bean对象 此时@Qualifier要去掉
    @Primary
    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

}
```

```java
public class IOCTest_Autowired {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);

//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);

        applicationContext.close();
    }
}
```

输出：

```
Nov 16, 2021 11:41:58 PM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Tue Nov 16 23:41:58 CST 2021]; root of context hierarchy
Nov 16, 2021 11:41:58 PM org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor <init>
INFO: JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
BookService{bookDao=BookDao{label='2'}}
Nov 16, 2021 11:41:58 PM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Tue Nov 16 23:41:58 CST 2021]; root of context hierarchy

```

得到输出为使用2

故Inject支持@Primary



### (3)@Autowired:构造器\参数\方法\属性 都可以标注这个注解

```
@Autowired:构造器\参数\方法\属性 都可以标注这个注解
 *      1) 标注在方法位置 则调用的时候会 Spring容器创建当前对象 就会调用这个方法完成赋值 这个方法用的参数Car car 自定义的值将会从ioc容器中获取
 *      2）标注在构造器上
 *      3）标注在参数位置
```

如果只有一个有参构造器则可以不用注释 直接调用唯一的bean注入。

```java
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

```

```java
package com.gao.spring.bean;

import org.springframework.stereotype.Component;

@Component
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

```java
public class IOCTest_Autowired {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);

//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);

        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);
        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);

        applicationContext.close();
    }
}
```

输出：

```
postProcessAfterInitialization ....dog=>com.gao.spring.bean.Dog@327b636c
BookService{bookDao=BookDao{label='2'}}
Boss{car=com.gao.spring.bean.Car@5ad851c9}
com.gao.spring.bean.Car@5ad851c9
Nov 17, 2021 12:17:21 AM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@136432db: startup date [Wed Nov 17 00:17:20 CST 2021]; root of context hierarchy
Dog ... @PreDestroy
cat ... destroy ... 
```





我们再来测试一个类

```java
package com.gao.spring.bean;

public class Color {
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Color{" +
                "car=" + car +
                '}';
    }
}

```

什么注解都不标，在配置文件中声明：

```java
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
 *      1) [标注在方法位置]：则调用的时候会 Spring容器创建当前对象 就会调用这个方法完成赋值 这个方法用的参数Car car 自定义的值将会从ioc容器中获取
 *      2）[标注在构造器上]：如果组件只有一个有参构造器 这个有参构造器参数的Autowired可以省略 参数位置组件可以自动从容器中获取
 *      3）标注在参数位置：
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
```



```java
public class IOCTest_Autowired {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);

//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);

        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);
        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);

        Color color = applicationContext.getBean(Color.class);
        System.out.println(color);

        applicationContext.close();
    }
}
```

输出：

```
Boss{car=com.gao.spring.bean.Car@37e547da}
com.gao.spring.bean.Car@37e547da
Color{car=com.gao.spring.bean.Car@37e547da}
```

都从容器中获取。











### 小结：

```
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
```

推荐使用@Autowired 结合@Primary使用



自动装配原理与这个类有关系：

AutowiredAnnotationBeanPostProcessor



## @Profile注解

```
/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境 动态的激活和切换一系列bean组件的功能
 * 开发环境\测试环境\生产环境
 * 数据源 (/A)(/B)(/C)
 * @Profile：指定组件在哪个环境的情况下才能被注册到容器当中，当不指定的情况下 任何的环境都能注册到这个组件的容器当中
 *
 * 1）加了环境标识的bean 只有这个环境被激活的时候才能被注册到容器中 默认是default 如果标识为@Profile("default") 则默认加载这一个
 * 2）写在配置类上，只有在指定的环境上的时候，整个配置类里面的所有配置才会生效
 * 3) 没有标志环境的bean在任何环境都是加载的
 */
```



三种环境 测试 开发 线上环境的转换

配置文件：dbconfig.properties

```xml
db.user=root
db.password=123456
db.driverClass=com.mysql.jdbc.Driver
```

配置类

```java
package com.gao.spring.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;

/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境 动态的激活和切换一系列bean组件的功能
 * 开发环境\测试环境\生产环境
 * 数据源 (/A)(/B)(/C)
 * @Profile
 */
@PropertySource("classpath:/dbconfig.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver valueResolver;

    private String driverClass;

    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Bean("devDataSource")
    public DataSource dataSourceTestDev(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ssm_crud");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Bean("prodDataSource")
    public DataSource dataSourceProd(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/scw_0515");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.valueResolver = resolver;
        this.driverClass = valueResolver.resolveStringValue("${db.driverClass}");
    }
}

```

测试类获取bean

```java
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
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for(String str : beanNamesForType) {
            System.out.println(str);
        }
        applicationContext.close();
    }
}

```



输出得到三个配置

```
testDataSource
devDataSource
prodDataSource
```



如何激活不同的环境的配置。

则使用@profile

```
@Profile：指定组件在哪个环境的情况下才能被注册到容器当中，当不指定的情况下 任何的环境都能注册到这个组件的容器当中

如果标志为@Profile("default")表示默认加载这一个 其他name可以随意
```

加上这个注释在各个bean上面

```java
package com.gao.spring.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;

/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境 动态的激活和切换一系列bean组件的功能
 * 开发环境\测试环境\生产环境
 * 数据源 (/A)(/B)(/C)
 * @Profile：指定组件在哪个环境的情况下才能被注册到容器当中，当不指定的情况下 任何的环境都能注册到这个组件的容器当中
 */
@PropertySource("classpath:/dbconfig.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver valueResolver;

    private String driverClass;

    @Profile("test")
    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Profile("dev")
    @Bean("devDataSource")
    public DataSource dataSourceTestDev(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ssm_crud");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Profile("prod")
    @Bean("prodDataSource")
    public DataSource dataSourceProd(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/scw_0515");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.valueResolver = resolver;
        this.driverClass = valueResolver.resolveStringValue("${db.driverClass}");
    }
}

```

标识当前各个bean在具体的环境下才能被注册到ioc容器当中。

测试类

```java
public class IOCTest_Profile {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for(String str : beanNamesForType) {
            System.out.println(str);
        }
        applicationContext.close();
    }
}

```

此时输出为空。因为加了环境标识 只有具体环境激活才能被注册到ioc容器当中。



如果标志为@Profile("default")表示默认加载这一个



如何设置运行环境为具体某个环境？

```
1. 使用命令行动态参数：在虚拟机参数位置加载 -Dspring.profiles.active=test
2. 使用代码的方式激活某种环境
```

```java
public class IOCTest_Profile {

    //1. 使用命令行动态参数：在虚拟机参数位置加载 -Dspring.profiles.active=test
    //2. 使用代码的方式激活某种环境：
    @Test
    public void test01() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //1 创建一个applicationContext对象
        //2 设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");
        //3 注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        //4 启动刷新器
        applicationContext.refresh();

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for(String str : beanNamesForType) {
            System.out.println(str);
        }
        applicationContext.close();
    }
}

```

输出：

```
devDataSource
```



除了写在bean上面 还可以写在类的上面

```java
package com.gao.spring.config;

import com.gao.spring.bean.Yellow;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;

/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境 动态的激活和切换一系列bean组件的功能
 * 开发环境\测试环境\生产环境
 * 数据源 (/A)(/B)(/C)
 * @Profile：指定组件在哪个环境的情况下才能被注册到容器当中，当不指定的情况下 任何的环境都能注册到这个组件的容器当中
 *
 * 1）加了环境标识的bean 只有这个环境被激活的时候才能被注册到容器中 默认是default 如果标识为@Profile("default") 则默认加载这一个
 */
@Profile("test")
@PropertySource("classpath:/dbconfig.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver valueResolver;

    private String driverClass;

   
    @Bean
    public Yellow yellow() {
        return new Yellow();
    }

    @Profile("test")
    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Profile("dev")
    @Bean("devDataSource")
    public DataSource dataSourceTestDev(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ssm_crud");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Profile("prod")
    @Bean("prodDataSource")
    public DataSource dataSourceProd(@Value("${db.password}") String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/scw_0515");
        dataSource.setDriverClass(driverClass);
        return null;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.valueResolver = resolver;
        this.driverClass = valueResolver.resolveStringValue("${db.driverClass}");
    }
}

```



```java
public class IOCTest_Profile {

    //1. 使用命令行动态参数：在虚拟机参数位置加载 -Dspring.profiles.active=test
    //2. 使用代码的方式激活某种环境
    @Test
    public void test01() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //1 创建一个applicationContext对象
        //2 设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");
        //3 注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        //4 启动刷新器
        applicationContext.refresh();

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for(String str : beanNamesForType) {
            System.out.println(str);
        }
        applicationContext.close();
    }
}

```

输出为空

因为只有test环境才会生效



修改为test

输出：

```
testDataSource
```

