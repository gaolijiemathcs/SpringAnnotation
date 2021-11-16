package com.gao.spring;

import com.gao.spring.bean.Boss;
import com.gao.spring.bean.Car;
import com.gao.spring.bean.Color;
import com.gao.spring.bean.Red;
import com.gao.spring.config.MainConfigOfAutowired;
import com.gao.spring.config.MainConfigOfLifeCycle;
import com.gao.spring.dao.BookDao;
import com.gao.spring.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

        System.out.println(applicationContext);
        applicationContext.close();
    }
}
