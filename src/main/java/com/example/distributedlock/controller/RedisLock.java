package com.example.distributedlock.controller;

import com.example.distributedlock.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisLock {
    @Autowired
    private RedisLockService redisLockService;

    @RequestMapping("/hello")
    public String bookAppointment() throws Exception {
        try {
            while (true) {
                if(redisLockService.lock("aaaa")) {
                    redisLockService.updateDb();
                    return ("get aaaa");
                } else {
                    System.out.println("can not get aaa");
                    Thread.sleep(100);
                }

            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisLockService.unLock("aaaa");
        }
    }

}
