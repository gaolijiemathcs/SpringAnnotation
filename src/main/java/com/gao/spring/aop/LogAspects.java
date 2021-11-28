package com.gao.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * 切面类
 * @Aspect：告诉Spring当前类是一个切面类 而不是其他普通的类
 */
@Aspect
public class LogAspects {

    // 抽取公共切入点表达式execution(切入点表达式)
    // 1.本类引用(直接使用奔雷的方法 @After("pointCut()")即可
    // 2.其他的切面类中的切面引用要用全类名称 @After("com.gao.spring.aop.LogAspect.pointCut()")
    @Pointcut("execution(public int com.gao.spring.aop.MathCalculator.*(..))")
    public void pointCut() {}

    //@Before 在目标方法之前切入： 切入点表达式(指定在哪个方法切入)
    //@Before("public int com.gao.spring.aop.MathCalculator.*(..)") //任意方法 任意参数
    //想要获取参数 可以使用JoinPoint JoinPoint只能写在第一位的参数位置
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        System.out.println("" + joinPoint.getSignature().getName() +
                ": 除法运行...@Before参数列表是：{"+ Arrays.asList(args)+"}");
    }

    @After("com.gao.spring.aop.LogAspects.pointCut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println("" + joinPoint.getSignature().getName() + "除法运行结束...@After");
    }

    // 增加一个returning = "result" 增加一个参数Object result, result用于获取最后的返回值 在注解里面说明以后
    // JoinPoint 一定出现在参数标的第一位
    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturen(JoinPoint joinPoint, Object result) {
        System.out.println("" + joinPoint.getSignature().getName() + "除法正常返回...@AfterReturning运行结果是:{"+result+"}");
    }

    // 申明函数参数Exception exception 在注解参数列表当中设置一个Exception exception 声明这个是我们要保存
    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.println(""+joinPoint.getSignature().getName()+"logException(): 除法异常...异常信息:{"+exception+"}");
    }

}
