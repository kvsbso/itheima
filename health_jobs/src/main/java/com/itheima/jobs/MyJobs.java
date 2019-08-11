package com.itheima.jobs;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 入门案例 - 每隔10秒钟打印helloword
 */
public class MyJobs {

    /**
     * 任务方法
     */
    public void run(){
        System.out.println(new Date()+"**********************hello world");
    }
}
