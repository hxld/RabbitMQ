package com.atguigu.rabbitmq.utils;

/**
 * @author hxld
 * @create 2022-08-23 22:42
 */
public class SleepUtils {

    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
