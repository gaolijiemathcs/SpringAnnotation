package com.gao.spring.config;

import com.gao.spring.aop.LogAspects;
import com.gao.spring.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP：指程序在运行期间能够动态的将某段代码切入到指定方法指定位置进行运行的编程方式
 *
 * 1. 导入aop模块： Spring AOP (spring-aspect)
 * 2. 业务逻辑类（定义一个业务逻辑类 MathCalculator）
 *    需求:在业务逻辑运行的时候 将日志进行打印(方法之前，方法运行结束，方法出现异常,...)
 * 3. 定义一个日志切面类(LogAspects): 切面里面的方法需要动态感知MathCalculator中div运行到哪里然后执行
 *      通知方法：
 *          前置通知(@Before): logStart 在目标方法(div)运行之前运行
 *          后置通知(@After): logEnd 在目标方法(div)运行结束之后运行 (无论方法正常结束还是异常结束都将调用此方法)
 *          返回通知(@AfterReturning): logReturn 在目标方法(div)正常返回之后运行
 *          异常通知(@AfterThrowing): logException 在目标方法(div)运行异常以后运行
 *          环绕通知(@Around): 动态代理 手动推进目标方法运行(joinPoint.procced())
 * 4. 给切面类的目标方法标注何时何地运行(通知注解)
 * 5. 将切面类和业务逻辑类(目标方法所在类)都加入到容器中
 * 6. 必须告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
 * [7].需要给配置类中加入@EnableAspectJAutoProxy [代表开启基于注解的aop模式]
 *       在Spring中会有很多 @EnableXXX; 表示开启某一中配置
 *
 *  主要把握三步：
 *      1）将业务逻辑组件和切面类都加入到容器当中，告诉Spring 哪个是切面类(@Aspectc
 *      2) 在切面类上的每一个通知方法都标注通知注解 告诉Spring何时何地运行(切入点表达式)
 *      3）开启基于注解的Aop模式(注解方式是@EnableAspectJAutoProxy  原始方式是在xml文件中写清楚aop切面的关系)
 *
 * AOP原理: [看给容器中注册了什么组件? 这个组件是什么时候工作? 这个组件的功能是什么?]
 *  从@EnableAspectJAutoProxy研究
 *  1. @EnableAspectJAutoProxy是什么?
 *      @Import(AspectJAutoProxyRegistrar.class):给容器中导入AspectJAutoProxyRegistrar
 *          利用AspectJAutoProxyRegistrar自定义给容器中注册bean
 *          internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator
 *
 *      给容器中注册一个AnnotationAwareAspectJAutoProxyCreator 自动代理创建器
 *
 *  2. AnnotationAwareAspectJAutoProxyCreator
 *      AnnotationAwareAspectJAutoProxyCreator
 *          -> 父类AspectJAwareAdvisorAutoProxyCreator
 *           -> 父类的父类AbstractAdvisorAutoProxyCreator
 *              -> public abstract class AbstractAutoProxyCreator extends ProxyProcessorSupport
 * 		                implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 * 		                    关注(1)后置处理器(在bean初始化完成前后做事情), (2)并且自动装配BeanFactory
 *
 *  AbstractAutoProxyCreator.setBeanFatory()
 *  AbstractAutoProxyCreator 有后置处理器的逻辑:
 *
 *  AbstractAdvisorAutoProxyCreator.setBeanFactory() -> 调用 initBeanFactory()
 *
 *  AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 *
 *  流程:
 *      1) 传入配置类 创建ioc容器
 *      2) 注册配置类,调用refresh() 刷新容器
 *      3) 注册后置处理器创建过程==>:registerBeanPostProcessors(beanFactory); 注册bean的后置处理器来方便拦截bean的创建
 *          1) 先获取ioc容器已经定义类的需要创建对象的所有BeanPostProcessor
 *          2) 给容器中加别的PostProcessor
 *          3) 优先注册实现了PriorityOrdered接口的BeanPostProcessor
 *          4) 再给容器中注册实现类Ordered接口的BeanPostProcessor
 *          5) 最后再给没有实现优先级接口的BeanPostProcessor
 *          6) 注册BeanPostProcessor, 实际上将是创建BeanPostProcessor对象 保存在容器当中
 *              创建internalAutoProxyCreator的BeanPostProcessor[AnnotationAwareAspectJAutoProxyCreator]
 *              1)创建Bean的实例
 *              2)populateBean: 给bean的各种属性赋值
 *              3)initializeBean: 初始化bean
 *                  1) invokeAwareMethods():处理Aware接口的方法回调
 *                  2) applyBeanPostProcessorsBeforeInitialization(): 应用后置处理器的postProcessBeforeInitialization
 *                  3) invokeInitMethods(): 执行自定义的初始化方法 @Bean注解定义初始化方法 销毁方法
 *                  4) applyBeanPostProcessorsAfterInitialization(): 执行后置处理器的postProcessAfterInitialization
 *              4)BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)创建成功 --> aspectJAdvisorsBuilder
 *           7) 把BeanPostProcessor注册到BeanFactory中
 *              beanFactory.addBeanPostProcessor(postProcessor);
 *     ======以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程=========
 *              AnnotationAwareAspectJAutoProxyCreator => InstantiationAwareBeanPostProcessor
 *     4) fininshBeanFactoryInitialization(beanFactory)：完成BeanFactory初始化工作，创建剩下的单实例Bean对象
 *          1）遍历获取容器中的所有Bean 依次创建对象getBean(beanName)
 *              getBean -> doGetBean() -> getSingleton() ->
 *          2) 创建bean
 *              [AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，InstantiationAwareBeanPostProcessor 会调用]
 *              1）先从缓存中获取当前bean 如果能够获取到 说明bean是之前创建过的；直接使用，否则再创建；
 *                  保证类单实例的bean 创建好的bean都会被缓存起来。
 *              2）createBean() 创建bean：
 *              AnnotationAwareAspectJAutoProxyCreator会在任何bean创建之前先尝试返回bean的实例
 *                  「BeanPostProcessor是Bean对象创建完成初始化之前后调用的」
 *                  「InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试使用后置处理器返回对象的」
 *                  1) resolveBeforeInstantiation(beanNaem, mbdToUse) 解析beforeInstantiationResolved
 *                      希望后置处理器 在此可以返回一个代理对象；如果能够返回代理对象就使用 如果不能就继续
 *                      1）后置处理器会先尝试返回对象
 *                          bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 *                              拿到所有的后置处理器 如果是InstantiationAwareBeanPostProcessor将
 *                              执行后置处理器的postProcessBeforeInstantiation
 *                          if (bean != null) {
 * 						        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *                          }
 *                  2）doCreateBean(beanName, mbdToUse, args); 真正的创建一个bean实例；和3.6流程一样。
 *                  3）
 *
 *
 *  AbstractAutoProxyCreator postProcessBeforeInstantiation
 *  1）每一个bean创建之前调用postProcessBeforeInstantiation
 *      关心MathCalculator 和 LogAspect创建
 *      1）判断当前bean是否存在advisedBean中国（保存所有需要增强bean）
 *      2）isInfrastructureClass判断当前bean是否是基础类型的bean Advice Pointcut Advisor AopInfrastructureBean
 *      或者是否是切面dvisor instanceof AspectJPointcutAdvisor
 *      3）是否需要跳过
 *          1）获取候选的增强器（切面里面的通知方法）[List<Advisor> candidateAdvisors = findCandidateAdvisors();]
 *              每一个封装的通知方法增强器是AspectJPointcutAdvisor
 *              判断每一个增强器是AspectJPointcutAdive类型的，返回true
 *          2）永远返回false
 *  2）创建对象
 *  postProcessAfterInitialization
 *      return wrapIfNecessary(bean, beanName, cacheKey);
 *      1）获取当前bean的所有增强器（通知方法） specificInterceptors
 *          1. 找到能够在当前bean使用的增强器，找到哪些通知方法需要触发啊切入当前bean方法的。
 *          2. 获取到能在bean使用的增强器
 *          3. 给增强器排序
 *      2）保存当前bean在adviceBeans中
 *      3）如果当前bean需要增强创建当前bean的代理对象
 *          1. 获取所有增强器(通知方法)
 *          2. 保存到proxyFactoru
 *          3. 创建代理对象：Spring自动决定
 *              JdkDynamicAopProxy(confg); jdk动态代理
 *              ObjenesisCglibAopProxy(config); cglib的动态代理
 *      4) 给容器中返回当前组件使用cglib增强了的代理对象
 *      5) 以后容器中获取到的将是这个组件的代理对象，执行目标方法的时候 代理对象就会执行通知方法的流程。
 *
 *  3)目标方法的执行
 *      容器中保存了组件的代理对象(cglib增强后的对象)， 这个对象里面存储了详细信息(比如增强器，目标对象，xxx)。
 *      1）CglibAopProxy.intercept(); 拦截目标方法的执行
 *      2）根据ProxyFactory对象获取将要执行目标方法的拦截器链
 *          List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *          1） List<Object> interceptorList 保存所有的拦截器5
 *              一个默认的ExposeInvocationInterceptor和4个增强器
 *          2）遍历所有的增强器 将其转化为Interceptor
 *              registry.getInterceptors(advisor);
 *          3）将增强器转化为 List<MethodInterceptor>
 *              如果是MethodInterceptor加入集合
 *              如果不是使用AdvisorAdapter将增强器转化为Interceptor
 *              转换完成 返回 MethodInterceptor[]数组
 *                  interceptors.toArray(new MethodInterceptor[interceptors.size()]);
 *
 *      3）如果没有拦截器链 直接执行目标方法
 *          拦截器链（每一个通知方法又被包装为方法拦截器 利用methodInterceptor机制）
 *      4）如果有拦截器链 把需要执行的目标对象、目标方法、拦截器链等信息 闯入 传教一个cglibMethodInvocation对象
 *          并调用Object retVal = mi.targetClass);
 *      5）拦截器链的触发
 *          1）如果没有拦截器执行执行目标方法，或者拦截器的索引和拦截器数组-1大小一样（指定链最后一个拦截器）执行目标方法
 *          2）链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行
 *              拦截器链的机制，保证通知方法与目标方法的执行顺序
 *
 *     总结：
 *         1）@EnableAspectJAutoProxy 开启AOP功能
 *         2）@EnableAspectJAutoProxy 会给容器中注册一个组件AnnotationAwareAspectJAutoProxyCreator
 *         3）AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
 *         4）容器创建流程
 *              1） registerBeanPostProcessors() 注册后置处理器 创建 AnnotationAwareAspectJAutoProxyCreator 对象
 *              2) finishBeanFactoryInitialization() 初始化剩下的单实例bean
 *                  1) 创建业务逻辑组件和切面组件
 *                  2） AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程
 *                  3）组件创建完成以后 判断组件是否需要增强
 *                      是：切面的通知方法包装成增强器（Advisor）给业务逻辑组件创建一个代理对象（cglib）
 *         5) 执行目标方法
 *              1）代理对象执行目标方法
 *              2）CglibAopProxy.intercept()
 *                  1) 得到目标方法的拦截器链(增强器包装成拦截器MethodInterceptor)
 *                  2）利用拦截器的链式机制 依次进入每一个拦截器进行执行
 *                  3）执行效果：
 *                      正常执行：前置通知->目标方法->后置通知->返回通知
 *                      异常执行：前置通知->目标方法->后置通知->异常通知
 *
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {

    // 将业务逻辑类加入容器中
    @Bean
    public MathCalculator calculator() {
        return new MathCalculator();
    }

    // 将切面类也加入到容器当中
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
