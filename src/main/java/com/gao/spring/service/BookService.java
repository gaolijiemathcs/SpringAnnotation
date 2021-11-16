package com.gao.spring.service;

import com.gao.spring.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

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
