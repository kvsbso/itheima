package com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:applicationContext-jobs.xml")
public class MyTest {


    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:applicationContext-jobs.xml");
    }

}
