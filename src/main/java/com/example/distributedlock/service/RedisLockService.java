package com.example.distributedlock.service;

import com.example.distributedlock.po.LockTestPo;
import com.example.distributedlock.repository.LockTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    LockTestRepository lockTestRepository;

    public boolean lock(String key) {
        int defaultExpire = 5000;
        return lock(key, defaultExpire);
    }

    /**
     * 加锁
     * @param key redis key
     * @param expire 过期时间，单位秒
     * @return true:加锁成功，false，加锁失败
     */
    public boolean lock(String key, int expire) {

        long value = System.currentTimeMillis() + expire;
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        long curVal;
        Object valueO = redisTemplate.opsForValue().get(key);
        if(null == valueO) {
            return false;
        } else {
            curVal = (long) valueO;
        }
        //获取key的值，判断是是否超时
        if (curVal < System.currentTimeMillis()) {
            //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
            //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
            long oldVal = (long) redisTemplate.opsForValue().getAndSet(key, value);
            if (oldVal == curVal) {
                return true;
            }
        }
        return false;

    }

    public void unLock(String key) {
        if(null != redisTemplate.opsForValue().get(key)) {
            long oldExpireTime = (long) redisTemplate.opsForValue().get(key);
            if (oldExpireTime > System.currentTimeMillis()) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }
    }

    public void updateDb() throws Exception {

        LockTestPo lockTestPo = lockTestRepository.getOne(1L);
        if(lockTestPo.getBooked()<lockTestPo.getTotal()) {
            lockTestPo.setBooked(lockTestPo.getBooked()+1);
            lockTestRepository.save(lockTestPo);
        }
    }
}
