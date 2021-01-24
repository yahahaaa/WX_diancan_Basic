package com.atzjhydx.weixindiancan.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis实现分布式锁，可以在多台机器，多个线程中对一个数据进行互斥（实现是基于redis是单线程处理命令的，不会出现两条命令同时被执行）
 * synchronized无法实现分布式上锁
 * Created by 廖师兄
 * 2017-08-07 23:55
 */
@Component
@Slf4j
public class  RedisLock {
    //超时时间设置为10秒
    private static final int TIMEOUT = 10 * 1000;   //超时时间

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key, String value) {
        if(redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        //当某个线程出现了问题，在设定的超时时间内未能解锁，则使用以下步骤实现
        //currentValue=A   这两个线程的value都是B  其中一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            //由于redis是单线程操作，所以下面这句代码一次只能有一个线程执行，谁先改了谁就能拿到锁
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }

}
