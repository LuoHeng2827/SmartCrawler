package com.luoheng.crawler.test;

import java.util.UUID;

/**
 * @description:
 * @author:
 * @create: 2019-09-23 14:23
 **/
public class Test {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        long currentMills = System.currentTimeMillis();
        System.out.println(uuid.toString());
        System.out.print(currentMills);
    }
}
